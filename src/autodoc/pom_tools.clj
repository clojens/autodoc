(ns autodoc.pom-tools
  (:import [java.io File FileInputStream])
  (:require [clojure.zip :as zip]
	    [clojure.data.zip :as zf]
	    [clojure.xml :as xml])
  (:use clojure.data.zip.xml))

(defn get-pom-file
  "Return a java.io.File that represents the pom.xml for this project."
  [root]
  (File. (File. root) "pom.xml"))

(defn- get-pom-xml
  "Get the zipper corresponding to the pom.xml file for this project."
  [root]
  (with-open [inp (FileInputStream. (get-pom-file root))]
    (zip/xml-zip (xml/parse inp))))

(defn get-version
  "Returns the 2-vector [version is-snapshot?] where version is a string representing 
the version (with -SNAPSHOT removed) and is-snapshot? is true if we 
removed that suffix and false otherwise"
  [root]
  (let [full-version (first (:content (first (xml-> (get-pom-xml root) :version zip/node))))]
    (if-let [[_ base-version] (re-matches #"(.*)-SNAPSHOT" full-version)]
      [base-version true]
      [full-version false])))

(defn add-clojure
  "Add a dependency on Clojure if it's not already on the list"
  [deps]
  (if (some #(= "clojure" (-> % first name)) deps)
    deps
    (conj deps ['org.clojure/clojure "1.5.1"])))

(defn get-dependencies
  "If dep-param is :from-pom, returns the dependencies from the pom as a
sequence of 2-vectors in the form ['group/artifact \"version\"].
Otherwise, returns dep-param as is. If the pom is empty, depend on Clojure 1.5"
  [root dep-param]
  (if (= :from-pom dep-param)
    (add-clojure (let [get-tag (fn [loc tag] (first (:content (xml1-> loc tag zip/node))))
                       deps (xml-> (get-pom-xml root) :dependencies :dependency)]
                   (for [d deps :when (not= "true" (get-tag d :optional))]
                     [(symbol (get-tag d :groupId) (get-tag d :artifactId)) (get-tag d :version)])))
    dep-param))

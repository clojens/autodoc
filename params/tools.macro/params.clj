(import (java.io File))

(let [file-prefix (.getAbsolutePath (File. "../autodoc-work-area/tools.macro"))
      root (str file-prefix "/src/")]
  {:name "Tools for Macro Writers",
   :file-prefix file-prefix,
   :root root,
   :source-path "src/main/clojure",
   :web-src-dir "https://github.com/clojure/tools.macro/blob/",

   :web-home "http://clojure.github.com/tools.macro/",
   :output-path (str file-prefix "/autodoc/"),
   :external-doc-tmpdir "/tmp/autodoc/doc",

   :load-jar-dirs [(str root "lib")],

   :namespaces-to-document ["clojure.tools.macro"],
   :trim-prefix nil,
   :build-json-index nil,

   :branches [{:name "master"
               :version :from-pom
               :status "in development"
               :params {:dependencies [['org.clojure/clojure "1.3.0"]]}},
              ]

   :load-except-list [#"/classes/"],
   })


(ns com.infolace.gen-docs.ant-wrapper
  (import
   [java.io File]
   [org.apache.tools.ant Project ProjectHelper DefaultLogger]))

(defn ant-wrapper
  [param-file build-target]
  (let [p (Project.)
        helper (ProjectHelper/getProjectHelper)
        build-file (File. "build.xml")]
    (doto p
      (.addBuildListener (doto (DefaultLogger.)
                           (.setOutputPrintStream System/out)
                           (.setErrorPrintStream System/err)
                           (.setMessageOutputLevel Project/MSG_INFO)))
      (.setUserProperty "ant.file" (.getAbsolutePath build-file))
      (.init)
      (.addReference "ant.projectHelper" helper))
    (.parse helper p build-file)
    (.executeTarget p build-target)))


;;; Sample code 

;;;   File buildFile = new File("build.xml")
;;;   Project p = new Project()                                    ;
;;;   DefaultLogger consoleLogger = new DefaultLogger()            ;
;;;   consoleLogger.setErrorPrintStream(System.err)                ;
;;;   consoleLogger.setOutputPrintStream(System.out)               ;
;;;   consoleLogger.setMessageOutputLevel(Project.MSG_INFO)        ;
;;;   p.addBuildListener(consoleLogger)                            ;
;;;   p.setUserProperty("ant.file", buildFile.getAbsolutePath())   ;
;;;   p.init()                                                     ;
;;;   ProjectHelper helper = ProjectHelper.getProjectHelper()      ;
;;;   p.addReference("ant.projectHelper", helper)                  ;
;;;   helper.parse(p, buildFile)                                   ;
;;;   p.executeTarget("test")
;;;   p.executeTarget(p.getDefaultTarget()) ;

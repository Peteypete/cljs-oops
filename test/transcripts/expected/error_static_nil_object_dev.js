// COMPILER CONFIG:
//   arena/error_static_nil_object.cljs [dev]
//   {:elide-asserts true,
//    :external-config {:oops/config {:static-nil-object :error}},
//    :main oops.arena.error-static-nil-object,
//    :optimizations :whitespace,
//    :output-dir
//    "test/resources/_compiled/error-static-nil-object-dev/_workdir",
//    :output-to
//    "test/resources/_compiled/error-static-nil-object-dev/main.js",
//    :pseudo-names true}
// ----------------------------------------------------------------------------------------------------------
// COMPILER STDERR:
//   THROWN: clojure.lang.ExceptionInfo: failed compiling file:test/src/arena/oops/arena/error_static_nil_object.cljs
//   Caused by: clojure.lang.ExceptionInfo: Oops, Unexpected nil object at line 8 test/src/arena/oops/arena/error_static_nil_object.cljs
// ----------------------------------------------------------------------------------------------------------
// NO GENERATED CODE
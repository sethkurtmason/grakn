java_test(
    name = "benchmark-big-it",
    test_class = "grakn.core.graql.internal.reasoner.BenchmarkBigIT",
    srcs = ["BenchmarkBigIT.java"],
    deps = [
            "//server",
            "//client-java",
            "//grakn-graql",
            "//test-integration/rule:concurrent-grakn-server",
            "//dependencies/maven/artifacts/org/hamcrest:hamcrest-library",
            "//dependencies/maven/artifacts/org/slf4j:slf4j-api",
            "//dependencies/maven/artifacts/com/google/guava:guava",
        ],
    size = "large",
    data = [
           "//test-integration/test/graql/reasoner/resources:multi-join",
           "//test-integration/test/graql/reasoner/resources:linear-transivity"
           ],
    classpath_resources = ["//test-integration/resources:logback-test"]
)

java_test(
    name = "benchmark-small-it",
    test_class = "grakn.core.graql.internal.reasoner.BenchmarkSmallIT",
    srcs = ["BenchmarkSmallIT.java"],
    deps = [
        "//server",
        "//client-java",
        "//grakn-graql",
        "//test-integration/rule:concurrent-grakn-server",
        "//dependencies/maven/artifacts/com/google/guava:guava",
        "//test-integration/test/graql/reasoner/graph:linear-transitivity-matrix-graph",
        "//test-integration/test/graql/reasoner/graph:transitivity-chain-graph",
        "//test-integration/test/graql/reasoner/graph:diagonal-graph",
        "//test-integration/test/graql/reasoner/graph:path-tree-graph",
        "//test-integration/test/graql/reasoner/graph:transitivity-matrix-graph",
    ],
    size = "large",
    resources = [
        "//test-integration/test/graql/reasoner/resources:generic-schema",
        "//test-integration/test/graql/reasoner/resources:unification-with-types"
        ],
    classpath_resources = ["//test-integration/resources:logback-test"]
)
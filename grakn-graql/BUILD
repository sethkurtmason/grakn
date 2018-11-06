#
# GRAKN.AI - THE KNOWLEDGE GRAPH
# Copyright (C) 2018 Grakn Labs Ltd
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

package(default_visibility = ["//visibility:public"])

load("@rules_antlr//antlr:antlr4.bzl", "antlr4")

antlr4(
    name = "graql-java",
    srcs = glob(["src/graql/grammar/*.g4"]),
    visitor = True,
    package = "ai.grakn.graql.grammar"
)

genrule(
    name = "grakn-version",
    srcs = [
        "templates/util/GraknVersion.java",
        "//:VERSION",
    ],
    cmd = "VERSION=`cat $(location //:VERSION)`;sed -e \"s/VERSION_PLACEHOLDER/$$VERSION/g\" $(location templates/util/GraknVersion.java) >> $@",
    outs = ["util/GraknVersion.java"]
)

java_library(
    name = "grakn-graql",
    srcs = [
        ":grakn-version",
        ":graql-java",
    ] + glob(["src/**/*.java"]),
    deps = [
        # External dependencies
        "//dependencies/maven/artifacts/ch/qos/logback:logback-classic",
        "//dependencies/maven/artifacts/ch/qos/logback:logback-core",
        "//dependencies/maven/artifacts/com/boundary:high-scale-lib",
        "//dependencies/maven/artifacts/com/datastax/cassandra:cassandra-driver-core", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/com/fasterxml/jackson/core:jackson-annotations",
        "//dependencies/maven/artifacts/com/fasterxml/jackson/core:jackson-core",
        "//dependencies/maven/artifacts/com/fasterxml/jackson/core:jackson-databind",
        "//dependencies/maven/artifacts/com/fasterxml/jackson/module:jackson-module-scala-2-11",
        "//dependencies/maven/artifacts/com/google/auto/value:auto-value",
        "//dependencies/maven/artifacts/com/google/code/findbugs:annotations",
        "//dependencies/maven/artifacts/com/google/guava:guava",
        "//dependencies/maven/artifacts/commons-configuration:commons-configuration", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/commons-lang:commons-lang",
        "//dependencies/maven/artifacts/io/netty:netty-tcnative-boringssl-static",
        "//dependencies/maven/artifacts/io/reactivex:rxjava",
        "//dependencies/maven/artifacts/io/zipkin/brave:brave",
        "//dependencies/maven/artifacts/org/antlr:antlr4-runtime", # sync version with @antlr4_runtime//jar
        "//dependencies/maven/artifacts/org/apache/cassandra:cassandra-all",
        "//dependencies/maven/artifacts/org/apache/cassandra:cassandra-thrift", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/commons:commons-lang3", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/hadoop:hadoop-annotations", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/hadoop:hadoop-common", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/hadoop:hadoop-mapreduce-client-core", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/httpcomponents:httpclient",
        "//dependencies/maven/artifacts/org/apache/httpcomponents:httpcore", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/spark:spark-core-2-11",
        "//dependencies/maven/artifacts/org/apache/spark:spark-launcher-2-11", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/thrift:libthrift", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/apache/tinkerpop:gremlin-core",
        "//dependencies/maven/artifacts/org/apache/tinkerpop:hadoop-gremlin",
        "//dependencies/maven/artifacts/org/apache/tinkerpop:spark-gremlin",
        "//dependencies/maven/artifacts/org/apache/tinkerpop:tinkergraph-gremlin",
        "//dependencies/maven/artifacts/org/janusgraph:janusgraph-cassandra",
        "//dependencies/maven/artifacts/org/janusgraph:janusgraph-core",
        "//dependencies/maven/artifacts/org/janusgraph:janusgraph-hadoop",
        "//dependencies/maven/artifacts/org/scala-lang:scala-library", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/scala-lang:scalap", # PREVIOUSLY UNDECLARED
        "//dependencies/maven/artifacts/org/sharegov:mjson",
        "//dependencies/maven/artifacts/org/slf4j:log4j-over-slf4j",
        "//dependencies/maven/artifacts/org/slf4j:slf4j-api",
    ],
    resources = glob(["src/resources/*"]),
    javacopts = ["-XepDisableAllChecks"], # TODO: THIS NEEDS TO BE REMOVED
)
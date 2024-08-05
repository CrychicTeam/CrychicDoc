package me.lucko.spark.lib.asm;

final class Edge {

    static final int JUMP = 0;

    static final int EXCEPTION = Integer.MAX_VALUE;

    final int info;

    final Label successor;

    Edge nextEdge;

    Edge(int info, Label successor, Edge nextEdge) {
        this.info = info;
        this.successor = successor;
        this.nextEdge = nextEdge;
    }
}
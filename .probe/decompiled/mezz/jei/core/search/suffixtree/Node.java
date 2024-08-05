package mezz.jei.core.search.suffixtree;

import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMaps;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import mezz.jei.core.util.SubString;
import org.jetbrains.annotations.Nullable;

class Node<T> {

    private Collection<T> data;

    private Char2ObjectMap<Edge<T>> edges = Char2ObjectMaps.emptyMap();

    @Nullable
    private Node<T> suffix;

    Node() {
        this.data = List.of();
        this.suffix = null;
    }

    public void getData(Consumer<Collection<T>> resultsConsumer) {
        if (!this.data.isEmpty()) {
            resultsConsumer.accept(Collections.unmodifiableCollection(this.data));
        }
        ObjectIterator var2 = this.edges.values().iterator();
        while (var2.hasNext()) {
            Edge<T> edge = (Edge<T>) var2.next();
            Node<T> dest = edge.getDest();
            dest.getData(resultsConsumer);
        }
    }

    void addRef(T value) {
        if (!this.contains(value)) {
            this.addValue(value);
            for (Node<T> iter = this.suffix; iter != null && !iter.contains(value); iter = iter.suffix) {
                iter.addValue(value);
            }
        }
    }

    protected boolean contains(T value) {
        return this.data.contains(value);
    }

    void addEdge(Edge<T> edge) {
        char firstChar = edge.charAt(0);
        switch(this.edges.size()) {
            case 0:
                this.edges = Char2ObjectMaps.singleton(firstChar, edge);
                break;
            case 1:
                Char2ObjectMap<Edge<T>> newEdges = new Char2ObjectOpenHashMap(this.edges);
                newEdges.put(firstChar, edge);
                this.edges = newEdges;
                break;
            default:
                this.edges.put(firstChar, edge);
        }
    }

    @Nullable
    Edge<T> getEdge(char ch) {
        return (Edge<T>) this.edges.get(ch);
    }

    @Nullable
    Edge<T> getEdge(SubString string) {
        if (string.isEmpty()) {
            return null;
        } else {
            char ch = string.charAt(0);
            return (Edge<T>) this.edges.get(ch);
        }
    }

    @Nullable
    Node<T> getSuffix() {
        return this.suffix;
    }

    void setSuffix(Node<T> suffix) {
        this.suffix = suffix;
    }

    protected void addValue(T value) {
        switch(this.data.size()) {
            case 0:
                this.data = List.of(value);
                break;
            case 1:
                this.data = List.of(this.data.iterator().next(), value);
                break;
            case 2:
                {
                    List<T> newData = new ArrayList(4);
                    newData.addAll(this.data);
                    newData.add(value);
                    this.data = newData;
                    break;
                }
            case 16:
                {
                    Collection<T> newData = Collections.newSetFromMap(new IdentityHashMap());
                    newData.addAll(this.data);
                    newData.add(value);
                    this.data = newData;
                    break;
                }
            default:
                this.data.add(value);
        }
    }

    public String toString() {
        return "Node: size:" + this.data.size() + " Edges: " + this.edges;
    }

    public IntSummaryStatistics nodeSizeStats() {
        return this.nodeSizes().summaryStatistics();
    }

    private IntStream nodeSizes() {
        return IntStream.concat(IntStream.of(this.data.size()), this.edges.values().stream().flatMapToInt(e -> e.getDest().nodeSizes()));
    }

    public String nodeEdgeStats() {
        IntSummaryStatistics edgeCounts = this.nodeEdgeCounts().summaryStatistics();
        IntSummaryStatistics edgeLengths = this.nodeEdgeLengths().summaryStatistics();
        return "Edge counts: " + edgeCounts + "\nEdge lengths: " + edgeLengths;
    }

    private IntStream nodeEdgeCounts() {
        return IntStream.concat(IntStream.of(this.edges.size()), this.edges.values().stream().map(Edge::getDest).flatMapToInt(Node::nodeEdgeCounts));
    }

    private IntStream nodeEdgeLengths() {
        return IntStream.concat(this.edges.values().stream().mapToInt(SubString::length), this.edges.values().stream().map(Edge::getDest).flatMapToInt(Node::nodeEdgeLengths));
    }

    public void printTree(PrintWriter out, boolean includeSuffixLinks) {
        out.println("digraph {");
        out.println("\trankdir = LR;");
        out.println("\tordering = out;");
        out.println("\tedge [arrowsize=0.4,fontsize=10]");
        out.println("\t" + nodeId(this) + " [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.1,height=.1];");
        out.println("//------leaves------");
        this.printLeaves(out);
        out.println("//------internal nodes------");
        this.printInternalNodes(this, out);
        out.println("//------edges------");
        this.printEdges(out);
        if (includeSuffixLinks) {
            out.println("//------suffix links------");
            this.printSLinks(out);
        }
        out.println("}");
    }

    private void printLeaves(PrintWriter out) {
        if (this.edges.size() == 0) {
            out.println("\t" + nodeId(this) + " [label=\"" + this.data + "\",shape=point,style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");
        } else {
            ObjectIterator var2 = this.edges.values().iterator();
            while (var2.hasNext()) {
                Edge<T> edge = (Edge<T>) var2.next();
                edge.getDest().printLeaves(out);
            }
        }
    }

    private void printInternalNodes(Node<T> root, PrintWriter out) {
        if (this != root && this.edges.size() > 0) {
            out.println("\t" + nodeId(this) + " [label=\"" + this.data + "\",style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");
        }
        ObjectIterator var3 = this.edges.values().iterator();
        while (var3.hasNext()) {
            Edge<T> edge = (Edge<T>) var3.next();
            edge.getDest().printInternalNodes(root, out);
        }
    }

    private void printEdges(PrintWriter out) {
        ObjectIterator var2 = this.edges.values().iterator();
        while (var2.hasNext()) {
            Edge<T> edge = (Edge<T>) var2.next();
            Node<T> child = edge.getDest();
            out.println("\t" + nodeId(this) + " -> " + nodeId(child) + " [label=\"" + edge + "\",weight=10]");
            child.printEdges(out);
        }
    }

    private void printSLinks(PrintWriter out) {
        if (this.suffix != null) {
            out.println("\t" + nodeId(this) + " -> " + nodeId(this.suffix) + " [label=\"\",weight=0,style=dotted]");
        }
        ObjectIterator var2 = this.edges.values().iterator();
        while (var2.hasNext()) {
            Edge<T> edge = (Edge<T>) var2.next();
            edge.getDest().printSLinks(out);
        }
    }

    private static <T> String nodeId(Node<T> node) {
        return "node" + Integer.toHexString(node.hashCode()).toUpperCase();
    }
}
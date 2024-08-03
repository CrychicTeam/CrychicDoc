package mezz.jei.core.search.suffixtree;

import mezz.jei.core.util.SubString;

public class Edge<T> extends SubString {

    private final Node<T> dest;

    public Edge(SubString subString, Node<T> dest) {
        super(subString);
        this.dest = dest;
    }

    public Node<T> getDest() {
        return this.dest;
    }
}
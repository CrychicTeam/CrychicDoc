package mezz.jei.core.search.suffixtree;

public class RootNode<T> extends Node<T> {

    @Override
    protected boolean contains(T value) {
        return true;
    }

    @Override
    protected void addValue(T value) {
    }
}
package net.minecraft.client.searchtree;

import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;

public class FullTextSearchTree<T> extends IdSearchTree<T> {

    private final List<T> contents;

    private final Function<T, Stream<String>> filler;

    private PlainTextSearchTree<T> plainTextSearchTree = PlainTextSearchTree.empty();

    public FullTextSearchTree(Function<T, Stream<String>> functionTStreamString0, Function<T, Stream<ResourceLocation>> functionTStreamResourceLocation1, List<T> listT2) {
        super(functionTStreamResourceLocation1, listT2);
        this.contents = listT2;
        this.filler = functionTStreamString0;
    }

    @Override
    public void refresh() {
        super.m_214078_();
        this.plainTextSearchTree = PlainTextSearchTree.create(this.contents, this.filler);
    }

    @Override
    protected List<T> searchPlainText(String string0) {
        return this.plainTextSearchTree.search(string0);
    }

    @Override
    protected List<T> searchResourceLocation(String string0, String string1) {
        List<T> $$2 = this.f_235165_.searchNamespace(string0);
        List<T> $$3 = this.f_235165_.searchPath(string1);
        List<T> $$4 = this.plainTextSearchTree.search(string1);
        Iterator<T> $$5 = new MergingUniqueIterator<T>($$3.iterator(), $$4.iterator(), this.f_235164_);
        return ImmutableList.copyOf(new IntersectionIterator($$2.iterator(), $$5, this.f_235164_));
    }
}
package net.minecraft.client.searchtree;

import com.google.common.collect.ImmutableList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

public class IdSearchTree<T> implements RefreshableSearchTree<T> {

    protected final Comparator<T> additionOrder;

    protected final ResourceLocationSearchTree<T> resourceLocationSearchTree;

    public IdSearchTree(Function<T, Stream<ResourceLocation>> functionTStreamResourceLocation0, List<T> listT1) {
        ToIntFunction<T> $$2 = Util.createIndexLookup(listT1);
        this.additionOrder = Comparator.comparingInt($$2);
        this.resourceLocationSearchTree = ResourceLocationSearchTree.create(listT1, functionTStreamResourceLocation0);
    }

    @Override
    public List<T> search(String string0) {
        int $$1 = string0.indexOf(58);
        return $$1 == -1 ? this.searchPlainText(string0) : this.searchResourceLocation(string0.substring(0, $$1).trim(), string0.substring($$1 + 1).trim());
    }

    protected List<T> searchPlainText(String string0) {
        return this.resourceLocationSearchTree.searchPath(string0);
    }

    protected List<T> searchResourceLocation(String string0, String string1) {
        List<T> $$2 = this.resourceLocationSearchTree.searchNamespace(string0);
        List<T> $$3 = this.resourceLocationSearchTree.searchPath(string1);
        return ImmutableList.copyOf(new IntersectionIterator($$2.iterator(), $$3.iterator(), this.additionOrder));
    }
}
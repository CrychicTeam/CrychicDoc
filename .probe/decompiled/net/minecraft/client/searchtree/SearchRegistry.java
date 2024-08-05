package net.minecraft.client.searchtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;

public class SearchRegistry implements ResourceManagerReloadListener {

    public static final SearchRegistry.Key<ItemStack> CREATIVE_NAMES = new SearchRegistry.Key<>();

    public static final SearchRegistry.Key<ItemStack> CREATIVE_TAGS = new SearchRegistry.Key<>();

    public static final SearchRegistry.Key<RecipeCollection> RECIPE_COLLECTIONS = new SearchRegistry.Key<>();

    private final Map<SearchRegistry.Key<?>, SearchRegistry.TreeEntry<?>> searchTrees = new HashMap();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        for (SearchRegistry.TreeEntry<?> $$1 : this.searchTrees.values()) {
            $$1.refresh();
        }
    }

    public <T> void register(SearchRegistry.Key<T> searchRegistryKeyT0, SearchRegistry.TreeBuilderSupplier<T> searchRegistryTreeBuilderSupplierT1) {
        this.searchTrees.put(searchRegistryKeyT0, new SearchRegistry.TreeEntry<>(searchRegistryTreeBuilderSupplierT1));
    }

    private <T> SearchRegistry.TreeEntry<T> getSupplier(SearchRegistry.Key<T> searchRegistryKeyT0) {
        SearchRegistry.TreeEntry<T> $$1 = (SearchRegistry.TreeEntry<T>) this.searchTrees.get(searchRegistryKeyT0);
        if ($$1 == null) {
            throw new IllegalStateException("Tree builder not registered");
        } else {
            return $$1;
        }
    }

    public <T> void populate(SearchRegistry.Key<T> searchRegistryKeyT0, List<T> listT1) {
        this.getSupplier(searchRegistryKeyT0).populate(listT1);
    }

    public <T> SearchTree<T> getTree(SearchRegistry.Key<T> searchRegistryKeyT0) {
        return this.getSupplier(searchRegistryKeyT0).tree;
    }

    public static class Key<T> {
    }

    public interface TreeBuilderSupplier<T> extends Function<List<T>, RefreshableSearchTree<T>> {
    }

    static class TreeEntry<T> {

        private final SearchRegistry.TreeBuilderSupplier<T> factory;

        RefreshableSearchTree<T> tree = RefreshableSearchTree.empty();

        TreeEntry(SearchRegistry.TreeBuilderSupplier<T> searchRegistryTreeBuilderSupplierT0) {
            this.factory = searchRegistryTreeBuilderSupplierT0;
        }

        void populate(List<T> listT0) {
            this.tree = (RefreshableSearchTree<T>) this.factory.apply(listT0);
            this.tree.refresh();
        }

        void refresh() {
            this.tree.refresh();
        }
    }
}
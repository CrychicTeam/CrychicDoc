package org.embeddedt.modernfix.searchtree;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.world.item.ItemStack;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;

public class SearchTreeProviderRegistry {

    private static final List<SearchTreeProviderRegistry.Provider> searchTreeProviders = new ArrayList();

    public static synchronized SearchTreeProviderRegistry.Provider getSearchTreeProvider() {
        for (SearchTreeProviderRegistry.Provider p : searchTreeProviders) {
            if (p.canUse()) {
                return p;
            }
        }
        return ModernFixMixinPlugin.instance.isOptionEnabled("perf.blast_search_trees.force.Registry") ? DummySearchTree.PROVIDER : null;
    }

    public static synchronized void register(SearchTreeProviderRegistry.Provider p) {
        if (p.canUse()) {
            searchTreeProviders.add(p);
        }
    }

    public interface Provider {

        RefreshableSearchTree<ItemStack> getSearchTree(boolean var1);

        boolean canUse();

        String getName();
    }
}
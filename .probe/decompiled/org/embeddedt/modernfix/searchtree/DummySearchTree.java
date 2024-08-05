package org.embeddedt.modernfix.searchtree;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.world.item.ItemStack;

public class DummySearchTree<T> implements RefreshableSearchTree<T> {

    static final SearchTreeProviderRegistry.Provider PROVIDER = new SearchTreeProviderRegistry.Provider() {

        @Override
        public RefreshableSearchTree<ItemStack> getSearchTree(boolean tag) {
            return new DummySearchTree<>();
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public String getName() {
            return "Dummy";
        }
    };

    @Override
    public void refresh() {
    }

    @Override
    public List<T> search(String pSearchText) {
        return Collections.emptyList();
    }
}
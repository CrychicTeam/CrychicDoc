package org.embeddedt.modernfix.searchtree;

import com.google.common.collect.ImmutableList;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.gui.ingredients.IngredientFilter;
import mezz.jei.gui.ingredients.IngredientFilterApi;
import mezz.jei.library.runtime.JeiRuntime;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.world.item.ItemStack;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;

public class JEIBackedSearchTree extends DummySearchTree<ItemStack> {

    private final boolean filteringByTag;

    private String lastSearchText = "";

    private final List<ItemStack> listCache = new ArrayList();

    private static final Field filterField;

    private static final MethodHandle getIngredientListUncached;

    public static final SearchTreeProviderRegistry.Provider PROVIDER;

    public JEIBackedSearchTree(boolean filteringByTag) {
        this.filteringByTag = filteringByTag;
    }

    @Override
    public List<ItemStack> search(String pSearchText) {
        Optional<JeiRuntime> runtime = JEIRuntimeCapturer.runtime();
        if (runtime.isPresent()) {
            IngredientFilterApi iFilterApi = (IngredientFilterApi) ((JeiRuntime) runtime.get()).getIngredientFilter();
            IngredientFilter filter;
            try {
                filter = (IngredientFilter) filterField.get(iFilterApi);
            } catch (ReflectiveOperationException var6) {
                ModernFix.LOGGER.error(var6);
                return Collections.emptyList();
            }
            return this.searchJEI(filter, pSearchText);
        } else {
            return super.search(pSearchText);
        }
    }

    private List<ItemStack> searchJEI(IngredientFilter filter, String pSearchText) {
        if (!pSearchText.equals(this.lastSearchText)) {
            this.listCache.clear();
            String finalSearchTerm = this.filteringByTag ? "$" + pSearchText : pSearchText;
            List<ITypedIngredient<?>> ingredients;
            try {
                ingredients = (List) getIngredientListUncached.invokeExact(filter, finalSearchTerm);
            } catch (Throwable var7) {
                ModernFix.LOGGER.error("Error searching", var7);
                ingredients = ImmutableList.of();
            }
            for (ITypedIngredient<?> ingredient : ingredients) {
                if (ingredient.getIngredient() instanceof ItemStack) {
                    this.listCache.add((ItemStack) ingredient.getIngredient());
                }
            }
            this.lastSearchText = pSearchText;
        }
        return this.listCache;
    }

    static {
        MethodHandle m;
        Field f;
        try {
            Method jeiMethod = IngredientFilter.class.getDeclaredMethod("getIngredientListUncached", String.class);
            jeiMethod.setAccessible(true);
            m = MethodHandles.lookup().unreflect(jeiMethod);
            f = IngredientFilterApi.class.getDeclaredField("ingredientFilter");
            f.setAccessible(true);
        } catch (RuntimeException | NoClassDefFoundError | ReflectiveOperationException var3) {
            m = null;
            f = null;
        }
        getIngredientListUncached = m;
        filterField = f;
        PROVIDER = new SearchTreeProviderRegistry.Provider() {

            @Override
            public RefreshableSearchTree<ItemStack> getSearchTree(boolean tag) {
                return new JEIBackedSearchTree(tag);
            }

            @Override
            public boolean canUse() {
                return ModernFixPlatformHooks.INSTANCE.modPresent("jei") && !ModernFixPlatformHooks.INSTANCE.modPresent("emi") && JEIBackedSearchTree.getIngredientListUncached != null && JEIBackedSearchTree.filterField != null;
            }

            @Override
            public String getName() {
                return "JEI";
            }
        };
    }
}
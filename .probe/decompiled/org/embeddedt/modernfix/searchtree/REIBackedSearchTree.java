package org.embeddedt.modernfix.searchtree;

import com.google.common.base.Predicates;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.impl.client.search.AsyncSearchManager;
import me.shedaniel.rei.impl.common.entry.type.EntryRegistryImpl;
import me.shedaniel.rei.impl.common.util.HashedEntryStackWrapper;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.world.item.ItemStack;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;

public class REIBackedSearchTree extends DummySearchTree<ItemStack> {

    private final AsyncSearchManager searchManager = createSearchManager();

    private final boolean filteringByTag;

    private String lastSearchText = "";

    private final List<ItemStack> listCache = new ArrayList();

    public static final SearchTreeProviderRegistry.Provider PROVIDER = new SearchTreeProviderRegistry.Provider() {

        @Override
        public RefreshableSearchTree<ItemStack> getSearchTree(boolean tag) {
            return new REIBackedSearchTree(tag);
        }

        @Override
        public boolean canUse() {
            return ModernFixPlatformHooks.INSTANCE.modPresent("roughlyenoughitems");
        }

        @Override
        public String getName() {
            return "REI";
        }
    };

    public REIBackedSearchTree(boolean filteringByTag) {
        this.filteringByTag = filteringByTag;
    }

    @Override
    public List<ItemStack> search(String pSearchText) {
        return this.searchREI(pSearchText);
    }

    private List<ItemStack> searchREI(String pSearchText) {
        if (!pSearchText.equals(this.lastSearchText)) {
            this.listCache.clear();
            this.searchManager.updateFilter(pSearchText);
            List stacks;
            try {
                stacks = this.searchManager.getNow();
            } catch (RuntimeException var6) {
                ModernFix.LOGGER.error("Couldn't search for '" + pSearchText + "'", var6);
                stacks = Collections.emptyList();
            }
            for (Object o : stacks) {
                if (!(o instanceof EntryStack<?> stack)) {
                    if (!(o instanceof HashedEntryStackWrapper)) {
                        ModernFix.LOGGER.error("Don't know how to handle {}", o.getClass().getName());
                        continue;
                    }
                    stack = ((HashedEntryStackWrapper) o).unwrap();
                }
                if (stack.getType() == VanillaEntryTypes.ITEM) {
                    this.listCache.add((ItemStack) stack.cheatsAs().getValue());
                }
            }
            this.lastSearchText = pSearchText;
        }
        return this.listCache;
    }

    private static AsyncSearchManager createSearchManager() {
        try {
            Method m;
            Method normalizeMethod;
            try {
                m = EntryRegistryImpl.class.getDeclaredMethod("getPreFilteredComplexList");
                m.setAccessible(true);
                normalizeMethod = HashedEntryStackWrapper.class.getDeclaredMethod("normalize");
                normalizeMethod.setAccessible(true);
            } catch (NoSuchMethodException var12) {
                m = EntryRegistryImpl.class.getDeclaredMethod("getPreFilteredList");
                m.setAccessible(true);
                normalizeMethod = EntryStack.class.getDeclaredMethod("normalize");
                normalizeMethod.setAccessible(true);
            }
            MethodHandle getListMethod = MethodHandles.publicLookup().unreflect(m);
            MethodHandle normalize = MethodHandles.publicLookup().unreflect(normalizeMethod);
            EntryRegistryImpl registry = (EntryRegistryImpl) EntryRegistry.getInstance();
            Supplier stackListSupplier = () -> {
                try {
                    return (List) getListMethod.invokeExact(registry);
                } catch (Throwable var3x) {
                    if (var3x instanceof RuntimeException) {
                        throw (RuntimeException) var3x;
                    } else {
                        throw new RuntimeException(var3x);
                    }
                }
            };
            UnaryOperator normalizeOperator = o -> {
                try {
                    return (Object) normalize.invoke(o);
                } catch (Throwable var3x) {
                    if (var3x instanceof RuntimeException) {
                        throw (RuntimeException) var3x;
                    } else {
                        throw new RuntimeException(var3x);
                    }
                }
            };
            Supplier<Predicate<Boolean>> shouldShowStack = () -> Predicates.alwaysTrue();
            try {
                try {
                    MethodHandle cn = MethodHandles.publicLookup().findConstructor(AsyncSearchManager.class, MethodType.methodType(void.class, Supplier.class, Supplier.class, UnaryOperator.class));
                    return (AsyncSearchManager) cn.invoke(stackListSupplier, shouldShowStack, normalizeOperator);
                } catch (NoSuchMethodException var10) {
                    MethodHandle cnx = MethodHandles.publicLookup().findConstructor(AsyncSearchManager.class, MethodType.methodType(void.class, Function.class, Supplier.class, UnaryOperator.class));
                    return (AsyncSearchManager) cnx.invoke(o -> stackListSupplier.get(), shouldShowStack, normalizeOperator);
                }
            } catch (Throwable var11) {
                throw new ReflectiveOperationException(var11);
            }
        } catch (ReflectiveOperationException var13) {
            throw new RuntimeException(var13);
        }
    }
}
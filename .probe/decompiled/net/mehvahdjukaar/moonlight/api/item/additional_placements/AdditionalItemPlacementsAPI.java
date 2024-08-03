package net.mehvahdjukaar.moonlight.api.item.additional_placements;

import com.mojang.datafixers.util.Pair;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.moonlight.core.misc.IExtendedItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class AdditionalItemPlacementsAPI {

    private static boolean isAfterRegistration = false;

    private static WeakReference<Map<Block, Item>> blockToItemsMap = new WeakReference(null);

    private static final List<Consumer<AdditionalItemPlacementsAPI.Event>> registrationListeners = new ArrayList();

    private static final List<Pair<Supplier<? extends AdditionalItemPlacement>, Supplier<? extends Item>>> PLACEMENTS = new ArrayList();

    private static final List<Pair<Function<Item, ? extends AdditionalItemPlacement>, Predicate<Item>>> PLACEMENTS_GENERIC = new ArrayList();

    @Deprecated(forRemoval = true)
    public static void register(Supplier<? extends AdditionalItemPlacement> placement, Supplier<? extends Item> itemSupplier) {
        if (PlatHelper.isDev() && isAfterRegistration) {
            throw new IllegalStateException("Attempted to add placeable behavior after registration");
        } else {
            PLACEMENTS.add(Pair.of(placement, itemSupplier));
        }
    }

    @Deprecated(forRemoval = true)
    public static void register(Function<Item, ? extends AdditionalItemPlacement> placement, Predicate<Item> itemPredicate) {
        if (PlatHelper.isDev() && isAfterRegistration) {
            throw new IllegalStateException("Attempted to add placeable behavior after registration");
        } else {
            PLACEMENTS_GENERIC.add(Pair.of(placement, itemPredicate));
        }
    }

    @Deprecated(forRemoval = true)
    public static void registerSimple(Supplier<? extends Block> block, Supplier<? extends Item> itemSupplier) {
        register(() -> new AdditionalItemPlacement((Block) block.get()), itemSupplier);
    }

    public static void addRegistration(Consumer<AdditionalItemPlacementsAPI.Event> eventConsumer) {
        Moonlight.assertInitPhase();
        registrationListeners.add(eventConsumer);
    }

    @Nullable
    public static AdditionalItemPlacement getBehavior(Item item) {
        return ((IExtendedItem) item).moonlight$getAdditionalBehavior();
    }

    public static boolean hasBehavior(Item item) {
        return getBehavior(item) != null;
    }

    @Internal
    public static void afterItemReg() {
        if (blockToItemsMap.get() == null && PlatHelper.isDev()) {
            throw new AssertionError("Block to items map was null");
        } else {
            attemptRegistering();
        }
    }

    private static void attemptRegistering() {
        Map<Block, Item> map = (Map<Block, Item>) blockToItemsMap.get();
        if (map != null) {
            for (Item item : BuiltInRegistries.ITEM) {
                for (Pair<Function<Item, ? extends AdditionalItemPlacement>, Predicate<Item>> v : PLACEMENTS_GENERIC) {
                    Predicate<Item> predicate = (Predicate<Item>) v.getSecond();
                    if (predicate.test(item)) {
                        PLACEMENTS.add(Pair.of((Supplier) () -> (AdditionalItemPlacement) ((Function) v.getFirst()).apply(item), (Supplier) () -> item));
                    }
                }
            }
            AdditionalItemPlacementsAPI.Event ev = (target, instance) -> PLACEMENTS.add(Pair.of((Supplier) () -> instance, (Supplier) () -> target));
            for (Consumer<AdditionalItemPlacementsAPI.Event> l : registrationListeners) {
                l.accept(ev);
            }
            PLACEMENTS_GENERIC.clear();
            for (Pair<Supplier<? extends AdditionalItemPlacement>, Supplier<? extends Item>> p : PLACEMENTS) {
                AdditionalItemPlacement placement = (AdditionalItemPlacement) ((Supplier) p.getFirst()).get();
                Item i = (Item) ((Supplier) p.getSecond()).get();
                Block b = placement.getPlacedBlock();
                if (i != null && b != null) {
                    if (i == Items.AIR || b == Blocks.AIR) {
                        throw new AssertionError("Attempted to register an Additional behavior for block " + b + " using with item " + i);
                    }
                    ((IExtendedItem) i).moonlight$addAdditionalBehavior(placement);
                    if (!map.containsKey(b)) {
                        map.put(b, i);
                    }
                }
            }
        }
    }

    static void onRegistryCallback(Map<Block, Item> pBlockToItemMap) {
        blockToItemsMap = new WeakReference(pBlockToItemMap);
        if (isAfterRegistration) {
            attemptRegistering();
            blockToItemsMap.clear();
        }
        isAfterRegistration = true;
    }

    public interface Event {

        void register(Item var1, AdditionalItemPlacement var2);

        default void registerSimple(Item target, Block toPlace) {
            this.register(target, new AdditionalItemPlacement(toPlace));
        }
    }
}
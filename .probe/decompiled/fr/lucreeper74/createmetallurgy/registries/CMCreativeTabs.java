package fr.lucreeper74.createmetallurgy.registries;

import com.simibubi.create.foundation.utility.Components;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import fr.lucreeper74.createmetallurgy.content.light_bulb.LightBulbBlock;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CMCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "createmetallurgy");

    public static final RegistryObject<CreativeModeTab> MAIN_CREATIVE_TAB = REGISTER.register("main_group", () -> CreativeModeTab.builder().title(Components.translatable("itemGroup.createmetallurgy.main_group")).icon(() -> new ItemStack((ItemLike) CMFluids.MOLTEN_IRON.getBucket().get())).displayItems(new CMCreativeTabs.RegistrateDisplayItemsGenerator()).build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private List<Item> collectBlocks(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList();
            for (RegistryEntry<Block> entry : CreateMetallurgy.REGISTRATE.getAll(Registries.BLOCK)) {
                Item item = ((Block) entry.get()).asItem();
                if (item != Items.AIR && !exclusionPredicate.test(item)) {
                    items.add(item);
                }
            }
            return new ReferenceArrayList(new ReferenceLinkedOpenHashSet(items));
        }

        private List<Item> collectItems(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList();
            for (RegistryEntry<Item> entry : CreateMetallurgy.REGISTRATE.getAll(Registries.ITEM)) {
                Item item = (Item) entry.get();
                if (!(item instanceof BlockItem) && !exclusionPredicate.test(item)) {
                    items.add(item);
                }
            }
            return items;
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items, Function<Item, CreativeModeTab.TabVisibility> visibilityFunc) {
            for (Item item : items) {
                output.accept(item, (CreativeModeTab.TabVisibility) visibilityFunc.apply(item));
            }
        }

        private static Function<Item, CreativeModeTab.TabVisibility> makeVisibilityFunc() {
            Map<Item, CreativeModeTab.TabVisibility> visibilities = new Reference2ObjectOpenHashMap();
            for (BlockEntry<LightBulbBlock> entry : CMBlocks.LIGHT_BULBS) {
                LightBulbBlock block = (LightBulbBlock) entry.get();
                if (block.getColor() != DyeColor.WHITE) {
                    visibilities.put(entry.m_5456_(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                }
            }
            return item -> {
                CreativeModeTab.TabVisibility visibility = (CreativeModeTab.TabVisibility) visibilities.get(item);
                return visibility != null ? visibility : CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
            };
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
            Function<Item, CreativeModeTab.TabVisibility> visibilityFunc = makeVisibilityFunc();
            List<Item> items = new LinkedList();
            items.addAll(this.collectBlocks(item -> false));
            items.addAll(this.collectItems(item -> false));
            outputAll(output, items, visibilityFunc);
        }
    }
}
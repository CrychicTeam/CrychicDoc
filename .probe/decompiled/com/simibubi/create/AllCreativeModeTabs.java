package com.simibubi.create;

import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import com.simibubi.create.content.decoration.palettes.AllPaletteBlocks;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.toolbox.ToolboxBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.simibubi.create.foundation.utility.Components;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.mutable.MutableObject;

@EventBusSubscriber(bus = Bus.MOD)
public class AllCreativeModeTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "create");

    public static final RegistryObject<CreativeModeTab> BASE_CREATIVE_TAB = REGISTER.register("base", () -> CreativeModeTab.builder().title(Components.translatable("itemGroup.create.base")).withTabsBefore(new ResourceKey[] { CreativeModeTabs.SPAWN_EGGS }).icon(() -> AllBlocks.COGWHEEL.asStack()).displayItems(new AllCreativeModeTabs.RegistrateDisplayItemsGenerator(true, BASE_CREATIVE_TAB)).build());

    public static final RegistryObject<CreativeModeTab> PALETTES_CREATIVE_TAB = REGISTER.register("palettes", () -> CreativeModeTab.builder().title(Components.translatable("itemGroup.create.palettes")).withTabsBefore(new ResourceKey[] { BASE_CREATIVE_TAB.getKey() }).icon(() -> AllPaletteBlocks.ORNATE_IRON_WINDOW.asStack()).displayItems(new AllCreativeModeTabs.RegistrateDisplayItemsGenerator(false, PALETTES_CREATIVE_TAB)).build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    private static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private static final Predicate<Item> IS_ITEM_3D_PREDICATE;

        private final boolean addItems;

        private final RegistryObject<CreativeModeTab> tabFilter;

        @OnlyIn(Dist.CLIENT)
        private static Predicate<Item> makeClient3dItemPredicate() {
            return item -> {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
                return model.isGui3d();
            };
        }

        public RegistrateDisplayItemsGenerator(boolean addItems, RegistryObject<CreativeModeTab> tabFilter) {
            this.addItems = addItems;
            this.tabFilter = tabFilter;
        }

        private static Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = new ReferenceOpenHashSet();
            List<ItemProviderEntry<?>> simpleExclusions = List.of(AllItems.INCOMPLETE_PRECISION_MECHANISM, AllItems.INCOMPLETE_REINFORCED_SHEET, AllItems.INCOMPLETE_TRACK, AllItems.CHROMATIC_COMPOUND, AllItems.SHADOW_STEEL, AllItems.REFINED_RADIANCE, AllItems.COPPER_BACKTANK_PLACEABLE, AllItems.NETHERITE_BACKTANK_PLACEABLE, AllItems.MINECART_CONTRAPTION, AllItems.FURNACE_MINECART_CONTRAPTION, AllItems.CHEST_MINECART_CONTRAPTION, AllItems.SCHEMATIC, AllBlocks.ANDESITE_ENCASED_SHAFT, AllBlocks.BRASS_ENCASED_SHAFT, AllBlocks.ANDESITE_ENCASED_COGWHEEL, AllBlocks.BRASS_ENCASED_COGWHEEL, AllBlocks.ANDESITE_ENCASED_LARGE_COGWHEEL, AllBlocks.BRASS_ENCASED_LARGE_COGWHEEL, AllBlocks.MYSTERIOUS_CUCKOO_CLOCK, AllBlocks.ELEVATOR_CONTACT, AllBlocks.SHADOW_STEEL_CASING, AllBlocks.REFINED_RADIANCE_CASING);
            List<ItemEntry<TagDependentIngredientItem>> tagDependentExclusions = List.of(AllItems.CRUSHED_OSMIUM, AllItems.CRUSHED_PLATINUM, AllItems.CRUSHED_SILVER, AllItems.CRUSHED_TIN, AllItems.CRUSHED_LEAD, AllItems.CRUSHED_QUICKSILVER, AllItems.CRUSHED_BAUXITE, AllItems.CRUSHED_URANIUM, AllItems.CRUSHED_NICKEL);
            for (ItemProviderEntry<?> entry : simpleExclusions) {
                exclusions.add(entry.m_5456_());
            }
            for (ItemEntry<TagDependentIngredientItem> entry : tagDependentExclusions) {
                TagDependentIngredientItem item = (TagDependentIngredientItem) entry.get();
                if (item.shouldHide()) {
                    exclusions.add(entry.m_5456_());
                }
            }
            return exclusions::contains;
        }

        private static List<AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering> makeOrderings() {
            List<AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering> orderings = new ReferenceArrayList();
            Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleBeforeOrderings = Map.of(AllItems.EMPTY_BLAZE_BURNER, AllBlocks.BLAZE_BURNER, AllItems.SCHEDULE, AllBlocks.TRACK_STATION);
            Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleAfterOrderings = Map.of(AllItems.VERTICAL_GEARBOX, AllBlocks.GEARBOX);
            simpleBeforeOrderings.forEach((entry, otherEntry) -> orderings.add(AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering.before(entry.m_5456_(), otherEntry.m_5456_())));
            simpleAfterOrderings.forEach((entry, otherEntry) -> orderings.add(AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering.after(entry.m_5456_(), otherEntry.m_5456_())));
            return orderings;
        }

        private static Function<Item, ItemStack> makeStackFunc() {
            Map<Item, Function<Item, ItemStack>> factories = new Reference2ReferenceOpenHashMap();
            Map<ItemProviderEntry<?>, Function<Item, ItemStack>> simpleFactories = Map.of(AllItems.COPPER_BACKTANK, (Function) item -> {
                ItemStack stack = new ItemStack(item);
                stack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants());
                return stack;
            }, AllItems.NETHERITE_BACKTANK, (Function) item -> {
                ItemStack stack = new ItemStack(item);
                stack.getOrCreateTag().putInt("Air", BacktankUtil.maxAirWithoutEnchants());
                return stack;
            });
            simpleFactories.forEach((entry, factory) -> factories.put(entry.m_5456_(), factory));
            return item -> {
                Function<Item, ItemStack> factory = (Function<Item, ItemStack>) factories.get(item);
                return factory != null ? (ItemStack) factory.apply(item) : new ItemStack(item);
            };
        }

        private static Function<Item, CreativeModeTab.TabVisibility> makeVisibilityFunc() {
            Map<Item, CreativeModeTab.TabVisibility> visibilities = new Reference2ObjectOpenHashMap();
            Map<ItemProviderEntry<?>, CreativeModeTab.TabVisibility> simpleVisibilities = Map.of(AllItems.BLAZE_CAKE_BASE, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
            simpleVisibilities.forEach((entryx, factory) -> visibilities.put(entryx.m_5456_(), factory));
            for (BlockEntry<ValveHandleBlock> entry : AllBlocks.DYED_VALVE_HANDLES) {
                visibilities.put(entry.m_5456_(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
            }
            for (BlockEntry<SeatBlock> entry : AllBlocks.SEATS) {
                SeatBlock block = (SeatBlock) entry.get();
                if (block.getColor() != DyeColor.RED) {
                    visibilities.put(entry.m_5456_(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                }
            }
            for (BlockEntry<ToolboxBlock> entryx : AllBlocks.TOOLBOXES) {
                ToolboxBlock block = (ToolboxBlock) entryx.get();
                if (block.getColor() != DyeColor.BROWN) {
                    visibilities.put(entryx.m_5456_(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                }
            }
            return item -> {
                CreativeModeTab.TabVisibility visibility = (CreativeModeTab.TabVisibility) visibilities.get(item);
                return visibility != null ? visibility : CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
            };
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            Predicate<Item> exclusionPredicate = makeExclusionPredicate();
            List<AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering> orderings = makeOrderings();
            Function<Item, ItemStack> stackFunc = makeStackFunc();
            Function<Item, CreativeModeTab.TabVisibility> visibilityFunc = makeVisibilityFunc();
            List<Item> items = new LinkedList();
            if (this.addItems) {
                items.addAll(this.collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE.negate())));
            }
            items.addAll(this.collectBlocks(exclusionPredicate));
            if (this.addItems) {
                items.addAll(this.collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE)));
            }
            applyOrderings(items, orderings);
            outputAll(output, items, stackFunc, visibilityFunc);
        }

        private List<Item> collectBlocks(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList();
            for (RegistryEntry<Block> entry : Create.REGISTRATE.getAll(Registries.BLOCK)) {
                if (CreateRegistrate.isInCreativeTab(entry, this.tabFilter)) {
                    Item item = ((Block) entry.get()).asItem();
                    if (item != Items.AIR && !exclusionPredicate.test(item)) {
                        items.add(item);
                    }
                }
            }
            return new ReferenceArrayList(new ReferenceLinkedOpenHashSet(items));
        }

        private List<Item> collectItems(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList();
            for (RegistryEntry<Item> entry : Create.REGISTRATE.getAll(Registries.ITEM)) {
                if (CreateRegistrate.isInCreativeTab(entry, this.tabFilter)) {
                    Item item = (Item) entry.get();
                    if (!(item instanceof BlockItem) && !exclusionPredicate.test(item)) {
                        items.add(item);
                    }
                }
            }
            return items;
        }

        private static void applyOrderings(List<Item> items, List<AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering> orderings) {
            for (AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering ordering : orderings) {
                int anchorIndex = items.indexOf(ordering.anchor());
                if (anchorIndex != -1) {
                    Item item = ordering.item();
                    int itemIndex = items.indexOf(item);
                    if (itemIndex != -1) {
                        items.remove(itemIndex);
                        if (itemIndex < anchorIndex) {
                            anchorIndex--;
                        }
                    }
                    if (ordering.type() == AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering.Type.AFTER) {
                        items.add(anchorIndex + 1, item);
                    } else {
                        items.add(anchorIndex, item);
                    }
                }
            }
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, CreativeModeTab.TabVisibility> visibilityFunc) {
            for (Item item : items) {
                output.accept((ItemStack) stackFunc.apply(item), (CreativeModeTab.TabVisibility) visibilityFunc.apply(item));
            }
        }

        static {
            MutableObject<Predicate<Item>> isItem3d = new MutableObject((Predicate) item -> false);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> isItem3d.setValue((Predicate) item -> {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
                return model.isGui3d();
            }));
            IS_ITEM_3D_PREDICATE = (Predicate<Item>) isItem3d.getValue();
        }

        private static record ItemOrdering(Item item, Item anchor, AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering.Type type) {

            public static AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering before(Item item, Item anchor) {
                return new AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering(item, anchor, AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering.Type.BEFORE);
            }

            public static AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering after(Item item, Item anchor) {
                return new AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering(item, anchor, AllCreativeModeTabs.RegistrateDisplayItemsGenerator.ItemOrdering.Type.AFTER);
            }

            public static enum Type {

                BEFORE, AFTER
            }
        }
    }
}
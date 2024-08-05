package net.mehvahdjukaar.supplementaries.reg;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacementsAPI;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FlagBlock;
import net.mehvahdjukaar.supplementaries.common.events.overrides.SuppAdditionalPlacement;
import net.mehvahdjukaar.supplementaries.common.items.FlagItem;
import net.mehvahdjukaar.supplementaries.common.items.PresentItem;
import net.mehvahdjukaar.supplementaries.common.items.SignPostItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.BuzzierBeesCompat;
import net.mehvahdjukaar.supplementaries.integration.CaveEnhancementsCompat;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class RegUtils {

    public static void initDynamicRegistry() {
        BlockSetAPI.addDynamicItemRegistration(RegUtils::registerSignPostItems, WoodType.class);
        AdditionalItemPlacementsAPI.addRegistration(RegUtils::registerPlacements);
    }

    private static void registerPlacements(AdditionalItemPlacementsAPI.Event event) {
        if ((Boolean) CommonConfigs.Tweaks.WRITTEN_BOOKS.get()) {
            SuppAdditionalPlacement horizontalPlacement = new SuppAdditionalPlacement((Block) ModRegistry.BOOK_PILE_H.get());
            event.register(Items.BOOK, horizontalPlacement);
            event.register(Items.WRITABLE_BOOK, horizontalPlacement);
            event.register(Items.WRITTEN_BOOK, horizontalPlacement);
        }
        if ((Boolean) CommonConfigs.Tweaks.PLACEABLE_BOOKS.get()) {
            SuppAdditionalPlacement verticalPlacement = new SuppAdditionalPlacement((Block) ModRegistry.BOOK_PILE.get());
            event.register(Items.ENCHANTED_BOOK, verticalPlacement);
            Item tome = (Item) CompatObjects.TOME.get();
            if (tome != null) {
                event.register(tome, verticalPlacement);
            }
            Item gene = (Item) CompatObjects.GENE_BOOK.get();
            if (gene != null) {
                event.register(gene, verticalPlacement);
            }
        }
        event.registerSimple((Item) ModRegistry.PANCAKE_ITEM.get(), (Block) ModRegistry.PANCAKE.get());
        if ((Boolean) CommonConfigs.Tweaks.PLACEABLE_STICKS.get()) {
            event.register(Items.STICK, new SuppAdditionalPlacement((Block) ModRegistry.STICK_BLOCK.get()));
        }
        if ((Boolean) CommonConfigs.Tweaks.PLACEABLE_RODS.get()) {
            event.register(Items.BLAZE_ROD, new SuppAdditionalPlacement((Block) ModRegistry.BLAZE_ROD_BLOCK.get()));
        }
        if ((Boolean) CommonConfigs.Tweaks.PLACEABLE_GUNPOWDER.get()) {
            event.register(Items.GUNPOWDER, new SuppAdditionalPlacement((Block) ModRegistry.GUNPOWDER_BLOCK.get()));
        }
    }

    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> sup) {
        return RegHelper.registerItem(Supplementaries.res(name), sup);
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> regTile(String name, Supplier<T> sup) {
        return RegHelper.registerBlockEntityType(Supplementaries.res(name), sup);
    }

    public static <T extends Block> RegSupplier<T> regBlock(String name, Supplier<T> sup) {
        return RegHelper.registerBlock(Supplementaries.res(name), sup);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory) {
        return regWithItem(name, blockFactory, 0);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory, int burnTime) {
        return regWithItem(name, blockFactory, new Item.Properties(), burnTime);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory, Item.Properties properties, int burnTime) {
        RegSupplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, properties, burnTime);
        return block;
    }

    public static RegSupplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties, int burnTime) {
        return RegHelper.registerItem(Supplementaries.res(name), () -> (BlockItem) (burnTime == 0 ? new BlockItem((Block) blockSup.get(), properties) : new WoodBasedBlockItem((Block) blockSup.get(), properties, burnTime)));
    }

    public static Map<DyeColor, Supplier<Block>> registerCandleHolders(ResourceLocation baseName) {
        Map<DyeColor, Supplier<Block>> map = new Object2ObjectLinkedOpenHashMap();
        BlockBehaviour.Properties prop = BlockBehaviour.Properties.of().noCollission().pushReaction(PushReaction.DESTROY).noOcclusion().instabreak().sound(SoundType.LANTERN);
        Supplier<Block> block = RegHelper.registerBlockWithItem(baseName, () -> new CandleHolderBlock(null, prop));
        map.put(null, block);
        for (DyeColor color : BlocksColorAPI.SORTED_COLORS) {
            String name = baseName.getPath() + "_" + color.getName();
            Supplier<Block> coloredBlock = RegHelper.registerBlockWithItem(new ResourceLocation(baseName.getNamespace(), name), () -> new CandleHolderBlock(color, prop));
            map.put(color, coloredBlock);
        }
        if (CompatHandler.BUZZIER_BEES) {
            BuzzierBeesCompat.registerCandle(baseName);
        }
        if (CompatHandler.CAVE_ENHANCEMENTS) {
            CaveEnhancementsCompat.registerCandle(baseName);
        }
        return map;
    }

    public static Map<DyeColor, Supplier<Block>> registerFlags(String baseName) {
        Map<DyeColor, Supplier<Block>> map = new Object2ObjectLinkedOpenHashMap();
        for (DyeColor color : BlocksColorAPI.SORTED_COLORS) {
            String name = baseName + "_" + color.getName();
            Supplier<Block> block = regBlock(name, () -> new FlagBlock(color, BlockBehaviour.Properties.of().ignitedByLava().mapColor(color.getMapColor()).strength(1.0F).noOcclusion().sound(SoundType.WOOD)));
            map.put(color, block);
            regItem(name, () -> new FlagItem((Block) block.get(), new Item.Properties().stacksTo(16)));
        }
        return map;
    }

    public static Map<DyeColor, Supplier<Block>> registerPresents(String baseName, BiFunction<DyeColor, BlockBehaviour.Properties, Block> presentFactory) {
        Map<DyeColor, Supplier<Block>> map = new Object2ObjectLinkedOpenHashMap();
        Supplier<Block> block = regBlock(baseName, () -> (Block) presentFactory.apply(null, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).pushReaction(PushReaction.DESTROY).strength(1.0F).sound(ModSounds.PRESENT)));
        map.put(null, block);
        regItem(baseName, () -> new PresentItem((Block) block.get(), new Item.Properties()));
        for (DyeColor color : BlocksColorAPI.SORTED_COLORS) {
            String name = baseName + "_" + color.getName();
            Supplier<Block> bb = regBlock(name, () -> (Block) presentFactory.apply(color, BlockBehaviour.Properties.of().mapColor(color.getMapColor()).strength(1.0F).sound(ModSounds.PRESENT)));
            map.put(color, bb);
            regItem(name, () -> new PresentItem((Block) bb.get(), new Item.Properties()));
        }
        return map;
    }

    private static void registerSignPostItems(Registrator<Item> event, Collection<WoodType> woodTypes) {
        for (WoodType wood : woodTypes) {
            String name = wood.getVariantId("sign_post");
            SignPostItem item = new SignPostItem(new Item.Properties().stacksTo(16), wood);
            wood.addChild("supplementaries:sign_post", item);
            event.register(Supplementaries.res(name), item);
            ModRegistry.SIGN_POST_ITEMS.put(wood, item);
        }
    }
}
package net.mehvahdjukaar.amendments.reg;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.mehvahdjukaar.amendments.Amendments;
import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.mehvahdjukaar.amendments.common.LecternEditMenu;
import net.mehvahdjukaar.amendments.common.block.CarpetSlabBlock;
import net.mehvahdjukaar.amendments.common.block.CarpetStairBlock;
import net.mehvahdjukaar.amendments.common.block.CeilingBannerBlock;
import net.mehvahdjukaar.amendments.common.block.DirectionalCakeBlock;
import net.mehvahdjukaar.amendments.common.block.DoubleCakeBlock;
import net.mehvahdjukaar.amendments.common.block.DoubleSkullBlock;
import net.mehvahdjukaar.amendments.common.block.DyeCauldronBlock;
import net.mehvahdjukaar.amendments.common.block.FloorCandleSkullBlock;
import net.mehvahdjukaar.amendments.common.block.HangingFlowerPotBlock;
import net.mehvahdjukaar.amendments.common.block.LiquidCauldronBlock;
import net.mehvahdjukaar.amendments.common.block.ToolHookBlock;
import net.mehvahdjukaar.amendments.common.block.WallCandleSkullBlock;
import net.mehvahdjukaar.amendments.common.block.WallLanternBlock;
import net.mehvahdjukaar.amendments.common.block.WaterloggedLilyBlock;
import net.mehvahdjukaar.amendments.common.entity.FallingLanternEntity;
import net.mehvahdjukaar.amendments.common.item.DyeBottleItem;
import net.mehvahdjukaar.amendments.common.item.placement.WallLanternPlacement;
import net.mehvahdjukaar.amendments.common.recipe.DyeBottleRecipe;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.mehvahdjukaar.amendments.common.tile.CarpetedBlockTile;
import net.mehvahdjukaar.amendments.common.tile.CeilingBannerBlockTile;
import net.mehvahdjukaar.amendments.common.tile.DoubleSkullBlockTile;
import net.mehvahdjukaar.amendments.common.tile.HangingFlowerPotBlockTile;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.common.tile.ToolHookBlockTile;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.mehvahdjukaar.amendments.common.tile.WaterloggedLilyBlockTile;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.CompatObjects;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacementsAPI;
import net.mehvahdjukaar.moonlight.api.misc.DataObjectReference;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class ModRegistry {

    public static final DataObjectReference<DamageType> BOILING_DAMAGE = new DataObjectReference<>(Amendments.res("boiling"), Registries.DAMAGE_TYPE);

    public static final DataObjectReference<SoftFluid> DYE_SOFT_FLUID = new DataObjectReference<>(Amendments.res("dye"), SoftFluidRegistry.KEY);

    public static final RegSupplier<RecipeSerializer<DyeBottleRecipe>> DYE_BOTTLE_RECIPE = RegHelper.registerSpecialRecipe(Amendments.res("dye_bottle"), DyeBottleRecipe::new);

    public static final Supplier<MenuType<LecternEditMenu>> LECTERN_EDIT_MENU = RegHelper.registerMenuType(Amendments.res("lectern_edit"), LecternEditMenu::new);

    public static final RegSupplier<SimpleParticleType> BOILING_PARTICLE = RegHelper.registerParticle(Amendments.res("boiling_bubble"));

    public static final RegSupplier<SimpleParticleType> SPLASH_PARTICLE = RegHelper.registerParticle(Amendments.res("fluid_splash"));

    public static final Supplier<Item> DYE_BOTTLE_ITEM = regItem("dye_bottle", () -> new DyeBottleItem(new Item.Properties().stacksTo(1).craftRemainder(Items.GLASS_BOTTLE)));

    public static final Supplier<Block> WATERLILY_BLOCK = regBlock("water_lily_pad", () -> new WaterloggedLilyBlock(BlockBehaviour.Properties.copy(Blocks.LILY_PAD).instabreak().sound(SoundType.LILY_PAD).noOcclusion()));

    public static final Supplier<BlockEntityType<WaterloggedLilyBlockTile>> WATERLILY_TILE = regTile("water_lily_pad", () -> PlatHelper.newBlockEntityType(WaterloggedLilyBlockTile::new, (Block) WATERLILY_BLOCK.get()));

    public static final Supplier<LiquidCauldronBlock> LIQUID_CAULDRON = regBlock("liquid_cauldron", () -> new LiquidCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));

    public static final Supplier<Block> DYE_CAULDRON = regBlock("dye_cauldron", () -> new DyeCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON)));

    public static final Supplier<BlockEntityType<LiquidCauldronBlockTile>> LIQUID_CAULDRON_TILE = regTile("liquid_cauldron", () -> PlatHelper.newBlockEntityType(LiquidCauldronBlockTile::new, (Block) LIQUID_CAULDRON.get(), (Block) DYE_CAULDRON.get()));

    public static final Supplier<Block> HANGING_FLOWER_POT = regBlock("hanging_flower_pot", () -> new HangingFlowerPotBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

    public static final Supplier<BlockEntityType<HangingFlowerPotBlockTile>> HANGING_FLOWER_POT_TILE = regTile("hanging_flower_pot", () -> PlatHelper.newBlockEntityType(HangingFlowerPotBlockTile::new, (Block) HANGING_FLOWER_POT.get()));

    public static final Map<DyeColor, Supplier<Block>> CEILING_BANNERS = Util.make(() -> {
        Map<DyeColor, Supplier<Block>> map = new Object2ObjectLinkedOpenHashMap();
        for (DyeColor color : BlocksColorAPI.SORTED_COLORS) {
            String name = "ceiling_banner_" + color.getName();
            map.put(color, regBlock(name, () -> new CeilingBannerBlock(color, BlockBehaviour.Properties.of().ignitedByLava().forceSolidOn().mapColor(color.getMapColor()).strength(1.0F).noCollission().sound(SoundType.WOOD))));
        }
        return Collections.unmodifiableMap(map);
    });

    public static final Supplier<BlockEntityType<CeilingBannerBlockTile>> CEILING_BANNER_TILE = regTile("ceiling_banner", () -> PlatHelper.newBlockEntityType(CeilingBannerBlockTile::new, (Block[]) CEILING_BANNERS.values().stream().map(Supplier::get).toArray(Block[]::new)));

    public static final Supplier<Block> CARPET_STAIRS = regBlock("carpet_stairs", () -> new CarpetStairBlock(Blocks.OAK_STAIRS));

    public static final Supplier<Block> CARPET_SLAB = regBlock("carpet_slab", () -> new CarpetSlabBlock(Blocks.OAK_SLAB));

    public static final Supplier<BlockEntityType<CarpetedBlockTile>> CARPET_STAIRS_TILE = regTile("carpeted_block", () -> PlatHelper.newBlockEntityType(CarpetedBlockTile::new, (Block) CARPET_STAIRS.get(), (Block) CARPET_SLAB.get()));

    public static final Supplier<WallLanternBlock> WALL_LANTERN = regBlock("wall_lantern", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(Blocks.LANTERN).pushReaction(PushReaction.DESTROY).lightLevel(state -> 15).noLootTable();
        return new WallLanternBlock(p);
    });

    public static final Supplier<BlockEntityType<WallLanternBlockTile>> WALL_LANTERN_TILE = regTile("wall_lantern", () -> PlatHelper.newBlockEntityType(WallLanternBlockTile::new, (Block) WALL_LANTERN.get()));

    public static final Supplier<EntityType<FallingLanternEntity>> FALLING_LANTERN = regEntity("falling_lantern", () -> EntityType.Builder.of(FallingLanternEntity::new, MobCategory.MISC).sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20));

    public static final Supplier<ToolHookBlock> TOOL_HOOK = regBlock("tool_hook", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(Blocks.TRIPWIRE_HOOK).dropsLike(Blocks.TRIPWIRE_HOOK);
        return new ToolHookBlock(p);
    });

    public static final Supplier<BlockEntityType<ToolHookBlockTile>> TOOL_HOOK_TILE = regTile("tool_hook", () -> PlatHelper.newBlockEntityType(ToolHookBlockTile::new, (Block) TOOL_HOOK.get()));

    public static final Supplier<Block> SKULL_PILE = regBlock("skull_pile", () -> {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).sound(SoundType.BONE_BLOCK);
        return new DoubleSkullBlock(p);
    });

    public static final Supplier<BlockEntityType<DoubleSkullBlockTile>> SKULL_PILE_TILE = regTile("skull_pile", () -> PlatHelper.newBlockEntityType(DoubleSkullBlockTile::new, (Block) SKULL_PILE.get()));

    public static final Supplier<Block> SKULL_CANDLE = regBlock("skull_candle", () -> new FloorCandleSkullBlock(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).sound(SoundType.BONE_BLOCK)));

    public static final Supplier<Block> SKULL_CANDLE_WALL = regBlock("skull_candle_wall", () -> new WallCandleSkullBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SKULL_CANDLE.get())));

    public static final Supplier<Block> SKULL_CANDLE_SOUL = regBlock("skull_candle_soul", () -> new FloorCandleSkullBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SKULL_CANDLE.get()), CompatHandler.BUZZIER_BEES ? CompatObjects.SMALL_SOUL_FLAME : () -> ParticleTypes.SOUL_FIRE_FLAME));

    public static final Supplier<Block> SKULL_CANDLE_SOUL_WALL = regBlock("skull_candle_soul_wall", () -> new WallCandleSkullBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SKULL_CANDLE.get()), CompatHandler.BUZZIER_BEES ? CompatObjects.SMALL_SOUL_FLAME : () -> ParticleTypes.SOUL_FIRE_FLAME));

    public static final Supplier<BlockEntityType<CandleSkullBlockTile>> SKULL_CANDLE_TILE = regTile("skull_candle", () -> PlatHelper.newBlockEntityType(CandleSkullBlockTile::new, (Block) SKULL_CANDLE.get(), (Block) SKULL_CANDLE_WALL.get(), (Block) SKULL_CANDLE_SOUL.get(), (Block) SKULL_CANDLE_SOUL_WALL.get()));

    public static final Supplier<Block> DIRECTIONAL_CAKE = regBlock("directional_cake", () -> new DirectionalCakeBlock(CakeRegistry.VANILLA));

    public static final Map<CakeRegistry.CakeType, DoubleCakeBlock> DOUBLE_CAKES = new LinkedHashMap();

    public static void init() {
        BlockSetAPI.registerBlockSetDefinition(CakeRegistry.INSTANCE);
        BlockSetAPI.addDynamicBlockRegistration(ModRegistry::registerDoubleCakes, CakeRegistry.CakeType.class);
        AdditionalItemPlacementsAPI.addRegistration(ModRegistry::registerAdditionalPlacements);
    }

    public static void registerAdditionalPlacements(AdditionalItemPlacementsAPI.Event event) {
        WallLanternPlacement wallLanternPlacement = new WallLanternPlacement();
        for (Item i : BuiltInRegistries.ITEM) {
            if (i instanceof BlockItem) {
                BlockItem bi = (BlockItem) i;
                Block block = bi.getBlock();
                if ((Boolean) CommonConfigs.WALL_LANTERN.get() && WallLanternBlock.isValidBlock(block)) {
                    event.register(i, wallLanternPlacement);
                }
            }
        }
        if ((Boolean) CommonConfigs.HANGING_POT.get()) {
            event.registerSimple(Items.FLOWER_POT, (Block) HANGING_FLOWER_POT.get());
        }
        if ((Boolean) CommonConfigs.CEILING_BANNERS.get()) {
            for (Entry<DyeColor, Supplier<Block>> e : CEILING_BANNERS.entrySet()) {
                event.registerSimple((Item) Preconditions.checkNotNull(BannerBlock.byColor((DyeColor) e.getKey()).asItem()), (Block) ((Supplier) e.getValue()).get());
            }
        }
    }

    private static void registerDoubleCakes(Registrator<Block> event, Collection<CakeRegistry.CakeType> cakeTypes) {
        for (CakeRegistry.CakeType type : cakeTypes) {
            ResourceLocation id = Amendments.res(type.getVariantId("double"));
            DoubleCakeBlock block = new DoubleCakeBlock(type);
            type.addChild("double_cake", block);
            event.register(id, block);
            DOUBLE_CAKES.put(type, block);
        }
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> regTile(String name, Supplier<T> sup) {
        return RegHelper.registerBlockEntityType(Amendments.res(name), sup);
    }

    public static <T extends Block> RegSupplier<T> regBlock(String name, Supplier<T> sup) {
        return RegHelper.registerBlock(Amendments.res(name), sup);
    }

    public static <T extends Item> RegSupplier<T> regItem(String name, Supplier<T> sup) {
        return RegHelper.registerItem(Amendments.res(name), sup);
    }

    public static <T extends Entity> Supplier<EntityType<T>> regEntity(String name, Supplier<EntityType.Builder<T>> builder) {
        return RegHelper.registerEntityType(Amendments.res(name), () -> ((EntityType.Builder) builder.get()).build(name));
    }
}
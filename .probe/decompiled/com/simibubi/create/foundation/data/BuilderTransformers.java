package com.simibubi.create.foundation.data;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllInteractionBehaviours;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.TrapdoorMovingInteraction;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonGenerator;
import com.simibubi.create.content.decoration.MetalScaffoldingBlock;
import com.simibubi.create.content.decoration.MetalScaffoldingBlockItem;
import com.simibubi.create.content.decoration.MetalScaffoldingCTBehaviour;
import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogwheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedShaftBlock;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelBlock;
import com.simibubi.create.content.logistics.tunnel.BeltTunnelItem;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.StandardBogeyBlock;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.HorizontalCTBehaviour;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class BuilderTransformers {

    public static <B extends EncasedShaftBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedShaft(String casing, Supplier<CTSpriteShiftEntry> casingShift) {
        return builder -> (BlockBuilder) ((BlockBuilder) ((BlockBuilder) encasedBase(builder, () -> (ItemLike) AllBlocks.SHAFT.get()).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour((CTSpriteShiftEntry) casingShift.get())))).onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, (CTSpriteShiftEntry) casingShift.get(), (s, f) -> f.getAxis() != s.m_61143_(EncasedShaftBlock.AXIS))))).blockstate((c, p) -> BlockStateGen.axisBlock(c, p, blockState -> p.models().getExistingFile(p.modLoc("block/encased_shaft/block_" + casing)), true)).item().model(AssetLookup.customBlockItemModel("encased_shaft", "item_" + casing)).build();
    }

    public static <B extends StandardBogeyBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> bogey() {
        return b -> (BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::softMetal).properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).properties(p -> p.noOcclusion()).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> BlockStateGen.horizontalAxisBlock(c, p, s -> p.models().getExistingFile(p.modLoc("block/track/bogey/top")))).loot((p, l) -> p.m_246125_(l, (ItemLike) AllBlocks.RAILWAY_CASING.get())).onRegister(block -> AbstractBogeyBlock.registerStandardBogey(RegisteredObjects.getKeyOrThrow(block)));
    }

    public static <B extends CopycatBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycat() {
        return b -> (BlockBuilder) b.initialProperties(SharedProperties::softMetal).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().getExistingFile(p.mcLoc("air")))).initialProperties(SharedProperties::softMetal).properties(p -> p.noOcclusion().mapColor(MapColor.NONE)).addLayer(() -> RenderType::m_110451_).addLayer(() -> RenderType::m_110463_).addLayer(() -> RenderType::m_110457_).addLayer(() -> RenderType::m_110466_).color(() -> CopycatBlock::wrappedColor).transform(TagGen.axeOrPickaxe());
    }

    public static <B extends TrapDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> trapdoor(boolean orientable) {
        return b -> (BlockBuilder) ((BlockBuilder) ((BlockBuilder) b.blockstate((c, p) -> {
            ModelFile bottom = AssetLookup.partialBaseModel(c, p, "bottom");
            ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
            ModelFile open = AssetLookup.partialBaseModel(c, p, "open");
            if (orientable) {
                p.trapdoorBlock((TrapDoorBlock) c.get(), bottom, top, open, orientable);
            } else {
                BlockStateGen.uvLockedTrapdoorBlock((P) ((TrapDoorBlock) c.get()), bottom, top, open).accept(c, p);
            }
        }).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { BlockTags.TRAPDOORS }).onRegister(AllInteractionBehaviours.interactionBehaviour(new TrapdoorMovingInteraction()))).item().tag(new TagKey[] { ItemTags.TRAPDOORS }).build();
    }

    public static <B extends SlidingDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> slidingDoor(String type) {
        return b -> (BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) b.initialProperties(() -> Blocks.IRON_DOOR).properties(p -> p.requiresCorrectToolForDrops().strength(3.0F, 6.0F)).blockstate((c, p) -> {
            ModelFile bottom = AssetLookup.partialBaseModel(c, p, "bottom");
            ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
            p.doorBlock((DoorBlock) c.get(), bottom, bottom, bottom, bottom, top, top, top, top);
        }).addLayer(() -> RenderType::m_110457_).transform(TagGen.pickaxeOnly())).onRegister(AllInteractionBehaviours.interactionBehaviour(new DoorMovingInteraction()))).onRegister(AllMovementBehaviours.movementBehaviour(new SlidingDoorMovementBehaviour()))).tag(new TagKey[] { BlockTags.DOORS }).tag(new TagKey[] { BlockTags.WOODEN_DOORS }).tag(new TagKey[] { AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag }).loot((lr, block) -> lr.m_247577_(block, lr.m_247398_(block))).item().tag(new TagKey[] { ItemTags.DOORS }).tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).model((c, p) -> p.blockSprite(c, p.modLoc("item/" + type + "_door"))).build();
    }

    public static <B extends EncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedCogwheel(String casing, Supplier<CTSpriteShiftEntry> casingShift) {
        return b -> encasedCogwheelBase(b, casing, casingShift, () -> (ItemLike) AllBlocks.COGWHEEL.get(), false);
    }

    public static <B extends EncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedLargeCogwheel(String casing, Supplier<CTSpriteShiftEntry> casingShift) {
        return b -> (BlockBuilder) encasedCogwheelBase(b, casing, casingShift, () -> (ItemLike) AllBlocks.LARGE_COGWHEEL.get(), true).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour((CTSpriteShiftEntry) casingShift.get())));
    }

    private static <B extends EncasedCogwheelBlock, P> BlockBuilder<B, P> encasedCogwheelBase(BlockBuilder<B, P> b, String casing, Supplier<CTSpriteShiftEntry> casingShift, Supplier<ItemLike> drop, boolean large) {
        String encasedSuffix = "_encased_cogwheel_side" + (large ? "_connected" : "");
        String blockFolder = large ? "encased_large_cogwheel" : "encased_cogwheel";
        String wood = casing.equals("brass") ? "dark_oak" : "spruce";
        String gearbox = casing.equals("brass") ? "brass_gearbox" : "gearbox";
        return (BlockBuilder<B, P>) ((BlockBuilder) encasedBase(b, drop).addLayer(() -> RenderType::m_110457_).onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, (CTSpriteShiftEntry) casingShift.get(), (s, f) -> f.getAxis() == s.m_61143_(EncasedCogwheelBlock.AXIS) && !(Boolean) s.m_61143_(f.getAxisDirection() == Direction.AxisDirection.POSITIVE ? EncasedCogwheelBlock.TOP_SHAFT : EncasedCogwheelBlock.BOTTOM_SHAFT))))).blockstate((c, p) -> BlockStateGen.axisBlock(c, p, blockState -> {
            String suffix = (blockState.m_61143_(EncasedCogwheelBlock.TOP_SHAFT) ? "_top" : "") + (blockState.m_61143_(EncasedCogwheelBlock.BOTTOM_SHAFT) ? "_bottom" : "");
            String modelName = c.getName() + suffix;
            return p.models().withExistingParent(modelName, p.modLoc("block/" + blockFolder + "/block" + suffix)).texture("casing", Create.asResource("block/" + casing + "_casing")).texture("particle", Create.asResource("block/" + casing + "_casing")).texture("4", Create.asResource("block/" + gearbox)).texture("1", new ResourceLocation("block/stripped_" + wood + "_log_top")).texture("side", Create.asResource("block/" + casing + encasedSuffix));
        }, false)).item().model((c, p) -> ((ItemModelBuilder) p.withExistingParent(c.getName(), p.modLoc("block/" + blockFolder + "/item"))).texture("casing", Create.asResource("block/" + casing + "_casing")).texture("particle", Create.asResource("block/" + casing + "_casing")).texture("1", new ResourceLocation("block/stripped_" + wood + "_log_top")).texture("side", Create.asResource("block/" + casing + encasedSuffix))).build();
    }

    private static <B extends RotatedPillarKineticBlock, P> BlockBuilder<B, P> encasedBase(BlockBuilder<B, P> b, Supplier<ItemLike> drop) {
        return ((BlockBuilder) b.initialProperties(SharedProperties::stone).properties(BlockBehaviour.Properties::m_60955_).transform(BlockStressDefaults.setNoImpact())).loot((p, lb) -> p.m_246125_(lb, (ItemLike) drop.get()));
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> cuckooClock() {
        return b -> (BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::wooden).blockstate((c, p) -> p.horizontalBlock((Block) c.get(), p.models().getExistingFile(p.modLoc("block/cuckoo_clock/block")))).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setImpact(1.0))).item().transform(ModelGen.customItemModel("cuckoo_clock", "item"));
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> ladder(String name, Supplier<DataIngredient> ingredient, MapColor color) {
        return b -> (BlockBuilder) ((BlockBuilder) b.initialProperties(() -> Blocks.LADDER).properties(p -> p.mapColor(color)).addLayer(() -> RenderType::m_110463_).blockstate((c, p) -> p.horizontalBlock((Block) c.get(), p.models().withExistingParent(c.getName(), p.modLoc("block/ladder")).texture("0", p.modLoc("block/ladder_" + name + "_hoop")).texture("1", p.modLoc("block/ladder_" + name)).texture("particle", p.modLoc("block/ladder_" + name)))).properties(p -> p.sound(SoundType.COPPER)).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { BlockTags.CLIMBABLE }).item().recipe((c, p) -> p.stonecutting((DataIngredient) ingredient.get(), RecipeCategory.DECORATIONS, c::get, 2)).model((c, p) -> p.blockSprite(c::get, p.modLoc("block/ladder_" + name))).build();
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> scaffold(String name, Supplier<DataIngredient> ingredient, MapColor color, CTSpriteShiftEntry scaffoldShift, CTSpriteShiftEntry scaffoldInsideShift, CTSpriteShiftEntry casingShift) {
        return b -> (BlockBuilder) ((BlockBuilder) ((BlockBuilder) b.initialProperties(() -> Blocks.SCAFFOLDING).properties(p -> p.sound(SoundType.COPPER).mapColor(color)).addLayer(() -> RenderType::m_110463_).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStatesExcept(s -> {
            String suffix = s.m_61143_(MetalScaffoldingBlock.f_56014_) ? "_horizontal" : "";
            return ConfiguredModel.builder().modelFile(p.models().withExistingParent(c.getName() + suffix, p.modLoc("block/scaffold/block" + suffix)).texture("top", p.modLoc("block/funnel/" + name + "_funnel_frame")).texture("inside", p.modLoc("block/scaffold/" + name + "_scaffold_inside")).texture("side", p.modLoc("block/scaffold/" + name + "_scaffold")).texture("casing", p.modLoc("block/" + name + "_casing")).texture("particle", p.modLoc("block/scaffold/" + name + "_scaffold"))).build();
        }, MetalScaffoldingBlock.f_56013_, MetalScaffoldingBlock.f_56012_)).onRegister(CreateRegistrate.connectedTextures(() -> new MetalScaffoldingCTBehaviour(scaffoldShift, scaffoldInsideShift, casingShift)))).transform(TagGen.pickaxeOnly())).tag(new TagKey[] { BlockTags.CLIMBABLE }).item(MetalScaffoldingBlockItem::new).recipe((c, p) -> p.stonecutting((DataIngredient) ingredient.get(), RecipeCategory.DECORATIONS, c::get, 2)).model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/" + c.getName()))).build();
    }

    public static <B extends ValveHandleBlock> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> valveHandle(@Nullable DyeColor color) {
        return b -> (BlockBuilder) ((BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::copperMetal).blockstate((c, p) -> {
            String variant = color == null ? "copper" : color.getSerializedName();
            p.directionalBlock((Block) c.get(), p.models().withExistingParent(variant + "_valve_handle", p.modLoc("block/valve_handle")).texture("3", p.modLoc("block/valve_handle/valve_handle_" + variant)));
        }).tag(new TagKey[] { AllTags.AllBlockTags.BRITTLE.tag, AllTags.AllBlockTags.VALVE_HANDLES.tag }).transform(BlockStressDefaults.setGeneratorSpeed(ValveHandleBlock::getSpeedRange))).onRegister(ItemUseOverrides::addBlock)).item().tag(new TagKey[] { AllTags.AllItemTags.VALVE_HANDLES.tag }).build();
    }

    public static <B extends CasingBlock> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> casing(Supplier<CTSpriteShiftEntry> ct) {
        return b -> (BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::stone).properties(p -> p.sound(SoundType.WOOD)).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.simpleBlock((Block) c.get())).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour((CTSpriteShiftEntry) ct.get())))).onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.makeCasing(block, (CTSpriteShiftEntry) ct.get())))).tag(new TagKey[] { AllTags.AllBlockTags.CASING.tag }).item().tag(new TagKey[] { AllTags.AllItemTags.CASING.tag }).build();
    }

    public static <B extends CasingBlock> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> layeredCasing(Supplier<CTSpriteShiftEntry> ct, Supplier<CTSpriteShiftEntry> ct2) {
        return b -> (BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::stone).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> p.simpleBlock((Block) c.get(), p.models().cubeColumn(c.getName(), ((CTSpriteShiftEntry) ct.get()).getOriginalResourceLocation(), ((CTSpriteShiftEntry) ct2.get()).getOriginalResourceLocation()))).onRegister(CreateRegistrate.connectedTextures(() -> new HorizontalCTBehaviour((CTSpriteShiftEntry) ct.get(), (CTSpriteShiftEntry) ct2.get())))).onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.makeCasing(block, (CTSpriteShiftEntry) ct.get())))).tag(new TagKey[] { AllTags.AllBlockTags.CASING.tag }).item().tag(new TagKey[] { AllTags.AllItemTags.CASING.tag }).build();
    }

    public static <B extends BeltTunnelBlock> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> beltTunnel(String type, ResourceLocation particleTexture) {
        String prefix = "block/tunnel/" + type + "_tunnel";
        String funnel_prefix = "block/funnel/" + type + "_funnel";
        return b -> (BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::stone).addLayer(() -> RenderType::m_110457_).properties(BlockBehaviour.Properties::m_60955_).transform(TagGen.pickaxeOnly())).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStates(state -> {
            BeltTunnelBlock.Shape shape = (BeltTunnelBlock.Shape) state.m_61143_(BeltTunnelBlock.SHAPE);
            String window = shape == BeltTunnelBlock.Shape.WINDOW ? "_window" : "";
            if (shape == BeltTunnelBlock.Shape.CLOSED) {
                shape = BeltTunnelBlock.Shape.STRAIGHT;
            }
            String shapeName = shape.getSerializedName();
            return ConfiguredModel.builder().modelFile(p.models().withExistingParent(prefix + "/" + shapeName, p.modLoc("block/belt_tunnel/" + shapeName)).texture("top", p.modLoc(prefix + "_top" + window)).texture("tunnel", p.modLoc(prefix)).texture("direction", p.modLoc(funnel_prefix + "_neutral")).texture("frame", p.modLoc(funnel_prefix + "_frame")).texture("particle", particleTexture)).rotationY(state.m_61143_(BeltTunnelBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? 0 : 90).build();
        })).item(BeltTunnelItem::new).model((c, p) -> ((ItemModelBuilder) p.withExistingParent("item/" + type + "_tunnel", p.modLoc("block/belt_tunnel/item"))).texture("top", p.modLoc(prefix + "_top")).texture("tunnel", p.modLoc(prefix)).texture("direction", p.modLoc(funnel_prefix + "_neutral")).texture("frame", p.modLoc(funnel_prefix + "_frame")).texture("particle", particleTexture)).build();
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> mechanicalPiston(PistonType type) {
        return b -> (BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion()).blockstate(new MechanicalPistonGenerator(type)::generate).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setImpact(4.0))).item().transform(ModelGen.customItemModel("mechanical_piston", type.getSerializedName(), "item"));
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> bearing(String prefix, String backTexture) {
        ResourceLocation baseBlockModelLocation = Create.asResource("block/bearing/block");
        ResourceLocation baseItemModelLocation = Create.asResource("block/bearing/item");
        ResourceLocation topTextureLocation = Create.asResource("block/bearing_top");
        ResourceLocation sideTextureLocation = Create.asResource("block/" + prefix + "_bearing_side");
        ResourceLocation backTextureLocation = Create.asResource("block/" + backTexture);
        return b -> (BlockBuilder) b.initialProperties(SharedProperties::stone).properties(p -> p.noOcclusion()).blockstate((c, p) -> p.directionalBlock((Block) c.get(), p.models().withExistingParent(c.getName(), baseBlockModelLocation).texture("side", sideTextureLocation).texture("back", backTextureLocation))).item().model((c, p) -> ((ItemModelBuilder) p.withExistingParent(c.getName(), baseItemModelLocation)).texture("top", topTextureLocation).texture("side", sideTextureLocation).texture("back", backTextureLocation)).build();
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> crate(String type) {
        return b -> (BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::stone).transform(TagGen.axeOrPickaxe())).blockstate((c, p) -> {
            String[] variants = new String[] { "single", "top", "bottom", "left", "right" };
            Map<String, ModelFile> models = new HashMap();
            ResourceLocation crate = p.modLoc("block/crate_" + type);
            ResourceLocation side = p.modLoc("block/crate_" + type + "_side");
            ResourceLocation casing = p.modLoc("block/" + type + "_casing");
            for (String variant : variants) {
                models.put(variant, p.models().withExistingParent("block/crate/" + type + "/" + variant, p.modLoc("block/crate/" + variant)).texture("crate", crate).texture("side", side).texture("casing", casing));
            }
            p.getVariantBuilder((Block) c.get()).forAllStates(state -> {
                String variantx = "single";
                return ConfiguredModel.builder().modelFile((ModelFile) models.get(variantx)).build();
            });
        }).item().properties(p -> type.equals("creative") ? p.rarity(Rarity.EPIC) : p).transform(ModelGen.customItemModel("crate", type, "single"));
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> backtank(Supplier<ItemLike> drop) {
        return b -> ((BlockBuilder) ((BlockBuilder) b.blockstate((c, p) -> p.horizontalBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).transform(TagGen.pickaxeOnly())).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setImpact(4.0))).loot((lt, block) -> {
            LootTable.Builder builder = LootTable.lootTable();
            LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
            lt.m_247577_(block, builder.withPool(LootPool.lootPool().when(survivesExplosion).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem((ItemLike) drop.get()).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Air", "Air")).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Enchantments", "Enchantments")))));
        });
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> bell() {
        return b -> (BlockBuilder) ((BlockBuilder) b.initialProperties(SharedProperties::softMetal).properties(p -> p.noOcclusion().sound(SoundType.ANVIL)).transform(TagGen.pickaxeOnly())).addLayer(() -> RenderType::m_110457_).tag(new TagKey[] { AllTags.AllBlockTags.BRITTLE.tag }).blockstate((c, p) -> p.horizontalBlock((Block) c.getEntry(), state -> {
            String variant = ((BellAttachType) state.m_61143_(BlockStateProperties.BELL_ATTACHMENT)).getSerializedName();
            return p.models().withExistingParent(c.getName() + "_" + variant, p.modLoc("block/bell_base/block_" + variant));
        })).item().model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/" + c.getName()))).tag(new TagKey[] { AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag }).build();
    }
}
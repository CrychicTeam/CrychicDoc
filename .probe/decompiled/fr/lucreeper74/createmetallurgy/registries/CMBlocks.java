package fr.lucreeper74.createmetallurgy.registries;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.processing.basin.BasinGenerator;
import com.simibubi.create.content.processing.basin.BasinMovementBehaviour;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.UncontainableBlockItem;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import fr.lucreeper74.createmetallurgy.content.belt_grinder.BeltGrinderBlock;
import fr.lucreeper74.createmetallurgy.content.belt_grinder.BeltGrinderGenerator;
import fr.lucreeper74.createmetallurgy.content.casting.basin.CastingBasinBlock;
import fr.lucreeper74.createmetallurgy.content.casting.basin.CastingBasinMovementBehaviour;
import fr.lucreeper74.createmetallurgy.content.casting.table.CastingTableBlock;
import fr.lucreeper74.createmetallurgy.content.casting.table.CastingTableMovementBehaviour;
import fr.lucreeper74.createmetallurgy.content.foundry_basin.FoundryBasinBlock;
import fr.lucreeper74.createmetallurgy.content.foundry_lid.FoundryLidBlock;
import fr.lucreeper74.createmetallurgy.content.foundry_lid.FoundryLidGenerator;
import fr.lucreeper74.createmetallurgy.content.foundry_mixer.FoundryMixerBlock;
import fr.lucreeper74.createmetallurgy.content.glassed_foundry_lid.GlassedFoundryLidBlock;
import fr.lucreeper74.createmetallurgy.content.glassed_foundry_lid.GlassedFoundryLidGenerator;
import fr.lucreeper74.createmetallurgy.content.light_bulb.LightBulbBlock;
import fr.lucreeper74.createmetallurgy.utils.CMDyeHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.Tags;

public class CMBlocks {

    public static final BlockEntry<Block> RAW_WOLFRAMITE_BLOCK = ((BlockBuilder) ((ItemBuilder) CreateMetallurgy.REGISTRATE.block("raw_wolframite_block", Block::new).initialProperties(() -> Blocks.RAW_COPPER_BLOCK).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).tag(new TagKey[] { BlockTags.NEEDS_DIAMOND_TOOL }).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).transform(TagGen.tagBlockAndItem("storage_blocks/raw_wolframite"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).register();

    public static final BlockEntry<Block> TUNGSTEN_BLOCK = ((BlockBuilder) ((ItemBuilder) CreateMetallurgy.REGISTRATE.block("tungsten_block", Block::new).initialProperties(() -> Blocks.EMERALD_BLOCK).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).tag(new TagKey[] { BlockTags.NEEDS_DIAMOND_TOOL }).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).transform(TagGen.tagBlockAndItem("storage_blocks/tungsten"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).register();

    public static final BlockEntry<Block> WOLFRAMITE_ORE = ((BlockBuilder) ((ItemBuilder) CreateMetallurgy.REGISTRATE.block("wolframite_ore", Block::new).initialProperties(() -> Blocks.COPPER_ORE).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).tag(new TagKey[] { BlockTags.NEEDS_DIAMOND_TOOL }).tag(new TagKey[] { Tags.Blocks.ORES }).transform(TagGen.tagBlockAndItem("ores/wolframite"))).tag(new TagKey[] { Tags.Items.ORES }).build()).register();

    public static final BlockEntry<Block> COKE_BLOCK = ((BlockBuilder) ((ItemBuilder) CreateMetallurgy.REGISTRATE.block("coke_block", Block::new).initialProperties(() -> Blocks.COAL_BLOCK).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).tag(new TagKey[] { BlockTags.NEEDS_STONE_TOOL }).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).transform(TagGen.tagBlockAndItem("storage_blocks/steel"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).register();

    public static final BlockEntry<Block> STEEL_BLOCK = ((BlockBuilder) ((ItemBuilder) CreateMetallurgy.REGISTRATE.block("steel_block", Block::new).initialProperties(() -> Blocks.IRON_BLOCK).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).tag(new TagKey[] { BlockTags.NEEDS_DIAMOND_TOOL }).tag(new TagKey[] { Tags.Blocks.STORAGE_BLOCKS }).transform(TagGen.tagBlockAndItem("storage_blocks/steel"))).tag(new TagKey[] { Tags.Items.STORAGE_BLOCKS }).build()).register();

    public static final BlockEntry<Block> REFRACTORY_MORTAR = CreateMetallurgy.REGISTRATE.block("refractory_mortar", Block::new).initialProperties(() -> Blocks.CLAY).tag(new TagKey[] { BlockTags.MINEABLE_WITH_SHOVEL }).simpleItem().register();

    public static final BlockEntry<FoundryBasinBlock> FOUNDRY_BASIN_BLOCK = ((BlockBuilder) ((BlockBuilder) CreateMetallurgy.REGISTRATE.block("foundry_basin", FoundryBasinBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).blockstate(new BasinGenerator()::generate).addLayer(() -> RenderType::m_110457_).onRegister(AllMovementBehaviours.movementBehaviour(new BasinMovementBehaviour()))).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<CastingBasinBlock> CASTING_BASIN_BLOCK = ((BlockBuilder) ((BlockBuilder) CreateMetallurgy.REGISTRATE.block("casting_basin", CastingBasinBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).addLayer(() -> RenderType::m_110457_).onRegister(AllMovementBehaviours.movementBehaviour(new CastingBasinMovementBehaviour()))).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<CastingTableBlock> CASTING_TABLE_BLOCK = ((BlockBuilder) ((BlockBuilder) CreateMetallurgy.REGISTRATE.block("casting_table", CastingTableBlock::new).initialProperties(SharedProperties::stone).properties(BlockBehaviour.Properties::m_60955_).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).addLayer(() -> RenderType::m_110457_).onRegister(AllMovementBehaviours.movementBehaviour(new CastingTableMovementBehaviour()))).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<FoundryLidBlock> FOUNDRY_LID_BLOCK = ((BlockBuilder) CreateMetallurgy.REGISTRATE.block("foundry_lid", FoundryLidBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).blockstate(new FoundryLidGenerator()::generate).addLayer(() -> RenderType::m_110457_).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<GlassedFoundryLidBlock> GLASSED_FOUNDRY_LID_BLOCK = ((BlockBuilder) CreateMetallurgy.REGISTRATE.block("glassed_foundry_lid", GlassedFoundryLidBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.COLOR_GRAY)).properties(p -> p.sound(SoundType.NETHERITE_BLOCK)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).blockstate(new GlassedFoundryLidGenerator()::generate).addLayer(() -> RenderType::m_110457_).item().transform(ModelGen.customItemModel("_", "block"))).register();

    public static final BlockEntry<FoundryMixerBlock> FOUNDRY_MIXER_BLOCK = ((BlockBuilder) ((BlockBuilder) CreateMetallurgy.REGISTRATE.block("foundry_mixer", FoundryMixerBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.STONE)).properties(BlockBehaviour.Properties::m_60955_).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).blockstate((c, p) -> p.simpleBlock((Block) c.getEntry(), AssetLookup.partialBaseModel(c, p))).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setImpact(8.0))).item().transform(ModelGen.customItemModel("foundry_mixer", "item"))).register();

    public static final BlockEntry<BeltGrinderBlock> BELT_GRINDER_BLOCK = ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) CreateMetallurgy.REGISTRATE.block("mechanical_belt_grinder", BeltGrinderBlock::new).initialProperties(SharedProperties::stone).properties(p -> p.mapColor(MapColor.STONE)).properties(BlockBehaviour.Properties::m_60955_).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).blockstate(new BeltGrinderGenerator()::generate).addLayer(() -> RenderType::m_110457_).transform(BlockStressDefaults.setImpact(6.0))).transform(TagGen.axeOrPickaxe())).onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.createmetallurgy.mechanical_grinder"))).item().transform(ModelGen.customItemModel("mechanical_belt_grinder", "item"))).register();

    public static final DyedBlockList<LightBulbBlock> LIGHT_BULBS = new DyedBlockList<>(color -> {
        String colorName = color.getSerializedName();
        return ((BlockBuilder) ((BlockBuilder) ((BlockBuilder) CreateMetallurgy.REGISTRATE.block(colorName + "_light_bulb", p -> new LightBulbBlock(p, color)).initialProperties(() -> Blocks.REDSTONE_LAMP).properties(p -> p.sound(SoundType.GLASS).mapColor(color).lightLevel(s -> (Integer) s.m_61143_(LightBulbBlock.LEVEL))).addLayer(() -> RenderType::m_110466_).transform(TagGen.axeOrPickaxe())).tag(new TagKey[] { AllTags.forgeBlockTag("light_bulbs") }).blockstate((c, p) -> p.getVariantBuilder((Block) c.get()).forAllStates(state -> {
            Direction dir = (Direction) state.m_61143_(LightBulbBlock.f_52588_);
            String path = "block/light_bulb/";
            return ConfiguredModel.builder().modelFile(p.models().withExistingParent(path + "tube/" + colorName, p.modLoc(path + "tube")).texture("0", p.modLoc(path + colorName))).modelFile(p.models().withExistingParent(path + "tube_glow/" + colorName, p.modLoc(path + "tube_glow")).texture("0", p.modLoc(path + colorName))).modelFile(p.models().withExistingParent(path + "block/" + colorName, p.modLoc(path + "block")).texture("0", p.modLoc(path + colorName)).texture("particle", p.modLoc(path + colorName))).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + 180) % 360).build();
        })).recipe((c, p) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, (ItemLike) c.get()).define('S', (ItemLike) AllItems.IRON_SHEET.get()).define('T', (ItemLike) CMItems.TUNGSTEN_WIRE_SPOOL.get()).define('G', CMDyeHelper.getGlassOfDye(color)).pattern(" G ").pattern(" T ").pattern(" S ").unlockedBy("has_tungsten_wire_spool", RegistrateRecipeProvider.m_125977_((ItemLike) CMItems.TUNGSTEN_WIRE_SPOOL.get())).save(p, CreateMetallurgy.genRL("crafting/" + c.getName()));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, (ItemLike) c.get()).requires(color.getTag()).requires(AllTags.forgeItemTag("light_bulbs")).unlockedBy("has_light_bulb", RegistrateRecipeProvider.m_206406_(AllTags.forgeItemTag("light_bulbs"))).save(p, CreateMetallurgy.genRL("crafting/" + c.getName() + "_from_other_light_bulb"));
        }).onRegisterAfter(Registries.ITEM, v -> ItemDescription.useKey(v, "block.createmetallurgy.light_bulb"))).item(UncontainableBlockItem::new).tag(new TagKey[] { AllTags.forgeItemTag("light_bulbs") }).model((c, p) -> ((ItemModelBuilder) p.withExistingParent(colorName + "_light_bulb", p.modLoc("block/light_bulb/item"))).texture("0", p.modLoc("block/light_bulb/" + colorName))).build()).register();
    });

    public static void register() {
    }

    static {
        CreateMetallurgy.REGISTRATE.setCreativeTab(CMCreativeTabs.MAIN_CREATIVE_TAB);
    }
}
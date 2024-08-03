package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.BasketBlock;
import vectorwing.farmersdelight.common.block.BuddingTomatoBlock;
import vectorwing.farmersdelight.common.block.CabbageBlock;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.block.OnionBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class BlockStates extends BlockStateProvider {

    private static final int DEFAULT_ANGLE_OFFSET = 180;

    public BlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "farmersdelight", existingFileHelper);
    }

    private String blockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    public ResourceLocation resourceBlock(String path) {
        return new ResourceLocation("farmersdelight", "block/" + path);
    }

    public ModelFile existingModel(Block block) {
        return new ModelFile.ExistingModelFile(this.resourceBlock(this.blockName(block)), this.models().existingFileHelper);
    }

    public ModelFile existingModel(String path) {
        return new ModelFile.ExistingModelFile(this.resourceBlock(path), this.models().existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(ModBlocks.RICH_SOIL.get(), this.cubeRandomRotation(ModBlocks.RICH_SOIL.get(), ""));
        this.simpleBlock(ModBlocks.SAFETY_NET.get(), this.existingModel(ModBlocks.SAFETY_NET.get()));
        for (Block sign : Sets.newHashSet(new Block[] { ModBlocks.CANVAS_SIGN.get(), ModBlocks.HANGING_CANVAS_SIGN.get(), ModBlocks.WHITE_CANVAS_SIGN.get(), ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(), ModBlocks.ORANGE_CANVAS_SIGN.get(), ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(), ModBlocks.MAGENTA_CANVAS_SIGN.get(), ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(), ModBlocks.YELLOW_CANVAS_SIGN.get(), ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(), ModBlocks.LIME_CANVAS_SIGN.get(), ModBlocks.LIME_HANGING_CANVAS_SIGN.get(), ModBlocks.PINK_CANVAS_SIGN.get(), ModBlocks.PINK_HANGING_CANVAS_SIGN.get(), ModBlocks.GRAY_CANVAS_SIGN.get(), ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(), ModBlocks.CYAN_CANVAS_SIGN.get(), ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(), ModBlocks.PURPLE_CANVAS_SIGN.get(), ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(), ModBlocks.BLUE_CANVAS_SIGN.get(), ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(), ModBlocks.BROWN_CANVAS_SIGN.get(), ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(), ModBlocks.GREEN_CANVAS_SIGN.get(), ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(), ModBlocks.RED_CANVAS_SIGN.get(), ModBlocks.RED_HANGING_CANVAS_SIGN.get(), ModBlocks.BLACK_CANVAS_SIGN.get(), ModBlocks.BLACK_HANGING_CANVAS_SIGN.get(), ModBlocks.CANVAS_WALL_SIGN.get(), ModBlocks.HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.WHITE_CANVAS_WALL_SIGN.get(), ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.ORANGE_CANVAS_WALL_SIGN.get(), ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get(), ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get(), ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.YELLOW_CANVAS_WALL_SIGN.get(), ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.LIME_CANVAS_WALL_SIGN.get(), ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.PINK_CANVAS_WALL_SIGN.get(), ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.GRAY_CANVAS_WALL_SIGN.get(), ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get(), ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.CYAN_CANVAS_WALL_SIGN.get(), ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.PURPLE_CANVAS_WALL_SIGN.get(), ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.BLUE_CANVAS_WALL_SIGN.get(), ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.BROWN_CANVAS_WALL_SIGN.get(), ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.GREEN_CANVAS_WALL_SIGN.get(), ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.RED_CANVAS_WALL_SIGN.get(), ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(), ModBlocks.BLACK_CANVAS_WALL_SIGN.get(), ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get() })) {
            this.simpleBlock(sign, this.existingModel(ModBlocks.CANVAS_SIGN.get()));
        }
        String riceBag = this.blockName(ModBlocks.RICE_BAG.get());
        this.simpleBlock(ModBlocks.RICE_BAG.get(), this.models().withExistingParent(riceBag, "cube").texture("particle", this.resourceBlock(riceBag + "_top")).texture("down", this.resourceBlock(riceBag + "_bottom")).texture("up", this.resourceBlock(riceBag + "_top")).texture("north", this.resourceBlock(riceBag + "_side_tied")).texture("south", this.resourceBlock(riceBag + "_side_tied")).texture("east", this.resourceBlock(riceBag + "_side")).texture("west", this.resourceBlock(riceBag + "_side")));
        this.customDirectionalBlock(ModBlocks.BASKET.get(), $ -> this.existingModel(ModBlocks.BASKET.get()), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
        this.customDirectionalBlock(ModBlocks.RICE_BALE.get(), $ -> this.existingModel(ModBlocks.RICE_BALE.get()));
        this.customHorizontalBlock(ModBlocks.CUTTING_BOARD.get(), $ -> this.existingModel(ModBlocks.CUTTING_BOARD.get()), BasketBlock.WATERLOGGED);
        this.horizontalBlock(ModBlocks.HALF_TATAMI_MAT.get(), this.existingModel("tatami_mat_half"));
        this.horizontalBlock(ModBlocks.STOVE.get(), state -> {
            String name = this.blockName(ModBlocks.STOVE.get());
            String suffix = state.m_61143_(StoveBlock.LIT) ? "_on" : "";
            return this.models().orientableWithBottom(name + suffix, this.resourceBlock(name + "_side"), this.resourceBlock(name + "_front" + suffix), this.resourceBlock(name + "_bottom"), this.resourceBlock(name + "_top" + suffix));
        });
        this.stageBlock(ModBlocks.BROWN_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
        this.stageBlock(ModBlocks.RED_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
        this.stageBlock(ModBlocks.RICE_CROP_PANICLES.get(), RicePaniclesBlock.RICE_AGE);
        this.customStageBlock(ModBlocks.CABBAGE_CROP.get(), this.resourceBlock("crop_cross"), "cross", CabbageBlock.f_52244_, new ArrayList());
        this.customStageBlock(ModBlocks.ONION_CROP.get(), this.mcLoc("crop"), "crop", OnionBlock.f_52244_, Arrays.asList(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageBlock(ModBlocks.BUDDING_TOMATO_CROP.get(), this.resourceBlock("crop_cross"), "cross", BuddingTomatoBlock.AGE, Arrays.asList(0, 1, 2, 3, 3));
        this.crateBlock(ModBlocks.CARROT_CRATE.get(), "carrot");
        this.crateBlock(ModBlocks.POTATO_CRATE.get(), "potato");
        this.crateBlock(ModBlocks.BEETROOT_CRATE.get(), "beetroot");
        this.crateBlock(ModBlocks.CABBAGE_CRATE.get(), "cabbage");
        this.crateBlock(ModBlocks.TOMATO_CRATE.get(), "tomato");
        this.crateBlock(ModBlocks.ONION_CRATE.get(), "onion");
        this.axisBlock((RotatedPillarBlock) ModBlocks.STRAW_BALE.get());
        this.cabinetBlock(ModBlocks.OAK_CABINET.get(), "oak");
        this.cabinetBlock(ModBlocks.BIRCH_CABINET.get(), "birch");
        this.cabinetBlock(ModBlocks.SPRUCE_CABINET.get(), "spruce");
        this.cabinetBlock(ModBlocks.JUNGLE_CABINET.get(), "jungle");
        this.cabinetBlock(ModBlocks.ACACIA_CABINET.get(), "acacia");
        this.cabinetBlock(ModBlocks.DARK_OAK_CABINET.get(), "dark_oak");
        this.cabinetBlock(ModBlocks.MANGROVE_CABINET.get(), "mangrove");
        this.cabinetBlock(ModBlocks.CHERRY_CABINET.get(), "cherry");
        this.cabinetBlock(ModBlocks.BAMBOO_CABINET.get(), "bamboo");
        this.cabinetBlock(ModBlocks.CRIMSON_CABINET.get(), "crimson");
        this.cabinetBlock(ModBlocks.WARPED_CABINET.get(), "warped");
        this.pieBlock(ModBlocks.APPLE_PIE.get());
        this.pieBlock(ModBlocks.CHOCOLATE_PIE.get());
        this.pieBlock(ModBlocks.SWEET_BERRY_CHEESECAKE.get());
        this.feastBlock((FeastBlock) ModBlocks.STUFFED_PUMPKIN_BLOCK.get());
        this.feastBlock((FeastBlock) ModBlocks.ROAST_CHICKEN_BLOCK.get());
        this.feastBlock((FeastBlock) ModBlocks.HONEY_GLAZED_HAM_BLOCK.get());
        this.feastBlock((FeastBlock) ModBlocks.SHEPHERDS_PIE_BLOCK.get());
        this.feastBlock((FeastBlock) ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get());
        this.wildCropBlock(ModBlocks.SANDY_SHRUB.get());
        this.wildCropBlock(ModBlocks.WILD_BEETROOTS.get());
        this.wildCropBlock(ModBlocks.WILD_CABBAGES.get());
        this.wildCropBlock(ModBlocks.WILD_POTATOES.get());
        this.wildCropBlock(ModBlocks.WILD_TOMATOES.get());
        this.wildCropBlock(ModBlocks.WILD_CARROTS.get());
        this.wildCropBlock(ModBlocks.WILD_ONIONS.get());
        this.doublePlantBlock(ModBlocks.WILD_RICE.get());
    }

    public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
        String formattedName = this.blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
        return ConfiguredModel.allYRotations(this.models().cubeAll(formattedName, this.resourceBlock(formattedName)), 0, false);
    }

    public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction dir = (Direction) state.m_61143_(BlockStateProperties.FACING);
            return ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + 180) % 360).build();
        }, ignored);
    }

    public void customHorizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile((ModelFile) modelFunc.apply(state)).rotationY(((int) ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + 180) % 360).build(), ignored);
    }

    public void stageBlock(Block block, IntegerProperty ageProperty, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            int ageSuffix = (Integer) state.m_61143_(ageProperty);
            String stageName = this.blockName(block) + "_stage" + ageSuffix;
            return ConfiguredModel.builder().modelFile(this.models().cross(stageName, this.resourceBlock(stageName)).renderType("cutout")).build();
        }, ignored);
    }

    public void customStageBlock(Block block, @Nullable ResourceLocation parent, String textureKey, IntegerProperty ageProperty, List<Integer> suffixes, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            int ageSuffix = (Integer) state.m_61143_(ageProperty);
            String stageName = this.blockName(block) + "_stage";
            stageName = stageName + (suffixes.isEmpty() ? ageSuffix : (Integer) suffixes.get(Math.min(suffixes.size(), ageSuffix)));
            return parent == null ? ConfiguredModel.builder().modelFile(this.models().cross(stageName, this.resourceBlock(stageName)).renderType("cutout")).build() : ConfiguredModel.builder().modelFile(this.models().singleTexture(stageName, parent, textureKey, this.resourceBlock(stageName)).renderType("cutout")).build();
        }, ignored);
    }

    public void wildCropBlock(Block block) {
        this.wildCropBlock(block, false);
    }

    public void wildCropBlock(Block block, boolean isBushCrop) {
        if (isBushCrop) {
            this.simpleBlock(block, this.models().singleTexture(this.blockName(block), this.resourceBlock("bush_crop"), "crop", this.resourceBlock(this.blockName(block))).renderType("cutout"));
        } else {
            this.simpleBlock(block, this.models().cross(this.blockName(block), this.resourceBlock(this.blockName(block))).renderType("cutout"));
        }
    }

    public void crateBlock(Block block, String cropName) {
        this.simpleBlock(block, this.models().cubeBottomTop(this.blockName(block), this.resourceBlock(cropName + "_crate_side"), this.resourceBlock("crate_bottom"), this.resourceBlock(cropName + "_crate_top")));
    }

    public void cabinetBlock(Block block, String woodType) {
        this.horizontalBlock(block, state -> {
            String suffix = state.m_61143_(CabinetBlock.OPEN) ? "_open" : "";
            return this.models().orientable(this.blockName(block) + suffix, this.resourceBlock(woodType + "_cabinet_side"), this.resourceBlock(woodType + "_cabinet_front" + suffix), this.resourceBlock(woodType + "_cabinet_top"));
        });
    }

    public void feastBlock(FeastBlock block) {
        this.getVariantBuilder(block).forAllStates(state -> {
            IntegerProperty servingsProperty = block.getServingsProperty();
            int servings = (Integer) state.m_61143_(servingsProperty);
            String suffix = "_stage" + (block.getMaxServings() - servings);
            if (servings == 0) {
                suffix = block.hasLeftovers ? "_leftover" : "_stage" + (servingsProperty.getPossibleValues().toArray().length - 2);
            }
            return ConfiguredModel.builder().modelFile(this.existingModel(this.blockName(block) + suffix)).rotationY(((int) ((Direction) state.m_61143_(FeastBlock.FACING)).toYRot() + 180) % 360).build();
        });
    }

    public void doublePlantBlock(Block block) {
        this.getVariantBuilder(block).partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).modelForState().modelFile(this.models().cross(this.blockName(block) + "_bottom", this.resourceBlock(this.blockName(block) + "_bottom")).renderType("cutout")).addModel().partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).modelForState().modelFile(this.models().cross(this.blockName(block) + "_top", this.resourceBlock(this.blockName(block) + "_top")).renderType("cutout")).addModel();
    }

    public void pieBlock(Block block) {
        this.getVariantBuilder(block).forAllStates(state -> {
            int bites = (Integer) state.m_61143_(PieBlock.BITES);
            String suffix = bites > 0 ? "_slice" + bites : "";
            return ConfiguredModel.builder().modelFile(this.existingModel(this.blockName(block) + suffix)).rotationY(((int) ((Direction) state.m_61143_(PieBlock.FACING)).toYRot() + 180) % 360).build();
        });
    }
}
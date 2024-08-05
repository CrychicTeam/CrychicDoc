package team.lodestar.lodestone.systems.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import team.lodestar.lodestone.systems.datagen.itemsmith.ItemModelSmith;
import team.lodestar.lodestone.systems.datagen.statesmith.BlockStateSmith;
import team.lodestar.lodestone.systems.datagen.statesmith.ModularBlockStateSmith;

public class BlockStateSmithTypes {

    public static ModularBlockStateSmith<Block> CUSTOM_MODEL = new ModularBlockStateSmith<>(Block.class, (block, provider, stateFunction, modelFileSupplier) -> stateFunction.act(block, modelFileSupplier.generateModel(block)));

    public static BlockStateSmith<Block> FULL_BLOCK = new BlockStateSmith<>(Block.class, (block, provider) -> provider.simpleBlock(block));

    public static BlockStateSmith<Block> GRASS_BLOCK = new BlockStateSmith<>(Block.class, (block, provider) -> provider.simpleBlock(block, provider.grassBlockModel(block)));

    public static BlockStateSmith<Block> CROSS_MODEL_BLOCK = new BlockStateSmith<>(Block.class, ItemModelSmithTypes.CROSS_MODEL_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        provider.simpleBlock(block, provider.models().cross(name, provider.getBlockTexture(name)));
    });

    public static BlockStateSmith<Block> TALL_CROSS_MODEL_BLOCK = new BlockStateSmith<>(Block.class, (ItemModelSmith) ItemModelSmithTypes.AFFIXED_BLOCK_TEXTURE_MODEL.apply("_top"), (block, provider) -> {
        String name = provider.getBlockName(block);
        provider.getVariantBuilder(block).forAllStates(s -> {
            String affix = ((DoubleBlockHalf) s.m_61143_(DoublePlantBlock.HALF)).equals(DoubleBlockHalf.LOWER) ? "_bottom" : "_top";
            String affixedName = name + affix;
            return ConfiguredModel.builder().modelFile(provider.models().cross(affixedName, provider.getBlockTexture(affixedName))).build();
        });
    });

    public static BlockStateSmith<Block> LEAVES_BLOCK = new BlockStateSmith<>(Block.class, (block, provider) -> provider.simpleBlock(block, provider.leavesBlockModel(block)));

    public static BlockStateSmith<RotatedPillarBlock> LOG_BLOCK = new BlockStateSmith<>(RotatedPillarBlock.class, (block, provider) -> provider.logBlock(block));

    public static BlockStateSmith<RotatedPillarBlock> AXIS_BLOCK = new BlockStateSmith<>(RotatedPillarBlock.class, (block, provider) -> provider.axisBlock(block));

    public static BlockStateSmith<RotatedPillarBlock> WOOD_BLOCK = new BlockStateSmith<>(RotatedPillarBlock.class, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("_wood", "") + "_log";
        ResourceLocation logTexture = provider.getBlockTexture(textureName);
        provider.axisBlock(block, logTexture, logTexture);
    });

    public static BlockStateSmith<DirectionalBlock> DIRECTIONAL_BLOCK = new BlockStateSmith<>(DirectionalBlock.class, (block, provider) -> {
        String name = provider.getBlockName(block);
        ResourceLocation textureName = provider.getBlockTexture(name);
        BlockModelBuilder directionalModel = provider.models().cubeColumnHorizontal(name, textureName, provider.extend(textureName, "_top"));
        provider.directionalBlock(block, directionalModel);
    });

    public static BlockStateSmith<HorizontalDirectionalBlock> HORIZONTAL_BLOCK = new BlockStateSmith<>(HorizontalDirectionalBlock.class, (block, provider) -> {
        String name = provider.getBlockName(block);
        ResourceLocation textureName = provider.getBlockTexture(name);
        BlockModelBuilder horizontalModel = provider.models().cubeAll(name, textureName);
        provider.horizontalBlock(block, horizontalModel);
    });

    public static BlockStateSmith<StairBlock> STAIRS_BLOCK = new BlockStateSmith<>(StairBlock.class, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("_stairs", "");
        provider.stairsBlock(block, provider.getBlockTexture(textureName));
    });

    public static BlockStateSmith<SlabBlock> SLAB_BLOCK = new BlockStateSmith<>(SlabBlock.class, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("_slab", "");
        provider.slabBlock(block, provider.getBlockTexture(textureName), provider.getBlockTexture(textureName));
    });

    public static BlockStateSmith<WallBlock> WALL_BLOCK = new BlockStateSmith<>(WallBlock.class, ItemModelSmithTypes.WALL_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("_wall", "");
        provider.wallBlock(block, provider.getBlockTexture(textureName));
    });

    public static BlockStateSmith<FenceBlock> FENCE_BLOCK = new BlockStateSmith<>(FenceBlock.class, ItemModelSmithTypes.FENCE_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("_fence", "");
        provider.fenceBlock(block, provider.getBlockTexture(textureName));
    });

    public static BlockStateSmith<FenceGateBlock> FENCE_GATE_BLOCK = new BlockStateSmith<>(FenceGateBlock.class, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("_fence_gate", "");
        provider.fenceGateBlock(block, provider.getBlockTexture(textureName));
    });

    public static BlockStateSmith<PressurePlateBlock> PRESSURE_PLATE_BLOCK = new BlockStateSmith<>(PressurePlateBlock.class, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("_pressure_plate", "");
        provider.pressurePlateBlock(block, provider.getBlockTexture(textureName));
    });

    public static BlockStateSmith<ButtonBlock> BUTTON_BLOCK = new BlockStateSmith<>(ButtonBlock.class, ItemModelSmithTypes.BUTTON_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        ResourceLocation texture = provider.getBlockTexture(name.replace("_button", ""));
        provider.buttonBlock(block, texture);
        provider.models().withExistingParent(name + "_inventory", new ResourceLocation("block/button_inventory")).texture("texture", texture);
    });

    public static BlockStateSmith<DoorBlock> DOOR_BLOCK = new BlockStateSmith<>(DoorBlock.class, ItemModelSmithTypes.GENERATED_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        provider.doorBlock(block, provider.getBlockTexture(name + "_bottom"), provider.getBlockTexture(name + "_top"));
    });

    public static BlockStateSmith<TrapDoorBlock> TRAPDOOR_BLOCK = new BlockStateSmith<>(TrapDoorBlock.class, ItemModelSmithTypes.TRAPDOOR_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        provider.trapdoorBlock(block, provider.getBlockTexture(name), true);
    });

    public static BlockStateSmith<TorchBlock> TORCH_BLOCK = new BlockStateSmith<>(TorchBlock.class, ItemModelSmithTypes.BLOCK_TEXTURE_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        ModelFile torchModel = provider.models().torch(provider.getBlockName(block), provider.getBlockTexture(name));
        provider.getVariantBuilder(block).forAllStates(s -> ConfiguredModel.builder().modelFile(torchModel).build());
    });

    public static BlockStateSmith<WallTorchBlock> WALL_TORCH_BLOCK = new BlockStateSmith<>(WallTorchBlock.class, ItemModelSmithTypes.NO_MODEL, (block, provider) -> {
        String name = provider.getBlockName(block);
        String textureName = name.replace("wall_", "");
        ModelFile torchModel = provider.models().torchWall(provider.getBlockName(block), provider.getBlockTexture(textureName));
        provider.horizontalBlock(block, torchModel, 90);
    });

    public static BlockStateSmith<SignBlock> WOODEN_SIGN_BLOCK = new BlockStateSmith<>(SignBlock.class, ItemModelSmithTypes.GENERATED_ITEM, (block, provider) -> {
        String name = provider.getBlockName(block);
        String particleTextureName = name.replace("_wall", "").replace("_sign", "") + "_planks";
        provider.getVariantBuilder(block).forAllStates(s -> ConfiguredModel.builder().modelFile(provider.models().sign(name, provider.getBlockTexture(particleTextureName))).build());
    });
}
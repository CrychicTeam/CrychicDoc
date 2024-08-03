package com.github.alexmodguy.alexscaves.server.level.feature;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.MusselBlock;
import com.github.alexmodguy.alexscaves.server.level.feature.config.WhalefallFeatureConfiguration;
import com.github.alexmodguy.alexscaves.server.level.structure.processor.WhalefallProcessor;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class WhalefallFeature extends Feature<WhalefallFeatureConfiguration> {

    public WhalefallFeature(Codec<WhalefallFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<WhalefallFeatureConfiguration> context) {
        RandomSource randomsource = context.random();
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos trenchBottom = new BlockPos.MutableBlockPos();
        trenchBottom.set(context.origin());
        while (!level.m_8055_(trenchBottom).m_60819_().isEmpty() && trenchBottom.m_123342_() > level.m_141937_()) {
            trenchBottom.move(0, -1, 0);
        }
        if (level.m_8055_(trenchBottom.m_7495_()).m_60713_(ACBlockRegistry.MUCK.get())) {
            Rotation rotation = Rotation.getRandom(randomsource);
            ResourceLocation head = (ResourceLocation) context.config().headStructures.get(randomsource.nextInt(context.config().headStructures.size()));
            ResourceLocation body = (ResourceLocation) context.config().bodyStructures.get(randomsource.nextInt(context.config().bodyStructures.size()));
            ResourceLocation tail = (ResourceLocation) context.config().tailStructures.get(randomsource.nextInt(context.config().tailStructures.size()));
            Direction direction = rotation.rotate(Direction.SOUTH);
            Direction bendTo = randomsource.nextBoolean() ? direction.getClockWise() : direction.getCounterClockWise();
            StructureTemplateManager structuretemplatemanager = level.m_6018_().getServer().getStructureManager();
            BlockPos startAt = trenchBottom.immutable().relative(direction.getOpposite(), 5);
            int i = this.generateStructurePiece(level, structuretemplatemanager, rotation, randomsource, head, startAt, false);
            int bendToDist = randomsource.nextInt(2);
            int j = this.generateStructurePiece(level, structuretemplatemanager, rotation, randomsource, body, startAt.relative(direction, i + randomsource.nextInt(1)).relative(bendTo, bendToDist), true) + 1;
            this.generateStructurePiece(level, structuretemplatemanager, rotation, randomsource, tail, startAt.relative(direction, i + j + randomsource.nextInt(1)).relative(bendTo, bendToDist + randomsource.nextInt(2)), true);
            return true;
        } else {
            return false;
        }
    }

    private int generateStructurePiece(WorldGenLevel level, StructureTemplateManager structuretemplatemanager, Rotation rotation, RandomSource randomsource, ResourceLocation head, BlockPos blockpos, boolean gravity) {
        StructureTemplate structuretemplate = structuretemplatemanager.getOrCreate(head);
        StructurePlaceSettings structureplacesettings = new StructurePlaceSettings().setRotation(rotation).setRandom(randomsource).addProcessor(gravity ? WhalefallProcessor.INSTANCE_GRAVITY : WhalefallProcessor.INSTANCE_NO_GRAVITY);
        Vec3i defaultSize = structuretemplate.getSize(Rotation.NONE);
        Vec3i rotatedSize = structuretemplate.getSize(rotation);
        BlockPos blockpos1 = blockpos.offset(-rotatedSize.getX() / 2, 0, -rotatedSize.getZ() / 2);
        BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos1, Mirror.NONE, rotation);
        structuretemplate.placeInWorld(level, blockpos2, blockpos2, structureplacesettings, randomsource, 18);
        BoundingBox decorateBox = structuretemplate.getBoundingBox(structureplacesettings, blockpos2).inflatedBy(1);
        BlockPos.betweenClosedStream(decorateBox).forEach(pos -> this.decorateWhalefallPos(pos, level, randomsource));
        return Math.abs(defaultSize.getZ());
    }

    private void decorateWhalefallPos(BlockPos pos, WorldGenLevel worldgenlevel, RandomSource randomsource) {
        Direction dir = Direction.getRandom(randomsource);
        BlockPos blockpos2 = pos.relative(dir);
        if (worldgenlevel.m_8055_(blockpos2).m_204336_(ACTagRegistry.WHALEFALL_IGNORES) || randomsource.nextInt(5) == 0) {
            BlockState replaceAt = worldgenlevel.m_8055_(pos);
            if ((replaceAt.m_60713_(Blocks.WATER) || replaceAt.m_60795_()) && worldgenlevel.m_8055_(blockpos2).m_60783_(worldgenlevel, blockpos2, dir.getOpposite())) {
                boolean waterlog = worldgenlevel.m_6425_(pos).is(FluidTags.WATER);
                if (randomsource.nextBoolean()) {
                    worldgenlevel.m_7731_(pos, (BlockState) ((BlockState) ACBlockRegistry.BONE_WORMS.get().defaultBlockState().m_61124_(MusselBlock.FACING, dir.getOpposite())).m_61124_(MusselBlock.WATERLOGGED, waterlog), 3);
                } else {
                    worldgenlevel.m_7731_(pos, (BlockState) ((BlockState) ((BlockState) ACBlockRegistry.MUSSEL.get().defaultBlockState().m_61124_(MusselBlock.FACING, dir.getOpposite())).m_61124_(MusselBlock.WATERLOGGED, waterlog)).m_61124_(MusselBlock.MUSSELS, 1 + randomsource.nextInt(4)), 3);
                }
            }
        }
    }
}
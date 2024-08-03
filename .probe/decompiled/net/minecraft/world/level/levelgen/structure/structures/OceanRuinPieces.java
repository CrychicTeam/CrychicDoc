package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.CappedProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosAlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class OceanRuinPieces {

    static final StructureProcessor WARM_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.SAND, Blocks.SUSPICIOUS_SAND, BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY);

    static final StructureProcessor COLD_SUSPICIOUS_BLOCK_PROCESSOR = archyRuleProcessor(Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL, BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY);

    private static final ResourceLocation[] WARM_RUINS = new ResourceLocation[] { new ResourceLocation("underwater_ruin/warm_1"), new ResourceLocation("underwater_ruin/warm_2"), new ResourceLocation("underwater_ruin/warm_3"), new ResourceLocation("underwater_ruin/warm_4"), new ResourceLocation("underwater_ruin/warm_5"), new ResourceLocation("underwater_ruin/warm_6"), new ResourceLocation("underwater_ruin/warm_7"), new ResourceLocation("underwater_ruin/warm_8") };

    private static final ResourceLocation[] RUINS_BRICK = new ResourceLocation[] { new ResourceLocation("underwater_ruin/brick_1"), new ResourceLocation("underwater_ruin/brick_2"), new ResourceLocation("underwater_ruin/brick_3"), new ResourceLocation("underwater_ruin/brick_4"), new ResourceLocation("underwater_ruin/brick_5"), new ResourceLocation("underwater_ruin/brick_6"), new ResourceLocation("underwater_ruin/brick_7"), new ResourceLocation("underwater_ruin/brick_8") };

    private static final ResourceLocation[] RUINS_CRACKED = new ResourceLocation[] { new ResourceLocation("underwater_ruin/cracked_1"), new ResourceLocation("underwater_ruin/cracked_2"), new ResourceLocation("underwater_ruin/cracked_3"), new ResourceLocation("underwater_ruin/cracked_4"), new ResourceLocation("underwater_ruin/cracked_5"), new ResourceLocation("underwater_ruin/cracked_6"), new ResourceLocation("underwater_ruin/cracked_7"), new ResourceLocation("underwater_ruin/cracked_8") };

    private static final ResourceLocation[] RUINS_MOSSY = new ResourceLocation[] { new ResourceLocation("underwater_ruin/mossy_1"), new ResourceLocation("underwater_ruin/mossy_2"), new ResourceLocation("underwater_ruin/mossy_3"), new ResourceLocation("underwater_ruin/mossy_4"), new ResourceLocation("underwater_ruin/mossy_5"), new ResourceLocation("underwater_ruin/mossy_6"), new ResourceLocation("underwater_ruin/mossy_7"), new ResourceLocation("underwater_ruin/mossy_8") };

    private static final ResourceLocation[] BIG_RUINS_BRICK = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_brick_1"), new ResourceLocation("underwater_ruin/big_brick_2"), new ResourceLocation("underwater_ruin/big_brick_3"), new ResourceLocation("underwater_ruin/big_brick_8") };

    private static final ResourceLocation[] BIG_RUINS_MOSSY = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_mossy_1"), new ResourceLocation("underwater_ruin/big_mossy_2"), new ResourceLocation("underwater_ruin/big_mossy_3"), new ResourceLocation("underwater_ruin/big_mossy_8") };

    private static final ResourceLocation[] BIG_RUINS_CRACKED = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_cracked_1"), new ResourceLocation("underwater_ruin/big_cracked_2"), new ResourceLocation("underwater_ruin/big_cracked_3"), new ResourceLocation("underwater_ruin/big_cracked_8") };

    private static final ResourceLocation[] BIG_WARM_RUINS = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_warm_4"), new ResourceLocation("underwater_ruin/big_warm_5"), new ResourceLocation("underwater_ruin/big_warm_6"), new ResourceLocation("underwater_ruin/big_warm_7") };

    private static StructureProcessor archyRuleProcessor(Block block0, Block block1, ResourceLocation resourceLocation2) {
        return new CappedProcessor(new RuleProcessor(List.of(new ProcessorRule(new BlockMatchTest(block0), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, block1.defaultBlockState(), new AppendLoot(resourceLocation2)))), ConstantInt.of(5));
    }

    private static ResourceLocation getSmallWarmRuin(RandomSource randomSource0) {
        return Util.getRandom(WARM_RUINS, randomSource0);
    }

    private static ResourceLocation getBigWarmRuin(RandomSource randomSource0) {
        return Util.getRandom(BIG_WARM_RUINS, randomSource0);
    }

    public static void addPieces(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, StructurePieceAccessor structurePieceAccessor3, RandomSource randomSource4, OceanRuinStructure oceanRuinStructure5) {
        boolean $$6 = randomSource4.nextFloat() <= oceanRuinStructure5.largeProbability;
        float $$7 = $$6 ? 0.9F : 0.8F;
        addPiece(structureTemplateManager0, blockPos1, rotation2, structurePieceAccessor3, randomSource4, oceanRuinStructure5, $$6, $$7);
        if ($$6 && randomSource4.nextFloat() <= oceanRuinStructure5.clusterProbability) {
            addClusterRuins(structureTemplateManager0, randomSource4, rotation2, blockPos1, oceanRuinStructure5, structurePieceAccessor3);
        }
    }

    private static void addClusterRuins(StructureTemplateManager structureTemplateManager0, RandomSource randomSource1, Rotation rotation2, BlockPos blockPos3, OceanRuinStructure oceanRuinStructure4, StructurePieceAccessor structurePieceAccessor5) {
        BlockPos $$6 = new BlockPos(blockPos3.m_123341_(), 90, blockPos3.m_123343_());
        BlockPos $$7 = StructureTemplate.transform(new BlockPos(15, 0, 15), Mirror.NONE, rotation2, BlockPos.ZERO).offset($$6);
        BoundingBox $$8 = BoundingBox.fromCorners($$6, $$7);
        BlockPos $$9 = new BlockPos(Math.min($$6.m_123341_(), $$7.m_123341_()), $$6.m_123342_(), Math.min($$6.m_123343_(), $$7.m_123343_()));
        List<BlockPos> $$10 = allPositions(randomSource1, $$9);
        int $$11 = Mth.nextInt(randomSource1, 4, 8);
        for (int $$12 = 0; $$12 < $$11; $$12++) {
            if (!$$10.isEmpty()) {
                int $$13 = randomSource1.nextInt($$10.size());
                BlockPos $$14 = (BlockPos) $$10.remove($$13);
                Rotation $$15 = Rotation.getRandom(randomSource1);
                BlockPos $$16 = StructureTemplate.transform(new BlockPos(5, 0, 6), Mirror.NONE, $$15, BlockPos.ZERO).offset($$14);
                BoundingBox $$17 = BoundingBox.fromCorners($$14, $$16);
                if (!$$17.intersects($$8)) {
                    addPiece(structureTemplateManager0, $$14, $$15, structurePieceAccessor5, randomSource1, oceanRuinStructure4, false, 0.8F);
                }
            }
        }
    }

    private static List<BlockPos> allPositions(RandomSource randomSource0, BlockPos blockPos1) {
        List<BlockPos> $$2 = Lists.newArrayList();
        $$2.add(blockPos1.offset(-16 + Mth.nextInt(randomSource0, 1, 8), 0, 16 + Mth.nextInt(randomSource0, 1, 7)));
        $$2.add(blockPos1.offset(-16 + Mth.nextInt(randomSource0, 1, 8), 0, Mth.nextInt(randomSource0, 1, 7)));
        $$2.add(blockPos1.offset(-16 + Mth.nextInt(randomSource0, 1, 8), 0, -16 + Mth.nextInt(randomSource0, 4, 8)));
        $$2.add(blockPos1.offset(Mth.nextInt(randomSource0, 1, 7), 0, 16 + Mth.nextInt(randomSource0, 1, 7)));
        $$2.add(blockPos1.offset(Mth.nextInt(randomSource0, 1, 7), 0, -16 + Mth.nextInt(randomSource0, 4, 6)));
        $$2.add(blockPos1.offset(16 + Mth.nextInt(randomSource0, 1, 7), 0, 16 + Mth.nextInt(randomSource0, 3, 8)));
        $$2.add(blockPos1.offset(16 + Mth.nextInt(randomSource0, 1, 7), 0, Mth.nextInt(randomSource0, 1, 7)));
        $$2.add(blockPos1.offset(16 + Mth.nextInt(randomSource0, 1, 7), 0, -16 + Mth.nextInt(randomSource0, 4, 8)));
        return $$2;
    }

    private static void addPiece(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, StructurePieceAccessor structurePieceAccessor3, RandomSource randomSource4, OceanRuinStructure oceanRuinStructure5, boolean boolean6, float float7) {
        switch(oceanRuinStructure5.biomeTemp) {
            case WARM:
            default:
                ResourceLocation $$8 = boolean6 ? getBigWarmRuin(randomSource4) : getSmallWarmRuin(randomSource4);
                structurePieceAccessor3.addPiece(new OceanRuinPieces.OceanRuinPiece(structureTemplateManager0, $$8, blockPos1, rotation2, float7, oceanRuinStructure5.biomeTemp, boolean6));
                break;
            case COLD:
                ResourceLocation[] $$9 = boolean6 ? BIG_RUINS_BRICK : RUINS_BRICK;
                ResourceLocation[] $$10 = boolean6 ? BIG_RUINS_CRACKED : RUINS_CRACKED;
                ResourceLocation[] $$11 = boolean6 ? BIG_RUINS_MOSSY : RUINS_MOSSY;
                int $$12 = randomSource4.nextInt($$9.length);
                structurePieceAccessor3.addPiece(new OceanRuinPieces.OceanRuinPiece(structureTemplateManager0, $$9[$$12], blockPos1, rotation2, float7, oceanRuinStructure5.biomeTemp, boolean6));
                structurePieceAccessor3.addPiece(new OceanRuinPieces.OceanRuinPiece(structureTemplateManager0, $$10[$$12], blockPos1, rotation2, 0.7F, oceanRuinStructure5.biomeTemp, boolean6));
                structurePieceAccessor3.addPiece(new OceanRuinPieces.OceanRuinPiece(structureTemplateManager0, $$11[$$12], blockPos1, rotation2, 0.5F, oceanRuinStructure5.biomeTemp, boolean6));
        }
    }

    public static class OceanRuinPiece extends TemplateStructurePiece {

        private final OceanRuinStructure.Type biomeType;

        private final float integrity;

        private final boolean isLarge;

        public OceanRuinPiece(StructureTemplateManager structureTemplateManager0, ResourceLocation resourceLocation1, BlockPos blockPos2, Rotation rotation3, float float4, OceanRuinStructure.Type oceanRuinStructureType5, boolean boolean6) {
            super(StructurePieceType.OCEAN_RUIN, 0, structureTemplateManager0, resourceLocation1, resourceLocation1.toString(), makeSettings(rotation3, float4, oceanRuinStructureType5), blockPos2);
            this.integrity = float4;
            this.biomeType = oceanRuinStructureType5;
            this.isLarge = boolean6;
        }

        private OceanRuinPiece(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1, Rotation rotation2, float float3, OceanRuinStructure.Type oceanRuinStructureType4, boolean boolean5) {
            super(StructurePieceType.OCEAN_RUIN, compoundTag1, structureTemplateManager0, p_277332_ -> makeSettings(rotation2, float3, oceanRuinStructureType4));
            this.integrity = float3;
            this.biomeType = oceanRuinStructureType4;
            this.isLarge = boolean5;
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation0, float float1, OceanRuinStructure.Type oceanRuinStructureType2) {
            StructureProcessor $$3 = oceanRuinStructureType2 == OceanRuinStructure.Type.COLD ? OceanRuinPieces.COLD_SUSPICIOUS_BLOCK_PROCESSOR : OceanRuinPieces.WARM_SUSPICIOUS_BLOCK_PROCESSOR;
            return new StructurePlaceSettings().setRotation(rotation0).setMirror(Mirror.NONE).addProcessor(new BlockRotProcessor(float1)).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR).addProcessor($$3);
        }

        public static OceanRuinPieces.OceanRuinPiece create(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1) {
            Rotation $$2 = Rotation.valueOf(compoundTag1.getString("Rot"));
            float $$3 = compoundTag1.getFloat("Integrity");
            OceanRuinStructure.Type $$4 = OceanRuinStructure.Type.valueOf(compoundTag1.getString("BiomeType"));
            boolean $$5 = compoundTag1.getBoolean("IsLarge");
            return new OceanRuinPieces.OceanRuinPiece(structureTemplateManager0, compoundTag1, $$2, $$3, $$4, $$5);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putString("Rot", this.f_73657_.getRotation().name());
            compoundTag1.putFloat("Integrity", this.integrity);
            compoundTag1.putString("BiomeType", this.biomeType.toString());
            compoundTag1.putBoolean("IsLarge", this.isLarge);
        }

        @Override
        protected void handleDataMarker(String string0, BlockPos blockPos1, ServerLevelAccessor serverLevelAccessor2, RandomSource randomSource3, BoundingBox boundingBox4) {
            if ("chest".equals(string0)) {
                serverLevelAccessor2.m_7731_(blockPos1, (BlockState) Blocks.CHEST.defaultBlockState().m_61124_(ChestBlock.WATERLOGGED, serverLevelAccessor2.m_6425_(blockPos1).is(FluidTags.WATER)), 2);
                BlockEntity $$5 = serverLevelAccessor2.m_7702_(blockPos1);
                if ($$5 instanceof ChestBlockEntity) {
                    ((ChestBlockEntity) $$5).m_59626_(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, randomSource3.nextLong());
                }
            } else if ("drowned".equals(string0)) {
                Drowned $$6 = EntityType.DROWNED.create(serverLevelAccessor2.getLevel());
                if ($$6 != null) {
                    $$6.m_21530_();
                    $$6.m_20035_(blockPos1, 0.0F, 0.0F);
                    $$6.finalizeSpawn(serverLevelAccessor2, serverLevelAccessor2.m_6436_(blockPos1), MobSpawnType.STRUCTURE, null, null);
                    serverLevelAccessor2.addFreshEntityWithPassengers($$6);
                    if (blockPos1.m_123342_() > serverLevelAccessor2.m_5736_()) {
                        serverLevelAccessor2.m_7731_(blockPos1, Blocks.AIR.defaultBlockState(), 2);
                    } else {
                        serverLevelAccessor2.m_7731_(blockPos1, Blocks.WATER.defaultBlockState(), 2);
                    }
                }
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            int $$7 = worldGenLevel0.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, this.f_73658_.m_123341_(), this.f_73658_.m_123343_());
            this.f_73658_ = new BlockPos(this.f_73658_.m_123341_(), $$7, this.f_73658_.m_123343_());
            BlockPos $$8 = StructureTemplate.transform(new BlockPos(this.f_73656_.getSize().getX() - 1, 0, this.f_73656_.getSize().getZ() - 1), Mirror.NONE, this.f_73657_.getRotation(), BlockPos.ZERO).offset(this.f_73658_);
            this.f_73658_ = new BlockPos(this.f_73658_.m_123341_(), this.getHeight(this.f_73658_, worldGenLevel0, $$8), this.f_73658_.m_123343_());
            super.postProcess(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, blockPos6);
        }

        private int getHeight(BlockPos blockPos0, BlockGetter blockGetter1, BlockPos blockPos2) {
            int $$3 = blockPos0.m_123342_();
            int $$4 = 512;
            int $$5 = $$3 - 1;
            int $$6 = 0;
            for (BlockPos $$7 : BlockPos.betweenClosed(blockPos0, blockPos2)) {
                int $$8 = $$7.m_123341_();
                int $$9 = $$7.m_123343_();
                int $$10 = blockPos0.m_123342_() - 1;
                BlockPos.MutableBlockPos $$11 = new BlockPos.MutableBlockPos($$8, $$10, $$9);
                BlockState $$12 = blockGetter1.getBlockState($$11);
                for (FluidState $$13 = blockGetter1.getFluidState($$11); ($$12.m_60795_() || $$13.is(FluidTags.WATER) || $$12.m_204336_(BlockTags.ICE)) && $$10 > blockGetter1.m_141937_() + 1; $$13 = blockGetter1.getFluidState($$11)) {
                    $$11.set($$8, --$$10, $$9);
                    $$12 = blockGetter1.getBlockState($$11);
                }
                $$4 = Math.min($$4, $$10);
                if ($$10 < $$5 - 2) {
                    $$6++;
                }
            }
            int $$14 = Math.abs(blockPos0.m_123341_() - blockPos2.m_123341_());
            if ($$5 - $$4 > 2 && $$6 > $$14 - 2) {
                $$3 = $$4 + 1;
            }
            return $$3;
        }
    }
}
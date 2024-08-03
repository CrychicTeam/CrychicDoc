package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.PrimalMagmaBlock;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class VolcanoStructurePiece extends StructurePiece {

    private BlockPos center;

    private BlockPos chunkCorner;

    private int volcanoRadius;

    private int volcanoHeight;

    public VolcanoStructurePiece(BlockPos center, BlockPos chunkCorner, int volcanoRadius, int volcanoHeight) {
        super(ACStructurePieceRegistry.VOLCANO.get(), 0, createBoundingBox(chunkCorner));
        this.center = center;
        this.chunkCorner = chunkCorner;
        this.volcanoRadius = volcanoRadius;
        this.volcanoHeight = volcanoHeight;
    }

    public VolcanoStructurePiece(CompoundTag tag) {
        super(ACStructurePieceRegistry.VOLCANO.get(), tag);
        this.center = new BlockPos(tag.getInt("CPX"), tag.getInt("CPY"), tag.getInt("CPZ"));
        this.chunkCorner = new BlockPos(tag.getInt("TPX"), tag.getInt("TPY"), tag.getInt("TPZ"));
        this.volcanoRadius = tag.getInt("Radius");
        this.volcanoHeight = tag.getInt("Height");
    }

    public VolcanoStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        tag.putInt("CPX", this.center.m_123341_());
        tag.putInt("CPY", this.center.m_123342_());
        tag.putInt("CPZ", this.center.m_123343_());
        tag.putInt("TPX", this.chunkCorner.m_123341_());
        tag.putInt("TPY", this.chunkCorner.m_123342_());
        tag.putInt("TPZ", this.chunkCorner.m_123343_());
        tag.putInt("Radius", this.volcanoRadius);
        tag.putInt("Height", this.volcanoHeight);
    }

    private static BoundingBox createBoundingBox(BlockPos chunkCorner) {
        ChunkPos chunkPos = new ChunkPos(chunkCorner);
        return new BoundingBox(chunkPos.getMinBlockX(), -64, chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX(), 256, chunkPos.getMaxBlockZ());
    }

    public void checkedSetBlock(WorldGenLevel level, BlockPos position, BlockState state) {
        if (this.m_73547_().isInside(position)) {
            level.m_7731_(position, state, 128);
        }
    }

    public BlockState checkedGetBlock(WorldGenLevel level, BlockPos position) {
        return this.m_73547_().isInside(position) ? level.m_8055_(position) : Blocks.VOID_AIR.defaultBlockState();
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        int cornerX = this.chunkCorner.m_123341_();
        int cornerY = this.chunkCorner.m_123342_();
        int cornerZ = this.chunkCorner.m_123343_();
        int generateCoreHeight = (int) Math.floor((double) this.volcanoHeight * 0.35);
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                carve.set(cornerX + x, cornerY, cornerZ + z);
                double dist = Math.sqrt(carve.m_203202_((double) this.center.m_123341_(), (double) carve.m_123342_(), (double) this.center.m_123343_())) / (double) this.volcanoRadius;
                if (dist < 1.0) {
                    double invDist = 1.0 - dist;
                    double volcanoCurve = this.calcVolcanoCurve(invDist);
                    int lavaSink = volcanoCurve < 1.0 && dist < 0.15F ? 2 : 0;
                    double heightAt = (double) this.volcanoHeight * volcanoCurve;
                    for (int generateHeight = (int) ((double) this.center.m_123342_() + heightAt - (double) lavaSink); carve.m_123342_() < generateHeight; carve.move(0, 1, 0)) {
                        BlockState checkedBlockstate = this.checkedGetBlock(level, carve);
                        if (checkedBlockstate.m_60713_(Blocks.BEDROCK)) {
                            break;
                        }
                        int beneathAmount = generateHeight - carve.m_123342_();
                        int aboveAmount = carve.m_123342_() - this.center.m_123342_();
                        float calderaAmount = (float) Math.sin(invDist * Math.PI);
                        if (beneathAmount == generateCoreHeight && carve.m_123341_() == this.center.m_123341_() && carve.m_123343_() == this.center.m_123343_()) {
                            this.checkedSetBlock(level, carve, ACBlockRegistry.VOLCANIC_CORE.get().defaultBlockState());
                        } else if ((float) beneathAmount > 3.0F + calderaAmount * 20.0F && (float) aboveAmount > 3.0F + calderaAmount * 10.0F) {
                            this.checkedSetBlock(level, carve, Blocks.LAVA.defaultBlockState());
                        } else if (lavaSink <= 0 && (beneathAmount <= 6 || aboveAmount <= 3)) {
                            float magmaVeinNoise = ACMath.sampleNoise3D((float) (carve.m_123341_() - 19000), (float) carve.m_123342_() * 0.5F + 120.0F, (float) (carve.m_123343_() + 19000), 20.0F);
                            if (volcanoCurve < 0.035F) {
                                if ((double) (random.nextFloat() * 0.13F) < volcanoCurve) {
                                    this.checkedSetBlock(level, carve, Blocks.SMOOTH_BASALT.defaultBlockState());
                                } else {
                                    this.checkedSetBlock(level, carve, ACBlockRegistry.LIMESTONE.get().defaultBlockState());
                                }
                            } else if (magmaVeinNoise < 0.075F && magmaVeinNoise > -0.075F) {
                                this.checkedSetBlock(level, carve, ACBlockRegistry.PRIMAL_MAGMA.get().defaultBlockState());
                            } else {
                                this.checkedSetBlock(level, carve, ACBlockRegistry.FLOOD_BASALT.get().defaultBlockState());
                            }
                        } else {
                            this.checkedSetBlock(level, carve, (BlockState) ((BlockState) ACBlockRegistry.PRIMAL_MAGMA.get().defaultBlockState().m_61124_(PrimalMagmaBlock.ACTIVE, true)).m_61124_(PrimalMagmaBlock.PERMANENT, true));
                        }
                    }
                    carve.set(carve.m_123341_(), cornerY - 1, carve.m_123343_());
                    for (int j = 0; (this.volcanoReplacesBeneath(this.checkedGetBlock(level, carve)) || j < 3) && carve.m_123342_() > level.m_141937_(); j++) {
                        this.checkedSetBlock(level, carve, ACBlockRegistry.LIMESTONE.get().defaultBlockState());
                        carve.move(0, -1, 0);
                    }
                }
            }
        }
    }

    private boolean volcanoReplacesBeneath(BlockState state) {
        return state.m_60713_(Blocks.AIR) || state.m_204336_(BlockTags.DIRT);
    }

    private double calcVolcanoCurve(double dist) {
        double off = 2.0 * dist - 1.0;
        double d0 = 0.5 * (0.85F * Math.sin(off * (float) Math.PI) + Math.pow(off, 3.0) + 0.5);
        return dist > 0.85F ? d0 : Math.max(d0, dist * dist);
    }
}
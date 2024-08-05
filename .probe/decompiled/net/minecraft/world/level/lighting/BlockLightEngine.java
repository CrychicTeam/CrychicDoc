package net.minecraft.world.level.lighting;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;

public final class BlockLightEngine extends LightEngine<BlockLightSectionStorage.BlockDataLayerStorageMap, BlockLightSectionStorage> {

    private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    public BlockLightEngine(LightChunkGetter lightChunkGetter0) {
        this(lightChunkGetter0, new BlockLightSectionStorage(lightChunkGetter0));
    }

    @VisibleForTesting
    public BlockLightEngine(LightChunkGetter lightChunkGetter0, BlockLightSectionStorage blockLightSectionStorage1) {
        super(lightChunkGetter0, blockLightSectionStorage1);
    }

    @Override
    protected void checkNode(long long0) {
        long $$1 = SectionPos.blockToSection(long0);
        if (((BlockLightSectionStorage) this.f_283849_).m_75791_($$1)) {
            BlockState $$2 = this.m_284512_(this.mutablePos.set(long0));
            int $$3 = this.getEmission(long0, $$2);
            int $$4 = ((BlockLightSectionStorage) this.f_283849_).m_75795_(long0);
            if ($$3 < $$4) {
                ((BlockLightSectionStorage) this.f_283849_).m_75772_(long0, 0);
                this.m_284343_(long0, LightEngine.QueueEntry.decreaseAllDirections($$4));
            } else {
                this.m_284343_(long0, f_283854_);
            }
            if ($$3 > 0) {
                this.m_284218_(long0, LightEngine.QueueEntry.increaseLightFromEmission($$3, m_284265_($$2)));
            }
        }
    }

    @Override
    protected void propagateIncrease(long long0, long long1, int int2) {
        BlockState $$3 = null;
        for (Direction $$4 : f_283814_) {
            if (LightEngine.QueueEntry.shouldPropagateInDirection(long1, $$4)) {
                long $$5 = BlockPos.offset(long0, $$4);
                if (((BlockLightSectionStorage) this.f_283849_).m_75791_(SectionPos.blockToSection($$5))) {
                    int $$6 = ((BlockLightSectionStorage) this.f_283849_).m_75795_($$5);
                    int $$7 = int2 - 1;
                    if ($$7 > $$6) {
                        this.mutablePos.set($$5);
                        BlockState $$8 = this.m_284512_(this.mutablePos);
                        int $$9 = int2 - this.m_284404_($$8, this.mutablePos);
                        if ($$9 > $$6) {
                            if ($$3 == null) {
                                $$3 = LightEngine.QueueEntry.isFromEmptyShape(long1) ? Blocks.AIR.defaultBlockState() : this.m_284512_(this.mutablePos.set(long0));
                            }
                            if (!this.m_284187_(long0, $$3, $$5, $$8, $$4)) {
                                ((BlockLightSectionStorage) this.f_283849_).m_75772_($$5, $$9);
                                if ($$9 > 1) {
                                    this.m_284218_($$5, LightEngine.QueueEntry.increaseSkipOneDirection($$9, m_284265_($$8), $$4.getOpposite()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void propagateDecrease(long long0, long long1) {
        int $$2 = LightEngine.QueueEntry.getFromLevel(long1);
        for (Direction $$3 : f_283814_) {
            if (LightEngine.QueueEntry.shouldPropagateInDirection(long1, $$3)) {
                long $$4 = BlockPos.offset(long0, $$3);
                if (((BlockLightSectionStorage) this.f_283849_).m_75791_(SectionPos.blockToSection($$4))) {
                    int $$5 = ((BlockLightSectionStorage) this.f_283849_).m_75795_($$4);
                    if ($$5 != 0) {
                        if ($$5 <= $$2 - 1) {
                            BlockState $$6 = this.m_284512_(this.mutablePos.set($$4));
                            int $$7 = this.getEmission($$4, $$6);
                            ((BlockLightSectionStorage) this.f_283849_).m_75772_($$4, 0);
                            if ($$7 < $$5) {
                                this.m_284343_($$4, LightEngine.QueueEntry.decreaseSkipOneDirection($$5, $$3.getOpposite()));
                            }
                            if ($$7 > 0) {
                                this.m_284218_($$4, LightEngine.QueueEntry.increaseLightFromEmission($$7, m_284265_($$6)));
                            }
                        } else {
                            this.m_284218_($$4, LightEngine.QueueEntry.increaseOnlyOneDirection($$5, false, $$3.getOpposite()));
                        }
                    }
                }
            }
        }
    }

    private int getEmission(long long0, BlockState blockState1) {
        int $$2 = blockState1.m_60791_();
        return $$2 > 0 && ((BlockLightSectionStorage) this.f_283849_).m_284382_(SectionPos.blockToSection(long0)) ? $$2 : 0;
    }

    @Override
    public void propagateLightSources(ChunkPos chunkPos0) {
        this.m_9335_(chunkPos0, true);
        LightChunk $$1 = this.f_283884_.getChunkForLighting(chunkPos0.x, chunkPos0.z);
        if ($$1 != null) {
            $$1.findBlockLightSources((p_285266_, p_285452_) -> {
                int $$2 = p_285452_.m_60791_();
                this.m_284218_(p_285266_.asLong(), LightEngine.QueueEntry.increaseLightFromEmission($$2, m_284265_(p_285452_)));
            });
        }
    }
}
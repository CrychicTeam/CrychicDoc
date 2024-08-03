package net.minecraft.world.level.lighting;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LightChunk;
import net.minecraft.world.level.chunk.LightChunkGetter;
import org.jetbrains.annotations.VisibleForTesting;

public final class SkyLightEngine extends LightEngine<SkyLightSectionStorage.SkyDataLayerStorageMap, SkyLightSectionStorage> {

    private static final long REMOVE_TOP_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseAllDirections(15);

    private static final long REMOVE_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseSkipOneDirection(15, Direction.UP);

    private static final long ADD_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.increaseSkipOneDirection(15, false, Direction.UP);

    private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    private final ChunkSkyLightSources emptyChunkSources;

    public SkyLightEngine(LightChunkGetter lightChunkGetter0) {
        this(lightChunkGetter0, new SkyLightSectionStorage(lightChunkGetter0));
    }

    @VisibleForTesting
    protected SkyLightEngine(LightChunkGetter lightChunkGetter0, SkyLightSectionStorage skyLightSectionStorage1) {
        super(lightChunkGetter0, skyLightSectionStorage1);
        this.emptyChunkSources = new ChunkSkyLightSources(lightChunkGetter0.getLevel());
    }

    private static boolean isSourceLevel(int int0) {
        return int0 == 15;
    }

    private int getLowestSourceY(int int0, int int1, int int2) {
        ChunkSkyLightSources $$3 = this.getChunkSources(SectionPos.blockToSectionCoord(int0), SectionPos.blockToSectionCoord(int1));
        return $$3 == null ? int2 : $$3.getLowestSourceY(SectionPos.sectionRelative(int0), SectionPos.sectionRelative(int1));
    }

    @Nullable
    private ChunkSkyLightSources getChunkSources(int int0, int int1) {
        LightChunk $$2 = this.f_283884_.getChunkForLighting(int0, int1);
        return $$2 != null ? $$2.getSkyLightSources() : null;
    }

    @Override
    protected void checkNode(long long0) {
        int $$1 = BlockPos.getX(long0);
        int $$2 = BlockPos.getY(long0);
        int $$3 = BlockPos.getZ(long0);
        long $$4 = SectionPos.blockToSection(long0);
        int $$5 = ((SkyLightSectionStorage) this.f_283849_).m_284382_($$4) ? this.getLowestSourceY($$1, $$3, Integer.MAX_VALUE) : Integer.MAX_VALUE;
        if ($$5 != Integer.MAX_VALUE) {
            this.updateSourcesInColumn($$1, $$3, $$5);
        }
        if (((SkyLightSectionStorage) this.f_283849_).m_75791_($$4)) {
            boolean $$6 = $$2 >= $$5;
            if ($$6) {
                this.m_284343_(long0, REMOVE_SKY_SOURCE_ENTRY);
                this.m_284218_(long0, ADD_SKY_SOURCE_ENTRY);
            } else {
                int $$7 = ((SkyLightSectionStorage) this.f_283849_).m_75795_(long0);
                if ($$7 > 0) {
                    ((SkyLightSectionStorage) this.f_283849_).m_75772_(long0, 0);
                    this.m_284343_(long0, LightEngine.QueueEntry.decreaseAllDirections($$7));
                } else {
                    this.m_284343_(long0, f_283854_);
                }
            }
        }
    }

    private void updateSourcesInColumn(int int0, int int1, int int2) {
        int $$3 = SectionPos.sectionToBlockCoord(((SkyLightSectionStorage) this.f_283849_).getBottomSectionY());
        this.removeSourcesBelow(int0, int1, int2, $$3);
        this.addSourcesAbove(int0, int1, int2, $$3);
    }

    private void removeSourcesBelow(int int0, int int1, int int2, int int3) {
        if (int2 > int3) {
            int $$4 = SectionPos.blockToSectionCoord(int0);
            int $$5 = SectionPos.blockToSectionCoord(int1);
            int $$6 = int2 - 1;
            for (int $$7 = SectionPos.blockToSectionCoord($$6); ((SkyLightSectionStorage) this.f_283849_).hasLightDataAtOrBelow($$7); $$7--) {
                if (((SkyLightSectionStorage) this.f_283849_).m_75791_(SectionPos.asLong($$4, $$7, $$5))) {
                    int $$8 = SectionPos.sectionToBlockCoord($$7);
                    int $$9 = $$8 + 15;
                    for (int $$10 = Math.min($$9, $$6); $$10 >= $$8; $$10--) {
                        long $$11 = BlockPos.asLong(int0, $$10, int1);
                        if (!isSourceLevel(((SkyLightSectionStorage) this.f_283849_).m_75795_($$11))) {
                            return;
                        }
                        ((SkyLightSectionStorage) this.f_283849_).m_75772_($$11, 0);
                        this.m_284343_($$11, $$10 == int2 - 1 ? REMOVE_TOP_SKY_SOURCE_ENTRY : REMOVE_SKY_SOURCE_ENTRY);
                    }
                }
            }
        }
    }

    private void addSourcesAbove(int int0, int int1, int int2, int int3) {
        int $$4 = SectionPos.blockToSectionCoord(int0);
        int $$5 = SectionPos.blockToSectionCoord(int1);
        int $$6 = Math.max(Math.max(this.getLowestSourceY(int0 - 1, int1, Integer.MIN_VALUE), this.getLowestSourceY(int0 + 1, int1, Integer.MIN_VALUE)), Math.max(this.getLowestSourceY(int0, int1 - 1, Integer.MIN_VALUE), this.getLowestSourceY(int0, int1 + 1, Integer.MIN_VALUE)));
        int $$7 = Math.max(int2, int3);
        for (long $$8 = SectionPos.asLong($$4, SectionPos.blockToSectionCoord($$7), $$5); !((SkyLightSectionStorage) this.f_283849_).isAboveData($$8); $$8 = SectionPos.offset($$8, Direction.UP)) {
            if (((SkyLightSectionStorage) this.f_283849_).m_75791_($$8)) {
                int $$9 = SectionPos.sectionToBlockCoord(SectionPos.y($$8));
                int $$10 = $$9 + 15;
                for (int $$11 = Math.max($$9, $$7); $$11 <= $$10; $$11++) {
                    long $$12 = BlockPos.asLong(int0, $$11, int1);
                    if (isSourceLevel(((SkyLightSectionStorage) this.f_283849_).m_75795_($$12))) {
                        return;
                    }
                    ((SkyLightSectionStorage) this.f_283849_).m_75772_($$12, 15);
                    if ($$11 < $$6 || $$11 == int2) {
                        this.m_284218_($$12, ADD_SKY_SOURCE_ENTRY);
                    }
                }
            }
        }
    }

    @Override
    protected void propagateIncrease(long long0, long long1, int int2) {
        BlockState $$3 = null;
        int $$4 = this.countEmptySectionsBelowIfAtBorder(long0);
        for (Direction $$5 : f_283814_) {
            if (LightEngine.QueueEntry.shouldPropagateInDirection(long1, $$5)) {
                long $$6 = BlockPos.offset(long0, $$5);
                if (((SkyLightSectionStorage) this.f_283849_).m_75791_(SectionPos.blockToSection($$6))) {
                    int $$7 = ((SkyLightSectionStorage) this.f_283849_).m_75795_($$6);
                    int $$8 = int2 - 1;
                    if ($$8 > $$7) {
                        this.mutablePos.set($$6);
                        BlockState $$9 = this.m_284512_(this.mutablePos);
                        int $$10 = int2 - this.m_284404_($$9, this.mutablePos);
                        if ($$10 > $$7) {
                            if ($$3 == null) {
                                $$3 = LightEngine.QueueEntry.isFromEmptyShape(long1) ? Blocks.AIR.defaultBlockState() : this.m_284512_(this.mutablePos.set(long0));
                            }
                            if (!this.m_284187_(long0, $$3, $$6, $$9, $$5)) {
                                ((SkyLightSectionStorage) this.f_283849_).m_75772_($$6, $$10);
                                if ($$10 > 1) {
                                    this.m_284218_($$6, LightEngine.QueueEntry.increaseSkipOneDirection($$10, m_284265_($$9), $$5.getOpposite()));
                                }
                                this.propagateFromEmptySections($$6, $$5, $$10, true, $$4);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void propagateDecrease(long long0, long long1) {
        int $$2 = this.countEmptySectionsBelowIfAtBorder(long0);
        int $$3 = LightEngine.QueueEntry.getFromLevel(long1);
        for (Direction $$4 : f_283814_) {
            if (LightEngine.QueueEntry.shouldPropagateInDirection(long1, $$4)) {
                long $$5 = BlockPos.offset(long0, $$4);
                if (((SkyLightSectionStorage) this.f_283849_).m_75791_(SectionPos.blockToSection($$5))) {
                    int $$6 = ((SkyLightSectionStorage) this.f_283849_).m_75795_($$5);
                    if ($$6 != 0) {
                        if ($$6 <= $$3 - 1) {
                            ((SkyLightSectionStorage) this.f_283849_).m_75772_($$5, 0);
                            this.m_284343_($$5, LightEngine.QueueEntry.decreaseSkipOneDirection($$6, $$4.getOpposite()));
                            this.propagateFromEmptySections($$5, $$4, $$6, false, $$2);
                        } else {
                            this.m_284218_($$5, LightEngine.QueueEntry.increaseOnlyOneDirection($$6, false, $$4.getOpposite()));
                        }
                    }
                }
            }
        }
    }

    private int countEmptySectionsBelowIfAtBorder(long long0) {
        int $$1 = BlockPos.getY(long0);
        int $$2 = SectionPos.sectionRelative($$1);
        if ($$2 != 0) {
            return 0;
        } else {
            int $$3 = BlockPos.getX(long0);
            int $$4 = BlockPos.getZ(long0);
            int $$5 = SectionPos.sectionRelative($$3);
            int $$6 = SectionPos.sectionRelative($$4);
            if ($$5 != 0 && $$5 != 15 && $$6 != 0 && $$6 != 15) {
                return 0;
            } else {
                int $$7 = SectionPos.blockToSectionCoord($$3);
                int $$8 = SectionPos.blockToSectionCoord($$1);
                int $$9 = SectionPos.blockToSectionCoord($$4);
                int $$10 = 0;
                while (!((SkyLightSectionStorage) this.f_283849_).m_75791_(SectionPos.asLong($$7, $$8 - $$10 - 1, $$9)) && ((SkyLightSectionStorage) this.f_283849_).hasLightDataAtOrBelow($$8 - $$10 - 1)) {
                    $$10++;
                }
                return $$10;
            }
        }
    }

    private void propagateFromEmptySections(long long0, Direction direction1, int int2, boolean boolean3, int int4) {
        if (int4 != 0) {
            int $$5 = BlockPos.getX(long0);
            int $$6 = BlockPos.getZ(long0);
            if (crossedSectionEdge(direction1, SectionPos.sectionRelative($$5), SectionPos.sectionRelative($$6))) {
                int $$7 = BlockPos.getY(long0);
                int $$8 = SectionPos.blockToSectionCoord($$5);
                int $$9 = SectionPos.blockToSectionCoord($$6);
                int $$10 = SectionPos.blockToSectionCoord($$7) - 1;
                int $$11 = $$10 - int4 + 1;
                while ($$10 >= $$11) {
                    if (!((SkyLightSectionStorage) this.f_283849_).m_75791_(SectionPos.asLong($$8, $$10, $$9))) {
                        $$10--;
                    } else {
                        int $$12 = SectionPos.sectionToBlockCoord($$10);
                        for (int $$13 = 15; $$13 >= 0; $$13--) {
                            long $$14 = BlockPos.asLong($$5, $$12 + $$13, $$6);
                            if (boolean3) {
                                ((SkyLightSectionStorage) this.f_283849_).m_75772_($$14, int2);
                                if (int2 > 1) {
                                    this.m_284218_($$14, LightEngine.QueueEntry.increaseSkipOneDirection(int2, true, direction1.getOpposite()));
                                }
                            } else {
                                ((SkyLightSectionStorage) this.f_283849_).m_75772_($$14, 0);
                                this.m_284343_($$14, LightEngine.QueueEntry.decreaseSkipOneDirection(int2, direction1.getOpposite()));
                            }
                        }
                        $$10--;
                    }
                }
            }
        }
    }

    private static boolean crossedSectionEdge(Direction direction0, int int1, int int2) {
        return switch(direction0) {
            case NORTH ->
                int2 == 15;
            case SOUTH ->
                int2 == 0;
            case WEST ->
                int1 == 15;
            case EAST ->
                int1 == 0;
            default ->
                false;
        };
    }

    @Override
    public void setLightEnabled(ChunkPos chunkPos0, boolean boolean1) {
        super.setLightEnabled(chunkPos0, boolean1);
        if (boolean1) {
            ChunkSkyLightSources $$2 = (ChunkSkyLightSources) Objects.requireNonNullElse(this.getChunkSources(chunkPos0.x, chunkPos0.z), this.emptyChunkSources);
            int $$3 = $$2.getHighestLowestSourceY() - 1;
            int $$4 = SectionPos.blockToSectionCoord($$3) + 1;
            long $$5 = SectionPos.getZeroNode(chunkPos0.x, chunkPos0.z);
            int $$6 = ((SkyLightSectionStorage) this.f_283849_).getTopSectionY($$5);
            int $$7 = Math.max(((SkyLightSectionStorage) this.f_283849_).getBottomSectionY(), $$4);
            for (int $$8 = $$6 - 1; $$8 >= $$7; $$8--) {
                DataLayer $$9 = ((SkyLightSectionStorage) this.f_283849_).m_284157_(SectionPos.asLong(chunkPos0.x, $$8, chunkPos0.z));
                if ($$9 != null && $$9.isEmpty()) {
                    $$9.fill(15);
                }
            }
        }
    }

    @Override
    public void propagateLightSources(ChunkPos chunkPos0) {
        long $$1 = SectionPos.getZeroNode(chunkPos0.x, chunkPos0.z);
        ((SkyLightSectionStorage) this.f_283849_).m_284259_($$1, true);
        ChunkSkyLightSources $$2 = (ChunkSkyLightSources) Objects.requireNonNullElse(this.getChunkSources(chunkPos0.x, chunkPos0.z), this.emptyChunkSources);
        ChunkSkyLightSources $$3 = (ChunkSkyLightSources) Objects.requireNonNullElse(this.getChunkSources(chunkPos0.x, chunkPos0.z - 1), this.emptyChunkSources);
        ChunkSkyLightSources $$4 = (ChunkSkyLightSources) Objects.requireNonNullElse(this.getChunkSources(chunkPos0.x, chunkPos0.z + 1), this.emptyChunkSources);
        ChunkSkyLightSources $$5 = (ChunkSkyLightSources) Objects.requireNonNullElse(this.getChunkSources(chunkPos0.x - 1, chunkPos0.z), this.emptyChunkSources);
        ChunkSkyLightSources $$6 = (ChunkSkyLightSources) Objects.requireNonNullElse(this.getChunkSources(chunkPos0.x + 1, chunkPos0.z), this.emptyChunkSources);
        int $$7 = ((SkyLightSectionStorage) this.f_283849_).getTopSectionY($$1);
        int $$8 = ((SkyLightSectionStorage) this.f_283849_).getBottomSectionY();
        int $$9 = SectionPos.sectionToBlockCoord(chunkPos0.x);
        int $$10 = SectionPos.sectionToBlockCoord(chunkPos0.z);
        for (int $$11 = $$7 - 1; $$11 >= $$8; $$11--) {
            long $$12 = SectionPos.asLong(chunkPos0.x, $$11, chunkPos0.z);
            DataLayer $$13 = ((SkyLightSectionStorage) this.f_283849_).m_284157_($$12);
            if ($$13 != null) {
                int $$14 = SectionPos.sectionToBlockCoord($$11);
                int $$15 = $$14 + 15;
                boolean $$16 = false;
                for (int $$17 = 0; $$17 < 16; $$17++) {
                    for (int $$18 = 0; $$18 < 16; $$18++) {
                        int $$19 = $$2.getLowestSourceY($$18, $$17);
                        if ($$19 <= $$15) {
                            int $$20 = $$17 == 0 ? $$3.getLowestSourceY($$18, 15) : $$2.getLowestSourceY($$18, $$17 - 1);
                            int $$21 = $$17 == 15 ? $$4.getLowestSourceY($$18, 0) : $$2.getLowestSourceY($$18, $$17 + 1);
                            int $$22 = $$18 == 0 ? $$5.getLowestSourceY(15, $$17) : $$2.getLowestSourceY($$18 - 1, $$17);
                            int $$23 = $$18 == 15 ? $$6.getLowestSourceY(0, $$17) : $$2.getLowestSourceY($$18 + 1, $$17);
                            int $$24 = Math.max(Math.max($$20, $$21), Math.max($$22, $$23));
                            for (int $$25 = $$15; $$25 >= Math.max($$14, $$19); $$25--) {
                                $$13.set($$18, SectionPos.sectionRelative($$25), $$17, 15);
                                if ($$25 == $$19 || $$25 < $$24) {
                                    long $$26 = BlockPos.asLong($$9 + $$18, $$25, $$10 + $$17);
                                    this.m_284218_($$26, LightEngine.QueueEntry.increaseSkySourceInDirections($$25 == $$19, $$25 < $$20, $$25 < $$21, $$25 < $$22, $$25 < $$23));
                                }
                            }
                            if ($$19 < $$14) {
                                $$16 = true;
                            }
                        }
                    }
                }
                if (!$$16) {
                    break;
                }
            }
        }
    }
}
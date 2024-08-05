package net.minecraft.world.level.levelgen;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.BitStorage;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.slf4j.Logger;

public class Heightmap {

    private static final Logger LOGGER = LogUtils.getLogger();

    static final Predicate<BlockState> NOT_AIR = p_284913_ -> !p_284913_.m_60795_();

    static final Predicate<BlockState> MATERIAL_MOTION_BLOCKING = BlockBehaviour.BlockStateBase::m_280555_;

    private final BitStorage data;

    private final Predicate<BlockState> isOpaque;

    private final ChunkAccess chunk;

    public Heightmap(ChunkAccess chunkAccess0, Heightmap.Types heightmapTypes1) {
        this.isOpaque = heightmapTypes1.isOpaque();
        this.chunk = chunkAccess0;
        int $$2 = Mth.ceillog2(chunkAccess0.getHeight() + 1);
        this.data = new SimpleBitStorage($$2, 256);
    }

    public static void primeHeightmaps(ChunkAccess chunkAccess0, Set<Heightmap.Types> setHeightmapTypes1) {
        int $$2 = setHeightmapTypes1.size();
        ObjectList<Heightmap> $$3 = new ObjectArrayList($$2);
        ObjectListIterator<Heightmap> $$4 = $$3.iterator();
        int $$5 = chunkAccess0.getHighestSectionPosition() + 16;
        BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos();
        for (int $$7 = 0; $$7 < 16; $$7++) {
            for (int $$8 = 0; $$8 < 16; $$8++) {
                for (Heightmap.Types $$9 : setHeightmapTypes1) {
                    $$3.add(chunkAccess0.getOrCreateHeightmapUnprimed($$9));
                }
                for (int $$10 = $$5 - 1; $$10 >= chunkAccess0.getMinBuildHeight(); $$10--) {
                    $$6.set($$7, $$10, $$8);
                    BlockState $$11 = chunkAccess0.m_8055_($$6);
                    if (!$$11.m_60713_(Blocks.AIR)) {
                        while ($$4.hasNext()) {
                            Heightmap $$12 = (Heightmap) $$4.next();
                            if ($$12.isOpaque.test($$11)) {
                                $$12.setHeight($$7, $$8, $$10 + 1);
                                $$4.remove();
                            }
                        }
                        if ($$3.isEmpty()) {
                            break;
                        }
                        $$4.back($$2);
                    }
                }
            }
        }
    }

    public boolean update(int int0, int int1, int int2, BlockState blockState3) {
        int $$4 = this.getFirstAvailable(int0, int2);
        if (int1 <= $$4 - 2) {
            return false;
        } else {
            if (this.isOpaque.test(blockState3)) {
                if (int1 >= $$4) {
                    this.setHeight(int0, int2, int1 + 1);
                    return true;
                }
            } else if ($$4 - 1 == int1) {
                BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
                for (int $$6 = int1 - 1; $$6 >= this.chunk.getMinBuildHeight(); $$6--) {
                    $$5.set(int0, $$6, int2);
                    if (this.isOpaque.test(this.chunk.m_8055_($$5))) {
                        this.setHeight(int0, int2, $$6 + 1);
                        return true;
                    }
                }
                this.setHeight(int0, int2, this.chunk.getMinBuildHeight());
                return true;
            }
            return false;
        }
    }

    public int getFirstAvailable(int int0, int int1) {
        return this.getFirstAvailable(getIndex(int0, int1));
    }

    public int getHighestTaken(int int0, int int1) {
        return this.getFirstAvailable(getIndex(int0, int1)) - 1;
    }

    private int getFirstAvailable(int int0) {
        return this.data.get(int0) + this.chunk.getMinBuildHeight();
    }

    private void setHeight(int int0, int int1, int int2) {
        this.data.set(getIndex(int0, int1), int2 - this.chunk.getMinBuildHeight());
    }

    public void setRawData(ChunkAccess chunkAccess0, Heightmap.Types heightmapTypes1, long[] long2) {
        long[] $$3 = this.data.getRaw();
        if ($$3.length == long2.length) {
            System.arraycopy(long2, 0, $$3, 0, long2.length);
        } else {
            LOGGER.warn("Ignoring heightmap data for chunk " + chunkAccess0.getPos() + ", size does not match; expected: " + $$3.length + ", got: " + long2.length);
            primeHeightmaps(chunkAccess0, EnumSet.of(heightmapTypes1));
        }
    }

    public long[] getRawData() {
        return this.data.getRaw();
    }

    private static int getIndex(int int0, int int1) {
        return int0 + int1 * 16;
    }

    public static enum Types implements StringRepresentable {

        WORLD_SURFACE_WG("WORLD_SURFACE_WG", Heightmap.Usage.WORLDGEN, Heightmap.NOT_AIR),
        WORLD_SURFACE("WORLD_SURFACE", Heightmap.Usage.CLIENT, Heightmap.NOT_AIR),
        OCEAN_FLOOR_WG("OCEAN_FLOOR_WG", Heightmap.Usage.WORLDGEN, Heightmap.MATERIAL_MOTION_BLOCKING),
        OCEAN_FLOOR("OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING),
        MOTION_BLOCKING("MOTION_BLOCKING", Heightmap.Usage.CLIENT, p_284915_ -> p_284915_.m_280555_() || !p_284915_.m_60819_().isEmpty()),
        MOTION_BLOCKING_NO_LEAVES("MOTION_BLOCKING_NO_LEAVES", Heightmap.Usage.LIVE_WORLD, p_284914_ -> (p_284914_.m_280555_() || !p_284914_.m_60819_().isEmpty()) && !(p_284914_.m_60734_() instanceof LeavesBlock));

        public static final Codec<Heightmap.Types> CODEC = StringRepresentable.fromEnum(Heightmap.Types::values);

        private final String serializationKey;

        private final Heightmap.Usage usage;

        private final Predicate<BlockState> isOpaque;

        private Types(String p_64284_, Heightmap.Usage p_64285_, Predicate<BlockState> p_64286_) {
            this.serializationKey = p_64284_;
            this.usage = p_64285_;
            this.isOpaque = p_64286_;
        }

        public String getSerializationKey() {
            return this.serializationKey;
        }

        public boolean sendToClient() {
            return this.usage == Heightmap.Usage.CLIENT;
        }

        public boolean keepAfterWorldgen() {
            return this.usage != Heightmap.Usage.WORLDGEN;
        }

        public Predicate<BlockState> isOpaque() {
            return this.isOpaque;
        }

        @Override
        public String getSerializedName() {
            return this.serializationKey;
        }
    }

    public static enum Usage {

        WORLDGEN, LIVE_WORLD, CLIENT
    }
}
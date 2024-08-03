package net.mehvahdjukaar.supplementaries.common.block.tiles;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.worldgen.RoadSignFeature;
import net.mehvahdjukaar.supplementaries.common.worldgen.StructureLocator;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGeneratorBlockTile extends BlockEntity {

    private static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();

    private CompletableFuture<List<StructureLocator.LocatedStruct>> threadResult;

    private boolean firstTick = true;

    private RoadSignFeature.Config config = null;

    public BlockGeneratorBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.BLOCK_GENERATOR_TILE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BlockGeneratorBlockTile tile) {
        if (tile.firstTick) {
            tile.firstTick = false;
            tile.threadResult = CompletableFuture.supplyAsync(() -> StructureLocator.findNearestMapFeatures((ServerLevel) level, ModTags.WAY_SIGN_DESTINATIONS, pos, 250, false, 2, 8), EXECUTORS).exceptionally(exception -> {
                Supplementaries.LOGGER.error("Failed to generate road sign at " + pos + ": " + exception);
                return null;
            });
        } else if (tile.config != null && tile.threadResult != null && !tile.threadResult.isCompletedExceptionally()) {
            if (tile.threadResult.isDone()) {
                try {
                    RoadSignFeature.applyPostProcess(tile.config, (ServerLevel) level, pos, (List<StructureLocator.LocatedStruct>) tile.threadResult.get());
                } catch (Exception var5) {
                    level.removeBlock(pos, false);
                    Supplementaries.LOGGER.error("Failed to generate road sign at " + pos + ": " + var5);
                }
            }
        } else {
            level.removeBlock(pos, false);
        }
    }

    public void setConfig(RoadSignFeature.Config c) {
        this.config = c;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.config != null) {
            tag.put("config", (Tag) RoadSignFeature.Config.CODEC.encodeStart(NbtOps.INSTANCE, this.config).getOrThrow(false, s -> {
            }));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("config")) {
            this.config = (RoadSignFeature.Config) ((Pair) RoadSignFeature.Config.CODEC.decode(NbtOps.INSTANCE, tag.get("config")).getOrThrow(true, s -> {
            })).getFirst();
        }
    }
}
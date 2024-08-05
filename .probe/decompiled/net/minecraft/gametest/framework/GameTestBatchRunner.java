package net.minecraft.gametest.framework;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

public class GameTestBatchRunner {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final BlockPos firstTestNorthWestCorner;

    final ServerLevel level;

    private final GameTestTicker testTicker;

    private final int testsPerRow;

    private final List<GameTestInfo> allTestInfos;

    private final List<Pair<GameTestBatch, Collection<GameTestInfo>>> batches;

    private final BlockPos.MutableBlockPos nextTestNorthWestCorner;

    public GameTestBatchRunner(Collection<GameTestBatch> collectionGameTestBatch0, BlockPos blockPos1, Rotation rotation2, ServerLevel serverLevel3, GameTestTicker gameTestTicker4, int int5) {
        this.nextTestNorthWestCorner = blockPos1.mutable();
        this.firstTestNorthWestCorner = blockPos1;
        this.level = serverLevel3;
        this.testTicker = gameTestTicker4;
        this.testsPerRow = int5;
        this.batches = (List<Pair<GameTestBatch, Collection<GameTestInfo>>>) collectionGameTestBatch0.stream().map(p_177068_ -> {
            Collection<GameTestInfo> $$3 = (Collection<GameTestInfo>) p_177068_.getTestFunctions().stream().map(p_177072_ -> new GameTestInfo(p_177072_, rotation2, serverLevel3)).collect(ImmutableList.toImmutableList());
            return Pair.of(p_177068_, $$3);
        }).collect(ImmutableList.toImmutableList());
        this.allTestInfos = (List<GameTestInfo>) this.batches.stream().flatMap(p_177074_ -> ((Collection) p_177074_.getSecond()).stream()).collect(ImmutableList.toImmutableList());
    }

    public List<GameTestInfo> getTestInfos() {
        return this.allTestInfos;
    }

    public void start() {
        this.runBatch(0);
    }

    void runBatch(final int int0) {
        if (int0 < this.batches.size()) {
            Pair<GameTestBatch, Collection<GameTestInfo>> $$1 = (Pair<GameTestBatch, Collection<GameTestInfo>>) this.batches.get(int0);
            final GameTestBatch $$2 = (GameTestBatch) $$1.getFirst();
            Collection<GameTestInfo> $$3 = (Collection<GameTestInfo>) $$1.getSecond();
            Map<GameTestInfo, BlockPos> $$4 = this.createStructuresForBatch($$3);
            String $$5 = $$2.getName();
            LOGGER.info("Running test batch '{}' ({} tests)...", $$5, $$3.size());
            $$2.runBeforeBatchFunction(this.level);
            final MultipleTestTracker $$6 = new MultipleTestTracker();
            $$3.forEach($$6::m_127809_);
            $$6.addListener(new GameTestListener() {

                private void testCompleted() {
                    if ($$6.isDone()) {
                        $$2.runAfterBatchFunction(GameTestBatchRunner.this.level);
                        GameTestBatchRunner.this.runBatch(int0 + 1);
                    }
                }

                @Override
                public void testStructureLoaded(GameTestInfo p_127590_) {
                }

                @Override
                public void testPassed(GameTestInfo p_177090_) {
                    this.testCompleted();
                }

                @Override
                public void testFailed(GameTestInfo p_127592_) {
                    this.testCompleted();
                }
            });
            $$3.forEach(p_177079_ -> {
                BlockPos $$2x = (BlockPos) $$4.get(p_177079_);
                GameTestRunner.runTest(p_177079_, $$2x, this.testTicker);
            });
        }
    }

    private Map<GameTestInfo, BlockPos> createStructuresForBatch(Collection<GameTestInfo> collectionGameTestInfo0) {
        Map<GameTestInfo, BlockPos> $$1 = Maps.newHashMap();
        int $$2 = 0;
        AABB $$3 = new AABB(this.nextTestNorthWestCorner);
        for (GameTestInfo $$4 : collectionGameTestInfo0) {
            BlockPos $$5 = new BlockPos(this.nextTestNorthWestCorner);
            StructureBlockEntity $$6 = StructureUtils.spawnStructure($$4.getStructureName(), $$5, $$4.getRotation(), 2, this.level, true);
            AABB $$7 = StructureUtils.getStructureBounds($$6);
            $$4.setStructureBlockPos($$6.m_58899_());
            $$1.put($$4, new BlockPos(this.nextTestNorthWestCorner));
            $$3 = $$3.minmax($$7);
            this.nextTestNorthWestCorner.move((int) $$7.getXsize() + 5, 0, 0);
            if ($$2++ % this.testsPerRow == this.testsPerRow - 1) {
                this.nextTestNorthWestCorner.move(0, 0, (int) $$3.getZsize() + 6);
                this.nextTestNorthWestCorner.setX(this.firstTestNorthWestCorner.m_123341_());
                $$3 = new AABB(this.nextTestNorthWestCorner);
            }
        }
        return $$1;
    }
}
package net.minecraft.gametest.framework;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.apache.commons.lang3.mutable.MutableInt;

public class GameTestRunner {

    private static final int MAX_TESTS_PER_BATCH = 100;

    public static final int PADDING_AROUND_EACH_STRUCTURE = 2;

    public static final int SPACE_BETWEEN_COLUMNS = 5;

    public static final int SPACE_BETWEEN_ROWS = 6;

    public static final int DEFAULT_TESTS_PER_ROW = 8;

    public static void runTest(GameTestInfo gameTestInfo0, BlockPos blockPos1, GameTestTicker gameTestTicker2) {
        gameTestInfo0.startExecution();
        gameTestTicker2.add(gameTestInfo0);
        gameTestInfo0.addListener(new ReportGameListener(gameTestInfo0, gameTestTicker2, blockPos1));
        gameTestInfo0.spawnStructure(blockPos1, 2);
    }

    public static Collection<GameTestInfo> runTestBatches(Collection<GameTestBatch> collectionGameTestBatch0, BlockPos blockPos1, Rotation rotation2, ServerLevel serverLevel3, GameTestTicker gameTestTicker4, int int5) {
        GameTestBatchRunner $$6 = new GameTestBatchRunner(collectionGameTestBatch0, blockPos1, rotation2, serverLevel3, gameTestTicker4, int5);
        $$6.start();
        return $$6.getTestInfos();
    }

    public static Collection<GameTestInfo> runTests(Collection<TestFunction> collectionTestFunction0, BlockPos blockPos1, Rotation rotation2, ServerLevel serverLevel3, GameTestTicker gameTestTicker4, int int5) {
        return runTestBatches(groupTestsIntoBatches(collectionTestFunction0), blockPos1, rotation2, serverLevel3, gameTestTicker4, int5);
    }

    public static Collection<GameTestBatch> groupTestsIntoBatches(Collection<TestFunction> collectionTestFunction0) {
        Map<String, List<TestFunction>> $$1 = (Map<String, List<TestFunction>>) collectionTestFunction0.stream().collect(Collectors.groupingBy(TestFunction::m_128081_));
        return (Collection<GameTestBatch>) $$1.entrySet().stream().flatMap(p_177537_ -> {
            String $$1x = (String) p_177537_.getKey();
            Consumer<ServerLevel> $$2 = GameTestRegistry.getBeforeBatchFunction($$1x);
            Consumer<ServerLevel> $$3 = GameTestRegistry.getAfterBatchFunction($$1x);
            MutableInt $$4 = new MutableInt();
            Collection<TestFunction> $$5 = (Collection<TestFunction>) p_177537_.getValue();
            return Streams.stream(Iterables.partition($$5, 100)).map(p_177535_ -> new GameTestBatch($$1x + ":" + $$4.incrementAndGet(), ImmutableList.copyOf(p_177535_), $$2, $$3));
        }).collect(ImmutableList.toImmutableList());
    }

    public static void clearAllTests(ServerLevel serverLevel0, BlockPos blockPos1, GameTestTicker gameTestTicker2, int int3) {
        gameTestTicker2.clear();
        BlockPos $$4 = blockPos1.offset(-int3, 0, -int3);
        BlockPos $$5 = blockPos1.offset(int3, 0, int3);
        BlockPos.betweenClosedStream($$4, $$5).filter(p_177540_ -> serverLevel0.m_8055_(p_177540_).m_60713_(Blocks.STRUCTURE_BLOCK)).forEach(p_177529_ -> {
            StructureBlockEntity $$2 = (StructureBlockEntity) serverLevel0.m_7702_(p_177529_);
            BlockPos $$3 = $$2.m_58899_();
            BoundingBox $$4x = StructureUtils.getStructureBoundingBox($$2);
            StructureUtils.clearSpaceForStructure($$4x, $$3.m_123342_(), serverLevel0);
        });
    }

    public static void clearMarkers(ServerLevel serverLevel0) {
        DebugPackets.sendGameTestClearPacket(serverLevel0);
    }
}
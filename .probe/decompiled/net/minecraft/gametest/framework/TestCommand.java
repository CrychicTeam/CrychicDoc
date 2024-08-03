package net.minecraft.gametest.framework;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.structures.NbtToSnbt;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.io.IOUtils;

public class TestCommand {

    private static final int DEFAULT_CLEAR_RADIUS = 200;

    private static final int MAX_CLEAR_RADIUS = 1024;

    private static final int STRUCTURE_BLOCK_NEARBY_SEARCH_RADIUS = 15;

    private static final int STRUCTURE_BLOCK_FULL_SEARCH_RADIUS = 200;

    private static final int TEST_POS_Z_OFFSET_FROM_PLAYER = 3;

    private static final int SHOW_POS_DURATION_MS = 10000;

    private static final int DEFAULT_X_SIZE = 5;

    private static final int DEFAULT_Y_SIZE = 5;

    private static final int DEFAULT_Z_SIZE = 5;

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("test").then(Commands.literal("runthis").executes(p_128057_ -> runNearbyTest((CommandSourceStack) p_128057_.getSource())))).then(Commands.literal("runthese").executes(p_128055_ -> runAllNearbyTests((CommandSourceStack) p_128055_.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("runfailed").executes(p_128053_ -> runLastFailedTests((CommandSourceStack) p_128053_.getSource(), false, 0, 8))).then(((RequiredArgumentBuilder) Commands.argument("onlyRequiredTests", BoolArgumentType.bool()).executes(p_128051_ -> runLastFailedTests((CommandSourceStack) p_128051_.getSource(), BoolArgumentType.getBool(p_128051_, "onlyRequiredTests"), 0, 8))).then(((RequiredArgumentBuilder) Commands.argument("rotationSteps", IntegerArgumentType.integer()).executes(p_128049_ -> runLastFailedTests((CommandSourceStack) p_128049_.getSource(), BoolArgumentType.getBool(p_128049_, "onlyRequiredTests"), IntegerArgumentType.getInteger(p_128049_, "rotationSteps"), 8))).then(Commands.argument("testsPerRow", IntegerArgumentType.integer()).executes(p_128047_ -> runLastFailedTests((CommandSourceStack) p_128047_.getSource(), BoolArgumentType.getBool(p_128047_, "onlyRequiredTests"), IntegerArgumentType.getInteger(p_128047_, "rotationSteps"), IntegerArgumentType.getInteger(p_128047_, "testsPerRow")))))))).then(Commands.literal("run").then(((RequiredArgumentBuilder) Commands.argument("testName", TestFunctionArgument.testFunctionArgument()).executes(p_128045_ -> runTest((CommandSourceStack) p_128045_.getSource(), TestFunctionArgument.getTestFunction(p_128045_, "testName"), 0))).then(Commands.argument("rotationSteps", IntegerArgumentType.integer()).executes(p_128043_ -> runTest((CommandSourceStack) p_128043_.getSource(), TestFunctionArgument.getTestFunction(p_128043_, "testName"), IntegerArgumentType.getInteger(p_128043_, "rotationSteps"))))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("runall").executes(p_128041_ -> runAllTests((CommandSourceStack) p_128041_.getSource(), 0, 8))).then(((RequiredArgumentBuilder) Commands.argument("testClassName", TestClassNameArgument.testClassName()).executes(p_128039_ -> runAllTestsInClass((CommandSourceStack) p_128039_.getSource(), TestClassNameArgument.getTestClassName(p_128039_, "testClassName"), 0, 8))).then(((RequiredArgumentBuilder) Commands.argument("rotationSteps", IntegerArgumentType.integer()).executes(p_128037_ -> runAllTestsInClass((CommandSourceStack) p_128037_.getSource(), TestClassNameArgument.getTestClassName(p_128037_, "testClassName"), IntegerArgumentType.getInteger(p_128037_, "rotationSteps"), 8))).then(Commands.argument("testsPerRow", IntegerArgumentType.integer()).executes(p_128035_ -> runAllTestsInClass((CommandSourceStack) p_128035_.getSource(), TestClassNameArgument.getTestClassName(p_128035_, "testClassName"), IntegerArgumentType.getInteger(p_128035_, "rotationSteps"), IntegerArgumentType.getInteger(p_128035_, "testsPerRow"))))))).then(((RequiredArgumentBuilder) Commands.argument("rotationSteps", IntegerArgumentType.integer()).executes(p_128033_ -> runAllTests((CommandSourceStack) p_128033_.getSource(), IntegerArgumentType.getInteger(p_128033_, "rotationSteps"), 8))).then(Commands.argument("testsPerRow", IntegerArgumentType.integer()).executes(p_128031_ -> runAllTests((CommandSourceStack) p_128031_.getSource(), IntegerArgumentType.getInteger(p_128031_, "rotationSteps"), IntegerArgumentType.getInteger(p_128031_, "testsPerRow"))))))).then(Commands.literal("export").then(Commands.argument("testName", StringArgumentType.word()).executes(p_128029_ -> exportTestStructure((CommandSourceStack) p_128029_.getSource(), StringArgumentType.getString(p_128029_, "testName")))))).then(Commands.literal("exportthis").executes(p_128027_ -> exportNearestTestStructure((CommandSourceStack) p_128027_.getSource())))).then(Commands.literal("import").then(Commands.argument("testName", StringArgumentType.word()).executes(p_128025_ -> importTestStructure((CommandSourceStack) p_128025_.getSource(), StringArgumentType.getString(p_128025_, "testName")))))).then(((LiteralArgumentBuilder) Commands.literal("pos").executes(p_128023_ -> showPos((CommandSourceStack) p_128023_.getSource(), "pos"))).then(Commands.argument("var", StringArgumentType.word()).executes(p_128021_ -> showPos((CommandSourceStack) p_128021_.getSource(), StringArgumentType.getString(p_128021_, "var")))))).then(Commands.literal("create").then(((RequiredArgumentBuilder) Commands.argument("testName", StringArgumentType.word()).executes(p_128019_ -> createNewStructure((CommandSourceStack) p_128019_.getSource(), StringArgumentType.getString(p_128019_, "testName"), 5, 5, 5))).then(((RequiredArgumentBuilder) Commands.argument("width", IntegerArgumentType.integer()).executes(p_128014_ -> createNewStructure((CommandSourceStack) p_128014_.getSource(), StringArgumentType.getString(p_128014_, "testName"), IntegerArgumentType.getInteger(p_128014_, "width"), IntegerArgumentType.getInteger(p_128014_, "width"), IntegerArgumentType.getInteger(p_128014_, "width")))).then(Commands.argument("height", IntegerArgumentType.integer()).then(Commands.argument("depth", IntegerArgumentType.integer()).executes(p_128007_ -> createNewStructure((CommandSourceStack) p_128007_.getSource(), StringArgumentType.getString(p_128007_, "testName"), IntegerArgumentType.getInteger(p_128007_, "width"), IntegerArgumentType.getInteger(p_128007_, "height"), IntegerArgumentType.getInteger(p_128007_, "depth"))))))))).then(((LiteralArgumentBuilder) Commands.literal("clearall").executes(p_128000_ -> clearAllTests((CommandSourceStack) p_128000_.getSource(), 200))).then(Commands.argument("radius", IntegerArgumentType.integer()).executes(p_127949_ -> clearAllTests((CommandSourceStack) p_127949_.getSource(), IntegerArgumentType.getInteger(p_127949_, "radius"))))));
    }

    private static int createNewStructure(CommandSourceStack commandSourceStack0, String string1, int int2, int int3, int int4) {
        if (int2 <= 48 && int3 <= 48 && int4 <= 48) {
            ServerLevel $$5 = commandSourceStack0.getLevel();
            BlockPos $$6 = BlockPos.containing(commandSourceStack0.getPosition());
            BlockPos $$7 = new BlockPos($$6.m_123341_(), commandSourceStack0.getLevel().m_5452_(Heightmap.Types.WORLD_SURFACE, $$6).m_123342_(), $$6.m_123343_() + 3);
            StructureUtils.createNewEmptyStructureBlock(string1.toLowerCase(), $$7, new Vec3i(int2, int3, int4), Rotation.NONE, $$5);
            for (int $$8 = 0; $$8 < int2; $$8++) {
                for (int $$9 = 0; $$9 < int4; $$9++) {
                    BlockPos $$10 = new BlockPos($$7.m_123341_() + $$8, $$7.m_123342_() + 1, $$7.m_123343_() + $$9);
                    Block $$11 = Blocks.POLISHED_ANDESITE;
                    BlockInput $$12 = new BlockInput($$11.defaultBlockState(), Collections.emptySet(), null);
                    $$12.place($$5, $$10, 2);
                }
            }
            StructureUtils.addCommandBlockAndButtonToStartTest($$7, new BlockPos(1, 0, -1), Rotation.NONE, $$5);
            return 0;
        } else {
            throw new IllegalArgumentException("The structure must be less than 48 blocks big in each axis");
        }
    }

    private static int showPos(CommandSourceStack commandSourceStack0, String string1) throws CommandSyntaxException {
        BlockHitResult $$2 = (BlockHitResult) commandSourceStack0.getPlayerOrException().m_19907_(10.0, 1.0F, false);
        BlockPos $$3 = $$2.getBlockPos();
        ServerLevel $$4 = commandSourceStack0.getLevel();
        Optional<BlockPos> $$5 = StructureUtils.findStructureBlockContainingPos($$3, 15, $$4);
        if (!$$5.isPresent()) {
            $$5 = StructureUtils.findStructureBlockContainingPos($$3, 200, $$4);
        }
        if (!$$5.isPresent()) {
            commandSourceStack0.sendFailure(Component.literal("Can't find a structure block that contains the targeted pos " + $$3));
            return 0;
        } else {
            StructureBlockEntity $$6 = (StructureBlockEntity) $$4.m_7702_((BlockPos) $$5.get());
            BlockPos $$7 = $$3.subtract((Vec3i) $$5.get());
            String $$8 = $$7.m_123341_() + ", " + $$7.m_123342_() + ", " + $$7.m_123343_();
            String $$9 = $$6.getStructurePath();
            Component $$10 = Component.literal($$8).setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.GREEN).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy to clipboard"))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "final BlockPos " + string1 + " = new BlockPos(" + $$8 + ");")));
            commandSourceStack0.sendSuccess(() -> Component.literal("Position relative to " + $$9 + ": ").append($$10), false);
            DebugPackets.sendGameTestAddMarker($$4, new BlockPos($$3), $$8, -2147418368, 10000);
            return 1;
        }
    }

    private static int runNearbyTest(CommandSourceStack commandSourceStack0) {
        BlockPos $$1 = BlockPos.containing(commandSourceStack0.getPosition());
        ServerLevel $$2 = commandSourceStack0.getLevel();
        BlockPos $$3 = StructureUtils.findNearestStructureBlock($$1, 15, $$2);
        if ($$3 == null) {
            say($$2, "Couldn't find any structure block within 15 radius", ChatFormatting.RED);
            return 0;
        } else {
            GameTestRunner.clearMarkers($$2);
            runTest($$2, $$3, null);
            return 1;
        }
    }

    private static int runAllNearbyTests(CommandSourceStack commandSourceStack0) {
        BlockPos $$1 = BlockPos.containing(commandSourceStack0.getPosition());
        ServerLevel $$2 = commandSourceStack0.getLevel();
        Collection<BlockPos> $$3 = StructureUtils.findStructureBlocks($$1, 200, $$2);
        if ($$3.isEmpty()) {
            say($$2, "Couldn't find any structure blocks within 200 block radius", ChatFormatting.RED);
            return 1;
        } else {
            GameTestRunner.clearMarkers($$2);
            say(commandSourceStack0, "Running " + $$3.size() + " tests...");
            MultipleTestTracker $$4 = new MultipleTestTracker();
            $$3.forEach(p_127943_ -> runTest($$2, p_127943_, $$4));
            return 1;
        }
    }

    private static void runTest(ServerLevel serverLevel0, BlockPos blockPos1, @Nullable MultipleTestTracker multipleTestTracker2) {
        StructureBlockEntity $$3 = (StructureBlockEntity) serverLevel0.m_7702_(blockPos1);
        String $$4 = $$3.getStructurePath();
        TestFunction $$5 = GameTestRegistry.getTestFunction($$4);
        GameTestInfo $$6 = new GameTestInfo($$5, $$3.getRotation(), serverLevel0);
        if (multipleTestTracker2 != null) {
            multipleTestTracker2.addTestToTrack($$6);
            $$6.addListener(new TestCommand.TestSummaryDisplayer(serverLevel0, multipleTestTracker2));
        }
        runTestPreparation($$5, serverLevel0);
        AABB $$7 = StructureUtils.getStructureBounds($$3);
        BlockPos $$8 = BlockPos.containing($$7.minX, $$7.minY, $$7.minZ);
        GameTestRunner.runTest($$6, $$8, GameTestTicker.SINGLETON);
    }

    static void showTestSummaryIfAllDone(ServerLevel serverLevel0, MultipleTestTracker multipleTestTracker1) {
        if (multipleTestTracker1.isDone()) {
            say(serverLevel0, "GameTest done! " + multipleTestTracker1.getTotalCount() + " tests were run", ChatFormatting.WHITE);
            if (multipleTestTracker1.hasFailedRequired()) {
                say(serverLevel0, multipleTestTracker1.getFailedRequiredCount() + " required tests failed :(", ChatFormatting.RED);
            } else {
                say(serverLevel0, "All required tests passed :)", ChatFormatting.GREEN);
            }
            if (multipleTestTracker1.hasFailedOptional()) {
                say(serverLevel0, multipleTestTracker1.getFailedOptionalCount() + " optional tests failed", ChatFormatting.GRAY);
            }
        }
    }

    private static int clearAllTests(CommandSourceStack commandSourceStack0, int int1) {
        ServerLevel $$2 = commandSourceStack0.getLevel();
        GameTestRunner.clearMarkers($$2);
        BlockPos $$3 = BlockPos.containing(commandSourceStack0.getPosition().x, (double) commandSourceStack0.getLevel().m_5452_(Heightmap.Types.WORLD_SURFACE, BlockPos.containing(commandSourceStack0.getPosition())).m_123342_(), commandSourceStack0.getPosition().z);
        GameTestRunner.clearAllTests($$2, $$3, GameTestTicker.SINGLETON, Mth.clamp(int1, 0, 1024));
        return 1;
    }

    private static int runTest(CommandSourceStack commandSourceStack0, TestFunction testFunction1, int int2) {
        ServerLevel $$3 = commandSourceStack0.getLevel();
        BlockPos $$4 = BlockPos.containing(commandSourceStack0.getPosition());
        int $$5 = commandSourceStack0.getLevel().m_5452_(Heightmap.Types.WORLD_SURFACE, $$4).m_123342_();
        BlockPos $$6 = new BlockPos($$4.m_123341_(), $$5, $$4.m_123343_() + 3);
        GameTestRunner.clearMarkers($$3);
        runTestPreparation(testFunction1, $$3);
        Rotation $$7 = StructureUtils.getRotationForRotationSteps(int2);
        GameTestInfo $$8 = new GameTestInfo(testFunction1, $$7, $$3);
        GameTestRunner.runTest($$8, $$6, GameTestTicker.SINGLETON);
        return 1;
    }

    private static void runTestPreparation(TestFunction testFunction0, ServerLevel serverLevel1) {
        Consumer<ServerLevel> $$2 = GameTestRegistry.getBeforeBatchFunction(testFunction0.getBatchName());
        if ($$2 != null) {
            $$2.accept(serverLevel1);
        }
    }

    private static int runAllTests(CommandSourceStack commandSourceStack0, int int1, int int2) {
        GameTestRunner.clearMarkers(commandSourceStack0.getLevel());
        Collection<TestFunction> $$3 = GameTestRegistry.getAllTestFunctions();
        say(commandSourceStack0, "Running all " + $$3.size() + " tests...");
        GameTestRegistry.forgetFailedTests();
        runTests(commandSourceStack0, $$3, int1, int2);
        return 1;
    }

    private static int runAllTestsInClass(CommandSourceStack commandSourceStack0, String string1, int int2, int int3) {
        Collection<TestFunction> $$4 = GameTestRegistry.getTestFunctionsForClassName(string1);
        GameTestRunner.clearMarkers(commandSourceStack0.getLevel());
        say(commandSourceStack0, "Running " + $$4.size() + " tests from " + string1 + "...");
        GameTestRegistry.forgetFailedTests();
        runTests(commandSourceStack0, $$4, int2, int3);
        return 1;
    }

    private static int runLastFailedTests(CommandSourceStack commandSourceStack0, boolean boolean1, int int2, int int3) {
        Collection<TestFunction> $$4;
        if (boolean1) {
            $$4 = (Collection<TestFunction>) GameTestRegistry.getLastFailedTests().stream().filter(TestFunction::m_128080_).collect(Collectors.toList());
        } else {
            $$4 = GameTestRegistry.getLastFailedTests();
        }
        if ($$4.isEmpty()) {
            say(commandSourceStack0, "No failed tests to rerun");
            return 0;
        } else {
            GameTestRunner.clearMarkers(commandSourceStack0.getLevel());
            say(commandSourceStack0, "Rerunning " + $$4.size() + " failed tests (" + (boolean1 ? "only required tests" : "including optional tests") + ")");
            runTests(commandSourceStack0, $$4, int2, int3);
            return 1;
        }
    }

    private static void runTests(CommandSourceStack commandSourceStack0, Collection<TestFunction> collectionTestFunction1, int int2, int int3) {
        BlockPos $$4 = BlockPos.containing(commandSourceStack0.getPosition());
        BlockPos $$5 = new BlockPos($$4.m_123341_(), commandSourceStack0.getLevel().m_5452_(Heightmap.Types.WORLD_SURFACE, $$4).m_123342_(), $$4.m_123343_() + 3);
        ServerLevel $$6 = commandSourceStack0.getLevel();
        Rotation $$7 = StructureUtils.getRotationForRotationSteps(int2);
        Collection<GameTestInfo> $$8 = GameTestRunner.runTests(collectionTestFunction1, $$5, $$7, $$6, GameTestTicker.SINGLETON, int3);
        MultipleTestTracker $$9 = new MultipleTestTracker($$8);
        $$9.addListener(new TestCommand.TestSummaryDisplayer($$6, $$9));
        $$9.addFailureListener(p_127992_ -> GameTestRegistry.rememberFailedTest(p_127992_.getTestFunction()));
    }

    private static void say(CommandSourceStack commandSourceStack0, String string1) {
        commandSourceStack0.sendSuccess(() -> Component.literal(string1), false);
    }

    private static int exportNearestTestStructure(CommandSourceStack commandSourceStack0) {
        BlockPos $$1 = BlockPos.containing(commandSourceStack0.getPosition());
        ServerLevel $$2 = commandSourceStack0.getLevel();
        BlockPos $$3 = StructureUtils.findNearestStructureBlock($$1, 15, $$2);
        if ($$3 == null) {
            say($$2, "Couldn't find any structure block within 15 radius", ChatFormatting.RED);
            return 0;
        } else {
            StructureBlockEntity $$4 = (StructureBlockEntity) $$2.m_7702_($$3);
            String $$5 = $$4.getStructurePath();
            return exportTestStructure(commandSourceStack0, $$5);
        }
    }

    private static int exportTestStructure(CommandSourceStack commandSourceStack0, String string1) {
        Path $$2 = Paths.get(StructureUtils.testStructuresDir);
        ResourceLocation $$3 = new ResourceLocation("minecraft", string1);
        Path $$4 = commandSourceStack0.getLevel().getStructureManager().getPathToGeneratedStructure($$3, ".nbt");
        Path $$5 = NbtToSnbt.convertStructure(CachedOutput.NO_CACHE, $$4, string1, $$2);
        if ($$5 == null) {
            say(commandSourceStack0, "Failed to export " + $$4);
            return 1;
        } else {
            try {
                Files.createDirectories($$5.getParent());
            } catch (IOException var7) {
                say(commandSourceStack0, "Could not create folder " + $$5.getParent());
                var7.printStackTrace();
                return 1;
            }
            say(commandSourceStack0, "Exported " + string1 + " to " + $$5.toAbsolutePath());
            return 0;
        }
    }

    private static int importTestStructure(CommandSourceStack commandSourceStack0, String string1) {
        Path $$2 = Paths.get(StructureUtils.testStructuresDir, string1 + ".snbt");
        ResourceLocation $$3 = new ResourceLocation("minecraft", string1);
        Path $$4 = commandSourceStack0.getLevel().getStructureManager().getPathToGeneratedStructure($$3, ".nbt");
        try {
            BufferedReader $$5 = Files.newBufferedReader($$2);
            String $$6 = IOUtils.toString($$5);
            Files.createDirectories($$4.getParent());
            OutputStream $$7 = Files.newOutputStream($$4);
            try {
                NbtIo.writeCompressed(NbtUtils.snbtToStructure($$6), $$7);
            } catch (Throwable var11) {
                if ($$7 != null) {
                    try {
                        $$7.close();
                    } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                    }
                }
                throw var11;
            }
            if ($$7 != null) {
                $$7.close();
            }
            say(commandSourceStack0, "Imported to " + $$4.toAbsolutePath());
            return 0;
        } catch (CommandSyntaxException | IOException var12) {
            System.err.println("Failed to load structure " + string1);
            var12.printStackTrace();
            return 1;
        }
    }

    private static void say(ServerLevel serverLevel0, String string1, ChatFormatting chatFormatting2) {
        serverLevel0.getPlayers(p_127945_ -> true).forEach(p_127990_ -> p_127990_.sendSystemMessage(Component.literal(chatFormatting2 + string1)));
    }

    static class TestSummaryDisplayer implements GameTestListener {

        private final ServerLevel level;

        private final MultipleTestTracker tracker;

        public TestSummaryDisplayer(ServerLevel serverLevel0, MultipleTestTracker multipleTestTracker1) {
            this.level = serverLevel0;
            this.tracker = multipleTestTracker1;
        }

        @Override
        public void testStructureLoaded(GameTestInfo gameTestInfo0) {
        }

        @Override
        public void testPassed(GameTestInfo gameTestInfo0) {
            TestCommand.showTestSummaryIfAllDone(this.level, this.tracker);
        }

        @Override
        public void testFailed(GameTestInfo gameTestInfo0) {
            TestCommand.showTestSummaryIfAllDone(this.level, this.tracker);
        }
    }
}
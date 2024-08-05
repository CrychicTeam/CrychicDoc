package net.minecraft.gametest.framework;

import com.google.common.base.MoreObjects;
import java.util.Arrays;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.apache.commons.lang3.exception.ExceptionUtils;

class ReportGameListener implements GameTestListener {

    private final GameTestInfo originalTestInfo;

    private final GameTestTicker testTicker;

    private final BlockPos structurePos;

    int attempts;

    int successes;

    public ReportGameListener(GameTestInfo gameTestInfo0, GameTestTicker gameTestTicker1, BlockPos blockPos2) {
        this.originalTestInfo = gameTestInfo0;
        this.testTicker = gameTestTicker1;
        this.structurePos = blockPos2;
        this.attempts = 0;
        this.successes = 0;
    }

    @Override
    public void testStructureLoaded(GameTestInfo gameTestInfo0) {
        spawnBeacon(this.originalTestInfo, Blocks.LIGHT_GRAY_STAINED_GLASS);
        this.attempts++;
    }

    @Override
    public void testPassed(GameTestInfo gameTestInfo0) {
        this.successes++;
        if (!gameTestInfo0.isFlaky()) {
            reportPassed(gameTestInfo0, gameTestInfo0.getTestName() + " passed! (" + gameTestInfo0.getRunTime() + "ms)");
        } else {
            if (this.successes >= gameTestInfo0.requiredSuccesses()) {
                reportPassed(gameTestInfo0, gameTestInfo0 + " passed " + this.successes + " times of " + this.attempts + " attempts.");
            } else {
                say(this.originalTestInfo.getLevel(), ChatFormatting.GREEN, "Flaky test " + this.originalTestInfo + " succeeded, attempt: " + this.attempts + " successes: " + this.successes);
                this.rerunTest();
            }
        }
    }

    @Override
    public void testFailed(GameTestInfo gameTestInfo0) {
        if (!gameTestInfo0.isFlaky()) {
            reportFailure(gameTestInfo0, gameTestInfo0.getError());
        } else {
            TestFunction $$1 = this.originalTestInfo.getTestFunction();
            String $$2 = "Flaky test " + this.originalTestInfo + " failed, attempt: " + this.attempts + "/" + $$1.getMaxAttempts();
            if ($$1.getRequiredSuccesses() > 1) {
                $$2 = $$2 + ", successes: " + this.successes + " (" + $$1.getRequiredSuccesses() + " required)";
            }
            say(this.originalTestInfo.getLevel(), ChatFormatting.YELLOW, $$2);
            if (gameTestInfo0.maxAttempts() - this.attempts + this.successes >= gameTestInfo0.requiredSuccesses()) {
                this.rerunTest();
            } else {
                reportFailure(gameTestInfo0, new ExhaustedAttemptsException(this.attempts, this.successes, gameTestInfo0));
            }
        }
    }

    public static void reportPassed(GameTestInfo gameTestInfo0, String string1) {
        spawnBeacon(gameTestInfo0, Blocks.LIME_STAINED_GLASS);
        visualizePassedTest(gameTestInfo0, string1);
    }

    private static void visualizePassedTest(GameTestInfo gameTestInfo0, String string1) {
        say(gameTestInfo0.getLevel(), ChatFormatting.GREEN, string1);
        GlobalTestReporter.onTestSuccess(gameTestInfo0);
    }

    protected static void reportFailure(GameTestInfo gameTestInfo0, Throwable throwable1) {
        spawnBeacon(gameTestInfo0, gameTestInfo0.isRequired() ? Blocks.RED_STAINED_GLASS : Blocks.ORANGE_STAINED_GLASS);
        spawnLectern(gameTestInfo0, Util.describeError(throwable1));
        visualizeFailedTest(gameTestInfo0, throwable1);
    }

    protected static void visualizeFailedTest(GameTestInfo gameTestInfo0, Throwable throwable1) {
        String $$2 = throwable1.getMessage() + (throwable1.getCause() == null ? "" : " cause: " + Util.describeError(throwable1.getCause()));
        String $$3 = (gameTestInfo0.isRequired() ? "" : "(optional) ") + gameTestInfo0.getTestName() + " failed! " + $$2;
        say(gameTestInfo0.getLevel(), gameTestInfo0.isRequired() ? ChatFormatting.RED : ChatFormatting.YELLOW, $$3);
        Throwable $$4 = (Throwable) MoreObjects.firstNonNull(ExceptionUtils.getRootCause(throwable1), throwable1);
        if ($$4 instanceof GameTestAssertPosException $$5) {
            showRedBox(gameTestInfo0.getLevel(), $$5.getAbsolutePos(), $$5.getMessageToShowAtBlock());
        }
        GlobalTestReporter.onTestFailed(gameTestInfo0);
    }

    private void rerunTest() {
        this.originalTestInfo.clearStructure();
        GameTestInfo $$0 = new GameTestInfo(this.originalTestInfo.getTestFunction(), this.originalTestInfo.getRotation(), this.originalTestInfo.getLevel());
        $$0.startExecution();
        this.testTicker.add($$0);
        $$0.addListener(this);
        $$0.spawnStructure(this.structurePos, 2);
    }

    protected static void spawnBeacon(GameTestInfo gameTestInfo0, Block block1) {
        ServerLevel $$2 = gameTestInfo0.getLevel();
        BlockPos $$3 = gameTestInfo0.getStructureBlockPos();
        BlockPos $$4 = new BlockPos(-1, -1, -1);
        BlockPos $$5 = StructureTemplate.transform($$3.offset($$4), Mirror.NONE, gameTestInfo0.getRotation(), $$3);
        $$2.m_46597_($$5, Blocks.BEACON.defaultBlockState().m_60717_(gameTestInfo0.getRotation()));
        BlockPos $$6 = $$5.offset(0, 1, 0);
        $$2.m_46597_($$6, block1.defaultBlockState());
        for (int $$7 = -1; $$7 <= 1; $$7++) {
            for (int $$8 = -1; $$8 <= 1; $$8++) {
                BlockPos $$9 = $$5.offset($$7, -1, $$8);
                $$2.m_46597_($$9, Blocks.IRON_BLOCK.defaultBlockState());
            }
        }
    }

    private static void spawnLectern(GameTestInfo gameTestInfo0, String string1) {
        ServerLevel $$2 = gameTestInfo0.getLevel();
        BlockPos $$3 = gameTestInfo0.getStructureBlockPos();
        BlockPos $$4 = new BlockPos(-1, 1, -1);
        BlockPos $$5 = StructureTemplate.transform($$3.offset($$4), Mirror.NONE, gameTestInfo0.getRotation(), $$3);
        $$2.m_46597_($$5, Blocks.LECTERN.defaultBlockState().m_60717_(gameTestInfo0.getRotation()));
        BlockState $$6 = $$2.m_8055_($$5);
        ItemStack $$7 = createBook(gameTestInfo0.getTestName(), gameTestInfo0.isRequired(), string1);
        LecternBlock.tryPlaceBook(null, $$2, $$5, $$6, $$7);
    }

    private static ItemStack createBook(String string0, boolean boolean1, String string2) {
        ItemStack $$3 = new ItemStack(Items.WRITABLE_BOOK);
        ListTag $$4 = new ListTag();
        StringBuffer $$5 = new StringBuffer();
        Arrays.stream(string0.split("\\.")).forEach(p_177716_ -> $$5.append(p_177716_).append('\n'));
        if (!boolean1) {
            $$5.append("(optional)\n");
        }
        $$5.append("-------------------\n");
        $$4.add(StringTag.valueOf($$5 + string2));
        $$3.addTagElement("pages", $$4);
        return $$3;
    }

    protected static void say(ServerLevel serverLevel0, ChatFormatting chatFormatting1, String string2) {
        serverLevel0.getPlayers(p_177705_ -> true).forEach(p_177709_ -> p_177709_.sendSystemMessage(Component.literal(string2).withStyle(chatFormatting1)));
    }

    private static void showRedBox(ServerLevel serverLevel0, BlockPos blockPos1, String string2) {
        DebugPackets.sendGameTestAddMarker(serverLevel0, blockPos1, string2, -2130771968, Integer.MAX_VALUE);
    }
}
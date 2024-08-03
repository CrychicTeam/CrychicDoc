package net.minecraft.gametest.framework;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

public class GameTestInfo {

    private final TestFunction testFunction;

    @Nullable
    private BlockPos structureBlockPos;

    private final ServerLevel level;

    private final Collection<GameTestListener> listeners = Lists.newArrayList();

    private final int timeoutTicks;

    private final Collection<GameTestSequence> sequences = Lists.newCopyOnWriteArrayList();

    private final Object2LongMap<Runnable> runAtTickTimeMap = new Object2LongOpenHashMap();

    private long startTick;

    private long tickCount;

    private boolean started;

    private final Stopwatch timer = Stopwatch.createUnstarted();

    private boolean done;

    private final Rotation rotation;

    @Nullable
    private Throwable error;

    @Nullable
    private StructureBlockEntity structureBlockEntity;

    public GameTestInfo(TestFunction testFunction0, Rotation rotation1, ServerLevel serverLevel2) {
        this.testFunction = testFunction0;
        this.level = serverLevel2;
        this.timeoutTicks = testFunction0.getMaxTicks();
        this.rotation = testFunction0.getRotation().getRotated(rotation1);
    }

    void setStructureBlockPos(BlockPos blockPos0) {
        this.structureBlockPos = blockPos0;
    }

    void startExecution() {
        this.startTick = this.level.m_46467_() + 1L + this.testFunction.getSetupTicks();
        this.timer.start();
    }

    public void tick() {
        if (!this.isDone()) {
            this.tickInternal();
            if (this.isDone()) {
                if (this.error != null) {
                    this.listeners.forEach(p_177482_ -> p_177482_.testFailed(this));
                } else {
                    this.listeners.forEach(p_177480_ -> p_177480_.testPassed(this));
                }
            }
        }
    }

    private void tickInternal() {
        this.tickCount = this.level.m_46467_() - this.startTick;
        if (this.tickCount >= 0L) {
            if (this.tickCount == 0L) {
                this.startTest();
            }
            ObjectIterator<Entry<Runnable>> $$0 = this.runAtTickTimeMap.object2LongEntrySet().iterator();
            while ($$0.hasNext()) {
                Entry<Runnable> $$1 = (Entry<Runnable>) $$0.next();
                if ($$1.getLongValue() <= this.tickCount) {
                    try {
                        ((Runnable) $$1.getKey()).run();
                    } catch (Exception var4) {
                        this.fail(var4);
                    }
                    $$0.remove();
                }
            }
            if (this.tickCount > (long) this.timeoutTicks) {
                if (this.sequences.isEmpty()) {
                    this.fail(new GameTestTimeoutException("Didn't succeed or fail within " + this.testFunction.getMaxTicks() + " ticks"));
                } else {
                    this.sequences.forEach(p_177478_ -> p_177478_.tickAndFailIfNotComplete(this.tickCount));
                    if (this.error == null) {
                        this.fail(new GameTestTimeoutException("No sequences finished"));
                    }
                }
            } else {
                this.sequences.forEach(p_177476_ -> p_177476_.tickAndContinue(this.tickCount));
            }
        }
    }

    private void startTest() {
        if (this.started) {
            throw new IllegalStateException("Test already started");
        } else {
            this.started = true;
            try {
                this.testFunction.run(new GameTestHelper(this));
            } catch (Exception var2) {
                this.fail(var2);
            }
        }
    }

    public void setRunAtTickTime(long long0, Runnable runnable1) {
        this.runAtTickTimeMap.put(runnable1, long0);
    }

    public String getTestName() {
        return this.testFunction.getTestName();
    }

    public BlockPos getStructureBlockPos() {
        return this.structureBlockPos;
    }

    @Nullable
    public Vec3i getStructureSize() {
        StructureBlockEntity $$0 = this.getStructureBlockEntity();
        return $$0 == null ? null : $$0.getStructureSize();
    }

    @Nullable
    public AABB getStructureBounds() {
        StructureBlockEntity $$0 = this.getStructureBlockEntity();
        return $$0 == null ? null : StructureUtils.getStructureBounds($$0);
    }

    @Nullable
    private StructureBlockEntity getStructureBlockEntity() {
        return (StructureBlockEntity) this.level.m_7702_(this.structureBlockPos);
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public boolean hasSucceeded() {
        return this.done && this.error == null;
    }

    public boolean hasFailed() {
        return this.error != null;
    }

    public boolean hasStarted() {
        return this.started;
    }

    public boolean isDone() {
        return this.done;
    }

    public long getRunTime() {
        return this.timer.elapsed(TimeUnit.MILLISECONDS);
    }

    private void finish() {
        if (!this.done) {
            this.done = true;
            this.timer.stop();
        }
    }

    public void succeed() {
        if (this.error == null) {
            this.finish();
        }
    }

    public void fail(Throwable throwable0) {
        this.error = throwable0;
        this.finish();
    }

    @Nullable
    public Throwable getError() {
        return this.error;
    }

    public String toString() {
        return this.getTestName();
    }

    public void addListener(GameTestListener gameTestListener0) {
        this.listeners.add(gameTestListener0);
    }

    public void spawnStructure(BlockPos blockPos0, int int1) {
        this.structureBlockEntity = StructureUtils.spawnStructure(this.getStructureName(), blockPos0, this.getRotation(), int1, this.level, false);
        this.structureBlockPos = this.structureBlockEntity.m_58899_();
        this.structureBlockEntity.setStructureName(this.getTestName());
        StructureUtils.addCommandBlockAndButtonToStartTest(this.structureBlockPos, new BlockPos(1, 0, -1), this.getRotation(), this.level);
        this.listeners.forEach(p_127630_ -> p_127630_.testStructureLoaded(this));
    }

    public void clearStructure() {
        if (this.structureBlockEntity == null) {
            throw new IllegalStateException("Expected structure to be initialized, but it was null");
        } else {
            BoundingBox $$0 = StructureUtils.getStructureBoundingBox(this.structureBlockEntity);
            StructureUtils.clearSpaceForStructure($$0, this.structureBlockPos.m_123342_(), this.level);
        }
    }

    long getTick() {
        return this.tickCount;
    }

    GameTestSequence createSequence() {
        GameTestSequence $$0 = new GameTestSequence(this);
        this.sequences.add($$0);
        return $$0;
    }

    public boolean isRequired() {
        return this.testFunction.isRequired();
    }

    public boolean isOptional() {
        return !this.testFunction.isRequired();
    }

    public String getStructureName() {
        return this.testFunction.getStructureName();
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public TestFunction getTestFunction() {
        return this.testFunction;
    }

    public int getTimeoutTicks() {
        return this.timeoutTicks;
    }

    public boolean isFlaky() {
        return this.testFunction.isFlaky();
    }

    public int maxAttempts() {
        return this.testFunction.getMaxAttempts();
    }

    public int requiredSuccesses() {
        return this.testFunction.getRequiredSuccesses();
    }
}
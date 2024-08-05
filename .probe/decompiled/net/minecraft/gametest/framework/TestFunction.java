package net.minecraft.gametest.framework;

import java.util.function.Consumer;
import net.minecraft.world.level.block.Rotation;

public class TestFunction {

    private final String batchName;

    private final String testName;

    private final String structureName;

    private final boolean required;

    private final int maxAttempts;

    private final int requiredSuccesses;

    private final Consumer<GameTestHelper> function;

    private final int maxTicks;

    private final long setupTicks;

    private final Rotation rotation;

    public TestFunction(String string0, String string1, String string2, int int3, long long4, boolean boolean5, Consumer<GameTestHelper> consumerGameTestHelper6) {
        this(string0, string1, string2, Rotation.NONE, int3, long4, boolean5, 1, 1, consumerGameTestHelper6);
    }

    public TestFunction(String string0, String string1, String string2, Rotation rotation3, int int4, long long5, boolean boolean6, Consumer<GameTestHelper> consumerGameTestHelper7) {
        this(string0, string1, string2, rotation3, int4, long5, boolean6, 1, 1, consumerGameTestHelper7);
    }

    public TestFunction(String string0, String string1, String string2, Rotation rotation3, int int4, long long5, boolean boolean6, int int7, int int8, Consumer<GameTestHelper> consumerGameTestHelper9) {
        this.batchName = string0;
        this.testName = string1;
        this.structureName = string2;
        this.rotation = rotation3;
        this.maxTicks = int4;
        this.required = boolean6;
        this.requiredSuccesses = int7;
        this.maxAttempts = int8;
        this.function = consumerGameTestHelper9;
        this.setupTicks = long5;
    }

    public void run(GameTestHelper gameTestHelper0) {
        this.function.accept(gameTestHelper0);
    }

    public String getTestName() {
        return this.testName;
    }

    public String getStructureName() {
        return this.structureName;
    }

    public String toString() {
        return this.testName;
    }

    public int getMaxTicks() {
        return this.maxTicks;
    }

    public boolean isRequired() {
        return this.required;
    }

    public String getBatchName() {
        return this.batchName;
    }

    public long getSetupTicks() {
        return this.setupTicks;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public boolean isFlaky() {
        return this.maxAttempts > 1;
    }

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    public int getRequiredSuccesses() {
        return this.requiredSuccesses;
    }
}
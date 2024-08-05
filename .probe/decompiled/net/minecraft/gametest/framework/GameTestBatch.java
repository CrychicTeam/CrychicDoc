package net.minecraft.gametest.framework;

import java.util.Collection;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;

public class GameTestBatch {

    public static final String DEFAULT_BATCH_NAME = "defaultBatch";

    private final String name;

    private final Collection<TestFunction> testFunctions;

    @Nullable
    private final Consumer<ServerLevel> beforeBatchFunction;

    @Nullable
    private final Consumer<ServerLevel> afterBatchFunction;

    public GameTestBatch(String string0, Collection<TestFunction> collectionTestFunction1, @Nullable Consumer<ServerLevel> consumerServerLevel2, @Nullable Consumer<ServerLevel> consumerServerLevel3) {
        if (collectionTestFunction1.isEmpty()) {
            throw new IllegalArgumentException("A GameTestBatch must include at least one TestFunction!");
        } else {
            this.name = string0;
            this.testFunctions = collectionTestFunction1;
            this.beforeBatchFunction = consumerServerLevel2;
            this.afterBatchFunction = consumerServerLevel3;
        }
    }

    public String getName() {
        return this.name;
    }

    public Collection<TestFunction> getTestFunctions() {
        return this.testFunctions;
    }

    public void runBeforeBatchFunction(ServerLevel serverLevel0) {
        if (this.beforeBatchFunction != null) {
            this.beforeBatchFunction.accept(serverLevel0);
        }
    }

    public void runAfterBatchFunction(ServerLevel serverLevel0) {
        if (this.afterBatchFunction != null) {
            this.afterBatchFunction.accept(serverLevel0);
        }
    }
}
package net.dnlayu.fastboot.mod;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.DataFixerBuilder;
import java.util.Collections;
import java.util.concurrent.Executor;

public class fastbootFixerBuilder extends DataFixerBuilder {

    private static final Executor NO_OP_EXECUTOR = command -> {
    };

    public fastbootFixerBuilder(int dataVersion) {
        super(dataVersion);
    }

    public DataFixer buildOptimized(Executor executor) {
        return super.buildOptimized(Collections.emptySet(), NO_OP_EXECUTOR);
    }
}
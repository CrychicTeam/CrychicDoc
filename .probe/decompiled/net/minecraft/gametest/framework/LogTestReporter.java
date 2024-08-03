package net.minecraft.gametest.framework;

import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import org.slf4j.Logger;

public class LogTestReporter implements TestReporter {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onTestFailed(GameTestInfo gameTestInfo0) {
        if (gameTestInfo0.isRequired()) {
            LOGGER.error("{} failed! {}", gameTestInfo0.getTestName(), Util.describeError(gameTestInfo0.getError()));
        } else {
            LOGGER.warn("(optional) {} failed. {}", gameTestInfo0.getTestName(), Util.describeError(gameTestInfo0.getError()));
        }
    }

    @Override
    public void onTestSuccess(GameTestInfo gameTestInfo0) {
    }
}
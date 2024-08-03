package net.minecraft.gametest.framework;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import com.mojang.logging.LogUtils;
import net.minecraft.Util;
import org.slf4j.Logger;

public class TeamcityTestReporter implements TestReporter {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Escaper ESCAPER = Escapers.builder().addEscape('\'', "|'").addEscape('\n', "|n").addEscape('\r', "|r").addEscape('|', "||").addEscape('[', "|[").addEscape(']', "|]").build();

    @Override
    public void onTestFailed(GameTestInfo gameTestInfo0) {
        String $$1 = ESCAPER.escape(gameTestInfo0.getTestName());
        String $$2 = ESCAPER.escape(gameTestInfo0.getError().getMessage());
        String $$3 = ESCAPER.escape(Util.describeError(gameTestInfo0.getError()));
        LOGGER.info("##teamcity[testStarted name='{}']", $$1);
        if (gameTestInfo0.isRequired()) {
            LOGGER.info("##teamcity[testFailed name='{}' message='{}' details='{}']", new Object[] { $$1, $$2, $$3 });
        } else {
            LOGGER.info("##teamcity[testIgnored name='{}' message='{}' details='{}']", new Object[] { $$1, $$2, $$3 });
        }
        LOGGER.info("##teamcity[testFinished name='{}' duration='{}']", $$1, gameTestInfo0.getRunTime());
    }

    @Override
    public void onTestSuccess(GameTestInfo gameTestInfo0) {
        String $$1 = ESCAPER.escape(gameTestInfo0.getTestName());
        LOGGER.info("##teamcity[testStarted name='{}']", $$1);
        LOGGER.info("##teamcity[testFinished name='{}' duration='{}']", $$1, gameTestInfo0.getRunTime());
    }
}
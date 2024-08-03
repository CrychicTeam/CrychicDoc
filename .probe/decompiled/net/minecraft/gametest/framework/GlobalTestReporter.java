package net.minecraft.gametest.framework;

public class GlobalTestReporter {

    private static TestReporter DELEGATE = new LogTestReporter();

    public static void replaceWith(TestReporter testReporter0) {
        DELEGATE = testReporter0;
    }

    public static void onTestFailed(GameTestInfo gameTestInfo0) {
        DELEGATE.onTestFailed(gameTestInfo0);
    }

    public static void onTestSuccess(GameTestInfo gameTestInfo0) {
        DELEGATE.onTestSuccess(gameTestInfo0);
    }

    public static void finish() {
        DELEGATE.finish();
    }
}
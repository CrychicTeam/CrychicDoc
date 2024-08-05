package net.minecraft.gametest.framework;

class ExhaustedAttemptsException extends Throwable {

    public ExhaustedAttemptsException(int int0, int int1, GameTestInfo gameTestInfo2) {
        super("Not enough successes: " + int1 + " out of " + int0 + " attempts. Required successes: " + gameTestInfo2.requiredSuccesses() + ". max attempts: " + gameTestInfo2.maxAttempts() + ".", gameTestInfo2.getError());
    }
}
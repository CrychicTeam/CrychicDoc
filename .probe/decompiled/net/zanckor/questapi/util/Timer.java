package net.zanckor.questapi.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Timer {

    private static final Table<UUID, String, Long> ACTIVE_COOLDOWNS_TABLE = HashBasedTable.create();

    private static final Table<UUID, String, Integer> ACTIVE_TICKERS_TABLE = HashBasedTable.create();

    public static boolean canUseWithCooldown(UUID playerUUID, String cooldownKey, float delayInSeconds) {
        Long timeInMillis = (Long) ACTIVE_COOLDOWNS_TABLE.get(playerUUID, cooldownKey);
        if (timeInMillis != null) {
            return calculateCooldownRemainder(timeInMillis) <= 0L;
        } else {
            updateCooldown(playerUUID, cooldownKey, delayInSeconds);
            return true;
        }
    }

    public static void updateCooldown(UUID playerUUID, String cooldownKey, float delayInSeconds) {
        ACTIVE_COOLDOWNS_TABLE.put(playerUUID, cooldownKey, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis((long) delayInSeconds));
    }

    public static long remainingTime(UUID playerUUID, String cooldownKey) {
        Long timeInMillis = (Long) ACTIVE_COOLDOWNS_TABLE.get(playerUUID, cooldownKey);
        return timeInMillis == null ? -1L : calculateCooldownRemainder(timeInMillis);
    }

    public static boolean existsTimer(UUID playerUUID, String cooldownKey) {
        Long timeInMillis = (Long) ACTIVE_COOLDOWNS_TABLE.get(playerUUID, cooldownKey);
        return timeInMillis != null;
    }

    public static long calculateCooldownRemainder(long timeInMillis) {
        return timeInMillis - System.currentTimeMillis();
    }

    public static boolean canUseWithTicker(UUID playerUUID, String tickerKey, boolean isFirstTickValid, int maxTicks) {
        Integer timeInTicks = (Integer) ACTIVE_TICKERS_TABLE.get(playerUUID, tickerKey);
        if (timeInTicks != null) {
            if (calculateTickRemainder(maxTicks, timeInTicks) <= 0) {
                updateTicker(playerUUID, tickerKey, 0);
                return true;
            } else {
                updateTicker(playerUUID, tickerKey, timeInTicks + 1);
                return false;
            }
        } else {
            updateTicker(playerUUID, tickerKey, 0);
            return isFirstTickValid;
        }
    }

    private static int calculateTickRemainder(int maxTicks, int timeInTicks) {
        return maxTicks - timeInTicks;
    }

    public static void updateTicker(UUID playerUUID, String tickerKey, int newValue) {
        ACTIVE_TICKERS_TABLE.put(playerUUID, tickerKey, newValue);
    }

    public static void clearTimers(UUID playerUUID) {
        ACTIVE_COOLDOWNS_TABLE.row(playerUUID).clear();
        ACTIVE_TICKERS_TABLE.row(playerUUID).clear();
    }

    public static void clearTimer(UUID playerUUID, String tickerKey) {
        ACTIVE_COOLDOWNS_TABLE.remove(playerUUID, tickerKey);
        ACTIVE_TICKERS_TABLE.remove(playerUUID, tickerKey);
    }
}
package me.lucko.spark.common.monitor.ping;

import java.util.Map;

@FunctionalInterface
public interface PlayerPingProvider {

    Map<String, Integer> poll();
}
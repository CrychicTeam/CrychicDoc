package dev.xkmc.l2hostility.compat.gateway;

import java.util.HashMap;
import java.util.Map;

public class WaveData {

    public final WaveId id;

    public Map<GatewayCondition, Integer> appliedCount = new HashMap();

    public WaveData(WaveId id) {
        this.id = id;
    }
}
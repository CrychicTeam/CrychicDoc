package com.nameless.impactful.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ImpactfulCapabilities {

    public static Capability<HitStopCap> INSTANCE = CapabilityManager.get(new CapabilityToken<HitStopCap>() {
    });
}
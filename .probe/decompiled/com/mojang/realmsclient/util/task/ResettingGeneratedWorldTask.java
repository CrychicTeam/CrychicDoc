package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.WorldGenerationInfo;
import net.minecraft.network.chat.Component;

public class ResettingGeneratedWorldTask extends ResettingWorldTask {

    private final WorldGenerationInfo generationInfo;

    public ResettingGeneratedWorldTask(WorldGenerationInfo worldGenerationInfo0, long long1, Component component2, Runnable runnable3) {
        super(long1, component2, runnable3);
        this.generationInfo = worldGenerationInfo0;
    }

    @Override
    protected void sendResetRequest(RealmsClient realmsClient0, long long1) throws RealmsServiceException {
        realmsClient0.resetWorldWithSeed(long1, this.generationInfo);
    }
}
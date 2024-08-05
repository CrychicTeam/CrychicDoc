package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.network.chat.Component;

public class ResettingTemplateWorldTask extends ResettingWorldTask {

    private final WorldTemplate template;

    public ResettingTemplateWorldTask(WorldTemplate worldTemplate0, long long1, Component component2, Runnable runnable3) {
        super(long1, component2, runnable3);
        this.template = worldTemplate0;
    }

    @Override
    protected void sendResetRequest(RealmsClient realmsClient0, long long1) throws RealmsServiceException {
        realmsClient0.resetWorldWithTemplate(long1, this.template.id);
    }
}
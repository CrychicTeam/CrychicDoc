package com.mojang.realmsclient.util.task;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsConnect;

public class ConnectTask extends LongRunningTask {

    private final RealmsConnect realmsConnect;

    private final RealmsServer server;

    private final RealmsServerAddress address;

    public ConnectTask(Screen screen0, RealmsServer realmsServer1, RealmsServerAddress realmsServerAddress2) {
        this.server = realmsServer1;
        this.address = realmsServerAddress2;
        this.realmsConnect = new RealmsConnect(screen0);
    }

    public void run() {
        this.m_90409_(Component.translatable("mco.connect.connecting"));
        this.realmsConnect.connect(this.server, ServerAddress.parseString(this.address.address));
    }

    @Override
    public void abortTask() {
        this.realmsConnect.abort();
        Minecraft.getInstance().getDownloadedPackSource().clearServerPack();
    }

    @Override
    public void tick() {
        this.realmsConnect.tick();
    }
}
package com.mojang.realmsclient.util.task;

import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

public class WorldCreationTask extends LongRunningTask {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String name;

    private final String motd;

    private final long worldId;

    private final Screen lastScreen;

    public WorldCreationTask(long long0, String string1, String string2, Screen screen3) {
        this.worldId = long0;
        this.name = string1;
        this.motd = string2;
        this.lastScreen = screen3;
    }

    public void run() {
        this.m_90409_(Component.translatable("mco.create.world.wait"));
        RealmsClient $$0 = RealmsClient.create();
        try {
            $$0.initializeWorld(this.worldId, this.name, this.motd);
            m_90405_(this.lastScreen);
        } catch (RealmsServiceException var3) {
            LOGGER.error("Couldn't create world");
            this.m_87791_(var3.toString());
        } catch (Exception var4) {
            LOGGER.error("Could not create world");
            this.m_87791_(var4.getLocalizedMessage());
        }
    }
}
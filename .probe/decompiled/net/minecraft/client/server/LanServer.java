package net.minecraft.client.server;

import net.minecraft.Util;

public class LanServer {

    private final String motd;

    private final String address;

    private long pingTime;

    public LanServer(String string0, String string1) {
        this.motd = string0;
        this.address = string1;
        this.pingTime = Util.getMillis();
    }

    public String getMotd() {
        return this.motd;
    }

    public String getAddress() {
        return this.address;
    }

    public void updatePingTime() {
        this.pingTime = Util.getMillis();
    }
}
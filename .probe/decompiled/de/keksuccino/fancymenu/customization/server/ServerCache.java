package de.keksuccino.fancymenu.customization.server;

import de.keksuccino.fancymenu.customization.ScreenCustomization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.network.chat.Component;

public class ServerCache {

    protected static final Component CANT_CONNECT_TEXT = Component.translatable("multiplayer.status.cannot_connect").withStyle(ChatFormatting.DARK_RED);

    protected static ServerStatusPinger pinger = new ServerStatusPinger();

    protected static Map<String, ServerData> servers = new HashMap();

    protected static Map<String, ServerData> serversUpdated = new HashMap();

    public static void init() {
        new Thread(() -> {
            while (true) {
                try {
                    if (ScreenCustomization.isCustomizationEnabledForScreen(Minecraft.getInstance().screen)) {
                        pingServers();
                    }
                } catch (Exception var2) {
                    var2.printStackTrace();
                }
                try {
                    Thread.sleep(30000L);
                } catch (Exception var1) {
                    var1.printStackTrace();
                }
            }
        }).start();
    }

    public static void cacheServer(ServerData server, ServerData serverUpdated) {
        if (server.ip != null) {
            try {
                server.ping = -1L;
                serverUpdated.ping = -1L;
                servers.put(server.ip, server);
                serversUpdated.put(server.ip, serverUpdated);
                pingServers();
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }
    }

    public static ServerData getServer(String ip) {
        if (!servers.containsKey(ip)) {
            cacheServer(new ServerData(ip, ip, false), new ServerData(ip, ip, false));
        }
        if (((ServerData) servers.get(ip)).motd != null && !((ServerData) servers.get(ip)).motd.equals(Component.translatable("multiplayer.status.pinging"))) {
            ((ServerData) serversUpdated.get(ip)).ping = ((ServerData) servers.get(ip)).ping;
            ((ServerData) serversUpdated.get(ip)).protocol = ((ServerData) servers.get(ip)).protocol;
            ((ServerData) serversUpdated.get(ip)).motd = ((ServerData) servers.get(ip)).motd;
            ((ServerData) serversUpdated.get(ip)).version = ((ServerData) servers.get(ip)).version;
            ((ServerData) serversUpdated.get(ip)).status = ((ServerData) servers.get(ip)).status;
            ((ServerData) serversUpdated.get(ip)).playerList = ((ServerData) servers.get(ip)).playerList;
        }
        return (ServerData) serversUpdated.get(ip);
    }

    public static void removeServer(String ip) {
        servers.remove(ip);
        serversUpdated.remove(ip);
    }

    public static void clear() {
        servers.clear();
        serversUpdated.clear();
    }

    public static void pingServers() {
        for (ServerData d : new ArrayList(servers.values())) {
            try {
                new Thread(() -> {
                    try {
                        pinger.pingServer(d, () -> {
                        });
                        if (d == null || d.status.getString().isEmpty()) {
                            d.ping = -1L;
                            d.motd = CANT_CONNECT_TEXT;
                        }
                    } catch (Exception var2x) {
                        d.ping = -1L;
                        d.motd = CANT_CONNECT_TEXT;
                    }
                }).start();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
    }
}
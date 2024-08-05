package net.minecraft.client.quickplay;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsServerList;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.util.task.GetServerDetailsTask;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ReloadInstance;

public class QuickPlay {

    public static final Component ERROR_TITLE = Component.translatable("quickplay.error.title");

    private static final Component INVALID_IDENTIFIER = Component.translatable("quickplay.error.invalid_identifier");

    private static final Component REALM_CONNECT = Component.translatable("quickplay.error.realm_connect");

    private static final Component REALM_PERMISSION = Component.translatable("quickplay.error.realm_permission");

    private static final Component TO_TITLE = Component.translatable("gui.toTitle");

    private static final Component TO_WORLD_LIST = Component.translatable("gui.toWorld");

    private static final Component TO_REALMS_LIST = Component.translatable("gui.toRealms");

    public static void connect(Minecraft minecraft0, GameConfig.QuickPlayData gameConfigQuickPlayData1, ReloadInstance reloadInstance2, RealmsClient realmsClient3) {
        String $$4 = gameConfigQuickPlayData1.singleplayer();
        String $$5 = gameConfigQuickPlayData1.multiplayer();
        String $$6 = gameConfigQuickPlayData1.realms();
        reloadInstance2.done().thenRunAsync(() -> {
            if (!Util.isBlank($$4)) {
                joinSingleplayerWorld(minecraft0, $$4);
            } else if (!Util.isBlank($$5)) {
                joinMultiplayerWorld(minecraft0, $$5);
            } else if (!Util.isBlank($$6)) {
                joinRealmsWorld(minecraft0, realmsClient3, $$6);
            }
        }, minecraft0);
    }

    private static void joinSingleplayerWorld(Minecraft minecraft0, String string1) {
        if (!minecraft0.getLevelSource().levelExists(string1)) {
            Screen $$2 = new SelectWorldScreen(new TitleScreen());
            minecraft0.setScreen(new DisconnectedScreen($$2, ERROR_TITLE, INVALID_IDENTIFIER, TO_WORLD_LIST));
        } else {
            minecraft0.forceSetScreen(new GenericDirtMessageScreen(Component.translatable("selectWorld.data_read")));
            minecraft0.createWorldOpenFlows().loadLevel(new TitleScreen(), string1);
        }
    }

    private static void joinMultiplayerWorld(Minecraft minecraft0, String string1) {
        ServerList $$2 = new ServerList(minecraft0);
        $$2.load();
        ServerData $$3 = $$2.get(string1);
        if ($$3 == null) {
            $$3 = new ServerData(I18n.get("selectServer.defaultName"), string1, false);
            $$2.add($$3, true);
            $$2.save();
        }
        ServerAddress $$4 = ServerAddress.parseString(string1);
        ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), minecraft0, $$4, $$3, true);
    }

    private static void joinRealmsWorld(Minecraft minecraft0, RealmsClient realmsClient1, String string2) {
        long $$3;
        RealmsServerList $$4;
        try {
            $$3 = Long.parseLong(string2);
            $$4 = realmsClient1.listWorlds();
        } catch (NumberFormatException var9) {
            Screen $$6 = new RealmsMainScreen(new TitleScreen());
            minecraft0.setScreen(new DisconnectedScreen($$6, ERROR_TITLE, INVALID_IDENTIFIER, TO_REALMS_LIST));
            return;
        } catch (RealmsServiceException var10) {
            Screen $$8 = new TitleScreen();
            minecraft0.setScreen(new DisconnectedScreen($$8, ERROR_TITLE, REALM_CONNECT, TO_TITLE));
            return;
        }
        RealmsServer $$11 = (RealmsServer) $$4.servers.stream().filter(p_279424_ -> p_279424_.id == $$3).findFirst().orElse(null);
        if ($$11 == null) {
            Screen $$12 = new RealmsMainScreen(new TitleScreen());
            minecraft0.setScreen(new DisconnectedScreen($$12, ERROR_TITLE, REALM_PERMISSION, TO_REALMS_LIST));
        } else {
            TitleScreen $$13 = new TitleScreen();
            GetServerDetailsTask $$14 = new GetServerDetailsTask(new RealmsMainScreen($$13), $$13, $$11, new ReentrantLock());
            minecraft0.setScreen(new RealmsLongRunningMcoTaskScreen($$13, $$14));
        }
    }
}
package org.violetmoon.quark.base.handler;

import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.client.event.play.ZRenderPlayer;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.play.entity.player.ZPlayer;

public class ContributorRewardHandler {

    private static final ImmutableSet<String> DEV_UUID = ImmutableSet.of("8c826f34-113b-4238-a173-44639c53b6e6", "0d054077-a977-4b19-9df9-8a4d5bf20ec3", "458391f5-6303-4649-b416-e4c0d18f837a", "75c298f9-27c8-415b-9a16-329e3884054b", "6c175d10-198a-49f9-8e2b-c74f1f0178f3", "07cb3dfd-ee1d-4ecf-b5b5-f70d317a82eb", new String[] { "e67eb09a-b5af-4822-b756-9065cdc49913" });

    private static final Set<String> done = Collections.newSetFromMap(new WeakHashMap());

    private static Thread thread;

    private static String name;

    private static final Map<String, Integer> tiers = new HashMap();

    public static int localPatronTier = 0;

    public static String featuredPatron = "N/A";

    @LoadEvent
    public static void init(ZCommonSetup event) {
        init();
    }

    @PlayEvent
    public static void onPlayerJoin(ZPlayer.LoggedIn event) {
        init();
    }

    public static void init() {
        if (thread == null || !thread.isAlive()) {
            thread = new ContributorRewardHandler.ThreadContributorListLoader();
        }
    }

    public static int getTier(Player player) {
        return getTier(player.getGameProfile().getName());
    }

    public static int getTier(String name) {
        return (Integer) tiers.getOrDefault(name.toLowerCase(Locale.ROOT), 0);
    }

    private static void load(Properties props) {
        List<String> allPatrons = new ArrayList(props.size());
        props.forEach((k, v) -> {
            String key = (String) k;
            String value = (String) v;
            int tier = Integer.parseInt(value);
            if (tier < 10) {
                allPatrons.add(key);
            }
            tiers.put(key.toLowerCase(Locale.ROOT), tier);
            if (key.toLowerCase(Locale.ROOT).equals(name)) {
                localPatronTier = tier;
            }
        });
        if (!allPatrons.isEmpty()) {
            featuredPatron = (String) allPatrons.get((int) (Math.random() * (double) allPatrons.size()));
        }
    }

    public static class Client {

        @LoadEvent
        public static void getLocalName(ZCommonSetup event) {
            ContributorRewardHandler.name = Minecraft.getInstance().getUser().getName().toLowerCase(Locale.ROOT);
        }

        @PlayEvent
        public static void onRenderPlayer(ZRenderPlayer.Post event) {
            Player player = event.getEntity();
            String uuid = player.m_20148_().toString();
            if (player instanceof AbstractClientPlayer clientPlayer && ContributorRewardHandler.DEV_UUID.contains(uuid) && !ContributorRewardHandler.done.contains(uuid) && clientPlayer.isCapeLoaded()) {
                PlayerInfo info = clientPlayer.playerInfo;
                Map<Type, ResourceLocation> textures = info.textureLocations;
                ResourceLocation loc = new ResourceLocation("quark", "textures/misc/dev_cape.png");
                textures.put(Type.CAPE, loc);
                textures.put(Type.ELYTRA, loc);
                ContributorRewardHandler.done.add(uuid);
            }
        }
    }

    private static class ThreadContributorListLoader extends Thread {

        public ThreadContributorListLoader() {
            this.setName("Quark Contributor Loading Thread");
            this.setDaemon(true);
            this.start();
        }

        public void run() {
            try {
                URL url = new URL("https://raw.githubusercontent.com/VazkiiMods/Quark/master/contributors.properties");
                URLConnection conn = url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                Properties patreonTiers = new Properties();
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                try {
                    patreonTiers.load(reader);
                    ContributorRewardHandler.load(patreonTiers);
                } catch (Throwable var8) {
                    try {
                        reader.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                    throw var8;
                }
                reader.close();
            } catch (IOException var9) {
                Quark.LOG.error("Failed to load patreon information", var9);
            }
        }
    }
}
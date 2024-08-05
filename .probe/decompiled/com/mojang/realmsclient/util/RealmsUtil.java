package com.mojang.realmsclient.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RealmsUtil {

    static final MinecraftSessionService SESSION_SERVICE = Minecraft.getInstance().getMinecraftSessionService();

    private static final Component RIGHT_NOW = Component.translatable("mco.util.time.now");

    private static final LoadingCache<String, GameProfile> GAME_PROFILE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(60L, TimeUnit.MINUTES).build(new CacheLoader<String, GameProfile>() {

        public GameProfile load(String p_90229_) {
            return RealmsUtil.SESSION_SERVICE.fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(p_90229_), null), false);
        }
    });

    private static final int MINUTES = 60;

    private static final int HOURS = 3600;

    private static final int DAYS = 86400;

    public static String uuidToName(String string0) {
        return ((GameProfile) GAME_PROFILE_CACHE.getUnchecked(string0)).getName();
    }

    public static GameProfile getGameProfile(String string0) {
        return (GameProfile) GAME_PROFILE_CACHE.getUnchecked(string0);
    }

    public static Component convertToAgePresentation(long long0) {
        if (long0 < 0L) {
            return RIGHT_NOW;
        } else {
            long $$1 = long0 / 1000L;
            if ($$1 < 60L) {
                return Component.translatable("mco.time.secondsAgo", $$1);
            } else if ($$1 < 3600L) {
                long $$2 = $$1 / 60L;
                return Component.translatable("mco.time.minutesAgo", $$2);
            } else if ($$1 < 86400L) {
                long $$3 = $$1 / 3600L;
                return Component.translatable("mco.time.hoursAgo", $$3);
            } else {
                long $$4 = $$1 / 86400L;
                return Component.translatable("mco.time.daysAgo", $$4);
            }
        }
    }

    public static Component convertToAgePresentationFromInstant(Date date0) {
        return convertToAgePresentation(System.currentTimeMillis() - date0.getTime());
    }

    public static void renderPlayerFace(GuiGraphics guiGraphics0, int int1, int int2, int int3, String string4) {
        GameProfile $$5 = getGameProfile(string4);
        ResourceLocation $$6 = Minecraft.getInstance().getSkinManager().getInsecureSkinLocation($$5);
        PlayerFaceRenderer.draw(guiGraphics0, $$6, int1, int2, int3);
    }
}
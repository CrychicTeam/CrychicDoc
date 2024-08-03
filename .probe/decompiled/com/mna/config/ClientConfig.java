package com.mna.config;

import com.mna.api.config.ClientConfigValues;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.util.Mth;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class ClientConfig {

    public static final ClientConfigProvider CLIENT;

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static HashSet<String> DidYouKnowTipsShown;

    @SubscribeEvent
    public static void onModConfigEvent(ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == CLIENT_SPEC) {
            bakeConfig();
        }
    }

    public static void bakeConfig() {
        ClientConfigValues.FancyMagelights = ClientConfigProvider.FANCY_MAGELIGHTS.get();
        ClientConfigValues.ParticleBlur = ClientConfigProvider.PARTICLE_BLUR.get();
        ClientConfigValues.HudAffinity = ClientConfigProvider.HUD_AFFINITY.get();
        DidYouKnowTipsShown = new HashSet((Collection) ClientConfigProvider.DID_YOU_KNOW_TIPS.get());
        ClientConfigValues.ShowHudMode = ClientConfigValues.HudMode.values()[Mth.clamp(ClientConfigProvider.SHOW_HUD_MODE.get(), 0, ClientConfigValues.HudMode.values().length - 1)];
        ClientConfigValues.CodexBackMode = ClientConfigValues.CodexMode.values()[Mth.clamp(ClientConfigProvider.CODEX_BACK_STYLE.get(), 0, ClientConfigValues.CodexMode.values().length - 1)];
        ClientConfigValues.HudPosition = ClientConfigValues.HudPos.values()[Mth.clamp(ClientConfigProvider.HUD_POSITION.get(), 0, ClientConfigValues.HudPos.values().length - 1)];
        ClientConfigValues.PinnedRecipeScale = ClientConfigValues.PinnedRecipeSize.values()[Mth.clamp(ClientConfigProvider.PINNED_RECIPE_SCALE.get(), 0, ClientConfigValues.PinnedRecipeSize.values().length - 1)];
    }

    public static void SetDidYouKnowTipShown(String id) {
        DidYouKnowTipsShown.add(id);
        ClientConfigProvider.DID_YOU_KNOW_TIPS.set(Arrays.asList((String[]) DidYouKnowTipsShown.toArray(new String[0])));
    }

    public static void setCodexBackStyle(int style) {
        ClientConfigProvider.CODEX_BACK_STYLE.set(Integer.valueOf(style));
    }

    public static void setHudMode(int mode) {
        ClientConfigProvider.SHOW_HUD_MODE.set(Integer.valueOf(mode));
    }

    public static void setHudPosition(int position) {
        ClientConfigProvider.HUD_POSITION.set(Integer.valueOf(position));
    }

    public static void setPinnedRecipeScale(int style) {
        ClientConfigProvider.PINNED_RECIPE_SCALE.set(Integer.valueOf(style));
    }

    static {
        Pair<ClientConfigProvider, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfigProvider::new);
        CLIENT = (ClientConfigProvider) specPair.getLeft();
        CLIENT_SPEC = (ForgeConfigSpec) specPair.getRight();
    }
}
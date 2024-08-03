package com.simibubi.create.infrastructure.command;

import com.mojang.logging.LogUtils;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.GoggleConfigScreen;
import com.simibubi.create.content.trains.CameraDistanceModifier;
import com.simibubi.create.foundation.config.ui.BaseConfigScreen;
import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.SubMenuConfigScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.ui.PonderIndexScreen;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import com.simibubi.create.foundation.utility.CameraAngleAnimationService;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

public class SConfigureConfigPacket extends SimplePacketBase {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String option;

    private final String value;

    public SConfigureConfigPacket(String option, String value) {
        this.option = option;
        this.value = value;
    }

    public SConfigureConfigPacket(FriendlyByteBuf buffer) {
        this.option = buffer.readUtf(32767);
        this.value = buffer.readUtf(32767);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.option);
        buffer.writeUtf(this.value);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (this.option.startsWith("SET")) {
                trySetConfig(this.option.substring(3), this.value);
            } else {
                try {
                    SConfigureConfigPacket.Actions.valueOf(this.option).performAction(this.value);
                } catch (IllegalArgumentException var2) {
                    LOGGER.warn("Received ConfigureConfigPacket with invalid Option: " + this.option);
                }
            }
        }));
        return true;
    }

    private static void trySetConfig(String option, String value) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ConfigHelper.ConfigPath configPath;
            try {
                configPath = ConfigHelper.ConfigPath.parse(option);
            } catch (IllegalArgumentException var7) {
                player.displayClientMessage(Components.literal(var7.getMessage()), false);
                return;
            }
            if (configPath.getType() != Type.CLIENT) {
                Create.LOGGER.warn("Received type-mismatched config packet on client");
            } else {
                try {
                    ConfigHelper.setConfigValue(configPath, value);
                    player.displayClientMessage(Components.literal("Great Success!"), false);
                } catch (ConfigHelper.InvalidValueException var5) {
                    player.displayClientMessage(Components.literal("Config could not be set the the specified value!"), false);
                } catch (Exception var6) {
                    player.displayClientMessage(Components.literal("Something went wrong while trying to set config value. Check the client logs for more information"), false);
                    Create.LOGGER.warn("Exception during client-side config value set:", var6);
                }
            }
        }
    }

    public static enum Actions {

        configScreen(() -> SConfigureConfigPacket.Actions::configScreen),
        rainbowDebug(() -> SConfigureConfigPacket.Actions::rainbowDebug),
        overlayScreen(() -> SConfigureConfigPacket.Actions::overlayScreen),
        fixLighting(() -> SConfigureConfigPacket.Actions::experimentalLighting),
        overlayReset(() -> SConfigureConfigPacket.Actions::overlayReset),
        openPonder(() -> SConfigureConfigPacket.Actions::openPonder),
        fabulousWarning(() -> SConfigureConfigPacket.Actions::fabulousWarning),
        zoomMultiplier(() -> SConfigureConfigPacket.Actions::zoomMultiplier),
        camAngleYawTarget(() -> value -> camAngleTarget(value, true)),
        camAnglePitchTarget(() -> value -> camAngleTarget(value, false)),
        camAngleFunction(() -> SConfigureConfigPacket.Actions::camAngleFunction);

        private final Supplier<Consumer<String>> consumer;

        private Actions(Supplier<Consumer<String>> action) {
            this.consumer = action;
        }

        void performAction(String value) {
            ((Consumer) this.consumer.get()).accept(value);
        }

        @OnlyIn(Dist.CLIENT)
        private static void configScreen(String value) {
            if (value.equals("")) {
                ScreenOpener.open(BaseConfigScreen.forCreate(null));
            } else {
                LocalPlayer player = Minecraft.getInstance().player;
                ConfigHelper.ConfigPath configPath;
                try {
                    configPath = ConfigHelper.ConfigPath.parse(value);
                } catch (IllegalArgumentException var5) {
                    player.displayClientMessage(Components.literal(var5.getMessage()), false);
                    return;
                }
                try {
                    ScreenOpener.open(SubMenuConfigScreen.find(configPath));
                } catch (Exception var4) {
                    player.displayClientMessage(Components.literal("Unable to find the specified config"), false);
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        private static void rainbowDebug(String value) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !"".equals(value)) {
                if (value.equals("info")) {
                    Component text = Components.literal("Rainbow Debug Utility is currently: ").append(boolToText(AllConfigs.client().rainbowDebug.get()));
                    player.displayClientMessage(text, false);
                } else {
                    AllConfigs.client().rainbowDebug.set(Boolean.valueOf(Boolean.parseBoolean(value)));
                    Component text = boolToText(AllConfigs.client().rainbowDebug.get()).append(Components.literal(" Rainbow Debug Utility").withStyle(ChatFormatting.WHITE));
                    player.displayClientMessage(text, false);
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        private static void overlayReset(String value) {
            AllConfigs.client().overlayOffsetX.set(Integer.valueOf(0));
            AllConfigs.client().overlayOffsetY.set(Integer.valueOf(0));
        }

        @OnlyIn(Dist.CLIENT)
        private static void overlayScreen(String value) {
            ScreenOpener.open(new GoggleConfigScreen());
        }

        @OnlyIn(Dist.CLIENT)
        private static void experimentalLighting(String value) {
            ForgeConfig.CLIENT.experimentalForgeLightPipelineEnabled.set(Boolean.valueOf(true));
            Minecraft.getInstance().levelRenderer.allChanged();
        }

        @OnlyIn(Dist.CLIENT)
        private static void openPonder(String value) {
            if (value.equals("index")) {
                ScreenOpener.transitionTo(new PonderIndexScreen());
            } else {
                ResourceLocation id = new ResourceLocation(value);
                if (!PonderRegistry.ALL.containsKey(id)) {
                    Create.LOGGER.error("Could not find ponder scenes for item: " + id);
                } else {
                    ScreenOpener.transitionTo(PonderUI.of(id));
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        private static void fabulousWarning(String value) {
            AllConfigs.client().ignoreFabulousWarning.set(Boolean.valueOf(true));
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.displayClientMessage(Components.literal("Disabled Fabulous graphics warning"), false);
            }
        }

        @OnlyIn(Dist.CLIENT)
        private static void zoomMultiplier(String value) {
            try {
                float v = Float.parseFloat(value);
                if (v <= 0.0F) {
                    return;
                }
                CameraDistanceModifier.zoomOut(v);
            } catch (NumberFormatException var2) {
                Create.LOGGER.debug("Received non-float value {} in zoom packet, ignoring", value);
            }
        }

        @OnlyIn(Dist.CLIENT)
        private static void camAngleTarget(String value, boolean yaw) {
            try {
                float v = Float.parseFloat(value);
                if (yaw) {
                    CameraAngleAnimationService.setYawTarget(v);
                } else {
                    CameraAngleAnimationService.setPitchTarget(v);
                }
            } catch (NumberFormatException var3) {
                Create.LOGGER.debug("Received non-float value {} in camAngle packet, ignoring", value);
            }
        }

        @OnlyIn(Dist.CLIENT)
        private static void camAngleFunction(String value) {
            CameraAngleAnimationService.Mode mode = CameraAngleAnimationService.Mode.LINEAR;
            String modeString = value;
            float speed = -1.0F;
            String[] split = value.split(":");
            if (split.length > 1) {
                modeString = split[0];
                try {
                    speed = Float.parseFloat(split[1]);
                } catch (NumberFormatException var7) {
                }
            }
            try {
                mode = CameraAngleAnimationService.Mode.valueOf(modeString);
            } catch (IllegalArgumentException var6) {
            }
            CameraAngleAnimationService.setAnimationMode(mode);
            CameraAngleAnimationService.setAnimationSpeed(speed);
        }

        private static MutableComponent boolToText(boolean b) {
            return b ? Components.literal("enabled").withStyle(ChatFormatting.DARK_GREEN) : Components.literal("disabled").withStyle(ChatFormatting.RED);
        }
    }
}
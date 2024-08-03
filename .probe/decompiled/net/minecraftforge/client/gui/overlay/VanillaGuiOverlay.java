package net.minecraftforge.client.gui.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.NotNull;

public enum VanillaGuiOverlay {

    VIGNETTE("vignette", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (Minecraft.useFancyGraphics()) {
            gui.setupOverlayRenderState(true, false);
            gui.m_280154_(guiGraphics, gui.getMinecraft().getCameraEntity());
        }
    }),
    SPYGLASS("spyglass", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        gui.setupOverlayRenderState(true, false);
        gui.renderSpyglassOverlay(guiGraphics);
    }),
    HELMET("helmet", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        gui.setupOverlayRenderState(true, false);
        gui.renderHelmet(partialTick, guiGraphics);
    }),
    FROSTBITE("frostbite", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        gui.setupOverlayRenderState(true, false);
        gui.renderFrostbite(guiGraphics);
    }),
    PORTAL("portal", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        float f1 = Mth.lerp(partialTick, gui.getMinecraft().player.oSpinningEffectIntensity, gui.getMinecraft().player.spinningEffectIntensity);
        if (f1 > 0.0F && !gui.getMinecraft().player.m_21023_(MobEffects.CONFUSION)) {
            gui.setupOverlayRenderState(true, false);
            gui.renderPortalOverlay(guiGraphics, f1);
        }
    }),
    HOTBAR("hotbar", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui) {
            gui.setupOverlayRenderState(true, false);
            if (gui.getMinecraft().gameMode.getPlayerMode() == GameType.SPECTATOR) {
                gui.m_93085_().renderHotbar(guiGraphics);
            } else {
                gui.m_280518_(partialTick, guiGraphics);
            }
        }
    }),
    CROSSHAIR("crosshair", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui) {
            gui.setupOverlayRenderState(true, false);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, -90.0F);
            gui.m_280130_(guiGraphics);
            guiGraphics.pose().popPose();
        }
    }),
    BOSS_EVENT_PROGRESS("boss_event_progress", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui) {
            gui.setupOverlayRenderState(true, false);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, -90.0F);
            gui.renderBossHealth(guiGraphics);
            guiGraphics.pose().popPose();
        }
    }),
    PLAYER_HEALTH("player_health", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            gui.renderHealth(screenWidth, screenHeight, guiGraphics);
        }
    }),
    ARMOR_LEVEL("armor_level", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            gui.renderArmor(guiGraphics, screenWidth, screenHeight);
        }
    }),
    FOOD_LEVEL("food_level", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Entity vehicle = gui.getMinecraft().player.m_20202_();
        boolean isMounted = vehicle != null && vehicle.showVehicleHealth();
        if (!isMounted && !gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            gui.renderFood(screenWidth, screenHeight, guiGraphics);
        }
    }),
    AIR_LEVEL("air_level", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            gui.renderAir(screenWidth, screenHeight, guiGraphics);
        }
    }),
    MOUNT_HEALTH("mount_health", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            gui.renderHealthMount(screenWidth, screenHeight, guiGraphics);
        }
    }),
    JUMP_BAR("jump_bar", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        PlayerRideableJumping playerRideableJumping = gui.getMinecraft().player.jumpableVehicle();
        if (playerRideableJumping != null && !gui.getMinecraft().options.hideGui) {
            gui.setupOverlayRenderState(true, false);
            gui.renderJumpMeter(playerRideableJumping, guiGraphics, screenWidth / 2 - 91);
        }
    }),
    EXPERIENCE_BAR("experience_bar", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (gui.getMinecraft().player.jumpableVehicle() == null && !gui.getMinecraft().options.hideGui) {
            gui.setupOverlayRenderState(true, false);
            gui.renderExperience(screenWidth / 2 - 91, guiGraphics);
        }
    }),
    ITEM_NAME("item_name", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui) {
            gui.setupOverlayRenderState(true, false);
            if (gui.getMinecraft().gameMode.getPlayerMode() != GameType.SPECTATOR) {
                gui.renderSelectedItemName(guiGraphics, Math.max(gui.leftHeight, gui.rightHeight));
            } else if (gui.getMinecraft().player.m_5833_()) {
                gui.m_93085_().renderTooltip(guiGraphics);
            }
        }
    }),
    SLEEP_FADE("sleep_fade", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> gui.renderSleepFade(screenWidth, screenHeight, guiGraphics)),
    POTION_ICONS("potion_icons", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> gui.m_280523_(guiGraphics)),
    DEBUG_TEXT("debug_text", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> gui.renderHUDText(screenWidth, screenHeight, guiGraphics)),
    FPS_GRAPH("fps_graph", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> gui.renderFPSGraph(guiGraphics)),
    RECORD_OVERLAY("record_overlay", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui) {
            gui.renderRecordOverlay(screenWidth, screenHeight, partialTick, guiGraphics);
        }
    }),
    TITLE_TEXT("title_text", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui) {
            gui.renderTitle(screenWidth, screenHeight, partialTick, guiGraphics);
        }
    }),
    SUBTITLES("subtitles", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (!gui.getMinecraft().options.hideGui) {
            gui.renderSubtitles(guiGraphics);
        }
    }),
    SCOREBOARD("scoreboard", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Scoreboard scoreboard = gui.getMinecraft().level.getScoreboard();
        Objective objective = null;
        PlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(gui.getMinecraft().player.m_6302_());
        if (scoreplayerteam != null) {
            int slot = scoreplayerteam.getColor().getId();
            if (slot >= 0) {
                objective = scoreboard.getDisplayObjective(3 + slot);
            }
        }
        Objective scoreobjective1 = objective != null ? objective : scoreboard.getDisplayObjective(1);
        if (scoreobjective1 != null) {
            gui.m_280030_(guiGraphics, scoreobjective1);
        }
    }),
    CHAT_PANEL("chat_panel", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        gui.renderChat(screenWidth, screenHeight, guiGraphics);
    }),
    PLAYER_LIST("player_list", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        gui.renderPlayerList(screenWidth, screenHeight, guiGraphics);
    });

    private final ResourceLocation id;

    final IGuiOverlay overlay;

    NamedGuiOverlay type;

    private VanillaGuiOverlay(String id, IGuiOverlay overlay) {
        this.id = new ResourceLocation("minecraft", id);
        this.overlay = overlay;
    }

    @NotNull
    public ResourceLocation id() {
        return this.id;
    }

    public NamedGuiOverlay type() {
        return this.type;
    }
}
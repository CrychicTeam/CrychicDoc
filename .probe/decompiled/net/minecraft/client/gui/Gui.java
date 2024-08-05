package net.minecraft.client.gui;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

public class Gui {

    private static final ResourceLocation VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");

    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    private static final ResourceLocation PUMPKIN_BLUR_LOCATION = new ResourceLocation("textures/misc/pumpkinblur.png");

    private static final ResourceLocation SPYGLASS_SCOPE_LOCATION = new ResourceLocation("textures/misc/spyglass_scope.png");

    private static final ResourceLocation POWDER_SNOW_OUTLINE_LOCATION = new ResourceLocation("textures/misc/powder_snow_outline.png");

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    private static final Component DEMO_EXPIRED_TEXT = Component.translatable("demo.demoExpired");

    private static final Component SAVING_TEXT = Component.translatable("menu.savingLevel");

    private static final int COLOR_WHITE = 16777215;

    private static final float MIN_CROSSHAIR_ATTACK_SPEED = 5.0F;

    private static final int NUM_HEARTS_PER_ROW = 10;

    private static final int LINE_HEIGHT = 10;

    private static final String SPACER = ": ";

    private static final float PORTAL_OVERLAY_ALPHA_MIN = 0.2F;

    private static final int HEART_SIZE = 9;

    private static final int HEART_SEPARATION = 8;

    private static final float AUTOSAVE_FADE_SPEED_FACTOR = 0.2F;

    private final RandomSource random = RandomSource.create();

    private final Minecraft minecraft;

    private final ItemRenderer itemRenderer;

    private final ChatComponent chat;

    private int tickCount;

    @Nullable
    private Component overlayMessageString;

    private int overlayMessageTime;

    private boolean animateOverlayMessageColor;

    private boolean chatDisabledByPlayerShown;

    public float vignetteBrightness = 1.0F;

    private int toolHighlightTimer;

    private ItemStack lastToolHighlight = ItemStack.EMPTY;

    private final DebugScreenOverlay debugScreen;

    private final SubtitleOverlay subtitleOverlay;

    private final SpectatorGui spectatorGui;

    private final PlayerTabOverlay tabList;

    private final BossHealthOverlay bossOverlay;

    private int titleTime;

    @Nullable
    private Component title;

    @Nullable
    private Component subtitle;

    private int titleFadeInTime;

    private int titleStayTime;

    private int titleFadeOutTime;

    private int lastHealth;

    private int displayHealth;

    private long lastHealthTime;

    private long healthBlinkTime;

    private int screenWidth;

    private int screenHeight;

    private float autosaveIndicatorValue;

    private float lastAutosaveIndicatorValue;

    private float scopeScale;

    public Gui(Minecraft minecraft0, ItemRenderer itemRenderer1) {
        this.minecraft = minecraft0;
        this.itemRenderer = itemRenderer1;
        this.debugScreen = new DebugScreenOverlay(minecraft0);
        this.spectatorGui = new SpectatorGui(minecraft0);
        this.chat = new ChatComponent(minecraft0);
        this.tabList = new PlayerTabOverlay(minecraft0, this);
        this.bossOverlay = new BossHealthOverlay(minecraft0);
        this.subtitleOverlay = new SubtitleOverlay(minecraft0);
        this.resetTitleTimes();
    }

    public void resetTitleTimes() {
        this.titleFadeInTime = 10;
        this.titleStayTime = 70;
        this.titleFadeOutTime = 20;
    }

    public void render(GuiGraphics guiGraphics0, float float1) {
        Window $$2 = this.minecraft.getWindow();
        this.screenWidth = guiGraphics0.guiWidth();
        this.screenHeight = guiGraphics0.guiHeight();
        Font $$3 = this.getFont();
        RenderSystem.enableBlend();
        if (Minecraft.useFancyGraphics()) {
            this.renderVignette(guiGraphics0, this.minecraft.getCameraEntity());
        } else {
            RenderSystem.enableDepthTest();
        }
        float $$4 = this.minecraft.getDeltaFrameTime();
        this.scopeScale = Mth.lerp(0.5F * $$4, this.scopeScale, 1.125F);
        if (this.minecraft.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.player.m_150108_()) {
                this.renderSpyglassOverlay(guiGraphics0, this.scopeScale);
            } else {
                this.scopeScale = 0.5F;
                ItemStack $$5 = this.minecraft.player.m_150109_().getArmor(3);
                if ($$5.is(Blocks.CARVED_PUMPKIN.asItem())) {
                    this.renderTextureOverlay(guiGraphics0, PUMPKIN_BLUR_LOCATION, 1.0F);
                }
            }
        }
        if (this.minecraft.player.m_146888_() > 0) {
            this.renderTextureOverlay(guiGraphics0, POWDER_SNOW_OUTLINE_LOCATION, this.minecraft.player.m_146889_());
        }
        float $$6 = Mth.lerp(float1, this.minecraft.player.oSpinningEffectIntensity, this.minecraft.player.spinningEffectIntensity);
        if ($$6 > 0.0F && !this.minecraft.player.m_21023_(MobEffects.CONFUSION)) {
            this.renderPortalOverlay(guiGraphics0, $$6);
        }
        if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            this.spectatorGui.renderHotbar(guiGraphics0);
        } else if (!this.minecraft.options.hideGui) {
            this.renderHotbar(float1, guiGraphics0);
        }
        if (!this.minecraft.options.hideGui) {
            RenderSystem.enableBlend();
            this.renderCrosshair(guiGraphics0);
            this.minecraft.getProfiler().push("bossHealth");
            this.bossOverlay.render(guiGraphics0);
            this.minecraft.getProfiler().pop();
            if (this.minecraft.gameMode.canHurtPlayer()) {
                this.renderPlayerHealth(guiGraphics0);
            }
            this.renderVehicleHealth(guiGraphics0);
            RenderSystem.disableBlend();
            int $$7 = this.screenWidth / 2 - 91;
            PlayerRideableJumping $$8 = this.minecraft.player.jumpableVehicle();
            if ($$8 != null) {
                this.renderJumpMeter($$8, guiGraphics0, $$7);
            } else if (this.minecraft.gameMode.hasExperience()) {
                this.renderExperienceBar(guiGraphics0, $$7);
            }
            if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
                this.renderSelectedItemName(guiGraphics0);
            } else if (this.minecraft.player.m_5833_()) {
                this.spectatorGui.renderTooltip(guiGraphics0);
            }
        }
        if (this.minecraft.player.m_36318_() > 0) {
            this.minecraft.getProfiler().push("sleep");
            float $$9 = (float) this.minecraft.player.m_36318_();
            float $$10 = $$9 / 100.0F;
            if ($$10 > 1.0F) {
                $$10 = 1.0F - ($$9 - 100.0F) / 10.0F;
            }
            int $$11 = (int) (220.0F * $$10) << 24 | 1052704;
            guiGraphics0.fill(RenderType.guiOverlay(), 0, 0, this.screenWidth, this.screenHeight, $$11);
            this.minecraft.getProfiler().pop();
        }
        if (this.minecraft.isDemo()) {
            this.renderDemoOverlay(guiGraphics0);
        }
        this.renderEffects(guiGraphics0);
        if (this.minecraft.options.renderDebug) {
            this.debugScreen.render(guiGraphics0);
        }
        if (!this.minecraft.options.hideGui) {
            if (this.overlayMessageString != null && this.overlayMessageTime > 0) {
                this.minecraft.getProfiler().push("overlayMessage");
                float $$12 = (float) this.overlayMessageTime - float1;
                int $$13 = (int) ($$12 * 255.0F / 20.0F);
                if ($$13 > 255) {
                    $$13 = 255;
                }
                if ($$13 > 8) {
                    guiGraphics0.pose().pushPose();
                    guiGraphics0.pose().translate((float) (this.screenWidth / 2), (float) (this.screenHeight - 68), 0.0F);
                    int $$14 = 16777215;
                    if (this.animateOverlayMessageColor) {
                        $$14 = Mth.hsvToRgb($$12 / 50.0F, 0.7F, 0.6F) & 16777215;
                    }
                    int $$15 = $$13 << 24 & 0xFF000000;
                    int $$16 = $$3.width(this.overlayMessageString);
                    this.drawBackdrop(guiGraphics0, $$3, -4, $$16, 16777215 | $$15);
                    guiGraphics0.drawString($$3, this.overlayMessageString, -$$16 / 2, -4, $$14 | $$15);
                    guiGraphics0.pose().popPose();
                }
                this.minecraft.getProfiler().pop();
            }
            if (this.title != null && this.titleTime > 0) {
                this.minecraft.getProfiler().push("titleAndSubtitle");
                float $$17 = (float) this.titleTime - float1;
                int $$18 = 255;
                if (this.titleTime > this.titleFadeOutTime + this.titleStayTime) {
                    float $$19 = (float) (this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime) - $$17;
                    $$18 = (int) ($$19 * 255.0F / (float) this.titleFadeInTime);
                }
                if (this.titleTime <= this.titleFadeOutTime) {
                    $$18 = (int) ($$17 * 255.0F / (float) this.titleFadeOutTime);
                }
                $$18 = Mth.clamp($$18, 0, 255);
                if ($$18 > 8) {
                    guiGraphics0.pose().pushPose();
                    guiGraphics0.pose().translate((float) (this.screenWidth / 2), (float) (this.screenHeight / 2), 0.0F);
                    RenderSystem.enableBlend();
                    guiGraphics0.pose().pushPose();
                    guiGraphics0.pose().scale(4.0F, 4.0F, 4.0F);
                    int $$20 = $$18 << 24 & 0xFF000000;
                    int $$21 = $$3.width(this.title);
                    this.drawBackdrop(guiGraphics0, $$3, -10, $$21, 16777215 | $$20);
                    guiGraphics0.drawString($$3, this.title, -$$21 / 2, -10, 16777215 | $$20);
                    guiGraphics0.pose().popPose();
                    if (this.subtitle != null) {
                        guiGraphics0.pose().pushPose();
                        guiGraphics0.pose().scale(2.0F, 2.0F, 2.0F);
                        int $$22 = $$3.width(this.subtitle);
                        this.drawBackdrop(guiGraphics0, $$3, 5, $$22, 16777215 | $$20);
                        guiGraphics0.drawString($$3, this.subtitle, -$$22 / 2, 5, 16777215 | $$20);
                        guiGraphics0.pose().popPose();
                    }
                    RenderSystem.disableBlend();
                    guiGraphics0.pose().popPose();
                }
                this.minecraft.getProfiler().pop();
            }
            this.subtitleOverlay.render(guiGraphics0);
            Scoreboard $$23 = this.minecraft.level.getScoreboard();
            Objective $$24 = null;
            PlayerTeam $$25 = $$23.getPlayersTeam(this.minecraft.player.m_6302_());
            if ($$25 != null) {
                int $$26 = $$25.getColor().getId();
                if ($$26 >= 0) {
                    $$24 = $$23.getDisplayObjective(3 + $$26);
                }
            }
            Objective $$27 = $$24 != null ? $$24 : $$23.getDisplayObjective(1);
            if ($$27 != null) {
                this.displayScoreboardSidebar(guiGraphics0, $$27);
            }
            RenderSystem.enableBlend();
            int $$28 = Mth.floor(this.minecraft.mouseHandler.xpos() * (double) $$2.getGuiScaledWidth() / (double) $$2.getScreenWidth());
            int $$29 = Mth.floor(this.minecraft.mouseHandler.ypos() * (double) $$2.getGuiScaledHeight() / (double) $$2.getScreenHeight());
            this.minecraft.getProfiler().push("chat");
            this.chat.render(guiGraphics0, this.tickCount, $$28, $$29);
            this.minecraft.getProfiler().pop();
            $$27 = $$23.getDisplayObjective(0);
            if (!this.minecraft.options.keyPlayerList.isDown() || this.minecraft.isLocalServer() && this.minecraft.player.connection.getListedOnlinePlayers().size() <= 1 && $$27 == null) {
                this.tabList.setVisible(false);
            } else {
                this.tabList.setVisible(true);
                this.tabList.render(guiGraphics0, this.screenWidth, $$23, $$27);
            }
            this.renderSavingIndicator(guiGraphics0);
        }
    }

    private void drawBackdrop(GuiGraphics guiGraphics0, Font font1, int int2, int int3, int int4) {
        int $$5 = this.minecraft.options.getBackgroundColor(0.0F);
        if ($$5 != 0) {
            int $$6 = -int3 / 2;
            guiGraphics0.fill($$6 - 2, int2 - 2, $$6 + int3 + 2, int2 + 9 + 2, FastColor.ARGB32.multiply($$5, int4));
        }
    }

    private void renderCrosshair(GuiGraphics guiGraphics0) {
        Options $$1 = this.minecraft.options;
        if ($$1.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR || this.canRenderCrosshairForSpectator(this.minecraft.hitResult)) {
                if ($$1.renderDebug && !$$1.hideGui && !this.minecraft.player.m_36330_() && !$$1.reducedDebugInfo().get()) {
                    Camera $$2 = this.minecraft.gameRenderer.getMainCamera();
                    PoseStack $$3 = RenderSystem.getModelViewStack();
                    $$3.pushPose();
                    $$3.mulPoseMatrix(guiGraphics0.pose().last().pose());
                    $$3.translate((float) (this.screenWidth / 2), (float) (this.screenHeight / 2), 0.0F);
                    $$3.mulPose(Axis.XN.rotationDegrees($$2.getXRot()));
                    $$3.mulPose(Axis.YP.rotationDegrees($$2.getYRot()));
                    $$3.scale(-1.0F, -1.0F, -1.0F);
                    RenderSystem.applyModelViewMatrix();
                    RenderSystem.renderCrosshair(10);
                    $$3.popPose();
                    RenderSystem.applyModelViewMatrix();
                } else {
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    int $$4 = 15;
                    guiGraphics0.blit(GUI_ICONS_LOCATION, (this.screenWidth - 15) / 2, (this.screenHeight - 15) / 2, 0, 0, 15, 15);
                    if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR) {
                        float $$5 = this.minecraft.player.m_36403_(0.0F);
                        boolean $$6 = false;
                        if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && $$5 >= 1.0F) {
                            $$6 = this.minecraft.player.m_36333_() > 5.0F;
                            $$6 &= this.minecraft.crosshairPickEntity.isAlive();
                        }
                        int $$7 = this.screenHeight / 2 - 7 + 16;
                        int $$8 = this.screenWidth / 2 - 8;
                        if ($$6) {
                            guiGraphics0.blit(GUI_ICONS_LOCATION, $$8, $$7, 68, 94, 16, 16);
                        } else if ($$5 < 1.0F) {
                            int $$9 = (int) ($$5 * 17.0F);
                            guiGraphics0.blit(GUI_ICONS_LOCATION, $$8, $$7, 36, 94, 16, 4);
                            guiGraphics0.blit(GUI_ICONS_LOCATION, $$8, $$7, 52, 94, $$9, 4);
                        }
                    }
                    RenderSystem.defaultBlendFunc();
                }
            }
        }
    }

    private boolean canRenderCrosshairForSpectator(HitResult hitResult0) {
        if (hitResult0 == null) {
            return false;
        } else if (hitResult0.getType() == HitResult.Type.ENTITY) {
            return ((EntityHitResult) hitResult0).getEntity() instanceof MenuProvider;
        } else if (hitResult0.getType() == HitResult.Type.BLOCK) {
            BlockPos $$1 = ((BlockHitResult) hitResult0).getBlockPos();
            Level $$2 = this.minecraft.level;
            return $$2.getBlockState($$1).m_60750_($$2, $$1) != null;
        } else {
            return false;
        }
    }

    protected void renderEffects(GuiGraphics guiGraphics0) {
        Collection<MobEffectInstance> $$1 = this.minecraft.player.m_21220_();
        if (!$$1.isEmpty()) {
            if (this.minecraft.screen instanceof EffectRenderingInventoryScreen $$2 && $$2.canSeeEffects()) {
                return;
            }
            RenderSystem.enableBlend();
            int $$3 = 0;
            int $$4 = 0;
            MobEffectTextureManager $$5 = this.minecraft.getMobEffectTextures();
            List<Runnable> $$6 = Lists.newArrayListWithExpectedSize($$1.size());
            for (MobEffectInstance $$7 : Ordering.natural().reverse().sortedCopy($$1)) {
                MobEffect $$8 = $$7.getEffect();
                if ($$7.showIcon()) {
                    int $$9 = this.screenWidth;
                    int $$10 = 1;
                    if (this.minecraft.isDemo()) {
                        $$10 += 15;
                    }
                    if ($$8.isBeneficial()) {
                        $$3++;
                        $$9 -= 25 * $$3;
                    } else {
                        $$4++;
                        $$9 -= 25 * $$4;
                        $$10 += 26;
                    }
                    float $$11 = 1.0F;
                    if ($$7.isAmbient()) {
                        guiGraphics0.blit(AbstractContainerScreen.INVENTORY_LOCATION, $$9, $$10, 165, 166, 24, 24);
                    } else {
                        guiGraphics0.blit(AbstractContainerScreen.INVENTORY_LOCATION, $$9, $$10, 141, 166, 24, 24);
                        if ($$7.endsWithin(200)) {
                            int $$12 = $$7.getDuration();
                            int $$13 = 10 - $$12 / 20;
                            $$11 = Mth.clamp((float) $$12 / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((float) $$12 * (float) Math.PI / 5.0F) * Mth.clamp((float) $$13 / 10.0F * 0.25F, 0.0F, 0.25F);
                        }
                    }
                    TextureAtlasSprite $$14 = $$5.get($$8);
                    int $$15 = $$9;
                    int $$16 = $$10;
                    float $$17 = $$11;
                    $$6.add((Runnable) () -> {
                        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, $$17);
                        guiGraphics0.blit($$15 + 3, $$16 + 3, 0, 18, 18, $$14);
                        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    });
                }
            }
            $$6.forEach(Runnable::run);
        }
    }

    private void renderHotbar(float float0, GuiGraphics guiGraphics1) {
        Player $$2 = this.getCameraPlayer();
        if ($$2 != null) {
            ItemStack $$3 = $$2.m_21206_();
            HumanoidArm $$4 = $$2.getMainArm().getOpposite();
            int $$5 = this.screenWidth / 2;
            int $$6 = 182;
            int $$7 = 91;
            guiGraphics1.pose().pushPose();
            guiGraphics1.pose().translate(0.0F, 0.0F, -90.0F);
            guiGraphics1.blit(WIDGETS_LOCATION, $$5 - 91, this.screenHeight - 22, 0, 0, 182, 22);
            guiGraphics1.blit(WIDGETS_LOCATION, $$5 - 91 - 1 + $$2.getInventory().selected * 20, this.screenHeight - 22 - 1, 0, 22, 24, 22);
            if (!$$3.isEmpty()) {
                if ($$4 == HumanoidArm.LEFT) {
                    guiGraphics1.blit(WIDGETS_LOCATION, $$5 - 91 - 29, this.screenHeight - 23, 24, 22, 29, 24);
                } else {
                    guiGraphics1.blit(WIDGETS_LOCATION, $$5 + 91, this.screenHeight - 23, 53, 22, 29, 24);
                }
            }
            guiGraphics1.pose().popPose();
            int $$8 = 1;
            for (int $$9 = 0; $$9 < 9; $$9++) {
                int $$10 = $$5 - 90 + $$9 * 20 + 2;
                int $$11 = this.screenHeight - 16 - 3;
                this.renderSlot(guiGraphics1, $$10, $$11, float0, $$2, $$2.getInventory().items.get($$9), $$8++);
            }
            if (!$$3.isEmpty()) {
                int $$12 = this.screenHeight - 16 - 3;
                if ($$4 == HumanoidArm.LEFT) {
                    this.renderSlot(guiGraphics1, $$5 - 91 - 26, $$12, float0, $$2, $$3, $$8++);
                } else {
                    this.renderSlot(guiGraphics1, $$5 + 91 + 10, $$12, float0, $$2, $$3, $$8++);
                }
            }
            RenderSystem.enableBlend();
            if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
                float $$13 = this.minecraft.player.m_36403_(0.0F);
                if ($$13 < 1.0F) {
                    int $$14 = this.screenHeight - 20;
                    int $$15 = $$5 + 91 + 6;
                    if ($$4 == HumanoidArm.RIGHT) {
                        $$15 = $$5 - 91 - 22;
                    }
                    int $$16 = (int) ($$13 * 19.0F);
                    guiGraphics1.blit(GUI_ICONS_LOCATION, $$15, $$14, 0, 94, 18, 18);
                    guiGraphics1.blit(GUI_ICONS_LOCATION, $$15, $$14 + 18 - $$16, 18, 112 - $$16, 18, $$16);
                }
            }
            RenderSystem.disableBlend();
        }
    }

    public void renderJumpMeter(PlayerRideableJumping playerRideableJumping0, GuiGraphics guiGraphics1, int int2) {
        this.minecraft.getProfiler().push("jumpBar");
        float $$3 = this.minecraft.player.getJumpRidingScale();
        int $$4 = 182;
        int $$5 = (int) ($$3 * 183.0F);
        int $$6 = this.screenHeight - 32 + 3;
        guiGraphics1.blit(GUI_ICONS_LOCATION, int2, $$6, 0, 84, 182, 5);
        if (playerRideableJumping0.getJumpCooldown() > 0) {
            guiGraphics1.blit(GUI_ICONS_LOCATION, int2, $$6, 0, 74, 182, 5);
        } else if ($$5 > 0) {
            guiGraphics1.blit(GUI_ICONS_LOCATION, int2, $$6, 0, 89, $$5, 5);
        }
        this.minecraft.getProfiler().pop();
    }

    public void renderExperienceBar(GuiGraphics guiGraphics0, int int1) {
        this.minecraft.getProfiler().push("expBar");
        int $$2 = this.minecraft.player.m_36323_();
        if ($$2 > 0) {
            int $$3 = 182;
            int $$4 = (int) (this.minecraft.player.f_36080_ * 183.0F);
            int $$5 = this.screenHeight - 32 + 3;
            guiGraphics0.blit(GUI_ICONS_LOCATION, int1, $$5, 0, 64, 182, 5);
            if ($$4 > 0) {
                guiGraphics0.blit(GUI_ICONS_LOCATION, int1, $$5, 0, 69, $$4, 5);
            }
        }
        this.minecraft.getProfiler().pop();
        if (this.minecraft.player.f_36078_ > 0) {
            this.minecraft.getProfiler().push("expLevel");
            String $$6 = this.minecraft.player.f_36078_ + "";
            int $$7 = (this.screenWidth - this.getFont().width($$6)) / 2;
            int $$8 = this.screenHeight - 31 - 4;
            guiGraphics0.drawString(this.getFont(), $$6, $$7 + 1, $$8, 0, false);
            guiGraphics0.drawString(this.getFont(), $$6, $$7 - 1, $$8, 0, false);
            guiGraphics0.drawString(this.getFont(), $$6, $$7, $$8 + 1, 0, false);
            guiGraphics0.drawString(this.getFont(), $$6, $$7, $$8 - 1, 0, false);
            guiGraphics0.drawString(this.getFont(), $$6, $$7, $$8, 8453920, false);
            this.minecraft.getProfiler().pop();
        }
    }

    public void renderSelectedItemName(GuiGraphics guiGraphics0) {
        this.minecraft.getProfiler().push("selectedItemName");
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
            MutableComponent $$1 = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color);
            if (this.lastToolHighlight.hasCustomHoverName()) {
                $$1.withStyle(ChatFormatting.ITALIC);
            }
            int $$2 = this.getFont().width($$1);
            int $$3 = (this.screenWidth - $$2) / 2;
            int $$4 = this.screenHeight - 59;
            if (!this.minecraft.gameMode.canHurtPlayer()) {
                $$4 += 14;
            }
            int $$5 = (int) ((float) this.toolHighlightTimer * 256.0F / 10.0F);
            if ($$5 > 255) {
                $$5 = 255;
            }
            if ($$5 > 0) {
                guiGraphics0.fill($$3 - 2, $$4 - 2, $$3 + $$2 + 2, $$4 + 9 + 2, this.minecraft.options.getBackgroundColor(0));
                guiGraphics0.drawString(this.getFont(), $$1, $$3, $$4, 16777215 + ($$5 << 24));
            }
        }
        this.minecraft.getProfiler().pop();
    }

    public void renderDemoOverlay(GuiGraphics guiGraphics0) {
        this.minecraft.getProfiler().push("demo");
        Component $$1;
        if (this.minecraft.level.m_46467_() >= 120500L) {
            $$1 = DEMO_EXPIRED_TEXT;
        } else {
            $$1 = Component.translatable("demo.remainingTime", StringUtil.formatTickDuration((int) (120500L - this.minecraft.level.m_46467_())));
        }
        int $$3 = this.getFont().width($$1);
        guiGraphics0.drawString(this.getFont(), $$1, this.screenWidth - $$3 - 10, 5, 16777215);
        this.minecraft.getProfiler().pop();
    }

    private void displayScoreboardSidebar(GuiGraphics guiGraphics0, Objective objective1) {
        Scoreboard $$2 = objective1.getScoreboard();
        Collection<Score> $$3 = $$2.getPlayerScores(objective1);
        List<Score> $$4 = (List<Score>) $$3.stream().filter(p_93027_ -> p_93027_.getOwner() != null && !p_93027_.getOwner().startsWith("#")).collect(Collectors.toList());
        if ($$4.size() > 15) {
            $$3 = Lists.newArrayList(Iterables.skip($$4, $$3.size() - 15));
        } else {
            $$3 = $$4;
        }
        List<Pair<Score, Component>> $$5 = Lists.newArrayListWithCapacity($$3.size());
        Component $$6 = objective1.getDisplayName();
        int $$7 = this.getFont().width($$6);
        int $$8 = $$7;
        int $$9 = this.getFont().width(": ");
        for (Score $$10 : $$3) {
            PlayerTeam $$11 = $$2.getPlayersTeam($$10.getOwner());
            Component $$12 = PlayerTeam.formatNameForTeam($$11, Component.literal($$10.getOwner()));
            $$5.add(Pair.of($$10, $$12));
            $$8 = Math.max($$8, this.getFont().width($$12) + $$9 + this.getFont().width(Integer.toString($$10.getScore())));
        }
        int $$13 = $$3.size() * 9;
        int $$14 = this.screenHeight / 2 + $$13 / 3;
        int $$15 = 3;
        int $$16 = this.screenWidth - $$8 - 3;
        int $$17 = 0;
        int $$18 = this.minecraft.options.getBackgroundColor(0.3F);
        int $$19 = this.minecraft.options.getBackgroundColor(0.4F);
        for (Pair<Score, Component> $$20 : $$5) {
            $$17++;
            Score $$21 = (Score) $$20.getFirst();
            Component $$22 = (Component) $$20.getSecond();
            String $$23 = "" + ChatFormatting.RED + $$21.getScore();
            int $$25 = $$14 - $$17 * 9;
            int $$26 = this.screenWidth - 3 + 2;
            guiGraphics0.fill($$16 - 2, $$25, $$26, $$25 + 9, $$18);
            guiGraphics0.drawString(this.getFont(), $$22, $$16, $$25, -1, false);
            guiGraphics0.drawString(this.getFont(), $$23, $$26 - this.getFont().width($$23), $$25, -1, false);
            if ($$17 == $$3.size()) {
                guiGraphics0.fill($$16 - 2, $$25 - 9 - 1, $$26, $$25 - 1, $$19);
                guiGraphics0.fill($$16 - 2, $$25 - 1, $$26, $$25, $$18);
                guiGraphics0.drawString(this.getFont(), $$6, $$16 + $$8 / 2 - $$7 / 2, $$25 - 9, -1, false);
            }
        }
    }

    private Player getCameraPlayer() {
        return !(this.minecraft.getCameraEntity() instanceof Player) ? null : (Player) this.minecraft.getCameraEntity();
    }

    private LivingEntity getPlayerVehicleWithHealth() {
        Player $$0 = this.getCameraPlayer();
        if ($$0 != null) {
            Entity $$1 = $$0.m_20202_();
            if ($$1 == null) {
                return null;
            }
            if ($$1 instanceof LivingEntity) {
                return (LivingEntity) $$1;
            }
        }
        return null;
    }

    private int getVehicleMaxHearts(LivingEntity livingEntity0) {
        if (livingEntity0 != null && livingEntity0.m_20152_()) {
            float $$1 = livingEntity0.getMaxHealth();
            int $$2 = (int) ($$1 + 0.5F) / 2;
            if ($$2 > 30) {
                $$2 = 30;
            }
            return $$2;
        } else {
            return 0;
        }
    }

    private int getVisibleVehicleHeartRows(int int0) {
        return (int) Math.ceil((double) int0 / 10.0);
    }

    private void renderPlayerHealth(GuiGraphics guiGraphics0) {
        Player $$1 = this.getCameraPlayer();
        if ($$1 != null) {
            int $$2 = Mth.ceil($$1.m_21223_());
            boolean $$3 = this.healthBlinkTime > (long) this.tickCount && (this.healthBlinkTime - (long) this.tickCount) / 3L % 2L == 1L;
            long $$4 = Util.getMillis();
            if ($$2 < this.lastHealth && $$1.f_19802_ > 0) {
                this.lastHealthTime = $$4;
                this.healthBlinkTime = (long) (this.tickCount + 20);
            } else if ($$2 > this.lastHealth && $$1.f_19802_ > 0) {
                this.lastHealthTime = $$4;
                this.healthBlinkTime = (long) (this.tickCount + 10);
            }
            if ($$4 - this.lastHealthTime > 1000L) {
                this.lastHealth = $$2;
                this.displayHealth = $$2;
                this.lastHealthTime = $$4;
            }
            this.lastHealth = $$2;
            int $$5 = this.displayHealth;
            this.random.setSeed((long) (this.tickCount * 312871));
            FoodData $$6 = $$1.getFoodData();
            int $$7 = $$6.getFoodLevel();
            int $$8 = this.screenWidth / 2 - 91;
            int $$9 = this.screenWidth / 2 + 91;
            int $$10 = this.screenHeight - 39;
            float $$11 = Math.max((float) $$1.m_21133_(Attributes.MAX_HEALTH), (float) Math.max($$5, $$2));
            int $$12 = Mth.ceil($$1.getAbsorptionAmount());
            int $$13 = Mth.ceil(($$11 + (float) $$12) / 2.0F / 10.0F);
            int $$14 = Math.max(10 - ($$13 - 2), 3);
            int $$15 = $$10 - ($$13 - 1) * $$14 - 10;
            int $$16 = $$10 - 10;
            int $$17 = $$1.m_21230_();
            int $$18 = -1;
            if ($$1.m_21023_(MobEffects.REGENERATION)) {
                $$18 = this.tickCount % Mth.ceil($$11 + 5.0F);
            }
            this.minecraft.getProfiler().push("armor");
            for (int $$19 = 0; $$19 < 10; $$19++) {
                if ($$17 > 0) {
                    int $$20 = $$8 + $$19 * 8;
                    if ($$19 * 2 + 1 < $$17) {
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$20, $$15, 34, 9, 9, 9);
                    }
                    if ($$19 * 2 + 1 == $$17) {
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$20, $$15, 25, 9, 9, 9);
                    }
                    if ($$19 * 2 + 1 > $$17) {
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$20, $$15, 16, 9, 9, 9);
                    }
                }
            }
            this.minecraft.getProfiler().popPush("health");
            this.renderHearts(guiGraphics0, $$1, $$8, $$10, $$14, $$18, $$11, $$2, $$5, $$12, $$3);
            LivingEntity $$21 = this.getPlayerVehicleWithHealth();
            int $$22 = this.getVehicleMaxHearts($$21);
            if ($$22 == 0) {
                this.minecraft.getProfiler().popPush("food");
                for (int $$23 = 0; $$23 < 10; $$23++) {
                    int $$24 = $$10;
                    int $$25 = 16;
                    int $$26 = 0;
                    if ($$1.m_21023_(MobEffects.HUNGER)) {
                        $$25 += 36;
                        $$26 = 13;
                    }
                    if ($$1.getFoodData().getSaturationLevel() <= 0.0F && this.tickCount % ($$7 * 3 + 1) == 0) {
                        $$24 = $$10 + (this.random.nextInt(3) - 1);
                    }
                    int $$27 = $$9 - $$23 * 8 - 9;
                    guiGraphics0.blit(GUI_ICONS_LOCATION, $$27, $$24, 16 + $$26 * 9, 27, 9, 9);
                    if ($$23 * 2 + 1 < $$7) {
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$27, $$24, $$25 + 36, 27, 9, 9);
                    }
                    if ($$23 * 2 + 1 == $$7) {
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$27, $$24, $$25 + 45, 27, 9, 9);
                    }
                }
                $$16 -= 10;
            }
            this.minecraft.getProfiler().popPush("air");
            int $$28 = $$1.m_6062_();
            int $$29 = Math.min($$1.m_20146_(), $$28);
            if ($$1.m_204029_(FluidTags.WATER) || $$29 < $$28) {
                int $$30 = this.getVisibleVehicleHeartRows($$22) - 1;
                $$16 -= $$30 * 10;
                int $$31 = Mth.ceil((double) ($$29 - 2) * 10.0 / (double) $$28);
                int $$32 = Mth.ceil((double) $$29 * 10.0 / (double) $$28) - $$31;
                for (int $$33 = 0; $$33 < $$31 + $$32; $$33++) {
                    if ($$33 < $$31) {
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$9 - $$33 * 8 - 9, $$16, 16, 18, 9, 9);
                    } else {
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$9 - $$33 * 8 - 9, $$16, 25, 18, 9, 9);
                    }
                }
            }
            this.minecraft.getProfiler().pop();
        }
    }

    private void renderHearts(GuiGraphics guiGraphics0, Player player1, int int2, int int3, int int4, int int5, float float6, int int7, int int8, int int9, boolean boolean10) {
        Gui.HeartType $$11 = Gui.HeartType.forPlayer(player1);
        int $$12 = 9 * (player1.m_9236_().getLevelData().isHardcore() ? 5 : 0);
        int $$13 = Mth.ceil((double) float6 / 2.0);
        int $$14 = Mth.ceil((double) int9 / 2.0);
        int $$15 = $$13 * 2;
        for (int $$16 = $$13 + $$14 - 1; $$16 >= 0; $$16--) {
            int $$17 = $$16 / 10;
            int $$18 = $$16 % 10;
            int $$19 = int2 + $$18 * 8;
            int $$20 = int3 - $$17 * int4;
            if (int7 + int9 <= 4) {
                $$20 += this.random.nextInt(2);
            }
            if ($$16 < $$13 && $$16 == int5) {
                $$20 -= 2;
            }
            this.renderHeart(guiGraphics0, Gui.HeartType.CONTAINER, $$19, $$20, $$12, boolean10, false);
            int $$21 = $$16 * 2;
            boolean $$22 = $$16 >= $$13;
            if ($$22) {
                int $$23 = $$21 - $$15;
                if ($$23 < int9) {
                    boolean $$24 = $$23 + 1 == int9;
                    this.renderHeart(guiGraphics0, $$11 == Gui.HeartType.WITHERED ? $$11 : Gui.HeartType.ABSORBING, $$19, $$20, $$12, false, $$24);
                }
            }
            if (boolean10 && $$21 < int8) {
                boolean $$25 = $$21 + 1 == int8;
                this.renderHeart(guiGraphics0, $$11, $$19, $$20, $$12, true, $$25);
            }
            if ($$21 < int7) {
                boolean $$26 = $$21 + 1 == int7;
                this.renderHeart(guiGraphics0, $$11, $$19, $$20, $$12, false, $$26);
            }
        }
    }

    private void renderHeart(GuiGraphics guiGraphics0, Gui.HeartType guiHeartType1, int int2, int int3, int int4, boolean boolean5, boolean boolean6) {
        guiGraphics0.blit(GUI_ICONS_LOCATION, int2, int3, guiHeartType1.getX(boolean6, boolean5), int4, 9, 9);
    }

    private void renderVehicleHealth(GuiGraphics guiGraphics0) {
        LivingEntity $$1 = this.getPlayerVehicleWithHealth();
        if ($$1 != null) {
            int $$2 = this.getVehicleMaxHearts($$1);
            if ($$2 != 0) {
                int $$3 = (int) Math.ceil((double) $$1.getHealth());
                this.minecraft.getProfiler().popPush("mountHealth");
                int $$4 = this.screenHeight - 39;
                int $$5 = this.screenWidth / 2 + 91;
                int $$6 = $$4;
                int $$7 = 0;
                for (boolean $$8 = false; $$2 > 0; $$7 += 20) {
                    int $$9 = Math.min($$2, 10);
                    $$2 -= $$9;
                    for (int $$10 = 0; $$10 < $$9; $$10++) {
                        int $$11 = 52;
                        int $$12 = 0;
                        int $$13 = $$5 - $$10 * 8 - 9;
                        guiGraphics0.blit(GUI_ICONS_LOCATION, $$13, $$6, 52 + $$12 * 9, 9, 9, 9);
                        if ($$10 * 2 + 1 + $$7 < $$3) {
                            guiGraphics0.blit(GUI_ICONS_LOCATION, $$13, $$6, 88, 9, 9, 9);
                        }
                        if ($$10 * 2 + 1 + $$7 == $$3) {
                            guiGraphics0.blit(GUI_ICONS_LOCATION, $$13, $$6, 97, 9, 9, 9);
                        }
                    }
                    $$6 -= 10;
                }
            }
        }
    }

    private void renderTextureOverlay(GuiGraphics guiGraphics0, ResourceLocation resourceLocation1, float float2) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, float2);
        guiGraphics0.blit(resourceLocation1, 0, 0, -90, 0.0F, 0.0F, this.screenWidth, this.screenHeight, this.screenWidth, this.screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderSpyglassOverlay(GuiGraphics guiGraphics0, float float1) {
        float $$2 = (float) Math.min(this.screenWidth, this.screenHeight);
        float $$4 = Math.min((float) this.screenWidth / $$2, (float) this.screenHeight / $$2) * float1;
        int $$5 = Mth.floor($$2 * $$4);
        int $$6 = Mth.floor($$2 * $$4);
        int $$7 = (this.screenWidth - $$5) / 2;
        int $$8 = (this.screenHeight - $$6) / 2;
        int $$9 = $$7 + $$5;
        int $$10 = $$8 + $$6;
        guiGraphics0.blit(SPYGLASS_SCOPE_LOCATION, $$7, $$8, -90, 0.0F, 0.0F, $$5, $$6, $$5, $$6);
        guiGraphics0.fill(RenderType.guiOverlay(), 0, $$10, this.screenWidth, this.screenHeight, -90, -16777216);
        guiGraphics0.fill(RenderType.guiOverlay(), 0, 0, this.screenWidth, $$8, -90, -16777216);
        guiGraphics0.fill(RenderType.guiOverlay(), 0, $$8, $$7, $$10, -90, -16777216);
        guiGraphics0.fill(RenderType.guiOverlay(), $$9, $$8, this.screenWidth, $$10, -90, -16777216);
    }

    private void updateVignetteBrightness(Entity entity0) {
        if (entity0 != null) {
            BlockPos $$1 = BlockPos.containing(entity0.getX(), entity0.getEyeY(), entity0.getZ());
            float $$2 = LightTexture.getBrightness(entity0.level().dimensionType(), entity0.level().m_46803_($$1));
            float $$3 = Mth.clamp(1.0F - $$2, 0.0F, 1.0F);
            this.vignetteBrightness = this.vignetteBrightness + ($$3 - this.vignetteBrightness) * 0.01F;
        }
    }

    private void renderVignette(GuiGraphics guiGraphics0, Entity entity1) {
        WorldBorder $$2 = this.minecraft.level.m_6857_();
        float $$3 = (float) $$2.getDistanceToBorder(entity1);
        double $$4 = Math.min($$2.getLerpSpeed() * (double) $$2.getWarningTime() * 1000.0, Math.abs($$2.getLerpTarget() - $$2.getSize()));
        double $$5 = Math.max((double) $$2.getWarningBlocks(), $$4);
        if ((double) $$3 < $$5) {
            $$3 = 1.0F - (float) ((double) $$3 / $$5);
        } else {
            $$3 = 0.0F;
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        if ($$3 > 0.0F) {
            $$3 = Mth.clamp($$3, 0.0F, 1.0F);
            guiGraphics0.setColor(0.0F, $$3, $$3, 1.0F);
        } else {
            float $$6 = this.vignetteBrightness;
            $$6 = Mth.clamp($$6, 0.0F, 1.0F);
            guiGraphics0.setColor($$6, $$6, $$6, 1.0F);
        }
        guiGraphics0.blit(VIGNETTE_LOCATION, 0, 0, -90, 0.0F, 0.0F, this.screenWidth, this.screenHeight, this.screenWidth, this.screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }

    private void renderPortalOverlay(GuiGraphics guiGraphics0, float float1) {
        if (float1 < 1.0F) {
            float1 *= float1;
            float1 *= float1;
            float1 = float1 * 0.8F + 0.2F;
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, float1);
        TextureAtlasSprite $$2 = this.minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
        guiGraphics0.blit(0, 0, -90, this.screenWidth, this.screenHeight, $$2);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderSlot(GuiGraphics guiGraphics0, int int1, int int2, float float3, Player player4, ItemStack itemStack5, int int6) {
        if (!itemStack5.isEmpty()) {
            float $$7 = (float) itemStack5.getPopTime() - float3;
            if ($$7 > 0.0F) {
                float $$8 = 1.0F + $$7 / 5.0F;
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().translate((float) (int1 + 8), (float) (int2 + 12), 0.0F);
                guiGraphics0.pose().scale(1.0F / $$8, ($$8 + 1.0F) / 2.0F, 1.0F);
                guiGraphics0.pose().translate((float) (-(int1 + 8)), (float) (-(int2 + 12)), 0.0F);
            }
            guiGraphics0.renderItem(player4, itemStack5, int1, int2, int6);
            if ($$7 > 0.0F) {
                guiGraphics0.pose().popPose();
            }
            guiGraphics0.renderItemDecorations(this.minecraft.font, itemStack5, int1, int2);
        }
    }

    public void tick(boolean boolean0) {
        this.tickAutosaveIndicator();
        if (!boolean0) {
            this.tick();
        }
    }

    private void tick() {
        if (this.overlayMessageTime > 0) {
            this.overlayMessageTime--;
        }
        if (this.titleTime > 0) {
            this.titleTime--;
            if (this.titleTime <= 0) {
                this.title = null;
                this.subtitle = null;
            }
        }
        this.tickCount++;
        Entity $$0 = this.minecraft.getCameraEntity();
        if ($$0 != null) {
            this.updateVignetteBrightness($$0);
        }
        if (this.minecraft.player != null) {
            ItemStack $$1 = this.minecraft.player.m_150109_().getSelected();
            if ($$1.isEmpty()) {
                this.toolHighlightTimer = 0;
            } else if (this.lastToolHighlight.isEmpty() || !$$1.is(this.lastToolHighlight.getItem()) || !$$1.getHoverName().equals(this.lastToolHighlight.getHoverName())) {
                this.toolHighlightTimer = (int) (40.0 * this.minecraft.options.notificationDisplayTime().get());
            } else if (this.toolHighlightTimer > 0) {
                this.toolHighlightTimer--;
            }
            this.lastToolHighlight = $$1;
        }
        this.chat.tick();
    }

    private void tickAutosaveIndicator() {
        MinecraftServer $$0 = this.minecraft.getSingleplayerServer();
        boolean $$1 = $$0 != null && $$0.isCurrentlySaving();
        this.lastAutosaveIndicatorValue = this.autosaveIndicatorValue;
        this.autosaveIndicatorValue = Mth.lerp(0.2F, this.autosaveIndicatorValue, $$1 ? 1.0F : 0.0F);
    }

    public void setNowPlaying(Component component0) {
        Component $$1 = Component.translatable("record.nowPlaying", component0);
        this.setOverlayMessage($$1, true);
        this.minecraft.getNarrator().sayNow($$1);
    }

    public void setOverlayMessage(Component component0, boolean boolean1) {
        this.setChatDisabledByPlayerShown(false);
        this.overlayMessageString = component0;
        this.overlayMessageTime = 60;
        this.animateOverlayMessageColor = boolean1;
    }

    public void setChatDisabledByPlayerShown(boolean boolean0) {
        this.chatDisabledByPlayerShown = boolean0;
    }

    public boolean isShowingChatDisabledByPlayer() {
        return this.chatDisabledByPlayerShown && this.overlayMessageTime > 0;
    }

    public void setTimes(int int0, int int1, int int2) {
        if (int0 >= 0) {
            this.titleFadeInTime = int0;
        }
        if (int1 >= 0) {
            this.titleStayTime = int1;
        }
        if (int2 >= 0) {
            this.titleFadeOutTime = int2;
        }
        if (this.titleTime > 0) {
            this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
        }
    }

    public void setSubtitle(Component component0) {
        this.subtitle = component0;
    }

    public void setTitle(Component component0) {
        this.title = component0;
        this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
    }

    public void clear() {
        this.title = null;
        this.subtitle = null;
        this.titleTime = 0;
    }

    public ChatComponent getChat() {
        return this.chat;
    }

    public int getGuiTicks() {
        return this.tickCount;
    }

    public Font getFont() {
        return this.minecraft.font;
    }

    public SpectatorGui getSpectatorGui() {
        return this.spectatorGui;
    }

    public PlayerTabOverlay getTabList() {
        return this.tabList;
    }

    public void onDisconnected() {
        this.tabList.reset();
        this.bossOverlay.reset();
        this.minecraft.getToasts().clear();
        this.minecraft.options.renderDebug = false;
        this.chat.clearMessages(true);
    }

    public BossHealthOverlay getBossOverlay() {
        return this.bossOverlay;
    }

    public void clearCache() {
        this.debugScreen.clearChunkCache();
    }

    private void renderSavingIndicator(GuiGraphics guiGraphics0) {
        if (this.minecraft.options.showAutosaveIndicator().get() && (this.autosaveIndicatorValue > 0.0F || this.lastAutosaveIndicatorValue > 0.0F)) {
            int $$1 = Mth.floor(255.0F * Mth.clamp(Mth.lerp(this.minecraft.getFrameTime(), this.lastAutosaveIndicatorValue, this.autosaveIndicatorValue), 0.0F, 1.0F));
            if ($$1 > 8) {
                Font $$2 = this.getFont();
                int $$3 = $$2.width(SAVING_TEXT);
                int $$4 = 16777215 | $$1 << 24 & 0xFF000000;
                guiGraphics0.drawString($$2, SAVING_TEXT, this.screenWidth - $$3 - 10, this.screenHeight - 15, $$4);
            }
        }
    }

    static enum HeartType {

        CONTAINER(0, false),
        NORMAL(2, true),
        POISIONED(4, true),
        WITHERED(6, true),
        ABSORBING(8, false),
        FROZEN(9, false);

        private final int index;

        private final boolean canBlink;

        private HeartType(int p_168729_, boolean p_168730_) {
            this.index = p_168729_;
            this.canBlink = p_168730_;
        }

        public int getX(boolean p_168735_, boolean p_168736_) {
            int $$2;
            if (this == CONTAINER) {
                $$2 = p_168736_ ? 1 : 0;
            } else {
                int $$3 = p_168735_ ? 1 : 0;
                int $$4 = this.canBlink && p_168736_ ? 2 : 0;
                $$2 = $$3 + $$4;
            }
            return 16 + (this.index * 2 + $$2) * 9;
        }

        static Gui.HeartType forPlayer(Player p_168733_) {
            Gui.HeartType $$1;
            if (p_168733_.m_21023_(MobEffects.POISON)) {
                $$1 = POISIONED;
            } else if (p_168733_.m_21023_(MobEffects.WITHER)) {
                $$1 = WITHERED;
            } else if (p_168733_.m_146890_()) {
                $$1 = FROZEN;
            } else {
                $$1 = NORMAL;
            }
            return $$1;
        }
    }
}
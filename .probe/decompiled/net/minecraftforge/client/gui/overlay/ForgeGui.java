package net.minecraftforge.client.gui.overlay;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.scores.Objective;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeGui extends Gui {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int WHITE = 16777215;

    public static double rayTraceDistance = 20.0;

    public int leftHeight = 39;

    public int rightHeight = 39;

    private Font font = null;

    private final ForgeGui.ForgeDebugScreenOverlay debugOverlay;

    public ForgeGui(Minecraft mc) {
        super(mc, mc.getItemRenderer());
        this.debugOverlay = new ForgeGui.ForgeDebugScreenOverlay(mc);
    }

    public Minecraft getMinecraft() {
        return this.f_92986_;
    }

    public void setupOverlayRenderState(boolean blend, boolean depthTest) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        } else {
            RenderSystem.disableBlend();
        }
        if (depthTest) {
            RenderSystem.enableDepthTest();
        } else {
            RenderSystem.disableDepthTest();
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::m_172817_);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float partialTick) {
        this.f_92977_ = this.f_92986_.getWindow().getGuiScaledWidth();
        this.f_92978_ = this.f_92986_.getWindow().getGuiScaledHeight();
        this.rightHeight = 39;
        this.leftHeight = 39;
        if (!MinecraftForge.EVENT_BUS.post(new RenderGuiEvent.Pre(this.f_92986_.getWindow(), guiGraphics, partialTick))) {
            this.font = this.f_92986_.font;
            this.f_92985_.setSeed((long) this.f_92989_ * 312871L);
            GuiOverlayManager.getOverlays().forEach(entry -> {
                try {
                    IGuiOverlay overlay = entry.overlay();
                    if (this.pre(entry, guiGraphics)) {
                        return;
                    }
                    overlay.render(this, guiGraphics, partialTick, this.f_92977_, this.f_92978_);
                    this.post(entry, guiGraphics);
                } catch (Exception var5) {
                    LOGGER.error("Error rendering overlay '{}'", entry.id(), var5);
                }
            });
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            MinecraftForge.EVENT_BUS.post(new RenderGuiEvent.Post(this.f_92986_.getWindow(), guiGraphics, partialTick));
        }
    }

    public boolean shouldDrawSurvivalElements() {
        return this.f_92986_.gameMode.canHurtPlayer() && this.f_92986_.getCameraEntity() instanceof Player;
    }

    protected void renderSubtitles(GuiGraphics guiGraphics) {
        this.f_92996_.render(guiGraphics);
    }

    protected void renderBossHealth(GuiGraphics guiGraphics) {
        RenderSystem.defaultBlendFunc();
        this.f_92986_.getProfiler().push("bossHealth");
        this.f_92999_.render(guiGraphics);
        this.f_92986_.getProfiler().pop();
    }

    void renderSpyglassOverlay(GuiGraphics guiGraphics) {
        float deltaFrame = this.f_92986_.getDeltaFrameTime();
        this.f_168664_ = Mth.lerp(0.5F * deltaFrame, this.f_168664_, 1.125F);
        if (this.f_92986_.options.getCameraType().isFirstPerson()) {
            if (this.f_92986_.player.m_150108_()) {
                this.m_280278_(guiGraphics, this.f_168664_);
            } else {
                this.f_168664_ = 0.5F;
            }
        }
    }

    void renderHelmet(float partialTick, GuiGraphics guiGraphics) {
        ItemStack itemstack = this.f_92986_.player.m_150109_().getArmor(3);
        if (this.f_92986_.options.getCameraType().isFirstPerson() && !itemstack.isEmpty()) {
            Item item = itemstack.getItem();
            if (item == Blocks.CARVED_PUMPKIN.asItem()) {
                this.m_280155_(guiGraphics, f_92983_, 1.0F);
            } else {
                IClientItemExtensions.of(item).renderHelmetOverlay(itemstack, this.f_92986_.player, this.f_92977_, this.f_92978_, partialTick);
            }
        }
    }

    void renderFrostbite(GuiGraphics guiGraphics) {
        if (this.f_92986_.player.m_146888_() > 0) {
            this.m_280155_(guiGraphics, f_168666_, this.f_92986_.player.m_146889_());
        }
    }

    protected void renderArmor(GuiGraphics guiGraphics, int width, int height) {
        this.f_92986_.getProfiler().push("armor");
        RenderSystem.enableBlend();
        int left = width / 2 - 91;
        int top = height - this.leftHeight;
        int level = this.f_92986_.player.m_21230_();
        for (int i = 1; level > 0 && i < 20; i += 2) {
            if (i < level) {
                guiGraphics.blit(f_279580_, left, top, 34, 9, 9, 9);
            } else if (i == level) {
                guiGraphics.blit(f_279580_, left, top, 25, 9, 9, 9);
            } else if (i > level) {
                guiGraphics.blit(f_279580_, left, top, 16, 9, 9, 9);
            }
            left += 8;
        }
        this.leftHeight += 10;
        RenderSystem.disableBlend();
        this.f_92986_.getProfiler().pop();
    }

    @Override
    protected void renderPortalOverlay(GuiGraphics guiGraphics, float alpha) {
        if (alpha > 0.0F) {
            super.renderPortalOverlay(guiGraphics, alpha);
        }
    }

    protected void renderAir(int width, int height, GuiGraphics guiGraphics) {
        this.f_92986_.getProfiler().push("air");
        Player player = (Player) this.f_92986_.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - this.rightHeight;
        int air = player.m_20146_();
        if (player.isEyeInFluidType(ForgeMod.WATER_TYPE.get()) || air < 300) {
            int full = Mth.ceil((double) (air - 2) * 10.0 / 300.0);
            int partial = Mth.ceil((double) air * 10.0 / 300.0) - full;
            for (int i = 0; i < full + partial; i++) {
                guiGraphics.blit(f_279580_, left - i * 8 - 9, top, i < full ? 16 : 25, 18, 9, 9);
            }
            this.rightHeight += 10;
        }
        RenderSystem.disableBlend();
        this.f_92986_.getProfiler().pop();
    }

    public void renderHealth(int width, int height, GuiGraphics guiGraphics) {
        this.f_92986_.getProfiler().push("health");
        RenderSystem.enableBlend();
        Player player = (Player) this.f_92986_.getCameraEntity();
        int health = Mth.ceil(player.m_21223_());
        boolean highlight = this.f_92976_ > (long) this.f_92989_ && (this.f_92976_ - (long) this.f_92989_) / 3L % 2L == 1L;
        if (health < this.f_92973_ && player.f_19802_ > 0) {
            this.f_92975_ = Util.getMillis();
            this.f_92976_ = (long) (this.f_92989_ + 20);
        } else if (health > this.f_92973_ && player.f_19802_ > 0) {
            this.f_92975_ = Util.getMillis();
            this.f_92976_ = (long) (this.f_92989_ + 10);
        }
        if (Util.getMillis() - this.f_92975_ > 1000L) {
            this.f_92973_ = health;
            this.f_92974_ = health;
            this.f_92975_ = Util.getMillis();
        }
        this.f_92973_ = health;
        int healthLast = this.f_92974_;
        AttributeInstance attrMaxHealth = player.m_21051_(Attributes.MAX_HEALTH);
        float healthMax = Math.max((float) attrMaxHealth.getValue(), (float) Math.max(healthLast, health));
        int absorb = Mth.ceil(player.getAbsorptionAmount());
        int healthRows = Mth.ceil((healthMax + (float) absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);
        this.f_92985_.setSeed((long) (this.f_92989_ * 312871));
        int left = width / 2 - 91;
        int top = height - this.leftHeight;
        this.leftHeight += healthRows * rowHeight;
        if (rowHeight != 10) {
            this.leftHeight += 10 - rowHeight;
        }
        int regen = -1;
        if (player.m_21023_(MobEffects.REGENERATION)) {
            regen = this.f_92989_ % Mth.ceil(healthMax + 5.0F);
        }
        this.m_168688_(guiGraphics, player, left, top, rowHeight, regen, healthMax, health, healthLast, absorb, highlight);
        RenderSystem.disableBlend();
        this.f_92986_.getProfiler().pop();
    }

    public void renderFood(int width, int height, GuiGraphics guiGraphics) {
        this.f_92986_.getProfiler().push("food");
        Player player = (Player) this.f_92986_.getCameraEntity();
        RenderSystem.enableBlend();
        int left = width / 2 + 91;
        int top = height - this.rightHeight;
        this.rightHeight += 10;
        boolean unused = false;
        FoodData stats = this.f_92986_.player.m_36324_();
        int level = stats.getFoodLevel();
        for (int i = 0; i < 10; i++) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            int icon = 16;
            byte background = 0;
            if (this.f_92986_.player.m_21023_(MobEffects.HUNGER)) {
                icon += 36;
                background = 13;
            }
            if (unused) {
                background = 1;
            }
            if (player.getFoodData().getSaturationLevel() <= 0.0F && this.f_92989_ % (level * 3 + 1) == 0) {
                y = top + (this.f_92985_.nextInt(3) - 1);
            }
            guiGraphics.blit(f_279580_, x, y, 16 + background * 9, 27, 9, 9);
            if (idx < level) {
                guiGraphics.blit(f_279580_, x, y, icon + 36, 27, 9, 9);
            } else if (idx == level) {
                guiGraphics.blit(f_279580_, x, y, icon + 45, 27, 9, 9);
            }
        }
        RenderSystem.disableBlend();
        this.f_92986_.getProfiler().pop();
    }

    protected void renderSleepFade(int width, int height, GuiGraphics guiGraphics) {
        if (this.f_92986_.player.m_36318_() > 0) {
            this.f_92986_.getProfiler().push("sleep");
            int sleepTime = this.f_92986_.player.m_36318_();
            float opacity = (float) sleepTime / 100.0F;
            if (opacity > 1.0F) {
                opacity = 1.0F - (float) (sleepTime - 100) / 10.0F;
            }
            int color = (int) (220.0F * opacity) << 24 | 1052704;
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, height, color);
            this.f_92986_.getProfiler().pop();
        }
    }

    protected void renderExperience(int x, GuiGraphics guiGraphics) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        if (this.f_92986_.gameMode.hasExperience()) {
            super.renderExperienceBar(guiGraphics, x);
        }
        RenderSystem.enableBlend();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderJumpMeter(PlayerRideableJumping playerRideableJumping, GuiGraphics guiGraphics, int x) {
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        super.renderJumpMeter(playerRideableJumping, guiGraphics, x);
        RenderSystem.enableBlend();
        this.f_92986_.getProfiler().pop();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderHUDText(int width, int height, GuiGraphics guiGraphics) {
        this.f_92986_.getProfiler().push("forgeHudText");
        RenderSystem.defaultBlendFunc();
        ArrayList<String> listL = new ArrayList();
        ArrayList<String> listR = new ArrayList();
        if (this.f_92986_.isDemo()) {
            long time = this.f_92986_.level.m_46467_();
            if (time >= 120500L) {
                listR.add(I18n.get("demo.demoExpired"));
            } else {
                listR.add(I18n.get("demo.remainingTime", StringUtil.formatTickDuration((int) (120500L - time))));
            }
        }
        if (this.f_92986_.options.renderDebug) {
            this.debugOverlay.update();
            listL.addAll(this.debugOverlay.getLeft());
            listR.addAll(this.debugOverlay.getRight());
        }
        CustomizeGuiOverlayEvent.DebugText event = new CustomizeGuiOverlayEvent.DebugText(this.f_92986_.getWindow(), guiGraphics, this.f_92986_.getFrameTime(), listL, listR);
        MinecraftForge.EVENT_BUS.post(event);
        int top = 2;
        for (String msg : listL) {
            if (msg != null && !msg.isEmpty()) {
                guiGraphics.fill(1, top - 1, 2 + this.font.width(msg) + 1, top + 9 - 1, -1873784752);
                guiGraphics.drawString(this.font, msg, 2, top, 14737632, false);
            }
            top += 9;
        }
        int var13 = 2;
        for (String msg : listR) {
            if (msg != null && !msg.isEmpty()) {
                int w = this.font.width(msg);
                int left = width - 2 - w;
                guiGraphics.fill(left - 1, var13 - 1, left + w + 1, var13 + 9 - 1, -1873784752);
                guiGraphics.drawString(this.font, msg, left, var13, 14737632, false);
            }
            var13 += 9;
        }
        this.f_92986_.getProfiler().pop();
    }

    protected void renderFPSGraph(GuiGraphics guiGraphics) {
        if (this.f_92986_.options.renderDebug && this.f_92986_.options.renderFpsChart) {
            this.debugOverlay.m_94056_(guiGraphics);
        }
    }

    @Override
    public void clearCache() {
        super.clearCache();
        this.debugOverlay.m_94040_();
    }

    protected void renderRecordOverlay(int width, int height, float partialTick, GuiGraphics guiGraphics) {
        if (this.f_92991_ > 0) {
            this.f_92986_.getProfiler().push("overlayMessage");
            float hue = (float) this.f_92991_ - partialTick;
            int opacity = (int) (hue * 255.0F / 20.0F);
            if (opacity > 255) {
                opacity = 255;
            }
            if (opacity > 8) {
                int yShift = Math.max(this.leftHeight, this.rightHeight) + 9;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((double) width / 2.0, (double) (height - Math.max(yShift, 68)), 0.0);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                int color = this.f_92992_ ? Mth.hsvToRgb(hue / 50.0F, 0.7F, 0.6F) & 16777215 : 16777215;
                int messageWidth = this.font.width(this.f_92990_);
                this.m_93039_(guiGraphics, this.font, -4, messageWidth, 16777215 | opacity << 24);
                guiGraphics.drawString(this.font, this.f_92990_.getVisualOrderText(), -messageWidth / 2, -4, color | opacity << 24);
                RenderSystem.disableBlend();
                guiGraphics.pose().popPose();
            }
            this.f_92986_.getProfiler().pop();
        }
    }

    protected void renderTitle(int width, int height, float partialTick, GuiGraphics guiGraphics) {
        if (this.f_93001_ != null && this.f_93000_ > 0) {
            this.f_92986_.getProfiler().push("titleAndSubtitle");
            float age = (float) this.f_93000_ - partialTick;
            int opacity = 255;
            if (this.f_93000_ > this.f_92972_ + this.f_92971_) {
                float f3 = (float) (this.f_92970_ + this.f_92971_ + this.f_92972_) - age;
                opacity = (int) (f3 * 255.0F / (float) this.f_92970_);
            }
            if (this.f_93000_ <= this.f_92972_) {
                opacity = (int) (age * 255.0F / (float) this.f_92972_);
            }
            opacity = Mth.clamp(opacity, 0, 255);
            if (opacity > 8) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((double) width / 2.0, (double) height / 2.0, 0.0);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(4.0F, 4.0F, 4.0F);
                int l = opacity << 24 & 0xFF000000;
                guiGraphics.drawString(this.font, this.f_93001_.getVisualOrderText(), -this.m_93082_().width(this.f_93001_) / 2, -10, 16777215 | l, true);
                guiGraphics.pose().popPose();
                if (this.f_93002_ != null) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().scale(2.0F, 2.0F, 2.0F);
                    guiGraphics.drawString(this.font, this.f_93002_.getVisualOrderText(), -this.m_93082_().width(this.f_93002_) / 2, 5, 16777215 | l, true);
                    guiGraphics.pose().popPose();
                }
                RenderSystem.disableBlend();
                guiGraphics.pose().popPose();
            }
            this.f_92986_.getProfiler().pop();
        }
    }

    protected void renderChat(int width, int height, GuiGraphics guiGraphics) {
        this.f_92986_.getProfiler().push("chat");
        Window window = this.f_92986_.getWindow();
        CustomizeGuiOverlayEvent.Chat event = new CustomizeGuiOverlayEvent.Chat(window, guiGraphics, this.f_92986_.getFrameTime(), 0, height - 40);
        MinecraftForge.EVENT_BUS.post(event);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((double) event.getPosX(), (double) (event.getPosY() - height + 40) / this.f_92988_.getScale(), 0.0);
        int mouseX = Mth.floor(this.f_92986_.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
        int mouseY = Mth.floor(this.f_92986_.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
        this.f_92988_.render(guiGraphics, this.f_92989_, mouseX, mouseY);
        guiGraphics.pose().popPose();
        this.f_92986_.getProfiler().pop();
    }

    protected void renderPlayerList(int width, int height, GuiGraphics guiGraphics) {
        Objective scoreobjective = this.f_92986_.level.getScoreboard().getDisplayObjective(0);
        ClientPacketListener handler = this.f_92986_.player.connection;
        if (!this.f_92986_.options.keyPlayerList.isDown() || this.f_92986_.isLocalServer() && handler.getOnlinePlayers().size() <= 1 && scoreobjective == null) {
            this.f_92998_.setVisible(false);
        } else {
            this.f_92998_.setVisible(true);
            this.f_92998_.render(guiGraphics, width, this.f_92986_.level.getScoreboard(), scoreobjective);
        }
    }

    protected void renderHealthMount(int width, int height, GuiGraphics guiGraphics) {
        Player player = (Player) this.f_92986_.getCameraEntity();
        Entity tmp = player.m_20202_();
        if (tmp instanceof LivingEntity) {
            boolean unused = false;
            int left_align = width / 2 + 91;
            this.f_92986_.getProfiler().popPush("mountHealth");
            RenderSystem.enableBlend();
            LivingEntity mount = (LivingEntity) tmp;
            int health = (int) Math.ceil((double) mount.getHealth());
            float healthMax = mount.getMaxHealth();
            int hearts = (int) (healthMax + 0.5F) / 2;
            if (hearts > 30) {
                hearts = 30;
            }
            int MARGIN = 52;
            int BACKGROUND = 52 + (unused ? 1 : 0);
            int HALF = 97;
            int FULL = 88;
            for (int heart = 0; hearts > 0; heart += 20) {
                int top = height - this.rightHeight;
                int rowCount = Math.min(hearts, 10);
                hearts -= rowCount;
                for (int i = 0; i < rowCount; i++) {
                    int x = left_align - i * 8 - 9;
                    guiGraphics.blit(f_279580_, x, top, BACKGROUND, 9, 9, 9);
                    if (i * 2 + 1 + heart < health) {
                        guiGraphics.blit(f_279580_, x, top, 88, 9, 9, 9);
                    } else if (i * 2 + 1 + heart == health) {
                        guiGraphics.blit(f_279580_, x, top, 97, 9, 9, 9);
                    }
                }
                this.rightHeight += 10;
            }
            RenderSystem.disableBlend();
        }
    }

    private boolean pre(NamedGuiOverlay overlay, GuiGraphics guiGraphics) {
        return MinecraftForge.EVENT_BUS.post(new RenderGuiOverlayEvent.Pre(this.f_92986_.getWindow(), guiGraphics, this.f_92986_.getFrameTime(), overlay));
    }

    private void post(NamedGuiOverlay overlay, GuiGraphics guiGraphics) {
        MinecraftForge.EVENT_BUS.post(new RenderGuiOverlayEvent.Post(this.f_92986_.getWindow(), guiGraphics, this.f_92986_.getFrameTime(), overlay));
    }

    private static class ForgeDebugScreenOverlay extends DebugScreenOverlay {

        private final Minecraft mc;

        private ForgeDebugScreenOverlay(Minecraft mc) {
            super(mc);
            this.mc = mc;
        }

        public void update() {
            Entity entity = this.mc.getCameraEntity();
            this.f_94032_ = entity.pick(ForgeGui.rayTraceDistance, 0.0F, false);
            this.f_94033_ = entity.pick(ForgeGui.rayTraceDistance, 0.0F, true);
        }

        @Override
        protected void drawGameInformation(GuiGraphics guiGraphics) {
            RenderSystem.disableDepthTest();
        }

        @Override
        protected void drawSystemInformation(GuiGraphics guiGraphics) {
        }

        private List<String> getLeft() {
            List<String> ret = this.m_94075_();
            ret.add("");
            boolean flag = this.mc.getSingleplayerServer() != null;
            ret.add("Debug: Pie [shift]: " + (this.mc.options.renderDebugCharts ? "visible" : "hidden") + (flag ? " FPS + TPS" : " FPS") + " [alt]: " + (this.mc.options.renderFpsChart ? "visible" : "hidden"));
            ret.add("For help: press F3 + Q");
            return ret;
        }

        private List<String> getRight() {
            return this.m_94078_();
        }
    }
}
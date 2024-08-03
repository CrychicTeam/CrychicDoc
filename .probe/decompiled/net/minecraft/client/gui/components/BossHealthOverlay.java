package net.minecraft.client.gui.components;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;

public class BossHealthOverlay {

    private static final ResourceLocation GUI_BARS_LOCATION = new ResourceLocation("textures/gui/bars.png");

    private static final int BAR_WIDTH = 182;

    private static final int BAR_HEIGHT = 5;

    private static final int OVERLAY_OFFSET = 80;

    private final Minecraft minecraft;

    final Map<UUID, LerpingBossEvent> events = Maps.newLinkedHashMap();

    public BossHealthOverlay(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void render(GuiGraphics guiGraphics0) {
        if (!this.events.isEmpty()) {
            int $$1 = guiGraphics0.guiWidth();
            int $$2 = 12;
            for (LerpingBossEvent $$3 : this.events.values()) {
                int $$4 = $$1 / 2 - 91;
                this.drawBar(guiGraphics0, $$4, $$2, $$3);
                Component $$6 = $$3.m_18861_();
                int $$7 = this.minecraft.font.width($$6);
                int $$8 = $$1 / 2 - $$7 / 2;
                int $$9 = $$2 - 9;
                guiGraphics0.drawString(this.minecraft.font, $$6, $$8, $$9, 16777215);
                $$2 += 10 + 9;
                if ($$2 >= guiGraphics0.guiHeight() / 3) {
                    break;
                }
            }
        }
    }

    private void drawBar(GuiGraphics guiGraphics0, int int1, int int2, BossEvent bossEvent3) {
        this.drawBar(guiGraphics0, int1, int2, bossEvent3, 182, 0);
        int $$4 = (int) (bossEvent3.getProgress() * 183.0F);
        if ($$4 > 0) {
            this.drawBar(guiGraphics0, int1, int2, bossEvent3, $$4, 5);
        }
    }

    private void drawBar(GuiGraphics guiGraphics0, int int1, int int2, BossEvent bossEvent3, int int4, int int5) {
        guiGraphics0.blit(GUI_BARS_LOCATION, int1, int2, 0, bossEvent3.getColor().ordinal() * 5 * 2 + int5, int4, 5);
        if (bossEvent3.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
            RenderSystem.enableBlend();
            guiGraphics0.blit(GUI_BARS_LOCATION, int1, int2, 0, 80 + (bossEvent3.getOverlay().ordinal() - 1) * 5 * 2 + int5, int4, 5);
            RenderSystem.disableBlend();
        }
    }

    public void update(ClientboundBossEventPacket clientboundBossEventPacket0) {
        clientboundBossEventPacket0.dispatch(new ClientboundBossEventPacket.Handler() {

            @Override
            public void add(UUID p_168824_, Component p_168825_, float p_168826_, BossEvent.BossBarColor p_168827_, BossEvent.BossBarOverlay p_168828_, boolean p_168829_, boolean p_168830_, boolean p_168831_) {
                BossHealthOverlay.this.events.put(p_168824_, new LerpingBossEvent(p_168824_, p_168825_, p_168826_, p_168827_, p_168828_, p_168829_, p_168830_, p_168831_));
            }

            @Override
            public void remove(UUID p_168812_) {
                BossHealthOverlay.this.events.remove(p_168812_);
            }

            @Override
            public void updateProgress(UUID p_168814_, float p_168815_) {
                ((LerpingBossEvent) BossHealthOverlay.this.events.get(p_168814_)).setProgress(p_168815_);
            }

            @Override
            public void updateName(UUID p_168821_, Component p_168822_) {
                ((LerpingBossEvent) BossHealthOverlay.this.events.get(p_168821_)).m_6456_(p_168822_);
            }

            @Override
            public void updateStyle(UUID p_168817_, BossEvent.BossBarColor p_168818_, BossEvent.BossBarOverlay p_168819_) {
                LerpingBossEvent $$3 = (LerpingBossEvent) BossHealthOverlay.this.events.get(p_168817_);
                $$3.m_6451_(p_168818_);
                $$3.m_5648_(p_168819_);
            }

            @Override
            public void updateProperties(UUID p_168833_, boolean p_168834_, boolean p_168835_, boolean p_168836_) {
                LerpingBossEvent $$4 = (LerpingBossEvent) BossHealthOverlay.this.events.get(p_168833_);
                $$4.m_7003_(p_168834_);
                $$4.m_7005_(p_168835_);
                $$4.m_7006_(p_168836_);
            }
        });
    }

    public void reset() {
        this.events.clear();
    }

    public boolean shouldPlayMusic() {
        if (!this.events.isEmpty()) {
            for (BossEvent $$0 : this.events.values()) {
                if ($$0.shouldPlayBossMusic()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldDarkenScreen() {
        if (!this.events.isEmpty()) {
            for (BossEvent $$0 : this.events.values()) {
                if ($$0.shouldDarkenScreen()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldCreateWorldFog() {
        if (!this.events.isEmpty()) {
            for (BossEvent $$0 : this.events.values()) {
                if ($$0.shouldCreateWorldFog()) {
                    return true;
                }
            }
        }
        return false;
    }
}
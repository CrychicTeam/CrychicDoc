package com.simibubi.create.foundation.config.ui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.config.ui.entries.NumberEntry;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.TickableGuiEventListener;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.element.TextStencilElement;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ConfigScreenList extends ObjectSelectionList<ConfigScreenList.Entry> implements TickableGuiEventListener {

    public static EditBox currentText;

    public ConfigScreenList(Minecraft client, int width, int height, int top, int bottom, int elementHeight) {
        super(client, width, height, top, bottom, elementHeight);
        this.m_93488_(false);
        this.m_93496_(false);
        this.m_93471_(false);
        currentText = null;
        this.f_93395_ = 3;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Color c = new Color(1610612736);
        UIRenderHelper.angledGradient(graphics, 90.0F, this.f_93393_ + this.f_93388_ / 2, this.f_93390_, this.f_93388_, 5, c, Color.TRANSPARENT_BLACK);
        UIRenderHelper.angledGradient(graphics, -90.0F, this.f_93393_ + this.f_93388_ / 2, this.f_93391_, this.f_93388_, 5, c, Color.TRANSPARENT_BLACK);
        UIRenderHelper.angledGradient(graphics, 0.0F, this.f_93393_, this.f_93390_ + this.f_93389_ / 2, this.f_93389_, 5, c, Color.TRANSPARENT_BLACK);
        UIRenderHelper.angledGradient(graphics, 180.0F, this.f_93392_, this.f_93390_ + this.f_93389_ / 2, this.f_93389_, 5, c, Color.TRANSPARENT_BLACK);
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderList(GuiGraphics graphics, int int0, int int1, float float2) {
        Window window = this.f_93386_.getWindow();
        double d0 = window.getGuiScale();
        RenderSystem.enableScissor((int) ((double) this.f_93393_ * d0), (int) ((double) window.getHeight() - (double) this.f_93391_ * d0), (int) ((double) this.f_93388_ * d0), (int) ((double) this.f_93389_ * d0));
        super.m_239227_(graphics, int0, int1, float2);
        RenderSystem.disableScissor();
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        this.m_6702_().stream().filter(e -> e instanceof NumberEntry).forEach(e -> e.mouseClicked(x, y, button));
        return super.m_6375_(x, y, button);
    }

    @Override
    public int getRowWidth() {
        return this.f_93388_ - 16;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.f_93393_ + this.f_93388_ - 6;
    }

    @Override
    public void tick() {
        this.m_6702_().forEach(ConfigScreenList.Entry::tick);
    }

    public boolean search(String query) {
        if (query != null && !query.isEmpty()) {
            String q = query.toLowerCase(Locale.ROOT);
            Optional<ConfigScreenList.Entry> first = this.m_6702_().stream().filter(entry -> {
                if (entry.path == null) {
                    return false;
                } else {
                    String[] split = entry.path.split("\\.");
                    String key = split[split.length - 1].toLowerCase(Locale.ROOT);
                    return key.contains(q);
                }
            }).findFirst();
            if (!first.isPresent()) {
                this.m_93410_(0.0);
                return false;
            } else {
                ConfigScreenList.Entry e = (ConfigScreenList.Entry) first.get();
                e.annotations.put("highlight", "(:");
                this.m_93494_(e);
                return true;
            }
        } else {
            this.m_93410_(0.0);
            return true;
        }
    }

    public void bumpCog(float force) {
        ConfigScreen.cogSpin.bump(3, (double) force);
    }

    public abstract static class Entry extends ObjectSelectionList.Entry<ConfigScreenList.Entry> implements TickableGuiEventListener {

        protected List<GuiEventListener> listeners = new ArrayList();

        protected Map<String, String> annotations = new HashMap();

        protected String path;

        protected Entry() {
        }

        @Override
        public boolean mouseClicked(double x, double y, int button) {
            return this.getGuiListeners().stream().anyMatch(l -> l.mouseClicked(x, y, button));
        }

        @Override
        public boolean keyPressed(int code, int keyPressed_2_, int keyPressed_3_) {
            return this.getGuiListeners().stream().anyMatch(l -> l.keyPressed(code, keyPressed_2_, keyPressed_3_));
        }

        @Override
        public boolean charTyped(char ch, int code) {
            return this.getGuiListeners().stream().anyMatch(l -> l.charTyped(ch, code));
        }

        @Override
        public void tick() {
        }

        public List<GuiEventListener> getGuiListeners() {
            return this.listeners;
        }

        protected void setEditable(boolean b) {
        }

        protected boolean isCurrentValueChanged() {
            return this.path == null ? false : ConfigHelper.changes.containsKey(this.path);
        }
    }

    public static class LabeledEntry extends ConfigScreenList.Entry {

        protected static final float labelWidthMult = 0.4F;

        protected TextStencilElement label;

        protected List<Component> labelTooltip;

        protected String unit = null;

        protected LerpedFloat differenceAnimation = LerpedFloat.linear().startWithValue(0.0);

        protected LerpedFloat highlightAnimation = LerpedFloat.linear().startWithValue(0.0);

        public LabeledEntry(String label) {
            this.label = new TextStencilElement(Minecraft.getInstance().font, label);
            this.label.withElementRenderer((graphics, width, height, alpha) -> UIRenderHelper.angledGradient(graphics, 0.0F, 0, height / 2, height, width, Theme.p(Theme.Key.TEXT_ACCENT_STRONG)));
            this.labelTooltip = new ArrayList();
        }

        public LabeledEntry(String label, String path) {
            this(label);
            this.path = path;
        }

        @Override
        public void tick() {
            this.differenceAnimation.tickChaser();
            this.highlightAnimation.tickChaser();
            super.tick();
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
            if (this.isCurrentValueChanged()) {
                if (this.differenceAnimation.getChaseTarget() != 1.0F) {
                    this.differenceAnimation.chase(1.0, 0.5, LerpedFloat.Chaser.EXP);
                }
            } else if (this.differenceAnimation.getChaseTarget() != 0.0F) {
                this.differenceAnimation.chase(0.0, 0.6F, LerpedFloat.Chaser.EXP);
            }
            float animation = this.differenceAnimation.getValue(partialTicks);
            if (animation > 0.1F) {
                int offset = (int) (30.0F * (1.0F - animation));
                if (this.annotations.containsKey(ConfigAnnotations.RequiresRestart.CLIENT.getName())) {
                    UIRenderHelper.streak(graphics, 180.0F, x + width + 10 + offset, y + height / 2, height - 6, 110, new Color(1348472848));
                } else if (this.annotations.containsKey(ConfigAnnotations.RequiresRelog.TRUE.getName())) {
                    UIRenderHelper.streak(graphics, 180.0F, x + width + 10 + offset, y + height / 2, height - 6, 110, new Color(1089403671));
                }
                UIRenderHelper.breadcrumbArrow(graphics, x - 10 - offset, y + 6, 0, -20, 24, -18, new Color(1895825407), Color.TRANSPARENT_BLACK);
            }
            UIRenderHelper.streak(graphics, 0.0F, x - 10, y + height / 2, height - 6, width / 8 * 7, -587202560);
            UIRenderHelper.streak(graphics, 180.0F, x + (int) ((float) width * 1.35F) + 10, y + height / 2, height - 6, width / 8 * 7, -587202560);
            MutableComponent component = this.label.getComponent();
            Font font = Minecraft.getInstance().font;
            if (font.width(component) > this.getLabelWidth(width) - 10) {
                this.label.withText(font.substrByWidth(component, this.getLabelWidth(width) - 15).getString() + "...");
            }
            if (this.unit != null) {
                int unitWidth = font.width(this.unit);
                graphics.drawString(font, this.unit, x + this.getLabelWidth(width) - unitWidth - 5, y + height / 2 + 2, Theme.i(Theme.Key.TEXT_DARKER), false);
                this.label.<RenderElement>at((float) (x + 10), (float) (y + height / 2 - 10), 0.0F).render(graphics);
            } else {
                this.label.<RenderElement>at((float) (x + 10), (float) (y + height / 2 - 4), 0.0F).render(graphics);
            }
            if (this.annotations.containsKey("highlight")) {
                this.highlightAnimation.startWithValue(1.0).chase(0.0, 0.1F, LerpedFloat.Chaser.LINEAR);
                this.annotations.remove("highlight");
            }
            animation = this.highlightAnimation.getValue(partialTicks);
            if (animation > 0.01F) {
                Color highlight = new Color(-1593835521).scaleAlpha(animation);
                UIRenderHelper.streak(graphics, 0.0F, x - 10, y + height / 2, height - 6, 5, highlight);
                UIRenderHelper.streak(graphics, 180.0F, x + width, y + height / 2, height - 6, 5, highlight);
                UIRenderHelper.streak(graphics, 90.0F, x + width / 2 - 5, y + 3, width + 10, 5, highlight);
                UIRenderHelper.streak(graphics, -90.0F, x + width / 2 - 5, y + height - 3, width + 10, 5, highlight);
            }
            if (mouseX > x && mouseX < x + this.getLabelWidth(width) && mouseY > y + 5 && mouseY < y + height - 5) {
                List<Component> tooltip = this.getLabelTooltip();
                if (tooltip.isEmpty()) {
                    return;
                }
                RenderSystem.disableScissor();
                Screen screen = Minecraft.getInstance().screen;
                RemovedGuiUtils.drawHoveringText(graphics, tooltip, mouseX, mouseY, screen.width, screen.height, 700, font);
                GlStateManager._enableScissorTest();
            }
        }

        public List<Component> getLabelTooltip() {
            return this.labelTooltip;
        }

        protected int getLabelWidth(int totalWidth) {
            return totalWidth;
        }

        @Override
        public Component getNarration() {
            return Components.immutableEmpty();
        }
    }
}
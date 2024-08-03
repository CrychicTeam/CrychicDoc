package com.simibubi.create.foundation.blockEntity.behaviour;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueHandler;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.lwjgl.glfw.GLFW;

public class ValueSettingsScreen extends AbstractSimiScreen {

    private int ticksOpen;

    private ValueSettingsBoard board;

    private int maxLabelWidth;

    private int valueBarWidth;

    private BlockPos pos;

    private ValueSettingsBehaviour.ValueSettings initialSettings;

    private ValueSettingsBehaviour.ValueSettings lastHovered = new ValueSettingsBehaviour.ValueSettings(-1, -1);

    private Consumer<ValueSettingsBehaviour.ValueSettings> onHover;

    private boolean iconMode;

    private int milestoneSize;

    private int soundCoolDown;

    public ValueSettingsScreen(BlockPos pos, ValueSettingsBoard board, ValueSettingsBehaviour.ValueSettings valueSettings, Consumer<ValueSettingsBehaviour.ValueSettings> onHover) {
        this.pos = pos;
        this.board = board;
        this.initialSettings = valueSettings;
        this.onHover = onHover;
        this.iconMode = board.formatter() instanceof ValueSettingsFormatter.ScrollOptionSettingsFormatter;
        this.milestoneSize = this.iconMode ? 8 : 4;
    }

    @Override
    protected void init() {
        int maxValue = this.board.maxValue();
        this.maxLabelWidth = 0;
        int milestoneCount = maxValue / this.board.milestoneInterval() + 1;
        int scale = maxValue > 128 ? 1 : 2;
        for (Component component : this.board.rows()) {
            this.maxLabelWidth = Math.max(this.maxLabelWidth, this.f_96547_.width(component));
        }
        if (this.iconMode) {
            this.maxLabelWidth = -18;
        }
        this.valueBarWidth = (maxValue + 1) * scale + 1 + milestoneCount * this.milestoneSize;
        int width = this.maxLabelWidth + 14 + this.valueBarWidth + 10;
        int height = this.board.rows().size() * 11;
        this.setWindowSize(width, height);
        super.init();
        Vec2 coordinateOfValue = this.getCoordinateOfValue(this.initialSettings.row(), this.initialSettings.value());
        this.setCursor(coordinateOfValue);
    }

    private void setCursor(Vec2 coordinateOfValue) {
        double guiScale = this.f_96541_.getWindow().getGuiScale();
        GLFW.glfwSetCursorPos(this.f_96541_.getWindow().getWindow(), (double) coordinateOfValue.x * guiScale, (double) coordinateOfValue.y * guiScale);
    }

    public ValueSettingsBehaviour.ValueSettings getClosestCoordinate(int mouseX, int mouseY) {
        int row = 0;
        int column = 0;
        boolean milestonesOnly = m_96638_();
        for (double bestDiff = Double.MAX_VALUE; row < this.board.rows().size(); row++) {
            Vec2 coord = this.getCoordinateOfValue(row, 0);
            double diff = (double) Math.abs(coord.y - (float) mouseY);
            if (bestDiff < diff) {
                break;
            }
            bestDiff = diff;
        }
        row--;
        for (double var13 = Double.MAX_VALUE; column <= this.board.maxValue(); column++) {
            Vec2 coord = this.getCoordinateOfValue(row, milestonesOnly ? column * this.board.milestoneInterval() : column);
            double diff = (double) Math.abs(coord.x - (float) mouseX);
            if (var13 < diff) {
                break;
            }
            var13 = diff;
        }
        column--;
        return new ValueSettingsBehaviour.ValueSettings(row, milestonesOnly ? Math.min(column * this.board.milestoneInterval(), this.board.maxValue()) : column);
    }

    public Vec2 getCoordinateOfValue(int row, int column) {
        int scale = this.board.maxValue() > 128 ? 1 : 2;
        float xOut = (float) (this.guiLeft + (Math.max(1, column) - 1) / this.board.milestoneInterval() * this.milestoneSize + column * scale) + 1.5F;
        xOut += (float) (this.maxLabelWidth + 14 + 4);
        if (column % this.board.milestoneInterval() == 0) {
            xOut += (float) (this.milestoneSize / 2);
        }
        if (column > 0) {
            xOut += (float) this.milestoneSize;
        }
        float yOut = (float) this.guiTop + ((float) row + 0.5F) * 11.0F - 0.5F;
        return new Vec2(xOut, yOut);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = this.guiLeft;
        int y = this.guiTop;
        int milestoneCount = this.board.maxValue() / this.board.milestoneInterval() + 1;
        int scale = this.board.maxValue() > 128 ? 1 : 2;
        Component title = this.board.title();
        Component tip = Lang.translateDirect("gui.value_settings.release_to_confirm", Components.keybind("key.use"));
        double fadeIn = Math.pow(Mth.clamp((double) ((float) this.ticksOpen + partialTicks) / 4.0, 0.0, 1.0), 1.0);
        int fattestLabel = Math.max(this.f_96547_.width(tip), this.f_96547_.width(title));
        if (this.iconMode) {
            for (int i = 0; i <= this.board.maxValue(); i++) {
                fattestLabel = Math.max(fattestLabel, this.f_96547_.width(this.board.formatter().format(new ValueSettingsBehaviour.ValueSettings(0, i))));
            }
        }
        int fatTipOffset = Math.max(0, fattestLabel + 10 - (this.windowWidth + 13)) / 2;
        int bgWidth = Math.max(this.windowWidth + 13, fattestLabel + 10);
        int fadeInWidth = (int) ((double) bgWidth * fadeIn);
        int fadeInStart = (bgWidth - fadeInWidth) / 2 - fatTipOffset;
        int additionalHeight = this.iconMode ? 46 : 33;
        int zLevel = 0;
        UIRenderHelper.drawStretched(graphics, x - 11 + fadeInStart, y - 17, fadeInWidth, this.windowHeight + additionalHeight, zLevel, AllGuiTextures.VALUE_SETTINGS_OUTER_BG);
        UIRenderHelper.drawStretched(graphics, x - 10 + fadeInStart, y - 18, fadeInWidth - 2, 1, zLevel, AllGuiTextures.VALUE_SETTINGS_OUTER_BG);
        UIRenderHelper.drawStretched(graphics, x - 10 + fadeInStart, y - 17 + this.windowHeight + additionalHeight, zLevel, fadeInWidth - 2, 1, AllGuiTextures.VALUE_SETTINGS_OUTER_BG);
        if (fadeInWidth > fattestLabel) {
            int textX = x - 11 - fatTipOffset + bgWidth / 2;
            graphics.drawString(this.f_96547_, title, textX - this.f_96547_.width(title) / 2, y - 14, 14540253, false);
            graphics.drawString(this.f_96547_, tip, textX - this.f_96547_.width(tip) / 2, y + this.windowHeight + additionalHeight - 27, 14540253, false);
        }
        this.renderBrassFrame(graphics, x + this.maxLabelWidth + 14, y - 3, this.valueBarWidth + 8, this.board.rows().size() * 11 + 5);
        UIRenderHelper.drawStretched(graphics, x + this.maxLabelWidth + 17, y, this.valueBarWidth + 2, this.board.rows().size() * 11 - 1, zLevel, AllGuiTextures.VALUE_SETTINGS_BAR_BG);
        int originalY = y;
        for (Component component : this.board.rows()) {
            int valueBarX = x + this.maxLabelWidth + 14 + 4;
            if (!this.iconMode) {
                UIRenderHelper.drawCropped(graphics, x - 4, y, this.maxLabelWidth + 8, 11, zLevel, AllGuiTextures.VALUE_SETTINGS_LABEL_BG);
                for (int w = 0; w < this.valueBarWidth; w += AllGuiTextures.VALUE_SETTINGS_BAR.width - 1) {
                    UIRenderHelper.drawCropped(graphics, valueBarX + w, y + 1, Math.min(AllGuiTextures.VALUE_SETTINGS_BAR.width - 1, this.valueBarWidth - w), 8, zLevel, AllGuiTextures.VALUE_SETTINGS_BAR);
                }
                graphics.drawString(this.f_96547_, component, x, y + 1, 4464640, false);
            }
            int milestoneX = valueBarX;
            for (int milestone = 0; milestone < milestoneCount; milestone++) {
                if (this.iconMode) {
                    AllGuiTextures.VALUE_SETTINGS_WIDE_MILESTONE.render(graphics, milestoneX, y + 1);
                } else {
                    AllGuiTextures.VALUE_SETTINGS_MILESTONE.render(graphics, milestoneX, y + 1);
                }
                milestoneX += this.milestoneSize + this.board.milestoneInterval() * scale;
            }
            y += 11;
        }
        if (!this.iconMode) {
            this.renderBrassFrame(graphics, x - 7, originalY - 3, this.maxLabelWidth + 14, this.board.rows().size() * 11 + 5);
        }
        if (this.ticksOpen >= 1) {
            ValueSettingsBehaviour.ValueSettings closest = this.getClosestCoordinate(mouseX, mouseY);
            if (!closest.equals(this.lastHovered)) {
                this.onHover.accept(closest);
                if (this.soundCoolDown == 0) {
                    float pitch = (float) closest.value() / (float) this.board.maxValue();
                    pitch = Mth.lerp(pitch, 1.15F, 1.5F);
                    this.f_96541_.getSoundManager().play(SimpleSoundInstance.forUI(AllSoundEvents.SCROLL_VALUE.getMainEvent(), pitch, 0.25F));
                    ScrollValueHandler.wrenchCog.bump(3, (double) (-(closest.value() - this.lastHovered.value()) * 10));
                    this.soundCoolDown = 1;
                }
            }
            this.lastHovered = closest;
            Vec2 coordinate = this.getCoordinateOfValue(closest.row(), closest.value());
            Component cursorText = this.board.formatter().format(closest);
            AllIcons cursorIcon = null;
            if (this.board.formatter() instanceof ValueSettingsFormatter.ScrollOptionSettingsFormatter sosf) {
                cursorIcon = sosf.getIcon(closest);
            }
            int cursorWidth = (cursorIcon != null ? 16 : this.f_96547_.width(cursorText)) / 2 * 2 + 3;
            int cursorX = (int) coordinate.x - cursorWidth / 2;
            int cursorY = (int) coordinate.y - 7;
            if (cursorIcon != null) {
                AllGuiTextures.VALUE_SETTINGS_CURSOR_ICON.render(graphics, cursorX - 2, cursorY - 3);
                RenderSystem.setShaderColor(0.265625F, 0.125F, 0.0F, 1.0F);
                cursorIcon.render(graphics, cursorX + 1, cursorY - 1);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                if (fadeInWidth > fattestLabel) {
                    graphics.drawString(this.f_96547_, cursorText, x - 11 - fatTipOffset + (bgWidth - this.f_96547_.width(cursorText)) / 2, originalY + this.windowHeight + additionalHeight - 40, 16505981, false);
                }
            } else {
                AllGuiTextures.VALUE_SETTINGS_CURSOR_LEFT.render(graphics, cursorX - 3, cursorY);
                UIRenderHelper.drawCropped(graphics, cursorX, cursorY, cursorWidth, 14, zLevel, AllGuiTextures.VALUE_SETTINGS_CURSOR);
                AllGuiTextures.VALUE_SETTINGS_CURSOR_RIGHT.render(graphics, cursorX + cursorWidth, cursorY);
                graphics.drawString(this.f_96547_, cursorText, cursorX + 2, cursorY + 3, 4464640, false);
            }
        }
    }

    protected void renderBrassFrame(GuiGraphics graphics, int x, int y, int w, int h) {
        AllGuiTextures.BRASS_FRAME_TL.render(graphics, x, y);
        AllGuiTextures.BRASS_FRAME_TR.render(graphics, x + w - 4, y);
        AllGuiTextures.BRASS_FRAME_BL.render(graphics, x, y + h - 4);
        AllGuiTextures.BRASS_FRAME_BR.render(graphics, x + w - 4, y + h - 4);
        int zLevel = 0;
        if (h > 8) {
            UIRenderHelper.drawStretched(graphics, x, y + 4, 3, h - 8, zLevel, AllGuiTextures.BRASS_FRAME_LEFT);
            UIRenderHelper.drawStretched(graphics, x + w - 3, y + 4, 3, h - 8, zLevel, AllGuiTextures.BRASS_FRAME_RIGHT);
        }
        if (w > 8) {
            UIRenderHelper.drawCropped(graphics, x + 4, y, w - 8, 3, zLevel, AllGuiTextures.BRASS_FRAME_TOP);
            UIRenderHelper.drawCropped(graphics, x + 4, y + h - 3, w - 8, 3, zLevel, AllGuiTextures.BRASS_FRAME_BOTTOM);
        }
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        int a = (int) (80.0F * Math.min(1.0F, ((float) this.ticksOpen + AnimationTickHolder.getPartialTicks()) / 20.0F)) << 24;
        graphics.fillGradient(0, 0, this.f_96543_, this.f_96544_, 1052688 | a, 1052688 | a);
    }

    @Override
    public void tick() {
        this.ticksOpen++;
        if (this.soundCoolDown > 0) {
            this.soundCoolDown--;
        }
        super.tick();
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        ValueSettingsBehaviour.ValueSettings closest = this.getClosestCoordinate((int) pMouseX, (int) pMouseY);
        int column = closest.value() + (int) Math.signum(pDelta) * (m_96638_() ? this.board.milestoneInterval() : 1);
        column = Mth.clamp(column, 0, this.board.maxValue());
        if (column == closest.value()) {
            return false;
        } else {
            this.setCursor(this.getCoordinateOfValue(closest.row(), column));
            return true;
        }
    }

    @Override
    public boolean keyReleased(int pKeyCode, int pScanCode, int pModifiers) {
        if (this.f_96541_.options.keyUse.matches(pKeyCode, pScanCode)) {
            Window window = this.f_96541_.getWindow();
            double x = this.f_96541_.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth();
            double y = this.f_96541_.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight();
            this.saveAndClose(x, y);
            return true;
        } else {
            return super.m_7920_(pKeyCode, pScanCode, pModifiers);
        }
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        if (this.f_96541_.options.keyUse.matchesMouse(pButton)) {
            this.saveAndClose(pMouseX, pMouseY);
            return true;
        } else {
            return super.m_6348_(pMouseX, pMouseY, pButton);
        }
    }

    protected void saveAndClose(double pMouseX, double pMouseY) {
        ValueSettingsBehaviour.ValueSettings closest = this.getClosestCoordinate((int) pMouseX, (int) pMouseY);
        AllPackets.getChannel().sendToServer(new ValueSettingsPacket(this.pos, closest.row(), closest.value(), null, Direction.UP, AllKeys.ctrlDown()));
        this.onClose();
    }

    @Override
    public void onClose() {
        super.m_7379_();
    }
}
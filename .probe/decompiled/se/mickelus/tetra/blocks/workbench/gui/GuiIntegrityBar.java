package se.mickelus.tetra.blocks.workbench.gui;

import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class GuiIntegrityBar extends GuiElement {

    private static final int segmentHeight = 2;

    private static final int segmentOffset = 6;

    private static final int gainColor = 587202559;

    private static final float gainOpacity = 0.15F;

    private static final int costColor = -1;

    private static final int overuseColor = -1996532395;

    private static final float overuseOpacity = 0.55F;

    private final GuiString label;

    private final List<Component> tooltip;

    private int segmentWidth = 8;

    private int integrityGain;

    private int integrityCost;

    public GuiIntegrityBar(int x, int y) {
        super(x, y, 0, 8);
        this.label = new GuiStringSmall(0, 0, "");
        this.label.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.label);
        this.setAttachmentPoint(GuiAttachment.topCenter);
        this.tooltip = Collections.singletonList(Component.translatable("tetra.stats.integrity_usage.tooltip"));
    }

    public void setItemStack(ItemStack itemStack, ItemStack previewStack) {
        boolean shouldShow = !itemStack.isEmpty() && itemStack.getItem() instanceof IModularItem;
        this.setVisible(shouldShow);
        if (shouldShow) {
            if (!previewStack.isEmpty()) {
                this.integrityGain = IModularItem.getIntegrityGain(previewStack);
                this.integrityCost = IModularItem.getIntegrityCost(previewStack);
            } else {
                this.integrityGain = IModularItem.getIntegrityGain(itemStack);
                this.integrityCost = IModularItem.getIntegrityCost(itemStack);
            }
            if (this.integrityGain - this.integrityCost < 0) {
                this.label.setString(ChatFormatting.RED + I18n.get("tetra.stats.integrity_usage", this.integrityCost, this.integrityGain));
            } else {
                this.label.setString(I18n.get("tetra.stats.integrity_usage", this.integrityCost, this.integrityGain));
            }
            if (this.integrityGain > 7) {
                this.segmentWidth = Math.max(64 / this.integrityGain - 1, 1);
            } else {
                this.segmentWidth = 8;
            }
            this.width = this.integrityGain * (this.segmentWidth + 1);
        }
    }

    public void showAnimation() {
        if (this.isVisible()) {
            new KeyframeAnimation(100, this).withDelay(200).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-3.0F, 0.0F, true)).start();
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : super.getTooltipLines();
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        for (int i = 0; i < this.integrityCost; i++) {
            if (i < this.integrityGain) {
                this.drawSegment(graphics, refX + this.x + i * (this.segmentWidth + 1), refY + this.y + 6, -1, opacity * this.getOpacity());
            } else {
                this.drawSegment(graphics, refX + this.x + i * (this.segmentWidth + 1), refY + this.y + 6, -1996532395, opacity * this.getOpacity());
            }
        }
        for (int ix = this.integrityCost; ix < this.integrityGain; ix++) {
            this.drawSegment(graphics, refX + this.x + ix * (this.segmentWidth + 1), refY + this.y + 6, 587202559, opacity * this.getOpacity());
        }
    }

    private void drawSegment(GuiGraphics graphics, int x, int y, int color, float opacity) {
        drawRect(graphics, x, y, x + this.segmentWidth, y + 2, color, opacity);
    }
}
package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.function.Consumer;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiItem;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.gui.GuiItemRolling;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.OutcomePreview;

@ParametersAreNonnullByDefault
public class HoloVariantItemGui extends GuiClickable {

    protected GuiItemRolling material;

    protected GuiTexture backdrop;

    protected OutcomePreview outcome;

    protected Consumer<OutcomePreview> onHover;

    protected Consumer<OutcomePreview> onBlur;

    protected boolean isMuted = false;

    public HoloVariantItemGui(int x, int y, int width, int height, OutcomePreview outcome, Consumer<OutcomePreview> onHover, Consumer<OutcomePreview> onBlur, Consumer<OutcomePreview> onSelect) {
        super(x, y, width, height, () -> onSelect.accept(outcome));
        this.outcome = outcome;
        this.onHover = onHover;
        this.onBlur = onBlur;
        this.material = new GuiItemRolling(-1, -1).setCountVisibility(GuiItem.CountMode.never).setItems(outcome.materials);
    }

    public HoloVariantItemGui(int x, int y, OutcomePreview outcome, @Nullable String label, Consumer<OutcomePreview> onHover, Consumer<OutcomePreview> onBlur, Consumer<OutcomePreview> onSelect) {
        this(x, y, 11, 11, outcome, onHover, onBlur, onSelect);
        this.backdrop = new GuiTexture(0, 0, 11, 11, 68, 0, GuiTextures.workbench);
        this.addChild(this.backdrop);
        if (label != null) {
            GuiString labelElement = new GuiStringOutline(1, 1, label);
            labelElement.setColor(outcome.glyph.tint);
            labelElement.setAttachment(GuiAttachment.middleCenter);
            this.addChild(labelElement);
        } else {
            this.addChild(new GuiModuleGlyph(2, 2, 8, 8, outcome.glyph).setShift(false));
        }
    }

    public void updateSelection(OutcomePreview outcome) {
        this.isMuted = outcome != null && !this.outcome.equals(outcome);
        this.backdrop.setColor(this.isMuted ? 8355711 : 16777215);
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        this.onHover.accept(this.outcome);
        this.backdrop.setColor(16777164);
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        this.onBlur.accept(this.outcome);
        this.backdrop.setColor(this.isMuted ? 8355711 : 16777215);
    }

    @Override
    protected void drawChildren(GuiGraphics guiGraphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.drawChildren(guiGraphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (Screen.hasShiftDown()) {
            this.material.draw(guiGraphics, refX + this.material.getX(), refY + this.material.getY(), screenWidth, screenHeight, mouseX, mouseY, opacity);
        }
    }
}
package se.mickelus.tetra.blocks.workbench.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTextureOffset;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.VariantData;

@ParametersAreNonnullByDefault
public class GuiModule extends GuiClickable {

    protected String slotKey = null;

    protected GuiModuleBackdrop backdrop;

    protected GuiString moduleString;

    protected GuiModuleGlyph glyph;

    protected GuiTextureOffset tweakingIndicator;

    protected boolean isEmpty;

    protected boolean isHovered;

    protected boolean isUnselected;

    protected boolean isSelected;

    protected boolean isPreview;

    protected boolean isRemoving;

    protected boolean isAdding;

    protected BiConsumer<String, String> hoverHandler;

    public GuiModule(int x, int y, GuiAttachment attachmentPoint, ItemStack itemStack, ItemStack previewStack, String slotKey, String slotName, ItemModule module, ItemModule previewModule, Consumer<String> slotClickHandler, BiConsumer<String, String> hoverHandler) {
        super(x, y, 0, 11, () -> slotClickHandler.accept(slotKey));
        this.slotKey = slotKey;
        this.setAttachmentPoint(attachmentPoint);
        if (module == null && previewModule == null) {
            this.isEmpty = true;
            this.setupChildren(null, null, slotName, false);
        } else if (previewModule == null) {
            this.isRemoving = true;
            VariantData data = module.getVariantData(itemStack);
            this.setupChildren(null, data.glyph, slotName, module.isTweakable(itemStack));
        } else if (module == null) {
            this.isAdding = true;
            VariantData previewData = previewModule.getVariantData(previewStack);
            this.setupChildren(previewModule.getName(previewStack), previewData.glyph, slotName, previewModule.isTweakable(itemStack));
        } else {
            VariantData data = module.getVariantData(itemStack);
            VariantData previewData = previewModule.getVariantData(previewStack);
            if (data.equals(previewData)) {
                this.setupChildren(module.getName(itemStack), data.glyph, slotName, module.isTweakable(itemStack));
            } else {
                this.isPreview = true;
                this.setupChildren(previewModule.getName(previewStack), previewData.glyph, slotName, previewModule.isTweakable(itemStack));
            }
        }
        this.hoverHandler = hoverHandler;
    }

    public void showAnimation(int offset) {
        if (this.isVisible()) {
            int direction = this.attachmentPoint == GuiAttachment.topLeft ? -2 : 2;
            new KeyframeAnimation(100, this.backdrop).withDelay(offset * 80).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) direction, 0.0F, true)).start();
            if (this.glyph != null) {
                new KeyframeAnimation(100, this.glyph).withDelay(offset * 80 + 100).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();
            }
            if (this.tweakingIndicator != null) {
                new KeyframeAnimation(100, this.tweakingIndicator).withDelay(offset * 80 + 100).applyTo(new Applier.Opacity(0.0F, 1.0F)).start();
            }
            new KeyframeAnimation(100, this.moduleString).withDelay(offset * 80 + 200).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (direction * 2), 0.0F, true)).start();
        }
    }

    protected void setupChildren(String moduleName, GlyphData glyphData, String slotName, boolean tweakable) {
        this.backdrop = new GuiModuleMinorBackdrop(1, -1, 16777215);
        if (GuiAttachment.topLeft.equals(this.attachmentPoint)) {
            this.backdrop.setX(-1);
        }
        this.backdrop.setAttachment(this.attachmentPoint);
        this.addChild(this.backdrop);
        if (tweakable) {
            this.tweakingIndicator = new GuiTextureOffset(1, -1, 11, 11, 192, 32, GuiTextures.workbench);
            if (GuiAttachment.topLeft.equals(this.attachmentPoint)) {
                this.tweakingIndicator.setX(-1);
            }
            this.tweakingIndicator.setAttachment(this.attachmentPoint);
            this.addChild(this.tweakingIndicator);
        }
        this.moduleString = new GuiString(-12, 1, moduleName != null ? moduleName : slotName);
        if (GuiAttachment.topLeft.equals(this.attachmentPoint)) {
            this.moduleString.setX(12);
        }
        this.moduleString.setAttachment(this.attachmentPoint);
        this.addChild(this.moduleString);
        this.width = this.moduleString.getWidth() + 12;
        if (glyphData != null) {
            this.glyph = new GuiModuleGlyph(0, 1, 8, 8, glyphData.tint, glyphData.textureX, glyphData.textureY, glyphData.textureLocation);
            if (GuiAttachment.topLeft.equals(this.attachmentPoint)) {
                this.glyph.setX(1);
            }
            this.glyph.setAttachment(this.attachmentPoint);
            this.addChild(this.glyph);
        }
    }

    public void updateSelectedHighlight(String selectedSlot) {
        this.isUnselected = selectedSlot != null && !this.slotKey.equals(selectedSlot);
        this.isSelected = selectedSlot != null && this.slotKey.equals(selectedSlot);
        this.updateColors();
    }

    private void updateColors() {
        if (this.isPreview) {
            this.setColor(11184895);
        } else if (this.isAdding) {
            this.setColor(11206570);
        } else if (this.isRemoving) {
            this.setColor(16755370);
        } else if (this.isHovered && this.isEmpty) {
            this.setColor(9408367);
        } else if (this.isHovered) {
            this.setColor(16777164);
        } else if (this.isSelected) {
            this.setColor(16777215);
        } else if (!this.isEmpty && !this.isUnselected) {
            this.setColor(16777215);
        } else {
            this.setColor(8355711);
        }
    }

    protected void setColor(int color) {
        this.backdrop.setColor(color);
        this.moduleString.setColor(color);
    }

    @Override
    protected void onFocus() {
        this.isHovered = true;
        this.updateColors();
        this.hoverHandler.accept(this.slotKey, null);
    }

    @Override
    protected void onBlur() {
        this.isHovered = false;
        this.updateColors();
        this.hoverHandler.accept(null, null);
    }
}
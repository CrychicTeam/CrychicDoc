package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.GuiAnimation;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class HoloItemGui extends GuiClickable {

    private final GuiTexture backdrop;

    private final GuiTexture icon;

    private final List<GuiAnimation> selectAnimations;

    private final List<GuiAnimation> deselectAnimations;

    private final List<GuiAnimation> hoverAnimations;

    private final List<GuiAnimation> blurAnimations;

    private final KeyframeAnimation itemShow;

    private final KeyframeAnimation itemHide;

    private final IModularItem item;

    GuiElement slotGroup;

    private boolean isSelected = false;

    public HoloItemGui(int x, int y, IModularItem item, int textureIndex, Runnable onSelect, Consumer<String> onSlotSelect) {
        this(x, y, item, item.getDefaultStack(), textureIndex, onSelect, onSlotSelect);
    }

    public HoloItemGui(int x, int y, IModularItem item, ItemStack itemStack, int textureIndex, Runnable onSelect, Consumer<String> onSlotSelect) {
        super(x, y, 64, 64, onSelect);
        this.selectAnimations = new ArrayList();
        this.deselectAnimations = new ArrayList();
        this.hoverAnimations = new ArrayList();
        this.blurAnimations = new ArrayList();
        this.backdrop = new GuiTexture(0, 0, 48, 48, GuiTextures.workbench);
        this.backdrop.setAttachment(GuiAttachment.middleCenter);
        this.addChild(this.backdrop);
        this.icon = new GuiTexture(0, 0, 38, 38, 38 * (textureIndex % 6), 218 - 38 * (textureIndex / 6), GuiTextures.holo);
        this.icon.setAttachment(GuiAttachment.middleCenter);
        this.addChild(this.icon);
        GuiElement labelGroup = new GuiElement(0, 0, 0, 0);
        String[] labelStrings = I18n.get("tetra.holo.craft." + item.getItem().toString()).split(" ");
        for (int i = 0; i < labelStrings.length; i++) {
            GuiString labelLine = new GuiStringOutline(0, i * 10, labelStrings[i]);
            labelLine.setAttachment(GuiAttachment.topCenter);
            labelLine.setColor(16777164);
            labelGroup.addChild(labelLine);
        }
        labelGroup.setAttachment(GuiAttachment.middleCenter);
        labelGroup.setHeight(10 * labelStrings.length);
        labelGroup.setOpacity(0.0F);
        this.addChild(labelGroup);
        this.slotGroup = new GuiElement(37, 15, 0, 0);
        this.setupSlots(item, itemStack, onSlotSelect);
        this.slotGroup.setVisible(false);
        this.addChild(this.slotGroup);
        this.selectAnimations.add(new KeyframeAnimation(80, this.slotGroup).applyTo(new Applier.Opacity(1.0F)));
        this.deselectAnimations.add(new KeyframeAnimation(80, this.slotGroup).applyTo(new Applier.Opacity(0.0F)).onStop(complete -> {
            if (complete) {
                this.slotGroup.setVisible(false);
            }
        }));
        this.selectAnimations.add(new KeyframeAnimation(80, this).applyTo(new Applier.TranslateX(0.0F), new Applier.TranslateY(0.0F)));
        this.deselectAnimations.add(new KeyframeAnimation(80, this).applyTo(new Applier.TranslateX((float) x), new Applier.TranslateY((float) y)));
        this.itemShow = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(1.0F));
        this.itemHide = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(0.0F)).onStop(complete -> this.isVisible = false);
        this.hoverAnimations.add(new KeyframeAnimation(80, labelGroup).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY(-2.0F, 0.0F)));
        this.blurAnimations.add(new KeyframeAnimation(120, labelGroup).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY(0.0F, 2.0F)));
        this.item = item;
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        for (int i = this.elements.size() - 1; i >= 0; i--) {
            if (((GuiElement) this.elements.get(i)).isVisible() && ((GuiElement) this.elements.get(i)).onMouseClick(x, y, button)) {
                return true;
            }
        }
        return super.onMouseClick(x, y, button);
    }

    public void onItemSelected(IModularItem item) {
        if (this.item.equals(item)) {
            this.setVisible(true);
            this.setSelected(true);
        } else if (item == null) {
            this.setVisible(true);
            this.setSelected(false);
        } else {
            this.setVisible(false);
            this.setSelected(false);
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            this.deselectAnimations.forEach(GuiAnimation::stop);
            this.slotGroup.setVisible(true);
            this.selectAnimations.forEach(GuiAnimation::start);
            this.icon.setColor(16777215);
            this.hoverAnimations.forEach(GuiAnimation::stop);
            this.blurAnimations.forEach(GuiAnimation::start);
        } else {
            this.selectAnimations.forEach(GuiAnimation::stop);
            this.deselectAnimations.forEach(GuiAnimation::start);
        }
        this.backdrop.setColor(16777215);
        this.isSelected = selected;
    }

    @Override
    protected void onShow() {
        super.onShow();
        this.itemHide.stop();
        this.itemShow.start();
    }

    @Override
    protected boolean onHide() {
        super.onHide();
        this.itemShow.stop();
        this.itemHide.start();
        return false;
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        this.elements.stream().filter(GuiElement::isVisible).forEach(element -> element.updateFocusState(refX + this.x + getXOffset(this, element.getAttachmentAnchor()) - getXOffset(element, element.getAttachmentPoint()), refY + this.y + getYOffset(this, element.getAttachmentAnchor()) - getYOffset(element, element.getAttachmentPoint()), mouseX, mouseY));
        int offsetMouseX = mouseX - refX - this.x;
        int offsetMouseY = mouseY - refY - this.y;
        boolean gainFocus = offsetMouseX + offsetMouseY >= 44;
        if (offsetMouseX + offsetMouseY > 84) {
            gainFocus = false;
        }
        if (offsetMouseX - offsetMouseY > 16) {
            gainFocus = false;
        }
        if (offsetMouseY - offsetMouseX > 19) {
            gainFocus = false;
        }
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
    }

    @Override
    protected void onFocus() {
        if (!this.isSelected) {
            this.backdrop.setColor(16777164);
            this.icon.setColor(8355711);
            this.blurAnimations.forEach(GuiAnimation::stop);
            this.hoverAnimations.forEach(GuiAnimation::start);
        }
    }

    @Override
    protected void onBlur() {
        this.backdrop.setColor(16777215);
        this.icon.setColor(16777215);
        this.hoverAnimations.forEach(GuiAnimation::stop);
        this.blurAnimations.forEach(GuiAnimation::start);
    }

    private void setupSlots(IModularItem item, ItemStack itemStack, Consumer<String> onSlotSelect) {
        String[] majorModuleNames = item.getMajorModuleNames(itemStack);
        String[] majorModuleKeys = item.getMajorModuleKeys(itemStack);
        GuiModuleOffsets majorOffsets = item.getMajorGuiOffsets(itemStack);
        String[] minorModuleNames = item.getMinorModuleNames(itemStack);
        String[] minorModuleKeys = item.getMinorModuleKeys(itemStack);
        GuiModuleOffsets minorOffsets = item.getMinorGuiOffsets(itemStack);
        for (int i = 0; i < majorModuleNames.length; i++) {
            int x = majorOffsets.getX(i);
            GuiAttachment attachment = x > 0 ? GuiAttachment.topLeft : GuiAttachment.topRight;
            this.slotGroup.addChild(new HoloSlotMajorGui(x, majorOffsets.getY(i), attachment, majorModuleKeys[i], majorModuleNames[i], onSlotSelect));
        }
        for (int i = 0; i < minorModuleNames.length; i++) {
            int x = minorOffsets.getX(i);
            GuiAttachment attachment = x > 0 ? GuiAttachment.topLeft : GuiAttachment.topRight;
            this.slotGroup.addChild(new HoloSlotGui(x, minorOffsets.getY(i), attachment, minorModuleKeys[i], minorModuleNames[i], onSlotSelect));
        }
    }
}
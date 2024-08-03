package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiVerticalLayoutGroup;
import se.mickelus.tetra.gui.GuiKeybinding;
import se.mickelus.tetra.gui.ZOffsetGui;
import se.mickelus.tetra.gui.stats.sorting.IStatSorter;

@ParametersAreNonnullByDefault
public class HoloSortPopover extends ZOffsetGui {

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final Consumer<IStatSorter> onSelect;

    private final GuiVerticalLayoutGroup items;

    private final GuiElement backdrop = new GuiRect(0, 0, 0, 0, 0).setOpacity(0.9F);

    public HoloSortPopover(int x, int y, Consumer<IStatSorter> onSelect) {
        super(x, y - 3, 200.0);
        this.addChild(this.backdrop);
        this.addChild(new GuiRect(1, 1, 6, 1, 16777215));
        this.addChild(new GuiRect(-1, 1, 6, 1, 16777215).setAttachment(GuiAttachment.topRight));
        this.addChild(new GuiRect(-1, -1, 6, 1, 16777215).setAttachment(GuiAttachment.bottomRight));
        this.addChild(new GuiRect(1, -1, 6, 1, 16777215).setAttachment(GuiAttachment.bottomLeft));
        this.items = new GuiVerticalLayoutGroup(6, 6, 0, 3);
        this.addChild(this.items);
        this.onSelect = onSelect;
        this.isVisible = false;
        this.showAnimation = new KeyframeAnimation(150, this).applyTo(new Applier.TranslateY((float) y), new Applier.Opacity(1.0F));
        this.hideAnimation = new KeyframeAnimation(100, this).applyTo(new Applier.TranslateY((float) (y - 3)), new Applier.Opacity(0.0F)).onStop(complete -> {
            if (complete) {
                this.isVisible = false;
            }
        });
    }

    public void update(IStatSorter[] sorters) {
        this.items.clearChildren();
        int maxWidth = 0;
        for (int i = 0; i < sorters.length; i++) {
            HoloSortPopover.Item item = new HoloSortPopover.Item(0, 0, i, sorters[i], this::onSelect);
            this.items.addChild(item);
            if (item.getWidth() > maxWidth) {
                maxWidth = item.getWidth();
            }
        }
        this.items.forceLayout();
        int constMaxWidth = maxWidth;
        this.items.getChildren().forEach(child -> child.setWidth(constMaxWidth));
        this.setHeight(this.items.getHeight() + 12);
        this.setWidth(maxWidth + 12);
        this.backdrop.setHeight(this.getHeight());
        this.backdrop.setWidth(this.getWidth());
    }

    public void onSelect(IStatSorter sorter) {
        this.onSelect.accept(sorter);
        this.setVisible(false);
    }

    @Override
    protected void onShow() {
        if (!this.showAnimation.isActive()) {
            this.showAnimation.start();
        }
        this.hideAnimation.stop();
        this.items.getChildren(HoloSortPopover.Item.class).forEach(HoloSortPopover.Item::resetKeybind);
    }

    @Override
    protected boolean onHide() {
        if (!this.hideAnimation.isActive()) {
            this.hideAnimation.start();
        }
        this.showAnimation.stop();
        return false;
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.setVisible(false);
            return true;
        } else {
            return super.onKeyPress(keyCode, scanCode, modifiers);
        }
    }

    static class Item extends GuiClickable {

        GuiString label;

        GuiElement keybinding;

        KeyframeAnimation showKeybind;

        KeyframeAnimation hideKeybind;

        int index;

        public Item(int x, int y, int index, IStatSorter statSorter, Consumer<IStatSorter> onClickHandler) {
            super(x, y, 40, 10, () -> onClickHandler.accept(statSorter));
            this.label = new GuiString(0, 0, statSorter.getName());
            this.addChild(this.label);
            this.setWidth(this.label.getWidth());
            this.index = index;
            if (index < 9) {
                GuiKeybinding inner = new GuiKeybinding(1, 1, index + 1 + "");
                this.keybinding = new GuiElement(-10, -2, inner.getWidth() + 2, inner.getHeight() + 2);
                this.keybinding.addChild(new GuiRect(0, 0, this.keybinding.getWidth(), this.keybinding.getHeight(), 0).setOpacity(0.9F));
                this.keybinding.addChild(inner);
                this.keybinding.setOpacity(0.0F);
                this.keybinding.setAttachmentPoint(GuiAttachment.topRight);
                this.addChild(this.keybinding);
                this.showKeybind = new KeyframeAnimation(150, this.keybinding).applyTo(new Applier.TranslateX(-7.0F), new Applier.Opacity(1.0F)).withDelay(index * 60);
                this.hideKeybind = new KeyframeAnimation(100, this.keybinding).applyTo(new Applier.TranslateX(-10.0F), new Applier.Opacity(0.0F));
            }
        }

        public void resetKeybind() {
            if (this.keybinding != null) {
                this.showKeybind.stop();
                this.hideKeybind.stop();
                this.keybinding.setOpacity(0.0F);
            }
        }

        @Override
        protected void onFocus() {
            this.label.setColor(16777164);
            super.onFocus();
        }

        @Override
        protected void onBlur() {
            this.label.setColor(16777215);
            super.onBlur();
        }

        @Override
        public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
            if ((keyCode == 340 || keyCode == 344) && this.keybinding != null) {
                if (this.hideKeybind.isActive()) {
                    this.hideKeybind.stop();
                }
                if (!this.showKeybind.isActive()) {
                    this.showKeybind.start();
                }
            }
            return false;
        }

        @Override
        public boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
            if ((keyCode == 340 || keyCode == 344) && this.keybinding != null) {
                if (this.showKeybind.isActive()) {
                    this.showKeybind.stop();
                }
                if (!this.hideKeybind.isActive()) {
                    this.hideKeybind.start();
                }
            }
            if (Character.getNumericValue(keyCode) == this.index + 1) {
                this.onClickHandler.run();
                return true;
            } else {
                return false;
            }
        }
    }
}
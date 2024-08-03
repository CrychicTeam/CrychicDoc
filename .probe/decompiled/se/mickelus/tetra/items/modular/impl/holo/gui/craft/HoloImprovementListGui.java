package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.mutil.gui.impl.GuiHorizontalScrollable;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloImprovementListGui extends GuiElement {

    private final List<HoloImprovementGui> improvements;

    private final GuiHorizontalScrollable container;

    private final GuiHorizontalLayoutGroup[] groups;

    private final Consumer<OutcomePreview> onVariantHover;

    private final Consumer<OutcomePreview> onVariantBlur;

    private final Consumer<OutcomeStack> onVariantSelect;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final int originalY;

    public HoloImprovementListGui(int x, int y, int width, int height, Consumer<OutcomePreview> onVariantHover, Consumer<OutcomePreview> onVariantBlur, Consumer<OutcomeStack> onVariantSelect) {
        super(x, y, width, height);
        this.originalY = y;
        this.improvements = new ArrayList();
        this.container = new GuiHorizontalScrollable(0, 0, width, height).setGlobal(true);
        this.addChild(this.container);
        this.groups = new GuiHorizontalLayoutGroup[3];
        for (int i = 0; i < this.groups.length; i++) {
            this.groups[i] = new GuiHorizontalLayoutGroup(0, i * 32, 32, 8);
            this.container.addChild(this.groups[i]);
        }
        this.showAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY((float) y)).withDelay(100);
        this.hideAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY((float) (y - 5))).onStop(complete -> {
            if (complete) {
                this.isVisible = false;
            }
        });
        this.onVariantHover = onVariantHover;
        this.onVariantBlur = onVariantBlur;
        this.onVariantSelect = onVariantSelect;
    }

    public void updateSchematics(ItemStack baseStack, String slot, UpgradeSchematic[] schematics) {
        this.improvements.clear();
        for (GuiElement group : this.groups) {
            group.clearChildren();
            group.setWidth(0);
        }
        for (int i = 0; i < schematics.length; i++) {
            HoloImprovementGui improvement = new HoloImprovementGui(0, 0, schematics[i], baseStack, slot, this.onVariantHover, this.onVariantBlur, this.onVariantSelect);
            this.improvements.add(improvement);
            GuiHorizontalLayoutGroup group = this.getNextGroup();
            group.addChild(improvement);
            group.forceLayout();
        }
        this.container.markDirty();
    }

    private GuiHorizontalLayoutGroup getNextGroup() {
        GuiHorizontalLayoutGroup next = this.groups[0];
        int width = next.getWidth();
        for (int i = 1; i < this.groups.length; i++) {
            if (this.groups[i].getWidth() < width) {
                next = this.groups[i];
                width = next.getWidth();
            }
        }
        return next;
    }

    public void show() {
        this.hideAnimation.stop();
        this.setVisible(true);
        this.showAnimation.start();
        this.container.setOffset(0.0);
    }

    public void hide() {
        this.showAnimation.stop();
        this.hideAnimation.start();
    }

    public void forceHide() {
        this.setY(this.originalY);
        this.setOpacity(0.0F);
        this.setVisible(false);
    }

    public void updateSelection(ItemStack itemStack, List<OutcomeStack> selectedOutcomes) {
        this.improvements.forEach(improvement -> improvement.updateSelection(itemStack, selectedOutcomes));
    }
}
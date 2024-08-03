package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.gui.stats.sorting.IStatSorter;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloSchematicGui extends GuiElement {

    private final GuiElement listGroup;

    private final Consumer<OutcomePreview> onVariantOpen;

    private final HoloDescription description;

    private final HoloMaterialTranslation translation;

    private final HoloSortButton sortbutton;

    private final HoloFilterButton filterButton;

    private final HoloVariantListGui list;

    private final HoloVariantDetailGui detail;

    private final KeyframeAnimation showListAnimation;

    private final KeyframeAnimation hideListAnimation;

    String slot;

    private OutcomePreview openVariant;

    private OutcomePreview selectedVariant;

    private OutcomePreview hoveredVariant;

    public HoloSchematicGui(int x, int y, int width, int height, Consumer<OutcomePreview> onVariantOpen) {
        super(x, y, width, height);
        this.onVariantOpen = onVariantOpen;
        this.listGroup = new GuiElement(0, 0, width, 64);
        this.addChild(this.listGroup);
        this.list = new HoloVariantListGui(0, 14, width, this::onVariantHover, this::onVariantBlur, this::onVariantSelect);
        this.listGroup.addChild(this.list);
        GuiHorizontalLayoutGroup buttons = new GuiHorizontalLayoutGroup(0, 0, 11, 6);
        this.listGroup.addChild(buttons);
        this.description = new HoloDescription(0, 0);
        buttons.addChild(this.description);
        this.translation = new HoloMaterialTranslation(0, 0);
        buttons.addChild(this.translation);
        this.sortbutton = new HoloSortButton(0, 0, this::onSortChange);
        buttons.addChild(this.sortbutton);
        this.filterButton = new HoloFilterButton(0, 0, this::onFilterChange);
        buttons.addChild(this.filterButton);
        this.detail = new HoloVariantDetailGui(0, 68, width, onVariantOpen);
        this.addChild(this.detail);
        this.showListAnimation = new KeyframeAnimation(60, this.listGroup).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY(0.0F)).withDelay(100);
        this.hideListAnimation = new KeyframeAnimation(100, this.listGroup).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY(-50.0F)).onStop(complete -> {
            if (complete) {
                this.list.setVisible(false);
            }
        });
    }

    public void update(IModularItem item, String slot, UpgradeSchematic schematic) {
        OutcomePreview[] previews = schematic.getPreviews(new ItemStack(item.getItem()), slot);
        this.list.update(previews);
        this.slot = slot;
        this.selectedVariant = null;
        this.hoveredVariant = null;
        this.detail.updateVariant(null, null, slot);
        this.filterButton.reset();
        this.sortbutton.update(previews);
        this.translation.update(schematic);
        this.description.update(previews);
    }

    public void openVariant(OutcomePreview variant) {
        this.openVariant = variant;
        if (variant != null) {
            this.showListAnimation.stop();
            this.hideListAnimation.start();
            this.detail.showImprovements();
        } else {
            this.hideListAnimation.stop();
            this.showListAnimation.start();
            this.list.setVisible(true);
            this.detail.hideImprovements();
        }
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (this.selectedVariant != null && this.openVariant == null && keyCode == 32) {
            this.onVariantOpen.accept(this.selectedVariant);
            return true;
        } else {
            return super.onKeyPress(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        return !this.sortbutton.isBlockingFocus() ? super.getTooltipLines() : null;
    }

    private void onVariantHover(OutcomePreview outcome) {
        if (!this.sortbutton.isBlockingFocus() && this.openVariant == null) {
            this.hoveredVariant = outcome;
            this.detail.updateVariant(this.selectedVariant, this.hoveredVariant, this.slot);
        }
    }

    private void onVariantBlur(OutcomePreview outcome) {
        if (outcome.equals(this.hoveredVariant) && this.openVariant == null) {
            this.detail.updateVariant(this.selectedVariant, null, this.slot);
        }
    }

    private void onVariantSelect(OutcomePreview outcome) {
        this.selectedVariant = outcome;
        this.list.updateSelection(outcome);
        this.detail.updateVariant(this.selectedVariant, this.hoveredVariant, this.slot);
    }

    private void onFilterChange(String filter) {
        if (this.hoveredVariant != null) {
            this.detail.updateVariant(this.selectedVariant, null, this.slot);
        }
        this.list.updateFilter(filter);
    }

    private void onSortChange(IStatSorter sorter) {
        if (this.hoveredVariant != null) {
            this.detail.updateVariant(this.selectedVariant, null, this.slot);
        }
        this.list.changeSorting(sorter);
    }

    public void animateOpen() {
        this.list.onShow();
        if (this.selectedVariant != null) {
            this.detail.animateOpen();
        }
    }

    @Override
    protected void onShow() {
        this.list.setVisible(true);
    }

    @Override
    protected boolean onHide() {
        this.list.setVisible(false);
        this.detail.forceHide();
        this.listGroup.setY(0);
        this.listGroup.setOpacity(1.0F);
        return true;
    }
}
package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloImprovementGui extends GuiElement {

    private final GuiHorizontalLayoutGroup header;

    private final GuiTexture backdrop;

    private final GuiTexture plus;

    private final GuiString label;

    private final HoloDescription description;

    private final HoloMaterialTranslation translation;

    private final GuiElement variants;

    private final Consumer<OutcomeStack> onVariantSelect;

    private final UpgradeSchematic schematic;

    private final String slot;

    private final Consumer<OutcomePreview> onVariantHover;

    private final Consumer<OutcomePreview> onVariantBlur;

    private boolean isActive;

    private OutcomePreview preview;

    public HoloImprovementGui(int x, int y, UpgradeSchematic schematic, ItemStack baseStack, String slot, Consumer<OutcomePreview> onVariantHover, Consumer<OutcomePreview> onVariantBlur, Consumer<OutcomeStack> onVariantSelect) {
        super(x, y, 52, 16);
        this.onVariantHover = onVariantHover;
        this.onVariantBlur = onVariantBlur;
        this.onVariantSelect = onVariantSelect;
        OutcomePreview[] previews = schematic.getPreviews(baseStack, slot);
        this.backdrop = new GuiTexture(1, 5, 16, 9, 52, 3, GuiTextures.workbench);
        this.addChild(this.backdrop);
        this.addChild(new GuiModuleGlyph(0, 2, 16, 16, schematic.getGlyph()).setShift(false));
        this.plus = new GuiTexture(7, 10, 7, 7, 68, 16, GuiTextures.workbench);
        this.plus.setColor(8355711);
        this.addChild(this.plus);
        this.header = new GuiHorizontalLayoutGroup(24, 0, 0, 4);
        this.addChild(this.header);
        this.label = new GuiString(0, 0, schematic.getName());
        this.header.addChild(this.label);
        this.description = new HoloDescription(0, 0);
        this.description.update(schematic, baseStack);
        this.header.addChild(this.description);
        this.translation = new HoloMaterialTranslation(0, 0);
        this.translation.update(schematic);
        this.header.addChild(this.translation);
        this.variants = new GuiElement(24, 11, 0, 11);
        this.addChild(this.variants);
        this.schematic = schematic;
        this.slot = slot;
        this.header.forceLayout();
        this.updateVariants(previews, Collections.emptyList());
    }

    public void updateVariants(OutcomePreview[] previews, List<OutcomeStack> selectedOutcomes) {
        this.variants.clearChildren();
        List<OutcomePreview> matchingSelections = (List<OutcomePreview>) selectedOutcomes.stream().filter(stack -> stack.schematicEquals(this.schematic)).map(stack -> stack.preview).collect(Collectors.toList());
        this.isActive = !matchingSelections.isEmpty();
        this.preview = null;
        if (previews.length > 1) {
            int labelStart = this.findLabelStart(previews);
            for (int i = 0; i < previews.length; i++) {
                String labelString = previews[i].variantName;
                boolean isConnected = i + 1 < previews.length && Objects.equals(previews[i].variantKey, previews[i + 1].variantKey);
                HoloImprovementVariantGui variant = new HoloImprovementVariantGui(i * 28, 0, labelString, labelStart, previews[i], isConnected, this.onVariantHover, this.onVariantBlur, preview -> this.onVariantSelect.accept(new OutcomeStack(this.schematic, preview)));
                this.variants.addChild(variant);
                if (!matchingSelections.isEmpty()) {
                    variant.setMuted(!matchingSelections.contains(previews[i]));
                }
                this.backdrop.setColor(16777215);
                this.label.setColor(16777215);
                this.variants.setVisible(true);
            }
            this.header.setY(0);
            this.setWidth(Math.max(this.variants.getX() + previews.length * 28 - 9, this.header.getWidth()));
            this.updateTint(false);
        } else {
            this.header.setY(6);
            this.variants.setVisible(false);
            if (previews.length == 1) {
                this.preview = previews[0];
            }
            this.setWidth(this.header.getWidth() + 24);
            this.updateTint(this.hasFocus());
        }
    }

    public void updateSelection(ItemStack itemStack, List<OutcomeStack> selectedOutcomes) {
        OutcomePreview[] previews = this.schematic.getPreviews(itemStack, this.slot);
        this.updateVariants(previews, selectedOutcomes);
    }

    private int findLabelStart(OutcomePreview[] previews) {
        int maxLength = Arrays.stream(previews).map(preview -> preview.variantName).filter(Objects::nonNull).mapToInt(String::length).max().orElse(0);
        Character cmp = null;
        for (int j = 0; j < maxLength; j++) {
            for (int i = 0; i < previews.length; i++) {
                String name = previews[i].variantName;
                if (name != null && name.length() > j) {
                    if (cmp == null) {
                        cmp = name.charAt(j);
                    } else if (!cmp.equals(name.charAt(j))) {
                        return j;
                    }
                }
            }
            cmp = null;
        }
        return 0;
    }

    protected void updateTint(boolean hasFocus) {
        if (this.isActive) {
            this.backdrop.setColor(9408367);
            this.label.setColor(16777164);
            this.plus.setColor(16777164);
        } else {
            this.backdrop.setColor(16777215);
            this.label.setColor(16777215);
            this.plus.setColor(8355711);
        }
        if (hasFocus) {
            this.backdrop.setColor(16777164);
            this.label.setColor(16777164);
        }
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        if (!this.variants.isVisible()) {
            this.updateTint(true);
            if (this.preview != null) {
                this.onVariantHover.accept(this.preview);
            }
        }
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        if (!this.variants.isVisible()) {
            this.updateTint(false);
            if (this.preview != null) {
                this.onVariantBlur.accept(this.preview);
            }
        }
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        if (this.hasFocus() && this.preview != null) {
            boolean wasActive = this.isActive;
            this.onVariantSelect.accept(new OutcomeStack(this.schematic, this.preview));
            if (wasActive) {
                this.onVariantHover.accept(this.preview);
            }
            return true;
        } else {
            return super.onMouseClick(x, y, button);
        }
    }
}
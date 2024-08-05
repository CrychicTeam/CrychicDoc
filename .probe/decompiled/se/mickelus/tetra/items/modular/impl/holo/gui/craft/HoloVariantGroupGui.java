package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.stats.sorting.IStatSorter;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.SchematicType;

@ParametersAreNonnullByDefault
public class HoloVariantGroupGui extends GuiElement {

    private final GuiElement variantsContainer;

    private final KeyframeAnimation labelAnimation;

    private final KeyframeAnimation[] itemAnimations;

    public HoloVariantGroupGui(int x, int y, String category, List<OutcomePreview> outcomes, int offset, IStatSorter sorter, Player player, Consumer<OutcomePreview> onVariantHover, Consumer<OutcomePreview> onVariantBlur, Consumer<OutcomePreview> onVariantSelect) {
        super(x, y, 0, 50);
        GuiString label = new GuiStringSmall(0, 0, I18n.get("tetra.variant_category." + category + ".label"));
        label.setColor(8355711);
        this.addChild(label);
        this.variantsContainer = new GuiElement(0, 8, this.width, this.height);
        this.addChild(this.variantsContainer);
        this.labelAnimation = new KeyframeAnimation(100, label).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (x - 5), (float) x)).withDelay(40 * offset);
        int width = 0;
        this.itemAnimations = new KeyframeAnimation[outcomes.size()];
        for (int i = 0; i < outcomes.size(); i++) {
            OutcomePreview outcome = (OutcomePreview) outcomes.get(i);
            if (SchematicType.minor.equals(outcome.type)) {
                HoloVariantItemGui variant = new HoloVariantItemGui(i / 2 * 15, i % 2 * 15, outcome, sorter.getValue(player, outcome.itemStack), onVariantHover, onVariantBlur, onVariantSelect);
                this.variantsContainer.addChild(variant);
                this.itemAnimations[i] = new KeyframeAnimation(80, variant).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY(-5.0F, 0.0F, true)).withDelay(40 + 40 * (offset + i / 2));
                width = variant.getX() + variant.getWidth();
            } else {
                HoloVariantMajorItemGui variant = new HoloVariantMajorItemGui(i / 2 * 20 + i % 2 * 10, i % 2 * 15, outcome, sorter.getValue(player, outcome.itemStack), onVariantHover, onVariantBlur, onVariantSelect);
                this.variantsContainer.addChild(variant);
                this.itemAnimations[i] = new KeyframeAnimation(80, variant).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX(-5.0F, 0.0F, true), new Applier.TranslateY(-5.0F, 0.0F, true)).withDelay(40 + 40 * (offset + i / 2));
                width = variant.getX() + variant.getWidth() - i % 2 * 5;
            }
        }
        this.setWidth(Math.max(width, label.getWidth()));
    }

    public void updateSelection(OutcomePreview outcome) {
        this.variantsContainer.getChildren(HoloVariantItemGui.class).forEach(variant -> variant.updateSelection(outcome));
    }

    public void animateIn() {
        this.labelAnimation.start();
        Arrays.stream(this.itemAnimations).forEach(KeyframeAnimation::start);
    }
}
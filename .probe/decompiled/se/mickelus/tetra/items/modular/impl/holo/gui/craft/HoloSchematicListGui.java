package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Arrays;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.util.Filter;
import se.mickelus.tetra.blocks.workbench.gui.GuiSchematicListItem;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.CraftingContext;
import se.mickelus.tetra.module.schematic.SchematicRarity;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloSchematicListGui extends GuiElement {

    private final Consumer<UpgradeSchematic> onSchematicSelect;

    private final KeyframeAnimation openAnimation;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    public HoloSchematicListGui(int x, int y, int width, int height, Consumer<UpgradeSchematic> onSchematicSelect) {
        super(x, y, width, height);
        this.onSchematicSelect = onSchematicSelect;
        this.openAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateY((float) (y - 5), (float) y)).withDelay(120);
        this.showAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY((float) y));
        this.hideAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY((float) (y - 5))).onStop(complete -> this.isVisible = false);
    }

    public void update(IModularItem item, String slot) {
        int offset = 0;
        int pageLines = 8;
        CraftingContext context = new CraftingContext(null, null, null, null, new ItemStack(item.getItem()), slot, new ResourceLocation[0]);
        UpgradeSchematic[] schematics = (UpgradeSchematic[]) Arrays.stream(SchematicRegistry.getSchematics(context)).filter(schematicx -> !schematicx.isHoning()).filter(schematicx -> !schematicx.getRarity().equals(SchematicRarity.temporary)).filter(schematicx -> !schematicx.getType().equals(SchematicType.improvement)).filter(Filter.distinct(UpgradeSchematic::getName)).toArray(UpgradeSchematic[]::new);
        int count = schematics.length;
        this.clearChildren();
        for (int i = 0; i < count; i++) {
            UpgradeSchematic schematic = schematics[i + offset];
            this.addChild(new GuiSchematicListItem(i / pageLines * 106, i % pageLines * 14, 103, schematic, () -> this.onSchematicSelect.accept(schematic)));
        }
    }

    public void animateOpen() {
        this.openAnimation.start();
    }

    @Override
    protected void onShow() {
        super.onShow();
        this.hideAnimation.stop();
        this.showAnimation.start();
    }

    @Override
    protected boolean onHide() {
        this.showAnimation.stop();
        this.hideAnimation.start();
        return false;
    }
}
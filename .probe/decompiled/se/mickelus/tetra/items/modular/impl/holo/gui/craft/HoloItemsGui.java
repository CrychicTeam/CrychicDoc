package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.ModularBladedItem;
import se.mickelus.tetra.items.modular.impl.ModularDoubleHeadedItem;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ModularToolbeltItem;

@ParametersAreNonnullByDefault
public class HoloItemsGui extends GuiElement {

    private final HoloSeparatorsGui separators;

    private final HoloMaterialsButtonGui materialsButton;

    private final KeyframeAnimation openAnimation;

    private final KeyframeAnimation backAnimation;

    public HoloItemsGui(int x, int y, int width, int height, BiConsumer<IModularItem, ItemStack> onItemSelect, Consumer<String> onSlotSelect, Runnable onMaterialsClick) {
        super(x, y, width, height);
        this.separators = new HoloSeparatorsGui(1, -71, width, height);
        this.addChild(this.separators);
        this.addChild(new HoloItemGui(-39, 0, ModularBladedItem.instance, 0, () -> onItemSelect.accept(ModularBladedItem.instance, ModularBladedItem.instance.getDefaultStack()), onSlotSelect).setAttachment(GuiAttachment.topCenter));
        this.addChild(new HoloItemGui(1, -40, ModularToolbeltItem.instance.get(), 4, () -> onItemSelect.accept(ModularToolbeltItem.instance.get(), ModularToolbeltItem.instance.get().getDefaultStack()), onSlotSelect).setAttachment(GuiAttachment.topCenter));
        this.addChild(new HoloItemGui(41, 0, ModularDoubleHeadedItem.instance, 1, () -> onItemSelect.accept(ModularDoubleHeadedItem.instance, ModularDoubleHeadedItem.instance.getDefaultStack()), onSlotSelect).setAttachment(GuiAttachment.topCenter));
        if (ConfigHandler.enableSingle.get()) {
            this.addChild(new HoloItemGui(81, -40, ModularSingleHeadedItem.instance, 2, () -> onItemSelect.accept(ModularSingleHeadedItem.instance, ModularSingleHeadedItem.instance.getDefaultStack()), onSlotSelect).setAttachment(GuiAttachment.topCenter));
        }
        if (ConfigHandler.enableCrossbow.get()) {
            this.addChild(new HoloItemGui(-79, 40, ModularCrossbowItem.instance, 7, () -> onItemSelect.accept(ModularCrossbowItem.instance, ModularCrossbowItem.instance.getDefaultStack()), onSlotSelect).setAttachment(GuiAttachment.topCenter));
        }
        if (ConfigHandler.enableBow.get()) {
            this.addChild(new HoloItemGui(-79, -40, ModularBowItem.instance, 5, () -> onItemSelect.accept(ModularBowItem.instance, ModularBowItem.instance.getDefaultStack()), onSlotSelect).setAttachment(GuiAttachment.topCenter));
        }
        if (ConfigHandler.enableShield.get()) {
            this.addChild(new HoloItemGui(81, 40, ModularShieldItem.instance, 3, () -> onItemSelect.accept(ModularShieldItem.instance, ModularShieldItem.instance.getDefaultStack()), onSlotSelect).setAttachment(GuiAttachment.topCenter));
        }
        this.materialsButton = new HoloMaterialsButtonGui(0, 60, onMaterialsClick);
        this.materialsButton.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.materialsButton);
        this.openAnimation = new KeyframeAnimation(200, this).applyTo(new Applier.TranslateY((float) (y - 4), (float) y), new Applier.Opacity(0.0F, 1.0F)).withDelay(800);
        this.backAnimation = new KeyframeAnimation(100, this).applyTo(new Applier.TranslateY((float) (y - 4), (float) y), new Applier.Opacity(0.0F, 1.0F));
    }

    public void animateOpen() {
        this.openAnimation.start();
    }

    public void animateOpenAll() {
        this.openAnimation.start();
        this.separators.animateOpen();
    }

    public void animateBack() {
        this.backAnimation.start();
    }

    public void changeItem(IModularItem item) {
        this.getChildren(HoloItemGui.class).forEach(child -> child.onItemSelected(item));
        this.materialsButton.setVisible(item == null);
        if (item == null) {
            this.separators.animateReopen();
        } else {
            this.separators.setVisible(false);
        }
    }
}
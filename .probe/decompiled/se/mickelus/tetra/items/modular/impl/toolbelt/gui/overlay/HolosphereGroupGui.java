package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.scan.ScannerOverlayGui;

@ParametersAreNonnullByDefault
public class HolosphereGroupGui extends GuiElement {

    public HolosphereGroupGui(int x, int y) {
        super(x, y, 0, 0);
        this.setAttachmentPoint(GuiAttachment.bottomCenter);
        this.setAttachmentAnchor(GuiAttachment.middleCenter);
    }

    public void update(ItemStack itemStack) {
        this.clearChildren();
        if (ModularHolosphereItem.instance.getEffectLevel(itemStack, ItemEffect.sweeperRange) > 0) {
            String label = I18n.get(ScannerOverlayGui.instance.isSnoozed() ? "tetra.holo.quick_access.snooze_off" : "tetra.holo.quick_access.snooze_on");
            this.addChild(new HolosphereActionGui(0, 0, 77, 28, label, this::toggleSnooze).setAttachment(GuiAttachment.bottomCenter));
        }
    }

    public void clear() {
        this.clearChildren();
    }

    @Override
    public void setVisible(boolean visible) {
        this.getChildren().forEach(child -> child.setVisible(visible));
    }

    public void performActions() {
        this.getChildren(HolosphereActionGui.class).stream().filter(GuiElement::hasFocus).findFirst().ifPresent(HolosphereActionGui::perform);
    }

    private void toggleSnooze() {
        ScannerOverlayGui.instance.toggleSnooze();
    }
}
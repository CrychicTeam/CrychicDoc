package net.minecraft.client.tutorial;

import javax.annotation.Nullable;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BundleTutorial {

    private final Tutorial tutorial;

    private final Options options;

    @Nullable
    private TutorialToast toast;

    public BundleTutorial(Tutorial tutorial0, Options options1) {
        this.tutorial = tutorial0;
        this.options = options1;
    }

    private void showToast() {
        if (this.toast != null) {
            this.tutorial.removeTimedToast(this.toast);
        }
        Component $$0 = Component.translatable("tutorial.bundleInsert.title");
        Component $$1 = Component.translatable("tutorial.bundleInsert.description");
        this.toast = new TutorialToast(TutorialToast.Icons.RIGHT_CLICK, $$0, $$1, true);
        this.tutorial.addTimedToast(this.toast, 160);
    }

    private void clearToast() {
        if (this.toast != null) {
            this.tutorial.removeTimedToast(this.toast);
            this.toast = null;
        }
        if (!this.options.hideBundleTutorial) {
            this.options.hideBundleTutorial = true;
            this.options.save();
        }
    }

    public void onInventoryAction(ItemStack itemStack0, ItemStack itemStack1, ClickAction clickAction2) {
        if (!this.options.hideBundleTutorial) {
            if (!itemStack0.isEmpty() && itemStack1.is(Items.BUNDLE)) {
                if (clickAction2 == ClickAction.PRIMARY) {
                    this.showToast();
                } else if (clickAction2 == ClickAction.SECONDARY) {
                    this.clearToast();
                }
            } else if (itemStack0.is(Items.BUNDLE) && !itemStack1.isEmpty() && clickAction2 == ClickAction.SECONDARY) {
                this.clearToast();
            }
        }
    }
}
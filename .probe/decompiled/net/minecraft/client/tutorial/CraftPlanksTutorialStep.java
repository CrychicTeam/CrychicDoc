package net.minecraft.client.tutorial;

import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CraftPlanksTutorialStep implements TutorialStepInstance {

    private static final int HINT_DELAY = 1200;

    private static final Component CRAFT_TITLE = Component.translatable("tutorial.craft_planks.title");

    private static final Component CRAFT_DESCRIPTION = Component.translatable("tutorial.craft_planks.description");

    private final Tutorial tutorial;

    private TutorialToast toast;

    private int timeWaiting;

    public CraftPlanksTutorialStep(Tutorial tutorial0) {
        this.tutorial = tutorial0;
    }

    @Override
    public void tick() {
        this.timeWaiting++;
        if (!this.tutorial.isSurvival()) {
            this.tutorial.setStep(TutorialSteps.NONE);
        } else {
            if (this.timeWaiting == 1) {
                LocalPlayer $$0 = this.tutorial.getMinecraft().player;
                if ($$0 != null) {
                    if ($$0.m_150109_().contains(ItemTags.PLANKS)) {
                        this.tutorial.setStep(TutorialSteps.NONE);
                        return;
                    }
                    if (hasCraftedPlanksPreviously($$0, ItemTags.PLANKS)) {
                        this.tutorial.setStep(TutorialSteps.NONE);
                        return;
                    }
                }
            }
            if (this.timeWaiting >= 1200 && this.toast == null) {
                this.toast = new TutorialToast(TutorialToast.Icons.WOODEN_PLANKS, CRAFT_TITLE, CRAFT_DESCRIPTION, false);
                this.tutorial.getMinecraft().getToasts().addToast(this.toast);
            }
        }
    }

    @Override
    public void clear() {
        if (this.toast != null) {
            this.toast.hide();
            this.toast = null;
        }
    }

    @Override
    public void onGetItem(ItemStack itemStack0) {
        if (itemStack0.is(ItemTags.PLANKS)) {
            this.tutorial.setStep(TutorialSteps.NONE);
        }
    }

    public static boolean hasCraftedPlanksPreviously(LocalPlayer localPlayer0, TagKey<Item> tagKeyItem1) {
        for (Holder<Item> $$2 : BuiltInRegistries.ITEM.m_206058_(tagKeyItem1)) {
            if (localPlayer0.getStats().getValue(Stats.ITEM_CRAFTED.get($$2.value())) > 0) {
                return true;
            }
        }
        return false;
    }
}
package net.minecraft.client.tutorial;

import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PunchTreeTutorialStepInstance implements TutorialStepInstance {

    private static final int HINT_DELAY = 600;

    private static final Component TITLE = Component.translatable("tutorial.punch_tree.title");

    private static final Component DESCRIPTION = Component.translatable("tutorial.punch_tree.description", Tutorial.key("attack"));

    private final Tutorial tutorial;

    private TutorialToast toast;

    private int timeWaiting;

    private int resetCount;

    public PunchTreeTutorialStepInstance(Tutorial tutorial0) {
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
                    if ($$0.m_150109_().contains(ItemTags.LOGS)) {
                        this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                        return;
                    }
                    if (FindTreeTutorialStepInstance.hasPunchedTreesPreviously($$0)) {
                        this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                        return;
                    }
                }
            }
            if ((this.timeWaiting >= 600 || this.resetCount > 3) && this.toast == null) {
                this.toast = new TutorialToast(TutorialToast.Icons.TREE, TITLE, DESCRIPTION, true);
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
    public void onDestroyBlock(ClientLevel clientLevel0, BlockPos blockPos1, BlockState blockState2, float float3) {
        boolean $$4 = blockState2.m_204336_(BlockTags.LOGS);
        if ($$4 && float3 > 0.0F) {
            if (this.toast != null) {
                this.toast.updateProgress(float3);
            }
            if (float3 >= 1.0F) {
                this.tutorial.setStep(TutorialSteps.OPEN_INVENTORY);
            }
        } else if (this.toast != null) {
            this.toast.updateProgress(0.0F);
        } else if ($$4) {
            this.resetCount++;
        }
    }

    @Override
    public void onGetItem(ItemStack itemStack0) {
        if (itemStack0.is(ItemTags.LOGS)) {
            this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
        }
    }
}
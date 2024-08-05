package net.minecraft.client.tutorial;

import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class FindTreeTutorialStepInstance implements TutorialStepInstance {

    private static final int HINT_DELAY = 6000;

    private static final Component TITLE = Component.translatable("tutorial.find_tree.title");

    private static final Component DESCRIPTION = Component.translatable("tutorial.find_tree.description");

    private final Tutorial tutorial;

    private TutorialToast toast;

    private int timeWaiting;

    public FindTreeTutorialStepInstance(Tutorial tutorial0) {
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
                if ($$0 != null && (hasCollectedTreeItems($$0) || hasPunchedTreesPreviously($$0))) {
                    this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
                    return;
                }
            }
            if (this.timeWaiting >= 6000 && this.toast == null) {
                this.toast = new TutorialToast(TutorialToast.Icons.TREE, TITLE, DESCRIPTION, false);
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
    public void onLookAt(ClientLevel clientLevel0, HitResult hitResult1) {
        if (hitResult1.getType() == HitResult.Type.BLOCK) {
            BlockState $$2 = clientLevel0.m_8055_(((BlockHitResult) hitResult1).getBlockPos());
            if ($$2.m_204336_(BlockTags.COMPLETES_FIND_TREE_TUTORIAL)) {
                this.tutorial.setStep(TutorialSteps.PUNCH_TREE);
            }
        }
    }

    @Override
    public void onGetItem(ItemStack itemStack0) {
        if (itemStack0.is(ItemTags.COMPLETES_FIND_TREE_TUTORIAL)) {
            this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
        }
    }

    private static boolean hasCollectedTreeItems(LocalPlayer localPlayer0) {
        return localPlayer0.m_150109_().m_216874_(p_235270_ -> p_235270_.is(ItemTags.COMPLETES_FIND_TREE_TUTORIAL));
    }

    public static boolean hasPunchedTreesPreviously(LocalPlayer localPlayer0) {
        for (Holder<Block> $$1 : BuiltInRegistries.BLOCK.m_206058_(BlockTags.COMPLETES_FIND_TREE_TUTORIAL)) {
            Block $$2 = $$1.value();
            if (localPlayer0.getStats().getValue(Stats.BLOCK_MINED.get($$2)) > 0) {
                return true;
            }
        }
        return false;
    }
}
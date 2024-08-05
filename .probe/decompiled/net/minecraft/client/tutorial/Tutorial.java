package net.minecraft.client.tutorial;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public class Tutorial {

    private final Minecraft minecraft;

    @Nullable
    private TutorialStepInstance instance;

    private final List<Tutorial.TimedToast> timedToasts = Lists.newArrayList();

    private final BundleTutorial bundleTutorial;

    public Tutorial(Minecraft minecraft0, Options options1) {
        this.minecraft = minecraft0;
        this.bundleTutorial = new BundleTutorial(this, options1);
    }

    public void onInput(Input input0) {
        if (this.instance != null) {
            this.instance.onInput(input0);
        }
    }

    public void onMouse(double double0, double double1) {
        if (this.instance != null) {
            this.instance.onMouse(double0, double1);
        }
    }

    public void onLookAt(@Nullable ClientLevel clientLevel0, @Nullable HitResult hitResult1) {
        if (this.instance != null && hitResult1 != null && clientLevel0 != null) {
            this.instance.onLookAt(clientLevel0, hitResult1);
        }
    }

    public void onDestroyBlock(ClientLevel clientLevel0, BlockPos blockPos1, BlockState blockState2, float float3) {
        if (this.instance != null) {
            this.instance.onDestroyBlock(clientLevel0, blockPos1, blockState2, float3);
        }
    }

    public void onOpenInventory() {
        if (this.instance != null) {
            this.instance.onOpenInventory();
        }
    }

    public void onGetItem(ItemStack itemStack0) {
        if (this.instance != null) {
            this.instance.onGetItem(itemStack0);
        }
    }

    public void stop() {
        if (this.instance != null) {
            this.instance.clear();
            this.instance = null;
        }
    }

    public void start() {
        if (this.instance != null) {
            this.stop();
        }
        this.instance = this.minecraft.options.tutorialStep.create(this);
    }

    public void addTimedToast(TutorialToast tutorialToast0, int int1) {
        this.timedToasts.add(new Tutorial.TimedToast(tutorialToast0, int1));
        this.minecraft.getToasts().addToast(tutorialToast0);
    }

    public void removeTimedToast(TutorialToast tutorialToast0) {
        this.timedToasts.removeIf(p_120577_ -> p_120577_.toast == tutorialToast0);
        tutorialToast0.hide();
    }

    public void tick() {
        this.timedToasts.removeIf(Tutorial.TimedToast::m_120609_);
        if (this.instance != null) {
            if (this.minecraft.level != null) {
                this.instance.tick();
            } else {
                this.stop();
            }
        } else if (this.minecraft.level != null) {
            this.start();
        }
    }

    public void setStep(TutorialSteps tutorialSteps0) {
        this.minecraft.options.tutorialStep = tutorialSteps0;
        this.minecraft.options.save();
        if (this.instance != null) {
            this.instance.clear();
            this.instance = tutorialSteps0.create(this);
        }
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public boolean isSurvival() {
        return this.minecraft.gameMode == null ? false : this.minecraft.gameMode.getPlayerMode() == GameType.SURVIVAL;
    }

    public static Component key(String string0) {
        return Component.keybind("key." + string0).withStyle(ChatFormatting.BOLD);
    }

    public void onInventoryAction(ItemStack itemStack0, ItemStack itemStack1, ClickAction clickAction2) {
        this.bundleTutorial.onInventoryAction(itemStack0, itemStack1, clickAction2);
    }

    static final class TimedToast {

        final TutorialToast toast;

        private final int durationTicks;

        private int progress;

        TimedToast(TutorialToast tutorialToast0, int int1) {
            this.toast = tutorialToast0;
            this.durationTicks = int1;
        }

        private boolean updateProgress() {
            this.toast.updateProgress(Math.min((float) (++this.progress) / (float) this.durationTicks, 1.0F));
            if (this.progress > this.durationTicks) {
                this.toast.hide();
                return true;
            } else {
                return false;
            }
        }
    }
}
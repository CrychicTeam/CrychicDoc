package net.minecraft.client;

import com.mojang.logging.LogUtils;
import com.mojang.text2speech.Narrator;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.main.SilentInitException;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import org.slf4j.Logger;

public class GameNarrator {

    public static final Component NO_TITLE = CommonComponents.EMPTY;

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Minecraft minecraft;

    private final Narrator narrator = Narrator.getNarrator();

    public GameNarrator(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    public void sayChat(Component component0) {
        if (this.getStatus().shouldNarrateChat()) {
            String $$1 = component0.getString();
            this.logNarratedMessage($$1);
            this.narrator.say($$1, false);
        }
    }

    public void say(Component component0) {
        String $$1 = component0.getString();
        if (this.getStatus().shouldNarrateSystem() && !$$1.isEmpty()) {
            this.logNarratedMessage($$1);
            this.narrator.say($$1, false);
        }
    }

    public void sayNow(Component component0) {
        this.sayNow(component0.getString());
    }

    public void sayNow(String string0) {
        if (this.getStatus().shouldNarrateSystem() && !string0.isEmpty()) {
            this.logNarratedMessage(string0);
            if (this.narrator.active()) {
                this.narrator.clear();
                this.narrator.say(string0, true);
            }
        }
    }

    private NarratorStatus getStatus() {
        return this.minecraft.options.narrator().get();
    }

    private void logNarratedMessage(String string0) {
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            LOGGER.debug("Narrating: {}", string0.replaceAll("\n", "\\\\n"));
        }
    }

    public void updateNarratorStatus(NarratorStatus narratorStatus0) {
        this.clear();
        this.narrator.say(Component.translatable("options.narrator").append(" : ").append(narratorStatus0.getName()).getString(), true);
        ToastComponent $$1 = Minecraft.getInstance().getToasts();
        if (this.narrator.active()) {
            if (narratorStatus0 == NarratorStatus.OFF) {
                SystemToast.addOrUpdate($$1, SystemToast.SystemToastIds.NARRATOR_TOGGLE, Component.translatable("narrator.toast.disabled"), null);
            } else {
                SystemToast.addOrUpdate($$1, SystemToast.SystemToastIds.NARRATOR_TOGGLE, Component.translatable("narrator.toast.enabled"), narratorStatus0.getName());
            }
        } else {
            SystemToast.addOrUpdate($$1, SystemToast.SystemToastIds.NARRATOR_TOGGLE, Component.translatable("narrator.toast.disabled"), Component.translatable("options.narrator.notavailable"));
        }
    }

    public boolean isActive() {
        return this.narrator.active();
    }

    public void clear() {
        if (this.getStatus() != NarratorStatus.OFF && this.narrator.active()) {
            this.narrator.clear();
        }
    }

    public void destroy() {
        this.narrator.destroy();
    }

    public void checkStatus(boolean boolean0) {
        if (boolean0 && !this.isActive() && !TinyFileDialogs.tinyfd_messageBox("Minecraft", "Failed to initialize text-to-speech library. Do you want to continue?\nIf this problem persists, please report it at bugs.mojang.com", "yesno", "error", true)) {
            throw new GameNarrator.NarratorInitException("Narrator library is not active");
        }
    }

    public static class NarratorInitException extends SilentInitException {

        public NarratorInitException(String string0) {
            super(string0);
        }
    }
}
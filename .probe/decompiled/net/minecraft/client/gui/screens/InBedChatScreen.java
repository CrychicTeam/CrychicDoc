package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;

public class InBedChatScreen extends ChatScreen {

    private Button leaveBedButton;

    public InBedChatScreen() {
        super("");
    }

    @Override
    protected void init() {
        super.init();
        this.leaveBedButton = Button.builder(Component.translatable("multiplayer.stopSleeping"), p_96074_ -> this.sendWakeUp()).bounds(this.f_96543_ / 2 - 100, this.f_96544_ - 40, 200, 20).build();
        this.m_142416_(this.leaveBedButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (!this.f_96541_.getChatStatus().isChatAllowed(this.f_96541_.isLocalServer())) {
            this.leaveBedButton.m_88315_(guiGraphics0, int1, int2, float3);
        } else {
            super.render(guiGraphics0, int1, int2, float3);
        }
    }

    @Override
    public void onClose() {
        this.sendWakeUp();
    }

    @Override
    public boolean charTyped(char char0, int int1) {
        return !this.f_96541_.getChatStatus().isChatAllowed(this.f_96541_.isLocalServer()) ? true : super.m_5534_(char0, int1);
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (int0 == 256) {
            this.sendWakeUp();
        }
        if (!this.f_96541_.getChatStatus().isChatAllowed(this.f_96541_.isLocalServer())) {
            return true;
        } else if (int0 != 257 && int0 != 335) {
            return super.keyPressed(int0, int1, int2);
        } else {
            if (this.m_241797_(this.f_95573_.getValue(), true)) {
                this.f_96541_.setScreen(null);
                this.f_95573_.setValue("");
                this.f_96541_.gui.getChat().resetChatScroll();
            }
            return true;
        }
    }

    private void sendWakeUp() {
        ClientPacketListener $$0 = this.f_96541_.player.connection;
        $$0.send(new ServerboundPlayerCommandPacket(this.f_96541_.player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
    }

    public void onPlayerWokeUp() {
        if (this.f_95573_.getValue().isEmpty()) {
            this.f_96541_.setScreen(null);
        } else {
            this.f_96541_.setScreen(new ChatScreen(this.f_95573_.getValue()));
        }
    }
}
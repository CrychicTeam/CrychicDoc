package com.simibubi.create.content.trains.entity;

import com.simibubi.create.content.trains.TrainHUD;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class TrainPromptPacket extends SimplePacketBase {

    private Component text;

    private boolean shadow;

    public TrainPromptPacket(Component text, boolean shadow) {
        this.text = text;
        this.shadow = shadow;
    }

    public TrainPromptPacket(FriendlyByteBuf buffer) {
        this.text = buffer.readComponent();
        this.shadow = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeComponent(this.text);
        buffer.writeBoolean(this.shadow);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::apply));
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void apply() {
        TrainHUD.currentPrompt = this.text;
        TrainHUD.currentPromptShadow = this.shadow;
        TrainHUD.promptKeepAlive = 30;
    }
}
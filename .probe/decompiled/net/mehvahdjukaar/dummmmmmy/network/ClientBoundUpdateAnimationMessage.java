package net.mehvahdjukaar.dummmmmmy.network;

import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class ClientBoundUpdateAnimationMessage implements Message {

    private final int entityID;

    private final float shake;

    public ClientBoundUpdateAnimationMessage(FriendlyByteBuf buf) {
        this.entityID = buf.readInt();
        this.shake = buf.readFloat();
    }

    public ClientBoundUpdateAnimationMessage(int id, float shakeAmount) {
        this.entityID = id;
        this.shake = shakeAmount;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID);
        buf.writeFloat(this.shake);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        if (Minecraft.getInstance().level.getEntity(this.entityID) instanceof TargetDummyEntity dummy) {
            dummy.updateAnimation(this.shake);
        }
    }
}
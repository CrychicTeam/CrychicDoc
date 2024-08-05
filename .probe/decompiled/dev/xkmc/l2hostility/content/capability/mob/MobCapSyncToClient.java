package dev.xkmc.l2hostility.content.capability.mob;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class MobCapSyncToClient extends SerialPacketBase {

    @SerialField
    public CompoundTag tag;

    @SerialField
    public int id;

    @Deprecated
    public MobCapSyncToClient() {
    }

    public MobCapSyncToClient(LivingEntity entity, MobTraitCap cap) {
        this.id = entity.m_19879_();
        this.tag = TagCodec.toTag(new CompoundTag(), MobTraitCap.class, cap, SerialField::toClient);
    }

    public void handle(NetworkEvent.Context context) {
        ClientCapHandler.handle(this);
    }
}
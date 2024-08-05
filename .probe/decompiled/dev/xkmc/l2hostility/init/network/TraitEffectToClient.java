package dev.xkmc.l2hostility.init.network;

import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class TraitEffectToClient extends SerialPacketBase {

    @SerialField
    public int id;

    @SerialField
    public MobTrait trait;

    @SerialField
    public TraitEffects effect;

    @SerialField
    public BlockPos pos = BlockPos.ZERO;

    @Deprecated
    public TraitEffectToClient() {
    }

    public TraitEffectToClient(LivingEntity e, MobTrait trait, TraitEffects effect) {
        this.id = e.m_19879_();
        this.trait = trait;
        this.effect = effect;
    }

    public TraitEffectToClient(BlockPos pos, TraitEffects effect) {
        this.pos = pos;
        this.effect = effect;
    }

    public void handle(NetworkEvent.Context context) {
        ClientSyncHandler.handleEffect(this);
    }
}
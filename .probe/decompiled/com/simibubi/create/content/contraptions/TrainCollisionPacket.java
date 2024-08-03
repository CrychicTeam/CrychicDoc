package com.simibubi.create.content.contraptions;

import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class TrainCollisionPacket extends SimplePacketBase {

    int damage;

    int contraptionEntityId;

    public TrainCollisionPacket(int damage, int contraptionEntityId) {
        this.damage = damage;
        this.contraptionEntityId = contraptionEntityId;
    }

    public TrainCollisionPacket(FriendlyByteBuf buffer) {
        this.contraptionEntityId = buffer.readInt();
        this.damage = buffer.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.contraptionEntityId);
        buffer.writeInt(this.damage);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level level = player.m_9236_();
            Entity entity = level.getEntity(this.contraptionEntityId);
            if (entity instanceof CarriageContraptionEntity cce) {
                player.hurt(CreateDamageSources.runOver(level, cce), (float) this.damage);
                player.m_9236_().playSound((Player) player, entity.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.NEUTRAL, 1.0F, 0.75F);
            }
        });
        return true;
    }
}
package com.mna.entities.boss.attacks;

import com.mna.entities.EntityInit;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.to_client.SpawnParticleEffectMessage;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;

public class IllusionCreeper extends Creeper {

    public IllusionCreeper(EntityType<IllusionCreeper> type, Level world) {
        super(type, world);
    }

    public IllusionCreeper(Level world) {
        this(EntityInit.ILLUSION_CREEPER.get(), world);
    }

    @Override
    public void explodeCreeper() {
        if (!this.m_9236_().isClientSide()) {
            ServerMessageDispatcher.sendParticleEffect(this.m_9236_().dimension(), 32.0F, this.m_20185_(), this.m_20188_(), this.m_20189_(), SpawnParticleEffectMessage.ParticleTypes.MANAWEAVE_CRAFT_COMPLETE);
            this.m_146870_();
        }
    }
}
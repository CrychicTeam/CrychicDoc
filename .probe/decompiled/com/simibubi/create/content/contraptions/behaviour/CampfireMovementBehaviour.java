package com.simibubi.create.content.contraptions.behaviour;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CampfireBlock;

public class CampfireMovementBehaviour implements MovementBehaviour {

    @Override
    public boolean renderAsNormalBlockEntity() {
        return true;
    }

    @Override
    public void tick(MovementContext context) {
        if (context.world != null && context.world.isClientSide && context.position != null && (Boolean) context.state.m_61143_(CampfireBlock.LIT) && !context.disabled) {
            RandomSource random = context.world.random;
            if (random.nextFloat() < 0.11F) {
                for (int i = 0; i < random.nextInt(2) + 2; i++) {
                    context.world.addAlwaysVisibleParticle(context.state.m_61143_(CampfireBlock.SIGNAL_FIRE) ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE, true, context.position.x() + random.nextDouble() / (random.nextBoolean() ? 3.0 : -3.0), context.position.y() + random.nextDouble() + random.nextDouble(), context.position.z() + random.nextDouble() / (random.nextBoolean() ? 3.0 : -3.0), 0.0, 0.07, 0.0);
                }
            }
        }
    }
}
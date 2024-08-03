package com.github.alexthe666.citadel.animation;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.message.AnimationMessage;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.ArrayUtils;

public enum AnimationHandler {

    INSTANCE;

    public <T extends Entity & IAnimatedEntity> void sendAnimationMessage(T entity, Animation animation) {
        if (!entity.level().isClientSide) {
            entity.setAnimation(animation);
            Citadel.sendMSGToAll(new AnimationMessage(entity.getId(), ArrayUtils.indexOf(entity.getAnimations(), animation)));
        }
    }

    public <T extends Entity & IAnimatedEntity> void updateAnimations(T entity) {
        if (entity.getAnimation() == null) {
            entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
        } else if (entity.getAnimation() != IAnimatedEntity.NO_ANIMATION) {
            if (entity.getAnimationTick() == 0) {
                AnimationEvent event = new AnimationEvent.Start(entity, entity.getAnimation());
                if (!MinecraftForge.EVENT_BUS.post(event)) {
                    this.sendAnimationMessage(entity, event.getAnimation());
                }
            }
            if (entity.getAnimationTick() < entity.getAnimation().getDuration()) {
                entity.setAnimationTick(entity.getAnimationTick() + 1);
                MinecraftForge.EVENT_BUS.post(new AnimationEvent.Tick(entity, entity.getAnimation(), entity.getAnimationTick()));
            }
            if (entity.getAnimationTick() == entity.getAnimation().getDuration()) {
                entity.setAnimationTick(0);
                entity.setAnimation(IAnimatedEntity.NO_ANIMATION);
            }
        }
    }
}
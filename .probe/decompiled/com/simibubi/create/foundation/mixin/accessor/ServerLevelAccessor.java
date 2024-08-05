package com.simibubi.create.foundation.mixin.accessor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ServerLevel.class })
public interface ServerLevelAccessor {

    @Accessor("entityTickList")
    EntityTickList create$getEntityTickList();
}
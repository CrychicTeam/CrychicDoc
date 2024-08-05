package com.cupboard.mixin;

import com.cupboard.Cupboard;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ServerLevel.class })
public abstract class ServerAddEntityMixin {

    @Unique
    private final ConcurrentLinkedQueue<Entity> toAdd = new ConcurrentLinkedQueue();

    @Unique
    private final ConcurrentHashMap<EntityType, EntityType> warned = new ConcurrentHashMap();

    @Unique
    private boolean addingLate = false;

    @Shadow
    public abstract boolean addEntity(Entity var1);

    @Inject(method = { "addEntity" }, at = { @At("HEAD") }, cancellable = true)
    private void OnaddEntity(Entity entityIn, CallbackInfoReturnable<Boolean> c) {
        String current = Thread.currentThread().getName();
        if (!current.toLowerCase().contains("server")) {
            if (Cupboard.config.getCommonConfig().logOffthreadEntityAdd && !this.warned.contains(entityIn.getType())) {
                Cupboard.LOGGER.warn("A mod is trying to add an entity from offthread, this should be avoided. Printing trace:", new Exception());
                this.warned.put(entityIn.getType(), entityIn.getType());
            }
            this.toAdd.add(entityIn);
            c.setReturnValue(true);
        } else if (!this.addingLate) {
            this.addingLate = true;
            Iterator<Entity> it = this.toAdd.iterator();
            while (it.hasNext()) {
                this.addEntity((Entity) it.next());
                it.remove();
            }
            this.addingLate = false;
        }
    }
}
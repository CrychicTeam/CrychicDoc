package forge.me.thosea.badoptimizations.mixin.entitydata;

import forge.me.thosea.badoptimizations.interfaces.EntityMethods;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SynchedEntityData.class })
public abstract class MixinDataTracker {

    @Shadow
    @Final
    @Mutable
    private ReadWriteLock lock;

    private EntityMethods bo$entityMethods;

    @Inject(method = { "set(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;Z)V" }, at = { @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/entity/data/DataTracker$Entry;set(Ljava/lang/Object;)V") })
    private void onDataSet(EntityDataAccessor<?> key, Object value, boolean force, CallbackInfo ci) {
        this.bo$entityMethods.bo$refreshEntityData(key.getId());
    }

    @Inject(method = { "copyToFrom" }, at = { @At("TAIL") })
    private void onCopy(SynchedEntityData.DataItem<?> to, SynchedEntityData.DataValue<?> from, CallbackInfo ci) {
        this.bo$entityMethods.bo$refreshEntityData(from.id());
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void replaceLock(Entity trackedEntity, CallbackInfo ci) {
        final Lock lock = new Lock() {

            public void lock() {
            }

            public void lockInterruptibly() {
            }

            public boolean tryLock() {
                return true;
            }

            public boolean tryLock(long time, @NotNull TimeUnit unit) {
                return true;
            }

            public void unlock() {
            }

            @NotNull
            public Condition newCondition() {
                throw new UnsupportedOperationException();
            }
        };
        this.lock = new ReadWriteLock() {

            @NotNull
            public Lock readLock() {
                return lock;
            }

            @NotNull
            public Lock writeLock() {
                return lock;
            }
        };
        this.bo$entityMethods = (EntityMethods) trackedEntity;
    }
}
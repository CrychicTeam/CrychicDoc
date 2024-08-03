package fabric.me.thosea.badoptimizations.mixin.entitydata;

import fabric.me.thosea.badoptimizations.interfaces.EntityMethods;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import net.minecraft.class_1297;
import net.minecraft.class_2940;
import net.minecraft.class_2945;
import net.minecraft.class_2945.class_2946;
import net.minecraft.class_2945.class_7834;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ class_2945.class })
public abstract class MixinDataTracker {

    @Shadow
    @Final
    @Mutable
    private ReadWriteLock field_13335;

    private EntityMethods bo$entityMethods;

    @Inject(method = { "set(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;Z)V" }, at = { @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/entity/data/DataTracker$Entry;set(Ljava/lang/Object;)V") })
    private void onDataSet(class_2940<?> key, Object value, boolean force, CallbackInfo ci) {
        this.bo$entityMethods.bo$refreshEntityData(key.method_12713());
    }

    @Inject(method = { "copyToFrom" }, at = { @At("TAIL") })
    private void onCopy(class_2946<?> to, class_7834<?> from, CallbackInfo ci) {
        this.bo$entityMethods.bo$refreshEntityData(from.comp_1115());
    }

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void replaceLock(class_1297 trackedEntity, CallbackInfo ci) {
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
        this.field_13335 = new ReadWriteLock() {

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
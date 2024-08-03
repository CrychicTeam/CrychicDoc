package forge.me.thosea.badoptimizations.mixin.entitydata;

import forge.me.thosea.badoptimizations.interfaces.EntityMethods;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { Entity.class }, priority = 700)
public abstract class MixinEntity implements EntityMethods {

    @Shadow
    @Final
    private static EntityDataAccessor<Optional<Component>> DATA_CUSTOM_NAME;

    @Shadow
    @Final
    protected static EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID;

    @Shadow
    @Final
    private static EntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE;

    @Shadow
    @Final
    private static EntityDataAccessor<Boolean> DATA_SILENT;

    @Shadow
    @Final
    private static EntityDataAccessor<Boolean> DATA_NO_GRAVITY;

    @Shadow
    @Final
    protected static EntityDataAccessor<Pose> DATA_POSE;

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_TICKS_FROZEN;

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID;

    @Shadow
    private Level level;

    private boolean bo$glowingClient;

    private boolean bo$onFire = false;

    private boolean bo$sneaking = false;

    private boolean bo$sprinting = false;

    private boolean bo$swimming = false;

    private boolean bo$invisible = false;

    private boolean bo$nameVisible = false;

    private boolean bo$silent = false;

    private boolean bo$noGravity = false;

    private int bo$frozenTicks = 0;

    private Pose bo$pose = Pose.STANDING;

    private Optional<Component> bo$customName = Optional.empty();

    private int bo$remainingAirTicks = this.getMaxAirSupply();

    @Shadow
    private boolean hasGlowingTag;

    @Shadow
    @Final
    protected SynchedEntityData entityData;

    @Shadow
    public abstract int getMaxAirSupply();

    @Redirect(method = { "isOnFire" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getFlag(I)Z"))
    private boolean getIsOnFire(Entity instance, int index) {
        return this.bo$onFire;
    }

    @Overwrite
    public boolean isCurrentlyGlowing() {
        return this.level.isClientSide ? this.bo$glowingClient : this.hasGlowingTag;
    }

    @Overwrite
    public boolean isShiftKeyDown() {
        return this.bo$sneaking;
    }

    @Overwrite
    public boolean isSprinting() {
        return this.bo$sprinting;
    }

    @Overwrite
    public boolean isSwimming() {
        return this.bo$swimming;
    }

    @Overwrite
    public boolean isInvisible() {
        return this.bo$invisible;
    }

    @Overwrite
    public int getAirSupply() {
        return this.bo$remainingAirTicks;
    }

    @Overwrite
    @Nullable
    public Component getCustomName() {
        return (Component) this.bo$customName.orElse(null);
    }

    @Overwrite
    public boolean hasCustomName() {
        return this.bo$customName.isPresent();
    }

    @Overwrite
    public boolean isCustomNameVisible() {
        return this.bo$nameVisible;
    }

    @Overwrite
    public boolean isSilent() {
        return this.bo$silent;
    }

    @Overwrite
    public boolean isNoGravity() {
        return this.bo$noGravity;
    }

    @Overwrite
    public Pose getPose() {
        return this.bo$pose;
    }

    @Overwrite
    public int getTicksFrozen() {
        return this.bo$frozenTicks;
    }

    @Override
    public void bo$refreshEntityData(int data) {
        if (data == DATA_SHARED_FLAGS_ID.getId()) {
            byte flags = this.entityData.get(DATA_SHARED_FLAGS_ID);
            this.bo$onFire = this.bo$getFlag(flags, 0);
            this.bo$sneaking = this.bo$getFlag(flags, 1);
            this.bo$sprinting = this.bo$getFlag(flags, 3);
            this.bo$swimming = this.bo$getFlag(flags, 4);
            this.bo$invisible = this.bo$getFlag(flags, 5);
            if (this.level.isClientSide) {
                this.bo$glowingClient = this.bo$getFlag(flags, 6);
            }
        } else if (data == DATA_AIR_SUPPLY_ID.getId()) {
            this.bo$remainingAirTicks = this.entityData.get(DATA_AIR_SUPPLY_ID);
        } else if (data == DATA_CUSTOM_NAME.getId()) {
            this.bo$customName = this.entityData.get(DATA_CUSTOM_NAME);
        } else if (data == DATA_CUSTOM_NAME_VISIBLE.getId()) {
            this.bo$nameVisible = this.entityData.get(DATA_CUSTOM_NAME_VISIBLE);
        } else if (data == DATA_SILENT.getId()) {
            this.bo$silent = this.entityData.get(DATA_SILENT);
        } else if (data == DATA_NO_GRAVITY.getId()) {
            this.bo$noGravity = this.entityData.get(DATA_NO_GRAVITY);
        } else if (data == DATA_POSE.getId()) {
            this.bo$pose = this.entityData.get(DATA_POSE);
        } else if (data == DATA_TICKS_FROZEN.getId()) {
            this.bo$frozenTicks = this.entityData.get(DATA_TICKS_FROZEN);
        }
    }

    private boolean bo$getFlag(byte flags, int index) {
        return (flags & 1 << index) != 0;
    }
}
package forge.me.thosea.badoptimizations.mixin.entitydata;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { LivingEntity.class }, priority = 700)
public abstract class MixinLivingEntity extends MixinEntity {

    @Shadow
    @Final
    protected static EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS;

    @Shadow
    @Final
    private static EntityDataAccessor<Float> DATA_HEALTH_ID;

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID;

    @Shadow
    @Final
    private static EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID;

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID;

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID;

    @Shadow
    @Final
    private static EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID;

    private boolean bo$isUsingItem = false;

    private boolean bo$potionSwirlsAmbient = false;

    private boolean bo$isUsingRiptide = false;

    private InteractionHand bo$activeHand = InteractionHand.MAIN_HAND;

    private float bo$health = 1.0F;

    private int bo$potionSwirlsColor = 0;

    private int bo$stuckArrowCount = 0;

    private int bo$stingerCount = 0;

    private Optional<BlockPos> bo$sleepingPosition = Optional.empty();

    @Redirect(method = { "tickStatusEffects" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;get(Lnet/minecraft/entity/data/TrackedData;)Ljava/lang/Object;", ordinal = 0))
    private Object onGetPotionSwirlsColor(SynchedEntityData instance, EntityDataAccessor<Integer> data) {
        return this.bo$potionSwirlsColor;
    }

    @Redirect(method = { "tickStatusEffects" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/data/DataTracker;get(Lnet/minecraft/entity/data/TrackedData;)Ljava/lang/Object;", ordinal = 1))
    private Object onGetPotionSwirlsAmbient(SynchedEntityData instance, EntityDataAccessor<Boolean> data) {
        return this.bo$potionSwirlsAmbient;
    }

    @Overwrite
    public boolean isUsingItem() {
        return this.bo$isUsingItem;
    }

    @Overwrite
    public InteractionHand getUsedItemHand() {
        return this.bo$activeHand;
    }

    @Overwrite
    public boolean isAutoSpinAttack() {
        return this.bo$isUsingRiptide;
    }

    @Overwrite
    public float getHealth() {
        return this.bo$health;
    }

    @Overwrite
    public final int getArrowCount() {
        return this.bo$stuckArrowCount;
    }

    @Overwrite
    public final int getStingerCount() {
        return this.bo$stingerCount;
    }

    @Overwrite
    public Optional<BlockPos> getSleepingPos() {
        return this.bo$sleepingPosition;
    }

    @Override
    public void bo$refreshEntityData(int data) {
        super.bo$refreshEntityData(data);
        if (data == DATA_LIVING_ENTITY_FLAGS.getId()) {
            this.bo$isUsingItem = (this.f_19804_.get(DATA_LIVING_ENTITY_FLAGS) & 1) > 0;
            this.bo$activeHand = (this.f_19804_.get(DATA_LIVING_ENTITY_FLAGS) & 2) > 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            this.bo$isUsingRiptide = (this.f_19804_.get(DATA_LIVING_ENTITY_FLAGS) & 4) != 0;
        } else if (data == DATA_HEALTH_ID.getId()) {
            this.bo$health = this.f_19804_.get(DATA_HEALTH_ID);
        } else if (data == DATA_EFFECT_COLOR_ID.getId()) {
            this.bo$potionSwirlsColor = this.f_19804_.get(DATA_EFFECT_COLOR_ID);
        } else if (data == DATA_EFFECT_AMBIENCE_ID.getId()) {
            this.bo$potionSwirlsAmbient = this.f_19804_.get(DATA_EFFECT_AMBIENCE_ID);
        } else if (data == DATA_ARROW_COUNT_ID.getId()) {
            this.bo$stuckArrowCount = this.f_19804_.get(DATA_ARROW_COUNT_ID);
        } else if (data == DATA_STINGER_COUNT_ID.getId()) {
            this.bo$stingerCount = this.f_19804_.get(DATA_STINGER_COUNT_ID);
        } else if (data == SLEEPING_POS_ID.getId()) {
            this.bo$sleepingPosition = this.f_19804_.get(SLEEPING_POS_ID);
        }
    }
}
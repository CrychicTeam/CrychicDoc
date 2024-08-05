package com.almostreliable.lootjs.predicate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobType;

public class ExtendedEntityFlagsPredicate extends EntityFlagsPredicate {

    @Nullable
    protected final Boolean isInWater;

    @Nullable
    protected final Boolean isUnderWater;

    @Nullable
    protected final Boolean isMonster;

    @Nullable
    protected final Boolean isCreature;

    @Nullable
    protected final Boolean isOnGround;

    @Nullable
    protected final Boolean isUndeadMob;

    @Nullable
    protected final Boolean isArthropodMob;

    @Nullable
    protected final Boolean isIllegarMob;

    @Nullable
    protected final Boolean isWaterMob;

    public ExtendedEntityFlagsPredicate(@Nullable Boolean isOnFire, @Nullable Boolean isCrouching, @Nullable Boolean isSprinting, @Nullable Boolean isSwimming, @Nullable Boolean isBaby, @Nullable Boolean isInWater, @Nullable Boolean isUnderWater, @Nullable Boolean isMonster, @Nullable Boolean isCreature, @Nullable Boolean isOnGround, @Nullable Boolean isUndeadMob, @Nullable Boolean isArthropodMob, @Nullable Boolean isIllegarMob, @Nullable Boolean isWaterMob) {
        super(isOnFire, isCrouching, isSprinting, isSwimming, isBaby);
        this.isInWater = isInWater;
        this.isUnderWater = isUnderWater;
        this.isMonster = isMonster;
        this.isCreature = isCreature;
        this.isOnGround = isOnGround;
        this.isUndeadMob = isUndeadMob;
        this.isArthropodMob = isArthropodMob;
        this.isIllegarMob = isIllegarMob;
        this.isWaterMob = isWaterMob;
    }

    @Override
    public boolean matches(Entity entity) {
        return super.matches(entity) && this.matchesInWater(entity) && this.matchesUnderWater(entity) && this.matchesMonster(entity) && this.matchesCreature(entity) && this.matchesOnGround(entity) && this.matchesMobTypes(entity);
    }

    private boolean matchesMobTypes(Entity entity) {
        return this.matchesMobType(this.isUndeadMob, entity, MobType.UNDEAD) && this.matchesMobType(this.isArthropodMob, entity, MobType.ARTHROPOD) && this.matchesMobType(this.isIllegarMob, entity, MobType.ILLAGER) && this.matchesMobType(this.isWaterMob, entity, MobType.WATER);
    }

    private boolean matchesMobType(@Nullable Boolean flag, Entity entity, MobType mobType) {
        if (flag == null) {
            return true;
        } else {
            return entity instanceof LivingEntity ? flag == (((LivingEntity) entity).getMobType() == mobType) : false;
        }
    }

    private boolean matchesOnGround(Entity entity) {
        return this.isOnGround == null || this.isOnGround == entity.onGround();
    }

    private boolean matchesCreature(Entity entity) {
        return this.isCreature == null || entity.getType().getCategory() == MobCategory.CREATURE == this.isCreature;
    }

    private boolean matchesMonster(Entity entity) {
        return this.isMonster == null || entity.getType().getCategory() == MobCategory.MONSTER == this.isMonster;
    }

    private boolean matchesUnderWater(Entity entity) {
        return this.isUnderWater == null || this.isUnderWater == entity.isUnderWater();
    }

    private boolean matchesInWater(Entity entity) {
        return this.isInWater == null || this.isInWater == entity.isInWater();
    }

    @Override
    public JsonElement serializeToJson() {
        JsonElement jsonElement = super.serializeToJson();
        JsonObject jsonobject = jsonElement.getAsJsonObject();
        this.addOptionalBoolean(jsonobject, "isInWater", this.isInWater);
        this.addOptionalBoolean(jsonobject, "isUnderWater", this.isUnderWater);
        this.addOptionalBoolean(jsonobject, "isMonster", this.isMonster);
        this.addOptionalBoolean(jsonobject, "isCreature", this.isCreature);
        this.addOptionalBoolean(jsonobject, "isOnGround", this.isOnGround);
        this.addOptionalBoolean(jsonobject, "isUndeadMob", this.isUndeadMob);
        this.addOptionalBoolean(jsonobject, "isArthropodMob", this.isArthropodMob);
        this.addOptionalBoolean(jsonobject, "isIllegarMob", this.isIllegarMob);
        this.addOptionalBoolean(jsonobject, "isWaterMob", this.isWaterMob);
        return jsonobject;
    }

    private void addOptionalBoolean(JsonObject pJson, String pName, @Nullable Boolean pValue) {
        if (pValue != null) {
            pJson.addProperty(pName, pValue);
        }
    }

    public static class Builder extends EntityFlagsPredicate.Builder implements ExtendedEntityFlagsPredicate.IBuilder<ExtendedEntityFlagsPredicate> {

        @Nullable
        protected Boolean isInWater;

        @Nullable
        protected Boolean isUnderWater;

        @Nullable
        protected Boolean isMonster;

        @Nullable
        protected Boolean isCreature;

        @Nullable
        protected Boolean isOnGround;

        @Nullable
        protected Boolean isUndeadMob;

        @Nullable
        protected Boolean isArthropodMob;

        @Nullable
        protected Boolean isIllegarMob;

        @Nullable
        protected Boolean isWaterMob;

        public ExtendedEntityFlagsPredicate build() {
            return new ExtendedEntityFlagsPredicate(this.f_33707_, this.f_33708_, this.f_33709_, this.f_33710_, this.f_33711_, this.isInWater, this.isUnderWater, this.isMonster, this.isCreature, this.isOnGround, this.isUndeadMob, this.isArthropodMob, this.isIllegarMob, this.isWaterMob);
        }

        public ExtendedEntityFlagsPredicate.Builder isOnFire(boolean flag) {
            this.f_33707_ = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isCrouching(boolean flag) {
            this.f_33708_ = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isSprinting(boolean flag) {
            this.f_33709_ = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isSwimming(boolean flag) {
            this.f_33710_ = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isBaby(boolean flag) {
            this.f_33711_ = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isInWater(boolean flag) {
            this.isInWater = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isUnderWater(boolean flag) {
            this.isUnderWater = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isMonster(boolean flag) {
            this.isMonster = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isCreature(boolean flag) {
            this.isCreature = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isOnGround(boolean flag) {
            this.isOnGround = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isUndeadMob(boolean flag) {
            this.isUndeadMob = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isArthropodMob(boolean flag) {
            this.isArthropodMob = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isIllegarMob(boolean flag) {
            this.isIllegarMob = flag;
            return this;
        }

        public ExtendedEntityFlagsPredicate.Builder isWaterMob(boolean flag) {
            this.isWaterMob = flag;
            return this;
        }
    }

    public interface IBuilder<T> {

        T build();

        ExtendedEntityFlagsPredicate.IBuilder<T> isOnFire(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isCrouching(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isSprinting(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isSwimming(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isBaby(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isInWater(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isUnderWater(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isMonster(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isCreature(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isOnGround(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isUndeadMob(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isArthropodMob(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isIllegarMob(boolean var1);

        ExtendedEntityFlagsPredicate.IBuilder<T> isWaterMob(boolean var1);
    }
}
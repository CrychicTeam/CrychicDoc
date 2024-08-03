package net.minecraft.world.damagesource;

import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DamageSource {

    private final Holder<DamageType> type;

    @Nullable
    private final Entity causingEntity;

    @Nullable
    private final Entity directEntity;

    @Nullable
    private final Vec3 damageSourcePosition;

    public String toString() {
        return "DamageSource (" + this.type().msgId() + ")";
    }

    public float getFoodExhaustion() {
        return this.type().exhaustion();
    }

    public boolean isIndirect() {
        return this.causingEntity != this.directEntity;
    }

    private DamageSource(Holder<DamageType> holderDamageType0, @Nullable Entity entity1, @Nullable Entity entity2, @Nullable Vec3 vec3) {
        this.type = holderDamageType0;
        this.causingEntity = entity2;
        this.directEntity = entity1;
        this.damageSourcePosition = vec3;
    }

    public DamageSource(Holder<DamageType> holderDamageType0, @Nullable Entity entity1, @Nullable Entity entity2) {
        this(holderDamageType0, entity1, entity2, null);
    }

    public DamageSource(Holder<DamageType> holderDamageType0, Vec3 vec1) {
        this(holderDamageType0, null, null, vec1);
    }

    public DamageSource(Holder<DamageType> holderDamageType0, @Nullable Entity entity1) {
        this(holderDamageType0, entity1, entity1);
    }

    public DamageSource(Holder<DamageType> holderDamageType0) {
        this(holderDamageType0, null, null, null);
    }

    @Nullable
    public Entity getDirectEntity() {
        return this.directEntity;
    }

    @Nullable
    public Entity getEntity() {
        return this.causingEntity;
    }

    public Component getLocalizedDeathMessage(LivingEntity livingEntity0) {
        String $$1 = "death.attack." + this.type().msgId();
        if (this.causingEntity == null && this.directEntity == null) {
            LivingEntity $$5 = livingEntity0.getKillCredit();
            String $$6 = $$1 + ".player";
            return $$5 != null ? Component.translatable($$6, livingEntity0.m_5446_(), $$5.m_5446_()) : Component.translatable($$1, livingEntity0.m_5446_());
        } else {
            Component $$2 = this.causingEntity == null ? this.directEntity.getDisplayName() : this.causingEntity.getDisplayName();
            ItemStack $$4 = this.causingEntity instanceof LivingEntity $$3 ? $$3.getMainHandItem() : ItemStack.EMPTY;
            return !$$4.isEmpty() && $$4.hasCustomHoverName() ? Component.translatable($$1 + ".item", livingEntity0.m_5446_(), $$2, $$4.getDisplayName()) : Component.translatable($$1, livingEntity0.m_5446_(), $$2);
        }
    }

    public String getMsgId() {
        return this.type().msgId();
    }

    public boolean scalesWithDifficulty() {
        return switch(this.type().scaling()) {
            case NEVER ->
                false;
            case WHEN_CAUSED_BY_LIVING_NON_PLAYER ->
                this.causingEntity instanceof LivingEntity && !(this.causingEntity instanceof Player);
            case ALWAYS ->
                true;
        };
    }

    public boolean isCreativePlayer() {
        if (this.getEntity() instanceof Player $$0 && $$0.getAbilities().instabuild) {
            return true;
        }
        return false;
    }

    @Nullable
    public Vec3 getSourcePosition() {
        if (this.damageSourcePosition != null) {
            return this.damageSourcePosition;
        } else {
            return this.directEntity != null ? this.directEntity.position() : null;
        }
    }

    @Nullable
    public Vec3 sourcePositionRaw() {
        return this.damageSourcePosition;
    }

    public boolean is(TagKey<DamageType> tagKeyDamageType0) {
        return this.type.is(tagKeyDamageType0);
    }

    public boolean is(ResourceKey<DamageType> resourceKeyDamageType0) {
        return this.type.is(resourceKeyDamageType0);
    }

    public DamageType type() {
        return this.type.value();
    }

    public Holder<DamageType> typeHolder() {
        return this.type;
    }
}
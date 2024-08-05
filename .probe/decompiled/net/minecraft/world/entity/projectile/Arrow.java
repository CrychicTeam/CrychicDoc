package net.minecraft.world.entity.projectile;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

public class Arrow extends AbstractArrow {

    private static final int EXPOSED_POTION_DECAY_TIME = 600;

    private static final int NO_EFFECT_COLOR = -1;

    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR = SynchedEntityData.defineId(Arrow.class, EntityDataSerializers.INT);

    private static final byte EVENT_POTION_PUFF = 0;

    private Potion potion = Potions.EMPTY;

    private final Set<MobEffectInstance> effects = Sets.newHashSet();

    private boolean fixedColor;

    public Arrow(EntityType<? extends Arrow> entityTypeExtendsArrow0, Level level1) {
        super(entityTypeExtendsArrow0, level1);
    }

    public Arrow(Level level0, double double1, double double2, double double3) {
        super(EntityType.ARROW, double1, double2, double3, level0);
    }

    public Arrow(Level level0, LivingEntity livingEntity1) {
        super(EntityType.ARROW, livingEntity1, level0);
    }

    public void setEffectsFromItem(ItemStack itemStack0) {
        if (itemStack0.is(Items.TIPPED_ARROW)) {
            this.potion = PotionUtils.getPotion(itemStack0);
            Collection<MobEffectInstance> $$1 = PotionUtils.getCustomEffects(itemStack0);
            if (!$$1.isEmpty()) {
                for (MobEffectInstance $$2 : $$1) {
                    this.effects.add(new MobEffectInstance($$2));
                }
            }
            int $$3 = getCustomColor(itemStack0);
            if ($$3 == -1) {
                this.updateColor();
            } else {
                this.setFixedColor($$3);
            }
        } else if (itemStack0.is(Items.ARROW)) {
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.f_19804_.set(ID_EFFECT_COLOR, -1);
        }
    }

    public static int getCustomColor(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        return $$1 != null && $$1.contains("CustomPotionColor", 99) ? $$1.getInt("CustomPotionColor") : -1;
    }

    private void updateColor() {
        this.fixedColor = false;
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.f_19804_.set(ID_EFFECT_COLOR, -1);
        } else {
            this.f_19804_.set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
        }
    }

    public void addEffect(MobEffectInstance mobEffectInstance0) {
        this.effects.add(mobEffectInstance0);
        this.m_20088_().set(ID_EFFECT_COLOR, PotionUtils.getColor(PotionUtils.getAllEffects(this.potion, this.effects)));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ID_EFFECT_COLOR, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide) {
            if (this.f_36703_) {
                if (this.f_36704_ % 5 == 0) {
                    this.makeParticle(1);
                }
            } else {
                this.makeParticle(2);
            }
        } else if (this.f_36703_ && this.f_36704_ != 0 && !this.effects.isEmpty() && this.f_36704_ >= 600) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 0);
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.f_19804_.set(ID_EFFECT_COLOR, -1);
        }
    }

    private void makeParticle(int int0) {
        int $$1 = this.getColor();
        if ($$1 != -1 && int0 > 0) {
            double $$2 = (double) ($$1 >> 16 & 0xFF) / 255.0;
            double $$3 = (double) ($$1 >> 8 & 0xFF) / 255.0;
            double $$4 = (double) ($$1 >> 0 & 0xFF) / 255.0;
            for (int $$5 = 0; $$5 < int0; $$5++) {
                this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), $$2, $$3, $$4);
            }
        }
    }

    public int getColor() {
        return this.f_19804_.get(ID_EFFECT_COLOR);
    }

    private void setFixedColor(int int0) {
        this.fixedColor = true;
        this.f_19804_.set(ID_EFFECT_COLOR, int0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        if (this.potion != Potions.EMPTY) {
            compoundTag0.putString("Potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
        }
        if (this.fixedColor) {
            compoundTag0.putInt("Color", this.getColor());
        }
        if (!this.effects.isEmpty()) {
            ListTag $$1 = new ListTag();
            for (MobEffectInstance $$2 : this.effects) {
                $$1.add($$2.save(new CompoundTag()));
            }
            compoundTag0.put("CustomPotionEffects", $$1);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("Potion", 8)) {
            this.potion = PotionUtils.getPotion(compoundTag0);
        }
        for (MobEffectInstance $$1 : PotionUtils.getCustomEffects(compoundTag0)) {
            this.addEffect($$1);
        }
        if (compoundTag0.contains("Color", 99)) {
            this.setFixedColor(compoundTag0.getInt("Color"));
        } else {
            this.updateColor();
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity livingEntity0) {
        super.doPostHurtEffects(livingEntity0);
        Entity $$1 = this.m_150173_();
        for (MobEffectInstance $$2 : this.potion.getEffects()) {
            livingEntity0.addEffect(new MobEffectInstance($$2.getEffect(), Math.max($$2.mapDuration(p_268168_ -> p_268168_ / 8), 1), $$2.getAmplifier(), $$2.isAmbient(), $$2.isVisible()), $$1);
        }
        if (!this.effects.isEmpty()) {
            for (MobEffectInstance $$3 : this.effects) {
                livingEntity0.addEffect($$3, $$1);
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
            return new ItemStack(Items.ARROW);
        } else {
            ItemStack $$0 = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.setPotion($$0, this.potion);
            PotionUtils.setCustomEffects($$0, this.effects);
            if (this.fixedColor) {
                $$0.getOrCreateTag().putInt("CustomPotionColor", this.getColor());
            }
            return $$0;
        }
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 0) {
            int $$1 = this.getColor();
            if ($$1 != -1) {
                double $$2 = (double) ($$1 >> 16 & 0xFF) / 255.0;
                double $$3 = (double) ($$1 >> 8 & 0xFF) / 255.0;
                double $$4 = (double) ($$1 >> 0 & 0xFF) / 255.0;
                for (int $$5 = 0; $$5 < 20; $$5++) {
                    this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), $$2, $$3, $$4);
                }
            }
        } else {
            super.m_7822_(byte0);
        }
    }
}
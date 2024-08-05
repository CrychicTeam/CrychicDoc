package net.minecraft.world.entity.animal.horse;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SoundType;

public class Horse extends AbstractHorse implements VariantHolder<Variant> {

    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(Horse.class, EntityDataSerializers.INT);

    public Horse(EntityType<? extends Horse> entityTypeExtendsHorse0, Level level1) {
        super(entityTypeExtendsHorse0, level1);
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource0) {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) m_271722_(randomSource0::m_188503_));
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(m_271981_(randomSource0::m_188500_));
        this.m_21051_(Attributes.JUMP_STRENGTH).setBaseValue(m_272017_(randomSource0::m_188500_));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_ID_TYPE_VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("Variant", this.getTypeVariant());
        if (!this.f_30520_.getItem(1).isEmpty()) {
            compoundTag0.put("ArmorItem", this.f_30520_.getItem(1).save(new CompoundTag()));
        }
    }

    public ItemStack getArmor() {
        return this.m_6844_(EquipmentSlot.CHEST);
    }

    private void setArmor(ItemStack itemStack0) {
        this.m_8061_(EquipmentSlot.CHEST, itemStack0);
        this.m_21409_(EquipmentSlot.CHEST, 0.0F);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.setTypeVariant(compoundTag0.getInt("Variant"));
        if (compoundTag0.contains("ArmorItem", 10)) {
            ItemStack $$1 = ItemStack.of(compoundTag0.getCompound("ArmorItem"));
            if (!$$1.isEmpty() && this.isArmor($$1)) {
                this.f_30520_.setItem(1, $$1);
            }
        }
        this.updateContainerEquipment();
    }

    private void setTypeVariant(int int0) {
        this.f_19804_.set(DATA_ID_TYPE_VARIANT, int0);
    }

    private int getTypeVariant() {
        return this.f_19804_.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariantAndMarkings(Variant variant0, Markings markings1) {
        this.setTypeVariant(variant0.getId() & 0xFF | markings1.getId() << 8 & 0xFF00);
    }

    public Variant getVariant() {
        return Variant.byId(this.getTypeVariant() & 0xFF);
    }

    public void setVariant(Variant variant0) {
        this.setTypeVariant(variant0.getId() & 0xFF | this.getTypeVariant() & -256);
    }

    public Markings getMarkings() {
        return Markings.byId((this.getTypeVariant() & 0xFF00) >> 8);
    }

    @Override
    protected void updateContainerEquipment() {
        if (!this.m_9236_().isClientSide) {
            super.updateContainerEquipment();
            this.setArmorEquipment(this.f_30520_.getItem(1));
            this.m_21409_(EquipmentSlot.CHEST, 0.0F);
        }
    }

    private void setArmorEquipment(ItemStack itemStack0) {
        this.setArmor(itemStack0);
        if (!this.m_9236_().isClientSide) {
            this.m_21051_(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            if (this.isArmor(itemStack0)) {
                int $$1 = ((HorseArmorItem) itemStack0.getItem()).getProtection();
                if ($$1 != 0) {
                    this.m_21051_(Attributes.ARMOR).addTransientModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Horse armor bonus", (double) $$1, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }

    @Override
    public void containerChanged(Container container0) {
        ItemStack $$1 = this.getArmor();
        super.containerChanged(container0);
        ItemStack $$2 = this.getArmor();
        if (this.f_19797_ > 20 && this.isArmor($$2) && $$1 != $$2) {
            this.m_5496_(SoundEvents.HORSE_ARMOR, 0.5F, 1.0F);
        }
    }

    @Override
    protected void playGallopSound(SoundType soundType0) {
        super.playGallopSound(soundType0);
        if (this.f_19796_.nextInt(10) == 0) {
            this.m_5496_(SoundEvents.HORSE_BREATHE, soundType0.getVolume() * 0.6F, soundType0.getPitch());
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HORSE_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.HORSE_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.HORSE_HURT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.HORSE_ANGRY;
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        boolean $$2 = !this.m_6162_() && this.m_30614_() && player0.isSecondaryUseActive();
        if (!this.m_20160_() && !$$2) {
            ItemStack $$3 = player0.m_21120_(interactionHand1);
            if (!$$3.isEmpty()) {
                if (this.m_6898_($$3)) {
                    return this.m_30580_(player0, $$3);
                }
                if (!this.m_30614_()) {
                    this.m_7564_();
                    return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
                }
            }
            return super.mobInteract(player0, interactionHand1);
        } else {
            return super.mobInteract(player0, interactionHand1);
        }
    }

    @Override
    public boolean canMate(Animal animal0) {
        if (animal0 == this) {
            return false;
        } else {
            return !(animal0 instanceof Donkey) && !(animal0 instanceof Horse) ? false : this.m_30628_() && ((AbstractHorse) animal0).canParent();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        if (ageableMob1 instanceof Donkey) {
            Mule $$2 = EntityType.MULE.create(serverLevel0);
            if ($$2 != null) {
                this.m_149508_(ageableMob1, $$2);
            }
            return $$2;
        } else {
            Horse $$3 = (Horse) ageableMob1;
            Horse $$4 = EntityType.HORSE.create(serverLevel0);
            if ($$4 != null) {
                int $$5 = this.f_19796_.nextInt(9);
                Variant $$6;
                if ($$5 < 4) {
                    $$6 = this.getVariant();
                } else if ($$5 < 8) {
                    $$6 = $$3.getVariant();
                } else {
                    $$6 = Util.getRandom(Variant.values(), this.f_19796_);
                }
                int $$9 = this.f_19796_.nextInt(5);
                Markings $$10;
                if ($$9 < 2) {
                    $$10 = this.getMarkings();
                } else if ($$9 < 4) {
                    $$10 = $$3.getMarkings();
                } else {
                    $$10 = Util.getRandom(Markings.values(), this.f_19796_);
                }
                $$4.setVariantAndMarkings($$6, $$10);
                this.m_149508_(ageableMob1, $$4);
            }
            return $$4;
        }
    }

    @Override
    public boolean canWearArmor() {
        return true;
    }

    @Override
    public boolean isArmor(ItemStack itemStack0) {
        return itemStack0.getItem() instanceof HorseArmorItem;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        Variant $$6;
        if (spawnGroupData3 instanceof Horse.HorseGroupData) {
            $$6 = ((Horse.HorseGroupData) spawnGroupData3).variant;
        } else {
            $$6 = Util.getRandom(Variant.values(), $$5);
            spawnGroupData3 = new Horse.HorseGroupData($$6);
        }
        this.setVariantAndMarkings($$6, Util.getRandom(Markings.values(), $$5));
        return super.finalizeSpawn(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    public static class HorseGroupData extends AgeableMob.AgeableMobGroupData {

        public final Variant variant;

        public HorseGroupData(Variant variant0) {
            super(true);
            this.variant = variant0;
        }
    }
}
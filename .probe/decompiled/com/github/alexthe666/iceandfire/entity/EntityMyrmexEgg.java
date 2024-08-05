package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.collect.ImmutableList;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EntityMyrmexEgg extends LivingEntity implements IBlacklistedFromStatues, IDeadMob {

    private static final EntityDataAccessor<Boolean> MYRMEX_TYPE = SynchedEntityData.defineId(EntityMyrmexEgg.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> MYRMEX_AGE = SynchedEntityData.defineId(EntityMyrmexEgg.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> MYRMEX_CASTE = SynchedEntityData.defineId(EntityMyrmexEgg.class, EntityDataSerializers.INT);

    public UUID hiveUUID;

    public EntityMyrmexEgg(EntityType t, Level worldIn) {
        super(t, worldIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Jungle", this.isJungle());
        tag.putInt("MyrmexAge", this.getMyrmexAge());
        tag.putInt("MyrmexCaste", this.getMyrmexCaste());
        tag.putUUID("HiveUUID", this.hiveUUID == null ? (this.hiveUUID = UUID.randomUUID()) : this.hiveUUID);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setJungle(tag.getBoolean("Jungle"));
        this.setMyrmexAge(tag.getInt("MyrmexAge"));
        this.setMyrmexCaste(tag.getInt("MyrmexCaste"));
        this.hiveUUID = tag.getUUID("HiveUUID");
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(MYRMEX_TYPE, false);
        this.m_20088_().define(MYRMEX_AGE, 0);
        this.m_20088_().define(MYRMEX_CASTE, 0);
    }

    public boolean isJungle() {
        return this.m_20088_().get(MYRMEX_TYPE);
    }

    public void setJungle(boolean jungle) {
        this.m_20088_().set(MYRMEX_TYPE, jungle);
    }

    public int getMyrmexAge() {
        return this.m_20088_().get(MYRMEX_AGE);
    }

    public void setMyrmexAge(int i) {
        this.m_20088_().set(MYRMEX_AGE, i);
    }

    public int getMyrmexCaste() {
        return this.m_20088_().get(MYRMEX_CASTE);
    }

    public void setMyrmexCaste(int i) {
        this.m_20088_().set(MYRMEX_CASTE, i);
    }

    public boolean canSeeSky() {
        return this.m_9236_().m_46861_(this.m_20183_());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.canSeeSky()) {
            this.setMyrmexAge(this.getMyrmexAge() + 1);
        }
        if (this.getMyrmexAge() > IafConfig.myrmexEggTicks) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            EntityMyrmexBase myrmex = switch(this.getMyrmexCaste()) {
                case 1 ->
                    new EntityMyrmexSoldier(IafEntityRegistry.MYRMEX_SOLDIER.get(), this.m_9236_());
                case 2 ->
                    new EntityMyrmexRoyal(IafEntityRegistry.MYRMEX_ROYAL.get(), this.m_9236_());
                case 3 ->
                    new EntityMyrmexSentinel(IafEntityRegistry.MYRMEX_SENTINEL.get(), this.m_9236_());
                case 4 ->
                    new EntityMyrmexQueen(IafEntityRegistry.MYRMEX_QUEEN.get(), this.m_9236_());
                default ->
                    new EntityMyrmexWorker(IafEntityRegistry.MYRMEX_WORKER.get(), this.m_9236_());
            };
            myrmex.setJungleVariant(this.isJungle());
            myrmex.setGrowthStage(0);
            myrmex.m_19890_(this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0F, 0.0F);
            if (myrmex instanceof EntityMyrmexQueen) {
                MyrmexHive hive = new MyrmexHive(this.m_9236_(), this.m_20183_(), 100);
                Player player = this.m_9236_().m_45930_(this, 30.0);
                if (player != null) {
                    hive.hasOwner = true;
                    hive.ownerUUID = player.m_20148_();
                    if (!this.m_9236_().isClientSide) {
                        hive.modifyPlayerReputation(player.m_20148_(), 100);
                    }
                }
                MyrmexWorldData.addHive(this.m_9236_(), hive);
                myrmex.setHive(hive);
            } else if (MyrmexWorldData.get(this.m_9236_()) != null) {
                MyrmexHive hive;
                if (this.hiveUUID == null) {
                    hive = MyrmexWorldData.get(this.m_9236_()).getNearestHive(this.m_20183_(), 400);
                } else {
                    hive = MyrmexWorldData.get(this.m_9236_()).getHiveFromUUID(this.hiveUUID);
                }
                if (!this.m_9236_().isClientSide && hive != null && Math.sqrt(this.m_20275_((double) hive.getCenter().m_123341_(), (double) hive.getCenter().m_123342_(), (double) hive.getCenter().m_123343_())) < 2000.0) {
                    myrmex.setHive(hive);
                }
            }
            if (!this.m_9236_().isClientSide) {
                this.m_9236_().m_7967_(myrmex);
            }
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_(), IafSoundRegistry.EGG_HATCH, this.m_5720_(), 2.5F, 1.0F, false);
        }
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @NotNull
    @Override
    public ItemStack getItemBySlot(@NotNull EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slotIn, @NotNull ItemStack stack) {
    }

    @Override
    public boolean hurt(@NotNull DamageSource dmg, float var2) {
        if (!dmg.is(DamageTypes.IN_WALL) && !dmg.is(DamageTypes.FALL)) {
            if (!this.m_9236_().isClientSide && !dmg.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                this.m_5552_(this.getItem(), 0.0F);
            }
            this.m_142687_(Entity.RemovalReason.KILLED);
            return super.hurt(dmg, var2);
        } else {
            return false;
        }
    }

    private ItemStack getItem() {
        ItemStack egg = new ItemStack(this.isJungle() ? IafItemRegistry.MYRMEX_JUNGLE_EGG.get() : IafItemRegistry.MYRMEX_DESERT_EGG.get(), 1);
        CompoundTag newTag = new CompoundTag();
        newTag.putInt("EggOrdinal", this.getMyrmexCaste());
        egg.setTag(newTag);
        return egg;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @NotNull
    @Override
    public HumanoidArm getMainArm() {
        return null;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    public void onPlayerPlace(Player player) {
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    public boolean isInNursery() {
        MyrmexHive hive = MyrmexWorldData.get(this.m_9236_()).getNearestHive(this.m_20183_(), 100);
        if (hive != null && hive.getRooms(WorldGenMyrmexHive.RoomType.NURSERY).isEmpty() && hive.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.m_217043_(), this.m_20183_()) != null) {
            return false;
        } else if (hive != null) {
            BlockPos nursery = hive.getRandomRoom(WorldGenMyrmexHive.RoomType.NURSERY, this.m_217043_(), this.m_20183_());
            return this.m_20275_((double) nursery.m_123341_(), (double) nursery.m_123342_(), (double) nursery.m_123343_()) < 2025.0;
        } else {
            return false;
        }
    }
}
package org.violetmoon.quark.content.mobs.entity;

import com.google.common.collect.ImmutableSet;
import java.util.stream.Stream;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.mobs.module.ForgottenModule;
import org.violetmoon.quark.content.tools.base.RuneColor;
import org.violetmoon.quark.content.tools.module.ColorRunesModule;

public class Forgotten extends Skeleton {

    public static final EntityDataAccessor<ItemStack> SHEATHED_ITEM = SynchedEntityData.defineId(Forgotten.class, EntityDataSerializers.ITEM_STACK);

    public static final ResourceLocation FORGOTTEN_LOOT_TABLE = new ResourceLocation("quark", "entities/forgotten");

    public Forgotten(EntityType<? extends Forgotten> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(SHEATHED_ITEM, ItemStack.EMPTY);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 60.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        SpawnGroupData ilivingentitydata = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.m_32164_();
        return ilivingentitydata;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide) {
            LivingEntity target = this.m_5448_();
            boolean shouldUseBow = target == null;
            if (!shouldUseBow) {
                MobEffectInstance eff = target.getEffect(MobEffects.BLINDNESS);
                shouldUseBow = eff == null || eff.getDuration() < 20;
            }
            boolean isUsingBow = this.m_21205_().getItem() instanceof BowItem;
            if (shouldUseBow != isUsingBow) {
                this.swap();
            }
        }
        double w = (double) (this.m_20205_() * 2.0F);
        double h = (double) this.m_20206_();
        this.m_9236_().addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, this.m_20185_() + Math.random() * w - w / 2.0, this.m_20186_() + Math.random() * h, this.m_20189_() + Math.random() * w - w / 2.0, 0.0, 0.0, 0.0);
    }

    private void swap() {
        ItemStack curr = this.m_21205_();
        ItemStack off = this.f_19804_.get(SHEATHED_ITEM);
        this.m_21008_(InteractionHand.MAIN_HAND, off);
        this.f_19804_.set(SHEATHED_ITEM, curr);
        Stream<WrappedGoal> stream = this.f_21345_.getRunningGoals();
        stream.map(WrappedGoal::m_26015_).filter(g -> g instanceof MeleeAttackGoal || g instanceof RangedBowAttackGoal).forEach(Goal::m_8041_);
        this.m_32164_();
    }

    @NotNull
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return FORGOTTEN_LOOT_TABLE;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        CompoundTag sheathed = new CompoundTag();
        this.f_19804_.get(SHEATHED_ITEM).save(sheathed);
        compound.put("sheathed", sheathed);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CompoundTag sheathed = compound.getCompound("sheathed");
        this.f_19804_.set(SHEATHED_ITEM, ItemStack.of(sheathed));
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose poseIn, @NotNull EntityDimensions sizeIn) {
        return 2.1F;
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource source, int looting, boolean recentlyHitIn) {
    }

    @Override
    public boolean canPickUpLoot() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NotNull RandomSource rand, @NotNull DifficultyInstance difficulty) {
        super.m_213945_(rand, difficulty);
        this.prepareEquipment();
    }

    public void prepareEquipment() {
        ItemStack bow = new ItemStack(Items.BOW);
        ItemStack sheathed = new ItemStack(Items.IRON_SWORD);
        EnchantmentHelper.enchantItem(this.f_19796_, bow, 20, false);
        EnchantmentHelper.enchantItem(this.f_19796_, sheathed, 20, false);
        if (Quark.ZETA.modules.isEnabled(ColorRunesModule.class) && this.f_19796_.nextBoolean()) {
            DyeColor color = DyeColor.values()[this.f_19796_.nextInt(DyeColor.values().length)];
            RuneColor rune = RuneColor.byDyeColor(color);
            if (rune != null) {
                ColorRunesModule.withRune(bow, rune);
                ColorRunesModule.withRune(sheathed, rune);
            }
        }
        this.m_8061_(EquipmentSlot.MAINHAND, bow);
        this.f_19804_.set(SHEATHED_ITEM, sheathed);
        this.m_8061_(EquipmentSlot.HEAD, new ItemStack(ForgottenModule.forgotten_hat));
    }

    @NotNull
    @Override
    protected AbstractArrow getArrow(@NotNull ItemStack arrowStack, float distanceFactor) {
        AbstractArrow arrow = super.m_7932_(arrowStack, distanceFactor);
        if (arrow instanceof Arrow arrowInstance) {
            ItemStack stack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.setCustomEffects(stack, ImmutableSet.of(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0)));
            arrowInstance.setEffectsFromItem(stack);
        }
        return arrow;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
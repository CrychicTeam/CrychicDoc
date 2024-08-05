package net.minecraft.world.entity.animal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class Cow extends Animal {

    public Cow(EntityType<? extends Cow> entityTypeExtendsCow0, Level level1) {
        super(entityTypeExtendsCow0, level1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PanicGoal(this, 2.0));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.WHEAT), false));
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.25));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        if ($$2.is(Items.BUCKET) && !this.m_6162_()) {
            player0.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack $$3 = ItemUtils.createFilledResult($$2, player0, Items.MILK_BUCKET.getDefaultInstance());
            player0.m_21008_(interactionHand1, $$3);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.mobInteract(player0, interactionHand1);
        }
    }

    @Nullable
    public Cow getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.COW.create(serverLevel0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return this.m_6162_() ? entityDimensions1.height * 0.95F : 1.3F;
    }
}
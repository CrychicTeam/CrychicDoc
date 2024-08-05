package com.mna.entities.summon;

import com.mna.tools.SummonUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;

public class SummonedSpectralHorse extends AbstractHorse {

    public SummonedSpectralHorse(EntityType<? extends AbstractHorse> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.JUMP_STRENGTH, 1.25);
    }

    @Override
    protected void randomizeAttributes(RandomSource randomSource) {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(20.0);
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35F);
        this.m_21051_(Attributes.JUMP_STRENGTH).setBaseValue(1.25);
        this.m_21051_(ForgeMod.ENTITY_GRAVITY.get()).setBaseValue(0.05F);
    }

    public float getStepHeight() {
        return 1.75F;
    }

    @Override
    public void openCustomInventoryScreen(Player pPlayer) {
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    protected boolean canParent() {
        return false;
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState0) {
        return fluidState0.is(Fluids.WATER) && this.m_20156_().y > -0.2F && !this.m_5842_();
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player == SummonUtils.getSummoner(this)) {
            this.m_6835_(player);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public boolean causeFallDamage(float float0, float float1, DamageSource damageSource2) {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    protected void playJumpSound() {
    }
}
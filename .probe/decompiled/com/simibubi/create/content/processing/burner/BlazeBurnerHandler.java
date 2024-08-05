package com.simibubi.create.content.processing.burner;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class BlazeBurnerHandler {

    @SubscribeEvent
    public static void onThrowableImpact(ProjectileImpactEvent event) {
        thrownEggsGetEatenByBurner(event);
        splashExtinguishesBurner(event);
    }

    public static void thrownEggsGetEatenByBurner(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile instanceof ThrownEgg) {
            if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
                BlockEntity blockEntity = projectile.m_9236_().getBlockEntity(BlockPos.containing(event.getRayTraceResult().getLocation()));
                if (blockEntity instanceof BlazeBurnerBlockEntity) {
                    event.setCanceled(true);
                    projectile.m_20256_(Vec3.ZERO);
                    projectile.m_146870_();
                    Level world = projectile.m_9236_();
                    if (!world.isClientSide) {
                        BlazeBurnerBlockEntity heater = (BlazeBurnerBlockEntity) blockEntity;
                        if (!heater.isCreative() && heater.activeFuel != BlazeBurnerBlockEntity.FuelType.SPECIAL) {
                            heater.activeFuel = BlazeBurnerBlockEntity.FuelType.NORMAL;
                            heater.remainingBurnTime = Mth.clamp(heater.remainingBurnTime + 80, 0, 10000);
                            heater.updateBlockState();
                            heater.notifyUpdate();
                        }
                        AllSoundEvents.BLAZE_MUNCH.playOnServer(world, heater.m_58899_());
                    }
                }
            }
        }
    }

    public static void splashExtinguishesBurner(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (!projectile.m_9236_().isClientSide) {
            if (projectile instanceof ThrownPotion entity) {
                if (event.getRayTraceResult().getType() == HitResult.Type.BLOCK) {
                    ItemStack stack = entity.m_7846_();
                    Potion potion = PotionUtils.getPotion(stack);
                    if (potion == Potions.WATER && PotionUtils.getMobEffects(stack).isEmpty()) {
                        BlockHitResult result = (BlockHitResult) event.getRayTraceResult();
                        Level world = entity.m_9236_();
                        Direction face = result.getDirection();
                        BlockPos pos = result.getBlockPos().relative(face);
                        extinguishLitBurners(world, pos, face);
                        extinguishLitBurners(world, pos.relative(face.getOpposite()), face);
                        for (Direction face1 : Direction.Plane.HORIZONTAL) {
                            extinguishLitBurners(world, pos.relative(face1), face1);
                        }
                    }
                }
            }
        }
    }

    private static void extinguishLitBurners(Level world, BlockPos pos, Direction direction) {
        BlockState state = world.getBlockState(pos);
        if (AllBlocks.LIT_BLAZE_BURNER.has(state)) {
            world.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
            world.setBlockAndUpdate(pos, AllBlocks.BLAZE_BURNER.getDefaultState());
        }
    }
}
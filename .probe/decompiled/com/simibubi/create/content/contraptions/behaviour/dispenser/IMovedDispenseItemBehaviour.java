package com.simibubi.create.content.contraptions.behaviour.dispenser;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface IMovedDispenseItemBehaviour {

    static void initSpawnEggs() {
        IMovedDispenseItemBehaviour spawnEggDispenseBehaviour = new MovedDefaultDispenseItemBehaviour() {

            @Override
            protected ItemStack dispenseStack(ItemStack itemStack, MovementContext context, BlockPos pos, Vec3 facing) {
                if (!(itemStack.getItem() instanceof SpawnEggItem)) {
                    return super.dispenseStack(itemStack, context, pos, facing);
                } else {
                    if (context.world instanceof ServerLevel) {
                        EntityType<?> entityType = ((SpawnEggItem) itemStack.getItem()).getType(itemStack.getTag());
                        Entity spawnedEntity = entityType.spawn((ServerLevel) context.world, itemStack, null, pos.offset(BlockPos.containing(facing.x + 0.7, facing.y + 0.7, facing.z + 0.7)), MobSpawnType.DISPENSER, facing.y < 0.5, false);
                        if (spawnedEntity != null) {
                            spawnedEntity.setDeltaMovement(context.motion.scale(2.0));
                        }
                    }
                    itemStack.shrink(1);
                    return itemStack;
                }
            }
        };
        for (SpawnEggItem spawneggitem : SpawnEggItem.eggs()) {
            DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(spawneggitem, spawnEggDispenseBehaviour);
        }
    }

    static void init() {
        MovedProjectileDispenserBehaviour movedPotionDispenseItemBehaviour = new MovedProjectileDispenserBehaviour() {

            @Override
            protected Projectile getProjectileEntity(Level world, double x, double y, double z, ItemStack itemStack) {
                return Util.make(new ThrownPotion(world, x, y, z), p_218411_1_ -> p_218411_1_.m_37446_(itemStack));
            }

            @Override
            protected float getProjectileInaccuracy() {
                return super.getProjectileInaccuracy() * 0.5F;
            }

            @Override
            protected float getProjectileVelocity() {
                return super.getProjectileVelocity() * 0.5F;
            }
        };
        DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(Items.SPLASH_POTION, movedPotionDispenseItemBehaviour);
        DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(Items.LINGERING_POTION, movedPotionDispenseItemBehaviour);
        DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(Items.TNT, new MovedDefaultDispenseItemBehaviour() {

            @Override
            protected ItemStack dispenseStack(ItemStack itemStack, MovementContext context, BlockPos pos, Vec3 facing) {
                double x = (double) pos.m_123341_() + facing.x * 0.7 + 0.5;
                double y = (double) pos.m_123342_() + facing.y * 0.7 + 0.5;
                double z = (double) pos.m_123343_() + facing.z * 0.7 + 0.5;
                PrimedTnt tntentity = new PrimedTnt(context.world, x, y, z, null);
                tntentity.m_5997_(context.motion.x, context.motion.y, context.motion.z);
                context.world.m_7967_(tntentity);
                context.world.playSound(null, tntentity.m_20185_(), tntentity.m_20186_(), tntentity.m_20189_(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
                itemStack.shrink(1);
                return itemStack;
            }
        });
        DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(Items.FIREWORK_ROCKET, new MovedDefaultDispenseItemBehaviour() {

            @Override
            protected ItemStack dispenseStack(ItemStack itemStack, MovementContext context, BlockPos pos, Vec3 facing) {
                double x = (double) pos.m_123341_() + facing.x * 0.7 + 0.5;
                double y = (double) pos.m_123342_() + facing.y * 0.7 + 0.5;
                double z = (double) pos.m_123343_() + facing.z * 0.7 + 0.5;
                FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(context.world, itemStack, x, y, z, true);
                fireworkrocketentity.m_6686_(facing.x, facing.y, facing.z, 0.5F, 1.0F);
                context.world.m_7967_(fireworkrocketentity);
                itemStack.shrink(1);
                return itemStack;
            }

            @Override
            protected void playDispenseSound(LevelAccessor world, BlockPos pos) {
                world.levelEvent(1004, pos, 0);
            }
        });
        DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(Items.FIRE_CHARGE, new MovedDefaultDispenseItemBehaviour() {

            @Override
            protected void playDispenseSound(LevelAccessor world, BlockPos pos) {
                world.levelEvent(1018, pos, 0);
            }

            @Override
            protected ItemStack dispenseStack(ItemStack itemStack, MovementContext context, BlockPos pos, Vec3 facing) {
                RandomSource random = context.world.random;
                double x = (double) pos.m_123341_() + facing.x * 0.7 + 0.5;
                double y = (double) pos.m_123342_() + facing.y * 0.7 + 0.5;
                double z = (double) pos.m_123343_() + facing.z * 0.7 + 0.5;
                context.world.m_7967_(Util.make(new SmallFireball(context.world, x, y, z, random.nextGaussian() * 0.05 + facing.x + context.motion.x, random.nextGaussian() * 0.05 + facing.y + context.motion.y, random.nextGaussian() * 0.05 + facing.z + context.motion.z), p_229425_1_ -> p_229425_1_.m_37010_(itemStack)));
                itemStack.shrink(1);
                return itemStack;
            }
        });
        DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(Items.GLASS_BOTTLE, new MovedOptionalDispenseBehaviour() {

            @Override
            protected ItemStack dispenseStack(ItemStack itemStack, MovementContext context, BlockPos pos, Vec3 facing) {
                this.successful = false;
                BlockPos interactAt = pos.relative(this.getClosestFacingDirection(facing));
                BlockState state = context.world.getBlockState(interactAt);
                Block block = state.m_60734_();
                if (state.m_204336_(BlockTags.BEEHIVES) && (Integer) state.m_61143_(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    ((BeehiveBlock) block).releaseBeesAndResetHoneyLevel(context.world, state, interactAt, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
                    this.successful = true;
                    return this.placeItemInInventory(itemStack, new ItemStack(Items.HONEY_BOTTLE), context, pos, facing);
                } else if (context.world.getFluidState(interactAt).is(FluidTags.WATER)) {
                    this.successful = true;
                    return this.placeItemInInventory(itemStack, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER), context, pos, facing);
                } else {
                    return super.dispenseStack(itemStack, context, pos, facing);
                }
            }
        });
        DispenserMovementBehaviour.registerMovedDispenseItemBehaviour(Items.BUCKET, new MovedDefaultDispenseItemBehaviour() {

            @Override
            protected ItemStack dispenseStack(ItemStack itemStack, MovementContext context, BlockPos pos, Vec3 facing) {
                BlockPos interactAt = pos.relative(this.getClosestFacingDirection(facing));
                BlockState state = context.world.getBlockState(interactAt);
                Block block = state.m_60734_();
                if (block instanceof BucketPickup) {
                    ItemStack bucket = ((BucketPickup) block).pickupBlock(context.world, interactAt, state);
                    return this.placeItemInInventory(itemStack, bucket, context, pos, facing);
                } else {
                    return super.dispenseStack(itemStack, context, pos, facing);
                }
            }
        });
    }

    ItemStack dispense(ItemStack var1, MovementContext var2, BlockPos var3);
}
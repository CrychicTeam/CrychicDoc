package com.simibubi.create.content.equipment.potatoCannon;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.mixin.accessor.FallingBlockEntityAccessor;
import com.simibubi.create.foundation.utility.WorldAttached;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class BuiltinPotatoProjectileTypes {

    private static final GameProfile ZOMBIE_CONVERTER_NAME = new GameProfile(UUID.fromString("be12d3dc-27d3-4992-8c97-66be53fd49c5"), "Converter");

    private static final WorldAttached<FakePlayer> ZOMBIE_CONVERTERS = new WorldAttached<>(w -> new FakePlayer((ServerLevel) w, ZOMBIE_CONVERTER_NAME));

    public static final PotatoCannonProjectileType FALLBACK = create("fallback").damage(0).register();

    public static final PotatoCannonProjectileType POTATO = create("potato").damage(5).reloadTicks(15).velocity(1.25F).knockback(1.5F).renderTumbling().onBlockHit(plantCrop(Blocks.POTATOES)).registerAndAssign(Items.POTATO);

    public static final PotatoCannonProjectileType BAKED_POTATO = create("baked_potato").damage(5).reloadTicks(15).velocity(1.25F).knockback(0.5F).renderTumbling().preEntityHit(setFire(3)).registerAndAssign(Items.BAKED_POTATO);

    public static final PotatoCannonProjectileType CARROT = create("carrot").damage(4).reloadTicks(12).velocity(1.45F).knockback(0.3F).renderTowardMotion(140, 1.0F).soundPitch(1.5F).onBlockHit(plantCrop(Blocks.CARROTS)).registerAndAssign(Items.CARROT);

    public static final PotatoCannonProjectileType GOLDEN_CARROT = create("golden_carrot").damage(12).reloadTicks(15).velocity(1.45F).knockback(0.5F).renderTowardMotion(140, 2.0F).soundPitch(1.5F).registerAndAssign(Items.GOLDEN_CARROT);

    public static final PotatoCannonProjectileType SWEET_BERRIES = create("sweet_berry").damage(3).reloadTicks(10).knockback(0.1F).velocity(1.05F).renderTumbling().splitInto(3).soundPitch(1.25F).registerAndAssign(Items.SWEET_BERRIES);

    public static final PotatoCannonProjectileType GLOW_BERRIES = create("glow_berry").damage(2).reloadTicks(10).knockback(0.05F).velocity(1.05F).renderTumbling().splitInto(2).soundPitch(1.2F).onEntityHit(potion(MobEffects.GLOWING, 1, 200, false)).registerAndAssign(Items.GLOW_BERRIES);

    public static final PotatoCannonProjectileType CHOCOLATE_BERRIES = create("chocolate_berry").damage(4).reloadTicks(10).knockback(0.2F).velocity(1.05F).renderTumbling().splitInto(3).soundPitch(1.25F).registerAndAssign((ItemLike) AllItems.CHOCOLATE_BERRIES.get());

    public static final PotatoCannonProjectileType POISON_POTATO = create("poison_potato").damage(5).reloadTicks(15).knockback(0.05F).velocity(1.25F).renderTumbling().onEntityHit(potion(MobEffects.POISON, 1, 160, true)).registerAndAssign(Items.POISONOUS_POTATO);

    public static final PotatoCannonProjectileType CHORUS_FRUIT = create("chorus_fruit").damage(3).reloadTicks(15).velocity(1.2F).knockback(0.05F).renderTumbling().onEntityHit(chorusTeleport(20.0)).registerAndAssign(Items.CHORUS_FRUIT);

    public static final PotatoCannonProjectileType APPLE = create("apple").damage(5).reloadTicks(10).velocity(1.45F).knockback(0.5F).renderTumbling().soundPitch(1.1F).registerAndAssign(Items.APPLE);

    public static final PotatoCannonProjectileType HONEYED_APPLE = create("honeyed_apple").damage(6).reloadTicks(15).velocity(1.35F).knockback(0.1F).renderTumbling().soundPitch(1.1F).onEntityHit(potion(MobEffects.MOVEMENT_SLOWDOWN, 2, 160, true)).registerAndAssign((ItemLike) AllItems.HONEYED_APPLE.get());

    public static final PotatoCannonProjectileType GOLDEN_APPLE = create("golden_apple").damage(1).reloadTicks(100).velocity(1.45F).knockback(0.05F).renderTumbling().soundPitch(1.1F).onEntityHit(ray -> {
        Entity entity = ray.getEntity();
        Level world = entity.level();
        if (!(entity instanceof ZombieVillager) || !((ZombieVillager) entity).m_21023_(MobEffects.WEAKNESS)) {
            return foodEffects(Foods.GOLDEN_APPLE, false).test(ray);
        } else if (world.isClientSide) {
            return false;
        } else {
            FakePlayer dummy = ZOMBIE_CONVERTERS.get(world);
            dummy.m_21008_(InteractionHand.MAIN_HAND, new ItemStack(Items.GOLDEN_APPLE, 1));
            ((ZombieVillager) entity).mobInteract(dummy, InteractionHand.MAIN_HAND);
            return true;
        }
    }).registerAndAssign(Items.GOLDEN_APPLE);

    public static final PotatoCannonProjectileType ENCHANTED_GOLDEN_APPLE = create("enchanted_golden_apple").damage(1).reloadTicks(100).velocity(1.45F).knockback(0.05F).renderTumbling().soundPitch(1.1F).onEntityHit(foodEffects(Foods.ENCHANTED_GOLDEN_APPLE, false)).registerAndAssign(Items.ENCHANTED_GOLDEN_APPLE);

    public static final PotatoCannonProjectileType BEETROOT = create("beetroot").damage(2).reloadTicks(5).velocity(1.6F).knockback(0.1F).renderTowardMotion(140, 2.0F).soundPitch(1.6F).registerAndAssign(Items.BEETROOT);

    public static final PotatoCannonProjectileType MELON_SLICE = create("melon_slice").damage(3).reloadTicks(8).knockback(0.1F).velocity(1.45F).renderTumbling().soundPitch(1.5F).registerAndAssign(Items.MELON_SLICE);

    public static final PotatoCannonProjectileType GLISTERING_MELON = create("glistering_melon").damage(5).reloadTicks(8).knockback(0.1F).velocity(1.45F).renderTumbling().soundPitch(1.5F).onEntityHit(potion(MobEffects.GLOWING, 1, 100, true)).registerAndAssign(Items.GLISTERING_MELON_SLICE);

    public static final PotatoCannonProjectileType MELON_BLOCK = create("melon_block").damage(8).reloadTicks(20).knockback(2.0F).velocity(0.95F).renderTumbling().soundPitch(0.9F).onBlockHit(placeBlockOnGround(Blocks.MELON)).registerAndAssign(Blocks.MELON);

    public static final PotatoCannonProjectileType PUMPKIN_BLOCK = create("pumpkin_block").damage(6).reloadTicks(15).knockback(2.0F).velocity(0.95F).renderTumbling().soundPitch(0.9F).onBlockHit(placeBlockOnGround(Blocks.PUMPKIN)).registerAndAssign(Blocks.PUMPKIN);

    public static final PotatoCannonProjectileType PUMPKIN_PIE = create("pumpkin_pie").damage(7).reloadTicks(15).knockback(0.05F).velocity(1.1F).renderTumbling().sticky().soundPitch(1.1F).registerAndAssign(Items.PUMPKIN_PIE);

    public static final PotatoCannonProjectileType CAKE = create("cake").damage(8).reloadTicks(15).knockback(0.1F).velocity(1.1F).renderTumbling().sticky().soundPitch(1.0F).registerAndAssign(Items.CAKE);

    public static final PotatoCannonProjectileType BLAZE_CAKE = create("blaze_cake").damage(15).reloadTicks(20).knockback(0.3F).velocity(1.1F).renderTumbling().sticky().preEntityHit(setFire(12)).soundPitch(1.0F).registerAndAssign((ItemLike) AllItems.BLAZE_CAKE.get());

    private static PotatoCannonProjectileType.Builder create(String name) {
        return new PotatoCannonProjectileType.Builder(Create.asResource(name));
    }

    private static Predicate<EntityHitResult> setFire(int seconds) {
        return ray -> {
            ray.getEntity().setSecondsOnFire(seconds);
            return false;
        };
    }

    private static Predicate<EntityHitResult> potion(MobEffect effect, int level, int ticks, boolean recoverable) {
        return ray -> {
            Entity entity = ray.getEntity();
            if (entity.level().isClientSide) {
                return true;
            } else {
                if (entity instanceof LivingEntity) {
                    applyEffect((LivingEntity) entity, new MobEffectInstance(effect, ticks, level - 1));
                }
                return !recoverable;
            }
        };
    }

    private static Predicate<EntityHitResult> foodEffects(FoodProperties food, boolean recoverable) {
        return ray -> {
            Entity entity = ray.getEntity();
            if (entity.level().isClientSide) {
                return true;
            } else {
                if (entity instanceof LivingEntity) {
                    for (Pair<MobEffectInstance, Float> effect : food.getEffects()) {
                        if (Create.RANDOM.nextFloat() < (Float) effect.getSecond()) {
                            applyEffect((LivingEntity) entity, new MobEffectInstance((MobEffectInstance) effect.getFirst()));
                        }
                    }
                }
                return !recoverable;
            }
        };
    }

    private static void applyEffect(LivingEntity entity, MobEffectInstance effect) {
        if (effect.getEffect().isInstantenous()) {
            effect.getEffect().applyInstantenousEffect(null, null, entity, effect.getDuration(), 1.0);
        } else {
            entity.addEffect(effect);
        }
    }

    private static BiPredicate<LevelAccessor, BlockHitResult> plantCrop(Supplier<? extends Block> cropBlock) {
        return (world, ray) -> {
            if (world.m_5776_()) {
                return true;
            } else {
                BlockPos hitPos = ray.getBlockPos();
                if (world instanceof Level l && !l.isLoaded(hitPos)) {
                    return true;
                }
                Direction face = ray.getDirection();
                if (face != Direction.UP) {
                    return false;
                } else {
                    BlockPos placePos = hitPos.relative(face);
                    if (!world.m_8055_(placePos).m_247087_()) {
                        return false;
                    } else if (!(cropBlock.get() instanceof IPlantable)) {
                        return false;
                    } else {
                        BlockState blockState = world.m_8055_(hitPos);
                        if (!blockState.canSustainPlant(world, hitPos, face, (IPlantable) cropBlock.get())) {
                            return false;
                        } else {
                            world.m_7731_(placePos, ((Block) cropBlock.get()).defaultBlockState(), 3);
                            return true;
                        }
                    }
                }
            }
        };
    }

    private static BiPredicate<LevelAccessor, BlockHitResult> plantCrop(Block cropBlock) {
        return plantCrop(ForgeRegistries.BLOCKS.getDelegateOrThrow(cropBlock));
    }

    private static BiPredicate<LevelAccessor, BlockHitResult> placeBlockOnGround(Supplier<? extends Block> block) {
        return (world, ray) -> {
            if (world.m_5776_()) {
                return true;
            } else {
                BlockPos hitPos = ray.getBlockPos();
                if (world instanceof Level l && !l.isLoaded(hitPos)) {
                    return true;
                }
                Direction face = ray.getDirection();
                BlockPos placePos = hitPos.relative(face);
                if (!world.m_8055_(placePos).m_247087_()) {
                    return false;
                } else {
                    if (face == Direction.UP) {
                        world.m_7731_(placePos, ((Block) block.get()).defaultBlockState(), 3);
                    } else if (world instanceof Level level) {
                        double y = ray.m_82450_().y - 0.5;
                        if (!world.m_46859_(placePos.above())) {
                            y = Math.min(y, (double) placePos.m_123342_());
                        }
                        if (!world.m_46859_(placePos.below())) {
                            y = Math.max(y, (double) placePos.m_123342_());
                        }
                        FallingBlockEntity falling = FallingBlockEntityAccessor.create$callInit(level, (double) placePos.m_123341_() + 0.5, y, (double) placePos.m_123343_() + 0.5, ((Block) block.get()).defaultBlockState());
                        falling.time = 1;
                        world.m_7967_(falling);
                    }
                    return true;
                }
            }
        };
    }

    private static BiPredicate<LevelAccessor, BlockHitResult> placeBlockOnGround(Block block) {
        return placeBlockOnGround(ForgeRegistries.BLOCKS.getDelegateOrThrow(block));
    }

    private static Predicate<EntityHitResult> chorusTeleport(double teleportDiameter) {
        return ray -> {
            Entity entity = ray.getEntity();
            Level world = entity.getCommandSenderWorld();
            if (world.isClientSide) {
                return true;
            } else if (!(entity instanceof LivingEntity livingEntity)) {
                return false;
            } else {
                double entityX = livingEntity.m_20185_();
                double entityY = livingEntity.m_20186_();
                double entityZ = livingEntity.m_20189_();
                for (int teleportTry = 0; teleportTry < 16; teleportTry++) {
                    double teleportX = entityX + (livingEntity.getRandom().nextDouble() - 0.5) * teleportDiameter;
                    double teleportY = Mth.clamp(entityY + (double) (livingEntity.getRandom().nextInt((int) teleportDiameter) - (int) (teleportDiameter / 2.0)), 0.0, (double) (world.m_141928_() - 1));
                    double teleportZ = entityZ + (livingEntity.getRandom().nextDouble() - 0.5) * teleportDiameter;
                    EntityTeleportEvent.ChorusFruit event = ForgeEventFactory.onChorusFruitTeleport(livingEntity, teleportX, teleportY, teleportZ);
                    if (event.isCanceled()) {
                        return false;
                    }
                    if (livingEntity.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                        if (livingEntity.m_20159_()) {
                            livingEntity.stopRiding();
                        }
                        SoundEvent soundevent = livingEntity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                        world.playSound(null, entityX, entityY, entityZ, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                        livingEntity.m_5496_(soundevent, 1.0F, 1.0F);
                        livingEntity.m_20256_(Vec3.ZERO);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static void register() {
    }
}
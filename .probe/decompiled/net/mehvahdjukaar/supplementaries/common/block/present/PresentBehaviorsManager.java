package net.mehvahdjukaar.supplementaries.common.block.present;

import java.util.Optional;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.TrappedPresentBlock;
import net.mehvahdjukaar.supplementaries.common.entities.BombEntity;
import net.mehvahdjukaar.supplementaries.common.entities.HatStandEntity;
import net.mehvahdjukaar.supplementaries.common.items.BombItem;
import net.mehvahdjukaar.supplementaries.common.misc.explosion.GunpowderExplosion;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundSendKnockbackPacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PresentBehaviorsManager {

    private static final IPresentItemBehavior SPAWN_EGG_BEHAVIOR = (source, stack) -> {
        EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
        try {
            ServerLevel level = source.getLevel();
            BlockPos pos = source.getPos();
            Entity e = spawnMob((EntityType<Entity>) type, level, source, stack);
            if (e != null) {
                stack.shrink(1);
                level.m_142346_(null, GameEvent.ENTITY_PLACE, pos);
                return Optional.of(stack);
            }
        } catch (Exception var6) {
            Supplementaries.LOGGER.error("Error while dispensing spawn egg from trapped present at {}", source.getPos(), var6);
        }
        return Optional.empty();
    };

    private static final IPresentItemBehavior SKIBIDI_BEHAVIOR = (source, stack) -> {
        EntityType<HatStandEntity> type = (EntityType<HatStandEntity>) ModEntities.HAT_STAND.get();
        try {
            ServerLevel level = source.getLevel();
            BlockPos pos = source.getPos();
            HatStandEntity e = spawnMob(type, level, source, stack);
            if (e != null) {
                stack.shrink(1);
                level.m_142346_(null, GameEvent.ENTITY_PLACE, pos);
                e.setSkibidi(true, false, null);
                e.m_20334_(0.0, 0.0, 0.0);
                return Optional.of(stack);
            }
        } catch (Exception var6) {
            Supplementaries.LOGGER.error("Error while dispensing spawn egg from trapped present at {}", source.getPos(), var6);
        }
        return Optional.empty();
    };

    private static final IPresentItemBehavior TNT_BEHAVIOR = (source, stack) -> {
        Level level = source.getLevel();
        BlockPos blockpos = source.getPos().above();
        if (!(stack.getItem() instanceof BlockItem bi)) {
            return Optional.empty();
        } else {
            Block tnt = bi.getBlock();
            if (tnt instanceof TntBlock) {
                Explosion dummyExplosion = new Explosion(level, null, (double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123341_() + 0.5, 0.0F, false, Explosion.BlockInteraction.KEEP);
                tnt.wasExploded(level, blockpos, dummyExplosion);
            } else {
                GunpowderExplosion.igniteTntHack(level, blockpos, tnt);
            }
            for (Entity e : level.getEntities((Entity) null, new AABB(blockpos).move(0.0, 0.5, 0.0), entity -> entity instanceof PrimedTnt || entity.getType() == CompatObjects.ALEX_NUKE.get())) {
                Vec3 p = e.position();
                e.setPos(new Vec3(p.x, (double) ((float) blockpos.m_123342_() + 0.625F), p.z));
            }
            level.m_142346_(null, GameEvent.ENTITY_PLACE, blockpos);
            stack.shrink(1);
            return Optional.of(stack);
        }
    };

    private static final IPresentItemBehavior FIREWORK_BEHAVIOR = new IPresentItemBehavior() {

        @Override
        public Optional<ItemStack> performSpecialAction(BlockSource source, ItemStack stack) {
            Level level = source.getLevel();
            Position p = IPresentItemBehavior.getDispensePosition(source);
            FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(level, stack, p.x(), p.y(), p.z(), true);
            fireworkrocketentity.m_6686_(0.0, 1.0, 0.0, 0.5F, 1.0F);
            level.m_7967_(fireworkrocketentity);
            stack.shrink(1);
            return Optional.of(stack);
        }

        @Override
        public void playAnimation(BlockSource pSource) {
            IPresentItemBehavior.super.playAnimation(pSource);
            pSource.getLevel().m_46796_(1004, pSource.getPos(), 0);
        }
    };

    private static final PresentBehaviorsManager.AbstractProjectileBehavior SPLASH_POTION_BEHAVIOR = new PresentBehaviorsManager.AbstractProjectileBehavior() {

        @Override
        protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
            return Util.make(new ThrownPotion(worldIn, position.x(), position.y(), position.z()), potion -> potion.m_37446_(stackIn));
        }

        @Override
        protected float getPower() {
            return 0.5F;
        }

        @Override
        protected float getUncertainty() {
            return 11.0F;
        }
    };

    private static final PresentBehaviorsManager.AbstractProjectileBehavior BOMB_BEHAVIOR = new PresentBehaviorsManager.AbstractProjectileBehavior() {

        @Override
        protected Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {
            return new BombEntity(worldIn, position.x(), position.y(), position.z(), ((BombItem) stackIn.getItem()).getType());
        }

        @Override
        protected float getUncertainty() {
            return 11.0F;
        }
    };

    public static void registerBehaviors() {
        for (Item i : BuiltInRegistries.ITEM) {
            if (i instanceof BlockItem bi && bi.getBlock() instanceof TntBlock) {
                TrappedPresentBlock.registerBehavior(i, TNT_BEHAVIOR);
            }
            if (i instanceof SpawnEggItem sp) {
                TrappedPresentBlock.registerBehavior(sp, SPAWN_EGG_BEHAVIOR);
            }
        }
        TrappedPresentBlock.registerBehavior(Items.FIREWORK_ROCKET, FIREWORK_BEHAVIOR);
        TrappedPresentBlock.registerBehavior(Items.SPLASH_POTION, SPLASH_POTION_BEHAVIOR);
        TrappedPresentBlock.registerBehavior(Items.LINGERING_POTION, SPLASH_POTION_BEHAVIOR);
        TrappedPresentBlock.registerBehavior((ItemLike) ModRegistry.BOMB_ITEM.get(), BOMB_BEHAVIOR);
        TrappedPresentBlock.registerBehavior((ItemLike) ModRegistry.BOMB_ITEM_ON.get(), BOMB_BEHAVIOR);
        TrappedPresentBlock.registerBehavior((ItemLike) ModRegistry.BOMB_BLUE_ITEM.get(), BOMB_BEHAVIOR);
        TrappedPresentBlock.registerBehavior((ItemLike) ModRegistry.BOMB_BLUE_ITEM_ON.get(), BOMB_BEHAVIOR);
        TrappedPresentBlock.registerBehavior((ItemLike) ModRegistry.BOMB_SPIKY_ITEM.get(), BOMB_BEHAVIOR);
        TrappedPresentBlock.registerBehavior((ItemLike) ModRegistry.BOMB_SPIKY_ITEM_ON.get(), BOMB_BEHAVIOR);
        TrappedPresentBlock.registerBehavior((ItemLike) ModRegistry.HAT_STAND.get(), SKIBIDI_BEHAVIOR);
        Block nuke = (Block) CompatObjects.NUKE_BLOCK.get();
        if (nuke != null) {
            TrappedPresentBlock.registerBehavior(nuke, TNT_BEHAVIOR);
        }
    }

    @Nullable
    private static <T extends Entity> T spawnMob(EntityType<T> entityType, ServerLevel serverLevel, BlockSource source, @Nullable ItemStack stack) {
        BlockPos pos = source.getPos();
        CompoundTag tag = stack == null ? null : stack.getTag();
        Component component = stack != null && stack.hasCustomHoverName() ? stack.getHoverName() : null;
        T entity = entityType.create(serverLevel);
        if (entity != null) {
            if (component != null) {
                entity.setCustomName(component);
            }
            EntityType.updateCustomEntityTag(serverLevel, null, entity, tag);
            Position p = IPresentItemBehavior.getDispensePosition(source);
            entity.setPos(p.x(), p.y(), p.z());
            entity.moveTo(p.x(), p.y(), p.z(), Mth.wrapDegrees(serverLevel.f_46441_.nextFloat() * 360.0F), 0.0F);
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0, 0.3, 0.0));
            if (entity instanceof Mob mob) {
                mob.f_20885_ = mob.m_146908_();
                mob.f_20883_ = mob.m_146908_();
                mob.finalizeSpawn(serverLevel, serverLevel.m_6436_(mob.m_20183_()), MobSpawnType.DISPENSER, null, tag);
                mob.playAmbientSound();
            }
            serverLevel.m_47205_(entity);
            ModNetwork.CHANNEL.sendToAllClientPlayersInRange(serverLevel, pos, 48.0, new ClientBoundSendKnockbackPacket(entity.getDeltaMovement(), entity.getId()));
        }
        return entity;
    }

    public abstract static class AbstractProjectileBehavior implements IPresentItemBehavior {

        @Override
        public Optional<ItemStack> performSpecialAction(BlockSource source, ItemStack stack) {
            Level level = source.getLevel();
            Position position = IPresentItemBehavior.getDispensePosition(source);
            Projectile projectile = this.getProjectile(level, position, stack);
            projectile.shoot(0.0, 1.0, 0.0, this.getPower(), this.getUncertainty());
            level.m_7967_(projectile);
            stack.shrink(1);
            return Optional.of(stack);
        }

        protected abstract Projectile getProjectile(Level var1, Position var2, ItemStack var3);

        protected float getUncertainty() {
            return 6.0F;
        }

        protected float getPower() {
            return 0.4F;
        }
    }
}
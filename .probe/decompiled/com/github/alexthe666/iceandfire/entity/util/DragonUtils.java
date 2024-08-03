package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafBlockTags;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityGhost;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.misc.IafTagRegistry;
import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class DragonUtils {

    public static BlockPos getBlockInViewEscort(EntityDragonBase dragon) {
        BlockPos escortPos = dragon.getEscortPosition();
        BlockPos ground = dragon.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, escortPos);
        int distFromGround = escortPos.m_123342_() - ground.m_123342_();
        for (int i = 0; i < 10; i++) {
            BlockPos pos = new BlockPos(escortPos.m_123341_() + dragon.m_217043_().nextInt(IafConfig.dragonWanderFromHomeDistance) - IafConfig.dragonWanderFromHomeDistance / 2, distFromGround > 16 ? escortPos.m_123342_() : escortPos.m_123342_() + 8 + dragon.m_217043_().nextInt(16), escortPos.m_123343_() + dragon.m_217043_().nextInt(IafConfig.dragonWanderFromHomeDistance) - IafConfig.dragonWanderFromHomeDistance / 2);
            if (dragon.getDistanceSquared(Vec3.atCenterOf(pos)) > 6.0F && !dragon.isTargetBlocked(Vec3.atCenterOf(pos))) {
                return pos;
            }
        }
        return null;
    }

    public static BlockPos getWaterBlockInViewEscort(EntityDragonBase dragon) {
        BlockPos inWaterEscortPos = dragon.getEscortPosition();
        if (Math.abs(dragon.m_20185_() - (double) inWaterEscortPos.m_123341_()) < dragon.m_20191_().getXsize() && Math.abs(dragon.m_20189_() - (double) inWaterEscortPos.m_123343_()) < dragon.m_20191_().getZsize()) {
            return dragon.m_20183_();
        } else {
            if ((double) inWaterEscortPos.m_123342_() - dragon.m_20186_() > (double) (8 + dragon.getYNavSize()) && !dragon.m_9236_().getFluidState(inWaterEscortPos.below()).is(FluidTags.WATER)) {
                dragon.setHovering(true);
            }
            return inWaterEscortPos;
        }
    }

    public static BlockPos getBlockInView(EntityDragonBase dragon) {
        float radius = 12.0F * (0.7F * dragon.getRenderSize() / 3.0F);
        float neg = dragon.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = dragon.f_20883_;
        if (dragon.hasHomePosition && dragon.homePos != null) {
            BlockPos dragonPos = dragon.m_20183_();
            BlockPos ground = dragon.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dragonPos);
            int distFromGround = (int) dragon.m_20186_() - ground.m_123342_();
            for (int i = 0; i < 10; i++) {
                BlockPos homePos = dragon.homePos.getPosition();
                BlockPos pos = new BlockPos(homePos.m_123341_() + dragon.m_217043_().nextInt(IafConfig.dragonWanderFromHomeDistance * 2) - IafConfig.dragonWanderFromHomeDistance, distFromGround > 16 ? (int) Math.min((double) IafConfig.maxDragonFlight, dragon.m_20186_() + (double) dragon.m_217043_().nextInt(16) - 8.0) : (int) dragon.m_20186_() + dragon.m_217043_().nextInt(16) + 1, homePos.m_123343_() + dragon.m_217043_().nextInt(IafConfig.dragonWanderFromHomeDistance * 2) - IafConfig.dragonWanderFromHomeDistance);
                if (dragon.getDistanceSquared(Vec3.atCenterOf(pos)) > 6.0F && !dragon.isTargetBlocked(Vec3.atCenterOf(pos))) {
                    return pos;
                }
            }
        }
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + dragon.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = BlockPos.containing(dragon.m_20185_() + extraX, 0.0, dragon.m_20189_() + extraZ);
        BlockPos ground = dragon.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) dragon.m_20186_() - ground.m_123342_();
        BlockPos newPos = radialPos.above(distFromGround > 16 ? (int) Math.min((double) IafConfig.maxDragonFlight, dragon.m_20186_() + (double) dragon.m_217043_().nextInt(16) - 8.0) : (int) dragon.m_20186_() + dragon.m_217043_().nextInt(16) + 1);
        BlockPos pos = dragon.doesWantToLand() ? ground : newPos;
        return dragon.getDistanceSquared(Vec3.atCenterOf(newPos)) > 6.0F && !dragon.isTargetBlocked(Vec3.atCenterOf(newPos)) ? pos : null;
    }

    public static BlockPos getWaterBlockInView(EntityDragonBase dragon) {
        float radius = 0.75F * (0.7F * dragon.getRenderSize() / 3.0F) * -7.0F - (float) dragon.m_217043_().nextInt(dragon.getDragonStage() * 6);
        float neg = dragon.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float angle = (float) (Math.PI / 180.0) * dragon.f_20883_ + 3.15F + dragon.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = BlockPos.containing(dragon.m_20185_() + extraX, 0.0, dragon.m_20189_() + extraZ);
        BlockPos ground = dragon.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) dragon.m_20186_() - ground.m_123342_();
        BlockPos newPos = radialPos.above(distFromGround > 16 ? (int) Math.min((double) IafConfig.maxDragonFlight, dragon.m_20186_() + (double) dragon.m_217043_().nextInt(16) - 8.0) : (int) dragon.m_20186_() + dragon.m_217043_().nextInt(16) + 1);
        BlockPos pos = dragon.doesWantToLand() ? ground : newPos;
        BlockPos surface = dragon.m_9236_().getFluidState(newPos.below(2)).is(FluidTags.WATER) ? newPos.below(dragon.m_217043_().nextInt(10) + 1) : newPos;
        return dragon.getDistanceSquared(Vec3.atCenterOf(surface)) > 6.0F && dragon.m_9236_().getFluidState(surface).is(FluidTags.WATER) ? surface : null;
    }

    public static LivingEntity riderLookingAtEntity(final LivingEntity dragon, LivingEntity rider, double dist) {
        Vec3 Vector3d = rider.m_20299_(1.0F);
        Vec3 Vector3d1 = rider.m_20252_(1.0F);
        Vec3 Vector3d2 = Vector3d.add(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist);
        Entity pointedEntity = null;
        List<Entity> list = rider.m_9236_().getEntities(rider, rider.m_20191_().expandTowards(Vector3d1.x * dist, Vector3d1.y * dist, Vector3d1.z * dist).inflate(1.0, 1.0, 1.0), new Predicate<Entity>() {

            public boolean apply(@Nullable Entity entity) {
                return DragonUtils.onSameTeam(dragon, entity) ? false : entity != null && entity.isPickable() && entity instanceof LivingEntity && !entity.is(dragon) && !entity.isAlliedTo(dragon) && (!(entity instanceof IDeadMob) || !((IDeadMob) entity).isMobDead());
            }
        });
        double d2 = dist;
        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);
            AABB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius() + 2.0);
            Vec3 raytraceresult = (Vec3) axisalignedbb.clip(Vector3d, Vector3d2).orElse(Vec3.ZERO);
            if (axisalignedbb.contains(Vector3d)) {
                if (d2 >= 0.0) {
                    pointedEntity = entity1;
                    d2 = 0.0;
                }
            } else if (raytraceresult != null) {
                double d3 = Vector3d.distanceTo(raytraceresult);
                if (d3 < d2 || d2 == 0.0) {
                    if (entity1.getRootVehicle() != rider.m_20201_() || rider.canRiderInteract()) {
                        pointedEntity = entity1;
                        d2 = d3;
                    } else if (d2 == 0.0) {
                        pointedEntity = entity1;
                    }
                }
            }
        }
        return (LivingEntity) pointedEntity;
    }

    public static BlockPos getBlockInViewHippogryph(EntityHippogryph hippo, float yawAddition) {
        float radius = -12.599999F - (float) hippo.m_217043_().nextInt(48);
        float neg = hippo.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float angle = (float) (Math.PI / 180.0) * (hippo.f_20883_ + yawAddition) + 3.15F + hippo.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        if (hippo.hasHomePosition && hippo.homePos != null) {
            BlockPos dragonPos = hippo.m_20183_();
            BlockPos ground = hippo.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, dragonPos);
            int distFromGround = (int) hippo.m_20186_() - ground.m_123342_();
            for (int i = 0; i < 10; i++) {
                BlockPos pos = BlockPos.containing((double) (hippo.homePos.m_123341_() + hippo.m_217043_().nextInt(IafConfig.dragonWanderFromHomeDistance) - IafConfig.dragonWanderFromHomeDistance), (double) (distFromGround > 16 ? (int) Math.min((double) IafConfig.maxDragonFlight, hippo.m_20186_() + (double) hippo.m_217043_().nextInt(16) - 8.0) : (int) hippo.m_20186_() + hippo.m_217043_().nextInt(16) + 1), (double) (hippo.homePos.m_123343_() + hippo.m_217043_().nextInt(IafConfig.dragonWanderFromHomeDistance * 2) - IafConfig.dragonWanderFromHomeDistance));
                if (hippo.getDistanceSquared(Vec3.atCenterOf(pos)) > 6.0F && !hippo.isTargetBlocked(Vec3.atCenterOf(pos))) {
                    return pos;
                }
            }
        }
        BlockPos radialPos = BlockPos.containing(hippo.m_20185_() + extraX, 0.0, hippo.m_20189_() + extraZ);
        BlockPos ground = hippo.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) hippo.m_20186_() - ground.m_123342_();
        BlockPos newPos = radialPos.above(distFromGround > 16 ? (int) Math.min((double) IafConfig.maxDragonFlight, hippo.m_20186_() + (double) hippo.m_217043_().nextInt(16) - 8.0) : (int) hippo.m_20186_() + hippo.m_217043_().nextInt(16) + 1);
        BlockPos pos = hippo.doesWantToLand() ? ground : newPos;
        return !hippo.isTargetBlocked(Vec3.atCenterOf(newPos)) && hippo.getDistanceSquared(Vec3.atCenterOf(newPos)) > 6.0F ? newPos : null;
    }

    public static BlockPos getBlockInViewStymphalian(EntityStymphalianBird bird) {
        float radius = -9.45F - (float) bird.m_217043_().nextInt(24);
        float neg = bird.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = bird.flock != null && !bird.flock.isLeader(bird) ? getStymphalianFlockDirection(bird) : bird.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + bird.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = getStymphalianFearPos(bird, BlockPos.containing(bird.m_20185_() + extraX, 0.0, bird.m_20189_() + extraZ));
        BlockPos ground = bird.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        int distFromGround = (int) bird.m_20186_() - ground.m_123342_();
        int flightHeight = Math.min(IafConfig.stymphalianBirdFlightHeight, ground.m_123342_() + bird.m_217043_().nextInt(16));
        BlockPos newPos = radialPos.above(distFromGround > 16 ? flightHeight : (int) bird.m_20186_() + bird.m_217043_().nextInt(16) + 1);
        return bird.getDistanceSquared(Vec3.atCenterOf(newPos)) > 6.0F && !bird.isTargetBlocked(Vec3.atCenterOf(newPos)) ? newPos : null;
    }

    private static BlockPos getStymphalianFearPos(EntityStymphalianBird bird, BlockPos fallback) {
        if (bird.getVictor() != null && bird.getVictor() instanceof PathfinderMob) {
            Vec3 Vector3d = DefaultRandomPos.getPosAway((PathfinderMob) bird.getVictor(), 16, IafConfig.stymphalianBirdFlightHeight, new Vec3(bird.getVictor().m_20185_(), bird.getVictor().m_20186_(), bird.getVictor().m_20189_()));
            if (Vector3d != null) {
                BlockPos pos = BlockPos.containing(Vector3d);
                return new BlockPos(pos.m_123341_(), 0, pos.m_123343_());
            }
        }
        return fallback;
    }

    private static float getStymphalianFlockDirection(EntityStymphalianBird bird) {
        EntityStymphalianBird leader = bird.flock.getLeader();
        if (bird.m_20280_(leader) > 2.0) {
            double d0 = leader.m_20185_() - bird.m_20185_();
            double d2 = leader.m_20189_() - bird.m_20189_();
            float f = (float) (Mth.atan2(d2, d0) * (180.0 / Math.PI)) - 90.0F;
            float degrees = Mth.wrapDegrees(f - bird.m_146908_());
            return bird.m_146908_() + degrees;
        } else {
            return leader.f_20883_;
        }
    }

    public static BlockPos getBlockInTargetsViewCockatrice(EntityCockatrice cockatrice, LivingEntity target) {
        float radius = (float) (10 + cockatrice.m_217043_().nextInt(10));
        float angle = (float) (Math.PI / 180.0) * target.yHeadRot;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = BlockPos.containing(target.m_20185_() + extraX, 0.0, target.m_20189_() + extraZ);
        BlockPos ground = target.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        return cockatrice.m_20238_(Vec3.atCenterOf(ground)) > 30.0 && !cockatrice.isTargetBlocked(Vec3.atCenterOf(ground)) ? ground : target.m_20183_();
    }

    public static BlockPos getBlockInTargetsViewGhost(EntityGhost ghost, LivingEntity target) {
        float radius = (float) (4 + ghost.m_217043_().nextInt(5));
        float angle = (float) (Math.PI / 180.0) * (target.yHeadRot + 90.0F + (float) ghost.m_217043_().nextInt(180));
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = BlockPos.containing(target.m_20185_() + extraX, target.m_20186_(), target.m_20189_() + extraZ);
        return ghost.m_20238_(Vec3.atCenterOf(radialPos)) > 30.0 ? radialPos : ghost.m_20183_();
    }

    public static BlockPos getBlockInTargetsViewGorgon(EntityGorgon cockatrice, LivingEntity target) {
        float radius = 6.0F;
        float angle = (float) (Math.PI / 180.0) * target.yHeadRot;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = BlockPos.containing(target.m_20185_() + extraX, target.m_20186_(), target.m_20189_() + extraZ);
        return cockatrice.m_20238_(Vec3.atCenterOf(radialPos)) < 300.0 && !cockatrice.isTargetBlocked(Vec3.atCenterOf(radialPos).add(0.0, 0.75, 0.0)) ? radialPos : target.m_20183_();
    }

    public static BlockPos getBlockInTargetsViewSeaSerpent(EntitySeaSerpent serpent, LivingEntity target) {
        float radius = 10.0F * serpent.getSeaSerpentScale() + (float) serpent.m_217043_().nextInt(10);
        float angle = (float) (Math.PI / 180.0) * target.yHeadRot;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = BlockPos.containing(target.m_20185_() + extraX, 0.0, target.m_20189_() + extraZ);
        BlockPos ground = target.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, radialPos);
        return serpent.m_20238_(Vec3.atCenterOf(ground)) > 30.0 ? ground : target.m_20183_();
    }

    public static boolean canTameDragonAttack(TamableAnimal dragon, Entity entity) {
        if (isVillager(entity)) {
            return false;
        } else if (entity instanceof AbstractVillager || entity instanceof AbstractGolem || entity instanceof Player) {
            return false;
        } else {
            return entity instanceof TamableAnimal ? !((TamableAnimal) entity).isTame() : true;
        }
    }

    public static boolean isVillager(Entity entity) {
        ITagManager<EntityType<?>> tags = ForgeRegistries.ENTITY_TYPES.tags();
        return tags == null ? false : entity.getType().is(tags.createTagKey(IafTagRegistry.VILLAGERS));
    }

    public static boolean isAnimaniaMob(Entity entity) {
        return false;
    }

    public static boolean isDragonTargetable(Entity entity, ResourceLocation tag) {
        return entity.getType().is(ForgeRegistries.ENTITY_TYPES.tags().createTagKey(tag));
    }

    public static String getDimensionName(Level world) {
        return world.dimension().location().toString();
    }

    public static boolean isInHomeDimension(EntityDragonBase dragonBase) {
        return dragonBase.getHomeDimensionName() == null || getDimensionName(dragonBase.m_9236_()).equals(dragonBase.getHomeDimensionName());
    }

    public static boolean canDragonBreak(BlockState state, Entity entity) {
        if (!ForgeEventFactory.getMobGriefingEvent(entity.level(), entity)) {
            return false;
        } else {
            Block block = state.m_60734_();
            return block.getExplosionResistance() < 1200.0F && !state.m_204336_(IafBlockTags.DRAGON_BLOCK_BREAK_BLACKLIST);
        }
    }

    public static boolean hasSameOwner(TamableAnimal cockatrice, Entity entity) {
        return !(entity instanceof TamableAnimal tameable) ? false : tameable.getOwnerUUID() != null && cockatrice.getOwnerUUID() != null && tameable.getOwnerUUID().equals(cockatrice.getOwnerUUID());
    }

    public static boolean isAlive(LivingEntity entity) {
        if (entity instanceof EntityDragonBase dragon && dragon.isMobDead()) {
            return false;
        }
        return (!(entity instanceof IDeadMob deadMob) || !deadMob.isMobDead()) && !EntityGorgon.isStoneMob(entity);
    }

    public static boolean canGrief(EntityDragonBase dragon) {
        return dragon.m_21824_() && !IafConfig.tamedDragonGriefing ? false : IafConfig.dragonGriefing < 2;
    }

    public static boolean canHostilesTarget(Entity entity) {
        if (!(entity instanceof Player) || entity.level().m_46791_() != Difficulty.PEACEFUL && !((Player) entity).isCreative()) {
            return entity instanceof EntityDragonBase && ((EntityDragonBase) entity).isMobDead() ? false : entity instanceof LivingEntity && isAlive((LivingEntity) entity);
        } else {
            return false;
        }
    }

    public static boolean onSameTeam(Entity entity1, Entity entity2) {
        Entity owner1 = null;
        Entity owner2 = null;
        boolean def = entity1.isAlliedTo(entity2);
        if (entity1 instanceof TamableAnimal) {
            owner1 = ((TamableAnimal) entity1).m_269323_();
        }
        if (entity2 instanceof TamableAnimal) {
            owner2 = ((TamableAnimal) entity2).m_269323_();
        }
        if (entity1 instanceof EntityMutlipartPart) {
            Entity multipart = ((EntityMutlipartPart) entity1).getParent();
            if (multipart != null && multipart instanceof TamableAnimal) {
                owner1 = ((TamableAnimal) multipart).m_269323_();
            }
        }
        if (entity2 instanceof EntityMutlipartPart) {
            Entity multipart = ((EntityMutlipartPart) entity2).getParent();
            if (multipart != null && multipart instanceof TamableAnimal) {
                owner2 = ((TamableAnimal) multipart).m_269323_();
            }
        }
        return owner1 != null && owner2 != null ? owner1.is(owner2) : def;
    }

    public static boolean isDreadBlock(BlockState state) {
        Block block = state.m_60734_();
        return block == IafBlockRegistry.DREAD_STONE.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_CHISELED.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_CRACKED.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_MOSSY.get() || block == IafBlockRegistry.DREAD_STONE_TILE.get() || block == IafBlockRegistry.DREAD_STONE_FACE.get() || block == IafBlockRegistry.DREAD_TORCH.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_STAIRS.get() || block == IafBlockRegistry.DREAD_STONE_BRICKS_SLAB.get() || block == IafBlockRegistry.DREADWOOD_LOG.get() || block == IafBlockRegistry.DREADWOOD_PLANKS.get() || block == IafBlockRegistry.DREADWOOD_PLANKS_LOCK.get() || block == IafBlockRegistry.DREAD_PORTAL.get() || block == IafBlockRegistry.DREAD_SPAWNER.get();
    }
}
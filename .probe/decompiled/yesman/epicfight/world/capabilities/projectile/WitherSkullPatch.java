package yesman.epicfight.world.capabilities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.server.SPPlayAnimationInstant;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.mob.WitherSkeletonPatch;
import yesman.epicfight.world.entity.EpicFightEntities;
import yesman.epicfight.world.entity.WitherSkeletonMinion;

public class WitherSkullPatch extends ProjectilePatch<WitherSkull> {

    public void onJoinWorld(WitherSkull projectileEntity, EntityJoinLevelEvent event) {
        super.onJoinWorld(projectileEntity, event);
        this.impact = 1.0F;
    }

    protected void setMaxStrikes(WitherSkull projectileEntity, int maxStrikes) {
    }

    @Override
    public boolean onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult entityHitResult) {
            return entityHitResult.getEntity() instanceof WitherSkeletonMinion;
        } else {
            if (Math.random() < 0.2) {
                Vec3 location = event.getRayTraceResult().getLocation();
                BlockPos blockpos = new BlockPos.MutableBlockPos(location.x, location.y, location.z);
                Projectile projectile = event.getProjectile();
                ServerLevel level = (ServerLevel) projectile.m_9236_();
                EntityType<?> entityType = EpicFightEntities.WITHER_SKELETON_MINION.get();
                if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(entityType), level, blockpos, entityType) && SpawnPlacements.checkSpawnRules(entityType, level, MobSpawnType.REINFORCEMENT, blockpos, level.f_46441_)) {
                    WitherBoss summoner = projectile.getOwner() instanceof WitherBoss ? (WitherBoss) projectile.getOwner() : null;
                    WitherSkeletonMinion witherskeletonminion = new WitherSkeletonMinion(level, summoner, projectile.m_20185_(), projectile.m_20186_() + 0.1, projectile.m_20189_());
                    witherskeletonminion.m_6518_(level, level.m_6436_(blockpos), MobSpawnType.REINFORCEMENT, null, null);
                    witherskeletonminion.m_146922_(projectile.m_146908_() - 180.0F);
                    level.addFreshEntity(witherskeletonminion);
                    WitherSkeletonPatch<?> witherskeletonpatch = EpicFightCapabilities.getEntityPatch(witherskeletonminion, WitherSkeletonPatch.class);
                    witherskeletonpatch.playAnimationSynchronized(Animations.WITHER_SKELETON_SPECIAL_SPAWN, 0.0F, SPPlayAnimationInstant::new);
                }
            }
            return false;
        }
    }
}
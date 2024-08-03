package noppes.npcs.api.wrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.Node;
import noppes.npcs.api.IPos;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.IMob;

public class EntityLivingWrapper<T extends Mob> extends EntityLivingBaseWrapper<T> implements IMob {

    public EntityLivingWrapper(T entity) {
        super(entity);
    }

    @Override
    public void navigateTo(double x, double y, double z, double speed) {
        this.entity.getNavigation().stop();
        this.entity.getNavigation().moveTo(x, y, z, speed * 0.7);
    }

    @Override
    public void clearNavigation() {
        this.entity.getNavigation().stop();
    }

    @Override
    public IPos getNavigationPath() {
        if (!this.isNavigating()) {
            return null;
        } else {
            Node point = this.entity.getNavigation().getPath().getEndNode();
            return point == null ? null : new BlockPosWrapper(new BlockPos(point.x, point.y, point.z));
        }
    }

    @Override
    public boolean isNavigating() {
        return !this.entity.getNavigation().isDone();
    }

    @Override
    public boolean isAttacking() {
        return super.isAttacking() || this.entity.getTarget() != null;
    }

    @Override
    public void setAttackTarget(IEntityLiving living) {
        if (living == null) {
            this.entity.setTarget(null);
        } else {
            this.entity.setTarget(living.getMCEntity());
        }
        super.setAttackTarget(living);
    }

    @Override
    public IEntityLiving getAttackTarget() {
        IEntityLiving base = (IEntityLiving) NpcAPI.Instance().getIEntity(this.entity.getTarget());
        return base != null ? base : super.getAttackTarget();
    }

    @Override
    public boolean canSeeEntity(IEntity entity) {
        return this.entity.getSensing().hasLineOfSight(entity.getMCEntity());
    }

    @Override
    public void jump() {
        this.entity.getJumpControl().jump();
    }
}
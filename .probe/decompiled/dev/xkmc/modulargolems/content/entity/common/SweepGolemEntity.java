package dev.xkmc.modulargolems.content.entity.common;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@SerialClass
public abstract class SweepGolemEntity<T extends SweepGolemEntity<T, P>, P extends IGolemPart<P>> extends AbstractGolemEntity<T, P> {

    protected SweepGolemEntity(EntityType<T> type, Level level) {
        super(type, level);
    }

    protected boolean performRangedDamage(Entity target, float damage, double kb) {
        boolean flag = this.performDamageTarget(target, damage, kb);
        double range = this.m_21133_((Attribute) GolemTypes.GOLEM_SWEEP.get());
        if (range > 0.0) {
            for (Entity t : this.m_9236_().getEntities(target, this.getAttackBoundingBox(target, range), e -> {
                if (e instanceof LivingEntity le && e instanceof Enemy && !(e instanceof Creeper) && this.m_6779_(le)) {
                    return true;
                }
                return false;
            })) {
                flag |= this.performDamageTarget(t, damage, kb);
            }
        }
        return flag;
    }

    protected AABB getAttackBoundingBox(Entity target, double range) {
        return target.getBoundingBox().inflate(range);
    }

    protected abstract boolean performDamageTarget(Entity var1, float var2, double var3);
}
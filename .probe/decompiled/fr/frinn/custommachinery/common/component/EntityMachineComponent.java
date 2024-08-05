package fr.frinn.custommachinery.common.component;

import com.google.common.base.Suppliers;
import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.CustomMachineDamageSource;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class EntityMachineComponent extends AbstractMachineComponent {

    private final Supplier<CustomMachineDamageSource> damageSource;

    public EntityMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.BOTH);
        this.damageSource = Suppliers.memoize(() -> new CustomMachineDamageSource(manager.getTile()));
    }

    @Override
    public MachineComponentType<EntityMachineComponent> getType() {
        return (MachineComponentType<EntityMachineComponent>) Registration.ENTITY_MACHINE_COMPONENT.get();
    }

    public int getEntitiesInRadius(int radius, Predicate<Entity> filter) {
        BlockPos pos = this.getManager().getTile().m_58899_();
        AABB bb = new AABB((double) (pos.m_123341_() - radius), (double) (pos.m_123342_() - radius), (double) (pos.m_123343_() - radius), (double) (pos.m_123341_() + radius), (double) (pos.m_123342_() + radius), (double) (pos.m_123343_() + radius));
        return this.getManager().getLevel().m_6443_(Entity.class, bb, entity -> entity.distanceToSqr(Utils.vec3dFromBlockPos(pos)) <= (double) (radius * radius) && filter.test(entity)).size();
    }

    public double getEntitiesInRadiusHealth(int radius, Predicate<Entity> filter) {
        BlockPos pos = this.getManager().getTile().m_58899_();
        AABB bb = new AABB((double) (pos.m_123341_() - radius), (double) (pos.m_123342_() - radius), (double) (pos.m_123343_() - radius), (double) (pos.m_123341_() + radius), (double) (pos.m_123342_() + radius), (double) (pos.m_123343_() + radius));
        return this.getManager().getLevel().m_6443_(LivingEntity.class, bb, entity -> filter.test(entity) && entity.m_20238_(Utils.vec3dFromBlockPos(pos)) <= (double) (radius * radius)).stream().mapToDouble(LivingEntity::m_21223_).sum();
    }

    public void removeEntitiesHealth(int radius, Predicate<Entity> filter, int amount) {
        BlockPos pos = this.getManager().getTile().m_58899_();
        AtomicInteger toRemove = new AtomicInteger(amount);
        AABB bb = new AABB((double) (pos.m_123341_() - radius), (double) (pos.m_123342_() - radius), (double) (pos.m_123343_() - radius), (double) (pos.m_123341_() + radius), (double) (pos.m_123342_() + radius), (double) (pos.m_123343_() + radius));
        this.getManager().getLevel().m_6443_(LivingEntity.class, bb, entity -> filter.test(entity) && entity.m_20238_(Utils.vec3dFromBlockPos(pos)) <= (double) (radius * radius)).forEach(entity -> {
            int maxRemove = Math.min((int) entity.getHealth(), toRemove.get());
            entity.hurt((DamageSource) this.damageSource.get(), (float) maxRemove);
            toRemove.addAndGet(-maxRemove);
        });
    }

    public void killEntities(int radius, Predicate<Entity> filter, int amount) {
        BlockPos pos = this.getManager().getTile().m_58899_();
        AABB bb = new AABB((double) (pos.m_123341_() - radius), (double) (pos.m_123342_() - radius), (double) (pos.m_123343_() - radius), (double) (pos.m_123341_() + radius), (double) (pos.m_123342_() + radius), (double) (pos.m_123343_() + radius));
        this.getManager().getLevel().m_6443_(LivingEntity.class, bb, entity -> filter.test(entity) && entity.m_20238_(Utils.vec3dFromBlockPos(pos)) <= (double) (radius * radius)).stream().limit((long) amount).forEach(entity -> entity.hurt((DamageSource) this.damageSource.get(), Float.MAX_VALUE));
    }
}
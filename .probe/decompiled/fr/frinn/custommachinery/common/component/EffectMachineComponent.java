package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class EffectMachineComponent extends AbstractMachineComponent {

    public EffectMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.NONE);
    }

    @Override
    public MachineComponentType<EffectMachineComponent> getType() {
        return (MachineComponentType<EffectMachineComponent>) Registration.EFFECT_MACHINE_COMPONENT.get();
    }

    public void applyEffect(MobEffectInstance effect, int radius, Predicate<Entity> filter) {
        BlockPos machinePos = this.getManager().getTile().m_58899_();
        AABB bb = new AABB(machinePos).inflate((double) radius);
        this.getManager().getLevel().m_6443_(LivingEntity.class, bb, filter).stream().filter(entity -> entity.m_20275_((double) machinePos.m_123341_(), (double) machinePos.m_123342_(), (double) machinePos.m_123343_()) < (double) (radius * radius)).forEach(entity -> entity.addEffect(effect));
    }
}
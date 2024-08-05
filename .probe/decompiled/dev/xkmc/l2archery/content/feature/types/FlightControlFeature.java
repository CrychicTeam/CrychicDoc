package dev.xkmc.l2archery.content.feature.types;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.Vec3;

public class FlightControlFeature implements BowArrowFeature {

    public static final FlightControlFeature INSTANCE = new FlightControlFeature();

    public float gravity = 0.05F;

    public float inertia = 0.99F;

    public float water_inertia = 0.6F;

    public int life = -1;

    public int ground_life = 1200;

    public void tickMotion(GenericArrowEntity entity, Vec3 velocity) {
        float inertia = entity.m_20069_() ? this.water_inertia : this.inertia;
        velocity = velocity.scale((double) inertia);
        float grav = !entity.m_20068_() && !entity.m_36797_() ? this.gravity : 0.0F;
        entity.m_20334_(velocity.x, velocity.y - (double) grav, velocity.z);
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
    }
}
package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;

public class SparkBehaviorComponent implements LodestoneBehaviorComponent {

    protected final GenericParticleData lengthData;

    protected Vec3 cachedDirection;

    public SparkBehaviorComponent(GenericParticleData lengthData) {
        this.lengthData = lengthData;
    }

    public SparkBehaviorComponent() {
        this(null);
    }

    @Override
    public void tick(LodestoneWorldParticle particle) {
        Vec3 direction = particle.getParticleSpeed().normalize();
        if (!direction.equals(Vec3.ZERO)) {
            this.cachedDirection = direction;
        }
    }

    public GenericParticleData getLengthData(LodestoneWorldParticle particle) {
        return this.getLengthData() != null ? this.getLengthData() : particle.scaleData;
    }

    public Vec3 getDirection(LodestoneWorldParticle particle) {
        return this.getCachedDirection() != null ? this.getCachedDirection() : particle.getParticleSpeed().normalize();
    }

    @Override
    public LodestoneParticleBehavior getBehaviorType() {
        return LodestoneParticleBehavior.SPARK;
    }

    public GenericParticleData getLengthData() {
        return this.lengthData;
    }

    public Vec3 getCachedDirection() {
        return this.cachedDirection;
    }

    public Vec3 sparkStart(Vec3 pos, Vec3 offset) {
        return pos.subtract(offset);
    }

    public Vec3 sparkEnd(Vec3 pos, Vec3 offset) {
        return pos.add(offset);
    }
}
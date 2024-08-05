package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import java.util.function.Function;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.LodestoneParticleBehavior;

public class DirectionalBehaviorComponent implements LodestoneBehaviorComponent {

    private final Function<LodestoneWorldParticle, Vec3> direction;

    protected DirectionalBehaviorComponent(Function<LodestoneWorldParticle, Vec3> direction) {
        this.direction = direction;
    }

    public DirectionalBehaviorComponent(Vec3 direction) {
        this(p -> direction);
    }

    public DirectionalBehaviorComponent() {
        this(p -> p.getParticleSpeed().normalize());
    }

    public Vec3 getDirection(LodestoneWorldParticle particle) {
        return (Vec3) this.direction.apply(particle);
    }

    @Override
    public LodestoneParticleBehavior getBehaviorType() {
        return LodestoneParticleBehavior.DIRECTIONAL;
    }
}
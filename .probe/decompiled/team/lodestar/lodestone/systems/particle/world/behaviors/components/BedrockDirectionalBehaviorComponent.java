package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.systems.particle.builder.AbstractParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class BedrockDirectionalBehaviorComponent extends DirectionalBehaviorComponent {

    private SpinParticleData pitchData;

    private SpinParticleData yawData;

    public float pitch;

    public float yaw;

    public BedrockDirectionalBehaviorComponent(SpinParticleData pitchData, SpinParticleData yawData) {
        this.pitchData = pitchData;
        this.yawData = yawData;
        this.pitch = pitchData.spinOffset + pitchData.startingValue;
        this.yaw = yawData.spinOffset + yawData.startingValue;
    }

    public BedrockDirectionalBehaviorComponent() {
        this.pitchData = null;
        this.yawData = null;
    }

    public BedrockDirectionalBehaviorComponent(SpinParticleData data) {
        this(data, data);
    }

    @Override
    public Vec3 getDirection(LodestoneWorldParticle particle) {
        float x = (float) (Math.cos((double) this.pitch) * Math.cos((double) this.yaw));
        float y = (float) Math.sin((double) this.pitch);
        float z = (float) (Math.cos((double) this.pitch) * Math.sin((double) this.yaw));
        return new Vec3((double) x, (double) y, (double) z).normalize();
    }

    @Override
    public void tick(LodestoneWorldParticle particle) {
        this.pitch = this.pitch + this.getPitchData(particle.spinData).getValue((float) particle.getAge(), (float) particle.getLifetime());
        this.yaw = this.yaw + this.getYawData(particle.spinData).getValue((float) particle.getAge(), (float) particle.getLifetime());
    }

    public SpinParticleData getPitchData(AbstractParticleBuilder<WorldParticleOptions> builder) {
        return this.getPitchData(builder.getSpinData());
    }

    public SpinParticleData getYawData(AbstractParticleBuilder<WorldParticleOptions> builder) {
        return this.getYawData(builder.getSpinData());
    }

    public SpinParticleData getPitchData(SpinParticleData delegate) {
        return this.pitchData != null ? this.pitchData : delegate;
    }

    public SpinParticleData getYawData(SpinParticleData delegate) {
        return this.yawData != null ? this.yawData : delegate;
    }
}
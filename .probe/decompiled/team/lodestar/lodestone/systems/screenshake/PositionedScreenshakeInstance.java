package team.lodestar.lodestone.systems.screenshake;

import net.minecraft.client.Camera;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.easing.Easing;

public class PositionedScreenshakeInstance extends ScreenshakeInstance {

    public final Vec3 position;

    public final float falloffDistance;

    public final float maxDistance;

    public final Easing falloffEasing;

    public PositionedScreenshakeInstance(int duration, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        super(duration);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.falloffEasing = falloffEasing;
    }

    public PositionedScreenshakeInstance(int duration, Vec3 position, float falloffDistance, float maxDistance) {
        this(duration, position, falloffDistance, maxDistance, Easing.LINEAR);
    }

    @Override
    public float updateIntensity(Camera camera, RandomSource random) {
        float intensity = super.updateIntensity(camera, random);
        float distance = (float) this.position.distanceTo(camera.getPosition());
        if (distance > this.maxDistance) {
            return 0.0F;
        } else {
            float distanceMultiplier = 1.0F;
            if (distance > this.falloffDistance) {
                float remaining = this.maxDistance - this.falloffDistance;
                float current = distance - this.falloffDistance;
                distanceMultiplier = 1.0F - current / remaining;
            }
            Vector3f lookDirection = camera.getLookVector();
            Vec3 directionToScreenshake = this.position.subtract(camera.getPosition()).normalize();
            float angle = Math.max(0.0F, lookDirection.dot(new Vector3f((float) directionToScreenshake.x, (float) directionToScreenshake.y, (float) directionToScreenshake.z)));
            return (intensity * distanceMultiplier + intensity * angle) * 0.5F;
        }
    }
}
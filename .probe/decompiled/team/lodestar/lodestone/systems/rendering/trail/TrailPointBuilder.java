package team.lodestar.lodestone.systems.rendering.trail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4f;

public class TrailPointBuilder {

    private final List<TrailPoint> trailPoints = new ArrayList();

    public final Supplier<Integer> trailLength;

    public TrailPointBuilder(Supplier<Integer> trailLength) {
        this.trailLength = trailLength;
    }

    public static TrailPointBuilder create(int trailLength) {
        return create(() -> trailLength);
    }

    public static TrailPointBuilder create(Supplier<Integer> trailLength) {
        return new TrailPointBuilder(trailLength);
    }

    public List<TrailPoint> getTrailPoints() {
        return this.trailPoints;
    }

    public List<TrailPoint> getTrailPoints(float lerp) {
        List<TrailPoint> lerpedTrailPoints = new ArrayList();
        int size = this.trailPoints.size();
        if (size > 1) {
            for (int i = 0; i < size - 2; i++) {
                lerpedTrailPoints.add(((TrailPoint) this.trailPoints.get(i)).lerp((TrailPoint) this.trailPoints.get(i + 1), lerp));
            }
        }
        return lerpedTrailPoints;
    }

    public TrailPointBuilder addTrailPoint(Vec3 point) {
        return this.addTrailPoint(new TrailPoint(point, 0));
    }

    public TrailPointBuilder addTrailPoint(TrailPoint point) {
        this.trailPoints.add(point);
        return this;
    }

    public TrailPointBuilder tickTrailPoints() {
        int trailLength = (Integer) this.trailLength.get();
        this.trailPoints.forEach(TrailPoint::tick);
        this.trailPoints.removeIf(p -> p.getTimeActive() > trailLength);
        return this;
    }

    public List<Vector4f> build() {
        return (List<Vector4f>) this.trailPoints.stream().map(TrailPoint::getMatrixPosition).collect(Collectors.toList());
    }
}
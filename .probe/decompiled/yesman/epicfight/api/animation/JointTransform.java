package yesman.epicfight.api.animation;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.MatrixOperation;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;

public class JointTransform {

    public static final String ANIMATION_TRANSFROM = "animation_transform";

    public static final String JOINT_LOCAL_TRANSFORM = "joint_local_transform";

    public static final String PARENT = "parent";

    public static final String RESULT1 = "front_result";

    public static final String RESULT2 = "overwrite_rotation";

    private final Map<String, JointTransform.TransformEntry> entries = Maps.newHashMap();

    private final Vec3f translation;

    private final Vec3f scale;

    private final Quaternionf rotation;

    public JointTransform(Vec3f translation, Quaternionf rotation, Vec3f scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vec3f translation() {
        return this.translation;
    }

    public Quaternionf rotation() {
        return this.rotation;
    }

    public Vec3f scale() {
        return this.scale;
    }

    public JointTransform copy() {
        return empty().copyFrom(this);
    }

    public JointTransform copyFrom(JointTransform jt) {
        Vec3f newV = jt.translation();
        Quaternionf newQ = jt.rotation();
        Vec3f newS = jt.scale;
        this.translation.set(newV);
        this.rotation.set(newQ);
        this.scale.set(newS);
        for (Entry<String, JointTransform.TransformEntry> entry : jt.entries.entrySet()) {
            this.entries.put((String) entry.getKey(), (JointTransform.TransformEntry) entry.getValue());
        }
        return this;
    }

    public void jointLocal(JointTransform transform, MatrixOperation multiplyFunction) {
        this.entries.put("joint_local_transform", new JointTransform.TransformEntry(multiplyFunction, transform));
    }

    public void parent(JointTransform transform, MatrixOperation multiplyFunction) {
        this.entries.put("parent", new JointTransform.TransformEntry(multiplyFunction, transform));
    }

    public void frontResult(JointTransform transform, MatrixOperation multiplyFunction) {
        this.entries.put("front_result", new JointTransform.TransformEntry(multiplyFunction, transform));
    }

    public void overwriteRotation(JointTransform transform) {
        this.entries.put("overwrite_rotation", new JointTransform.TransformEntry(OpenMatrix4f::mul, transform));
    }

    public OpenMatrix4f getAnimationBindedMatrix(Joint joint, OpenMatrix4f parentTransform) {
        OpenMatrix4f.AnimationTransformEntry animationTransformEntry = new OpenMatrix4f.AnimationTransformEntry();
        for (Entry<String, JointTransform.TransformEntry> entry : this.entries.entrySet()) {
            animationTransformEntry.put((String) entry.getKey(), ((JointTransform.TransformEntry) entry.getValue()).transform.toMatrix(), ((JointTransform.TransformEntry) entry.getValue()).multiplyFunction);
        }
        animationTransformEntry.put("animation_transform", this.toMatrix(), OpenMatrix4f::mul);
        animationTransformEntry.put("joint_local_transform", joint.getLocalTrasnform());
        animationTransformEntry.put("parent", parentTransform);
        animationTransformEntry.put("animation_transform", joint.getPoseTransform());
        return animationTransformEntry.getResult();
    }

    public OpenMatrix4f toMatrix() {
        return new OpenMatrix4f().translate(this.translation).mulBack(OpenMatrix4f.fromQuaternion(this.rotation)).scale(this.scale);
    }

    public String toString() {
        return String.format("translation:%s, rotation:%s, %d entries ", this.translation, this.rotation, this.entries.size());
    }

    private static JointTransform interpolateSimple(JointTransform prev, JointTransform next, float progression) {
        return new JointTransform(MathUtils.lerpVector(prev.translation, next.translation, progression), MathUtils.lerpQuaternion(prev.rotation, next.rotation, progression), MathUtils.lerpVector(prev.scale, next.scale, progression));
    }

    public static JointTransform interpolate(JointTransform prev, JointTransform next, float progression) {
        if (prev != null && next != null) {
            progression = Mth.clamp(progression, 0.0F, 1.0F);
            JointTransform interpolated = interpolateSimple(prev, next, progression);
            for (Entry<String, JointTransform.TransformEntry> entry : prev.entries.entrySet()) {
                JointTransform transform = next.entries.containsKey(entry.getKey()) ? ((JointTransform.TransformEntry) next.entries.get(entry.getKey())).transform : empty();
                interpolated.entries.put((String) entry.getKey(), new JointTransform.TransformEntry(((JointTransform.TransformEntry) entry.getValue()).multiplyFunction, interpolateSimple(((JointTransform.TransformEntry) entry.getValue()).transform, transform, progression)));
            }
            for (Entry<String, JointTransform.TransformEntry> entry : next.entries.entrySet()) {
                if (!interpolated.entries.containsKey(entry.getKey())) {
                    interpolated.entries.put((String) entry.getKey(), new JointTransform.TransformEntry(((JointTransform.TransformEntry) entry.getValue()).multiplyFunction, interpolateSimple(empty(), ((JointTransform.TransformEntry) entry.getValue()).transform, progression)));
                }
            }
            return interpolated;
        } else {
            return empty();
        }
    }

    public static JointTransform fromMatrixNoScale(OpenMatrix4f matrix) {
        return new JointTransform(matrix.toTranslationVector(), matrix.toQuaternion(), new Vec3f(1.0F, 1.0F, 1.0F));
    }

    public static JointTransform getTranslation(Vec3f vec) {
        return translationRotation(vec, new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F));
    }

    public static JointTransform getRotation(Quaternionf quat) {
        return translationRotation(new Vec3f(0.0F, 0.0F, 0.0F), quat);
    }

    public static JointTransform getScale(Vec3f vec) {
        return new JointTransform(new Vec3f(1.0F, 1.0F, 1.0F), new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F), vec);
    }

    public static JointTransform fromMatrix(OpenMatrix4f matrix) {
        return new JointTransform(matrix.toTranslationVector(), matrix.toQuaternion(), matrix.toScaleVector());
    }

    public static JointTransform translationRotation(Vec3f vec, Quaternionf quat) {
        return new JointTransform(vec, quat, new Vec3f(1.0F, 1.0F, 1.0F));
    }

    public static JointTransform empty() {
        return new JointTransform(new Vec3f(0.0F, 0.0F, 0.0F), new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F), new Vec3f(1.0F, 1.0F, 1.0F));
    }

    public static class TransformEntry {

        public final MatrixOperation multiplyFunction;

        public final JointTransform transform;

        public TransformEntry(MatrixOperation multiplyFunction, JointTransform transform) {
            this.multiplyFunction = multiplyFunction;
            this.transform = transform;
        }
    }
}
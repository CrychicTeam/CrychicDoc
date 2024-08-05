package yesman.epicfight.api.client.animation.property;

import com.mojang.datafixers.util.Pair;
import java.util.Map;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
public class JointMask {

    public static final JointMask.BindModifier KEEP_CHILD_LOCROT = (entitypatch, baseLayerPose, result, priority, joint, poses) -> {
        Pose currentPose = (Pose) ((Pair) poses.get(priority)).getSecond();
        JointTransform lowestTransform = baseLayerPose.getOrDefaultTransform(joint.getName());
        JointTransform currentTransform = currentPose.getOrDefaultTransform(joint.getName());
        ((JointTransform) result.getJointTransformData().getOrDefault(joint.getName(), JointTransform.empty())).translation().y = lowestTransform.translation().y;
        OpenMatrix4f lowestMatrix = lowestTransform.toMatrix();
        OpenMatrix4f currentMatrix = currentTransform.toMatrix();
        OpenMatrix4f currentToLowest = OpenMatrix4f.mul(OpenMatrix4f.invert(currentMatrix, null), lowestMatrix, null);
        for (Joint subJoint : joint.getSubJoints()) {
            if (!((DynamicAnimation) ((Pair) poses.get(priority)).getFirst()).isJointEnabled(entitypatch, priority, subJoint.getName())) {
                OpenMatrix4f lowestLocalTransform = OpenMatrix4f.mul(joint.getLocalTrasnform(), lowestMatrix, null);
                OpenMatrix4f currentLocalTransform = OpenMatrix4f.mul(joint.getLocalTrasnform(), currentMatrix, null);
                OpenMatrix4f childTransform = OpenMatrix4f.mul(subJoint.getLocalTrasnform(), result.getOrDefaultTransform(subJoint.getName()).toMatrix(), null);
                OpenMatrix4f lowestFinal = OpenMatrix4f.mul(lowestLocalTransform, childTransform, null);
                OpenMatrix4f currentFinal = OpenMatrix4f.mul(currentLocalTransform, childTransform, null);
                Vec3f vec = new Vec3f((currentFinal.m30 - lowestFinal.m30) * 0.5F, currentFinal.m31 - lowestFinal.m31, currentFinal.m32 - lowestFinal.m32);
                JointTransform jt = (JointTransform) result.getJointTransformData().getOrDefault(subJoint.getName(), JointTransform.empty());
                jt.parent(JointTransform.getTranslation(vec), OpenMatrix4f::mul);
                jt.jointLocal(JointTransform.fromMatrixNoScale(currentToLowest), OpenMatrix4f::mul);
            }
        }
    };

    private final String jointName;

    private final JointMask.BindModifier bindModifier;

    public static JointMask of(String jointName, JointMask.BindModifier bindModifier) {
        return new JointMask(jointName, bindModifier);
    }

    public static JointMask of(String jointName) {
        return new JointMask(jointName, null);
    }

    private JointMask(String jointName, JointMask.BindModifier bindModifier) {
        this.jointName = jointName;
        this.bindModifier = bindModifier;
    }

    public boolean equals(Object object) {
        return object instanceof JointMask jointMask ? jointMask.jointName.equals(this.jointName) : super.equals(object);
    }

    public String getJointName() {
        return this.jointName;
    }

    public JointMask.BindModifier getBindModifier() {
        return this.bindModifier;
    }

    @FunctionalInterface
    public interface BindModifier {

        void modify(LivingEntityPatch<?> var1, Pose var2, Pose var3, Layer.Priority var4, Joint var5, Map<Layer.Priority, Pair<DynamicAnimation, Pose>> var6);
    }
}
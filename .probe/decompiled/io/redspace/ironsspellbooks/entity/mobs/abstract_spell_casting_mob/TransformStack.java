package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import java.util.HashMap;
import java.util.Map;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;

public class TransformStack {

    private final Map<CoreGeoBone, Vector3f> positionStack = new HashMap();

    private final Map<CoreGeoBone, Vector3f> rotationStack = new HashMap();

    private boolean needsReset;

    public void pushPosition(CoreGeoBone bone, Vector3f appendVec) {
        Vector3f vec = (Vector3f) this.positionStack.getOrDefault(bone, new Vector3f(0.0F, 0.0F, 0.0F));
        vec.add(appendVec);
        this.positionStack.put(bone, vec);
    }

    public void pushPosition(CoreGeoBone bone, float x, float y, float z) {
        this.pushPosition(bone, new Vector3f(x, y, z));
    }

    public void overridePosition(CoreGeoBone bone, Vector3f newVec) {
        this.positionStack.put(bone, newVec);
    }

    public void pushRotation(CoreGeoBone bone, Vector3f appendVec) {
        Vector3f vec = (Vector3f) this.rotationStack.getOrDefault(bone, new Vector3f(0.0F, 0.0F, 0.0F));
        vec.add(appendVec);
        this.rotationStack.put(bone, vec);
    }

    public void pushRotation(CoreGeoBone bone, float x, float y, float z) {
        this.pushRotation(bone, new Vector3f(x, y, z));
    }

    public void pushRotationWithBase(CoreGeoBone bone, float x, float y, float z) {
        Vector3f base = new Vector3f(bone.getRotX(), bone.getRotY(), bone.getRotZ());
        base.add(x, y, z);
        this.pushRotation(bone, x, y, z);
    }

    public void overrideRotation(CoreGeoBone bone, Vector3f newVec) {
        this.rotationStack.put(bone, newVec);
    }

    public void popStack() {
        this.positionStack.forEach(this::setPosImpl);
        this.rotationStack.forEach(this::setRotImpl);
        this.positionStack.clear();
        this.rotationStack.clear();
    }

    public void setRotImpl(CoreGeoBone bone, Vector3f vector3f) {
        bone.setRotX(wrapRadians(vector3f.x()));
        bone.setRotY(wrapRadians(vector3f.y()));
        bone.setRotZ(wrapRadians(vector3f.z()));
    }

    public void setPosImpl(CoreGeoBone bone, Vector3f vector3f) {
        bone.setPosX(vector3f.x());
        bone.setPosY(vector3f.y());
        bone.setPosZ(vector3f.z());
    }

    public static float wrapRadians(float pValue) {
        float twoPi = 6.2831F;
        float pi = 3.14155F;
        float f = pValue % twoPi;
        if (f >= pi) {
            f -= twoPi;
        }
        if (f < -pi) {
            f += twoPi;
        }
        return f;
    }
}
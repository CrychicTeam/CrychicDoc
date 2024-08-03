package dev.kosmx.playerAnim.api.layered.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.core.util.Vec3f;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class MirrorModifier extends AbstractModifier {

    public static final Map<String, String> mirrorMap;

    private boolean enabled = true;

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        if (!this.isEnabled()) {
            return super.get3DTransform(modelName, type, tickDelta, value0);
        } else {
            if (mirrorMap.containsKey(modelName)) {
                modelName = (String) mirrorMap.get(modelName);
            }
            value0 = this.transformVector(value0, type);
            Vec3f vec3f = super.get3DTransform(modelName, type, tickDelta, value0);
            return this.transformVector(vec3f, type);
        }
    }

    @NotNull
    @Override
    public FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
        FirstPersonConfiguration configuration = super.getFirstPersonConfiguration(tickDelta);
        return this.isEnabled() ? new FirstPersonConfiguration().setShowLeftArm(configuration.isShowRightArm()).setShowRightArm(configuration.isShowLeftArm()).setShowLeftItem(configuration.isShowRightItem()).setShowRightItem(configuration.isShowLeftItem()) : configuration;
    }

    protected Vec3f transformVector(Vec3f value0, TransformType type) {
        switch(type) {
            case POSITION:
                return new Vec3f(-value0.getX(), value0.getY(), value0.getZ());
            case ROTATION:
                return new Vec3f(value0.getX(), -value0.getY(), -value0.getZ());
            case BEND:
                return new Vec3f(value0.getX(), -value0.getY(), value0.getZ());
            default:
                return value0;
        }
    }

    public MirrorModifier() {
    }

    public MirrorModifier(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    static {
        HashMap<String, String> partMap = new HashMap();
        partMap.put("leftArm", "rightArm");
        partMap.put("leftLeg", "rightLeg");
        partMap.put("leftItem", "rightItem");
        partMap.put("rightArm", "leftArm");
        partMap.put("rightLeg", "leftLeg");
        partMap.put("rightItem", "leftItem");
        mirrorMap = Collections.unmodifiableMap(partMap);
    }
}
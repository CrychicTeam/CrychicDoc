package dev.kosmx.playerAnim.api.layered;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.core.util.Vec3f;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerAnimationFrame implements IAnimation {

    protected PlayerAnimationFrame.PlayerPart head = new PlayerAnimationFrame.PlayerPart();

    protected PlayerAnimationFrame.PlayerPart body = new PlayerAnimationFrame.PlayerPart();

    protected PlayerAnimationFrame.PlayerPart rightArm = new PlayerAnimationFrame.PlayerPart();

    protected PlayerAnimationFrame.PlayerPart leftArm = new PlayerAnimationFrame.PlayerPart();

    protected PlayerAnimationFrame.PlayerPart rightLeg = new PlayerAnimationFrame.PlayerPart();

    protected PlayerAnimationFrame.PlayerPart leftLeg = new PlayerAnimationFrame.PlayerPart();

    protected PlayerAnimationFrame.PlayerPart rightItem = new PlayerAnimationFrame.PlayerPart();

    protected PlayerAnimationFrame.PlayerPart leftItem = new PlayerAnimationFrame.PlayerPart();

    HashMap<String, PlayerAnimationFrame.PlayerPart> parts = new HashMap();

    public PlayerAnimationFrame() {
        this.parts.put("head", this.head);
        this.parts.put("body", this.body);
        this.parts.put("rightArm", this.rightArm);
        this.parts.put("leftArm", this.leftArm);
        this.parts.put("rightLeg", this.rightLeg);
        this.parts.put("leftLeg", this.leftLeg);
        this.parts.put("rightItem", this.rightItem);
        this.parts.put("leftItem", this.leftItem);
    }

    @Override
    public void tick() {
        IAnimation.super.tick();
    }

    @Override
    public boolean isActive() {
        for (Entry<String, PlayerAnimationFrame.PlayerPart> entry : this.parts.entrySet()) {
            PlayerAnimationFrame.PlayerPart part = (PlayerAnimationFrame.PlayerPart) entry.getValue();
            if (part.bend != null || part.pos != null || part.rot != null) {
                return true;
            }
        }
        return false;
    }

    public void resetPose() {
        for (Entry<String, PlayerAnimationFrame.PlayerPart> entry : this.parts.entrySet()) {
            ((PlayerAnimationFrame.PlayerPart) entry.getValue()).setNull();
        }
    }

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        PlayerAnimationFrame.PlayerPart part = (PlayerAnimationFrame.PlayerPart) this.parts.get(modelName);
        if (part == null) {
            return value0;
        } else {
            switch(type) {
                case POSITION:
                    return part.pos == null ? value0 : part.pos;
                case ROTATION:
                    return part.rot == null ? value0 : part.rot;
                case BEND:
                    return part.bend == null ? value0 : new Vec3f(part.bend.getLeft(), part.bend.getRight(), 0.0F);
                default:
                    return value0;
            }
        }
    }

    public static class PlayerPart {

        public Vec3f pos;

        public Vec3f rot;

        public Pair<Float, Float> bend;

        protected void setNull() {
            this.pos = this.rot = null;
            this.bend = null;
        }
    }
}
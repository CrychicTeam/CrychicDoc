package dev.kosmx.playerAnim.api.layered;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.MathHelper;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.core.util.Vector3;
import java.util.HashMap;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KeyframeAnimationPlayer implements IAnimation {

    private final KeyframeAnimation data;

    private boolean isRunning = true;

    private int currentTick;

    private boolean isLoopStarted = false;

    protected float tickDelta;

    public final HashMap<String, KeyframeAnimationPlayer.BodyPart> bodyParts;

    public int perspective = 0;

    @NotNull
    private FirstPersonConfiguration firstPersonConfiguration = new FirstPersonConfiguration();

    @NotNull
    private FirstPersonMode firstPersonMode = FirstPersonMode.NONE;

    @NotNull
    @Override
    public FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
        return this.firstPersonConfiguration;
    }

    @NotNull
    @Override
    public FirstPersonMode getFirstPersonMode(float tickDelta) {
        return this.firstPersonMode;
    }

    public KeyframeAnimationPlayer(@NotNull KeyframeAnimation animation, int t, boolean mutable) {
        if (animation == null) {
            throw new IllegalArgumentException("Animation can not be null");
        } else {
            this.data = animation;
            this.bodyParts = new HashMap(animation.getBodyParts().size());
            for (Entry<String, KeyframeAnimation.StateCollection> part : animation.getBodyParts().entrySet()) {
                this.bodyParts.put((String) part.getKey(), new KeyframeAnimationPlayer.BodyPart(mutable ? ((KeyframeAnimation.StateCollection) part.getValue()).copy() : (KeyframeAnimation.StateCollection) part.getValue()));
            }
            this.currentTick = t;
            if (this.isInfinite() && t > this.data.returnToTick) {
                this.currentTick = (t - this.data.returnToTick) % (this.data.endTick - this.data.returnToTick + 1) + this.data.returnToTick;
            }
        }
    }

    public KeyframeAnimationPlayer(@NotNull KeyframeAnimation animation, int t) {
        this(animation, t, false);
    }

    public KeyframeAnimationPlayer(@NotNull KeyframeAnimation animation) {
        this(animation, 0);
    }

    @Override
    public void tick() {
        if (this.isRunning) {
            this.currentTick++;
            if (this.data.isInfinite && this.currentTick > this.data.endTick) {
                this.currentTick = this.data.returnToTick;
                this.isLoopStarted = true;
            }
            if (this.currentTick >= this.data.stopTick) {
                this.stop();
            }
        }
    }

    public int getTick() {
        return this.currentTick;
    }

    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean isActive() {
        return this.isRunning;
    }

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        KeyframeAnimationPlayer.BodyPart part = (KeyframeAnimationPlayer.BodyPart) this.bodyParts.get(modelName);
        if (part == null) {
            return value0;
        } else {
            switch(type) {
                case POSITION:
                    return part.getBodyOffset(value0);
                case ROTATION:
                    Vector3<Float> rot = part.getBodyRotation(value0);
                    return new Vec3f((Float) rot.getX(), (Float) rot.getY(), (Float) rot.getZ());
                case BEND:
                    Pair<Float, Float> bend = part.getBend(new Pair<>(value0.getX(), value0.getY()));
                    return new Vec3f(bend.getLeft(), bend.getRight(), 0.0F);
                default:
                    return value0;
            }
        }
    }

    @Override
    public void setupAnim(float tickDelta) {
        this.tickDelta = tickDelta;
    }

    public boolean isLoopStarted() {
        return this.isLoopStarted;
    }

    public KeyframeAnimation getData() {
        return this.data;
    }

    public KeyframeAnimationPlayer.BodyPart getPart(String string) {
        KeyframeAnimationPlayer.BodyPart part = (KeyframeAnimationPlayer.BodyPart) this.bodyParts.get(string);
        return part == null ? new KeyframeAnimationPlayer.BodyPart(null) : part;
    }

    public int getStopTick() {
        return this.data.stopTick;
    }

    public int getCurrentTick() {
        return this.currentTick;
    }

    public boolean isInfinite() {
        return this.data.isInfinite;
    }

    public KeyframeAnimationPlayer setFirstPersonConfiguration(@NotNull FirstPersonConfiguration firstPersonConfiguration) {
        if (firstPersonConfiguration == null) {
            throw new NullPointerException("firstPersonConfiguration is marked non-null but is null");
        } else {
            this.firstPersonConfiguration = firstPersonConfiguration;
            return this;
        }
    }

    public KeyframeAnimationPlayer setFirstPersonMode(@NotNull FirstPersonMode firstPersonMode) {
        if (firstPersonMode == null) {
            throw new NullPointerException("firstPersonMode is marked non-null but is null");
        } else {
            this.firstPersonMode = firstPersonMode;
            return this;
        }
    }

    public class Axis {

        protected final KeyframeAnimation.StateCollection.State keyframes;

        public Axis(KeyframeAnimation.StateCollection.State keyframes) {
            this.keyframes = keyframes;
        }

        private KeyframeAnimation.KeyFrame findBefore(int pos, float currentState) {
            if (pos == -1) {
                return KeyframeAnimationPlayer.this.currentTick < KeyframeAnimationPlayer.this.data.beginTick ? new KeyframeAnimation.KeyFrame(0, currentState) : (KeyframeAnimationPlayer.this.currentTick < KeyframeAnimationPlayer.this.data.endTick ? new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.data.beginTick, this.keyframes.defaultValue) : new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.data.endTick, this.keyframes.defaultValue));
            } else {
                KeyframeAnimation.KeyFrame frame = (KeyframeAnimation.KeyFrame) this.keyframes.getKeyFrames().get(pos);
                return !KeyframeAnimationPlayer.this.isInfinite() && KeyframeAnimationPlayer.this.currentTick >= KeyframeAnimationPlayer.this.getData().endTick && pos == this.keyframes.length() - 1 && frame.tick < KeyframeAnimationPlayer.this.getData().endTick ? new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.getData().endTick, frame.value, frame.ease) : frame;
            }
        }

        private KeyframeAnimation.KeyFrame findAfter(int pos, float currentState) {
            if (this.keyframes.length() > pos + 1) {
                return (KeyframeAnimation.KeyFrame) this.keyframes.getKeyFrames().get(pos + 1);
            } else if (KeyframeAnimationPlayer.this.isInfinite()) {
                return new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.getData().endTick + 1, this.keyframes.defaultValue);
            } else if (KeyframeAnimationPlayer.this.currentTick < KeyframeAnimationPlayer.this.getData().endTick && this.keyframes.length() > 0) {
                KeyframeAnimation.KeyFrame lastFrame = (KeyframeAnimation.KeyFrame) this.keyframes.getKeyFrames().get(this.keyframes.length() - 1);
                return new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.getData().endTick, lastFrame.value, lastFrame.ease);
            } else {
                return KeyframeAnimationPlayer.this.currentTick >= KeyframeAnimationPlayer.this.data.endTick ? new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.data.stopTick, currentState) : (KeyframeAnimationPlayer.this.currentTick >= KeyframeAnimationPlayer.this.getData().beginTick ? new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.getData().endTick, this.keyframes.defaultValue) : new KeyframeAnimation.KeyFrame(KeyframeAnimationPlayer.this.getData().beginTick, this.keyframes.defaultValue));
            }
        }

        public float getValueAtCurrentTick(float currentValue) {
            if (this.keyframes != null && this.keyframes.isEnabled()) {
                int pos = this.keyframes.findAtTick(KeyframeAnimationPlayer.this.currentTick);
                KeyframeAnimation.KeyFrame keyBefore = this.findBefore(pos, currentValue);
                if (KeyframeAnimationPlayer.this.isLoopStarted && keyBefore.tick < KeyframeAnimationPlayer.this.data.returnToTick) {
                    keyBefore = this.findBefore(this.keyframes.findAtTick(KeyframeAnimationPlayer.this.data.endTick), currentValue);
                }
                KeyframeAnimation.KeyFrame keyAfter = this.findAfter(pos, currentValue);
                if (KeyframeAnimationPlayer.this.data.isInfinite && keyAfter.tick > KeyframeAnimationPlayer.this.data.endTick) {
                    keyAfter = this.findAfter(this.keyframes.findAtTick(KeyframeAnimationPlayer.this.data.returnToTick - 1), currentValue);
                }
                return this.getValueFromKeyframes(keyBefore, keyAfter);
            } else {
                return currentValue;
            }
        }

        protected final float getValueFromKeyframes(KeyframeAnimation.KeyFrame before, KeyframeAnimation.KeyFrame after) {
            int tickBefore = before.tick;
            int tickAfter = after.tick;
            if (tickBefore >= tickAfter) {
                if (KeyframeAnimationPlayer.this.currentTick < tickBefore) {
                    tickBefore -= KeyframeAnimationPlayer.this.data.endTick - KeyframeAnimationPlayer.this.data.returnToTick + 1;
                } else {
                    tickAfter += KeyframeAnimationPlayer.this.data.endTick - KeyframeAnimationPlayer.this.data.returnToTick + 1;
                }
            }
            if (tickBefore == tickAfter) {
                return before.value;
            } else {
                float f = ((float) KeyframeAnimationPlayer.this.currentTick + KeyframeAnimationPlayer.this.tickDelta - (float) tickBefore) / (float) (tickAfter - tickBefore);
                return MathHelper.lerp((KeyframeAnimationPlayer.this.data.isEasingBefore ? after.ease : before.ease).invoke(f), before.value, after.value);
            }
        }
    }

    public class BodyPart {

        @Nullable
        public final KeyframeAnimation.StateCollection part;

        public final KeyframeAnimationPlayer.Axis x;

        public final KeyframeAnimationPlayer.Axis y;

        public final KeyframeAnimationPlayer.Axis z;

        public final KeyframeAnimationPlayer.RotationAxis pitch;

        public final KeyframeAnimationPlayer.RotationAxis yaw;

        public final KeyframeAnimationPlayer.RotationAxis roll;

        public final KeyframeAnimationPlayer.RotationAxis bendAxis;

        public final KeyframeAnimationPlayer.RotationAxis bend;

        public BodyPart(@Nullable KeyframeAnimation.StateCollection part) {
            this.part = part;
            if (part != null) {
                this.x = KeyframeAnimationPlayer.this.new Axis(part.x);
                this.y = KeyframeAnimationPlayer.this.new Axis(part.y);
                this.z = KeyframeAnimationPlayer.this.new Axis(part.z);
                this.pitch = KeyframeAnimationPlayer.this.new RotationAxis(part.pitch);
                this.yaw = KeyframeAnimationPlayer.this.new RotationAxis(part.yaw);
                this.roll = KeyframeAnimationPlayer.this.new RotationAxis(part.roll);
                this.bendAxis = KeyframeAnimationPlayer.this.new RotationAxis(part.bendDirection);
                this.bend = KeyframeAnimationPlayer.this.new RotationAxis(part.bend);
            } else {
                this.x = null;
                this.y = null;
                this.z = null;
                this.pitch = null;
                this.yaw = null;
                this.roll = null;
                this.bendAxis = null;
                this.bend = null;
            }
        }

        public Pair<Float, Float> getBend(Pair<Float, Float> value0) {
            return this.bend == null ? value0 : new Pair<>(this.bendAxis.getValueAtCurrentTick(value0.getLeft()), this.bend.getValueAtCurrentTick(value0.getRight()));
        }

        public Vec3f getBodyOffset(Vec3f value0) {
            if (this.part == null) {
                return value0;
            } else {
                float x = this.x.getValueAtCurrentTick(value0.getX());
                float y = this.y.getValueAtCurrentTick(value0.getY());
                float z = this.z.getValueAtCurrentTick(value0.getZ());
                return new Vec3f(x, y, z);
            }
        }

        public Vec3f getBodyRotation(Vec3f value0) {
            return this.part == null ? value0 : new Vec3f(this.pitch.getValueAtCurrentTick(value0.getX()), this.yaw.getValueAtCurrentTick(value0.getY()), this.roll.getValueAtCurrentTick(value0.getZ()));
        }
    }

    public class RotationAxis extends KeyframeAnimationPlayer.Axis {

        public RotationAxis(KeyframeAnimation.StateCollection.State keyframes) {
            super(keyframes);
        }

        @Override
        public float getValueAtCurrentTick(float currentValue) {
            return MathHelper.clampToRadian(super.getValueAtCurrentTick(MathHelper.clampToRadian(currentValue)));
        }
    }
}
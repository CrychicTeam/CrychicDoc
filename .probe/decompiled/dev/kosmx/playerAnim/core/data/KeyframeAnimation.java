package dev.kosmx.playerAnim.core.data;

import dev.kosmx.playerAnim.core.data.opennbs.NBS;
import dev.kosmx.playerAnim.core.util.Ease;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Supplier;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.Nullable;

@Immutable
public final class KeyframeAnimation implements Supplier<UUID> {

    public static final KeyframeAnimation.StateCollection.State EMPTY_STATE = new KeyframeAnimation.StateCollection.State("empty", 0.0F, 0.0F, false);

    public final int beginTick;

    public final int endTick;

    public final int stopTick;

    public final boolean isInfinite;

    public final int returnToTick;

    private final Map<String, KeyframeAnimation.StateCollection> bodyParts;

    public final boolean isEasingBefore;

    public final boolean nsfw;

    private final UUID uuid;

    public final boolean isUUIDGenerated;

    public final HashMap<String, Object> extraData = new HashMap();

    public final AnimationFormat animationFormat;

    private KeyframeAnimation(int beginTick, int endTick, int stopTick, boolean isInfinite, int returnToTick, HashMap<String, KeyframeAnimation.StateCollection> bodyParts, boolean isEasingBefore, boolean nsfw, UUID uuid, AnimationFormat emoteFormat, HashMap<String, Object> extraData) {
        this.beginTick = Math.max(beginTick, 0);
        this.endTick = Math.max(beginTick + 1, endTick);
        this.stopTick = stopTick <= endTick ? endTick + 3 : stopTick;
        this.isInfinite = isInfinite;
        if (!isInfinite || returnToTick >= 0 && returnToTick <= endTick) {
            this.returnToTick = returnToTick;
            HashMap<String, KeyframeAnimation.StateCollection> bodyMap = new HashMap();
            for (Entry<String, KeyframeAnimation.StateCollection> entry : bodyParts.entrySet()) {
                bodyMap.put((String) entry.getKey(), ((KeyframeAnimation.StateCollection) entry.getValue()).copy());
            }
            bodyMap.forEach((s, stateCollection) -> stateCollection.verifyAndLock(this.getLength()));
            this.bodyParts = Collections.unmodifiableMap(bodyMap);
            this.isEasingBefore = isEasingBefore;
            this.nsfw = nsfw;
            if (uuid == null) {
                this.isUUIDGenerated = true;
                uuid = this.generateUuid();
            } else {
                this.isUUIDGenerated = false;
            }
            this.uuid = uuid;
            this.animationFormat = emoteFormat;
            assert emoteFormat != null;
            this.extraData.putAll(extraData);
        } else {
            throw new IllegalArgumentException("Trying to construct invalid animation");
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof KeyframeAnimation)) {
            return false;
        } else {
            KeyframeAnimation emoteData = (KeyframeAnimation) o;
            if (this.beginTick != emoteData.beginTick) {
                return false;
            } else if (this.endTick != emoteData.endTick) {
                return false;
            } else if (this.stopTick != emoteData.stopTick) {
                return false;
            } else if (this.isInfinite != emoteData.isInfinite) {
                return false;
            } else if (this.returnToTick != emoteData.returnToTick) {
                return false;
            } else {
                return this.isEasingBefore != emoteData.isEasingBefore ? false : this.bodyParts.equals(emoteData.bodyParts);
            }
        }
    }

    public int hashCode() {
        int result = this.beginTick;
        result = 31 * result + this.endTick;
        result = 31 * result + this.stopTick;
        result = 31 * result + (this.isInfinite ? 1 : 0);
        result = 31 * result + this.returnToTick;
        result = 31 * result + (this.isEasingBefore ? 1 : 0);
        return 31 * result + this.bodyParts.hashCode();
    }

    private UUID generateUuid() {
        int result = this.beginTick;
        result = 31 * result + this.endTick;
        result = 31 * result + this.stopTick;
        result = 31 * result + (this.isInfinite ? 1 : 0);
        result = 31 * result + this.returnToTick;
        result = 31 * result + (this.isEasingBefore ? 1 : 0);
        long dataHash = (long) result * 31L + (long) this.bodyParts.hashCode();
        long nameHash = (long) this.extraData.hashCode();
        long descHash = 0L;
        long authHash = (long) result * 31L + (long) this.extraData.hashCode();
        return new UUID(dataHash << (int) (32L + nameHash), descHash << (int) (32L + authHash));
    }

    public KeyframeAnimation copy() {
        return this.mutableCopy().build();
    }

    public KeyframeAnimation.AnimationBuilder mutableCopy() {
        HashMap<String, KeyframeAnimation.StateCollection> newParts = new HashMap();
        for (Entry<String, KeyframeAnimation.StateCollection> part : this.bodyParts.entrySet()) {
            newParts.put((String) part.getKey(), ((KeyframeAnimation.StateCollection) part.getValue()).copy());
        }
        return new KeyframeAnimation.AnimationBuilder(this.beginTick, this.endTick, this.stopTick, this.isInfinite, this.returnToTick, newParts, this.isEasingBefore, this.nsfw, this.uuid, this.animationFormat, this.extraData);
    }

    public boolean isPlayingAt(int tick) {
        return this.isInfinite || tick < this.stopTick && tick > 0;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public UUID get() {
        return this.uuid;
    }

    public int getLength() {
        return this.stopTick;
    }

    public boolean isInfinite() {
        return this.isInfinite;
    }

    @Nullable
    public KeyframeAnimation.StateCollection getPart(String partID) {
        return (KeyframeAnimation.StateCollection) this.bodyParts.get(partID);
    }

    public Optional<KeyframeAnimation.StateCollection> getPartOptional(String id) {
        return Optional.ofNullable(this.getPart(id));
    }

    public String toString() {
        return "KeyframeAnimation{uuid=" + this.uuid + ", length=" + this.getLength() + ", extra=" + this.extraData + '}';
    }

    public Map<String, KeyframeAnimation.StateCollection> getBodyParts() {
        return this.bodyParts;
    }

    public static class AnimationBuilder {

        public static float staticThreshold = 8.0F;

        public final KeyframeAnimation.StateCollection head;

        public final KeyframeAnimation.StateCollection body;

        public final KeyframeAnimation.StateCollection rightArm;

        public final KeyframeAnimation.StateCollection leftArm;

        public final KeyframeAnimation.StateCollection rightLeg;

        public final KeyframeAnimation.StateCollection leftLeg;

        public final KeyframeAnimation.StateCollection leftItem;

        public final KeyframeAnimation.StateCollection rightItem;

        public final KeyframeAnimation.StateCollection torso;

        public boolean isEasingBefore = false;

        public boolean nsfw = false;

        private final HashMap<String, KeyframeAnimation.StateCollection> bodyParts = new HashMap();

        @Nullable
        public UUID uuid = null;

        public int beginTick = 0;

        public int endTick;

        public int stopTick = 0;

        public boolean isLooped = false;

        public int returnTick;

        final AnimationFormat emoteEmoteFormat;

        private final float validationThreshold;

        public String name = null;

        @Nullable
        public String description = null;

        @Nullable
        public String author = null;

        @Nullable
        public NBS song = null;

        @Nullable
        public ByteBuffer iconData;

        public HashMap<String, Object> extraData = new HashMap();

        public AnimationBuilder(AnimationFormat source) {
            this(staticThreshold, source);
        }

        public AnimationBuilder(float validationThreshold, AnimationFormat emoteFormat) {
            this.validationThreshold = validationThreshold;
            this.head = new KeyframeAnimation.StateCollection(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, validationThreshold, false);
            this.body = new KeyframeAnimation.StateCollection(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, validationThreshold / 8.0F, true);
            this.rightArm = new KeyframeAnimation.StateCollection(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, validationThreshold, true);
            this.leftArm = new KeyframeAnimation.StateCollection(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, validationThreshold, true);
            this.leftLeg = new KeyframeAnimation.StateCollection(1.9F, 12.0F, 0.1F, 0.0F, 0.0F, 0.0F, validationThreshold, true);
            this.rightLeg = new KeyframeAnimation.StateCollection(-1.9F, 12.0F, 0.1F, 0.0F, 0.0F, 0.0F, validationThreshold, true);
            this.leftItem = new KeyframeAnimation.StateCollection(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, validationThreshold, false);
            this.rightItem = new KeyframeAnimation.StateCollection(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, validationThreshold, false);
            this.torso = new KeyframeAnimation.StateCollection(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, validationThreshold, true);
            this.bodyParts.put("head", this.head);
            this.bodyParts.put("body", this.body);
            this.bodyParts.put("rightArm", this.rightArm);
            this.bodyParts.put("rightLeg", this.rightLeg);
            this.bodyParts.put("leftArm", this.leftArm);
            this.bodyParts.put("leftLeg", this.leftLeg);
            this.bodyParts.put("leftItem", this.leftItem);
            this.bodyParts.put("rightItem", this.rightItem);
            this.bodyParts.put("torso", this.torso);
            this.emoteEmoteFormat = emoteFormat;
        }

        private AnimationBuilder(int beginTick, int endTick, int stopTick, boolean isInfinite, int returnToTick, HashMap<String, KeyframeAnimation.StateCollection> bodyParts, boolean isEasingBefore, boolean nsfw, @Nullable UUID uuid, AnimationFormat emoteFormat, HashMap<String, Object> extraData) {
            this.validationThreshold = staticThreshold;
            this.bodyParts.putAll(bodyParts);
            this.head = (KeyframeAnimation.StateCollection) bodyParts.get("head");
            this.body = (KeyframeAnimation.StateCollection) bodyParts.get("body");
            this.rightArm = (KeyframeAnimation.StateCollection) bodyParts.get("rightArm");
            this.rightLeg = (KeyframeAnimation.StateCollection) bodyParts.get("rightLeg");
            this.leftArm = (KeyframeAnimation.StateCollection) bodyParts.get("leftArm");
            this.leftLeg = (KeyframeAnimation.StateCollection) bodyParts.get("leftLeg");
            this.leftItem = (KeyframeAnimation.StateCollection) bodyParts.get("leftItem");
            this.rightItem = (KeyframeAnimation.StateCollection) bodyParts.get("rightItem");
            this.torso = (KeyframeAnimation.StateCollection) bodyParts.get("torso");
            this.beginTick = beginTick;
            this.endTick = endTick;
            this.stopTick = stopTick;
            this.isLooped = isInfinite;
            this.returnTick = returnToTick;
            this.isEasingBefore = isEasingBefore;
            this.nsfw = nsfw;
            this.uuid = uuid;
            this.extraData.putAll(extraData);
            this.name = extraData.containsKey("name") && extraData.get("name") instanceof String ? (String) extraData.get("name") : null;
            this.description = extraData.containsKey("description") && extraData.get("description") instanceof String ? (String) extraData.get("description") : null;
            this.author = extraData.containsKey("author") && extraData.get("author") instanceof String ? (String) extraData.get("author") : null;
            this.emoteEmoteFormat = emoteFormat;
            this.iconData = extraData.containsKey("iconData") && extraData.get("iconData") instanceof ByteBuffer ? (ByteBuffer) extraData.get("iconData") : null;
            this.song = extraData.containsKey("song") && extraData.get("song") instanceof NBS ? (NBS) extraData.get("song") : null;
        }

        public KeyframeAnimation.AnimationBuilder setDescription(String s) {
            this.description = s;
            return this;
        }

        public KeyframeAnimation.AnimationBuilder setName(String s) {
            this.name = s;
            return this;
        }

        public KeyframeAnimation.AnimationBuilder setAuthor(String s) {
            this.author = s;
            return this;
        }

        public KeyframeAnimation.StateCollection getOrCreateNewPart(String name, float x, float y, float z, float pitch, float yaw, float roll, boolean bendable) {
            if (!this.bodyParts.containsKey(name)) {
                this.bodyParts.put(name, new KeyframeAnimation.StateCollection(x, y, z, pitch, yaw, roll, this.validationThreshold, bendable));
            }
            return (KeyframeAnimation.StateCollection) this.bodyParts.get(name);
        }

        @Nullable
        public KeyframeAnimation.StateCollection getPart(String name) {
            return (KeyframeAnimation.StateCollection) this.bodyParts.get(name);
        }

        public KeyframeAnimation.StateCollection getOrCreatePart(String name) {
            if (!this.bodyParts.containsKey(name)) {
                this.bodyParts.put(name, new KeyframeAnimation.StateCollection(this.validationThreshold));
            }
            return (KeyframeAnimation.StateCollection) this.bodyParts.get(name);
        }

        public KeyframeAnimation.AnimationBuilder fullyEnableParts() {
            for (Entry<String, KeyframeAnimation.StateCollection> part : this.bodyParts.entrySet()) {
                ((KeyframeAnimation.StateCollection) part.getValue()).fullyEnablePart(false);
            }
            return this;
        }

        public KeyframeAnimation.AnimationBuilder optimizeEmote() {
            for (Entry<String, KeyframeAnimation.StateCollection> part : this.bodyParts.entrySet()) {
                ((KeyframeAnimation.StateCollection) part.getValue()).optimize(this.isLooped, this.returnTick);
            }
            return this;
        }

        public KeyframeAnimation build() throws IllegalArgumentException {
            if (this.name != null) {
                this.extraData.put("name", this.name);
            }
            if (this.description != null) {
                this.extraData.put("description", this.description);
            }
            if (this.author != null) {
                this.extraData.put("author", this.author);
            }
            if (this.iconData != null) {
                this.extraData.put("iconData", this.iconData);
            }
            if (this.song != null) {
                this.extraData.put("song", this.song);
            }
            return new KeyframeAnimation(this.beginTick, this.endTick, this.stopTick, this.isLooped, this.returnTick, this.bodyParts, this.isEasingBefore, this.nsfw, this.uuid, this.emoteEmoteFormat, this.extraData);
        }

        public KeyframeAnimation.AnimationBuilder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public String toString() {
            return "AnimationBuilder{uuid=" + this.uuid + ", extra=" + this.extraData + '}';
        }
    }

    @Immutable
    public static final class KeyFrame {

        public final int tick;

        public final float value;

        public final Ease ease;

        public KeyFrame(int tick, float value, Ease ease) {
            this.tick = tick;
            this.value = value;
            this.ease = ease;
        }

        public boolean equals(Object other) {
            return !(other instanceof KeyframeAnimation.KeyFrame) ? super.equals(other) : ((KeyframeAnimation.KeyFrame) other).ease == this.ease && ((KeyframeAnimation.KeyFrame) other).tick == this.tick && ((KeyframeAnimation.KeyFrame) other).value == this.value;
        }

        public KeyFrame(int tick, float value) {
            this(tick, value, Ease.INOUTSINE);
        }

        public int hashCode() {
            int result = this.tick;
            result = 31 * result + Float.hashCode(this.value);
            return 31 * result + this.ease.getId();
        }

        public String toString() {
            return "KeyFrame{tick=" + this.tick + ", value=" + this.value + ", ease=" + this.ease + '}';
        }
    }

    public static final class StateCollection {

        public final KeyframeAnimation.StateCollection.State x;

        public final KeyframeAnimation.StateCollection.State y;

        public final KeyframeAnimation.StateCollection.State z;

        public final KeyframeAnimation.StateCollection.State pitch;

        public final KeyframeAnimation.StateCollection.State yaw;

        public final KeyframeAnimation.StateCollection.State roll;

        @Nullable
        public final KeyframeAnimation.StateCollection.State bend;

        @Nullable
        public final KeyframeAnimation.StateCollection.State bendDirection;

        public final boolean isBendable;

        public StateCollection(float x, float y, float z, float pitch, float yaw, float roll, float translationThreshold, boolean bendable) {
            this.x = new KeyframeAnimation.StateCollection.State("x", x, translationThreshold, false);
            this.y = new KeyframeAnimation.StateCollection.State("y", y, translationThreshold, false);
            this.z = new KeyframeAnimation.StateCollection.State("z", z, translationThreshold, false);
            this.pitch = new KeyframeAnimation.StateCollection.State("pitch", pitch, 0.0F, true);
            this.yaw = new KeyframeAnimation.StateCollection.State("yaw", yaw, 0.0F, true);
            this.roll = new KeyframeAnimation.StateCollection.State("roll", roll, 0.0F, true);
            if (bendable) {
                this.bendDirection = new KeyframeAnimation.StateCollection.State("axis", 0.0F, 0.0F, true);
                this.bend = new KeyframeAnimation.StateCollection.State("bend", 0.0F, 0.0F, true);
            } else {
                this.bend = null;
                this.bendDirection = null;
            }
            this.isBendable = bendable;
        }

        public StateCollection(KeyframeAnimation.StateCollection stateCollection) {
            this.x = stateCollection.x.copy();
            this.y = stateCollection.y.copy();
            this.z = stateCollection.z.copy();
            this.pitch = stateCollection.pitch.copy();
            this.yaw = stateCollection.yaw.copy();
            this.roll = stateCollection.roll.copy();
            this.isBendable = stateCollection.isBendable;
            if (stateCollection.isBendable) {
                this.bendDirection = stateCollection.bendDirection.copy();
                this.bend = stateCollection.bend.copy();
            } else {
                this.bend = null;
                this.bendDirection = null;
            }
        }

        public StateCollection(float threshold) {
            this(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, threshold, true);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof KeyframeAnimation.StateCollection)) {
                return false;
            } else {
                KeyframeAnimation.StateCollection that = (KeyframeAnimation.StateCollection) o;
                if (this.isBendable != that.isBendable) {
                    return false;
                } else if (!this.x.equals(that.x)) {
                    return false;
                } else if (!this.y.equals(that.y)) {
                    return false;
                } else if (!this.z.equals(that.z)) {
                    return false;
                } else if (!this.pitch.equals(that.pitch)) {
                    return false;
                } else if (!this.yaw.equals(that.yaw)) {
                    return false;
                } else if (!this.roll.equals(that.roll)) {
                    return false;
                } else {
                    return !Objects.equals(this.bend, that.bend) ? false : Objects.equals(this.bendDirection, that.bendDirection);
                }
            }
        }

        public int hashCode() {
            int result = 0;
            result = 31 * result + this.x.hashCode();
            result = 31 * result + this.y.hashCode();
            result = 31 * result + this.z.hashCode();
            result = 31 * result + this.pitch.hashCode();
            result = 31 * result + this.yaw.hashCode();
            result = 31 * result + this.roll.hashCode();
            result = 31 * result + (this.bend != null ? this.bend.hashCode() : 0);
            result = 31 * result + (this.bendDirection != null ? this.bendDirection.hashCode() : 0);
            return 31 * result + (this.isBendable ? 1 : 0);
        }

        public void fullyEnablePart(boolean always) {
            if (always || this.x.isEnabled || this.y.isEnabled || this.z.isEnabled || this.pitch.isEnabled || this.yaw.isEnabled || this.roll.isEnabled || this.isBendable && (this.bend.isEnabled || this.bendDirection.isEnabled)) {
                this.setEnabled(true);
            }
        }

        public void setEnabled(boolean enabled) {
            this.x.setEnabled(enabled);
            this.y.setEnabled(enabled);
            this.z.setEnabled(enabled);
            this.pitch.setEnabled(enabled);
            this.yaw.setEnabled(enabled);
            this.roll.setEnabled(enabled);
            if (this.isBendable) {
                this.bend.setEnabled(enabled);
                this.bendDirection.setEnabled(enabled);
            }
        }

        public boolean isEnabled() {
            return this.x.isEnabled() || this.y.isEnabled() || this.z.isEnabled() || this.pitch.isEnabled() || this.yaw.isEnabled() || this.roll.isEnabled() || this.bend != null && this.bend.isEnabled() || this.bendDirection != null && this.bend.isEnabled();
        }

        public void verifyAndLock(int maxLength) {
            this.x.lockAndVerify(maxLength);
            this.y.lockAndVerify(maxLength);
            this.z.lockAndVerify(maxLength);
            this.pitch.lockAndVerify(maxLength);
            this.yaw.lockAndVerify(maxLength);
            this.roll.lockAndVerify(maxLength);
            if (this.bend != null) {
                this.bend.lockAndVerify(maxLength);
            }
            if (this.bendDirection != null) {
                this.bendDirection.lockAndVerify(maxLength);
            }
        }

        private void optimize(boolean isLooped, int ret) {
            this.x.optimize(isLooped, ret);
            this.y.optimize(isLooped, ret);
            this.z.optimize(isLooped, ret);
            this.pitch.optimize(isLooped, ret);
            this.yaw.optimize(isLooped, ret);
            this.roll.optimize(isLooped, ret);
            if (this.isBendable) {
                this.bend.optimize(isLooped, ret);
                this.bendDirection.optimize(isLooped, ret);
            }
        }

        public KeyframeAnimation.StateCollection copy() {
            return new KeyframeAnimation.StateCollection(this);
        }

        public boolean isBendable() {
            return this.isBendable;
        }

        public static final class State {

            private boolean isModifiable = true;

            public final float defaultValue;

            public final float threshold;

            private List<KeyframeAnimation.KeyFrame> keyFrames = new ArrayList();

            public final String name;

            private final boolean isAngle;

            private boolean isEnabled = false;

            public State(KeyframeAnimation.StateCollection.State state) {
                this.defaultValue = state.defaultValue;
                this.threshold = state.threshold;
                this.keyFrames.addAll(state.keyFrames);
                this.name = state.name;
                this.isAngle = state.isAngle;
                this.setEnabled(state.isEnabled);
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (!(o instanceof KeyframeAnimation.StateCollection.State)) {
                    return false;
                } else {
                    KeyframeAnimation.StateCollection.State state = (KeyframeAnimation.StateCollection.State) o;
                    if (Float.compare(state.defaultValue, this.defaultValue) != 0) {
                        return false;
                    } else if (this.isAngle != state.isAngle) {
                        return false;
                    } else if (!this.keyFrames.equals(state.keyFrames)) {
                        return false;
                    } else {
                        return this.isEnabled != state.isEnabled ? false : Objects.equals(this.name, state.name);
                    }
                }
            }

            private void lock() {
                this.isModifiable = false;
                this.keyFrames = Collections.unmodifiableList(this.keyFrames);
            }

            public void lockAndVerify(int maxLength) {
                for (KeyframeAnimation.KeyFrame keyFrame : this.getKeyFrames()) {
                    if (keyFrame == null || keyFrame.tick < 0 || keyFrame.ease == null || !Float.isFinite(keyFrame.value)) {
                        throw new IllegalArgumentException("Animation is invalid: " + keyFrame);
                    }
                }
                this.lock();
            }

            public void setEnabled(boolean newValue) {
                if (this.isModifiable) {
                    this.isEnabled = newValue;
                } else {
                    throw new AssertionError("Can not modify locked things");
                }
            }

            public int hashCode() {
                int result = this.defaultValue != 0.0F ? Float.floatToIntBits(this.defaultValue) : 0;
                result = 31 * result + this.keyFrames.hashCode();
                result = 31 * result + (this.isAngle ? 1 : 0);
                return 31 * result + (this.isEnabled ? 1 : 0);
            }

            private State(String name, float defaultValue, float threshold, boolean isAngle) {
                this.defaultValue = defaultValue;
                this.threshold = threshold;
                this.name = name;
                this.isAngle = isAngle;
            }

            public int length() {
                return this.keyFrames.size();
            }

            public int findAtTick(int tick) {
                int i = -1;
                while (this.keyFrames.size() > i + 1 && ((KeyframeAnimation.KeyFrame) this.keyFrames.get(i + 1)).tick <= tick) {
                    i++;
                }
                return i;
            }

            public boolean addKeyFrame(int tick, float value, Ease ease, int rotate, boolean degrees) {
                if (degrees && this.isAngle) {
                    value *= (float) (Math.PI / 180.0);
                }
                boolean bl = this.addKeyFrame(new KeyframeAnimation.KeyFrame(tick, value, ease));
                if (this.isAngle && rotate != 0) {
                    bl = this.addKeyFrame(new KeyframeAnimation.KeyFrame(tick, (float) ((double) value + (Math.PI * 2) * (double) rotate), ease)) && bl;
                }
                return bl;
            }

            public boolean addKeyFrame(int tick, float value, Ease ease) {
                if (Float.isNaN(value)) {
                    throw new IllegalArgumentException("value can't be NaN");
                } else {
                    return this.addKeyFrame(new KeyframeAnimation.KeyFrame(tick, value, ease));
                }
            }

            private boolean addKeyFrame(KeyframeAnimation.KeyFrame keyFrame) {
                this.setEnabled(true);
                int i = this.findAtTick(keyFrame.tick) + 1;
                this.keyFrames.add(i, keyFrame);
                return this.isAngle || !(Math.abs(this.defaultValue - keyFrame.value) > this.threshold);
            }

            public void replace(KeyframeAnimation.KeyFrame keyFrame, int pos) {
                this.keyFrames.remove(pos);
                this.keyFrames.add(pos, keyFrame);
            }

            public void replaceEase(int pos, Ease ease) {
                KeyframeAnimation.KeyFrame original = (KeyframeAnimation.KeyFrame) this.keyFrames.get(pos);
                this.replace(new KeyframeAnimation.KeyFrame(original.tick, original.value, ease), pos);
            }

            private void optimize(boolean isLooped, int returnToTick) {
                for (int i = 1; i < this.keyFrames.size() - 1; i++) {
                    if (((KeyframeAnimation.KeyFrame) this.keyFrames.get(i - 1)).value == ((KeyframeAnimation.KeyFrame) this.keyFrames.get(i)).value && this.keyFrames.size() > i + 1 && ((KeyframeAnimation.KeyFrame) this.keyFrames.get(i)).value == ((KeyframeAnimation.KeyFrame) this.keyFrames.get(i + 1)).value && (!isLooped || ((KeyframeAnimation.KeyFrame) this.keyFrames.get(i - 1)).tick >= returnToTick || ((KeyframeAnimation.KeyFrame) this.keyFrames.get(i)).tick < returnToTick)) {
                        this.keyFrames.remove(i--);
                    }
                }
            }

            public KeyframeAnimation.StateCollection.State copy() {
                return new KeyframeAnimation.StateCollection.State(this);
            }

            public List<KeyframeAnimation.KeyFrame> getKeyFrames() {
                return this.keyFrames;
            }

            public boolean isEnabled() {
                return this.isEnabled;
            }
        }
    }
}
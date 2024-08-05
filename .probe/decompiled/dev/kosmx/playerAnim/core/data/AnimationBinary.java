package dev.kosmx.playerAnim.core.data;

import dev.kosmx.playerAnim.core.util.Ease;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;

public final class AnimationBinary {

    private static final byte keyframeSize = 9;

    public static <T extends ByteBuffer> T write(KeyframeAnimation animation, T buf, int version) throws BufferOverflowException {
        buf.putInt(animation.beginTick);
        buf.putInt(animation.endTick);
        buf.putInt(animation.stopTick);
        putBoolean(buf, animation.isInfinite());
        buf.putInt(animation.returnToTick);
        putBoolean(buf, animation.isEasingBefore);
        putBoolean(buf, animation.nsfw);
        buf.put((byte) 9);
        if (version >= 2) {
            buf.putInt(animation.getBodyParts().size());
            for (Entry<String, KeyframeAnimation.StateCollection> part : animation.getBodyParts().entrySet()) {
                putString(buf, (String) part.getKey());
                writePart(buf, (KeyframeAnimation.StateCollection) part.getValue(), version);
            }
        } else {
            writePart(buf, animation.getPart("head"), version);
            writePart(buf, animation.getPart("body"), version);
            writePart(buf, animation.getPart("rightArm"), version);
            writePart(buf, animation.getPart("leftArm"), version);
            writePart(buf, animation.getPart("rightLeg"), version);
            writePart(buf, animation.getPart("leftLeg"), version);
        }
        buf.putLong(animation.getUuid().getMostSignificantBits());
        buf.putLong(animation.getUuid().getLeastSignificantBits());
        return buf;
    }

    public static <T extends ByteBuffer> T write(KeyframeAnimation animation, T buf) throws BufferOverflowException {
        return write(animation, buf, getCurrentVersion());
    }

    private static void writePart(ByteBuffer buf, KeyframeAnimation.StateCollection part, int version) {
        writeKeyframes(buf, part.x, version);
        writeKeyframes(buf, part.y, version);
        writeKeyframes(buf, part.z, version);
        writeKeyframes(buf, part.pitch, version);
        writeKeyframes(buf, part.yaw, version);
        writeKeyframes(buf, part.roll, version);
        if (part.isBendable) {
            writeKeyframes(buf, part.bendDirection, version);
            writeKeyframes(buf, part.bend, version);
        }
    }

    private static void writeKeyframes(ByteBuffer buf, KeyframeAnimation.StateCollection.State part, int version) {
        List<KeyframeAnimation.KeyFrame> list = part.getKeyFrames();
        if (version >= 2) {
            putBoolean(buf, part.isEnabled());
            buf.putInt(list.size());
        } else {
            buf.putInt(part.isEnabled() ? list.size() : -1);
        }
        if (part.isEnabled() || version >= 2) {
            for (KeyframeAnimation.KeyFrame move : list) {
                buf.putInt(move.tick);
                buf.putFloat(move.value);
                buf.put(move.ease.getId());
            }
        }
    }

    public static KeyframeAnimation read(ByteBuffer buf, int version) throws IOException {
        KeyframeAnimation.AnimationBuilder animation = new KeyframeAnimation.AnimationBuilder(AnimationFormat.BINARY);
        animation.beginTick = buf.getInt();
        animation.endTick = buf.getInt();
        animation.stopTick = buf.getInt();
        animation.isLooped = getBoolean(buf);
        animation.returnTick = buf.getInt();
        animation.isEasingBefore = getBoolean(buf);
        animation.nsfw = getBoolean(buf);
        int keyframeSize = buf.get();
        if (keyframeSize <= 0) {
            throw new IOException("keyframe size must be greater than 0, current: " + keyframeSize);
        } else {
            boolean valid = true;
            if (version >= 2) {
                int count = buf.getInt();
                for (int i = 0; i < count; i++) {
                    String name = getString(buf);
                    KeyframeAnimation.StateCollection part = animation.getOrCreatePart(name);
                    valid = readPart(buf, part, version, keyframeSize) && valid;
                }
            } else {
                valid = readPart(buf, animation.head, version, keyframeSize);
                valid = readPart(buf, animation.body, version, keyframeSize) && valid;
                valid = readPart(buf, animation.rightArm, version, keyframeSize) && valid;
                valid = readPart(buf, animation.leftArm, version, keyframeSize) && valid;
                valid = readPart(buf, animation.rightLeg, version, keyframeSize) && valid;
                valid = readPart(buf, animation.leftLeg, version, keyframeSize) && valid;
            }
            long msb = buf.getLong();
            long lsb = buf.getLong();
            animation.uuid = new UUID(msb, lsb);
            animation.extraData.put("valid", valid);
            return animation.build();
        }
    }

    private static boolean readPart(ByteBuffer buf, KeyframeAnimation.StateCollection part, int version, int keyframeSize) {
        boolean bl = readKeyframes(buf, part.x, version, keyframeSize);
        bl = readKeyframes(buf, part.y, version, keyframeSize) && bl;
        bl = readKeyframes(buf, part.z, version, keyframeSize) && bl;
        bl = readKeyframes(buf, part.pitch, version, keyframeSize) && bl;
        bl = readKeyframes(buf, part.yaw, version, keyframeSize) && bl;
        bl = readKeyframes(buf, part.roll, version, keyframeSize) && bl;
        if (part.isBendable()) {
            bl = readKeyframes(buf, part.bendDirection, version, keyframeSize) && bl;
            bl = readKeyframes(buf, part.bend, version, keyframeSize) && bl;
        }
        return bl;
    }

    private static boolean readKeyframes(ByteBuffer buf, KeyframeAnimation.StateCollection.State part, int version, int keyframeSize) {
        boolean valid = true;
        int length;
        boolean enabled;
        if (version >= 2) {
            enabled = getBoolean(buf);
            length = buf.getInt();
        } else {
            length = buf.getInt();
            enabled = length >= 0;
        }
        for (int i = 0; i < length; i++) {
            int currentPos = buf.position();
            if (!part.addKeyFrame(buf.getInt(), buf.getFloat(), Ease.getEase(buf.get()))) {
                valid = false;
            }
            buf.position(currentPos + keyframeSize);
        }
        part.setEnabled(enabled);
        return valid;
    }

    public static int getCurrentVersion() {
        return 2;
    }

    public static int calculateSize(KeyframeAnimation animation, int version) {
        int size = 36;
        if (version < 2) {
            size += partSize(animation.getPart("head"), version);
            size += partSize(animation.getPart("body"), version);
            size += partSize(animation.getPart("rightArm"), version);
            size += partSize(animation.getPart("leftArm"), version);
            size += partSize(animation.getPart("rightLeg"), version);
            size += partSize(animation.getPart("leftLeg"), version);
        } else {
            size += 4;
            for (Entry<String, KeyframeAnimation.StateCollection> entry : animation.getBodyParts().entrySet()) {
                size += stringSize((String) entry.getKey()) + partSize((KeyframeAnimation.StateCollection) entry.getValue(), version);
            }
        }
        return size;
    }

    private static int stringSize(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return bytes.length + 4;
    }

    private static int partSize(@Nullable KeyframeAnimation.StateCollection part, int version) {
        int size = 0;
        size += axisSize(part.x, version);
        size += axisSize(part.y, version);
        size += axisSize(part.z, version);
        size += axisSize(part.pitch, version);
        size += axisSize(part.yaw, version);
        size += axisSize(part.roll, version);
        if (part.isBendable) {
            size += axisSize(part.bend, version);
            size += axisSize(part.bendDirection, version);
        }
        return size;
    }

    private static int axisSize(KeyframeAnimation.StateCollection.State axis, int version) {
        return axis.getKeyFrames().size() * 9 + (version >= 2 ? 5 : 4);
    }

    public static void putBoolean(ByteBuffer byteBuffer, boolean bl) {
        byteBuffer.put((byte) (bl ? 1 : 0));
    }

    public static boolean getBoolean(ByteBuffer buf) {
        return buf.get() != 0;
    }

    public static void putString(ByteBuffer buf, String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        buf.putInt(bytes.length);
        buf.put(bytes);
    }

    public static String getString(ByteBuffer buf) {
        int len = buf.getInt();
        byte[] bytes = new byte[len];
        buf.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
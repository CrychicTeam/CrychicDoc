package snownee.kiwi.customization.placement;

import com.mojang.serialization.Codec;
import java.util.Locale;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.kiwi.customization.block.KBlockUtils;

public record ParsedProtoTag(String prefix, String key, String value) {

    public static final Codec<ParsedProtoTag> CODEC = ExtraCodecs.NON_EMPTY_STRING.xmap(ParsedProtoTag::of, ParsedProtoTag::toString);

    public static ParsedProtoTag of(String s) {
        String prefix;
        if (!s.startsWith("*") && !s.startsWith("@")) {
            prefix = "";
        } else {
            prefix = s.substring(0, 1);
            s = s.substring(1);
        }
        int i = s.indexOf(58);
        String key = i == -1 ? s : s.substring(0, i);
        String value = i == -1 ? "" : s.substring(i + 1);
        return new ParsedProtoTag(prefix, key, value);
    }

    public String prefixedKey() {
        return this.prefix + this.key;
    }

    public ParsedProtoTag resolve(BlockState blockState) {
        return this.resolve(blockState, Rotation.NONE);
    }

    public ParsedProtoTag resolve(BlockState blockState, Rotation rotation) {
        if (this.isResolved()) {
            return this;
        } else {
            String newValue;
            if (this.value.isEmpty()) {
                newValue = KBlockUtils.getValueString(blockState, this.key);
            } else {
                Direction direction = Direction.valueOf(this.value.toUpperCase(Locale.ENGLISH));
                newValue = rotation.rotate(direction).getSerializedName();
            }
            return new ParsedProtoTag("", this.key, newValue);
        }
    }

    public boolean isResolved() {
        return !this.prefix.equals("@");
    }

    public String toString() {
        return this.prefix + this.key + (this.value.isEmpty() ? "" : ":" + this.value);
    }
}
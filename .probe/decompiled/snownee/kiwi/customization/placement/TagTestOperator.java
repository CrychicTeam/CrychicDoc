package snownee.kiwi.customization.placement;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record TagTestOperator(String name, BiPredicate<String, String> test) {

    private static final Map<String, String> OPPOSITE_LOOKUP = (Map<String, String>) Direction.stream().collect(Collectors.toUnmodifiableMap(Direction::m_7912_, $ -> $.getOpposite().getSerializedName()));

    public static final BiMap<String, TagTestOperator> VALUES = HashBiMap.create(4);

    public static final TagTestOperator EQUAL = new TagTestOperator("==", Objects::equals);

    public static final Codec<TagTestOperator> CODEC = CustomizationCodecs.simpleByNameCodec(VALUES);

    public static void register(TagTestOperator operator) {
        VALUES.put(operator.name, operator);
    }

    static {
        register(EQUAL);
        register(new TagTestOperator("!=", (a, b) -> !Objects.equals(a, b)));
        register(new TagTestOperator("true", (a, b) -> true));
        register(new TagTestOperator("is_opposite", (a, b) -> Objects.equals(OPPOSITE_LOOKUP.get(a), b)));
    }
}
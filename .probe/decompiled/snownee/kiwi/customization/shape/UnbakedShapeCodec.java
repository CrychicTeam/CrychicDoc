package snownee.kiwi.customization.shape;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DataResult.PartialResult;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

public class UnbakedShapeCodec implements Codec<UnbakedShape> {

    private final Pattern simpleShapePattern = Pattern.compile("\\((-?\\d+\\.?\\d*)d?,(-?\\d+\\.?\\d*)d?,(-?\\d+\\.?\\d*)d?,(-?\\d+\\.?\\d*)d?,(-?\\d+\\.?\\d*)d?,(-?\\d+\\.?\\d*)d?\\)");

    private final Map<String, BooleanOp> booleanOpMap = Map.ofEntries(Map.entry("and", BooleanOp.AND), Map.entry("or", BooleanOp.OR), Map.entry("not_or", BooleanOp.NOT_OR), Map.entry("not_and", BooleanOp.NOT_AND), Map.entry("only_first", BooleanOp.ONLY_FIRST), Map.entry("only_second", BooleanOp.ONLY_SECOND), Map.entry("not_same", BooleanOp.NOT_SAME), Map.entry("same", BooleanOp.SAME), Map.entry("causes", BooleanOp.CAUSES), Map.entry("caused_by", BooleanOp.CAUSED_BY));

    private final Map<String, Codec<? extends UnbakedShape>> typedCodecMap;

    private final Map<ResourceLocation, ShapeRef> refInterner = Maps.newHashMap();

    public UnbakedShapeCodec() {
        this.typedCodecMap = Map.ofEntries(Map.entry("directional", DirectionalShape.Unbaked.codec(this)), Map.entry("horizontal", HorizontalShape.Unbaked.codec(this)), Map.entry("choices", ChoicesShape.Unbaked.codec(this)), Map.entry("moulding", MouldingShape.Unbaked.codec(this)), Map.entry("six_way", SixWayShape.Unbaked.codec(this)), Map.entry("front_and_top", FrontAndTopShape.Unbaked.codec(this)), Map.entry("configure_wall", ConfigureWallShape.codec()), Map.entry("configure_cross_collision", ConfigureCrossCollisionShape.codec()));
    }

    public <T> DataResult<Pair<UnbakedShape, T>> decode(DynamicOps<T> ops, T input) {
        try {
            return DataResult.success(Pair.of(this.directDecode(ops, input), input));
        } catch (IllegalArgumentException var4) {
            return DataResult.error(var4::getMessage);
        }
    }

    public <T> DataResult<T> encode(UnbakedShape input, DynamicOps<T> ops, T prefix) {
        throw new NotImplementedException();
    }

    public <T> UnbakedShape directDecode(DynamicOps<T> ops, T input) {
        Optional<String> stringValue = ops.getStringValue(input).result();
        if (stringValue.isPresent()) {
            return this.decodeVoxelShape((String) stringValue.get());
        } else {
            Optional<MapLike<T>> mapValue = ops.getMap(input).result();
            if (mapValue.isPresent()) {
                DataResult<String> typeResult = ops.getStringValue(((MapLike) mapValue.get()).get("type"));
                if (typeResult.error().isPresent()) {
                    throw new IllegalArgumentException("No \"type\" in shape object " + input);
                } else {
                    String type = (String) typeResult.result().orElseThrow();
                    Codec<? extends UnbakedShape> codec = (Codec<? extends UnbakedShape>) this.typedCodecMap.get(type);
                    if (codec == null) {
                        throw new IllegalArgumentException("Unknown shape type " + type);
                    } else {
                        DataResult<? extends Pair<? extends UnbakedShape, T>> result = codec.decode(ops, input);
                        if (result.error().isPresent()) {
                            throw new IllegalArgumentException(((PartialResult) result.error().get()).message());
                        } else {
                            return (UnbakedShape) result.result().map(Pair::getFirst).orElseThrow();
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Shape must be a string or an object " + input);
            }
        }
    }

    private UnbakedShape decodeVoxelShape(String s) {
        s = StringUtils.deleteWhitespace(s).toLowerCase(Locale.ENGLISH);
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Empty shape string");
        } else {
            return (UnbakedShape) (ResourceLocation.isValidResourceLocation(s) ? (UnbakedShape) this.refInterner.computeIfAbsent(new ResourceLocation(s), ShapeRef::new) : new UnbakedShape.Inlined(this.recursiveDecodeVoxelShape(s)));
        }
    }

    private VoxelShape recursiveDecodeVoxelShape(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Empty shape string in parameters");
        } else {
            if (s.endsWith(")")) {
                int leftBracket = s.indexOf(40);
                BooleanOp booleanOp = leftBracket > 1 ? (BooleanOp) this.booleanOpMap.get(s.substring(0, leftBracket)) : null;
                if (booleanOp != null) {
                    String s1 = s.substring(leftBracket + 1, s.length() - 1);
                    int expectedRightBrackets = 0;
                    int comma = -1;
                    for (int j = 0; j < s1.length(); j++) {
                        char c = s1.charAt(j);
                        if (c == '(') {
                            expectedRightBrackets++;
                        } else if (c == ')') {
                            expectedRightBrackets--;
                        } else if (c == ',' && expectedRightBrackets == 0) {
                            if (comma >= 0) {
                                throw new IllegalArgumentException("Multiple commas in a shape boolean function");
                            }
                            comma = j;
                        }
                    }
                    if (expectedRightBrackets != 0) {
                        throw new IllegalArgumentException("Unmatched brackets in a shape boolean function");
                    }
                    if (comma == -1) {
                        throw new IllegalArgumentException("No comma in a shape boolean function");
                    }
                    VoxelShape left = this.recursiveDecodeVoxelShape(s1.substring(0, comma));
                    VoxelShape right = this.recursiveDecodeVoxelShape(s1.substring(comma + 1));
                    return Shapes.join(left, right, booleanOp);
                }
            }
            Matcher matcher = this.simpleShapePattern.matcher(s);
            if (!matcher.find()) {
                throw new IllegalArgumentException("Invalid shape string: " + s);
            } else {
                VoxelShape shape = Shapes.empty();
                do {
                    double x1 = Double.parseDouble(matcher.group(1));
                    double y1 = Double.parseDouble(matcher.group(2));
                    double z1 = Double.parseDouble(matcher.group(3));
                    double x2 = Double.parseDouble(matcher.group(4));
                    double y2 = Double.parseDouble(matcher.group(5));
                    double z2 = Double.parseDouble(matcher.group(6));
                    shape = Shapes.joinUnoptimized(shape, Block.box(x1, y1, z1, x2, y2, z2), BooleanOp.OR);
                } while (matcher.find());
                return shape.optimize();
            }
        }
    }

    public static String encodeVoxelShape(VoxelShape shape) {
        NumberFormat format = new DecimalFormat("0.###");
        StringBuilder builder = new StringBuilder();
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
            if (!builder.isEmpty()) {
                builder.append('\n');
            }
            builder.append('(').append(format.format(x1 * 16.0)).append(", ").append(format.format(y1 * 16.0)).append(", ").append(format.format(z1 * 16.0)).append(", ").append(format.format(x2 * 16.0)).append(", ").append(format.format(y2 * 16.0)).append(", ").append(format.format(z2 * 16.0)).append(')');
        });
        return builder.toString();
    }
}
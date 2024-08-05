package snownee.kiwi.customization.placement;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.customization.block.loader.KBlockDefinition;
import snownee.kiwi.customization.block.loader.KBlockTemplate;
import snownee.kiwi.customization.duck.KBlockProperties;
import snownee.kiwi.customization.item.MultipleBlockItem;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.util.BlockPredicateHelper;
import snownee.kiwi.util.KHolder;
import snownee.kiwi.util.Util;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record PlaceChoices(List<PlaceTarget> target, Optional<String> transformWith, List<PlaceChoices.Flow> flow, List<PlaceChoices.Alter> alter, List<PlaceChoices.Limit> limit, List<PlaceChoices.Interests> interests, boolean skippable) {

    public static final BiMap<String, PlaceChoices.BlockFaceType> BLOCK_FACE_TYPES = HashBiMap.create();

    public static final Codec<PlaceChoices> CODEC;

    public static void setTo(Block block, @Nullable KHolder<PlaceChoices> holder) {
        KBlockSettings settings = KBlockSettings.of(block);
        if (settings == null && holder != null) {
            ((KBlockProperties) block.f_60439_).kiwi$setSettings(settings = KBlockSettings.empty());
        }
        if (settings != null) {
            settings.placeChoices = holder == null ? null : holder.value();
        }
    }

    public int test(BlockState baseState, BlockState targetState) {
        for (PlaceChoices.Limit limit : this.limit) {
            if (!limit.test(baseState, targetState)) {
                return Integer.MIN_VALUE;
            }
        }
        int interest = 0;
        for (PlaceChoices.Interests provider : this.interests) {
            if (provider.when().smartTest(baseState, targetState)) {
                interest += provider.bonus;
            }
        }
        return interest;
    }

    public BlockState getStateForPlacement(Level level, BlockPos pos, BlockState original) {
        if (this.flow.isEmpty()) {
            return original;
        } else {
            MutableObject<Rotation> rotation = new MutableObject(Rotation.NONE);
            String transformWith = (String) this.transformWith.orElse("none");
            if (!transformWith.equals("none")) {
                if (!(KBlockUtils.getProperty(original, transformWith) instanceof DirectionProperty directionProperty)) {
                    throw new IllegalArgumentException("Invalid transform_with property: " + transformWith);
                }
                Direction direction = (Direction) original.m_61143_(directionProperty);
                for (Rotation r : Rotation.values()) {
                    if (r.rotate(Direction.NORTH) == direction) {
                        rotation.setValue(r);
                        break;
                    }
                }
            }
            BlockState blockState = original;
            BlockPos.MutableBlockPos mutable = pos.mutable();
            label46: for (PlaceChoices.Flow f : this.flow) {
                for (Entry<Direction, PlaceChoices.Limit> entry : f.when.entrySet()) {
                    Direction direction = ((Rotation) rotation.getValue()).rotate((Direction) entry.getKey());
                    try {
                        if (!((PlaceChoices.Limit) entry.getValue()).testFace(level.getBlockState(mutable.setWithOffset(pos, direction)), direction.getOpposite())) {
                            continue label46;
                        }
                    } catch (Exception var14) {
                        continue label46;
                    }
                }
                blockState = f.action.apply(level, mutable, blockState);
                if (f.end) {
                    break;
                }
            }
            return blockState;
        }
    }

    static {
        BLOCK_FACE_TYPES.put("any", PlaceChoices.BlockFaceType.ANY);
        BLOCK_FACE_TYPES.put("horizontal", PlaceChoices.BlockFaceType.HORIZONTAL);
        BLOCK_FACE_TYPES.put("vertical", PlaceChoices.BlockFaceType.VERTICAL);
        BLOCK_FACE_TYPES.put("clicked_face", (PlaceChoices.BlockFaceType) (context, directionx) -> context.getClickedFace() == directionx.getOpposite());
        for (Direction direction : Util.DIRECTIONS) {
            BLOCK_FACE_TYPES.put(direction.getSerializedName(), (PlaceChoices.BlockFaceType) (context, dir) -> dir == direction);
        }
        CODEC = RecordCodecBuilder.create(instance -> instance.group(CustomizationCodecs.compactList(PlaceTarget.CODEC).fieldOf("target").forGetter(PlaceChoices::target), CustomizationCodecs.strictOptionalField(Codec.STRING, "transform_with").forGetter(PlaceChoices::transformWith), CustomizationCodecs.strictOptionalField(CustomizationCodecs.compactList(PlaceChoices.Flow.CODEC), "flow", List.of()).forGetter(PlaceChoices::flow), CustomizationCodecs.strictOptionalField(CustomizationCodecs.compactList(PlaceChoices.Alter.CODEC), "alter", List.of()).forGetter(PlaceChoices::alter), CustomizationCodecs.strictOptionalField(CustomizationCodecs.compactList(PlaceChoices.Limit.CODEC), "limit", List.of()).forGetter(PlaceChoices::limit), CustomizationCodecs.strictOptionalField(CustomizationCodecs.compactList(PlaceChoices.Interests.CODEC), "interests", List.of()).forGetter(PlaceChoices::interests), Codec.BOOL.optionalFieldOf("skippable", true).forGetter(PlaceChoices::skippable)).apply(instance, PlaceChoices::new));
    }

    public static record Alter(List<PlaceChoices.AlterCondition> when, String use) {

        public static final Codec<PlaceChoices.Alter> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.nonEmptyList(CustomizationCodecs.compactList(PlaceChoices.AlterCondition.CODEC)).fieldOf("when").forGetter(PlaceChoices.Alter::when), Codec.STRING.fieldOf("use").forGetter(PlaceChoices.Alter::use)).apply(instance, PlaceChoices.Alter::new));

        @Nullable
        public BlockState alter(BlockItem blockItem, BlockPlaceContext context) {
            if (blockItem instanceof MultipleBlockItem multipleBlockItem) {
                for (PlaceChoices.AlterCondition condition : this.when) {
                    if (condition.test(context)) {
                        Block block = multipleBlockItem.getBlock(this.use);
                        Preconditions.checkNotNull(block, "Block %s not found in %s", this.use, multipleBlockItem);
                        Preconditions.checkState(block != blockItem.getBlock(), "Block %s is the same as the original block, dead loop detected", block);
                        BlockState blockState = block.getStateForPlacement(context);
                        if (blockState == null) {
                            return null;
                        }
                        KBlockSettings settings = KBlockSettings.of(block);
                        if (settings != null) {
                            blockState = settings.getStateForPlacement(blockState, context);
                        }
                        return blockState;
                    }
                }
                return null;
            } else {
                return null;
            }
        }
    }

    public static record AlterCondition(String target, PlaceChoices.BlockFaceType faces, BlockPredicate block, List<ParsedProtoTag> tags) {

        public static final Codec<PlaceChoices.AlterCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(CustomizationCodecs.strictOptionalField(Codec.STRING, "target", "neighbor").forGetter(PlaceChoices.AlterCondition::target), CustomizationCodecs.strictOptionalField(CustomizationCodecs.simpleByNameCodec(PlaceChoices.BLOCK_FACE_TYPES), "faces", PlaceChoices.BlockFaceType.ANY).forGetter(PlaceChoices.AlterCondition::faces), CustomizationCodecs.strictOptionalField(CustomizationCodecs.BLOCK_PREDICATE, "block", BlockPredicate.ANY).forGetter(PlaceChoices.AlterCondition::block), CustomizationCodecs.strictOptionalField(CustomizationCodecs.compactList(ParsedProtoTag.CODEC), "tags", List.of()).forGetter(PlaceChoices.AlterCondition::tags)).apply(instance, PlaceChoices.AlterCondition::new));

        public boolean test(BlockPlaceContext context) {
            String pos = this.target;
            List<Direction> directions = switch(pos) {
                case "clicked_face" ->
                    List.of(context.m_43719_().getOpposite());
                case "neighbor" ->
                    Util.DIRECTIONS;
                default ->
                    throw new IllegalStateException("Unexpected value: " + this.target);
            };
            BlockPos pos = context.getClickedPos();
            BlockPos.MutableBlockPos mutable = pos.mutable();
            label49: for (Direction direction : directions) {
                if (this.faces.test(context, direction)) {
                    BlockState neighbor = context.m_43725_().getBlockState(mutable.setWithOffset(pos, direction));
                    if (BlockPredicateHelper.fastMatch(this.block, neighbor)) {
                        for (ParsedProtoTag tag : this.tags) {
                            ParsedProtoTag resolvedTag = tag.resolve(neighbor, Rotation.NONE);
                            if (PlaceSlot.find(neighbor, direction.getOpposite()).stream().noneMatch(slot -> slot.hasTag(resolvedTag))) {
                                continue label49;
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public interface BlockFaceType extends BiPredicate<UseOnContext, Direction> {

        PlaceChoices.BlockFaceType ANY = (context, direction) -> true;

        PlaceChoices.BlockFaceType HORIZONTAL = (context, direction) -> direction.getAxis().isHorizontal();

        PlaceChoices.BlockFaceType VERTICAL = (context, direction) -> direction.getAxis().isVertical();
    }

    public static record Flow(Map<Direction, PlaceChoices.Limit> when, SlotLink.ResultAction action, boolean end) {

        public static final Codec<PlaceChoices.Flow> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.unboundedMap(CustomizationCodecs.DIRECTION, PlaceChoices.Limit.CODEC).fieldOf("when").forGetter(PlaceChoices.Flow::when), SlotLink.ResultAction.MAP_CODEC.forGetter(PlaceChoices.Flow::action), Codec.BOOL.optionalFieldOf("end", false).forGetter(PlaceChoices.Flow::end)).apply(instance, PlaceChoices.Flow::new));
    }

    public static record Interests(StatePropertiesPredicate when, int bonus) {

        public static final Codec<PlaceChoices.Interests> CODEC = RecordCodecBuilder.create(instance -> instance.group(StatePropertiesPredicate.CODEC.fieldOf("when").forGetter(PlaceChoices.Interests::when), Codec.INT.fieldOf("bonus").forGetter(PlaceChoices.Interests::bonus)).apply(instance, PlaceChoices.Interests::new));
    }

    public static record Limit(String type, List<ParsedProtoTag> tags) {

        public static final Codec<PlaceChoices.Limit> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("type").forGetter(PlaceChoices.Limit::type), CustomizationCodecs.compactList(ParsedProtoTag.CODEC).fieldOf("tags").forGetter(PlaceChoices.Limit::tags)).apply(instance, PlaceChoices.Limit::new));

        public boolean test(BlockState baseState, BlockState targetState) {
            for (ParsedProtoTag tag : this.tags) {
                ParsedProtoTag resolvedTag = tag.resolve(baseState, Rotation.NONE);
                String var7 = this.type;
                byte var8 = -1;
                switch(var7.hashCode()) {
                    case 140831390:
                        if (var7.equals("has_tags")) {
                            var8 = 0;
                        }
                }
                boolean var10000 = switch(var8) {
                    case 0 ->
                        {
                        }
                    default ->
                        false;
                };
                boolean pass = var10000;
                if (!pass) {
                    return false;
                }
            }
            return true;
        }

        public boolean testFace(BlockState blockState, Direction direction) {
            Collection<PlaceSlot> slots = PlaceSlot.find(blockState, direction);
            for (ParsedProtoTag tag : this.tags) {
                ParsedProtoTag resolvedTag = tag.resolve(blockState, Rotation.NONE);
                String var8 = this.type;
                byte var9 = -1;
                switch(var8.hashCode()) {
                    case 140831390:
                        if (var8.equals("has_tags")) {
                            var9 = 0;
                        }
                }
                boolean var10000 = switch(var9) {
                    case 0 ->
                        {
                        }
                    default ->
                        false;
                };
                boolean pass = var10000;
                if (!pass) {
                    return false;
                }
            }
            return true;
        }
    }

    public static record Preparation(Map<ResourceLocation, PlaceChoices> choices, Map<KBlockTemplate, KHolder<PlaceChoices>> byTemplate, Map<ResourceLocation, KHolder<PlaceChoices>> byBlock) {

        public static PlaceChoices.Preparation of(Supplier<Map<ResourceLocation, PlaceChoices>> choicesSupplier, Map<ResourceLocation, KBlockTemplate> templates) {
            Map<ResourceLocation, PlaceChoices> choices = Platform.isDataGen() ? Map.of() : (Map) choicesSupplier.get();
            Map<KBlockTemplate, KHolder<PlaceChoices>> byTemplate = Maps.newHashMap();
            Map<ResourceLocation, KHolder<PlaceChoices>> byBlock = Maps.newHashMap();
            for (Entry<ResourceLocation, PlaceChoices> entry : choices.entrySet()) {
                KHolder<PlaceChoices> holder = new KHolder<>((ResourceLocation) entry.getKey(), (PlaceChoices) entry.getValue());
                for (PlaceTarget target : holder.value().target) {
                    switch(target.type()) {
                        case TEMPLATE:
                            KBlockTemplate template = (KBlockTemplate) templates.get(target.id());
                            if (template == null) {
                                Kiwi.LOGGER.error("Template {} not found for place choices {}", target.id(), holder);
                            } else {
                                KHolder<PlaceChoices> oldChoicesx = (KHolder<PlaceChoices>) byTemplate.put(template, holder);
                                if (oldChoicesx != null) {
                                    Kiwi.LOGGER.error("Duplicate place choices for template {}: {} and {}", new Object[] { template, oldChoicesx, holder });
                                }
                            }
                            break;
                        case BLOCK:
                            KHolder<PlaceChoices> oldChoices = (KHolder<PlaceChoices>) byBlock.put(target.id(), holder);
                            if (oldChoices != null) {
                                Kiwi.LOGGER.error("Duplicate place choices for block {}: {} and {}", new Object[] { target.id(), oldChoices, holder });
                            }
                    }
                }
            }
            return new PlaceChoices.Preparation(choices, byTemplate, byBlock);
        }

        public boolean attachChoicesA(Block block, KBlockDefinition definition) {
            KHolder<PlaceChoices> choices = (KHolder<PlaceChoices>) this.byTemplate.get(definition.template().template());
            PlaceChoices.setTo(block, choices);
            return choices != null;
        }

        public int attachChoicesB() {
            AtomicInteger counter = new AtomicInteger();
            this.byBlock.forEach((blockId, choices) -> {
                Block block = BuiltInRegistries.BLOCK.get(blockId);
                if (block == Blocks.AIR) {
                    Kiwi.LOGGER.error("Block %s not found for place choices %s".formatted(blockId, choices));
                } else {
                    PlaceChoices.setTo(block, choices);
                    counter.incrementAndGet();
                }
            });
            return counter.get();
        }
    }
}
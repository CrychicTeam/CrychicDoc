package snownee.kiwi.customization.placement;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.apache.commons.lang3.mutable.MutableObject;
import snownee.kiwi.Kiwi;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.customization.block.loader.KBlockDefinition;
import snownee.kiwi.customization.block.loader.KBlockTemplate;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.util.KHolder;
import snownee.kiwi.util.Util;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record PlaceSlotProvider(List<PlaceTarget> target, Optional<String> transformWith, List<String> tag, List<PlaceSlotProvider.Slot> slots) {

    public static final Predicate<String> TAG_PATTERN = Pattern.compile("^[*@]?(?:[a-z0-9_/.]+:)?[a-z0-9_/.]+$").asPredicate();

    public static final Codec<String> TAG_CODEC = ExtraCodecs.validate(Codec.STRING, s -> TAG_PATTERN.test(s) ? DataResult.success(s) : DataResult.error(() -> "Bad tag format: " + s));

    public static final Codec<PlaceSlotProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(CustomizationCodecs.compactList(PlaceTarget.CODEC).fieldOf("target").forGetter(PlaceSlotProvider::target), CustomizationCodecs.strictOptionalField(Codec.STRING, "transform_with").forGetter(PlaceSlotProvider::transformWith), CustomizationCodecs.strictOptionalField(TAG_CODEC.listOf(), "tag", List.of()).forGetter(PlaceSlotProvider::tag), PlaceSlotProvider.Slot.CODEC.listOf().fieldOf("slots").forGetter(PlaceSlotProvider::slots)).apply(instance, PlaceSlotProvider::new));

    private void attachSlots(PlaceSlotProvider.Preparation preparation, Block block) {
        for (PlaceSlotProvider.Slot slot : this.slots) {
            UnmodifiableIterator var5 = block.getStateDefinition().getPossibleStates().iterator();
            while (var5.hasNext()) {
                BlockState blockState = (BlockState) var5.next();
                if ((!blockState.m_61138_(BlockStateProperties.WATERLOGGED) || !(Boolean) blockState.m_61143_(BlockStateProperties.WATERLOGGED)) && (slot.when.isEmpty() || !slot.when.stream().noneMatch(predicate -> predicate.test(blockState)))) {
                    for (Direction direction : Util.DIRECTIONS) {
                        PlaceSlotProvider.Side side = (PlaceSlotProvider.Side) slot.sides.get(direction);
                        if (side != null) {
                            PlaceSlot placeSlot = new PlaceSlot(direction, this.generateTags(slot, side, blockState, Rotation.NONE));
                            preparation.register(blockState, placeSlot);
                        }
                    }
                    String transformWith = (String) (slot.transformWith.isPresent() ? slot.transformWith : this.transformWith).orElse("none");
                    if (!"none".equals(transformWith)) {
                        if (!(KBlockUtils.getProperty(blockState, transformWith) instanceof DirectionProperty directionProperty)) {
                            throw new IllegalArgumentException("Invalid transform_with property: " + transformWith);
                        }
                        this.attachSlotWithTransformation(preparation, slot, blockState, directionProperty);
                    }
                }
            }
        }
    }

    private void attachSlotWithTransformation(PlaceSlotProvider.Preparation preparation, PlaceSlotProvider.Slot slot, BlockState blockState, DirectionProperty property) {
        Direction baseDirection = (Direction) blockState.m_61143_(property);
        BlockState rotatedState = blockState;
        while ((rotatedState = (BlockState) rotatedState.m_61122_(property)) != blockState) {
            Direction newDirection = (Direction) rotatedState.m_61143_(property);
            if (!Direction.Plane.VERTICAL.test(newDirection)) {
                Rotation rotation = null;
                for (Rotation value : Rotation.values()) {
                    if (value.rotate(baseDirection) == newDirection) {
                        rotation = value;
                        break;
                    }
                }
                if (rotation == null) {
                    throw new IllegalStateException("Invalid direction: " + newDirection);
                }
                for (Direction direction : Util.DIRECTIONS) {
                    PlaceSlotProvider.Side side = (PlaceSlotProvider.Side) slot.sides.get(direction);
                    if (side != null) {
                        PlaceSlot placeSlot = new PlaceSlot(rotation.rotate(direction), this.generateTags(slot, side, rotatedState, rotation));
                        preparation.register(rotatedState, placeSlot);
                    }
                }
            }
        }
    }

    private ImmutableSortedMap<String, String> generateTags(PlaceSlotProvider.Slot slot, PlaceSlotProvider.Side side, BlockState rotatedState, Rotation rotation) {
        Map<String, String> map = Maps.newHashMap();
        MutableObject<String> primaryKey = new MutableObject();
        Streams.concat(new Stream[] { this.tag.stream(), slot.tag.stream(), side.tag.stream() }).forEach(s -> {
            ParsedProtoTag tag = ParsedProtoTag.of(s).resolve(rotatedState, rotation);
            if (tag.prefix().equals("*")) {
                if (primaryKey.getValue() == null) {
                    primaryKey.setValue(tag.key());
                } else if (!Objects.equals(primaryKey.getValue(), tag.key())) {
                    throw new IllegalArgumentException("Only one primary tag is allowed");
                }
            }
            map.put(tag.key(), tag.value());
        });
        if (primaryKey.getValue() == null) {
            throw new IllegalArgumentException("Primary tag is required");
        } else {
            String primaryValue = (String) map.get(primaryKey.getValue());
            map.remove(primaryKey.getValue());
            if (primaryValue.isEmpty()) {
                map.put("*%s".formatted(primaryKey.getValue()), "");
            } else {
                map.put("*%s:%s".formatted(primaryKey.getValue(), primaryValue), "");
            }
            return ImmutableSortedMap.copyOf(map, PlaceSlot.TAG_COMPARATOR);
        }
    }

    public static record Preparation(Map<ResourceLocation, PlaceSlotProvider> providers, ListMultimap<KBlockTemplate, KHolder<PlaceSlotProvider>> byTemplate, ListMultimap<ResourceLocation, KHolder<PlaceSlotProvider>> byBlock, ListMultimap<Pair<BlockState, Direction>, PlaceSlot> slots, Interner<PlaceSlot> slotInterner, Set<Block> accessedBlocks, Set<String> knownPrimaryTags) {

        public static PlaceSlotProvider.Preparation of(Supplier<Map<ResourceLocation, PlaceSlotProvider>> providersSupplier, Map<ResourceLocation, KBlockTemplate> templates) {
            Map<ResourceLocation, PlaceSlotProvider> providers = Platform.isDataGen() ? Map.of() : (Map) providersSupplier.get();
            ListMultimap<KBlockTemplate, KHolder<PlaceSlotProvider>> byTemplate = ArrayListMultimap.create();
            ListMultimap<ResourceLocation, KHolder<PlaceSlotProvider>> byBlock = ArrayListMultimap.create();
            for (Entry<ResourceLocation, PlaceSlotProvider> entry : providers.entrySet()) {
                KHolder<PlaceSlotProvider> holder = new KHolder<>((ResourceLocation) entry.getKey(), (PlaceSlotProvider) entry.getValue());
                for (PlaceTarget target : holder.value().target) {
                    switch(target.type()) {
                        case TEMPLATE:
                            KBlockTemplate template = (KBlockTemplate) templates.get(target.id());
                            if (template == null) {
                                Kiwi.LOGGER.error("Template {} not found for slot provider {}", target.id(), holder);
                            } else {
                                byTemplate.put(template, holder);
                            }
                            break;
                        case BLOCK:
                            byBlock.put(target.id(), holder);
                    }
                }
            }
            return new PlaceSlotProvider.Preparation(providers, byTemplate, byBlock, ArrayListMultimap.create(), Interners.newStrongInterner(), Sets.newHashSet(), Sets.newHashSet());
        }

        public void attachSlotsA(Block block, KBlockDefinition definition) {
            if (this.accessedBlocks.add(block)) {
                for (KHolder<PlaceSlotProvider> holder : this.byTemplate.get(definition.template().template())) {
                    try {
                        holder.value().attachSlots(this, block);
                    } catch (Exception var6) {
                        Kiwi.LOGGER.error("Failed to attach slots for block %s with provider %s".formatted(block, holder), var6);
                    }
                }
            }
        }

        public void attachSlotsB() {
            this.byBlock.asMap().forEach((blockId, holders) -> {
                Block block = BuiltInRegistries.BLOCK.get(blockId);
                if (block == Blocks.AIR) {
                    Kiwi.LOGGER.error("Block %s not found for slot providers %s".formatted(blockId, holders));
                } else {
                    for (KHolder<PlaceSlotProvider> holder : holders) {
                        try {
                            holder.value().attachSlots(this, block);
                        } catch (Exception var7) {
                            Kiwi.LOGGER.error("Failed to attach slots for block %s with provider %s".formatted(block, holder), var7);
                        }
                    }
                }
            });
            PlaceSlot.renewData(this);
        }

        public void register(BlockState blockState, PlaceSlot placeSlot) {
            if (blockState.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) blockState.m_61143_(BlockStateProperties.WATERLOGGED)) {
                throw new IllegalArgumentException("Waterlogged block state is not supported: %s".formatted(blockState));
            } else {
                Pair<BlockState, Direction> key = Pair.of(blockState, placeSlot.side());
                Collection<PlaceSlot> slots = this.slots().get(key);
                if (!slots.isEmpty()) {
                    String primaryTag = placeSlot.primaryTag();
                    Optional<PlaceSlot> any = slots.stream().filter(slot -> slot.primaryTag().equals(primaryTag)).findAny();
                    if (any.isPresent()) {
                        throw new IllegalArgumentException("Primary tag %s conflict: %s and %s".formatted(primaryTag, placeSlot, any.get()));
                    }
                }
                placeSlot = (PlaceSlot) this.slotInterner.intern(placeSlot);
                this.slots().put(key, placeSlot);
                this.knownPrimaryTags().add(placeSlot.primaryTag());
            }
        }
    }

    public static record Side(List<String> tag) {

        public static final Codec<PlaceSlotProvider.Side> CODEC = RecordCodecBuilder.create(instance -> instance.group(CustomizationCodecs.strictOptionalField(PlaceSlotProvider.TAG_CODEC.listOf(), "tag", List.of()).forGetter(PlaceSlotProvider.Side::tag)).apply(instance, PlaceSlotProvider.Side::new));
    }

    public static record Slot(List<StatePropertiesPredicate> when, Optional<String> transformWith, List<String> tag, Map<Direction, PlaceSlotProvider.Side> sides) {

        public static final Codec<PlaceSlotProvider.Slot> CODEC = RecordCodecBuilder.create(instance -> instance.group(CustomizationCodecs.strictOptionalField(ExtraCodecs.nonEmptyList(CustomizationCodecs.compactList(StatePropertiesPredicate.CODEC)), "when", List.of()).forGetter(PlaceSlotProvider.Slot::when), CustomizationCodecs.strictOptionalField(Codec.STRING, "transform_with").forGetter(PlaceSlotProvider.Slot::transformWith), CustomizationCodecs.strictOptionalField(PlaceSlotProvider.TAG_CODEC.listOf(), "tag", List.of()).forGetter(PlaceSlotProvider.Slot::tag), Codec.unboundedMap(CustomizationCodecs.DIRECTION, PlaceSlotProvider.Side.CODEC).xmap(Map::copyOf, Function.identity()).fieldOf("sides").forGetter(PlaceSlotProvider.Slot::sides)).apply(instance, PlaceSlotProvider.Slot::new));
    }
}
package snownee.lychee.core.def;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.mixin.BlockPredicateAccess;
import snownee.lychee.mixin.StatePropertiesPredicateAccess;
import snownee.lychee.util.CommonProxy;

public class BlockPredicateHelper {

    public static final NbtPredicate NBT_PREDICATE_DUMMY = new NbtPredicate(new CompoundTag());

    private static final Cache<BlockPredicate, List<BlockState>> CACHE = CacheBuilder.newBuilder().expireAfterAccess(10L, TimeUnit.MINUTES).build();

    public static Set<Property<?>> ITERABLE_PROPERTIES = Sets.newConcurrentHashSet();

    public static Set<Block> getMatchedBlocks(BlockPredicate predicate) {
        BlockPredicateAccess access = (BlockPredicateAccess) predicate;
        Set<Block> blocks = Sets.newLinkedHashSet();
        if (access.getBlocks() != null) {
            blocks.addAll(access.getBlocks());
        }
        if (access.getTag() != null) {
            blocks.addAll(CommonProxy.<Block>tagElements(BuiltInRegistries.BLOCK, access.getTag()));
        }
        return blocks;
    }

    public static Set<Fluid> getMatchedFluids(BlockPredicate predicate) {
        return (Set<Fluid>) getMatchedBlocks(predicate).stream().filter(LiquidBlock.class::isInstance).map(Block::m_49966_).map(BlockBehaviour.BlockStateBase::m_60819_).filter(Predicate.not(FluidState::m_76178_)).map(FluidState::m_76152_).collect(Collectors.toSet());
    }

    public static List<ItemStack> getMatchedItemStacks(BlockPredicate predicate) {
        return getMatchedBlocks(predicate).stream().map(Block::m_5456_).filter(Predicate.not(Items.AIR::equals)).distinct().map(Item::m_7968_).toList();
    }

    public static boolean fastMatch(BlockPredicate predicate, LycheeContext context) {
        return fastMatch(predicate, context.getParam(LootContextParams.BLOCK_STATE), () -> context.getParamOrNull(LootContextParams.BLOCK_ENTITY));
    }

    public static boolean fastMatch(BlockPredicate predicate, BlockState blockstate, Supplier<BlockEntity> beGetter) {
        if (predicate == BlockPredicate.ANY) {
            return true;
        } else {
            BlockPredicateAccess access = (BlockPredicateAccess) predicate;
            if (access.getTag() != null && !blockstate.m_204336_(access.getTag())) {
                return false;
            } else if (access.getBlocks() != null && !access.getBlocks().contains(blockstate.m_60734_())) {
                return false;
            } else if (!access.getProperties().matches(blockstate)) {
                return false;
            } else {
                if (access.getNbt() != NbtPredicate.ANY) {
                    BlockEntity blockentity = (BlockEntity) beGetter.get();
                    if (blockentity == null || !access.getNbt().matches(blockentity.saveWithFullMetadata())) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public static BlockPredicate fromJson(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonPrimitive()) {
            String id = jsonElement.getAsString();
            if ("*".equals(id)) {
                return BlockPredicate.ANY;
            } else if (id.startsWith("#")) {
                TagKey<Block> key = TagKey.create(Registries.BLOCK, new ResourceLocation(id.substring(1)));
                return new BlockPredicate(key, null, StatePropertiesPredicate.ANY, NbtPredicate.ANY);
            } else {
                Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(id));
                return new BlockPredicate(null, Set.of(block), StatePropertiesPredicate.ANY, NbtPredicate.ANY);
            }
        } else {
            return BlockPredicate.fromJson(jsonElement);
        }
    }

    public static JsonElement toJson(BlockPredicate predicate) {
        return (JsonElement) (predicate == BlockPredicate.ANY ? new JsonPrimitive("*") : predicate.serializeToJson());
    }

    public static BlockPredicate fromNetwork(FriendlyByteBuf pBuffer) {
        int blockCount = pBuffer.readVarInt();
        if (blockCount == -1) {
            return BlockPredicate.ANY;
        } else {
            Set<Block> blocks = null;
            if (blockCount > 0) {
                blocks = Sets.newHashSet();
                for (int i = 0; i < blockCount; i++) {
                    blocks.add(CommonProxy.readRegistryId(BuiltInRegistries.BLOCK, pBuffer));
                }
            }
            TagKey<Block> tag = null;
            ResourceLocation tagId = CommonProxy.readNullableRL(pBuffer);
            if (tagId != null) {
                tag = TagKey.create(Registries.BLOCK, tagId);
            }
            StatePropertiesPredicate propertiesPredicate = PropertiesPredicateHelper.fromNetwork(pBuffer);
            NbtPredicate nbtPredicate = pBuffer.readBoolean() ? NBT_PREDICATE_DUMMY : NbtPredicate.ANY;
            return new BlockPredicate(tag, blocks, propertiesPredicate, nbtPredicate);
        }
    }

    public static void toNetwork(BlockPredicate predicate, FriendlyByteBuf pBuffer) {
        if (predicate == BlockPredicate.ANY) {
            pBuffer.writeVarInt(-1);
        } else {
            BlockPredicateAccess access = (BlockPredicateAccess) predicate;
            Set<Block> blocks = access.getBlocks();
            if (blocks == null) {
                pBuffer.writeVarInt(0);
            } else {
                pBuffer.writeVarInt(blocks.size());
                for (Block block : blocks) {
                    CommonProxy.writeRegistryId(BuiltInRegistries.BLOCK, block, pBuffer);
                }
            }
            ResourceLocation tagId = null;
            TagKey<Block> tag = access.getTag();
            if (tag != null) {
                tagId = tag.location();
            }
            CommonProxy.writeNullableRL(tagId, pBuffer);
            PropertiesPredicateHelper.toNetwork(access.getProperties(), pBuffer);
            NbtPredicate nbtPredicate = access.getNbt();
            pBuffer.writeBoolean(nbtPredicate != NbtPredicate.ANY);
        }
    }

    public static BlockState anyBlockState(BlockPredicate predicate) {
        return (BlockState) getShowcaseBlockStates(predicate).stream().findFirst().orElse(Blocks.AIR.defaultBlockState());
    }

    public static List<BlockState> getShowcaseBlockStates(BlockPredicate predicate) {
        try {
            return (List<BlockState>) CACHE.get(predicate, () -> getShowcaseBlockStates(predicate, ITERABLE_PROPERTIES));
        } catch (ExecutionException var2) {
            return List.of();
        }
    }

    public static List<BlockState> getShowcaseBlockStates(BlockPredicate predicate, Collection<Property<?>> iterableProperties) {
        Set<Block> blocks = getMatchedBlocks(predicate);
        if (blocks.isEmpty()) {
            return List.of();
        } else {
            List<BlockState> states = Lists.newArrayList();
            BlockPredicateAccess access = (BlockPredicateAccess) predicate;
            StatePropertiesPredicate propertiesPredicate = access.getProperties();
            for (Block block : blocks) {
                BlockState state = block.defaultBlockState();
                Multimap<Property<?>, Comparable> propertyMap = ArrayListMultimap.create();
                for (Property<? extends Comparable> property : block.getStateDefinition().getProperties()) {
                    String name = property.getName();
                    StatePropertiesPredicate.PropertyMatcher matcher = PropertiesPredicateHelper.findMatcher(propertiesPredicate, name);
                    if (matcher != null) {
                        for (Comparable object : property.getPossibleValues()) {
                            if (matcher.match(block.getStateDefinition(), (BlockState) state.m_61124_(property, object))) {
                                propertyMap.put(property, object);
                            }
                        }
                    } else if (iterableProperties.contains(property)) {
                        propertyMap.putAll(property, property.getPossibleValues());
                    }
                }
                Stream<BlockState> stream = Stream.of(state);
                for (Entry<Property<?>, Collection<Comparable>> e : propertyMap.asMap().entrySet()) {
                    stream = stream.flatMap($ -> ((Collection) e.getValue()).stream().map(v -> (BlockState) $.m_61124_((Property) e.getKey(), v)));
                }
                states.addAll(stream.toList());
            }
            return states;
        }
    }

    public static List<Component> getTooltips(BlockState state, BlockPredicate predicate) {
        if (predicate == BlockPredicate.ANY) {
            return List.of(Component.translatable("tip.lychee.anyBlock"));
        } else {
            List<Component> list = Lists.newArrayList();
            list.add(state.m_60734_().getName());
            BlockPredicateAccess access = (BlockPredicateAccess) predicate;
            for (StatePropertiesPredicate.PropertyMatcher matcher : ((StatePropertiesPredicateAccess) access.getProperties()).getProperties()) {
                MutableComponent name = Component.literal(matcher.getName() + "=").withStyle(ChatFormatting.GRAY);
                JsonElement json = matcher.toJson();
                if (json.isJsonPrimitive()) {
                    name.append(Component.literal(json.getAsString()).withStyle(ChatFormatting.WHITE));
                } else {
                    JsonObject object = json.getAsJsonObject();
                    StateDefinition<Block, BlockState> definition = state.m_60734_().getStateDefinition();
                    Property property = definition.getProperty(matcher.getName());
                    List<Comparable> sorted = null;
                    boolean hasMin = object.has("min");
                    boolean hasMax = object.has("max");
                    Preconditions.checkArgument(hasMin || hasMax);
                    if (!hasMin || !hasMax) {
                        sorted = property.getPossibleValues().stream().sorted().toList();
                    }
                    String min;
                    if (hasMin) {
                        min = object.get("min").getAsString();
                    } else {
                        min = property.getName((Comparable) sorted.get(0));
                    }
                    String max;
                    if (hasMax) {
                        max = object.get("max").getAsString();
                    } else {
                        max = property.getName((Comparable) sorted.get(sorted.size() - 1));
                    }
                    name.append(Component.literal(min).withStyle(ChatFormatting.WHITE));
                    name.append(Component.literal("~").withStyle(ChatFormatting.GRAY));
                    name.append(Component.literal(max).withStyle(ChatFormatting.WHITE));
                }
                list.add(name);
            }
            if (access.getNbt() != NbtPredicate.ANY) {
                list.add(Component.translatable("tip.lychee.nbtPredicate").withStyle(ChatFormatting.GRAY));
            }
            return list;
        }
    }

    static {
        ITERABLE_PROPERTIES.addAll(List.of(BlockStateProperties.AGE_1, BlockStateProperties.AGE_2, BlockStateProperties.AGE_3, BlockStateProperties.AGE_5, BlockStateProperties.AGE_7, BlockStateProperties.CANDLES, BlockStateProperties.BITES, BlockStateProperties.POWER, BlockStateProperties.POWERED, BlockStateProperties.LIT, BlockStateProperties.BERRIES, BlockStateProperties.OPEN, BlockStateProperties.DELAY, BlockStateProperties.DISTANCE, BlockStateProperties.LAYERS, BlockStateProperties.PICKLES, BlockStateProperties.LEVEL, BlockStateProperties.LEVEL_HONEY, BlockStateProperties.LEVEL_CAULDRON, BlockStateProperties.DRIPSTONE_THICKNESS));
    }
}
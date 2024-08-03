package net.mehvahdjukaar.supplementaries.integration.quark;

import com.google.common.base.Stopwatch;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.entities.trades.AdventurerMapsHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.tools.item.PathfindersQuillItem;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class CartographersQuillItem extends PathfindersQuillItem {

    public static final String TAG_STRUCTURE = "targetStructure";

    public static final String TAG_SKIP_KNOWN = "skinKnown";

    public static final String TAG_SEARCH_RADIUS = "maxSearchRadius";

    public static final String TAG_ZOOM = "zoomLevel";

    public static final String TAG_DECORATION = "decoration";

    public static final String TAG_NAME = "name";

    protected static final String TAG_RADIUS = "searchRadius";

    protected static final String TAG_POS_INDEX = "searchIndex";

    protected static final String TAG_WAITING = "waiting";

    private static Thread mainThread;

    private static final Map<CartographersQuillItem.Key, InteractionResultHolder<BlockPos>> RESULTS = new ConcurrentHashMap();

    private static final Set<CartographersQuillItem.Key> COMPUTING = ConcurrentHashMap.newKeySet();

    private static final Set<ChunkPos> COMPUTING_CHUNKPOS = ConcurrentHashMap.newKeySet();

    public CartographersQuillItem() {
        super(null, new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> comps, TooltipFlag flags) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            if (ItemNBTHelper.getBoolean(stack, "isSearchingForBiome", false)) {
                comps.add(PathfindersQuillItem.getSearchingComponent().withStyle(ChatFormatting.BLUE));
            }
        } else {
            comps.add(Component.translatable("message.supplementaries.cartographers_quill").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    protected String getFailMessage() {
        return "message.supplementaries.quill_failed";
    }

    @Override
    protected String getSuccessMessage() {
        return "message.supplementaries.quill_finished";
    }

    @Override
    protected String getRetryMessage() {
        return "message.supplementaries.quill_retry";
    }

    @Override
    public int getIterations() {
        return 500;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            CompoundTag tag = player.m_21120_(hand).getOrCreateTag();
            if (!tag.contains("targetStructure")) {
                String str = selectRandomTarget(serverLevel, ModTags.ADVENTURE_MAP_DESTINATIONS);
                if (str != null) {
                    tag.putString("targetStructure", str);
                }
            }
        }
        return super.use(level, player, hand);
    }

    @Nullable
    @Override
    public ResourceLocation getTarget(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        String str = tag.getString("targetStructure");
        return new ResourceLocation(str);
    }

    @Nullable
    private Holder<Structure> getStructureHolder(ServerLevel level, ResourceLocation key) {
        Registry<Structure> reg = level.m_9598_().registryOrThrow(Registries.STRUCTURE);
        Optional<Holder.Reference<Structure>> structure = reg.getHolder(ResourceKey.create(reg.key(), key));
        return (Holder<Structure>) structure.orElse(null);
    }

    @Override
    public ItemStack createMap(ServerLevel level, BlockPos targetPos, ResourceLocation structure, ItemStack original) {
        CompoundTag tag = original.getOrCreateTag();
        ItemStack m = AdventurerMapsHandler.createStructureMap(level, targetPos, this.getStructureHolder(level, structure), this.getZoomLevel(tag), this.getDecoration(tag), this.getMapName(tag), this.getColor(tag));
        if (original.hasCustomHoverName()) {
            m.setHoverName(original.getHoverName());
        }
        return m;
    }

    @Override
    protected InteractionResultHolder<BlockPos> searchConcurrent(ResourceLocation target, ItemStack stack, ServerLevel level, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        Holder<Structure> structure = this.getStructureHolder(level, target);
        CartographersQuillItem.State state = CartographersQuillItem.State.get(tag);
        if (structure != null && state != null) {
            BlockPos center = this.getOrCreateStartPos(tag, player);
            int radius = this.getSearchRadius(tag);
            boolean skipKnown = this.getSkipKnown(tag);
            CartographersQuillItem.Key key = new CartographersQuillItem.Key(GlobalPos.of(level.m_46472_(), center), ((ResourceKey) structure.unwrapKey().get()).location(), radius, skipKnown);
            if (COMPUTING.contains(key)) {
                return InteractionResultHolder.pass(BlockPos.ZERO);
            } else if (RESULTS.containsKey(key)) {
                InteractionResultHolder<BlockPos> ret = (InteractionResultHolder<BlockPos>) RESULTS.remove(key);
                return ret.getResult() == InteractionResult.PASS ? InteractionResultHolder.fail(BlockPos.ZERO) : ret;
            } else {
                ItemStack dummy = stack.copy();
                PathfindersQuillItem.EXECUTORS.submit(() -> {
                    COMPUTING.add(key);
                    RESULTS.put(key, this.searchIterative(target, dummy, level, player, Integer.MAX_VALUE));
                    COMPUTING.remove(key);
                });
                return InteractionResultHolder.pass(BlockPos.ZERO);
            }
        } else {
            return InteractionResultHolder.fail(BlockPos.ZERO);
        }
    }

    @Override
    protected InteractionResultHolder<BlockPos> searchIterative(ResourceLocation target, ItemStack stack, ServerLevel level, Player player, int maxIter) {
        CompoundTag tag = stack.getOrCreateTag();
        Holder<Structure> structure = this.getStructureHolder(level, target);
        CartographersQuillItem.State state = CartographersQuillItem.State.get(tag);
        if (structure != null && state != null) {
            BlockPos center = this.getOrCreateStartPos(tag, player);
            int radius = this.getSearchRadius(tag);
            boolean skipKnown = this.getSkipKnown(tag);
            return this.findNearestMapStructure(level, structure, radius, center, skipKnown, state, maxIter);
        } else {
            return InteractionResultHolder.fail(BlockPos.ZERO);
        }
    }

    @Override
    protected ItemStack search(ItemStack stack, ServerLevel level, Player player, int slot) {
        if (mainThread == null) {
            mainThread = Thread.currentThread();
        }
        return super.search(stack, level, player, slot);
    }

    private BlockPos getOrCreateStartPos(CompoundTag tag, Player player) {
        if (tag.contains("searchSourceX") && tag.contains("searchSourceZ")) {
            int sourceX = tag.getInt("searchSourceX");
            int sourceZ = tag.getInt("searchSourceZ");
            return new BlockPos(sourceX, 64, sourceZ);
        } else {
            BlockPos pos = player.m_20183_();
            tag.putInt("searchSourceX", pos.m_123341_());
            tag.putInt("searchSourceZ", pos.m_123343_());
            return pos;
        }
    }

    private int getSearchRadius(CompoundTag tag) {
        return tag.contains("maxSearchRadius") ? tag.getInt("maxSearchRadius") : 150;
    }

    @Nullable
    private String getMapName(CompoundTag tag) {
        return tag.contains("name") ? tag.getString("name") : null;
    }

    private int getColor(CompoundTag tag) {
        return tag.contains("targetBiomeColor") ? tag.getInt("targetBiomeColor") : 0;
    }

    @Nullable
    private ResourceLocation getDecoration(CompoundTag tag) {
        return tag.contains("decoration") ? new ResourceLocation(tag.getString("decoration")) : null;
    }

    private int getZoomLevel(CompoundTag tag) {
        return tag.contains("zoomLevel") ? tag.getInt("zoomLevel") : 2;
    }

    private boolean getSkipKnown(CompoundTag tag) {
        return tag.contains("skinKnown") ? tag.getBoolean("skinKnown") : true;
    }

    @Nullable
    public InteractionResultHolder<BlockPos> findNearestMapStructure(ServerLevel level, Holder<Structure> holder, int searchRadius, BlockPos center, boolean skipKnownStructures, CartographersQuillItem.State state, int maxIterations) {
        if (!level.getServer().getWorldData().worldGenOptions().generateStructures()) {
            return null;
        } else {
            ServerChunkCache source = level.getChunkSource();
            ChunkGenerator gen = source.getGenerator();
            Map<StructurePlacement, Set<Holder<Structure>>> map = new Object2ObjectArrayMap();
            ChunkGeneratorStructureState structureState = level.getChunkSource().getGeneratorState();
            for (StructurePlacement structurePlacement : structureState.getPlacementsForStructure(holder)) {
                ((Set) map.computeIfAbsent(structurePlacement, ss -> new ObjectArraySet())).add(holder);
            }
            if (map.isEmpty()) {
                return InteractionResultHolder.fail(BlockPos.ZERO);
            } else {
                double d = Double.MAX_VALUE;
                StructureManager structureManager = level.structureManager();
                List<Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>>> list = new ArrayList(map.size());
                for (Entry<StructurePlacement, Set<Holder<Structure>>> ent : map.entrySet()) {
                    StructurePlacement placement = (StructurePlacement) ent.getKey();
                    if (placement instanceof ConcentricRingsStructurePlacement) {
                        ConcentricRingsStructurePlacement concentricRingsStructurePlacement = (ConcentricRingsStructurePlacement) placement;
                        Pair<BlockPos, Holder<Structure>> pair2 = gen.getNearestGeneratedStructure((Set<Holder<Structure>>) ent.getValue(), level, structureManager, center, skipKnownStructures, concentricRingsStructurePlacement);
                        if (pair2 != null) {
                            BlockPos blockPos = (BlockPos) pair2.getFirst();
                            double e = center.m_123331_(blockPos);
                            if (e < d) {
                                return InteractionResultHolder.success((BlockPos) pair2.getFirst());
                            }
                        }
                    } else if (placement instanceof RandomSpreadStructurePlacement rr) {
                        list.add(Pair.of(rr, (Set) ent.getValue()));
                    }
                }
                if (list.isEmpty()) {
                    return null;
                } else {
                    int centerX = SectionPos.blockToSectionCoord(center.m_123341_());
                    int centerY = SectionPos.blockToSectionCoord(center.m_123343_());
                    long seed = level.getSeed();
                    int currentIter = 0;
                    while (state.radius <= searchRadius) {
                        BlockPos found;
                        for (found = null; state.placementInd < list.size(); state.placementInd++) {
                            Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>> pl = (Pair<RandomSpreadStructurePlacement, Set<Holder<Structure>>>) list.get(state.placementInd);
                            RandomSpreadStructurePlacement placement = (RandomSpreadStructurePlacement) pl.getFirst();
                            Set<Holder<Structure>> holderSet = (Set<Holder<Structure>>) pl.getSecond();
                            BlockPos foundPair = null;
                            label107: for (int spacing = placement.spacing(); state.x <= state.radius; state.x++) {
                                for (boolean onEdgeX = state.x == -state.radius || state.x == state.radius; state.z <= state.radius; state.z++) {
                                    boolean onEdgeY = state.z == -state.radius || state.z == state.radius;
                                    if (onEdgeX || onEdgeY) {
                                        currentIter++;
                                        int testX = centerX + spacing * state.x;
                                        int testY = centerY + spacing * state.z;
                                        ChunkPos chunkPos = placement.getPotentialStructureChunk(seed, testX, testY);
                                        Either<BlockPos, ChunkPos> pair = getStructureGeneratingAt(holderSet, source, structureManager, skipKnownStructures, placement, chunkPos, state);
                                        if (pair != null) {
                                            Optional<BlockPos> left = pair.left();
                                            if (!left.isPresent()) {
                                                return InteractionResultHolder.pass(BlockPos.ZERO);
                                            }
                                            foundPair = (BlockPos) left.get();
                                            state.z = -state.radius;
                                            break label107;
                                        }
                                        if (currentIter > maxIterations) {
                                            return InteractionResultHolder.pass(BlockPos.ZERO);
                                        }
                                    }
                                }
                                state.z = -state.radius;
                            }
                            state.x = -state.radius;
                            if (foundPair != null) {
                                double f = center.m_123331_(foundPair);
                                if (f < d) {
                                    d = f;
                                    found = foundPair;
                                }
                            }
                        }
                        state.placementInd = 0;
                        if (found != null) {
                            return InteractionResultHolder.success(found);
                        }
                        state.radius++;
                    }
                    return InteractionResultHolder.fail(BlockPos.ZERO);
                }
            }
        }
    }

    public static Either<BlockPos, ChunkPos> getStructureGeneratingAt(Set<Holder<Structure>> structureHoldersSet, ServerChunkCache chunkCache, StructureManager structureManager, boolean skipKnownStructures, StructurePlacement placement, ChunkPos chunkPos, CartographersQuillItem.State state) {
        Stopwatch s2 = Stopwatch.createStarted();
        for (Holder<Structure> holder : structureHoldersSet) {
            Structure structure = holder.value();
            StructureCheckResult structureCheckResult = structureManager.checkStructurePresence(chunkPos, structure, skipKnownStructures);
            if (structureCheckResult != StructureCheckResult.START_NOT_PRESENT) {
                if (!skipKnownStructures && structureCheckResult == StructureCheckResult.START_PRESENT) {
                    return Either.left(placement.getLocatePos(chunkPos));
                }
                boolean shouldMultiThread = Thread.currentThread() == mainThread;
                ChunkAccess chunkAccess = chunkCache.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS, !shouldMultiThread);
                if (chunkAccess == null && shouldMultiThread) {
                    if (state.waiting && !COMPUTING_CHUNKPOS.contains(chunkPos)) {
                        state.waiting = false;
                    }
                    if (!state.waiting) {
                        PathfindersQuillItem.EXECUTORS.submit(() -> {
                            COMPUTING_CHUNKPOS.add(chunkPos);
                            chunkCache.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS, true);
                            COMPUTING_CHUNKPOS.remove(chunkPos);
                        });
                        state.waiting = true;
                    }
                    if (state.z == -state.radius) {
                        state.z++;
                        if (state.x == -state.radius) {
                            state.x++;
                            state.radius--;
                        } else {
                            state.x--;
                        }
                    } else {
                        state.z--;
                    }
                    Supplementaries.LOGGER.warn("E " + s2.elapsed());
                    return Either.right(chunkPos);
                }
                state.waiting = false;
                StructureStart structureStart = structureManager.getStartForStructure(SectionPos.bottomOf(chunkAccess), structure, chunkAccess);
                if (structureStart != null && structureStart.isValid() && (!skipKnownStructures || tryAddReference(structureManager, structureStart))) {
                    return Either.left(placement.getLocatePos(structureStart.getChunkPos()));
                }
            }
        }
        return null;
    }

    private static boolean tryAddReference(StructureManager structureManager, StructureStart structureStrart) {
        if (structureStrart.canBeReferenced()) {
            structureManager.addReference(structureStrart);
            return true;
        } else {
            return false;
        }
    }

    public static ItemStack forStructure(ServerLevel level, @Nullable HolderSet<Structure> targets, int searchRadius, boolean skipKnown, int zoom, @Nullable MapDecoration.Type deco, @Nullable String name, int color) {
        ItemStack stack = forStructure(level, targets);
        CompoundTag t = stack.getOrCreateTag();
        t.putInt("maxSearchRadius", searchRadius);
        t.putBoolean("skinKnown", skipKnown);
        t.putInt("zoomLevel", zoom);
        if (deco != null) {
            t.putString("decoration", deco.toString().toLowerCase(Locale.ROOT));
        }
        if (name != null) {
            t.putString("name", name);
        }
        if (color != 0) {
            t.putInt("targetBiomeColor", color);
        }
        return stack;
    }

    public static int getItemColor(ItemStack stack, int layer) {
        if (layer == 0) {
            return -1;
        } else {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag != null && compoundTag.contains("targetBiomeColor")) {
                int i = compoundTag.getInt("targetBiomeColor");
                return 0xFF000000 | i & 16777215;
            } else {
                return 0;
            }
        }
    }

    public static ItemStack forStructure(ServerLevel level, @Nullable HolderSet<Structure> tag) {
        ItemStack stack = ((Item) QuarkCompat.CARTOGRAPHERS_QUILL.get()).getDefaultInstance();
        if (tag != null) {
            String target = selectRandomTarget(level, tag);
            if (target == null) {
                return ItemStack.EMPTY;
            }
            stack.getOrCreateTag().putString("targetStructure", target);
        }
        return stack;
    }

    @Nullable
    private static String selectRandomTarget(ServerLevel level, TagKey<Structure> tag) {
        Optional<HolderSet.Named<Structure>> targets = level.m_9598_().registryOrThrow(Registries.STRUCTURE).getTag(tag);
        return (String) targets.map(holders -> selectRandomTarget(level, holders)).orElse(null);
    }

    @Nullable
    private static String selectRandomTarget(ServerLevel level, HolderSet<Structure> taggedStructures) {
        List<Holder<Structure>> reachable = new ArrayList();
        for (Holder<Structure> s : taggedStructures) {
            ChunkGeneratorStructureState randomState = level.getChunkSource().getGeneratorState();
            if (!randomState.getPlacementsForStructure(s).isEmpty()) {
                reachable.add(s);
            }
        }
        if (!reachable.isEmpty()) {
            Holder<Structure> selected = (Holder<Structure>) reachable.get(level.f_46441_.nextInt(reachable.size()));
            return ((ResourceKey) selected.unwrapKey().get()).location().toString();
        } else {
            return null;
        }
    }

    private static record Key(GlobalPos pos, ResourceLocation structure, int radius, boolean bool) {
    }

    private static final class State {

        private boolean waiting;

        private int radius;

        private int x;

        private int z;

        private int placementInd;

        private State(int lastRadius, int lastX, int lastZ, int index, boolean waiting) {
            this.radius = lastRadius;
            this.x = lastX;
            this.z = lastZ;
            this.placementInd = index;
            this.waiting = waiting;
        }

        public void save(CompoundTag tag) {
            tag.putInt("searchRadius", this.radius);
            tag.putInt("searchPosX", this.x);
            tag.putInt("searchPosZ", this.z);
            tag.putInt("searchIndex", this.placementInd);
            tag.putBoolean("waiting", this.waiting);
        }

        @Nullable
        private static CartographersQuillItem.State get(CompoundTag tag) {
            int radius = 0;
            if (tag.contains("searchRadius")) {
                radius = tag.getInt("searchRadius");
            }
            int x = 0;
            if (tag.contains("searchPosX")) {
                x = tag.getInt("searchPosX");
            }
            int z = 0;
            if (tag.contains("searchPosZ")) {
                z = tag.getInt("searchPosZ");
            }
            int index = 0;
            if (tag.contains("searchIndex")) {
                index = tag.getInt("searchIndex");
            }
            boolean waiting = false;
            if (tag.contains("waiting")) {
                waiting = tag.getBoolean("waiting");
            }
            return x <= radius && z <= radius ? new CartographersQuillItem.State(radius, x, z, index, waiting) : null;
        }
    }
}
package me.jellysquid.mods.lithium.common.block;

import cpw.mods.modlauncher.api.INameMappingService.Domain;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2BooleanMap.Entry;
import java.util.ArrayList;
import me.jellysquid.mods.lithium.common.ai.pathing.BlockStatePathingCache;
import me.jellysquid.mods.lithium.common.ai.pathing.PathNodeCache;
import me.jellysquid.mods.lithium.common.entity.FluidCachingEntity;
import me.jellysquid.mods.lithium.common.reflection.ReflectionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class BlockStateFlags {

    public static final boolean ENABLED = BlockCountingSection.class.isAssignableFrom(LevelChunkSection.class);

    public static final int NUM_LISTENING_FLAGS;

    public static final ListeningBlockStatePredicate[] LISTENING_FLAGS;

    public static final int LISTENING_MASK_OR;

    public static final ListeningBlockStatePredicate ANY;

    public static final int NUM_TRACKED_FLAGS;

    public static final TrackedBlockStatePredicate[] TRACKED_FLAGS;

    public static final TrackedBlockStatePredicate OVERSIZED_SHAPE;

    public static final TrackedBlockStatePredicate PATH_NOT_OPEN;

    public static final TrackedBlockStatePredicate ANY_FLUID;

    public static final TrackedBlockStatePredicate[] FLAGS;

    public static final TrackedBlockStatePredicate ENTITY_TOUCHABLE;

    static {
        Reference2BooleanArrayMap<ListeningBlockStatePredicate> listeningFlags = new Reference2BooleanArrayMap();
        ANY = new ListeningBlockStatePredicate(listeningFlags.size()) {

            public boolean test(BlockState operand) {
                return true;
            }
        };
        listeningFlags.put(ANY, false);
        NUM_LISTENING_FLAGS = listeningFlags.size();
        int listenMaskOR = 0;
        int iteration = 0;
        ObjectIterator countingFlags = listeningFlags.reference2BooleanEntrySet().iterator();
        while (countingFlags.hasNext()) {
            Entry<ListeningBlockStatePredicate> entry = (Entry<ListeningBlockStatePredicate>) countingFlags.next();
            boolean listenOnlyXOR = entry.getBooleanValue();
            listenMaskOR |= listenOnlyXOR ? 0 : 1 << iteration;
        }
        LISTENING_MASK_OR = listenMaskOR;
        LISTENING_FLAGS = (ListeningBlockStatePredicate[]) listeningFlags.keySet().toArray(new ListeningBlockStatePredicate[NUM_LISTENING_FLAGS]);
        ArrayList<TrackedBlockStatePredicate> countingFlagsx = new ArrayList(listeningFlags.keySet());
        OVERSIZED_SHAPE = new TrackedBlockStatePredicate(countingFlagsx.size()) {

            public boolean test(BlockState operand) {
                return operand.m_60779_();
            }
        };
        countingFlagsx.add(OVERSIZED_SHAPE);
        if (FluidCachingEntity.class.isAssignableFrom(Entity.class)) {
            ANY_FLUID = new TrackedBlockStatePredicate(countingFlagsx.size()) {

                public boolean test(BlockState operand) {
                    return !operand.m_60819_().isEmpty();
                }
            };
            countingFlagsx.add(ANY_FLUID);
        } else {
            ANY_FLUID = null;
        }
        if (BlockStatePathingCache.class.isAssignableFrom(BlockBehaviour.BlockStateBase.class)) {
            PATH_NOT_OPEN = new TrackedBlockStatePredicate(countingFlagsx.size()) {

                public boolean test(BlockState operand) {
                    return PathNodeCache.getNeighborPathNodeType(operand) != BlockPathTypes.OPEN;
                }
            };
            countingFlagsx.add(PATH_NOT_OPEN);
        } else {
            PATH_NOT_OPEN = null;
        }
        NUM_TRACKED_FLAGS = countingFlagsx.size();
        TRACKED_FLAGS = (TrackedBlockStatePredicate[]) countingFlagsx.toArray(new TrackedBlockStatePredicate[NUM_TRACKED_FLAGS]);
        ArrayList<TrackedBlockStatePredicate> flags = new ArrayList(countingFlagsx);
        ENTITY_TOUCHABLE = new TrackedBlockStatePredicate(countingFlagsx.size()) {

            private final String remapped_onEntityCollision = ObfuscationReflectionHelper.remapName(Domain.METHOD, "m_7892_");

            public boolean test(BlockState operand) {
                return ReflectionUtil.hasMethodOverride(operand.m_60734_().getClass(), BlockBehaviour.class, true, this.remapped_onEntityCollision, BlockState.class, Level.class, BlockPos.class, Entity.class);
            }
        };
        flags.add(ENTITY_TOUCHABLE);
        FLAGS = (TrackedBlockStatePredicate[]) flags.toArray(new TrackedBlockStatePredicate[0]);
    }
}
package org.violetmoon.quark.content.automation.module;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.common.util.NonNullConsumer;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.api.IPistonCallback;
import org.violetmoon.quark.api.QuarkCapabilities;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.module.SturdyStoneModule;
import org.violetmoon.zeta.api.IIndirectConnector;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.ZLevelTick;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.piston.ZetaPistonStructureResolver;

@ZetaLoadModule(category = "automation")
public class PistonsMoveTileEntitiesModule extends ZetaModule {

    private static final WeakHashMap<Level, Map<BlockPos, CompoundTag>> movements = new WeakHashMap();

    private static final WeakHashMap<Level, List<Pair<BlockPos, CompoundTag>>> delayedUpdates = new WeakHashMap();

    @Config
    public static boolean enableChestsMovingTogether = true;

    public static boolean staticEnabled;

    @Config
    public static List<String> renderBlacklist = Lists.newArrayList(new String[] { "psi:programmer", "botania:starfield" });

    @Config
    public static List<String> movementBlacklist = Lists.newArrayList(new String[] { "minecraft:spawner", "integrateddynamics:cable", "randomthings:blockbreaker", "minecraft:ender_chest", "minecraft:enchanting_table", "minecraft:trapped_chest", "quark:spruce_trapped_chest", "quark:birch_trapped_chest", "quark:jungle_trapped_chest", "quark:acacia_trapped_chest", "quark:dark_oak_trapped_chest", "endergetic:bolloom_bud" });

    @Config
    public static List<String> delayedUpdateList = Lists.newArrayList(new String[] { "minecraft:dispenser", "minecraft:dropper" });

    @LoadEvent
    public final void register(ZRegister event) {
        IIndirectConnector.INDIRECT_STICKY_BLOCKS.add(Pair.of(PistonsMoveTileEntitiesModule.ChestConnection.PREDICATE, PistonsMoveTileEntitiesModule.ChestConnection.INSTANCE));
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        staticEnabled = this.enabled;
    }

    @PlayEvent
    public void onWorldTick(ZLevelTick.End event) {
        if (delayedUpdates.containsKey(event.getLevel())) {
            List<Pair<BlockPos, CompoundTag>> delays = (List<Pair<BlockPos, CompoundTag>>) delayedUpdates.get(event.getLevel());
            if (!delays.isEmpty()) {
                for (Pair<BlockPos, CompoundTag> delay : delays) {
                    BlockPos pos = (BlockPos) delay.getLeft();
                    BlockState state = event.getLevel().getBlockState(pos);
                    BlockEntity entity = loadBlockEntitySafe(event.getLevel(), pos, (CompoundTag) delay.getRight());
                    callCallback(entity, IPistonCallback::onPistonMovementFinished);
                    event.getLevel().updateNeighbourForOutputSignal(pos, state.m_60734_());
                }
                delays.clear();
            }
        }
    }

    @PlayEvent
    public void addAdditionalHints(ZGatherHints event) {
        MutableComponent comp = Component.translatable("quark.jei.hint.piston_te");
        if (Quark.ZETA.modules.isEnabled(SturdyStoneModule.class)) {
            comp = comp.append(" ").append(Component.translatable("quark.jei.hint.piston_sturdy"));
        }
        if (ZetaPistonStructureResolver.GlobalSettings.getPushLimit() != 12) {
            comp = comp.append(" ").append(Component.translatable("quark.jei.hint.piston_max_blocks", ZetaPistonStructureResolver.GlobalSettings.getPushLimit()));
        }
        event.accept(Items.PISTON, comp);
        event.accept(Items.STICKY_PISTON, comp);
    }

    public static boolean shouldMoveTE(boolean te, BlockState state) {
        return !Quark.ZETA.modules.isEnabled(PistonsMoveTileEntitiesModule.class) ? te : shouldMoveTE(state);
    }

    public static boolean shouldMoveTE(BlockState state) {
        if (state.m_61148_().containsKey(JukeboxBlock.HAS_RECORD) && (Boolean) state.m_61143_(JukeboxBlock.HAS_RECORD)) {
            return true;
        } else if (state.m_60734_() == Blocks.PISTON_HEAD) {
            return true;
        } else {
            ResourceLocation res = BuiltInRegistries.BLOCK.getKey(state.m_60734_());
            return res == null || movementBlacklist.contains(res.toString()) || movementBlacklist.contains(res.getNamespace());
        }
    }

    public static void detachTileEntities(Level world, PistonStructureResolver helper, Direction facing, boolean extending) {
        if (Quark.ZETA.modules.isEnabled(PistonsMoveTileEntitiesModule.class)) {
            if (!extending) {
                facing = facing.getOpposite();
            }
            for (BlockPos pos : helper.getToPush()) {
                BlockState state = world.getBlockState(pos);
                if (state.m_60734_() instanceof EntityBlock) {
                    BlockEntity tile = world.getBlockEntity(pos);
                    if (tile != null) {
                        callCallback(tile, IPistonCallback::onPistonMovementStarted);
                        CompoundTag tag = tile.saveWithFullMetadata();
                        setMovingBlockEntityData(world, pos.relative(facing), tag);
                        world.removeBlockEntity(pos);
                    }
                }
            }
        }
    }

    public static boolean setPistonBlock(Level world, BlockPos pos, BlockState state, int flags) {
        if (!Quark.ZETA.modules.isEnabled(PistonsMoveTileEntitiesModule.class)) {
            world.setBlock(pos, state, flags);
            return false;
        } else {
            if (!enableChestsMovingTogether && state.m_61148_().containsKey(ChestBlock.TYPE)) {
                state = (BlockState) state.m_61124_(ChestBlock.TYPE, ChestType.SINGLE);
            }
            Block block = state.m_60734_();
            CompoundTag entityTag = getAndClearMovement(world, pos);
            boolean destroyed = false;
            if (entityTag != null) {
                BlockState currState = world.getBlockState(pos);
                BlockEntity currEntity = world.getBlockEntity(pos);
                CompoundTag currTag = currEntity == null ? null : currEntity.saveWithFullMetadata();
                world.removeBlock(pos, false);
                if (!state.m_60710_(world, pos)) {
                    world.setBlock(pos, state, flags);
                    BlockEntity entity = loadBlockEntitySafe(world, pos, entityTag);
                    callCallback(entity, IPistonCallback::onPistonMovementFinished);
                    Block.dropResources(state, world, pos, entity);
                    world.removeBlock(pos, false);
                    destroyed = true;
                }
                if (!destroyed) {
                    world.setBlockAndUpdate(pos, currState);
                    if (currTag != null) {
                        loadBlockEntitySafe(world, pos, currTag);
                    }
                }
            }
            if (!destroyed) {
                world.setBlock(pos, state, flags);
                if (world.getBlockEntity(pos) != null) {
                    world.setBlock(pos, state, 0);
                }
                if (entityTag != null && !world.isClientSide) {
                    if (delayedUpdateList.contains(Objects.toString(BuiltInRegistries.BLOCK.getKey(block)))) {
                        registerDelayedUpdate(world, pos, entityTag);
                    } else {
                        BlockEntity entity = loadBlockEntitySafe(world, pos, entityTag);
                        callCallback(entity, IPistonCallback::onPistonMovementFinished);
                    }
                }
                world.updateNeighborsAt(pos, block);
            }
            return true;
        }
    }

    public static void setMovingBlockEntityData(Level world, BlockPos pos, CompoundTag nbt) {
        ((Map) movements.computeIfAbsent(world, l -> new HashMap())).put(pos, nbt);
    }

    @Deprecated(forRemoval = true)
    public static BlockEntity getMovement(Level world, BlockPos pos) {
        return null;
    }

    public static CompoundTag getMovingBlockEntityData(Level world, BlockPos pos) {
        return getMovingBlockEntityData(world, pos, false);
    }

    private static CompoundTag getMovingBlockEntityData(Level world, BlockPos pos, boolean remove) {
        if (!movements.containsKey(world)) {
            return null;
        } else {
            Map<BlockPos, CompoundTag> worldMovements = (Map<BlockPos, CompoundTag>) movements.get(world);
            if (!worldMovements.containsKey(pos)) {
                return null;
            } else {
                CompoundTag ret = (CompoundTag) worldMovements.get(pos);
                if (remove) {
                    worldMovements.remove(pos);
                }
                return ret;
            }
        }
    }

    private static CompoundTag getAndClearMovement(Level world, BlockPos pos) {
        return getMovingBlockEntityData(world, pos, true);
    }

    private static void registerDelayedUpdate(Level world, BlockPos pos, CompoundTag tag) {
        if (!delayedUpdates.containsKey(world)) {
            delayedUpdates.put(world, new ArrayList());
        }
        ((List) delayedUpdates.get(world)).add(Pair.of(pos, tag));
    }

    private static void callCallback(@Nullable BlockEntity entity, NonNullConsumer<? super IPistonCallback> caller) {
        if (entity != null) {
            IPistonCallback cb = Quark.ZETA.capabilityManager.getCapability(QuarkCapabilities.PISTON_CALLBACK, entity);
            if (cb != null) {
                caller.accept(cb);
            }
        }
    }

    @Nullable
    private static BlockEntity loadBlockEntitySafe(Level level, BlockPos pos, CompoundTag tag) {
        BlockEntity inWorldEntity = level.getBlockEntity(pos);
        String expectedTypeStr = tag.getString("id");
        if (inWorldEntity == null) {
            Quark.LOG.warn("No block entity found at {} (expected {})", pos.m_123344_(), expectedTypeStr);
            return null;
        } else if (inWorldEntity.getType() != BuiltInRegistries.BLOCK_ENTITY_TYPE.get(new ResourceLocation(expectedTypeStr))) {
            Quark.LOG.warn("Wrong block entity found at {} (expected {}, got {})", pos.m_123344_(), expectedTypeStr, BlockEntityType.getKey(inWorldEntity.getType()));
            return null;
        } else {
            inWorldEntity.load(tag);
            inWorldEntity.setChanged();
            return inWorldEntity;
        }
    }

    public static class ChestConnection implements IIndirectConnector {

        public static PistonsMoveTileEntitiesModule.ChestConnection INSTANCE = new PistonsMoveTileEntitiesModule.ChestConnection();

        public static Predicate<BlockState> PREDICATE = PistonsMoveTileEntitiesModule.ChestConnection::isValidState;

        @Override
        public boolean isEnabled() {
            return PistonsMoveTileEntitiesModule.enableChestsMovingTogether;
        }

        private static boolean isValidState(BlockState state) {
            if (!(state.m_60734_() instanceof ChestBlock)) {
                return false;
            } else {
                ChestType type = (ChestType) state.m_61143_(ChestBlock.TYPE);
                return type != ChestType.SINGLE;
            }
        }

        @Override
        public boolean canConnectIndirectly(Level world, BlockPos ourPos, BlockPos sourcePos, BlockState ourState, BlockState sourceState) {
            if (sourceState.isStickyBlock()) {
                return true;
            } else {
                ChestType ourType = (ChestType) ourState.m_61143_(ChestBlock.TYPE);
                Direction baseDirection = (Direction) ourState.m_61143_(ChestBlock.FACING);
                Direction targetDirection = ourType == ChestType.LEFT ? baseDirection.getClockWise() : baseDirection.getCounterClockWise();
                BlockPos targetPos = ourPos.relative(targetDirection);
                return targetPos.equals(sourcePos);
            }
        }
    }
}
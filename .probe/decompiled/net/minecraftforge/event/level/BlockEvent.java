package net.minecraftforge.event.level;

import com.google.common.collect.ImmutableList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockEvent extends Event {

    private static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("forge.debugBlockEvent", "false"));

    private final LevelAccessor level;

    private final BlockPos pos;

    private final BlockState state;

    public BlockEvent(LevelAccessor level, BlockPos pos, BlockState state) {
        this.pos = pos;
        this.level = level;
        this.state = state;
    }

    public LevelAccessor getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockState getState() {
        return this.state;
    }

    @Cancelable
    public static class BlockToolModificationEvent extends BlockEvent {

        private final UseOnContext context;

        private final ToolAction toolAction;

        private final boolean simulate;

        private BlockState state;

        public BlockToolModificationEvent(BlockState originalState, @NotNull UseOnContext context, ToolAction toolAction, boolean simulate) {
            super(context.getLevel(), context.getClickedPos(), originalState);
            this.context = context;
            this.state = originalState;
            this.toolAction = toolAction;
            this.simulate = simulate;
        }

        @Nullable
        public Player getPlayer() {
            return this.context.getPlayer();
        }

        public ItemStack getHeldItemStack() {
            return this.context.getItemInHand();
        }

        public ToolAction getToolAction() {
            return this.toolAction;
        }

        public boolean isSimulated() {
            return this.simulate;
        }

        @NotNull
        public UseOnContext getContext() {
            return this.context;
        }

        public void setFinalState(@Nullable BlockState finalState) {
            this.state = finalState;
        }

        public BlockState getFinalState() {
            return this.state;
        }
    }

    @Cancelable
    public static class BreakEvent extends BlockEvent {

        private final Player player;

        private int exp;

        public BreakEvent(Level level, BlockPos pos, BlockState state, Player player) {
            super(level, pos, state);
            this.player = player;
            if (state != null && ForgeHooks.isCorrectToolForDrops(state, player)) {
                int fortuneLevel = player.m_21205_().getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                int silkTouchLevel = player.m_21205_().getEnchantmentLevel(Enchantments.SILK_TOUCH);
                this.exp = state.getExpDrop(level, level.random, pos, fortuneLevel, silkTouchLevel);
            } else {
                this.exp = 0;
            }
        }

        public Player getPlayer() {
            return this.player;
        }

        public int getExpToDrop() {
            return this.isCanceled() ? 0 : this.exp;
        }

        public void setExpToDrop(int exp) {
            this.exp = exp;
        }
    }

    @HasResult
    public static class CreateFluidSourceEvent extends Event {

        private final Level level;

        private final BlockPos pos;

        private final BlockState state;

        public CreateFluidSourceEvent(Level level, BlockPos pos, BlockState state) {
            this.level = level;
            this.pos = pos;
            this.state = state;
        }

        public Level getLevel() {
            return this.level;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public BlockState getState() {
            return this.state;
        }
    }

    public static class CropGrowEvent extends BlockEvent {

        public CropGrowEvent(Level level, BlockPos pos, BlockState state) {
            super(level, pos, state);
        }

        public static class Post extends BlockEvent.CropGrowEvent {

            private final BlockState originalState;

            public Post(Level level, BlockPos pos, BlockState original, BlockState state) {
                super(level, pos, state);
                this.originalState = original;
            }

            public BlockState getOriginalState() {
                return this.originalState;
            }
        }

        @HasResult
        public static class Pre extends BlockEvent.CropGrowEvent {

            public Pre(Level level, BlockPos pos, BlockState state) {
                super(level, pos, state);
            }
        }
    }

    @Cancelable
    public static class EntityMultiPlaceEvent extends BlockEvent.EntityPlaceEvent {

        private final List<BlockSnapshot> blockSnapshots;

        public EntityMultiPlaceEvent(@NotNull List<BlockSnapshot> blockSnapshots, @NotNull BlockState placedAgainst, @Nullable Entity entity) {
            super((BlockSnapshot) blockSnapshots.get(0), placedAgainst, entity);
            this.blockSnapshots = ImmutableList.copyOf(blockSnapshots);
            if (BlockEvent.DEBUG) {
                System.out.printf("Created EntityMultiPlaceEvent - [PlacedAgainst: %s ][Entity: %s ]\n", placedAgainst, entity);
            }
        }

        public List<BlockSnapshot> getReplacedBlockSnapshots() {
            return this.blockSnapshots;
        }
    }

    @Cancelable
    public static class EntityPlaceEvent extends BlockEvent {

        private final Entity entity;

        private final BlockSnapshot blockSnapshot;

        private final BlockState placedBlock;

        private final BlockState placedAgainst;

        public EntityPlaceEvent(@NotNull BlockSnapshot blockSnapshot, @NotNull BlockState placedAgainst, @Nullable Entity entity) {
            super(blockSnapshot.getLevel(), blockSnapshot.getPos(), !(entity instanceof Player) ? blockSnapshot.getReplacedBlock() : blockSnapshot.getCurrentBlock());
            this.entity = entity;
            this.blockSnapshot = blockSnapshot;
            this.placedBlock = !(entity instanceof Player) ? blockSnapshot.getReplacedBlock() : blockSnapshot.getCurrentBlock();
            this.placedAgainst = placedAgainst;
            if (BlockEvent.DEBUG) {
                System.out.printf("Created EntityPlaceEvent - [PlacedBlock: %s ][PlacedAgainst: %s ][Entity: %s ]\n", this.getPlacedBlock(), placedAgainst, entity);
            }
        }

        @Nullable
        public Entity getEntity() {
            return this.entity;
        }

        public BlockSnapshot getBlockSnapshot() {
            return this.blockSnapshot;
        }

        public BlockState getPlacedBlock() {
            return this.placedBlock;
        }

        public BlockState getPlacedAgainst() {
            return this.placedAgainst;
        }
    }

    @Cancelable
    public static class FarmlandTrampleEvent extends BlockEvent {

        private final Entity entity;

        private final float fallDistance;

        public FarmlandTrampleEvent(Level level, BlockPos pos, BlockState state, float fallDistance, Entity entity) {
            super(level, pos, state);
            this.entity = entity;
            this.fallDistance = fallDistance;
        }

        public Entity getEntity() {
            return this.entity;
        }

        public float getFallDistance() {
            return this.fallDistance;
        }
    }

    @Cancelable
    public static class FluidPlaceBlockEvent extends BlockEvent {

        private final BlockPos liquidPos;

        private BlockState newState;

        private BlockState origState;

        public FluidPlaceBlockEvent(LevelAccessor level, BlockPos pos, BlockPos liquidPos, BlockState state) {
            super(level, pos, state);
            this.liquidPos = liquidPos;
            this.newState = state;
            this.origState = level.m_8055_(pos);
        }

        public BlockPos getLiquidPos() {
            return this.liquidPos;
        }

        public BlockState getNewState() {
            return this.newState;
        }

        public void setNewState(BlockState state) {
            this.newState = state;
        }

        public BlockState getOriginalState() {
            return this.origState;
        }
    }

    @Cancelable
    public static class NeighborNotifyEvent extends BlockEvent {

        private final EnumSet<Direction> notifiedSides;

        private final boolean forceRedstoneUpdate;

        public NeighborNotifyEvent(Level level, BlockPos pos, BlockState state, EnumSet<Direction> notifiedSides, boolean forceRedstoneUpdate) {
            super(level, pos, state);
            this.notifiedSides = notifiedSides;
            this.forceRedstoneUpdate = forceRedstoneUpdate;
        }

        public EnumSet<Direction> getNotifiedSides() {
            return this.notifiedSides;
        }

        public boolean getForceRedstoneUpdate() {
            return this.forceRedstoneUpdate;
        }
    }

    @Cancelable
    public static class PortalSpawnEvent extends BlockEvent {

        private final PortalShape size;

        public PortalSpawnEvent(LevelAccessor level, BlockPos pos, BlockState state, PortalShape size) {
            super(level, pos, state);
            this.size = size;
        }

        public PortalShape getPortalSize() {
            return this.size;
        }
    }
}
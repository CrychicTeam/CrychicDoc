package org.violetmoon.quark.addons.oddities.block.be;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.violetmoon.quark.addons.oddities.block.pipe.BasePipeBlock;
import org.violetmoon.quark.addons.oddities.module.PipesModule;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.zeta.util.MiscUtil;
import org.violetmoon.zeta.util.SimpleInventoryBlockEntity;

public class PipeBlockEntity extends SimpleInventoryBlockEntity {

    private static final String TAG_PIPE_ITEMS = "pipeItems";

    private static final String TAG_CONNECTIONS = "connections";

    private boolean iterating = false;

    public final List<PipeBlockEntity.PipeItem> pipeItems = new LinkedList();

    public final List<PipeBlockEntity.PipeItem> queuedItems = new LinkedList();

    private boolean skipSync = false;

    private final PipeBlockEntity.ConnectionType[] connectionsCache = new PipeBlockEntity.ConnectionType[6];

    private boolean convert = false;

    public PipeBlockEntity(BlockPos pos, BlockState state) {
        super(PipesModule.blockEntityType, pos, state);
    }

    public static boolean isTheGoodDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(2) + 1 == 4 && calendar.get(5) == 1;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PipeBlockEntity be) {
        be.tick();
    }

    public void tick() {
        if (this.convert) {
            this.convert = false;
            this.refreshVisualConnections();
        }
        boolean enabled = this.isPipeEnabled();
        if (!enabled && this.f_58857_.getGameTime() % 10L == 0L && this.f_58857_ instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F), (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5, 3, 0.2, 0.2, 0.2, 0.0);
        }
        BlockState blockAt = this.f_58857_.getBlockState(this.f_58858_);
        if (!this.f_58857_.isClientSide && enabled && blockAt.m_204336_(PipesModule.pipesTag)) {
            for (Direction side : Direction.values()) {
                if (this.connectionsCache[side.ordinal()] == PipeBlockEntity.ConnectionType.OPENING) {
                    double minX = (double) this.f_58858_.m_123341_() + 0.25 + 0.5 * (double) Math.min(0, side.getStepX());
                    double minY = (double) this.f_58858_.m_123342_() + 0.25 + 0.5 * (double) Math.min(0, side.getStepY());
                    double minZ = (double) this.f_58858_.m_123343_() + 0.25 + 0.5 * (double) Math.min(0, side.getStepZ());
                    double maxX = (double) this.f_58858_.m_123341_() + 0.75 + 0.5 * (double) Math.max(0, side.getStepX());
                    double maxY = (double) this.f_58858_.m_123342_() + 0.75 + 0.5 * (double) Math.max(0, side.getStepY());
                    double maxZ = (double) this.f_58858_.m_123343_() + 0.75 + 0.5 * (double) Math.max(0, side.getStepZ());
                    Direction opposite = side.getOpposite();
                    boolean pickedItemsUp = false;
                    Predicate<ItemEntity> predicate = entity -> {
                        if (entity != null && entity.m_6084_()) {
                            Vec3 motion = entity.m_20184_();
                            Direction dir = Direction.getNearest(motion.x, motion.y, motion.z);
                            return dir == opposite;
                        } else {
                            return false;
                        }
                    };
                    for (ItemEntity item : this.f_58857_.m_6443_(ItemEntity.class, new AABB(minX, minY, minZ, maxX, maxY, maxZ), predicate)) {
                        this.passIn(item.getItem().copy(), side);
                        if (PipesModule.doPipesWhoosh) {
                            if (isTheGoodDay()) {
                                this.f_58857_.playSound(null, item.m_20185_(), item.m_20186_(), item.m_20189_(), QuarkSounds.BLOCK_PIPE_PICKUP_LENNY, SoundSource.BLOCKS, 1.0F, 1.0F);
                            } else {
                                this.f_58857_.playSound(null, item.m_20185_(), item.m_20186_(), item.m_20189_(), QuarkSounds.BLOCK_PIPE_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                        }
                        if (PipesModule.emitVibrations) {
                            this.m_58904_().m_220407_(GameEvent.PROJECTILE_LAND, this.m_58899_(), GameEvent.Context.of(this.m_58900_()));
                        }
                        pickedItemsUp = true;
                        item.m_146870_();
                    }
                    if (pickedItemsUp) {
                        this.sync();
                    }
                }
            }
        }
        int currentOut = this.getComparatorOutput();
        if (!this.pipeItems.isEmpty()) {
            if (PipesModule.maxPipeItems > 0 && this.pipeItems.size() > PipesModule.maxPipeItems && !this.f_58857_.isClientSide) {
                this.f_58857_.m_46796_(2001, this.f_58858_, Block.getId(this.f_58857_.getBlockState(this.f_58858_)));
                this.dropItem(new ItemStack(this.m_58900_().m_60734_()));
                this.f_58857_.removeBlock(this.m_58899_(), false);
            }
            ListIterator<PipeBlockEntity.PipeItem> itemItr = this.pipeItems.listIterator();
            this.iterating = true;
            while (itemItr.hasNext()) {
                PipeBlockEntity.PipeItem item = (PipeBlockEntity.PipeItem) itemItr.next();
                Direction lastFacing = item.outgoingFace;
                if (item.tick(this)) {
                    itemItr.remove();
                    if (item.valid) {
                        this.passOut(item);
                    } else if (!this.f_58857_.isClientSide) {
                        this.dropItem(item.stack, lastFacing, true);
                    }
                }
            }
            this.iterating = false;
            this.pipeItems.addAll(this.queuedItems);
            if (!this.queuedItems.isEmpty()) {
                this.sync();
            }
            this.queuedItems.clear();
        }
        if (this.getComparatorOutput() != currentOut) {
            this.f_58857_.updateNeighbourForOutputSignal(this.m_58899_(), this.m_58900_().m_60734_());
        }
    }

    public int getComparatorOutput() {
        return Math.min(15, this.pipeItems.size());
    }

    public Iterator<PipeBlockEntity.PipeItem> getItemIterator() {
        return this.pipeItems.iterator();
    }

    public boolean allowsFullConnection(PipeBlockEntity.ConnectionType conn) {
        if (this.f_58856_.m_60734_() instanceof BasePipeBlock pipe && pipe.allowsFullConnection(conn)) {
            return true;
        }
        return false;
    }

    public boolean passIn(ItemStack stack, Direction face, Direction backlog, long seed, int time) {
        PipeBlockEntity.PipeItem item = new PipeBlockEntity.PipeItem(stack, face, seed);
        item.lastTickUpdated = this.f_58857_.getGameTime();
        item.backloggedFace = backlog;
        if (!this.iterating) {
            int currentOut = this.getComparatorOutput();
            this.pipeItems.add(item);
            item.timeInWorld = time;
            if (this.getComparatorOutput() != currentOut) {
                this.f_58857_.updateNeighbourForOutputSignal(this.m_58899_(), this.m_58900_().m_60734_());
            }
        } else {
            this.queuedItems.add(item);
        }
        return true;
    }

    public boolean passIn(ItemStack stack, Direction face) {
        return this.passIn(stack, face, null, this.f_58857_.random.nextLong(), 0);
    }

    protected void passOut(PipeBlockEntity.PipeItem item) {
        boolean did = false;
        BlockPos targetPos = this.m_58899_().relative(item.outgoingFace);
        if (this.f_58857_.getBlockState(targetPos).m_60734_() instanceof WorldlyContainerHolder) {
            ItemStack result = MiscUtil.putIntoInv(item.stack, this.f_58857_, targetPos, null, item.outgoingFace.getOpposite(), false, false);
            if (result.getCount() != item.stack.getCount()) {
                did = true;
                if (!result.isEmpty()) {
                    this.bounceBack(item, result);
                }
            }
        } else {
            BlockEntity tile = this.f_58857_.getBlockEntity(targetPos);
            if (tile != null) {
                if (tile instanceof PipeBlockEntity pipe) {
                    did = pipe.passIn(item.stack, item.outgoingFace.getOpposite(), null, item.rngSeed, item.timeInWorld);
                } else if (!this.f_58857_.isClientSide) {
                    ItemStack result = MiscUtil.putIntoInv(item.stack, this.f_58857_, targetPos, tile, item.outgoingFace.getOpposite(), false, false);
                    if (result.getCount() != item.stack.getCount()) {
                        did = true;
                        if (!result.isEmpty()) {
                            this.bounceBack(item, result);
                        }
                    }
                }
            }
        }
        if (!did) {
            this.bounceBack(item, null);
        }
    }

    private void bounceBack(PipeBlockEntity.PipeItem item, ItemStack stack) {
        if (!this.f_58857_.isClientSide) {
            this.passIn(stack == null ? item.stack : stack, item.outgoingFace, item.incomingFace, item.rngSeed, item.timeInWorld);
        }
    }

    public void dropItem(ItemStack stack) {
        this.dropItem(stack, null, false);
    }

    public void dropItem(ItemStack stack, Direction facing, boolean playSound) {
        if (!this.f_58857_.isClientSide) {
            double posX = (double) this.f_58858_.m_123341_() + 0.5;
            double posY = (double) this.f_58858_.m_123342_() + 0.25;
            double posZ = (double) this.f_58858_.m_123343_() + 0.5;
            if (facing != null) {
                double shift = this.allowsFullConnection(PipeBlockEntity.ConnectionType.OPENING) ? 0.7 : 0.4;
                posX -= (double) facing.getStepX() * shift;
                posY -= (double) facing.getStepY() * (shift + 0.15);
                posZ -= (double) facing.getStepZ() * shift;
            }
            boolean shootOut = this.isPipeEnabled();
            float pitch = 1.0F;
            if (!shootOut) {
                pitch = 0.025F;
            }
            if (playSound) {
                if (PipesModule.doPipesWhoosh) {
                    if (isTheGoodDay()) {
                        this.f_58857_.playSound(null, posX, posY, posZ, QuarkSounds.BLOCK_PIPE_SHOOT_LENNY, SoundSource.BLOCKS, 1.0F, pitch);
                    } else {
                        this.f_58857_.playSound(null, posX, posY, posZ, QuarkSounds.BLOCK_PIPE_SHOOT, SoundSource.BLOCKS, 1.0F, pitch);
                    }
                }
                if (PipesModule.emitVibrations) {
                    this.m_58904_().m_220407_(GameEvent.PROJECTILE_SHOOT, this.m_58899_(), GameEvent.Context.of(this.m_58900_()));
                }
            }
            ItemEntity entity = new ItemEntity(this.f_58857_, posX, posY, posZ, stack);
            entity.setDefaultPickUpDelay();
            double velocityMod = 0.5;
            if (!shootOut) {
                velocityMod = 0.125;
            }
            if (facing != null) {
                double mx = (double) (-facing.getStepX()) * velocityMod;
                double my = (double) (-facing.getStepY()) * velocityMod;
                double mz = (double) (-facing.getStepZ()) * velocityMod;
                entity.m_20334_(mx, my, mz);
            }
            this.f_58857_.m_7967_(entity);
        }
    }

    public void dropAllItems() {
        for (PipeBlockEntity.PipeItem item : this.pipeItems) {
            this.dropItem(item.stack);
        }
        this.pipeItems.clear();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void readSharedNBT(CompoundTag cmp) {
        this.skipSync = true;
        super.readSharedNBT(cmp);
        this.skipSync = false;
        ListTag pipeItemList = cmp.getList("pipeItems", cmp.getId());
        this.pipeItems.clear();
        pipeItemList.forEach(listCmp -> {
            PipeBlockEntity.PipeItem item = PipeBlockEntity.PipeItem.readFromNBT((CompoundTag) listCmp);
            this.pipeItems.add(item);
        });
        if (cmp.contains("connections")) {
            byte[] c = cmp.getByteArray("connections");
            for (int i = 0; i < c.length; i++) {
                this.connectionsCache[i] = PipeBlockEntity.ConnectionType.values()[c[i]];
            }
        }
    }

    @Override
    public void writeSharedNBT(CompoundTag cmp) {
        super.writeSharedNBT(cmp);
        ListTag pipeItemList = new ListTag();
        for (PipeBlockEntity.PipeItem item : this.pipeItems) {
            CompoundTag listCmp = new CompoundTag();
            item.writeToNBT(listCmp);
            pipeItemList.add(listCmp);
        }
        cmp.put("pipeItems", pipeItemList);
        for (int i = 0; i < this.connectionsCache.length; i++) {
            if (this.connectionsCache[i] == null) {
                this.connectionsCache[i] = PipeBlockEntity.ConnectionType.NONE;
                this.convert = true;
            }
        }
        cmp.putByteArray("connections", Arrays.stream(this.connectionsCache).map(c -> (byte) c.ordinal()).toList());
    }

    protected boolean canFit(ItemStack stack, BlockPos pos, Direction face) {
        if (this.f_58857_.getBlockState(pos).m_60734_() instanceof WorldlyContainerHolder) {
            return MiscUtil.canPutIntoInv(stack, this.f_58857_, pos, null, face, false);
        } else {
            BlockEntity tile = this.f_58857_.getBlockEntity(pos);
            if (tile == null) {
                return false;
            } else {
                return tile instanceof PipeBlockEntity pipe ? pipe.isPipeEnabled() : MiscUtil.canPutIntoInv(stack, this.f_58857_, pos, tile, face, false);
            }
        }
    }

    protected boolean isPipeEnabled() {
        BlockState state = this.f_58857_.getBlockState(this.f_58858_);
        return state.m_204336_(PipesModule.pipesTag) && !this.f_58857_.m_276867_(this.f_58858_);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStackIn, @NotNull Direction direction) {
        return direction != null && index == direction.ordinal() && this.isPipeEnabled();
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemstack) {
        if (!itemstack.isEmpty()) {
            Direction side = Direction.values()[i];
            this.passIn(itemstack, side);
            if (!this.f_58857_.isClientSide && !this.skipSync) {
                this.sync();
            }
        }
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    protected boolean needsToSyncInventory() {
        return true;
    }

    @Override
    public void sync() {
        MiscUtil.syncTE(this);
    }

    public void refreshVisualConnections() {
        for (Direction direction : Direction.values()) {
            this.updateConnection(direction);
        }
    }

    public PipeBlockEntity.ConnectionType updateConnection(Direction facing) {
        PipeBlockEntity.ConnectionType c = computeConnectionTo(this.f_58857_, this.f_58858_, facing);
        this.connectionsCache[facing.ordinal()] = c;
        return c;
    }

    public PipeBlockEntity.ConnectionType getConnectionTo(Direction side) {
        PipeBlockEntity.ConnectionType c = this.connectionsCache[side.ordinal()];
        if (c == null) {
            c = this.updateConnection(side);
        }
        return c;
    }

    public static PipeBlockEntity.ConnectionType computeConnectionTo(BlockGetter world, BlockPos pos, Direction face) {
        return computeConnectionTo(world, pos, face, false);
    }

    private static PipeBlockEntity.ConnectionType computeConnectionTo(BlockGetter world, BlockPos pos, Direction face, boolean recursed) {
        BlockPos truePos = pos.relative(face);
        BlockState state = world.getBlockState(truePos);
        if (state.m_60734_() instanceof WorldlyContainerHolder) {
            return PipeBlockEntity.ConnectionType.TERMINAL;
        } else {
            BlockEntity tile = world.getBlockEntity(truePos);
            if (tile != null) {
                if (tile instanceof PipeBlockEntity) {
                    return PipeBlockEntity.ConnectionType.PIPE;
                }
                if (tile instanceof Container || tile.getCapability(ForgeCapabilities.ITEM_HANDLER, face.getOpposite()).isPresent()) {
                    return canHaveOffset(state, pos, world, face) ? PipeBlockEntity.ConnectionType.TERMINAL_OFFSET : PipeBlockEntity.ConnectionType.TERMINAL;
                }
            }
            if (!recursed) {
                PipeBlockEntity.ConnectionType other = computeConnectionTo(world, pos, face.getOpposite(), true);
                if (other.isSolid) {
                    Direction[] var8 = Direction.values();
                    int var9 = var8.length;
                    int var10 = 0;
                    while (true) {
                        if (var10 >= var9) {
                            return PipeBlockEntity.ConnectionType.OPENING;
                        }
                        Direction d = var8[var10];
                        if (d.getAxis() != face.getAxis()) {
                            other = computeConnectionTo(world, pos, d, true);
                            if (other.isSolid) {
                                break;
                            }
                        }
                        var10++;
                    }
                }
            }
            return PipeBlockEntity.ConnectionType.NONE;
        }
    }

    private static boolean canHaveOffset(BlockState state, BlockPos pos, BlockGetter world, Direction dir) {
        VoxelShape shape = state.m_60812_(world, pos);
        return dir.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? shape.max(dir.getAxis()) < 1.0 : shape.min(dir.getAxis()) > 0.0;
    }

    public static enum ConnectionType {

        NONE(false, false, false, 0.0), PIPE(true, true, false, 0.0), OPENING(false, true, true, -0.125, 0.1875), TERMINAL(true, true, true, 0.125), TERMINAL_OFFSET(true, true, true, 0.1875);

        public final boolean isSolid;

        public final boolean allowsItems;

        public final boolean isFlared;

        private final double flareShift;

        private final double fullFlareShift;

        private ConnectionType(boolean isSolid, boolean allowsItems, boolean isFlared, double flareShift, double fullFlareShift) {
            this.isSolid = isSolid;
            this.allowsItems = allowsItems;
            this.isFlared = isFlared;
            this.flareShift = flareShift;
            this.fullFlareShift = fullFlareShift;
        }

        private ConnectionType(boolean isSolid, boolean allowsItems, boolean isFlared, double flareShift) {
            this(isSolid, allowsItems, isFlared, flareShift, flareShift);
        }

        public double getFlareShift(PipeBlockEntity pipe) {
            return pipe.allowsFullConnection(this) ? this.fullFlareShift : this.flareShift;
        }
    }

    public static class PipeItem {

        private static final String TAG_TICKS = "ticksInPipe";

        private static final String TAG_INCOMING = "incomingFace";

        private static final String TAG_OUTGOING = "outgoingFace";

        private static final String TAG_BACKLOGGED = "backloggedFace";

        private static final String TAG_RNG_SEED = "rngSeed";

        private static final String TAG_TIME_IN_WORLD = "timeInWorld";

        private static final List<Direction> HORIZONTAL_SIDES_LIST = Arrays.asList(MiscUtil.HORIZONTALS);

        public final ItemStack stack;

        public int ticksInPipe;

        public final Direction incomingFace;

        public Direction outgoingFace;

        public Direction backloggedFace;

        public long rngSeed;

        public int timeInWorld = 0;

        public boolean valid = true;

        protected long lastTickUpdated = 0L;

        public PipeItem(ItemStack stack, Direction face, long rngSeed) {
            this.stack = stack;
            this.ticksInPipe = 0;
            this.incomingFace = this.outgoingFace = face;
            this.rngSeed = rngSeed;
        }

        protected boolean tick(PipeBlockEntity pipe) {
            long gameTime = pipe.f_58857_.getGameTime();
            if (this.lastTickUpdated != gameTime) {
                this.lastTickUpdated = gameTime;
                this.ticksInPipe++;
                this.timeInWorld++;
                if (this.ticksInPipe == PipesModule.effectivePipeSpeed / 2 - 1) {
                    this.outgoingFace = this.getTargetFace(pipe);
                }
                if (this.outgoingFace == null) {
                    this.valid = false;
                    return true;
                }
            }
            return this.ticksInPipe >= PipesModule.effectivePipeSpeed;
        }

        protected Direction getTargetFace(PipeBlockEntity pipe) {
            BlockPos pipePos = pipe.m_58899_();
            if (this.incomingFace != Direction.DOWN && this.backloggedFace != Direction.DOWN && pipe.canFit(this.stack, pipePos.relative(Direction.DOWN), Direction.UP)) {
                return Direction.DOWN;
            } else {
                Direction incomingOpposite = this.incomingFace;
                if (this.incomingFace.getAxis() != Direction.Axis.Y) {
                    incomingOpposite = this.incomingFace.getOpposite();
                    if (incomingOpposite != this.backloggedFace && pipe.canFit(this.stack, pipePos.relative(incomingOpposite), this.incomingFace)) {
                        return incomingOpposite;
                    }
                }
                List<Direction> sides = new ArrayList(HORIZONTAL_SIDES_LIST);
                sides.remove(this.incomingFace);
                sides.remove(incomingOpposite);
                Random rng = new Random(this.rngSeed);
                this.rngSeed = rng.nextLong();
                Collections.shuffle(sides, rng);
                for (Direction side : sides) {
                    if (side != this.backloggedFace && pipe.canFit(this.stack, pipePos.relative(side), side.getOpposite())) {
                        return side;
                    }
                }
                if (this.incomingFace != Direction.UP && this.backloggedFace != Direction.UP && pipe.canFit(this.stack, pipePos.relative(Direction.UP), Direction.DOWN)) {
                    return Direction.UP;
                } else {
                    return this.backloggedFace != null ? this.backloggedFace : null;
                }
            }
        }

        public float getTimeFract(float partial) {
            return ((float) this.ticksInPipe + partial) / (float) PipesModule.effectivePipeSpeed;
        }

        public void writeToNBT(CompoundTag cmp) {
            this.stack.save(cmp);
            cmp.putInt("ticksInPipe", this.ticksInPipe);
            cmp.putInt("incomingFace", this.incomingFace.ordinal());
            cmp.putInt("outgoingFace", this.outgoingFace.ordinal());
            cmp.putInt("backloggedFace", this.backloggedFace != null ? this.backloggedFace.ordinal() : -1);
            cmp.putLong("rngSeed", this.rngSeed);
            cmp.putInt("timeInWorld", this.timeInWorld);
        }

        public static PipeBlockEntity.PipeItem readFromNBT(CompoundTag cmp) {
            ItemStack stack = ItemStack.of(cmp);
            Direction inFace = Direction.values()[cmp.getInt("incomingFace")];
            long rngSeed = cmp.getLong("rngSeed");
            PipeBlockEntity.PipeItem item = new PipeBlockEntity.PipeItem(stack, inFace, rngSeed);
            item.ticksInPipe = cmp.getInt("ticksInPipe");
            item.outgoingFace = Direction.values()[cmp.getInt("outgoingFace")];
            item.timeInWorld = cmp.getInt("timeInWorld");
            int backloggedId = cmp.getInt("backloggedFace");
            item.backloggedFace = backloggedId == -1 ? null : Direction.values()[backloggedId];
            return item;
        }
    }
}
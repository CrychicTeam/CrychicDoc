package com.rekindled.embers.blockentity;

import com.rekindled.embers.api.block.IPipeConnection;
import com.rekindled.embers.block.PipeBlockBase;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.Misc;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.joml.Vector3f;

public class PipeBlockEntityBase extends BlockEntity {

    public static final int PRIORITY_BLOCK = 0;

    public static final int PRIORITY_PIPE = 0;

    static Random random = new Random();

    public PipeBlockEntityBase.PipeConnection[] connections = new PipeBlockEntityBase.PipeConnection[] { PipeBlockEntityBase.PipeConnection.NONE, PipeBlockEntityBase.PipeConnection.NONE, PipeBlockEntityBase.PipeConnection.NONE, PipeBlockEntityBase.PipeConnection.NONE, PipeBlockEntityBase.PipeConnection.NONE, PipeBlockEntityBase.PipeConnection.NONE };

    public boolean[] from = new boolean[Direction.values().length];

    public boolean clogged = false;

    public Direction lastTransfer;

    public int ticksExisted;

    public int lastRobin;

    public boolean loaded = false;

    public boolean saveConnections = true;

    public boolean syncConnections = true;

    public boolean syncCloggedFlag = true;

    public boolean syncTransfer = true;

    public static final ModelProperty<int[]> DATA_TYPE = new ModelProperty<>();

    public PipeBlockEntityBase(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public void setFrom(Direction facing, boolean flag) {
        this.from[facing.get3DDataValue()] = flag;
    }

    public void resetFrom() {
        for (Direction facing : Direction.values()) {
            this.setFrom(facing, false);
        }
    }

    protected boolean isFrom(Direction facing) {
        return this.from[facing.get3DDataValue()];
    }

    protected boolean isAnySideUnclogged() {
        for (Direction facing : Direction.values()) {
            if (this.getConnection(facing).transfer) {
                BlockEntity tile = this.f_58857_.getBlockEntity(this.f_58858_.relative(facing));
                if (tile instanceof PipeBlockEntityBase && !((PipeBlockEntityBase) tile).clogged) {
                    return true;
                }
            }
        }
        return false;
    }

    public void initConnections() {
        Block block = this.f_58857_.getBlockState(this.f_58858_).m_60734_();
        for (Direction direction : Direction.values()) {
            if (block instanceof PipeBlockBase) {
                PipeBlockBase pipeBlock = (PipeBlockBase) block;
                BlockState facingState = this.f_58857_.getBlockState(this.f_58858_.relative(direction));
                BlockEntity facingBE = this.f_58857_.getBlockEntity(this.f_58858_.relative(direction));
                if (!(facingBE instanceof PipeBlockEntityBase) || ((PipeBlockEntityBase) facingBE).getConnection(direction.getOpposite()) != PipeBlockEntityBase.PipeConnection.DISABLED) {
                    if (facingState.m_204336_(pipeBlock.getConnectionTag())) {
                        if (facingBE instanceof PipeBlockEntityBase && ((PipeBlockEntityBase) facingBE).getConnection(direction.getOpposite()) == PipeBlockEntityBase.PipeConnection.DISABLED) {
                            this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.DISABLED;
                        } else {
                            this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.PIPE;
                        }
                    } else if (pipeBlock.connected(direction, facingState)) {
                        this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.LEVER;
                    } else if (pipeBlock.connectToTile(facingBE, direction)) {
                        if (facingState.m_60734_() instanceof IPipeConnection) {
                            this.connections[direction.get3DDataValue()] = ((IPipeConnection) facingState.m_60734_()).getPipeConnection(facingState, direction.getOpposite());
                        } else {
                            this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.END;
                        }
                    } else {
                        this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.NONE;
                    }
                }
            }
        }
        this.syncConnections = true;
        Misc.sendToTrackingPlayers(this.f_58857_, this.f_58858_, this.getUpdatePacket());
        this.loaded = true;
        this.setChanged();
        this.f_58857_.getChunkAt(this.f_58858_).m_8092_(true);
        this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, block);
    }

    public ModelData getModelData() {
        int[] data = new int[] { this.connections[0].visualIndex, this.connections[1].visualIndex, this.connections[2].visualIndex, this.connections[3].visualIndex, this.connections[4].visualIndex, this.connections[5].visualIndex };
        return ModelData.builder().with(DATA_TYPE, data).build();
    }

    public void setConnection(Direction direction, PipeBlockEntityBase.PipeConnection connection) {
        this.connections[direction.get3DDataValue()] = connection;
        this.syncConnections = true;
        this.requestModelDataUpdate();
        this.setChanged();
    }

    public PipeBlockEntityBase.PipeConnection getConnection(Direction direction) {
        return this.connections[direction.get3DDataValue()];
    }

    public void setConnections(PipeBlockEntityBase.PipeConnection[] connections) {
        this.connections = connections;
        this.syncConnections = true;
        this.requestModelDataUpdate();
        this.setChanged();
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick(Level level, BlockPos pos, BlockState state, PipeBlockEntityBase blockEntity) {
        if (blockEntity.lastTransfer != null && Misc.isWearingLens(Minecraft.getInstance().player)) {
            float vx = (float) (blockEntity.lastTransfer.getStepX() / 1);
            float vy = (float) (blockEntity.lastTransfer.getStepY() / 1);
            float vz = (float) (blockEntity.lastTransfer.getStepZ() / 1);
            double x = (double) ((float) pos.m_123341_() + 0.4F + random.nextFloat() * 0.2F);
            double y = (double) ((float) pos.m_123342_() + 0.4F + random.nextFloat() * 0.2F);
            double z = (double) ((float) pos.m_123343_() + 0.4F + random.nextFloat() * 0.2F);
            float r = blockEntity.clogged ? 255.0F : 16.0F;
            float g = blockEntity.clogged ? 16.0F : 255.0F;
            float b = 16.0F;
            for (int i = 0; i < 3; i++) {
                level.addParticle(new GlowParticleOptions(new Vector3f(r / 255.0F, g / 255.0F, b / 255.0F), new Vec3((double) vx, (double) vy, (double) vz), 2.0F), x, y, z, (double) vx, (double) vy, (double) vz);
            }
        }
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        if (this.f_58857_.isClientSide()) {
            this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 3);
        }
    }

    protected void resetSync() {
        this.syncCloggedFlag = false;
        this.syncTransfer = false;
        this.syncConnections = false;
    }

    protected boolean requiresSync() {
        return this.syncCloggedFlag || this.syncTransfer || this.syncConnections || !this.loaded;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("clogged")) {
            this.clogged = nbt.getBoolean("clogged");
        }
        if (nbt.contains("lastTransfer")) {
            this.lastTransfer = Misc.readNullableFacing(nbt.getInt("lastTransfer"));
        }
        for (Direction facing : Direction.values()) {
            if (nbt.contains("from" + facing.get3DDataValue())) {
                this.from[facing.get3DDataValue()] = nbt.getBoolean("from" + facing.get3DDataValue());
            }
        }
        if (nbt.contains("lastRobin")) {
            this.lastRobin = nbt.getInt("lastRobin");
        }
        this.loadConnections(nbt);
        this.loaded = true;
    }

    public void loadConnections(CompoundTag nbt) {
        for (Direction direction : Direction.values()) {
            if (nbt.contains("connection" + direction.get3DDataValue())) {
                this.connections[direction.get3DDataValue()] = PipeBlockEntityBase.PipeConnection.values()[nbt.getInt("connection" + direction.get3DDataValue())];
            }
        }
        this.requestModelDataUpdate();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (this.saveConnections) {
            this.writeConnections(nbt);
        }
        this.writeCloggedFlag(nbt);
        this.writeLastTransfer(nbt);
        for (Direction facing : Direction.values()) {
            nbt.putBoolean("from" + facing.get3DDataValue(), this.from[facing.get3DDataValue()]);
        }
        nbt.putInt("lastRobin", this.lastRobin);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        if (this.saveConnections) {
            this.writeConnections(nbt);
        }
        if (this.syncCloggedFlag) {
            this.writeCloggedFlag(nbt);
        }
        if (this.syncTransfer) {
            this.writeLastTransfer(nbt);
        }
        return nbt;
    }

    public void writeConnections(CompoundTag nbt) {
        for (Direction direction : Direction.values()) {
            nbt.putInt("connection" + direction.get3DDataValue(), this.getConnection(direction).index);
        }
    }

    public void writeCloggedFlag(CompoundTag nbt) {
        nbt.putBoolean("clogged", this.clogged);
    }

    public void writeLastTransfer(CompoundTag nbt) {
        nbt.putInt("lastTransfer", Misc.writeNullableFacing(this.lastTransfer));
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        if (this.requiresSync()) {
            Packet<ClientGamePacketListener> packet = ClientboundBlockEntityDataPacket.create(this);
            this.resetSync();
            return packet;
        } else {
            return null;
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    public static enum PipeConnection implements StringRepresentable {

        NONE("none", 0, 0, false), DISABLED("disabled", 1, 0, false), PIPE("pipe", 2, 1, true), END("end", 3, 2, true), LEVER("lever", 4, 2, false);

        private final String name;

        public final int index;

        public final int visualIndex;

        public final boolean transfer;

        public static final PipeBlockEntityBase.PipeConnection[] visualValues = new PipeBlockEntityBase.PipeConnection[] { NONE, PIPE, END };

        private PipeConnection(String name, int index, int visualIndex, boolean transfer) {
            this.name = name;
            this.index = index;
            this.visualIndex = visualIndex;
            this.transfer = transfer;
        }

        public static PipeBlockEntityBase.PipeConnection[] visual() {
            return visualValues;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
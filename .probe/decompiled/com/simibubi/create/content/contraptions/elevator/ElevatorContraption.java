package com.simibubi.create.content.contraptions.elevator;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsMovement;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.pulley.PulleyContraption;
import com.simibubi.create.content.redstone.contact.RedstoneContactBlock;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.IntAttached;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ElevatorContraption extends PulleyContraption {

    protected ElevatorColumn.ColumnCoords column;

    protected int contactYOffset;

    public boolean arrived;

    private int namesListVersion = -1;

    public List<IntAttached<Couple<String>>> namesList = ImmutableList.of();

    public int clientYTarget;

    public int maxContactY;

    public int minContactY;

    private int contacts;

    public ElevatorContraption() {
    }

    public ElevatorContraption(int initialOffset) {
        super(initialOffset);
    }

    @Override
    public void tickStorage(AbstractContraptionEntity entity) {
        super.tickStorage(entity);
        if (entity.f_19797_ % 10 == 0) {
            ElevatorColumn.ColumnCoords coords = this.getGlobalColumn();
            ElevatorColumn column = ElevatorColumn.get(entity.m_9236_(), coords);
            if (column != null) {
                if (column.namesListVersion != this.namesListVersion) {
                    this.namesList = column.compileNamesList();
                    this.namesListVersion = column.namesListVersion;
                    AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new ElevatorFloorListPacket(entity, this.namesList));
                }
            }
        }
    }

    @Override
    protected void disableActorOnStart(MovementContext context) {
    }

    public ElevatorColumn.ColumnCoords getGlobalColumn() {
        return this.column.relative(this.anchor);
    }

    public Integer getCurrentTargetY(Level level) {
        ElevatorColumn.ColumnCoords coords = this.getGlobalColumn();
        ElevatorColumn column = ElevatorColumn.get(level, coords);
        if (column == null) {
            return null;
        } else {
            int targetedYLevel = column.targetedYLevel;
            return this.isTargetUnreachable(targetedYLevel) ? null : targetedYLevel;
        }
    }

    public boolean isTargetUnreachable(int contactY) {
        return contactY < this.minContactY || contactY > this.maxContactY;
    }

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        if (!this.searchMovedStructure(world, pos, null)) {
            return false;
        } else if (this.blocks.size() <= 0) {
            return false;
        } else if (this.contacts == 0) {
            throw new AssemblyException(Lang.translateDirect("gui.assembly.exception.no_contacts"));
        } else if (this.contacts > 1) {
            throw new AssemblyException(Lang.translateDirect("gui.assembly.exception.too_many_contacts"));
        } else {
            ElevatorColumn column = ElevatorColumn.get(world, this.getGlobalColumn());
            if (column != null && column.isActive()) {
                throw new AssemblyException(Lang.translateDirect("gui.assembly.exception.column_conflict"));
            } else {
                this.startMoving(world);
                return true;
            }
        }
    }

    @Override
    protected Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (!AllBlocks.REDSTONE_CONTACT.has(blockState)) {
            return super.capture(world, pos);
        } else {
            Direction facing = (Direction) blockState.m_61143_(RedstoneContactBlock.f_52588_);
            if (facing.getAxis() == Direction.Axis.Y) {
                return super.capture(world, pos);
            } else {
                this.contacts++;
                BlockPos local = this.toLocalPos(pos.relative(facing));
                this.column = new ElevatorColumn.ColumnCoords(local.m_123341_(), local.m_123343_(), facing.getOpposite());
                this.contactYOffset = local.m_123342_();
                return super.capture(world, pos);
            }
        }
    }

    public int getContactYOffset() {
        return this.contactYOffset;
    }

    public void broadcastFloorData(Level level, BlockPos contactPos) {
        ElevatorColumn column = ElevatorColumn.get(level, this.getGlobalColumn());
        if (this.world.m_7702_(contactPos) instanceof ElevatorContactBlockEntity ecbe) {
            if (column != null) {
                column.floorReached(level, ecbe.shortName);
            }
        }
    }

    @Override
    public CompoundTag writeNBT(boolean spawnPacket) {
        CompoundTag tag = super.writeNBT(spawnPacket);
        tag.putBoolean("Arrived", this.arrived);
        tag.put("Column", this.column.write());
        tag.putInt("ContactY", this.contactYOffset);
        tag.putInt("MaxContactY", this.maxContactY);
        tag.putInt("MinContactY", this.minContactY);
        return tag;
    }

    @Override
    public void readNBT(Level world, CompoundTag nbt, boolean spawnData) {
        this.arrived = nbt.getBoolean("Arrived");
        this.column = ElevatorColumn.ColumnCoords.read(nbt.getCompound("Column"));
        this.contactYOffset = nbt.getInt("ContactY");
        this.maxContactY = nbt.getInt("MaxContactY");
        this.minContactY = nbt.getInt("MinContactY");
        super.readNBT(world, nbt, spawnData);
    }

    @Override
    public ContraptionType getType() {
        return ContraptionType.ELEVATOR;
    }

    public void setClientYTarget(int clientYTarget) {
        if (this.clientYTarget != clientYTarget) {
            this.clientYTarget = clientYTarget;
            this.syncControlDisplays();
        }
    }

    public void syncControlDisplays() {
        if (!this.namesList.isEmpty()) {
            for (int i = 0; i < this.namesList.size(); i++) {
                if (((IntAttached) this.namesList.get(i)).getFirst() == this.clientYTarget) {
                    this.setAllControlsToFloor(i);
                }
            }
        }
    }

    public void setAllControlsToFloor(int floorIndex) {
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : this.actors) {
            if (pair.right != null && ((MovementContext) pair.right).temporaryData instanceof ContraptionControlsMovement.ElevatorFloorSelection efs) {
                efs.currentIndex = floorIndex;
            }
        }
    }
}
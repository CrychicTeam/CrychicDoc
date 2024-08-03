package com.simibubi.create.content.contraptions.mounted;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.contraptions.render.NonStationaryLighter;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Iterator;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.commons.lang3.tuple.Pair;

public class MountedContraption extends Contraption {

    public CartAssemblerBlockEntity.CartMovementMode rotationMode;

    public AbstractMinecart connectedCart;

    public MountedContraption() {
        this(CartAssemblerBlockEntity.CartMovementMode.ROTATE);
    }

    public MountedContraption(CartAssemblerBlockEntity.CartMovementMode mode) {
        this.rotationMode = mode;
    }

    @Override
    public ContraptionType getType() {
        return ContraptionType.MOUNTED;
    }

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        BlockState state = world.getBlockState(pos);
        if (!state.m_61138_(CartAssemblerBlock.RAIL_SHAPE)) {
            return false;
        } else if (!this.searchMovedStructure(world, pos, null)) {
            return false;
        } else {
            Direction.Axis axis = state.m_61143_(CartAssemblerBlock.RAIL_SHAPE) == RailShape.EAST_WEST ? Direction.Axis.X : Direction.Axis.Z;
            this.addBlock(pos, Pair.of(new StructureTemplate.StructureBlockInfo(pos, (BlockState) AllBlocks.MINECART_ANCHOR.getDefaultState().m_61124_(BlockStateProperties.HORIZONTAL_AXIS, axis), null), null));
            return this.blocks.size() != 1;
        }
    }

    @Override
    protected boolean addToInitialFrontier(Level world, BlockPos pos, Direction direction, Queue<BlockPos> frontier) {
        frontier.clear();
        frontier.add(pos.above());
        return true;
    }

    @Override
    protected Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture(Level world, BlockPos pos) {
        Pair<StructureTemplate.StructureBlockInfo, BlockEntity> pair = super.capture(world, pos);
        StructureTemplate.StructureBlockInfo capture = (StructureTemplate.StructureBlockInfo) pair.getKey();
        if (!AllBlocks.CART_ASSEMBLER.has(capture.state())) {
            return pair;
        } else {
            Pair<StructureTemplate.StructureBlockInfo, BlockEntity> anchorSwap = Pair.of(new StructureTemplate.StructureBlockInfo(pos, CartAssemblerBlock.createAnchor(capture.state()), null), (BlockEntity) pair.getValue());
            if (!pos.equals(this.anchor) && this.connectedCart == null) {
                for (Direction.Axis axis : Iterate.axes) {
                    if (!axis.isVertical() && VecHelper.onSameAxis(this.anchor, pos, axis)) {
                        for (AbstractMinecart abstractMinecartEntity : world.m_45976_(AbstractMinecart.class, new AABB(pos))) {
                            if (!CartAssemblerBlock.canAssembleTo(abstractMinecartEntity)) {
                                break;
                            }
                            this.connectedCart = abstractMinecartEntity;
                            this.connectedCart.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F));
                        }
                    }
                }
                return anchorSwap;
            } else {
                return anchorSwap;
            }
        }
    }

    @Override
    protected boolean movementAllowed(BlockState state, Level world, BlockPos pos) {
        return !pos.equals(this.anchor) && AllBlocks.CART_ASSEMBLER.has(state) ? this.testSecondaryCartAssembler(world, state, pos) : super.movementAllowed(state, world, pos);
    }

    protected boolean testSecondaryCartAssembler(Level world, BlockState state, BlockPos pos) {
        for (Direction.Axis axis : Iterate.axes) {
            if (!axis.isVertical() && VecHelper.onSameAxis(this.anchor, pos, axis)) {
                Iterator var8 = world.m_45976_(AbstractMinecart.class, new AABB(pos)).iterator();
                if (var8.hasNext()) {
                    AbstractMinecart abstractMinecartEntity = (AbstractMinecart) var8.next();
                    if (CartAssemblerBlock.canAssembleTo(abstractMinecartEntity)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public CompoundTag writeNBT(boolean spawnPacket) {
        CompoundTag tag = super.writeNBT(spawnPacket);
        NBTHelper.writeEnum(tag, "RotationMode", this.rotationMode);
        return tag;
    }

    @Override
    public void readNBT(Level world, CompoundTag nbt, boolean spawnData) {
        this.rotationMode = NBTHelper.readEnum(nbt, "RotationMode", CartAssemblerBlockEntity.CartMovementMode.class);
        super.readNBT(world, nbt, spawnData);
    }

    @Override
    protected boolean customBlockPlacement(LevelAccessor world, BlockPos pos, BlockState state) {
        return AllBlocks.MINECART_ANCHOR.has(state);
    }

    @Override
    protected boolean customBlockRemoval(LevelAccessor world, BlockPos pos, BlockState state) {
        return AllBlocks.MINECART_ANCHOR.has(state);
    }

    @Override
    public boolean canBeStabilized(Direction facing, BlockPos localPos) {
        return true;
    }

    public void addExtraInventories(Entity cart) {
        if (cart instanceof Container container) {
            this.storage.attachExternal(new Contraption.ContraptionInvWrapper(true, new InvWrapper(container)));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ContraptionLighter<?> makeLighter() {
        return new NonStationaryLighter<>(this);
    }
}
package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.ITransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.redstone.RoseQuartzLampBlock;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockEntityItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.IPartialSafeNBT;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.items.ItemHandlerHelper;

public class CopycatBlockEntity extends SmartBlockEntity implements ISpecialBlockEntityItemRequirement, ITransformableBlockEntity, IPartialSafeNBT {

    private BlockState material = AllBlocks.COPYCAT_BASE.getDefaultState();

    private ItemStack consumedItem = ItemStack.EMPTY;

    public CopycatBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public BlockState getMaterial() {
        return this.material;
    }

    public boolean hasCustomMaterial() {
        return !AllBlocks.COPYCAT_BASE.has(this.getMaterial());
    }

    public void setMaterial(BlockState blockState) {
        BlockState wrapperState = this.m_58900_();
        if (!this.material.m_60713_(blockState.m_60734_())) {
            for (Direction side : Iterate.directions) {
                BlockPos neighbour = this.f_58858_.relative(side);
                BlockState neighbourState = this.f_58857_.getBlockState(neighbour);
                if (neighbourState == wrapperState && this.f_58857_.getBlockEntity(neighbour) instanceof CopycatBlockEntity cbe) {
                    BlockState otherMaterial = cbe.getMaterial();
                    if (otherMaterial.m_60713_(blockState.m_60734_())) {
                        blockState = otherMaterial;
                        break;
                    }
                }
            }
        }
        this.material = blockState;
        if (!this.f_58857_.isClientSide()) {
            this.notifyUpdate();
        } else {
            this.redraw();
        }
    }

    public boolean cycleMaterial() {
        if (this.material.m_61138_(TrapDoorBlock.HALF) && (Boolean) this.material.m_61145_(TrapDoorBlock.OPEN).orElse(false)) {
            this.setMaterial((BlockState) this.material.m_61122_(TrapDoorBlock.HALF));
        } else if (this.material.m_61138_(BlockStateProperties.FACING)) {
            this.setMaterial((BlockState) this.material.m_61122_(BlockStateProperties.FACING));
        } else if (this.material.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
            this.setMaterial((BlockState) this.material.m_61124_(BlockStateProperties.HORIZONTAL_FACING, ((Direction) this.material.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).getClockWise()));
        } else if (this.material.m_61138_(BlockStateProperties.AXIS)) {
            this.setMaterial((BlockState) this.material.m_61122_(BlockStateProperties.AXIS));
        } else if (this.material.m_61138_(BlockStateProperties.HORIZONTAL_AXIS)) {
            this.setMaterial((BlockState) this.material.m_61122_(BlockStateProperties.HORIZONTAL_AXIS));
        } else if (this.material.m_61138_(BlockStateProperties.LIT)) {
            this.setMaterial((BlockState) this.material.m_61122_(BlockStateProperties.LIT));
        } else {
            if (!this.material.m_61138_(RoseQuartzLampBlock.POWERING)) {
                return false;
            }
            this.setMaterial((BlockState) this.material.m_61122_(RoseQuartzLampBlock.POWERING));
        }
        return true;
    }

    public ItemStack getConsumedItem() {
        return this.consumedItem;
    }

    public void setConsumedItem(ItemStack stack) {
        this.consumedItem = ItemHandlerHelper.copyStackWithSize(stack, 1);
        this.m_6596_();
    }

    private void redraw() {
        if (!this.isVirtual()) {
            this.requestModelDataUpdate();
        }
        if (this.m_58898_()) {
            this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 16);
            this.f_58857_.m_7726_().getLightEngine().checkBlock(this.f_58858_);
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state) {
        return this.consumedItem.isEmpty() ? ItemRequirement.NONE : new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, this.consumedItem);
    }

    @Override
    public void transform(StructureTransform transform) {
        this.material = transform.apply(this.material);
        this.notifyUpdate();
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        this.consumedItem = ItemStack.of(tag.getCompound("Item"));
        BlockState prevMaterial = this.material;
        if (!tag.contains("Material")) {
            this.consumedItem = ItemStack.EMPTY;
        } else {
            this.material = NbtUtils.readBlockState(this.blockHolderGetter(), tag.getCompound("Material"));
            if (this.material != null && !clientPacket) {
                BlockState blockState = this.m_58900_();
                if (blockState == null) {
                    return;
                }
                if (!(blockState.m_60734_() instanceof CopycatBlock cb)) {
                    return;
                }
                BlockState acceptedBlockState = cb.getAcceptedBlockState(this.f_58857_, this.f_58858_, this.consumedItem, null);
                if (acceptedBlockState != null && this.material.m_60713_(acceptedBlockState.m_60734_())) {
                    return;
                }
                this.consumedItem = ItemStack.EMPTY;
                this.material = AllBlocks.COPYCAT_BASE.getDefaultState();
            }
            if (clientPacket && prevMaterial != this.material) {
                this.redraw();
            }
        }
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        super.writeSafe(tag);
        ItemStack stackWithoutNBT = this.consumedItem.copy();
        stackWithoutNBT.setTag(null);
        this.write(tag, stackWithoutNBT, this.material);
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        this.write(tag, this.consumedItem, this.material);
    }

    protected void write(CompoundTag tag, ItemStack stack, BlockState material) {
        tag.put("Item", stack.serializeNBT());
        tag.put("Material", NbtUtils.writeBlockState(material));
    }

    public ModelData getModelData() {
        return ModelData.builder().with(CopycatModel.MATERIAL_PROPERTY, this.material).build();
    }
}
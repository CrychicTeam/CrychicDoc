package com.simibubi.create.content.contraptions.bearing;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import com.simibubi.create.content.decoration.copycat.CopycatBlockEntity;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

public class BearingContraption extends Contraption {

    protected int sailBlocks;

    protected Direction facing;

    private boolean isWindmill;

    public BearingContraption() {
    }

    public BearingContraption(boolean isWindmill, Direction facing) {
        this.isWindmill = isWindmill;
        this.facing = facing;
    }

    @Override
    public boolean assemble(Level world, BlockPos pos) throws AssemblyException {
        BlockPos offset = pos.relative(this.facing);
        if (!this.searchMovedStructure(world, offset, null)) {
            return false;
        } else {
            this.startMoving(world);
            this.expandBoundsAroundAxis(this.facing.getAxis());
            if (this.isWindmill && this.sailBlocks < AllConfigs.server().kinetics.minimumWindmillSails.get()) {
                throw AssemblyException.notEnoughSails(this.sailBlocks);
            } else {
                return !this.blocks.isEmpty();
            }
        }
    }

    @Override
    public ContraptionType getType() {
        return ContraptionType.BEARING;
    }

    @Override
    protected boolean isAnchoringBlockAt(BlockPos pos) {
        return pos.equals(this.anchor.relative(this.facing.getOpposite()));
    }

    @Override
    public void addBlock(BlockPos pos, Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture) {
        BlockPos localPos = pos.subtract(this.anchor);
        if (!this.getBlocks().containsKey(localPos) && AllTags.AllBlockTags.WINDMILL_SAILS.matches(this.getSailBlock(capture))) {
            this.sailBlocks++;
        }
        super.addBlock(pos, capture);
    }

    private BlockState getSailBlock(Pair<StructureTemplate.StructureBlockInfo, BlockEntity> capture) {
        BlockState state = ((StructureTemplate.StructureBlockInfo) capture.getKey()).state();
        return AllBlocks.COPYCAT_PANEL.has(state) && capture.getRight() instanceof CopycatBlockEntity cbe ? cbe.getMaterial() : state;
    }

    @Override
    public CompoundTag writeNBT(boolean spawnPacket) {
        CompoundTag tag = super.writeNBT(spawnPacket);
        tag.putInt("Sails", this.sailBlocks);
        tag.putInt("Facing", this.facing.get3DDataValue());
        return tag;
    }

    @Override
    public void readNBT(Level world, CompoundTag tag, boolean spawnData) {
        this.sailBlocks = tag.getInt("Sails");
        this.facing = Direction.from3DDataValue(tag.getInt("Facing"));
        super.readNBT(world, tag, spawnData);
    }

    public int getSailBlocks() {
        return this.sailBlocks;
    }

    public Direction getFacing() {
        return this.facing;
    }

    @Override
    public boolean canBeStabilized(Direction facing, BlockPos localPos) {
        return facing.getOpposite() == this.facing && BlockPos.ZERO.equals(localPos) ? false : facing.getAxis() == this.facing.getAxis();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ContraptionLighter<?> makeLighter() {
        return new AnchoredLighter(this);
    }
}
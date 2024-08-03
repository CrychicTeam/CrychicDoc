package com.simibubi.create.content.equipment.symmetryWand.mirror;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.Lang;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class SymmetryMirror {

    public static final String EMPTY = "empty";

    public static final String PLANE = "plane";

    public static final String CROSS_PLANE = "cross_plane";

    public static final String TRIPLE_PLANE = "triple_plane";

    protected Vec3 position;

    protected StringRepresentable orientation;

    protected int orientationIndex;

    public boolean enable;

    private static final String $ORIENTATION = "direction";

    private static final String $POSITION = "pos";

    private static final String $TYPE = "type";

    private static final String $ENABLE = "enable";

    public SymmetryMirror(Vec3 pos) {
        this.position = pos;
        this.enable = true;
        this.orientationIndex = 0;
    }

    public static List<Component> getMirrors() {
        return ImmutableList.of(Lang.translateDirect("symmetry.mirror.plane"), Lang.translateDirect("symmetry.mirror.doublePlane"), Lang.translateDirect("symmetry.mirror.triplePlane"));
    }

    public StringRepresentable getOrientation() {
        return this.orientation;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public int getOrientationIndex() {
        return this.orientationIndex;
    }

    public void rotate(boolean forward) {
        this.orientationIndex += forward ? 1 : -1;
        this.setOrientation();
    }

    public void process(Map<BlockPos, BlockState> blocks) {
        Map<BlockPos, BlockState> result = new HashMap();
        for (BlockPos pos : blocks.keySet()) {
            result.putAll(this.process(pos, (BlockState) blocks.get(pos)));
        }
        blocks.putAll(result);
    }

    public abstract Map<BlockPos, BlockState> process(BlockPos var1, BlockState var2);

    protected abstract void setOrientation();

    public abstract void setOrientation(int var1);

    public abstract String typeName();

    @OnlyIn(Dist.CLIENT)
    public abstract PartialModel getModel();

    public void applyModelTransform(PoseStack ms) {
    }

    public CompoundTag writeToNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("direction", this.orientationIndex);
        ListTag floatList = new ListTag();
        floatList.add(FloatTag.valueOf((float) this.position.x));
        floatList.add(FloatTag.valueOf((float) this.position.y));
        floatList.add(FloatTag.valueOf((float) this.position.z));
        nbt.put("pos", floatList);
        nbt.putString("type", this.typeName());
        nbt.putBoolean("enable", this.enable);
        return nbt;
    }

    public static SymmetryMirror fromNBT(CompoundTag nbt) {
        ListTag floatList = nbt.getList("pos", 5);
        Vec3 pos = new Vec3((double) floatList.getFloat(0), (double) floatList.getFloat(1), (double) floatList.getFloat(2));
        String var4 = nbt.getString("type");
        SymmetryMirror element = switch(var4) {
            case "plane" ->
                new PlaneMirror(pos);
            case "cross_plane" ->
                new CrossPlaneMirror(pos);
            case "triple_plane" ->
                new TriplePlaneMirror(pos);
            default ->
                new EmptyMirror(pos);
        };
        element.setOrientation(nbt.getInt("direction"));
        element.enable = nbt.getBoolean("enable");
        return element;
    }

    protected Vec3 getDiff(BlockPos position) {
        return this.position.scale(-1.0).add((double) position.m_123341_(), (double) position.m_123342_(), (double) position.m_123343_());
    }

    protected BlockPos getIDiff(BlockPos position) {
        Vec3 diff = this.getDiff(position);
        return new BlockPos((int) diff.x, (int) diff.y, (int) diff.z);
    }

    protected BlockState flipX(BlockState in) {
        return in.m_60715_(Mirror.FRONT_BACK);
    }

    protected BlockState flipY(BlockState in) {
        for (Property<?> property : in.m_61147_()) {
            if (property == BlockStateProperties.HALF) {
                return (BlockState) in.m_61122_(property);
            }
            if (property instanceof DirectionProperty) {
                if (in.m_61143_(property) == Direction.DOWN) {
                    return (BlockState) in.m_61124_(property, Direction.UP);
                }
                if (in.m_61143_(property) == Direction.UP) {
                    return (BlockState) in.m_61124_(property, Direction.DOWN);
                }
            }
        }
        return in;
    }

    protected BlockState flipZ(BlockState in) {
        return in.m_60715_(Mirror.LEFT_RIGHT);
    }

    protected BlockState flipD1(BlockState in) {
        return in.m_60717_(Rotation.COUNTERCLOCKWISE_90).m_60715_(Mirror.FRONT_BACK);
    }

    protected BlockState flipD2(BlockState in) {
        return in.m_60717_(Rotation.COUNTERCLOCKWISE_90).m_60715_(Mirror.LEFT_RIGHT);
    }

    protected BlockPos flipX(BlockPos position) {
        BlockPos diff = this.getIDiff(position);
        return new BlockPos(position.m_123341_() - 2 * diff.m_123341_(), position.m_123342_(), position.m_123343_());
    }

    protected BlockPos flipY(BlockPos position) {
        BlockPos diff = this.getIDiff(position);
        return new BlockPos(position.m_123341_(), position.m_123342_() - 2 * diff.m_123342_(), position.m_123343_());
    }

    protected BlockPos flipZ(BlockPos position) {
        BlockPos diff = this.getIDiff(position);
        return new BlockPos(position.m_123341_(), position.m_123342_(), position.m_123343_() - 2 * diff.m_123343_());
    }

    protected BlockPos flipD2(BlockPos position) {
        BlockPos diff = this.getIDiff(position);
        return new BlockPos(position.m_123341_() - diff.m_123341_() + diff.m_123343_(), position.m_123342_(), position.m_123343_() - diff.m_123343_() + diff.m_123341_());
    }

    protected BlockPos flipD1(BlockPos position) {
        BlockPos diff = this.getIDiff(position);
        return new BlockPos(position.m_123341_() - diff.m_123341_() - diff.m_123343_(), position.m_123342_(), position.m_123343_() - diff.m_123343_() - diff.m_123341_());
    }

    public void setPosition(Vec3 pos3d) {
        this.position = pos3d;
    }

    public abstract List<Component> getAlignToolTips();
}
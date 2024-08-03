package com.simibubi.create.content.equipment.symmetryWand.mirror;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.core.PartialModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EmptyMirror extends SymmetryMirror {

    public EmptyMirror(Vec3 pos) {
        super(pos);
        this.orientation = EmptyMirror.Align.None;
    }

    @Override
    protected void setOrientation() {
    }

    @Override
    public void setOrientation(int index) {
        this.orientation = EmptyMirror.Align.values()[index];
        this.orientationIndex = index;
    }

    @Override
    public Map<BlockPos, BlockState> process(BlockPos position, BlockState block) {
        return new HashMap();
    }

    @Override
    public String typeName() {
        return "empty";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel getModel() {
        return null;
    }

    @Override
    public List<Component> getAlignToolTips() {
        return ImmutableList.of();
    }

    public static enum Align implements StringRepresentable {

        None("none");

        private final String name;

        private Align(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }
    }
}
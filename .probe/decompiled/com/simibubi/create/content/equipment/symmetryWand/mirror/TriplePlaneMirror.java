package com.simibubi.create.content.equipment.symmetryWand.mirror;

import com.google.common.collect.ImmutableList;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.utility.Lang;
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

public class TriplePlaneMirror extends SymmetryMirror {

    public TriplePlaneMirror(Vec3 pos) {
        super(pos);
        this.orientationIndex = 0;
    }

    @Override
    public Map<BlockPos, BlockState> process(BlockPos position, BlockState block) {
        Map<BlockPos, BlockState> result = new HashMap();
        result.put(this.flipX(position), this.flipX(block));
        result.put(this.flipZ(position), this.flipZ(block));
        result.put(this.flipX(this.flipZ(position)), this.flipX(this.flipZ(block)));
        result.put(this.flipD1(position), this.flipD1(block));
        result.put(this.flipD1(this.flipX(position)), this.flipD1(this.flipX(block)));
        result.put(this.flipD1(this.flipZ(position)), this.flipD1(this.flipZ(block)));
        result.put(this.flipD1(this.flipX(this.flipZ(position))), this.flipD1(this.flipX(this.flipZ(block))));
        return result;
    }

    @Override
    public String typeName() {
        return "triple_plane";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel getModel() {
        return AllPartialModels.SYMMETRY_TRIPLEPLANE;
    }

    @Override
    protected void setOrientation() {
    }

    @Override
    public void setOrientation(int index) {
    }

    @Override
    public StringRepresentable getOrientation() {
        return CrossPlaneMirror.Align.Y;
    }

    @Override
    public List<Component> getAlignToolTips() {
        return ImmutableList.of(Lang.translateDirect("orientation.horizontal"));
    }
}
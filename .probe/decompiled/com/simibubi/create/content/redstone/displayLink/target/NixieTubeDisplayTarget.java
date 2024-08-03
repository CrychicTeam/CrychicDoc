package com.simibubi.create.content.redstone.displayLink.target;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

public class NixieTubeDisplayTarget extends SingleLineDisplayTarget {

    @Override
    protected void acceptLine(MutableComponent text, DisplayLinkContext context) {
        String tagElement = Component.Serializer.toJson(text);
        NixieTubeBlock.walkNixies(context.level(), context.getTargetPos(), (currentPos, rowPosition) -> {
            if (context.level().getBlockEntity(currentPos) instanceof NixieTubeBlockEntity nixie) {
                nixie.displayCustomText(tagElement, rowPosition);
            }
        });
    }

    @Override
    protected int getWidth(DisplayLinkContext context) {
        MutableInt count = new MutableInt(0);
        NixieTubeBlock.walkNixies(context.level(), context.getTargetPos(), (currentPos, rowPosition) -> count.add(2));
        return count.intValue();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AABB getMultiblockBounds(LevelAccessor level, BlockPos pos) {
        MutableObject<BlockPos> start = new MutableObject(null);
        MutableObject<BlockPos> end = new MutableObject(null);
        NixieTubeBlock.walkNixies(level, pos, (currentPos, rowPosition) -> {
            end.setValue(currentPos);
            if (start.getValue() == null) {
                start.setValue(currentPos);
            }
        });
        BlockPos diffToCurrent = ((BlockPos) start.getValue()).subtract(pos);
        BlockPos diff = ((BlockPos) end.getValue()).subtract((Vec3i) start.getValue());
        return super.getMultiblockBounds(level, pos).move(diffToCurrent).expandTowards(Vec3.atLowerCornerOf(diff));
    }
}
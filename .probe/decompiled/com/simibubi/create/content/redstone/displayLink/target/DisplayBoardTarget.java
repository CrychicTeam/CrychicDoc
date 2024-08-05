package com.simibubi.create.content.redstone.displayLink.target;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.DisplaySource;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.trains.display.FlapDisplayBlockEntity;
import com.simibubi.create.content.trains.display.FlapDisplayLayout;
import com.simibubi.create.content.trains.display.FlapDisplaySection;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DisplayBoardTarget extends DisplayTarget {

    @Override
    public void acceptText(int line, List<MutableComponent> text, DisplayLinkContext context) {
    }

    public void acceptFlapText(int line, List<List<MutableComponent>> text, DisplayLinkContext context) {
        FlapDisplayBlockEntity controller = this.getController(context);
        if (controller != null) {
            if (controller.isSpeedRequirementFulfilled()) {
                DisplaySource source = context.blockEntity().activeSource;
                List<FlapDisplayLayout> lines = controller.getLines();
                for (int i = 0; i + line < lines.size(); i++) {
                    if (i == 0) {
                        reserve(i + line, controller, context);
                    }
                    if (i > 0 && this.isReserved(i + line, controller, context)) {
                        break;
                    }
                    FlapDisplayLayout layout = (FlapDisplayLayout) lines.get(i + line);
                    if (i >= text.size()) {
                        if (source instanceof SingleLineDisplaySource) {
                            break;
                        }
                        controller.applyTextManually(i + line, null);
                    } else {
                        source.loadFlapDisplayLayout(context, controller, layout, i);
                        for (int sectionIndex = 0; sectionIndex < layout.getSections().size(); sectionIndex++) {
                            List<MutableComponent> textLine = (List<MutableComponent>) text.get(i);
                            if (textLine.size() <= sectionIndex) {
                                break;
                            }
                            ((FlapDisplaySection) layout.getSections().get(sectionIndex)).setText((Component) textLine.get(sectionIndex));
                        }
                    }
                }
                controller.sendData();
            }
        }
    }

    @Override
    public boolean isReserved(int line, BlockEntity target, DisplayLinkContext context) {
        if (super.isReserved(line, target, context)) {
            return true;
        } else {
            if (target instanceof FlapDisplayBlockEntity fdte && fdte.manualLines.length > line && fdte.manualLines[line]) {
                return true;
            }
            return false;
        }
    }

    @Override
    public DisplayTargetStats provideStats(DisplayLinkContext context) {
        FlapDisplayBlockEntity controller = this.getController(context);
        return controller == null ? new DisplayTargetStats(1, 1, this) : new DisplayTargetStats(controller.ySize * 2, controller.getMaxCharCount(), this);
    }

    private FlapDisplayBlockEntity getController(DisplayLinkContext context) {
        return context.getTargetBlockEntity() instanceof FlapDisplayBlockEntity be ? be.getController() : null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AABB getMultiblockBounds(LevelAccessor level, BlockPos pos) {
        AABB baseShape = super.getMultiblockBounds(level, pos);
        if (level.m_7702_(pos) instanceof FlapDisplayBlockEntity fdbe) {
            FlapDisplayBlockEntity controller = fdbe.getController();
            if (controller == null) {
                return baseShape;
            } else {
                Vec3i normal = controller.getDirection().getClockWise().getNormal();
                return baseShape.move(controller.m_58899_().subtract(pos)).expandTowards((double) (normal.getX() * (controller.xSize - 1)), (double) (1 - controller.ySize), (double) (normal.getZ() * (controller.xSize - 1)));
            }
        } else {
            return baseShape;
        }
    }
}
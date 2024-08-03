package net.mehvahdjukaar.modelfix;

import java.util.List;
import java.util.Set;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ModelFixGeom {

    private static final ResourceLocation BLOCK_ATLAS = new ResourceLocation("textures/atlas/blocks.png");

    public static float getShrinkRatio(ResourceLocation atlasLocation, float defaultValue, float returnValue) {
        return atlasLocation.equals(BLOCK_ATLAS) && defaultValue == returnValue ? (float) ((double) defaultValue * (Double) ModelFix.shrinkMult.get()) : -1.0F;
    }

    public static void createOrExpandSpan(List<ItemModelGenerator.Span> listSpans, ItemModelGenerator.SpanFacing spanFacing, int pixelX, int pixelY) {
        ItemModelGenerator.Span existingSpan = null;
        for (ItemModelGenerator.Span span : listSpans) {
            if (span.getFacing() == spanFacing) {
                int i = spanFacing.isHorizontal() ? pixelY : pixelX;
                if (span.getAnchor() == i && ((Double) ModelFix.expansion.get() == 0.0 || span.getMax() == (!spanFacing.isHorizontal() ? pixelY : pixelX) - 1)) {
                    existingSpan = span;
                    break;
                }
            }
        }
        int length = spanFacing.isHorizontal() ? pixelX : pixelY;
        if (existingSpan == null) {
            int newStart = spanFacing.isHorizontal() ? pixelY : pixelX;
            listSpans.add(new ItemModelGenerator.Span(spanFacing, length, newStart));
        } else {
            existingSpan.expand(length);
        }
    }

    public static void enlargeFaces(CallbackInfoReturnable<List<BlockElement>> cir) {
        double inc = (Double) ModelFix.indent.get();
        double inc2 = (Double) ModelFix.expansion.get();
        for (BlockElement e : (List) cir.getReturnValue()) {
            Vector3f from = e.from;
            Vector3f to = e.to;
            Set<Direction> set = e.faces.keySet();
            if (set.size() == 1) {
                Direction dir = (Direction) set.stream().findAny().get();
                switch(dir) {
                    case UP:
                        from.set((double) from.x() - inc2, (double) from.y() - inc, (double) from.z() - inc2);
                        to.set((double) to.x() + inc2, (double) to.y() - inc, (double) to.z() + inc2);
                        break;
                    case DOWN:
                        from.set((double) from.x() - inc2, (double) from.y() + inc, (double) from.z() - inc2);
                        to.set((double) to.x() + inc2, (double) to.y() + inc, (double) to.z() + inc2);
                        break;
                    case WEST:
                        from.set((double) from.x() - inc, (double) from.y() + inc2, (double) from.z() - inc2);
                        to.set((double) to.x() - inc, (double) to.y() - inc2, (double) to.z() + inc2);
                        break;
                    case EAST:
                        from.set((double) from.x() + inc, (double) from.y() + inc2, (double) from.z() - inc2);
                        to.set((double) to.x() + inc, (double) to.y() - inc2, (double) to.z() + inc2);
                }
            }
        }
    }
}
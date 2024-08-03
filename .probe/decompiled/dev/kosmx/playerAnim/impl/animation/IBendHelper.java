package dev.kosmx.playerAnim.impl.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.impl.Helper;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Internal
public interface IBendHelper {

    IBendHelper INSTANCE = (IBendHelper) (Helper.isBendEnabled() ? new BendHelper() : new IBendHelper.DummyBendable());

    static void rotateMatrixStack(PoseStack matrices, Pair<Float, Float> pair) {
        float offset = 0.375F;
        matrices.translate(0.0F, offset, 0.0F);
        float bend = pair.getRight();
        float axisf = -pair.getLeft();
        Vector3f axis = new Vector3f((float) Math.cos((double) axisf), 0.0F, (float) Math.sin((double) axisf));
        matrices.mulPose(new Quaternionf().rotateAxis(bend, axis));
        matrices.translate(0.0F, -offset, 0.0F);
    }

    void bend(ModelPart var1, float var2, float var3);

    void bend(ModelPart var1, @Nullable Pair<Float, Float> var2);

    void initBend(ModelPart var1, Direction var2);

    public static class DummyBendable implements IBendHelper {

        @Override
        public void bend(ModelPart modelPart, float a, float b) {
        }

        @Override
        public void bend(ModelPart modelPart, @Nullable Pair<Float, Float> pair) {
        }

        @Override
        public void initBend(ModelPart modelPart, Direction direction) {
        }
    }
}
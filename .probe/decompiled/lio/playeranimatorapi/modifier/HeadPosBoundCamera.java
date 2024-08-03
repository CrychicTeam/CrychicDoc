package lio.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import lio.playeranimatorapi.playeranims.CustomModifierLayer;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.NotNull;

public class HeadPosBoundCamera extends AbstractCameraModifier {

    private final CustomModifierLayer layer;

    public HeadPosBoundCamera(CustomModifierLayer layer) {
        this.layer = layer;
    }

    @NotNull
    @Override
    public Vec3f get3DCameraTransform(GameRenderer renderer, Camera camera, TransformType type, float tickDelta, @NotNull Vec3f value0) {
        if (type == TransformType.POSITION && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            value0 = value0.add(this.layer.get3DTransform("head", TransformType.POSITION, tickDelta, value0));
        }
        return value0;
    }
}
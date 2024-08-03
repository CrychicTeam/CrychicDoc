package lio.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCameraModifier extends AbstractModifier {

    @NotNull
    public Vec3f get3DCameraTransform(GameRenderer renderer, Camera camera, TransformType type, float tickDelta, @NotNull Vec3f value0) {
        return value0;
    }
}
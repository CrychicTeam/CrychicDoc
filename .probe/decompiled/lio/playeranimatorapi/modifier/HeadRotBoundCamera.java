package lio.playeranimatorapi.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import lio.playeranimatorapi.playeranims.CustomModifierLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.jetbrains.annotations.NotNull;

public class HeadRotBoundCamera extends AbstractCameraModifier {

    private final CustomModifierLayer layer;

    public HeadRotBoundCamera(CustomModifierLayer layer) {
        this.layer = layer;
    }

    @NotNull
    @Override
    public Vec3f get3DCameraTransform(GameRenderer renderer, Camera camera, TransformType type, float tickDelta, @NotNull Vec3f value0) {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        float f = player.f_20883_;
        float g = player.f_20885_;
        float netHeadYaw = g - f;
        if (player.m_20159_() && player.m_20202_() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) player.m_20202_();
            f = player.f_20883_;
            netHeadYaw = g - f;
            float i = Mth.wrapDegrees(netHeadYaw);
            if (i < -85.0F) {
                i = -85.0F;
            }
            if (i >= 85.0F) {
                i = 85.0F;
            }
            f = g - i;
            if (i * i > 2500.0F) {
                f += i * 0.2F;
            }
            netHeadYaw = g - f;
        }
        float headPitch = player.m_146909_();
        if (isEntityUpsideDown(player)) {
            headPitch *= -1.0F;
            netHeadYaw *= -1.0F;
        }
        if (type == TransformType.ROTATION && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            value0 = this.layer.get3DTransform("head", TransformType.ROTATION, tickDelta, value0).add(new Vec3f(0.0F, Mth.wrapDegrees(player.m_146908_() - netHeadYaw) * (float) (Math.PI / 180.0), 0.0F));
        }
        return value0;
    }

    public static boolean isEntityUpsideDown(LivingEntity entity) {
        if (entity instanceof Player || entity.m_8077_()) {
            String string = ChatFormatting.stripFormatting(entity.m_7755_().getString());
            if ("Dinnerbone".equals(string) || "Grumm".equals(string)) {
                return !(entity instanceof Player) || ((Player) entity).isModelPartShown(PlayerModelPart.CAPE);
            }
        }
        return false;
    }
}
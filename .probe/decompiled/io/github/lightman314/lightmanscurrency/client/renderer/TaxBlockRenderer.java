package io.github.lightman314.lightmanscurrency.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.lightman314.lightmanscurrency.client.data.ClientTaxData;
import io.github.lightman314.lightmanscurrency.client.util.OutlineUtil;
import io.github.lightman314.lightmanscurrency.common.taxes.TaxEntry;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "lightmanscurrency")
public class TaxBlockRenderer {

    @SubscribeEvent
    public static void onLevelRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            Player player = Minecraft.getInstance().player;
            Level level = Minecraft.getInstance().level;
            PoseStack pose = event.getPoseStack();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            double cameraX = event.getCamera().getPosition().x();
            double cameraY = event.getCamera().getPosition().y();
            double cameraZ = event.getCamera().getPosition().z();
            for (TaxEntry entry : ClientTaxData.GetAllTaxEntries()) {
                if (shouldRenderEntry(entry, level, player, event.getCamera().getPosition())) {
                    pose.pushPose();
                    int radius = entry.getRadius();
                    int height = entry.getHeight();
                    int vertOffset = entry.getVertOffset();
                    AABB renderArea = new AABB((double) (-radius), (double) vertOffset, (double) (-radius), (double) radius + 1.0, (double) (vertOffset + height), (double) radius + 1.0);
                    BlockPos center = entry.getCenter().getPos();
                    pose.translate((double) center.m_123341_() - cameraX, (double) center.m_123342_() - cameraY, (double) center.m_123343_() - cameraZ);
                    OutlineUtil.renderBox(pose, buffer, renderArea, entry.getRenderColor(player), 0.1F);
                    pose.popPose();
                }
            }
        }
    }

    private static boolean shouldRenderEntry(@Nonnull TaxEntry entry, @Nonnull Level level, @Nonnull Player player, @Nonnull Vec3 cameraPos) {
        BlockPos center = entry.getCenter().getPos();
        double renderDistance = 256.0 + (double) entry.getRadius();
        return entry.shouldRender(player) && entry.getCenter().sameDimension(level) && center.m_203198_(cameraPos.x, cameraPos.y, cameraPos.z) <= renderDistance * renderDistance;
    }
}
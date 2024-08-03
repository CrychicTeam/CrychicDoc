package icyllis.modernui.mc.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.Nonnull;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Experimental
final class ProjectBuilderRenderer extends BlockEntityWithoutLevelRenderer {

    ProjectBuilderRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void onResourceManagerReload(@Nonnull ResourceManager resourceManager) {
    }

    @Override
    public void renderByItem(@Nonnull ItemStack stack, @Nonnull ItemDisplayContext transformType, @Nonnull PoseStack ps, @Nonnull MultiBufferSource source, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ProjectBuilderModel model = (ProjectBuilderModel) itemRenderer.getModel(stack, null, null, 0);
        ps.pushPose();
        ps.translate(0.5, 0.5, 0.5);
        itemRenderer.render(stack, transformType, true, ps, source, combinedLight, combinedOverlay, model.main);
        long time = Util.getMillis();
        float angel = (float) time * -0.08F;
        angel %= 360.0F;
        ps.translate(0.0F, 0.0F, -0.671875F);
        ps.mulPose(Axis.YN.rotationDegrees(angel));
        float f = ((float) Math.sin((double) time / 200.0) + 1.0F) * 0.5F;
        int glowX = (int) Mth.lerp(f, (float) (combinedLight >> 16), 240.0F);
        int glowY = (int) Mth.lerp(f, (float) (combinedLight & 65535), 240.0F);
        itemRenderer.render(stack, transformType, true, ps, source, glowX << 16 | glowY, combinedOverlay, model.cube);
        ps.popPose();
    }
}
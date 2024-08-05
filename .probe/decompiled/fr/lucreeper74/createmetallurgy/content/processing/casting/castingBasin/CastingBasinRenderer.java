package fr.lucreeper74.createmetallurgy.content.processing.casting.castingBasin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import fr.lucreeper74.createmetallurgy.utils.CastingItemRenderTypeBuffer;
import fr.lucreeper74.createmetallurgy.utils.ColoredFluidRenderer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;

public class CastingBasinRenderer extends SmartBlockEntityRenderer<CastingBasinBlockEntity> {

    private CastingBasinRecipe recipe;

    public CastingBasinRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(CastingBasinBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        List<Recipe<?>> recipes = be.getMatchingRecipes();
        if (!recipes.isEmpty()) {
            this.recipe = (CastingBasinRecipe) recipes.get(0);
        }
        SmartFluidTankBehaviour tank = be.inputTank;
        if (tank != null) {
            int fluidOpacity = 255;
            SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
            FluidStack fluidStack = primaryTank.getRenderedFluid();
            float level = primaryTank.getFluidLevel().getValue(partialTicks);
            if (!fluidStack.isEmpty() && level > 0.01F) {
                float min = 0.125F;
                float yOffset = 0.8125F * level;
                ms.pushPose();
                ms.translate(0.0F, yOffset, 0.0F);
                if (be.running) {
                    int timer = be.processingTick;
                    int totalTime = this.recipe.getProcessingDuration();
                    if (timer > 0 && totalTime > 0) {
                        fluidOpacity = 255 * timer / totalTime;
                    }
                }
                ColoredFluidRenderer.renderFluidBox(fluidStack, 0.125F, min - yOffset, 0.125F, 0.875F, min, 0.875F, buffer, ms, light, ColoredFluidRenderer.RGBAtoColor(255, 255, 255, 255), false);
                ms.popPose();
            }
            ms.pushPose();
            ms.translate(0.5F, 0.0F, 0.5F);
            ms.scale(3.1F, 3.1F, 3.1F);
            if (be.running) {
                MultiBufferSource bufferOut = new CastingItemRenderTypeBuffer(buffer, 255 - fluidOpacity, fluidOpacity);
                this.renderItem(ms, bufferOut, light, overlay, this.recipe.m_8043_(be.m_58904_().registryAccess()).copy());
            }
            this.renderItem(ms, buffer, light, overlay, be.inv.m_8020_(0));
            ms.popPose();
        }
    }

    protected void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer, mc.level, 0);
    }
}
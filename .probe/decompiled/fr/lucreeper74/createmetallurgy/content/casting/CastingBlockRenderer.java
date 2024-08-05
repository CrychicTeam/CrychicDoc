package fr.lucreeper74.createmetallurgy.content.casting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import fr.lucreeper74.createmetallurgy.content.casting.recipe.CastingRecipe;
import fr.lucreeper74.createmetallurgy.content.casting.table.CastingTableBlockEntity;
import fr.lucreeper74.createmetallurgy.utils.CastingItemRenderTypeBuffer;
import fr.lucreeper74.createmetallurgy.utils.ColoredFluidRenderer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;

public class CastingBlockRenderer extends SmartBlockEntityRenderer<CastingBlockEntity> {

    private CastingRecipe recipe;

    public CastingBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(CastingBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        List<Recipe<?>> recipes = be.getMatchingRecipes();
        if (!recipes.isEmpty()) {
            this.recipe = (CastingRecipe) recipes.get(0);
        }
        SmartFluidTankBehaviour tank = be.inputTank;
        if (tank != null) {
            SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
            FluidStack fluidStack = primaryTank.getRenderedFluid();
            float level = primaryTank.getFluidLevel().getValue(partialTicks);
            int fluidOpacity = 255;
            if (!fluidStack.isEmpty() && level > 0.01F) {
                float min;
                float yOffset;
                if (be instanceof CastingTableBlockEntity) {
                    min = 0.8125F;
                    yOffset = 0.05F * level;
                } else {
                    min = 0.125F;
                    yOffset = 0.8125F * level;
                }
                ms.pushPose();
                ms.translate(0.0F, yOffset, 0.0F);
                if (be.running) {
                    int timer = be.processingTick;
                    int totalTime = this.recipe.getProcessingDuration();
                    if (timer > 0 && totalTime > 0) {
                        fluidOpacity = 255 * timer / totalTime;
                    }
                }
                ColoredFluidRenderer.renderFluidBox(fluidStack, 0.125F, min - yOffset, 0.125F, 0.875F, min, 0.875F, buffer, ms, light, ColoredFluidRenderer.RGBAtoColor(255, 255, 255, fluidOpacity), false);
                ms.popPose();
            }
            if (be.running) {
                MultiBufferSource bufferOut = new CastingItemRenderTypeBuffer(buffer, 255 - fluidOpacity, fluidOpacity);
                this.renderItem(be, ms, bufferOut, light, overlay, this.recipe.getResultItem(be.m_58904_().registryAccess()).copy());
            }
            this.renderItem(be, ms, buffer, light, overlay, be.inv.m_8020_(0));
            this.renderItem(be, ms, buffer, light, overlay, be.moldInv.m_8020_(0));
        }
    }

    protected void renderItem(CastingBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack) {
        ms.pushPose();
        if (be instanceof CastingTableBlockEntity) {
            ms.translate(0.5F, 0.0F, 0.5F);
            ms.mulPose(Axis.YP.rotationDegrees(-90.0F * (float) ((Direction) be.m_58900_().m_61143_(CastingBlock.FACING)).get2DDataValue()));
            ms.translate(-0.5F, 0.0F, -0.5F);
            ms.translate(0.5F, 0.84375F, 0.6875F);
            ms.scale(1.5F, 1.5F, 1.5F);
            ms.mulPose(Axis.XP.rotationDegrees(-90.0F));
        } else if (stack.getItem() instanceof BlockItem) {
            ms.translate(0.5F, 0.0F, 0.5F);
            ms.scale(3.1F, 3.1F, 3.1F);
        } else {
            ms.translate(0.5F, 0.5F, 0.5F);
            ms.scale(1.0F, 1.0F, 1.0F);
        }
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer, mc.level, 0);
        ms.popPose();
    }
}
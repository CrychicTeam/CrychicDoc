package com.rekindled.embers.upgrade;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.EmberBoreBladeRenderEvent;
import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.EmberBoreBlockEntity;
import com.rekindled.embers.blockentity.ExcavationBucketsBlockEntity;
import com.rekindled.embers.recipe.BoringContext;
import com.rekindled.embers.recipe.IBoringRecipe;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

public class ExcavationBucketsUpgrade extends DefaultUpgradeProvider {

    @OnlyIn(Dist.CLIENT)
    public static BakedModel buckets;

    public ExcavationBucketsUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "excavation_buckets"), tile);
    }

    @Override
    public int getPriority() {
        return -90;
    }

    @Override
    public int getLimit(BlockEntity tile) {
        return tile instanceof EmberBoreBlockEntity ? Integer.MAX_VALUE : 0;
    }

    @Override
    public boolean doTick(BlockEntity tile, List<UpgradeContext> upgrades, int distance, int count) {
        ((ExcavationBucketsBlockEntity) this.tile).lastAngle = ((ExcavationBucketsBlockEntity) this.tile).angle;
        if (tile instanceof EmberBoreBlockEntity bore && bore.isRunning) {
            ((ExcavationBucketsBlockEntity) this.tile).angle = (float) ((double) ((ExcavationBucketsBlockEntity) this.tile).angle + 14.0 * bore.speedMod);
        }
        return false;
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (tile instanceof EmberBoreBlockEntity bore && event instanceof MachineRecipeEvent<?> recipeEvent && recipeEvent.getRecipe() instanceof List) {
            List<IBoringRecipe> recipes = (List<IBoringRecipe>) recipeEvent.getRecipe();
            ResourceKey<Biome> biome = (ResourceKey<Biome>) tile.getLevel().m_204166_(tile.getBlockPos()).unwrapKey().get();
            BoringContext context = new BoringContext(tile.getLevel().dimension().location(), biome.location(), tile.getBlockPos().m_123342_(), (BlockState[]) tile.getLevel().m_46847_(bore.getBladeBoundingBox()).toArray(BlockState[]::new));
            List<IBoringRecipe> newRecipes = tile.getLevel().getRecipeManager().getRecipesFor(RegistryManager.EXCAVATION.get(), context, tile.getLevel());
            recipes.addAll(newRecipes);
        }
        if (tile.getLevel().isClientSide()) {
            ExcavationBucketsUpgrade.CLientStuff.throwEvent(tile, upgrades, event, distance, count);
        }
    }

    public static class CLientStuff {

        public static void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
            if (ExcavationBucketsUpgrade.buckets != null && event instanceof EmberBoreBladeRenderEvent renderEvent) {
                Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(renderEvent.getPose().last(), renderEvent.getBuffer().getBuffer(Sheets.solidBlockSheet()), renderEvent.getBlockState(), ExcavationBucketsUpgrade.buckets, 0.0F, 0.0F, 0.0F, renderEvent.getLight(), renderEvent.getOverlay(), ModelData.EMPTY, Sheets.solidBlockSheet());
            }
        }
    }
}
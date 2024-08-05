package fuzs.puzzleslib.api.client.core.v1;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.searchtree.SearchRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface ClientAbstractions {

    ClientAbstractions INSTANCE = ServiceProviderHelper.load(ClientAbstractions.class);

    boolean isKeyActiveAndMatches(KeyMapping var1, int var2, int var3);

    ClientTooltipComponent createImageComponent(TooltipComponent var1);

    BakedModel getBakedModel(ResourceLocation var1);

    @Deprecated(forRemoval = true)
    default BakedModel getBakedModel(ModelManager modelManager, ResourceLocation identifier) {
        return this.getBakedModel(identifier);
    }

    RenderType getRenderType(Block var1);

    default RenderType getRenderType(Fluid fluid) {
        return ItemBlockRenderTypes.getRenderLayer(fluid.defaultFluidState());
    }

    void registerRenderType(Block var1, RenderType var2);

    void registerRenderType(Fluid var1, RenderType var2);

    float getPartialTick();

    @Deprecated(forRemoval = true)
    default float getPartialTick(Minecraft minecraft) {
        return this.getPartialTick();
    }

    SearchRegistry getSearchRegistry();

    @Deprecated(forRemoval = true)
    default SearchRegistry getSearchRegistry(Minecraft minecraft) {
        return this.getSearchRegistry();
    }
}
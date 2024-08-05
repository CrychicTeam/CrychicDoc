package noppes.npcs.client.renderer;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import noppes.npcs.client.layer.LayerSlimeNpc;
import noppes.npcs.client.model.ModelNpcSlime;
import noppes.npcs.entity.EntityNpcSlime;

public class RenderNpcSlime<T extends EntityNpcSlime, M extends ModelNpcSlime<T>> extends RenderNPCInterface<T, M> {

    private Model scaleAmount;

    public RenderNpcSlime(EntityRendererProvider.Context manager, M par1Model, Model limbSwingAmountModel, float par3) {
        super(manager, par1Model, par3);
        this.scaleAmount = limbSwingAmountModel;
        this.m_115326_(new LayerSlimeNpc(this));
    }
}
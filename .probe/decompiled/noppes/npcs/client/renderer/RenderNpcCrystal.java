package noppes.npcs.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import noppes.npcs.client.model.ModelNpcCrystal;

public class RenderNpcCrystal extends RenderNPCInterface {

    ModelNpcCrystal mainmodel;

    public RenderNpcCrystal(EntityRendererProvider.Context manager, ModelNpcCrystal model) {
        super(manager, model, 0.0F);
        this.mainmodel = model;
    }
}
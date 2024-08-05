package noppes.npcs.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import noppes.npcs.entity.EntityNPCInterface;

public class RenderNpcDragon<T extends EntityNPCInterface, M extends EntityModel<T>> extends RenderNPCInterface<T, M> {

    public RenderNpcDragon(EntityRendererProvider.Context manager, M model, float f) {
        super(manager, model, f);
    }

    @Override
    protected void scale(T npc, PoseStack matrixScale, float f) {
        matrixScale.translate(0.0F, 0.0F, 0.120000005F * (float) npc.display.getSize());
        super.scale(npc, matrixScale, f);
    }
}
package noppes.npcs.mixin;

import java.util.List;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import noppes.npcs.entity.EntityCustomNpc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ LivingEntityRenderer.class })
public interface LivingRenderer2Mixin<T extends EntityCustomNpc, M extends HumanoidModel<T>> {

    @Accessor("layers")
    List<RenderLayer<T, M>> layers();
}
package fuzs.puzzleslib.api.client.init.v1;

import java.util.function.BiConsumer;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.world.level.block.SkullBlock;

@FunctionalInterface
public interface SkullRenderersFactory {

    void createSkullRenderers(EntityModelSet var1, BiConsumer<SkullBlock.Type, SkullModelBase> var2);
}
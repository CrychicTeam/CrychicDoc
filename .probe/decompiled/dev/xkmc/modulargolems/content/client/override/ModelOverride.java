package dev.xkmc.modulargolems.content.client.override;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;

public class ModelOverride {

    public static final ModelOverride DEFAULT = new ModelOverride();

    public static ModelOverride texturePredicate(final Function<AbstractGolemEntity<?, ?>, String> modifier) {
        return new ModelOverride() {

            @Override
            public ResourceLocation getTexture(AbstractGolemEntity<?, ?> golem, ResourceLocation id) {
                return super.getTexture(golem, id).withSuffix((String) modifier.apply(golem));
            }
        };
    }

    public ResourceLocation getTexture(AbstractGolemEntity<?, ?> golem, ResourceLocation id) {
        return id;
    }
}
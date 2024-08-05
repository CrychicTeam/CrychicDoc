package snownee.jade.impl.template;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;

public final class TemplateEntityComponentProvider extends TemplateComponentProvider<EntityAccessor> implements IEntityComponentProvider {

    public TemplateEntityComponentProvider(ResourceLocation uid) {
        this(uid, false, true, 0);
    }

    public TemplateEntityComponentProvider(ResourceLocation uid, boolean required, boolean enabledByDefault, int defaultPriority) {
        super(uid, required, enabledByDefault, defaultPriority);
    }
}
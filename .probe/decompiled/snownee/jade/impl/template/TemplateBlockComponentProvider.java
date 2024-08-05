package snownee.jade.impl.template;

import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;

public final class TemplateBlockComponentProvider extends TemplateComponentProvider<BlockAccessor> implements IBlockComponentProvider {

    public TemplateBlockComponentProvider(ResourceLocation uid) {
        this(uid, false, true, 0);
    }

    public TemplateBlockComponentProvider(ResourceLocation uid, boolean required, boolean enabledByDefault, int defaultPriority) {
        super(uid, required, enabledByDefault, defaultPriority);
    }
}
package snownee.jade.impl.template;

import java.util.function.BiConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.Accessor;
import snownee.jade.api.IServerDataProvider;

public class TemplateServerDataProvider<T extends Accessor<?>> implements IServerDataProvider<T> {

    private final ResourceLocation uid;

    private BiConsumer<CompoundTag, T> dataFunction = (data, accessor) -> {
    };

    protected TemplateServerDataProvider(ResourceLocation uid) {
        this.uid = uid;
    }

    @Override
    public ResourceLocation getUid() {
        return this.uid;
    }

    @Override
    public void appendServerData(CompoundTag data, T accessor) {
        this.dataFunction.accept(data, accessor);
    }

    public void setDataFunction(BiConsumer<CompoundTag, T> dataFunction) {
        this.dataFunction = dataFunction;
    }
}
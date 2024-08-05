package com.mrcrayfish.configured.impl.forge;

import com.mrcrayfish.configured.client.screen.list.IListConfigValue;
import com.mrcrayfish.configured.client.screen.list.IListType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeListValue<T> extends ForgeValue<List<T>> implements IListConfigValue<T> {

    @Nullable
    protected final Function<List<T>, List<T>> converter;

    public ForgeListValue(ForgeConfigSpec.ConfigValue<List<T>> configValue, ForgeConfigSpec.ValueSpec valueSpec) {
        super(configValue, valueSpec);
        this.converter = this.createConverter(configValue);
    }

    @Nullable
    private Function<List<T>, List<T>> createConverter(ForgeConfigSpec.ConfigValue<List<T>> configValue) {
        List<T> original = configValue.get();
        if (original instanceof ArrayList) {
            return ArrayList::new;
        } else {
            return original instanceof LinkedList ? LinkedList::new : null;
        }
    }

    public void set(List<T> value) {
        this.valueSpec.correct(value);
        super.set((T) (new ArrayList(value)));
    }

    @Nullable
    public List<T> getConverted() {
        return this.converter != null ? (List) this.converter.apply(this.get()) : null;
    }

    @Override
    public IListType<T> getListType() {
        return null;
    }
}
package com.mrcrayfish.configured.impl.framework;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mrcrayfish.configured.api.IConfigEntry;
import com.mrcrayfish.configured.api.IConfigValue;
import com.mrcrayfish.configured.api.ValueEntry;
import com.mrcrayfish.framework.api.config.EnumProperty;
import com.mrcrayfish.framework.api.config.ListProperty;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class FrameworkFolderEntry implements IConfigEntry {

    private final FrameworkModConfig.PropertyMap map;

    private List<IConfigEntry> entries;

    public FrameworkFolderEntry(FrameworkModConfig.PropertyMap map) {
        this.map = map;
    }

    @Override
    public List<IConfigEntry> getChildren() {
        if (this.entries == null) {
            Builder<IConfigEntry> builder = ImmutableList.builder();
            this.map.getConfigMaps().forEach(pair -> builder.add(new FrameworkFolderEntry((FrameworkModConfig.PropertyMap) pair.right())));
            this.map.getConfigProperties().forEach(property -> {
                if (property instanceof ListProperty<?> listProperty) {
                    builder.add(new ValueEntry(new FrameworkListValue(listProperty)));
                } else if (property instanceof EnumProperty<?> enumProperty) {
                    builder.add(new ValueEntry(new FrameworkEnumValue(enumProperty)));
                } else {
                    builder.add(new ValueEntry(new FrameworkValue(property)));
                }
            });
            this.entries = builder.build();
        }
        return this.entries;
    }

    @Override
    public boolean isRoot() {
        return this.map.getPath().isEmpty();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Nullable
    @Override
    public IConfigValue<?> getValue() {
        return null;
    }

    @Override
    public String getEntryName() {
        return lastValue(this.map.getPath(), "Root");
    }

    @Nullable
    @Override
    public Component getTooltip() {
        String comment = this.map.getComment();
        return comment != null ? Component.literal(comment) : null;
    }

    @Nullable
    @Override
    public String getTranslationKey() {
        return this.map.getTranslationKey();
    }

    public static <V> V lastValue(List<V> list, V defaultValue) {
        return (V) (!list.isEmpty() ? list.get(list.size() - 1) : defaultValue);
    }
}
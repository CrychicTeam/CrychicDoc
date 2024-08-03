package me.jellysquid.mods.sodium.mixin.features.model;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = { ModelData.class }, remap = false)
public class ModelDataMixin {

    @Shadow
    @Final
    private Map<ModelProperty<?>, Object> properties;

    private Set<ModelProperty<?>> embeddium$propertySetView;

    @Overwrite
    public Set<ModelProperty<?>> getProperties() {
        Set<ModelProperty<?>> view = this.embeddium$propertySetView;
        if (view == null) {
            this.embeddium$propertySetView = view = Collections.unmodifiableSet(this.properties.keySet());
        }
        return view;
    }
}
package me.jellysquid.mods.sodium.mixin.features.render.entity.remove_streams;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraft.client.model.geom.ModelPart;
import org.embeddedt.embeddium.render.entity.ModelPartExtended;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ModelPart.class })
public class ModelPartMixin implements ModelPartExtended {

    @Shadow
    @Final
    private Map<String, ModelPart> children;

    private List<ModelPart> embeddium$allParts;

    private Optional<ModelPart> embeddium$optional;

    private Map<String, ModelPart> embeddium$descendantsByName;

    @Override
    public Optional<ModelPart> embeddium$asOptional() {
        if (this.embeddium$optional == null) {
            this.embeddium$optional = Optional.of((ModelPart) this);
        }
        return this.embeddium$optional;
    }

    @Override
    public Map<String, ModelPart> embeddium$getDescendantsByName() {
        if (this.embeddium$descendantsByName == null) {
            Object2ObjectOpenHashMap<String, ModelPart> map = new Object2ObjectOpenHashMap();
            for (ModelPart part : this.embeddium$getPartsList()) {
                for (Entry<String, ModelPart> entry : ((ModelPartMixin) part).children.entrySet()) {
                    map.putIfAbsent((String) entry.getKey(), (ModelPart) entry.getValue());
                }
            }
            this.embeddium$descendantsByName = Map.copyOf(map);
        }
        return this.embeddium$descendantsByName;
    }

    @Override
    public List<ModelPart> embeddium$getPartsList() {
        if (this.embeddium$allParts == null) {
            Builder<ModelPart> listBuilder = ImmutableList.builder();
            listBuilder.add((ModelPart) this);
            for (ModelPart part : this.children.values()) {
                listBuilder.addAll(part.getAllParts().toList());
            }
            this.embeddium$allParts = listBuilder.build();
        }
        return this.embeddium$allParts;
    }

    @Overwrite
    public Stream<ModelPart> getAllParts() {
        return this.embeddium$getPartsList().stream();
    }
}
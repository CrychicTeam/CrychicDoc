package me.jellysquid.mods.sodium.mixin.features.render.entity.remove_streams;

import java.util.Optional;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import org.embeddedt.embeddium.render.entity.ModelPartExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ HierarchicalModel.class })
public abstract class HierarchicalModelMixin {

    @Shadow
    public abstract ModelPart root();

    @Overwrite
    public Optional<ModelPart> getAnyDescendantWithName(String pName) {
        ModelPartExtended extendedRoot = ModelPartExtended.of(this.root());
        if (pName.equals("root")) {
            return extendedRoot.embeddium$asOptional();
        } else {
            ModelPart part = (ModelPart) extendedRoot.embeddium$getDescendantsByName().get(pName);
            return part != null ? ModelPartExtended.of(part).embeddium$asOptional() : Optional.empty();
        }
    }
}
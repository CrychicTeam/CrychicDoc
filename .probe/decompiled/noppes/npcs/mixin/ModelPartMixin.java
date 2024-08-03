package noppes.npcs.mixin;

import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ModelPart.class })
public interface ModelPartMixin {

    @Accessor("children")
    Map<String, ModelPart> getChildren();
}
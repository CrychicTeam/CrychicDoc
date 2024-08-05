package noppes.npcs.mixin;

import net.minecraft.world.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ArmorMaterials.class })
public interface ArmorMaterialsMixin {

    @Accessor("durabilityMultiplier")
    int durabilityMultiplier();
}
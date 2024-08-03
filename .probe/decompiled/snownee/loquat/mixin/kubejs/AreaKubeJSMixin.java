package snownee.loquat.mixin.kubejs;

import dev.latvian.mods.kubejs.core.WithPersistentData;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import snownee.loquat.core.area.Area;

@Mixin({ Area.class })
public class AreaKubeJSMixin implements WithPersistentData {

    @Override
    public CompoundTag kjs$getPersistentData() {
        return ((Area) this).getOrCreateAttachedData();
    }
}
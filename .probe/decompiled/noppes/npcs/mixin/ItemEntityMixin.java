package noppes.npcs.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ItemEntity.class })
public interface ItemEntityMixin {

    @Accessor("pickupDelay")
    int pickupDelay();

    @Accessor("age")
    void age(int var1);
}
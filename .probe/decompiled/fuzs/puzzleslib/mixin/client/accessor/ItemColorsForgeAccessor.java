package fuzs.puzzleslib.mixin.client.accessor;

import java.util.Map;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ItemColors.class })
public interface ItemColorsForgeAccessor {

    @Accessor("itemColors")
    Map<Holder.Reference<Item>, ItemColor> puzzleslib$getItemColors();
}
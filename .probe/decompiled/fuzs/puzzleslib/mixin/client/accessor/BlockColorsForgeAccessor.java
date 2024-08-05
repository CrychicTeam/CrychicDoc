package fuzs.puzzleslib.mixin.client.accessor;

import java.util.Map;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ BlockColors.class })
public interface BlockColorsForgeAccessor {

    @Accessor("blockColors")
    Map<Holder.Reference<Block>, BlockColor> puzzleslib$getBlockColors();
}
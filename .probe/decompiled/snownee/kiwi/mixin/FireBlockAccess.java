package snownee.kiwi.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ FireBlock.class })
public interface FireBlockAccess {

    @Invoker
    void callSetFlammable(Block var1, int var2, int var3);
}
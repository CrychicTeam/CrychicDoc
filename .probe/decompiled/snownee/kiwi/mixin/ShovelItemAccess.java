package snownee.kiwi.mixin;

import java.util.Map;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ ShovelItem.class })
public interface ShovelItemAccess {

    @Accessor
    static Map<Block, BlockState> getFLATTENABLES() {
        throw new IllegalStateException();
    }

    @Accessor
    @Mutable
    static void setFLATTENABLES(Map<Block, BlockState> map) {
        throw new IllegalStateException();
    }
}
package snownee.jade.api.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface CustomEnchantPower {

    float getEnchantPowerBonus(BlockState var1, Level var2, BlockPos var3);
}
package snownee.kiwi.customization.block.behavior;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface UseHandler {

    InteractionResult use(BlockState var1, Player var2, Level var3, InteractionHand var4, BlockHitResult var5);
}
package team.lodestar.lodestone.systems.placementassistance;

import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface IPlacementAssistant {

    void onPlaceBlock(Player var1, Level var2, BlockHitResult var3, BlockState var4, ItemStack var5);

    void onObserveBlock(Player var1, Level var2, BlockHitResult var3, BlockState var4, ItemStack var5);

    Predicate<ItemStack> canAssist();
}
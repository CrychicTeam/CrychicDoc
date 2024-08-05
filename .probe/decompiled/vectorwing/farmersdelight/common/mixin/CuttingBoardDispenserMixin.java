package vectorwing.farmersdelight.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.entity.dispenser.CuttingBoardDispenseBehavior;
import vectorwing.farmersdelight.common.registry.ModBlocks;

@Mixin({ DispenserBlock.class })
public abstract class CuttingBoardDispenserMixin {

    @Shadow
    protected abstract DispenseItemBehavior getDispenseMethod(ItemStack var1);

    @Inject(method = { "dispenseFrom" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/DispenserBlock;getDispenseMethod(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/core/dispenser/DispenseItemBehavior;") }, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void onCuttingBoardDispenseFromInject(ServerLevel level, BlockPos pos, CallbackInfo ci, BlockSourceImpl source, DispenserBlockEntity dispenser, int slot, ItemStack stack) {
        BlockState facingState = level.m_8055_(pos.relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING)));
        if (Configuration.DISPENSER_TOOLS_CUTTING_BOARD.get() && facingState.m_60713_(ModBlocks.CUTTING_BOARD.get())) {
            dispenser.m_6836_(slot, CuttingBoardDispenseBehavior.INSTANCE.dispense(source, stack));
            ci.cancel();
        }
    }
}
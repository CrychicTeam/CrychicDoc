package harmonised.pmmo.mixin;

import harmonised.pmmo.api.events.FurnaceBurnEvent;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ AbstractFurnaceBlockEntity.class })
public class AbstractFurnaceTileEntityShrinkMixin {

    @Shadow
    protected NonNullList<ItemStack> items;

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V") }, method = { "burn(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/crafting/Recipe;Lnet/minecraft/core/NonNullList;I)Z" })
    public void projectmmo$$handleSmeltingShrink(RegistryAccess registryAccess0, @Nullable Recipe<?> recipe1, NonNullList<ItemStack> nonNullListItemStack2, int int3, CallbackInfoReturnable<?> info) {
        Level world = ((AbstractFurnaceBlockEntity) this).m_58904_();
        BlockPos pos = ((AbstractFurnaceBlockEntity) this).m_58899_();
        MinecraftForge.EVENT_BUS.post(new FurnaceBurnEvent(this.items.get(0), world, pos));
    }
}
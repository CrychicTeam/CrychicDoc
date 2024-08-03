package dev.architectury.mixin.forge;

import dev.architectury.extensions.ItemExtension;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemExtension.class })
public interface MixinItemExtension extends IForgeItem {

    @Override
    default void onArmorTick(ItemStack stack, Level world, Player player) {
        ((ItemExtension) this).tickArmor(stack, player);
    }

    @Nullable
    @Override
    default EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return ((ItemExtension) this).getCustomEquipmentSlot(stack);
    }
}
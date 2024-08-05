package snownee.lychee.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ InventoryMenu.class })
public interface InventoryMenuAccess {

    @Accessor
    Player getOwner();
}
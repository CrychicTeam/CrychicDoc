package snownee.lychee.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ CraftingMenu.class })
public interface CraftingMenuAccess {

    @Accessor
    ContainerLevelAccess getAccess();

    @Accessor
    Player getPlayer();
}
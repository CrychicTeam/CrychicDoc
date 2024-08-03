package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.ModernUIClient;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EffectRenderingInventoryScreen.class })
public abstract class MixinInventoryScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public MixinInventoryScreen(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public boolean isPauseScreen() {
        return ModernUIClient.sInventoryPause;
    }
}
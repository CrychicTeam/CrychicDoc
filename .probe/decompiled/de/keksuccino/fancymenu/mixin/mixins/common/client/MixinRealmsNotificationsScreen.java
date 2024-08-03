package de.keksuccino.fancymenu.mixin.mixins.common.client;

import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RealmsNotificationsScreen.class })
public class MixinRealmsNotificationsScreen {

    @Inject(method = { "<init>" }, at = { @At("TAIL") })
    private void onConstructFancyMenu(CallbackInfo info) {
        this.getRealmsScreenFancyMenu().m_6575_(Minecraft.getInstance(), 0, 0);
    }

    private RealmsNotificationsScreen getRealmsScreenFancyMenu() {
        return (RealmsNotificationsScreen) this;
    }
}
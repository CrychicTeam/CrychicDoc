package forge.me.thosea.badoptimizations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.realmsclient.client.RealmsClient;
import forge.me.thosea.badoptimizations.other.Config;
import java.io.File;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ReloadInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ Minecraft.class })
public class MixinClient_ShowInvalidConfig {

    @WrapOperation(method = { "onInitFinished" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V") })
    private void onSetScreen(Minecraft client, Screen screen, Operation<Void> original) {
        client.setScreen(this.bo$makeBOWarnScreen(() -> original.call(new Object[] { client, screen })));
    }

    @WrapOperation(method = { "onInitFinished" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/QuickPlay;startQuickPlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/RunArgs$QuickPlay;Lnet/minecraft/resource/ResourceReload;Lnet/minecraft/client/realms/RealmsClient;)V") })
    private void onStartQuickPlay(Minecraft client, GameConfig.QuickPlayData quickPlay, ReloadInstance reload, RealmsClient realms, Operation<Void> original) {
        client.setScreen(this.bo$makeBOWarnScreen(() -> original.call(new Object[] { client, quickPlay, reload, realms })));
    }

    private Screen bo$makeBOWarnScreen(Runnable onClose) {
        return new ConfirmScreen(yes -> {
            if (yes) {
                onClose.run();
            } else {
                Util.getPlatform().openFile(Config.FILE);
            }
        }, Component.empty(), Component.literal(Config.error), Component.literal("Continue"), Component.literal("Open config file")) {

            @Override
            protected void addButtons(int y) {
                super.addButtons(y);
                this.m_169253_(Button.builder(Component.literal("Open log file"), button -> Util.getPlatform().openFile(new File("./logs/latest.log"))).bounds(this.f_96543_ / 2 - 155 + 80, y + 25, 150, 20).build());
            }
        };
    }
}
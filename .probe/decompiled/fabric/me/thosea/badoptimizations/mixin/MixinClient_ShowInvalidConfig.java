package fabric.me.thosea.badoptimizations.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fabric.me.thosea.badoptimizations.other.Config;
import java.io.File;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_4011;
import net.minecraft.class_410;
import net.minecraft.class_4185;
import net.minecraft.class_4341;
import net.minecraft.class_437;
import net.minecraft.class_542.class_8495;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ class_310.class })
public class MixinClient_ShowInvalidConfig {

    @WrapOperation(method = { "onInitFinished" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V") })
    private void onSetScreen(class_310 client, class_437 screen, Operation<Void> original) {
        client.method_1507(this.bo$makeBOWarnScreen(() -> original.call(new Object[] { client, screen })));
    }

    @WrapOperation(method = { "onInitFinished" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/QuickPlay;startQuickPlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/RunArgs$QuickPlay;Lnet/minecraft/resource/ResourceReload;Lnet/minecraft/client/realms/RealmsClient;)V") })
    private void onStartQuickPlay(class_310 client, class_8495 quickPlay, class_4011 reload, class_4341 realms, Operation<Void> original) {
        client.method_1507(this.bo$makeBOWarnScreen(() -> original.call(new Object[] { client, quickPlay, reload, realms })));
    }

    private class_437 bo$makeBOWarnScreen(Runnable onClose) {
        return new class_410(yes -> {
            if (yes) {
                onClose.run();
            } else {
                class_156.method_668().method_672(Config.FILE);
            }
        }, class_2561.method_43473(), class_2561.method_43470(Config.error), class_2561.method_43470("Continue"), class_2561.method_43470("Open config file")) {

            protected void method_37051(int y) {
                super.method_37051(y);
                this.method_37052(class_4185.method_46430(class_2561.method_43470("Open log file"), button -> class_156.method_668().method_672(new File("./logs/latest.log"))).method_46434(this.field_22789 / 2 - 155 + 80, y + 25, 150, 20).method_46431());
            }
        };
    }
}
package net.zanckor.questapi.multiloader.mixin;

import net.minecraft.client.Minecraft;
import net.zanckor.questapi.CommonMain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Minecraft.class })
public class MixinMinecraft {

    @Inject(at = { @At("TAIL") }, method = { "<init>" })
    private void init(CallbackInfo info) {
        CommonMain.Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }
}
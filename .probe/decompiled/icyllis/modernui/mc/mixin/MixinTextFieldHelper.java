package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.MuiModApi;
import net.minecraft.client.gui.font.TextFieldHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ TextFieldHelper.class })
public class MixinTextFieldHelper {

    @Redirect(method = { "moveByChars(IZ)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;offsetByCodepoints(Ljava/lang/String;II)I"))
    private int onMoveByChars(String value, int cursor, int dir) {
        return MuiModApi.offsetByGrapheme(value, cursor, dir);
    }

    @Redirect(method = { "removeCharsFromCursor" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;offsetByCodepoints(Ljava/lang/String;II)I"))
    private int onRemoveCharsFromCursor(String value, int cursor, int dir) {
        return MuiModApi.offsetByGrapheme(value, cursor, dir);
    }
}
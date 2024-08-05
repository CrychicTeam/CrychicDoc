package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.MuiModApi;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ ChatFormatting.class })
public class MixinChatFormatting {

    @Overwrite
    @Nullable
    public static ChatFormatting getByCode(char formattingCode) {
        return MuiModApi.getFormattingByCode(formattingCode);
    }
}
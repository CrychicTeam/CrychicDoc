package fabric.me.thosea.badoptimizations.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fabric.me.thosea.badoptimizations.other.PlatformMethods;
import java.util.List;
import net.minecraft.class_340;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = { class_340.class }, priority = 999)
public class MixinDebugHud_AddText {

    private static final String BO$F3_TEXT = "BadOptimizations " + PlatformMethods.getVersion();

    @ModifyReturnValue(method = { "getLeftText" }, at = { @At("RETURN") })
    private List<String> addBadOptimizationsText(List<String> list) {
        list.add("");
        list.add(BO$F3_TEXT);
        return list;
    }
}
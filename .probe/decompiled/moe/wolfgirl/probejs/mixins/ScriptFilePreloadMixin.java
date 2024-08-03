package moe.wolfgirl.probejs.mixins;

import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import dev.latvian.mods.kubejs.script.ScriptSource;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.io.IOException;
import java.util.List;
import moe.wolfgirl.probejs.lang.transformer.KubeJSScript;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ScriptFileInfo.class })
public class ScriptFilePreloadMixin {

    @Shadow(remap = false)
    public String[] lines;

    @Inject(method = { "preload" }, at = { @At("RETURN") }, remap = false)
    private void probejs$$preloadFile(ScriptSource source, CallbackInfo ci) throws IOException {
        this.lines = (String[]) source.readSource((ScriptFileInfo) this).toArray(UtilsJS.EMPTY_STRING_ARRAY);
        for (int i = 0; i < this.lines.length; i++) {
            String tline = this.lines[i].trim();
            if (tline.startsWith("//")) {
                this.lines[i] = "";
            }
        }
        this.lines = new KubeJSScript(List.of(this.lines)).transform();
    }
}
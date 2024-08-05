package net.mehvahdjukaar.supplementaries.mixins;

import java.util.function.Function;
import net.mehvahdjukaar.supplementaries.api.IAntiqueTextProvider;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Font.class })
public abstract class FontRendererMixin implements IAntiqueTextProvider {

    @Unique
    private boolean supplementaries$antique = false;

    @Final
    @Shadow
    private Function<ResourceLocation, FontSet> fonts;

    @Override
    public boolean hasAntiqueInk() {
        return this.supplementaries$antique;
    }

    @Override
    public void setAntiqueInk(boolean hasInk) {
        this.supplementaries$antique = hasInk;
    }

    @Inject(method = { "getFontSet" }, at = { @At("HEAD") }, cancellable = true)
    private void getFontSet(ResourceLocation resourceLocation, CallbackInfoReturnable<FontSet> cir) {
        if (this.supplementaries$antique) {
            cir.setReturnValue((FontSet) this.fonts.apply(ModTextures.ANTIQUABLE_FONT));
        }
    }
}
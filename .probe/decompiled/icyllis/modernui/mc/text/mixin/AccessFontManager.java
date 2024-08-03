package icyllis.modernui.mc.text.mixin;

import java.util.Map;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ FontManager.class })
public interface AccessFontManager {

    @Accessor("fontSets")
    Map<ResourceLocation, FontSet> getFontSets();
}
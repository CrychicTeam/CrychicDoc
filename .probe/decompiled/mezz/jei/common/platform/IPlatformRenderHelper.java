package mezz.jei.common.platform;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IPlatformRenderHelper {

    Font getFontRenderer(Minecraft var1, ItemStack var2);

    boolean shouldRender(MobEffectInstance var1);

    TextureAtlasSprite getParticleIcon(BakedModel var1);

    ItemColors getItemColors();

    Optional<NativeImage> getMainImage(TextureAtlasSprite var1);

    void renderTooltip(Screen var1, GuiGraphics var2, List<Component> var3, Optional<TooltipComponent> var4, int var5, int var6, @Nullable Font var7, ItemStack var8);
}
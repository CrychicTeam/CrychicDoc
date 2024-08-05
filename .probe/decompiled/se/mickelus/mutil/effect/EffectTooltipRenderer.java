package se.mickelus.mutil.effect;

import com.mojang.blaze3d.platform.Window;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

public class EffectTooltipRenderer implements IClientMobEffectExtensions {

    private final Function<MobEffectInstance, String> constructEffectTooltip;

    public EffectTooltipRenderer(Function<MobEffectInstance, String> constructEffectTooltip) {
        this.constructEffectTooltip = constructEffectTooltip;
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderInventoryEffectTooltip(GuiGraphics graphics, int x, int y, Supplier<Component> tooltip) {
        Minecraft mc = Minecraft.getInstance();
        Window window = mc.getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        int mouseX = (int) (mc.mouseHandler.xpos() * (double) width / (double) window.getScreenWidth());
        int mouseY = (int) (mc.mouseHandler.ypos() * (double) height / (double) window.getScreenHeight());
        if (x < mouseX && mouseX < x + 120 && y < mouseY && mouseY < y + 32) {
            graphics.renderTooltip(mc.font, (Component) tooltip.get(), mouseX, mouseY);
        }
    }

    @Override
    public boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics graphics, int x, int y, int blitOffset) {
        renderInventoryEffectTooltip(graphics, x, y, () -> Component.literal((String) this.constructEffectTooltip.apply(instance)));
        return false;
    }
}
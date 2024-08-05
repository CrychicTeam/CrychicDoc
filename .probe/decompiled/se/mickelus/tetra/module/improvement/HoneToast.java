package se.mickelus.tetra.module.improvement;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import se.mickelus.tetra.TetraSounds;
import se.mickelus.tetra.module.schematic.SchematicRarity;

@ParametersAreNonnullByDefault
public class HoneToast implements Toast {

    private static final ResourceLocation texture = new ResourceLocation("tetra", "textures/gui/toasts.png");

    private final ItemStack itemStack;

    private boolean hasPlayedSound = false;

    public HoneToast(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent toastGui, long delta) {
        if (this.itemStack != null) {
            graphics.blit(texture, 0, 0, 0, 0, 160, 32);
            String itemName = toastGui.getMinecraft().font.plainSubstrByWidth(this.itemStack.getHoverName().getString(), 125);
            graphics.drawString(toastGui.getMinecraft().font, I18n.get("tetra.hone.available"), 30, 7, SchematicRarity.hone.tint);
            graphics.drawString(toastGui.getMinecraft().font, itemName, 30, 18, 8355711);
            graphics.renderItem(this.itemStack, 8, 8);
            graphics.renderItemDecorations(toastGui.getMinecraft().font, this.itemStack, 8, 8);
            if (!this.hasPlayedSound && delta > 0L) {
                toastGui.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(TetraSounds.honeGain, 1.0F, 1.0F));
                this.hasPlayedSound = true;
            }
            return delta > 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        } else {
            return Toast.Visibility.HIDE;
        }
    }
}
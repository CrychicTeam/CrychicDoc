package se.mickelus.tetra.module.improvement;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.TetraSounds;
import se.mickelus.tetra.blocks.workbench.gui.GuiModuleGlyph;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.schematic.SchematicRarity;

@ParametersAreNonnullByDefault
public class SettleToast implements Toast {

    private static final ResourceLocation texture = new ResourceLocation("tetra", "textures/gui/toasts.png");

    private final ItemStack itemStack;

    private final String moduleName;

    private final GuiModuleGlyph glyph;

    private boolean hasPlayedSound = false;

    public SettleToast(ItemStack itemStack, String slot) {
        this.itemStack = itemStack;
        ItemModule itemModule = (ItemModule) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).orElse(null);
        this.glyph = (GuiModuleGlyph) Optional.ofNullable(itemModule).map(module -> module.getVariantData(itemStack)).map(data -> data.glyph).map(glyphData -> new GuiModuleGlyph(0, 0, 16, 16, glyphData).setShift(false)).orElse(null);
        this.moduleName = (String) Optional.ofNullable(itemModule).map(module -> module.getName(itemStack)).orElse(slot);
    }

    @Override
    public Toast.Visibility render(GuiGraphics graphics, ToastComponent toastGui, long delta) {
        if (this.itemStack != null) {
            graphics.blit(texture, 0, 0, 0, 0, 160, 32);
            if (this.glyph != null) {
                graphics.blit(texture, 20, 14, 160, 0, 15, 15);
                this.glyph.draw(graphics, 19, 14, 260, 43, -1, -1, 1.0F);
            }
            graphics.drawString(toastGui.getMinecraft().font, I18n.get("tetra.settled.toast"), 30, 7, SchematicRarity.hone.tint);
            graphics.drawString(toastGui.getMinecraft().font, toastGui.getMinecraft().font.plainSubstrByWidth(this.moduleName, 118), 37, 18, 8355711);
            graphics.renderItem(this.itemStack, 8, 8);
            graphics.renderItemDecorations(toastGui.getMinecraft().font, this.itemStack, 8, 8);
            if (!this.hasPlayedSound && delta > 0L) {
                toastGui.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(TetraSounds.settle, 1.0F, 1.0F));
                this.hasPlayedSound = true;
            }
            return delta > 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
        } else {
            return Toast.Visibility.HIDE;
        }
    }
}
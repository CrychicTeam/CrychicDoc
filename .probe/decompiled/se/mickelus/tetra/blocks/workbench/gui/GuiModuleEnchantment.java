package se.mickelus.tetra.blocks.workbench.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.gui.GuiTextures;

public class GuiModuleEnchantment extends GuiElement {

    private final List<Component> tooltipLines;

    private Runnable hoverHandler;

    private Runnable blurHandler;

    private int color;

    private GuiTexture texture;

    public GuiModuleEnchantment(int x, int y, Enchantment enchantment, int level, int color, Runnable hoverHandler, Runnable blurHandler) {
        super(x, y, 5, 4);
        this.color = color;
        this.texture = new GuiTexture(0, 0, 5, 4, 68, 27, GuiTextures.workbench).setColor(color);
        this.addChild(this.texture);
        this.tooltipLines = new ArrayList();
        if (level < 0) {
            this.tooltipLines.add(Component.literal("-").append(TetraEnchantmentHelper.getEnchantmentName(enchantment, 0)).withStyle(ChatFormatting.DARK_RED));
        } else {
            this.tooltipLines.add(Component.literal(TetraEnchantmentHelper.getEnchantmentName(enchantment, level)));
        }
        Optional.ofNullable(TetraEnchantmentHelper.getEnchantmentDescription(enchantment)).map(description -> Component.literal(description).withStyle(ChatFormatting.DARK_GRAY)).ifPresent(this.tooltipLines::add);
        this.hoverHandler = hoverHandler;
        this.blurHandler = blurHandler;
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltipLines : null;
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        this.hoverHandler.run();
        this.texture.setColor(16777164);
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        this.blurHandler.run();
        this.texture.setColor(this.color);
    }
}
package net.blay09.mods.craftingtweaks.client;

import java.util.ArrayList;
import java.util.List;
import net.blay09.mods.balm.mixin.AbstractContainerScreenAccessor;
import net.blay09.mods.craftingtweaks.api.ButtonProperties;
import net.blay09.mods.craftingtweaks.api.ButtonStyle;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.TweakType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public abstract class GuiTweakButton extends GuiImageButton implements ITooltipProvider {

    private final AbstractContainerScreen<?> screen;

    private final CraftingGrid grid;

    private final TweakType tweak;

    private final TweakType altTweak;

    private final Tooltip normalTooltip;

    private final Tooltip altTooltip;

    private final ButtonProperties normalProperties;

    private final ButtonProperties altProperties;

    private int lastGuiLeft;

    private int lastGuiTop;

    public GuiTweakButton(@Nullable AbstractContainerScreen<?> screen, int x, int y, ButtonStyle style, CraftingGrid grid, TweakType tweak, TweakType altTweak) {
        super(x, y, style.getTweak(tweak));
        this.screen = screen;
        if (screen != null) {
            this.lastGuiLeft = ((AbstractContainerScreenAccessor) screen).getLeftPos();
            this.lastGuiTop = ((AbstractContainerScreenAccessor) screen).getTopPos();
        }
        this.grid = grid;
        this.tweak = tweak;
        this.altTweak = altTweak;
        this.normalTooltip = this.createTooltip(tweak);
        this.altTooltip = this.createTooltip(altTweak);
        this.normalProperties = this.properties;
        this.altProperties = style.getTweak(altTweak);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.m_7435_(Minecraft.getInstance().getSoundManager());
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            this.onTweakButtonClicked(player, this.screen != null ? this.screen.getMenu() : player.containerMenu, this.grid, Screen.hasShiftDown() ? this.altTweak : this.tweak);
        }
    }

    protected abstract void onTweakButtonClicked(Player var1, AbstractContainerMenu var2, CraftingGrid var3, TweakType var4);

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.m_257544_(Screen.hasShiftDown() ? this.altTooltip : this.normalTooltip);
        if (this.screen != null) {
            int guiLeft = ((AbstractContainerScreenAccessor) this.screen).getLeftPos();
            int guiTop = ((AbstractContainerScreenAccessor) this.screen).getTopPos();
            if (guiLeft != this.lastGuiLeft || guiTop != this.lastGuiTop) {
                this.m_252865_(this.m_252754_() + guiLeft - this.lastGuiLeft);
                this.m_253211_(this.m_252907_() + guiTop - this.lastGuiTop);
            }
            this.lastGuiLeft = guiLeft;
            this.lastGuiTop = guiTop;
        }
        if (Screen.hasShiftDown()) {
            this.properties = this.altProperties;
        } else {
            this.properties = this.normalProperties;
        }
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private Tooltip createTooltip(TweakType tweak) {
        return switch(tweak) {
            case Rotate, RotateCounterClockwise ->
                Tooltip.create(Component.translatable("tooltip.craftingtweaks.rotate"));
            case Clear ->
                Tooltip.create(Component.translatable("tooltip.craftingtweaks.clear"));
            case Balance ->
                Tooltip.create(Component.translatable("tooltip.craftingtweaks.balance"));
            case ForceClear ->
                Tooltip.create(Component.translatable("tooltip.craftingtweaks.forceClear").append(Component.literal("\n")).append(Component.translatable("tooltip.craftingtweaks.forceClearInfo").withStyle(ChatFormatting.GRAY)));
            case Spread ->
                Tooltip.create(Component.translatable("tooltip.craftingtweaks.spread"));
        };
    }

    @Override
    public List<Component> getTooltipComponents() {
        List<Component> tooltip = new ArrayList();
        switch(this.tweak) {
            case Rotate:
            case RotateCounterClockwise:
                tooltip.add(Component.translatable("tooltip.craftingtweaks.rotate"));
                break;
            case Clear:
                tooltip.add(Component.translatable("tooltip.craftingtweaks.clear"));
                break;
            case Balance:
                tooltip.add(Component.translatable("tooltip.craftingtweaks.balance"));
                break;
            case ForceClear:
                tooltip.add(Component.translatable("tooltip.craftingtweaks.forceClear"));
                MutableComponent forceClearInfoText = Component.translatable("tooltip.craftingtweaks.forceClearInfo");
                forceClearInfoText.withStyle(ChatFormatting.GRAY);
                tooltip.add(forceClearInfoText);
                break;
            case Spread:
                tooltip.add(Component.translatable("tooltip.craftingtweaks.spread"));
        }
        return tooltip;
    }
}
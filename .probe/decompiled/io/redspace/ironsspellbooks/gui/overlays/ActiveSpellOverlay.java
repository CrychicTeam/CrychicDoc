package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ActiveSpellOverlay implements IGuiOverlay {

    public static ActiveSpellOverlay instance = new ActiveSpellOverlay();

    protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/icons.png");

    @Override
    public void render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack stack = player.m_21205_();
            AbstractSpell spell;
            if (hasRightClickCasting(stack.getItem())) {
                if (ISpellContainer.isSpellContainer(stack)) {
                    spell = ISpellContainer.get(stack).getSpellAtIndex(0).getSpell();
                } else {
                    spell = ClientMagicData.getSpellSelectionManager().getSelectedSpellData().getSpell();
                }
            } else {
                stack = player.m_21206_();
                if (!hasRightClickCasting(stack.getItem())) {
                    return;
                }
                if (ISpellContainer.isSpellContainer(stack)) {
                    spell = ISpellContainer.get(stack).getSpellAtIndex(0).getSpell();
                } else {
                    spell = ClientMagicData.getSpellSelectionManager().getSelectedSpellData().getSpell();
                }
            }
            if (!stack.isEmpty() && spell != SpellRegistry.none()) {
                int centerX = screenWidth / 2 + 91 + 9;
                int centerY = screenHeight - 23;
                guiHelper.blit(WIDGETS_LOCATION, centerX, centerY, 24, 22, 29, 24);
                guiHelper.blit(spell.getSpellIconResource(), centerX + 3, centerY + 4, 0.0F, 0.0F, 16, 16, 16, 16);
                float f = ClientMagicData.getCooldownPercent(spell);
                if (f > 0.0F && !stack.getItem().equals(ItemRegistry.SCROLL.get())) {
                    int pixels = (int) (16.0F * f + 1.0F);
                    guiHelper.blit(TEXTURE, centerX + 3, centerY + 20 - pixels, 47, 87, 16, pixels);
                }
            }
        }
    }

    private static boolean hasRightClickCasting(Item item) {
        return item instanceof Scroll || item instanceof CastingItem;
    }

    private static void setOpaqueTexture(ResourceLocation texture) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
    }

    private static void setTranslucentTexture(ResourceLocation texture) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172649_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
    }
}
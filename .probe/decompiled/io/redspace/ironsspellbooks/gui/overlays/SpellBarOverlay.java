package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.ClientRenderCache;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SpellBarOverlay implements IGuiOverlay {

    public static final SpellBarOverlay instance = new SpellBarOverlay();

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/icons.png");

    static final int IMAGE_HEIGHT = 21;

    static final int IMAGE_WIDTH = 21;

    static final int HOTBAR_HALFWIDTH = 91;

    static final int boxSize = 20;

    static int screenHeight;

    static int screenWidth;

    static final int CONTEXTUAL_FADE_WAIT = 80;

    static int fadeoutDelay;

    static int lastTick;

    static float alpha;

    static SpellSelectionManager lastSelection;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        ManaBarOverlay.Display displayMode = ClientConfigs.SPELL_BAR_DISPLAY.get();
        if (displayMode != ManaBarOverlay.Display.Never && player != null) {
            if (displayMode == ManaBarOverlay.Display.Contextual) {
                handleFading(player);
                if (fadeoutDelay <= 0) {
                    return;
                }
            } else {
                alpha = 1.0F;
            }
            SpellSelectionManager ssm = ClientMagicData.getSpellSelectionManager();
            if (ssm != lastSelection) {
                lastSelection = ssm;
                ClientRenderCache.generateRelativeLocations(ssm, 20, 22);
                if (displayMode == ManaBarOverlay.Display.Contextual) {
                    fadeoutDelay = 80;
                }
            }
            if (ssm.getSpellCount() > 0) {
                int configOffsetY = ClientConfigs.SPELL_BAR_Y_OFFSET.get();
                int configOffsetX = ClientConfigs.SPELL_BAR_X_OFFSET.get();
                SpellBarOverlay.Anchor anchor = ClientConfigs.SPELL_BAR_ANCHOR.get();
                int centerX;
                int centerY;
                if (anchor == SpellBarOverlay.Anchor.Hotbar) {
                    centerX = screenWidth / 2 - Math.max(110, screenWidth / 4);
                    centerY = screenHeight - Math.max(55, screenHeight / 8);
                } else {
                    centerX = screenWidth * anchor.m1;
                    centerY = screenHeight * anchor.m2;
                }
                centerX += configOffsetX;
                centerY += configOffsetY;
                List<SpellData> spells = ssm.getAllSpells().stream().map(slot -> slot.spellData).toList();
                int spellbookCount = ssm.getSpellsForSlot(Curios.SPELLBOOK_SLOT).size();
                List<Vec2> locations = ClientRenderCache.relativeSpellBarSlotLocations;
                int approximateWidth = locations.size() / 3;
                centerX -= approximateWidth * 5;
                int selectedSpellIndex = ssm.getGlobalSelectionIndex();
                for (Vec2 location : locations) {
                    guiHelper.blit(TEXTURE, centerX + (int) location.x, centerY + (int) location.y, 66, 84, 22, 22);
                }
                for (int i = 0; i < locations.size(); i++) {
                    guiHelper.blit(((SpellData) spells.get(i)).getSpell().getSpellIconResource(), centerX + (int) ((Vec2) locations.get(i)).x + 3, centerY + (int) ((Vec2) locations.get(i)).y + 3, 0.0F, 0.0F, 16, 16, 16, 16);
                }
                for (int i = 0; i < locations.size(); i++) {
                    setTranslucentTexture(TEXTURE);
                    if (i != selectedSpellIndex) {
                        guiHelper.blit(TEXTURE, centerX + (int) ((Vec2) locations.get(i)).x, centerY + (int) ((Vec2) locations.get(i)).y, 22 + (!((SpellSelectionManager.SelectionOption) ssm.getAllSpells().get(i)).slot.equals(Curios.SPELLBOOK_SLOT) ? 110 : 0), 84, 22, 22);
                    }
                    float f = ClientMagicData.getCooldownPercent(((SpellData) spells.get(i)).getSpell());
                    if (f > 0.0F) {
                        int pixels = (int) (16.0F * f + 1.0F);
                        guiHelper.blit(TEXTURE, centerX + (int) ((Vec2) locations.get(i)).x + 3, centerY + (int) ((Vec2) locations.get(i)).y + 19 - pixels, 47, 87, 16, pixels);
                    }
                }
                for (int i = 0; i < locations.size(); i++) {
                    if (i == selectedSpellIndex) {
                        guiHelper.blit(TEXTURE, centerX + (int) ((Vec2) locations.get(i)).x, centerY + (int) ((Vec2) locations.get(i)).y, 0, 84, 22, 22);
                    }
                }
            }
        }
    }

    private static void handleFading(Player player) {
        if (lastTick != player.f_19797_) {
            lastTick = player.f_19797_;
            if (ClientMagicData.isCasting() || ClientMagicData.getCooldowns().hasCooldownsActive() || ClientMagicData.getRecasts().hasRecastsActive()) {
                fadeoutDelay = 80;
            }
            if (fadeoutDelay > 0) {
                fadeoutDelay--;
            }
        }
        alpha = Mth.clamp((float) fadeoutDelay / 20.0F, 0.0F, 1.0F);
        if (fadeoutDelay > 0) {
            ;
        }
    }

    private static void setTranslucentTexture(ResourceLocation texture) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172649_);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.setShaderTexture(0, texture);
    }

    public static enum Anchor {

        Hotbar(0, 0), TopLeft(0, 0), TopRight(0, 1), BottomLeft(0, 1), BottomRight(1, 1);

        final int m1;

        final int m2;

        private Anchor(int mx, int my) {
            this.m1 = mx;
            this.m2 = my;
        }
    }
}
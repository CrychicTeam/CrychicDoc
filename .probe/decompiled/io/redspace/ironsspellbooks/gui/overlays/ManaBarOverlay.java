package io.redspace.ironsspellbooks.gui.overlays;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.item.CastingItem;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ManaBarOverlay implements IGuiOverlay {

    public static final ManaBarOverlay instance = new ManaBarOverlay();

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/icons.png");

    static final int DEFAULT_IMAGE_WIDTH = 98;

    static final int XP_IMAGE_WIDTH = 188;

    static final int IMAGE_HEIGHT = 21;

    static final int HOTBAR_HEIGHT = 25;

    static final int ICON_ROW_HEIGHT = 11;

    static final int CHAR_WIDTH = 6;

    static final int HUNGER_BAR_OFFSET = 50;

    static final int SCREEN_BORDER_MARGIN = 20;

    static final int TEXT_COLOR = ChatFormatting.AQUA.getColor();

    @Override
    public void render(ForgeGui gui, GuiGraphics guiHelper, float partialTick, int screenWidth, int screenHeight) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (shouldShowManaBar(player)) {
            int maxMana = (int) player.m_21133_(AttributeRegistry.MAX_MANA.get());
            int mana = ClientMagicData.getPlayerMana();
            int configOffsetY = ClientConfigs.MANA_BAR_Y_OFFSET.get();
            int configOffsetX = ClientConfigs.MANA_BAR_X_OFFSET.get();
            ManaBarOverlay.Anchor anchor = ClientConfigs.MANA_BAR_ANCHOR.get();
            if (anchor != ManaBarOverlay.Anchor.XP || !(player.getJumpRidingScale() > 0.0F)) {
                int barX = getBarX(anchor, screenWidth) + configOffsetX;
                int barY = getBarY(anchor, screenHeight, gui) - configOffsetY;
                int imageWidth = anchor == ManaBarOverlay.Anchor.XP ? 188 : 98;
                int spriteX = anchor == ManaBarOverlay.Anchor.XP ? 68 : 0;
                int spriteY = anchor == ManaBarOverlay.Anchor.XP ? 40 : 0;
                guiHelper.blit(TEXTURE, barX, barY, (float) spriteX, (float) spriteY, imageWidth, 21, 256, 256);
                guiHelper.blit(TEXTURE, barX, barY, spriteX, spriteY + 21, (int) ((double) imageWidth * Math.min((double) mana / (double) maxMana, 1.0)), 21);
                String manaFraction = mana + "/" + maxMana;
                int textX = ClientConfigs.MANA_TEXT_X_OFFSET.get() + barX + imageWidth / 2 - (int) (((double) (mana + "").length() + 0.5) * 6.0);
                int textY = ClientConfigs.MANA_TEXT_Y_OFFSET.get() + barY + (anchor == ManaBarOverlay.Anchor.XP ? 3 : 11);
                if (ClientConfigs.MANA_BAR_TEXT_VISIBLE.get()) {
                    guiHelper.drawString(gui.m_93082_(), manaFraction, textX, textY, TEXT_COLOR);
                }
            }
        }
    }

    public static boolean shouldShowManaBar(Player player) {
        ManaBarOverlay.Display display = ClientConfigs.MANA_BAR_DISPLAY.get();
        return !player.isSpectator() && display != ManaBarOverlay.Display.Never && (display == ManaBarOverlay.Display.Always || player.m_21093_(itemStack -> itemStack.getItem() instanceof CastingItem || ISpellContainer.isSpellContainer(itemStack) && !ISpellContainer.get(itemStack).mustEquip()) || (double) ClientMagicData.getPlayerMana() < player.m_21133_(AttributeRegistry.MAX_MANA.get()));
    }

    private static int getBarX(ManaBarOverlay.Anchor anchor, int screenWidth) {
        if (anchor == ManaBarOverlay.Anchor.XP) {
            return screenWidth / 2 - 91 - 3;
        } else if (anchor == ManaBarOverlay.Anchor.Hunger || anchor == ManaBarOverlay.Anchor.Center) {
            return screenWidth / 2 - 49 + (anchor == ManaBarOverlay.Anchor.Center ? 0 : 50);
        } else {
            return anchor != ManaBarOverlay.Anchor.TopLeft && anchor != ManaBarOverlay.Anchor.BottomLeft ? screenWidth - 20 - 98 : 20;
        }
    }

    private static int getBarY(ManaBarOverlay.Anchor anchor, int screenHeight, ForgeGui gui) {
        if (anchor == ManaBarOverlay.Anchor.XP) {
            return screenHeight - 32 + 3 - 8;
        } else if (anchor == ManaBarOverlay.Anchor.Hunger) {
            return screenHeight - (getAndIncrementRightHeight(gui) - 2) - 10;
        } else if (anchor == ManaBarOverlay.Anchor.Center) {
            return screenHeight - 25 - 27 - 10;
        } else {
            return anchor != ManaBarOverlay.Anchor.TopLeft && anchor != ManaBarOverlay.Anchor.TopRight ? screenHeight - 20 - 21 : 20;
        }
    }

    private static int getAndIncrementRightHeight(ForgeGui gui) {
        int x = gui.rightHeight;
        gui.rightHeight += 10;
        return x;
    }

    public static enum Anchor {

        Hunger,
        XP,
        Center,
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight
    }

    public static enum Display {

        Never, Always, Contextual
    }
}
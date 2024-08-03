package net.minecraftforge.forge.snapshots;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.VersionChecker.Status;

public class ForgeSnapshotsModClient {

    public static void renderMainMenuWarning(Status status, TitleScreen gui, GuiGraphics graphics, Font font, int width, int height, int alpha) {
        if (status == Status.BETA || status == Status.BETA_OUTDATED) {
            Component line = Component.translatable("forge.update.beta.1", ChatFormatting.RED, ChatFormatting.RESET).withStyle(ChatFormatting.RED);
            graphics.drawCenteredString(font, line, width / 2, 4 + 0 * (9 + 1), 16777215 | alpha);
            line = Component.translatable("forge.update.beta.2");
            graphics.drawCenteredString(font, line, width / 2, 4 + 1 * (9 + 1), 16777215 | alpha);
        }
    }
}
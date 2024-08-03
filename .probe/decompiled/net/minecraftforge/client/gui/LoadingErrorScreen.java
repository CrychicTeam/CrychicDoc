package net.minecraftforge.client.gui;

import com.google.common.base.Strings;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.ErrorScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import net.minecraftforge.common.ForgeI18n;
import net.minecraftforge.fml.LoadingFailedException;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadingErrorScreen extends ErrorScreen {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Path modsDir;

    private final Path logFile;

    private final List<ModLoadingException> modLoadErrors;

    private final List<ModLoadingWarning> modLoadWarnings;

    private final Path dumpedLocation;

    private LoadingErrorScreen.LoadingEntryList entryList;

    private Component errorHeader;

    private Component warningHeader;

    public LoadingErrorScreen(LoadingFailedException loadingException, List<ModLoadingWarning> warnings, File dumpedLocation) {
        super(Component.literal("Loading Error"), null);
        this.modLoadWarnings = warnings;
        this.modLoadErrors = loadingException == null ? Collections.emptyList() : loadingException.getErrors();
        this.modsDir = FMLPaths.MODSDIR.get();
        this.logFile = FMLPaths.GAMEDIR.get().resolve(Paths.get("logs", "latest.log"));
        this.dumpedLocation = dumpedLocation != null ? dumpedLocation.toPath() : null;
    }

    @Override
    public void init() {
        super.init();
        this.m_169413_();
        this.errorHeader = Component.literal(ChatFormatting.RED + ForgeI18n.parseMessage("fml.loadingerrorscreen.errorheader", this.modLoadErrors.size()) + ChatFormatting.RESET);
        this.warningHeader = Component.literal(ChatFormatting.YELLOW + ForgeI18n.parseMessage("fml.loadingerrorscreen.warningheader", this.modLoadErrors.size()) + ChatFormatting.RESET);
        int yOffset = 46;
        this.m_142416_(new ExtendedButton(50, this.f_96544_ - yOffset, this.f_96543_ / 2 - 55, 20, Component.literal(ForgeI18n.parseMessage("fml.button.open.mods.folder")), b -> Util.getPlatform().openFile(this.modsDir.toFile())));
        this.m_142416_(new ExtendedButton(this.f_96543_ / 2 + 5, this.f_96544_ - yOffset, this.f_96543_ / 2 - 55, 20, Component.literal(ForgeI18n.parseMessage("fml.button.open.file", this.logFile.getFileName())), b -> Util.getPlatform().openFile(this.logFile.toFile())));
        if (this.modLoadErrors.isEmpty()) {
            this.m_142416_(new ExtendedButton(this.f_96543_ / 4, this.f_96544_ - 24, this.f_96543_ / 2, 20, Component.literal(ForgeI18n.parseMessage("fml.button.continue.launch")), b -> this.f_96541_.setScreen(null)));
        } else {
            this.m_142416_(new ExtendedButton(this.f_96543_ / 4, this.f_96544_ - 24, this.f_96543_ / 2, 20, Component.literal(ForgeI18n.parseMessage("fml.button.open.file", this.dumpedLocation.getFileName())), b -> Util.getPlatform().openFile(this.dumpedLocation.toFile())));
        }
        this.entryList = new LoadingErrorScreen.LoadingEntryList(this, this.modLoadErrors, this.modLoadWarnings);
        this.m_7787_(this.entryList);
        this.m_7522_(this.entryList);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.m_280273_(guiGraphics);
        this.entryList.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        this.drawMultiLineCenteredString(guiGraphics, this.f_96547_, this.modLoadErrors.isEmpty() ? this.warningHeader : this.errorHeader, this.f_96543_ / 2, 10);
        this.f_169369_.forEach(button -> button.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    private void drawMultiLineCenteredString(GuiGraphics guiGraphics, Font fr, Component str, int x, int y) {
        for (FormattedCharSequence s : fr.split(str, this.f_96543_)) {
            guiGraphics.drawString(fr, s, (float) ((double) x - (double) fr.width(s) / 2.0), (float) y, 16777215, true);
            y += 9;
        }
    }

    public static class LoadingEntryList extends ObjectSelectionList<LoadingErrorScreen.LoadingEntryList.LoadingMessageEntry> {

        LoadingEntryList(LoadingErrorScreen parent, List<ModLoadingException> errors, List<ModLoadingWarning> warnings) {
            super((Minecraft) Objects.requireNonNull(parent.f_96541_), parent.f_96543_, parent.f_96544_, 35, parent.f_96544_ - 50, Math.max(errors.stream().mapToInt(error -> parent.f_96547_.split(Component.literal(error.getMessage() != null ? error.getMessage() : ""), parent.f_96543_ - 20).size()).max().orElse(0), warnings.stream().mapToInt(warning -> parent.f_96547_.split(Component.literal(warning.formatToString() != null ? warning.formatToString() : ""), parent.f_96543_ - 20).size()).max().orElse(0)) * 9 + 8);
            boolean both = !errors.isEmpty() && !warnings.isEmpty();
            if (both) {
                this.m_7085_(new LoadingErrorScreen.LoadingEntryList.LoadingMessageEntry(parent.errorHeader, true));
            }
            errors.forEach(e -> this.m_7085_(new LoadingErrorScreen.LoadingEntryList.LoadingMessageEntry(Component.literal(e.formatToString()))));
            if (both) {
                int maxChars = (this.f_93388_ - 10) / parent.f_96541_.font.width("-");
                this.m_7085_(new LoadingErrorScreen.LoadingEntryList.LoadingMessageEntry(Component.literal("\n" + Strings.repeat("-", maxChars) + "\n")));
                this.m_7085_(new LoadingErrorScreen.LoadingEntryList.LoadingMessageEntry(parent.warningHeader, true));
            }
            warnings.forEach(w -> this.m_7085_(new LoadingErrorScreen.LoadingEntryList.LoadingMessageEntry(Component.literal(w.formatToString()))));
        }

        @Override
        protected int getScrollbarPosition() {
            return this.getRight() - 6;
        }

        @Override
        public int getRowWidth() {
            return this.f_93388_;
        }

        public class LoadingMessageEntry extends ObjectSelectionList.Entry<LoadingErrorScreen.LoadingEntryList.LoadingMessageEntry> {

            private final Component message;

            private final boolean center;

            LoadingMessageEntry(Component message) {
                this(message, false);
            }

            LoadingMessageEntry(Component message, boolean center) {
                this.message = (Component) Objects.requireNonNull(message);
                this.center = center;
            }

            @Override
            public Component getNarration() {
                return Component.translatable("narrator.select", this.message);
            }

            @Override
            public void render(GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTick) {
                Font font = Minecraft.getInstance().font;
                List<FormattedCharSequence> strings = font.split(this.message, LoadingEntryList.this.f_93388_ - 20);
                int y = top + 2;
                for (FormattedCharSequence string : strings) {
                    if (this.center) {
                        guiGraphics.drawString(font, string, (float) left + (float) LoadingEntryList.this.f_93388_ / 2.0F - (float) font.width(string) / 2.0F, (float) y, 16777215, false);
                    } else {
                        guiGraphics.drawString(font, string, left + 5, y, 16777215, false);
                    }
                    y += 9;
                }
            }
        }
    }
}
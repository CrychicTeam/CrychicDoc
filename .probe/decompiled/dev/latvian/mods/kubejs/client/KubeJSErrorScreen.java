package dev.latvian.mods.kubejs.client;

import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.script.ConsoleLine;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.LogType;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class KubeJSErrorScreen extends Screen {

    public final Screen lastScreen;

    public final ScriptType scriptType;

    public final Path logFile;

    public final List<ConsoleLine> errors;

    public final List<ConsoleLine> warnings;

    public List<ConsoleLine> viewing;

    private KubeJSErrorScreen.ErrorList list;

    public KubeJSErrorScreen(Screen lastScreen, ScriptType scriptType, @Nullable Path logFile, List<ConsoleLine> errors, List<ConsoleLine> warnings) {
        super(Component.empty());
        this.lastScreen = lastScreen;
        this.scriptType = scriptType;
        this.logFile = logFile;
        this.errors = errors;
        this.warnings = warnings;
        this.viewing = errors.isEmpty() && !warnings.isEmpty() ? warnings : errors;
    }

    public KubeJSErrorScreen(Screen lastScreen, ConsoleJS console) {
        this(lastScreen, console.scriptType, console.scriptType.getLogFile(), new ArrayList(console.errors), new ArrayList(console.warnings));
    }

    @Override
    public Component getNarrationMessage() {
        return Component.literal("There were KubeJS " + this.scriptType.name + " errors!");
    }

    @Override
    protected void init() {
        super.init();
        this.list = new KubeJSErrorScreen.ErrorList(this, this.f_96541_, this.f_96543_, this.f_96544_, 32, this.f_96544_ - 32, this.viewing);
        this.m_7787_(this.list);
        int i = this.f_96544_ - 26;
        Button openLog;
        if (CommonProperties.get().startupErrorReportUrl.isBlank()) {
            openLog = (Button) this.m_142416_(Button.builder(Component.literal("Open Log File"), this::openLog).bounds(this.f_96543_ / 2 - 155, i, 150, 20).build());
            this.m_142416_(Button.builder(Component.literal(this.scriptType.isStartup() ? "Quit" : "Close"), this::quit).bounds(this.f_96543_ / 2 - 155 + 160, i, 150, 20).build());
        } else {
            openLog = (Button) this.m_142416_(Button.builder(Component.literal("Open Log File"), this::openLog).bounds(this.f_96543_ / 4 - 55, i, 100, 20).build());
            this.m_142416_(Button.builder(Component.literal("Report"), this::report).bounds(this.f_96543_ / 2 - 50, i, 100, 20).build());
            this.m_142416_(Button.builder(Component.literal(this.scriptType.isStartup() ? "Quit" : "Close"), this::quit).bounds(this.f_96543_ * 3 / 4 - 45, i, 100, 20).build());
        }
        openLog.f_93623_ = this.logFile != null;
        Button viewOther = (Button) this.m_142416_(Button.builder(Component.literal(this.viewing == this.errors ? "View Warnings [" + this.warnings.size() + "]" : "View Errors [" + this.errors.size() + "]"), this::viewOther).bounds(this.f_96543_ - 107, 7, 100, 20).build());
        if (this.errors.isEmpty() || this.warnings.isEmpty()) {
            viewOther.f_93623_ = false;
        }
    }

    private void quit(Button button) {
        if (this.scriptType.isStartup()) {
            this.f_96541_.stop();
        } else {
            this.onClose();
        }
    }

    private void report(Button button) {
        this.m_5561_(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, CommonProperties.get().startupErrorReportUrl)));
    }

    private void openLog(Button button) {
        if (this.logFile != null) {
            this.m_5561_(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, this.logFile.toAbsolutePath().toString())));
        }
    }

    private void viewOther(Button button) {
        this.viewing = this.viewing == this.errors ? this.warnings : this.errors;
        this.m_267719_();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mx, int my, float delta) {
        this.m_280273_(guiGraphics);
        this.list.m_88315_(guiGraphics, mx, my, delta);
        guiGraphics.drawCenteredString(this.f_96547_, "KubeJS " + this.scriptType.name + " script " + (this.viewing == this.errors ? "errors" : "warnings"), this.f_96543_ / 2, 12, 16777215);
        super.render(guiGraphics, mx, my, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !this.scriptType.isStartup();
    }

    @Override
    public void onClose() {
        this.f_96541_.setScreen(this.lastScreen);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Entry extends ObjectSelectionList.Entry<KubeJSErrorScreen.Entry> {

        private final KubeJSErrorScreen.ErrorList errorList;

        private final Minecraft minecraft;

        private final ConsoleLine line;

        private long lastClickTime;

        private final FormattedCharSequence indexText;

        private final FormattedCharSequence scriptLineText;

        private final FormattedCharSequence timestampText;

        private final List<FormattedCharSequence> errorText;

        private final List<FormattedCharSequence> stackTraceText;

        public Entry(KubeJSErrorScreen.ErrorList errorList, Minecraft minecraft, int index, ConsoleLine line, Calendar calendar) {
            this.errorList = errorList;
            this.minecraft = minecraft;
            this.line = line;
            this.indexText = Component.literal("#" + (index + 1)).getVisualOrderText();
            ArrayList<ConsoleLine.SourceLine> sourceLines = new ArrayList(line.sourceLines);
            Collections.reverse(sourceLines);
            this.scriptLineText = Component.literal((String) sourceLines.stream().map(Object::toString).map(s -> s.isEmpty() ? (this.line.type == LogType.WARN ? "Internal Warning" : "Internal Error") : s).collect(Collectors.joining(" -> "))).getVisualOrderText();
            StringBuilder sb = new StringBuilder();
            calendar.setTimeInMillis(line.timestamp);
            UtilsJS.appendTimestamp(sb, calendar);
            this.timestampText = Component.literal(sb.toString()).getVisualOrderText();
            this.errorText = new ArrayList(minecraft.font.split(Component.literal(line.message), errorList.getRowWidth()).stream().limit(3L).toList());
            this.stackTraceText = line.stackTrace.isEmpty() ? List.of() : minecraft.font.split(Component.literal(String.join("\n", line.stackTrace)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)), Integer.MAX_VALUE);
        }

        @Override
        public Component getNarration() {
            return Component.empty();
        }

        @Override
        public void render(GuiGraphics g, int idx, int y, int x, int w, int h, int mx, int my, boolean hovered, float delta) {
            int col = this.line.type == LogType.ERROR ? 16735075 : 16759643;
            g.drawString(this.minecraft.font, this.indexText, x + 1, y + 1, col);
            g.drawCenteredString(this.minecraft.font, this.scriptLineText, x + w / 2, y + 1, 16777215);
            g.drawString(this.minecraft.font, this.timestampText, x + w - this.minecraft.font.width(this.timestampText) - 4, y + 1, 6710886);
            for (int i = 0; i < this.errorText.size(); i++) {
                g.drawString(this.minecraft.font, (FormattedCharSequence) this.errorText.get(i), x + 1, y + 13 + i * 10, col);
            }
            if (hovered && !this.stackTraceText.isEmpty()) {
                this.errorList.screen.m_257959_(Screen.hasShiftDown() ? this.stackTraceText : this.stackTraceText.stream().limit(4L).toList());
            }
        }

        @Override
        public boolean mouseClicked(double d, double e, int i) {
            this.errorList.m_6987_(this);
            if (Util.getMillis() - this.lastClickTime < 250L) {
                if (i == 1) {
                    this.minecraft.keyboardHandler.setClipboard(String.join("\n", this.line.stackTrace));
                } else {
                    this.open();
                }
                return true;
            } else {
                this.lastClickTime = Util.getMillis();
                return true;
            }
        }

        public void open() {
            Path path = this.line.externalFile == null ? (!this.line.sourceLines.isEmpty() && !((ConsoleLine.SourceLine) this.line.sourceLines.iterator().next()).source().isEmpty() ? this.line.console.scriptType.path.resolve(((ConsoleLine.SourceLine) this.line.sourceLines.iterator().next()).source()) : null) : this.line.externalFile;
            if (path != null && Files.exists(path, new LinkOption[0])) {
                try {
                    if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Action.BROWSE_FILE_DIR)) {
                        throw new IllegalStateException("Error");
                    }
                    Desktop.getDesktop().browseFileDirectory(path.toFile());
                } catch (Exception var3) {
                    if (Files.isRegularFile(path, new LinkOption[0]) && !path.getFileName().toString().endsWith(".js")) {
                        path = path.getParent();
                    }
                    this.errorList.screen.m_5561_(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, path.toAbsolutePath().toString())));
                }
            }
        }
    }

    public static class ErrorList extends ObjectSelectionList<KubeJSErrorScreen.Entry> {

        public final KubeJSErrorScreen screen;

        public final List<ConsoleLine> lines;

        public ErrorList(KubeJSErrorScreen screen, Minecraft minecraft, int x1, int height, int y0, int y1, List<ConsoleLine> lines) {
            super(minecraft, x1, height, y0, y1, 48);
            this.screen = screen;
            this.lines = lines;
            this.m_93488_(false);
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < lines.size(); i++) {
                this.m_7085_(new KubeJSErrorScreen.Entry(this, minecraft, i, (ConsoleLine) lines.get(i), calendar));
            }
        }

        @Override
        public boolean keyPressed(int i, int j, int k) {
            if (CommonInputs.selected(i)) {
                KubeJSErrorScreen.Entry sel = (KubeJSErrorScreen.Entry) this.m_93511_();
                if (sel != null) {
                    sel.open();
                    return true;
                }
            }
            return super.m_7933_(i, j, k);
        }

        @Override
        public int getRowWidth() {
            return (int) ((double) this.f_93388_ * 0.93);
        }
    }
}
package noppes.npcs.shared.client.gui.components;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import noppes.npcs.CustomNpcs;
import noppes.npcs.mixin.MouseHelperMixin;
import noppes.npcs.shared.client.gui.listeners.IGui;
import noppes.npcs.shared.client.gui.listeners.ITextChangeListener;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import noppes.npcs.shared.client.util.TrueTypeFont;

public class GuiTextArea extends AbstractWidget implements IGui, GuiEventListener {

    public int id;

    public int x;

    public int y;

    public int width;

    public int height;

    private int cursorCounter;

    private ITextChangeListener listener;

    private static TrueTypeFont font = new TrueTypeFont(new Font("Arial Unicode MS", 0, CustomNpcs.FontSize), 1.0F);

    public String text = null;

    private TextContainer container = null;

    public boolean active = false;

    public boolean enabled = true;

    public boolean visible = true;

    public boolean clicked = false;

    public boolean doubleClicked = false;

    public boolean clickScrolling = false;

    private int startSelection;

    private int endSelection;

    private int cursorPosition;

    private int scrolledLine = 0;

    private boolean enableCodeHighlighting = false;

    private static final char colorChar = '\uffff';

    public List<GuiTextArea.UndoData> undoList = new ArrayList();

    public List<GuiTextArea.UndoData> redoList = new ArrayList();

    public boolean undoing = false;

    private long lastClicked = 0L;

    public GuiTextArea(int id, int x, int y, int width, int height, String text) {
        super(x, y, width, height, Component.literal(text));
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.undoing = true;
        this.setText(text);
        this.undoing = false;
        font.setSpecial('\uffff');
    }

    @Override
    public void render(GuiGraphics graphics, int xMouse, int yMouse) {
        if (this.visible) {
            graphics.fill(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
            graphics.fill(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
            this.container.visibleLines = this.height / this.container.lineHeight;
            if (this.clicked) {
                this.clicked = ((MouseHelperMixin) Minecraft.getInstance().mouseHandler).getActiveButton() == 0;
                int i = this.getSelectionPos((double) xMouse, (double) yMouse);
                if (i != this.cursorPosition) {
                    if (this.doubleClicked) {
                        this.startSelection = this.endSelection = this.cursorPosition;
                        this.doubleClicked = false;
                    }
                    this.setCursor(i, true);
                }
            } else if (this.doubleClicked) {
                this.doubleClicked = false;
            }
            if (this.clickScrolling) {
                this.clickScrolling = ((MouseHelperMixin) Minecraft.getInstance().mouseHandler).getActiveButton() == 0;
                int diff = this.container.linesCount - this.container.visibleLines;
                this.scrolledLine = Math.min(Math.max((int) (1.0F * (float) diff * (float) (yMouse - this.y) / (float) this.height), 0), diff);
            }
            int startBracket = 0;
            int endBracket = 0;
            if (this.endSelection - this.startSelection == 1 || this.startSelection == this.endSelection && this.startSelection < this.text.length()) {
                char c = this.text.charAt(this.startSelection);
                int found = 0;
                if (c == '{') {
                    found = this.findClosingBracket(this.text.substring(this.startSelection), '{', '}');
                } else if (c == '[') {
                    found = this.findClosingBracket(this.text.substring(this.startSelection), '[', ']');
                } else if (c == '(') {
                    found = this.findClosingBracket(this.text.substring(this.startSelection), '(', ')');
                } else if (c == '}') {
                    found = this.findOpeningBracket(this.text.substring(0, this.startSelection + 1), '{', '}');
                } else if (c == ']') {
                    found = this.findOpeningBracket(this.text.substring(0, this.startSelection + 1), '[', ']');
                } else if (c == ')') {
                    found = this.findOpeningBracket(this.text.substring(0, this.startSelection + 1), '(', ')');
                }
                if (found != 0) {
                    startBracket = this.startSelection;
                    endBracket = this.startSelection + found;
                }
            }
            List<TextContainer.LineData> list = new ArrayList(this.container.lines);
            String wordHightLight = null;
            if (this.startSelection != this.endSelection) {
                Matcher m = this.container.regexWord.matcher(this.text);
                while (m.find()) {
                    if (m.start() == this.startSelection && m.end() == this.endSelection) {
                        wordHightLight = this.text.substring(this.startSelection, this.endSelection);
                    }
                }
            }
            for (int i = 0; i < list.size(); i++) {
                TextContainer.LineData data = (TextContainer.LineData) list.get(i);
                String line = data.text;
                int w = line.length();
                if (startBracket != endBracket) {
                    if (startBracket >= data.start && startBracket < data.end) {
                        int s = font.width(line.substring(0, startBracket - data.start));
                        int e = font.width(line.substring(0, startBracket - data.start + 1)) + 1;
                        int posY = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                        graphics.fill(this.x + 1 + s, posY, this.x + 1 + e, posY + this.container.lineHeight + 1, -1728001024);
                    }
                    if (endBracket >= data.start && endBracket < data.end) {
                        int s = font.width(line.substring(0, endBracket - data.start));
                        int e = font.width(line.substring(0, endBracket - data.start + 1)) + 1;
                        int posY = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                        graphics.fill(this.x + 1 + s, posY, this.x + 1 + e, posY + this.container.lineHeight + 1, -1728001024);
                    }
                }
                if (i >= this.scrolledLine && i < this.scrolledLine + this.container.visibleLines) {
                    if (wordHightLight != null) {
                        Matcher m = this.container.regexWord.matcher(line);
                        while (m.find()) {
                            if (line.substring(m.start(), m.end()).equals(wordHightLight)) {
                                int s = font.width(line.substring(0, m.start()));
                                int e = font.width(line.substring(0, m.end())) + 1;
                                int posY = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                                graphics.fill(this.x + 1 + s, posY, this.x + 1 + e, posY + this.container.lineHeight + 1, -1728033792);
                            }
                        }
                    }
                    if (this.startSelection != this.endSelection && this.endSelection > data.start && this.startSelection <= data.end && this.startSelection < data.end) {
                        int s = font.width(line.substring(0, Math.max(this.startSelection - data.start, 0)));
                        int e = font.width(line.substring(0, Math.min(this.endSelection - data.start, w))) + 1;
                        int posY = this.y + 1 + (i - this.scrolledLine) * this.container.lineHeight;
                        graphics.fill(this.x + 1 + s, posY, this.x + 1 + e, posY + this.container.lineHeight + 1, -1728052993);
                    }
                    int yPos = this.y + (i - this.scrolledLine) * this.container.lineHeight + 1;
                    font.draw(graphics.pose(), data.getFormattedString(), (float) (this.x + 1), (float) yPos, -2039584);
                    if (this.active && this.isEnabled() && this.cursorCounter / 6 % 2 == 0 && this.cursorPosition >= data.start && this.cursorPosition < data.end) {
                        int posX = this.x + font.width(line.substring(0, this.cursorPosition - data.start));
                        graphics.fill(posX + 1, yPos, posX + 2, yPos + 1 + this.container.lineHeight, -3092272);
                    }
                }
            }
            if (this.hasVerticalScrollbar()) {
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, GuiCustomScrollNop.resource);
                int sbSize = Math.max((int) (1.0F * (float) this.container.visibleLines / (float) this.container.linesCount * (float) this.height), 2);
                int posX = this.x + this.width - 6;
                int posY = (int) ((float) this.y + 1.0F * (float) this.scrolledLine / (float) this.container.linesCount * (float) (this.height - 4)) + 1;
                graphics.fill(posX, posY, posX + 5, posY + sbSize, -2039584);
            }
        }
    }

    private int findClosingBracket(String str, char s, char e) {
        int found = 0;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == s) {
                found++;
            } else if (c == e) {
                if (--found == 0) {
                    return i;
                }
            }
        }
        return 0;
    }

    private int findOpeningBracket(String str, char s, char e) {
        int found = 0;
        char[] chars = str.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            char c = chars[i];
            if (c == e) {
                found++;
            } else if (c == s) {
                if (--found == 0) {
                    return i - chars.length + 1;
                }
            }
        }
        return 0;
    }

    private int getSelectionPos(double xMouse, double yMouse) {
        xMouse -= (double) (this.x + 1);
        yMouse -= (double) (this.y + 1);
        List<TextContainer.LineData> list = new ArrayList(this.container.lines);
        for (int i = 0; i < list.size(); i++) {
            TextContainer.LineData data = (TextContainer.LineData) list.get(i);
            if (i >= this.scrolledLine && i < this.scrolledLine + this.container.visibleLines) {
                int yPos = (i - this.scrolledLine) * this.container.lineHeight;
                if (yMouse >= (double) yPos && yMouse < (double) (yPos + this.container.lineHeight)) {
                    int lineWidth = 0;
                    char[] chars = data.text.toCharArray();
                    for (int j = 1; j <= chars.length; j++) {
                        int w = font.width(data.text.substring(0, j));
                        if (xMouse < (double) (lineWidth + (w - lineWidth) / 2)) {
                            return data.start + j - 1;
                        }
                        lineWidth = w;
                    }
                    return data.end - 1;
                }
            }
        }
        return this.container.text.length();
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public boolean charTyped(char c, int i) {
        if (!this.active) {
            return false;
        } else if (!this.isEnabled()) {
            return false;
        } else {
            if (SharedConstants.isAllowedChatCharacter(c)) {
                this.addText(Character.toString(c));
            }
            return true;
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.active) {
            return false;
        } else if (Screen.isSelectAll(keyCode)) {
            this.startSelection = this.cursorPosition = 0;
            this.endSelection = this.text.length();
            return true;
        } else if (keyCode == 263) {
            int j = 1;
            if (Screen.hasControlDown()) {
                Matcher m = this.container.regexWord.matcher(this.text.substring(0, this.cursorPosition));
                while (m.find()) {
                    if (m.start() != m.end()) {
                        j = this.cursorPosition - m.start();
                    }
                }
            }
            this.setCursor(this.cursorPosition - j, Screen.hasShiftDown());
            return true;
        } else if (keyCode == 262) {
            int j = 1;
            if (Screen.hasControlDown()) {
                Matcher m = this.container.regexWord.matcher(this.text.substring(this.cursorPosition));
                if (m.find() && m.start() > 0 || m.find()) {
                    j = m.start();
                }
            }
            this.setCursor(this.cursorPosition + j, Screen.hasShiftDown());
            return true;
        } else if (keyCode == InputConstants.getKey("key.keyboard.up").getValue()) {
            this.setCursor(this.cursorUp(), Screen.hasShiftDown());
            return true;
        } else if (keyCode == InputConstants.getKey("key.keyboard.down").getValue()) {
            this.setCursor(this.cursorDown(), Screen.hasShiftDown());
            return true;
        } else if (keyCode == 261) {
            String s = this.getSelectionAfterText();
            if (!s.isEmpty() && this.startSelection == this.endSelection) {
                s = s.substring(1);
            }
            this.setText(this.getSelectionBeforeText() + s);
            this.endSelection = this.cursorPosition = this.startSelection;
            return true;
        } else if (keyCode == 259) {
            String s = this.getSelectionBeforeText();
            if (this.startSelection > 0 && this.startSelection == this.endSelection) {
                s = s.substring(0, s.length() - 1);
                this.startSelection--;
            }
            this.setText(s + this.getSelectionAfterText());
            this.endSelection = this.cursorPosition = this.startSelection;
            return true;
        } else if (Screen.isCut(keyCode)) {
            if (this.startSelection != this.endSelection) {
                NoppesStringUtils.setClipboardContents(this.text.substring(this.startSelection, this.endSelection));
                String s = this.getSelectionBeforeText();
                this.setText(s + this.getSelectionAfterText());
                this.endSelection = this.startSelection = this.cursorPosition = s.length();
            }
            return true;
        } else if (Screen.isCopy(keyCode)) {
            if (this.startSelection != this.endSelection) {
                NoppesStringUtils.setClipboardContents(this.text.substring(this.startSelection, this.endSelection));
            }
            return true;
        } else if (Screen.isPaste(keyCode)) {
            this.addText(NoppesStringUtils.getClipboardContents());
            return true;
        } else if (keyCode == 90 && Screen.hasControlDown()) {
            if (this.undoList.isEmpty()) {
                return true;
            } else {
                this.undoing = true;
                this.redoList.add(new GuiTextArea.UndoData(this.text, this.cursorPosition));
                GuiTextArea.UndoData data = (GuiTextArea.UndoData) this.undoList.remove(this.undoList.size() - 1);
                this.setText(data.text);
                this.endSelection = this.startSelection = this.cursorPosition = data.cursorPosition;
                this.undoing = false;
                return true;
            }
        } else if (keyCode == 89 && Screen.hasControlDown()) {
            if (this.redoList.isEmpty()) {
                return true;
            } else {
                this.undoing = true;
                this.undoList.add(new GuiTextArea.UndoData(this.text, this.cursorPosition));
                GuiTextArea.UndoData data = (GuiTextArea.UndoData) this.redoList.remove(this.redoList.size() - 1);
                this.setText(data.text);
                this.endSelection = this.startSelection = this.cursorPosition = data.cursorPosition;
                this.undoing = false;
                return true;
            }
        } else {
            if (keyCode == 258) {
                this.addText("    ");
            }
            if (keyCode == 257 || keyCode == 335) {
                this.addText(Character.toString('\n') + this.getIndentCurrentLine());
            }
            return true;
        }
    }

    private String getIndentCurrentLine() {
        for (TextContainer.LineData data : this.container.lines) {
            if (this.cursorPosition > data.start && this.cursorPosition <= data.end) {
                int i = 0;
                while (i < data.text.length() && data.text.charAt(i) == ' ') {
                    i++;
                }
                return data.text.substring(0, i);
            }
        }
        return "";
    }

    private void setCursor(int i, boolean select) {
        i = Math.min(Math.max(i, 0), this.text.length());
        if (i != this.cursorPosition) {
            if (!select) {
                this.endSelection = this.startSelection = this.cursorPosition = i;
            } else {
                int diff = this.cursorPosition - i;
                if (this.cursorPosition == this.startSelection) {
                    this.startSelection -= diff;
                } else if (this.cursorPosition == this.endSelection) {
                    this.endSelection -= diff;
                }
                if (this.startSelection > this.endSelection) {
                    int j = this.endSelection;
                    this.endSelection = this.startSelection;
                    this.startSelection = j;
                }
                this.cursorPosition = i;
            }
        }
    }

    public void addText(String s) {
        this.setText(this.getSelectionBeforeText() + s + this.getSelectionAfterText());
        this.endSelection = this.startSelection = this.cursorPosition = this.startSelection + s.length();
    }

    private int cursorUp() {
        for (int i = 0; i < this.container.lines.size(); i++) {
            TextContainer.LineData data = (TextContainer.LineData) this.container.lines.get(i);
            if (this.cursorPosition >= data.start && this.cursorPosition < data.end) {
                if (i == 0) {
                    return 0;
                }
                int linePos = this.cursorPosition - data.start;
                return this.getSelectionPos((double) (this.x + 1 + font.width(data.text.substring(0, this.cursorPosition - data.start))), (double) (this.y + 1 + (i - 1 - this.scrolledLine) * this.container.lineHeight));
            }
        }
        return 0;
    }

    private int cursorDown() {
        for (int i = 0; i < this.container.lines.size(); i++) {
            TextContainer.LineData data = (TextContainer.LineData) this.container.lines.get(i);
            if (this.cursorPosition >= data.start && this.cursorPosition < data.end) {
                int linePos = this.cursorPosition - data.start;
                return this.getSelectionPos((double) (this.x + 1 + font.width(data.text.substring(0, this.cursorPosition - data.start))), (double) (this.y + 1 + (i + 1 - this.scrolledLine) * this.container.lineHeight));
            }
        }
        return this.text.length();
    }

    public String getSelectionBeforeText() {
        return this.startSelection == 0 ? "" : this.text.substring(0, this.startSelection);
    }

    public String getSelectionAfterText() {
        return this.text.substring(this.endSelection);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    public boolean mouseClicked(double xMouse, double yMouse, int mouseButton) {
        this.active = xMouse >= (double) this.x && xMouse < (double) (this.x + this.width) && yMouse >= (double) this.y && yMouse < (double) (this.y + this.height);
        if (this.active) {
            this.startSelection = this.endSelection = this.cursorPosition = this.getSelectionPos(xMouse, yMouse);
            this.clicked = mouseButton == 0;
            this.doubleClicked = false;
            long time = System.currentTimeMillis();
            if (this.clicked && this.container.linesCount * this.container.lineHeight > this.height && xMouse > (double) (this.x + this.width - 8)) {
                this.clicked = false;
                this.clickScrolling = true;
            } else if (time - this.lastClicked < 500L) {
                this.doubleClicked = true;
                Matcher m = this.container.regexWord.matcher(this.text);
                while (m.find()) {
                    if (this.cursorPosition > m.start() && this.cursorPosition < m.end()) {
                        this.startSelection = m.start();
                        this.endSelection = m.end();
                        break;
                    }
                }
            }
            this.lastClicked = time;
        }
        return this.active;
    }

    @Override
    public void tick() {
        this.cursorCounter++;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrolled) {
        if (scrolled != 0.0) {
            this.scrolledLine += scrolled > 0.0 ? -1 : 1;
            this.scrolledLine = Math.max(Math.min(this.scrolledLine, this.container.linesCount - this.height / this.container.lineHeight), 0);
        }
        return true;
    }

    public void setText(String text) {
        text = text.replace("\r", "");
        if (this.text == null || !this.text.equals(text)) {
            if (this.listener != null) {
                this.listener.textUpdate(text);
            }
            if (!this.undoing) {
                this.undoList.add(new GuiTextArea.UndoData(this.text, this.cursorPosition));
                this.redoList.clear();
            }
            this.text = text;
            this.container = new TextContainer(text);
            this.container.init(font, this.width, this.height);
            if (this.enableCodeHighlighting) {
                this.container.formatCodeText();
            }
            if (this.scrolledLine > this.container.linesCount - this.container.visibleLines) {
                this.scrolledLine = Math.max(0, this.container.linesCount - this.container.visibleLines);
            }
        }
    }

    public String getText() {
        return this.text;
    }

    public boolean isEnabled() {
        return this.enabled && this.visible;
    }

    public boolean hasVerticalScrollbar() {
        return this.container.visibleLines < this.container.linesCount;
    }

    public void enableCodeHighlighting() {
        this.enableCodeHighlighting = true;
        this.container.formatCodeText();
    }

    public void setListener(ITextChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    class UndoData {

        public String text;

        public int cursorPosition;

        public UndoData(String text, int cursorPosition) {
            this.text = text;
            this.cursorPosition = cursorPosition;
        }
    }
}
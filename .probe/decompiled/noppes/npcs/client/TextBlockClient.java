package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import noppes.npcs.TextBlock;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class TextBlockClient extends TextBlock {

    public int color = 14737632;

    private String name;

    private CommandSourceStack sender;

    public TextBlockClient(String name, String text, int lineWidth, int color, Object... obs) {
        this(text, lineWidth, false, obs);
        this.color = color;
        this.name = name;
    }

    public TextBlockClient(CommandSourceStack sender, String text, int lineWidth, int color, Object... obs) {
        this(text, lineWidth, false, obs);
        this.color = color;
        this.sender = sender;
    }

    public String getName() {
        return this.sender != null ? this.sender.getTextName() : this.name;
    }

    public TextBlockClient(String text, int lineWidth, boolean mcFont, Object... obs) {
        text = NoppesStringUtils.formatText(text, obs);
        String line = "";
        text = text.replace("\n", " \n ");
        text = text.replace("\r", " \r ");
        String[] words = text.split(" ");
        Font font = Minecraft.getInstance().font;
        for (String word : words) {
            if (!word.isEmpty()) {
                if (word.length() == 1) {
                    char c = word.charAt(0);
                    if (c == '\r' || c == '\n') {
                        this.addLine(line);
                        line = "";
                        continue;
                    }
                }
                String newLine;
                if (line.isEmpty()) {
                    newLine = word;
                } else {
                    newLine = line + " " + word;
                }
                if ((mcFont ? font.width(newLine) : ClientProxy.Font.width(newLine)) > lineWidth) {
                    this.addLine(line);
                    line = word.trim();
                } else {
                    line = newLine;
                }
            }
        }
        if (!line.isEmpty()) {
            this.addLine(line);
        }
    }

    private void addLine(String text) {
        Component line = Component.literal(text);
        this.lines.add(line);
    }
}
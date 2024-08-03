package icyllis.modernui.mc.text;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import icyllis.modernui.ModernUI;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.mc.ModernUIMod;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class MuiTextCommand {

    public static final ResourceLocation JB_MONO = ModernUIMod.location("jetbrains-mono-medium");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("modernui").then(Commands.literal("text").then(Commands.literal("layout").then(Commands.argument("message", ComponentArgument.textComponent()).executes(ctx -> {
            layout((CommandSourceStack) ctx.getSource(), ComponentArgument.getComponent(ctx, "message"));
            return 1;
        })))));
    }

    private static void layout(CommandSourceStack source, Component component) {
        TextLayout layout = TextLayoutEngine.getInstance().lookupFormattedLayout(component, Style.EMPTY, 5);
        StringBuilder b = new StringBuilder();
        char[] chars = layout.getTextBuf();
        b.append("chars (logical order): ").append(chars.length).append('\n');
        float[] advances = layout.getAdvances();
        b.append("advances (normalized, cluster-based, logical order)\n");
        b.append("LB=line break, GB=grapheme break, NB=non-breaking\n");
        int[] lineBoundaries = layout.getLineBoundaries();
        int lineBoundaryIndex = 0;
        int nextLineBoundary = lineBoundaries[lineBoundaryIndex++];
        int i = 0;
        while (i < chars.length) {
            b.append(String.format(" %04X ", i));
            int lim = Math.min(i + 8, chars.length);
            for (int j = i; j < lim; j++) {
                b.append(String.format("\\u%04X", Integer.valueOf(chars[j])));
            }
            b.append("\n      ");
            for (int j = i; j < lim; j++) {
                b.append(String.format(" %5.1f", advances[j]));
            }
            b.append("\n      ");
            for (int j = i; j < lim; j++) {
                if (j == nextLineBoundary) {
                    b.append("LB    ");
                    nextLineBoundary = lineBoundaries[lineBoundaryIndex++];
                } else if (advances[j] != 0.0F) {
                    b.append("GB    ");
                } else {
                    b.append("NB    ");
                }
            }
            b.append('\n');
            i = lim;
        }
        int[] glyphs = layout.getGlyphs();
        b.append("glyphs (font/slot/glyph, visual order): ").append(glyphs.length).append('\n');
        float[] positions = layout.getPositions();
        byte[] fontIndices = layout.getFontIndices();
        b.append("positions (normalized x/y, visual order)\n");
        int[] glyphFlags = layout.getGlyphFlags();
        b.append("B=bold, I=italic, U=underline, S=strikethrough\n");
        b.append("O=obfuscated, E=color emoji, M=embedded bitmap\n");
        int ix = 0;
        while (ix < glyphs.length) {
            b.append(String.format(" %04X ", ix));
            int lim = Math.min(ix + 4, glyphs.length);
            for (int jx = ix; jx < lim; jx++) {
                int idx;
                if (fontIndices == null) {
                    idx = 0;
                } else {
                    idx = fontIndices[jx] & 255;
                }
                b.append(String.format(" %02X %02X %04X ", idx, glyphs[jx] >>> 24, glyphs[jx] & 65535));
            }
            b.append("\n      ");
            for (int jx = ix; jx < lim; jx++) {
                b.append(String.format("%6.1f,%4.1f ", positions[jx << 1], positions[jx << 1 | 1]));
            }
            b.append("\n      ");
            for (int jx = ix; jx < lim; jx++) {
                int flag = glyphFlags[jx];
                b.append(' ');
                if ((flag & 16777216) != 0) {
                    b.append('B');
                } else {
                    b.append(' ');
                }
                if ((flag & 33554432) != 0) {
                    b.append('I');
                } else {
                    b.append(' ');
                }
                if ((flag & 67108864) != 0) {
                    b.append('U');
                } else {
                    b.append(' ');
                }
                if ((flag & 134217728) != 0) {
                    b.append('S');
                } else {
                    b.append(' ');
                }
                if ((flag & 268435456) != 0) {
                    b.append('O');
                } else {
                    b.append(' ');
                }
                if ((flag & 536870912) != 0) {
                    b.append('E');
                } else {
                    b.append(' ');
                }
                if ((flag & 1073741824) != 0) {
                    b.append('M');
                } else {
                    b.append(' ');
                }
                b.append("    ");
            }
            b.append('\n');
            ix = lim;
        }
        Font[] fonts = layout.getFontVector();
        for (int ixx = 0; ixx < fonts.length; ixx++) {
            b.append(String.format(" %02X: %s\n", ixx, fonts[ixx].getFamilyName()));
        }
        b.append("total advance: ");
        b.append(layout.getTotalAdvance());
        b.append('\n');
        String result = b.toString();
        source.sendSystemMessage(component);
        source.sendSystemMessage(Component.literal(result).setStyle(Style.EMPTY.withFont(JB_MONO)));
        Util.ioPool().execute(() -> ModernUI.LOGGER.info(TextLayoutEngine.MARKER, result));
    }
}
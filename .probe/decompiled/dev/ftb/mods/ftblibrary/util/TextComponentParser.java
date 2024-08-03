package dev.ftb.mods.ftblibrary.util;

import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.Nullable;

public class TextComponentParser {

    public static final Char2ObjectOpenHashMap<ChatFormatting> CODE_TO_FORMATTING = new Char2ObjectOpenHashMap();

    private final String text;

    private final Function<String, Component> substitutes;

    private MutableComponent component;

    private StringBuilder builder;

    private Style style;

    public static Component parse(String text, @Nullable Function<String, Component> substitutes) {
        Component c = parse0(text, substitutes);
        if (c == Component.f_130760_) {
            return c;
        } else {
            while (c.getContents() == ComponentContents.EMPTY && c.getStyle().equals(Style.EMPTY) && c.getSiblings().size() == 1) {
                c = (Component) c.getSiblings().get(0);
            }
            return c;
        }
    }

    private static Component parse0(String text, @Nullable Function<String, Component> substitutes) {
        try {
            return new TextComponentParser(text, substitutes).parse();
        } catch (TextComponentParser.BadFormatException var3) {
            return Component.literal(var3.getMessage()).withStyle(ChatFormatting.RED);
        } catch (Exception var4) {
            return Component.literal(var4.toString()).withStyle(ChatFormatting.RED);
        }
    }

    private TextComponentParser(String txt, @Nullable Function<String, Component> sub) {
        this.text = txt;
        this.substitutes = sub;
    }

    private Component parse() throws TextComponentParser.BadFormatException {
        if (this.text.isEmpty()) {
            return Component.empty();
        } else {
            char[] c = this.text.replaceAll("\\\\n", "\n").toCharArray();
            boolean hasSpecialCodes = false;
            for (char c1 : c) {
                if (c1 == '{' || c1 == '&' || c1 == 167) {
                    hasSpecialCodes = true;
                    break;
                }
            }
            if (!hasSpecialCodes) {
                return Component.literal(new String(c));
            } else {
                this.component = Component.literal("");
                this.style = Style.EMPTY;
                this.builder = new StringBuilder();
                boolean sub = false;
                for (int i = 0; i < c.length; i++) {
                    boolean escape = i > 0 && c[i - 1] == '\\';
                    boolean end = i == c.length - 1;
                    if (!sub || !end && c[i] != '{' && c[i] != '}') {
                        if (!escape) {
                            if (c[i] == '&' || c[i] == 167) {
                                this.finishPart();
                                if (end) {
                                    throw new TextComponentParser.BadFormatException("Invalid formatting! Can't end string with &!");
                                }
                                if (c[++i] == '#') {
                                    char[] rrggbb = new char[7];
                                    rrggbb[0] = '#';
                                    System.arraycopy(c, i + 1, rrggbb, 1, 6);
                                    i += 6;
                                    this.style = this.style.withColor(TextColor.parseColor(new String(rrggbb)));
                                } else {
                                    if (c[i] == ' ') {
                                        throw new TextComponentParser.BadFormatException("Invalid formatting! You must escape whitespace after & with \\&!");
                                    }
                                    ChatFormatting formatting = (ChatFormatting) CODE_TO_FORMATTING.get(c[i]);
                                    if (formatting == null) {
                                        throw new TextComponentParser.BadFormatException("Invalid formatting! Unknown formatting symbol after &: '" + c[i] + "'!");
                                    }
                                    this.style = this.style.applyFormat(formatting);
                                }
                                continue;
                            }
                            if (c[i] == '{') {
                                this.finishPart();
                                if (end) {
                                    throw new TextComponentParser.BadFormatException("Invalid formatting! Can't end string with {!");
                                }
                                sub = true;
                            }
                        }
                        if (c[i] != '\\' || escape) {
                            this.builder.append(c[i]);
                        }
                    } else {
                        if (c[i] == '{') {
                            throw new TextComponentParser.BadFormatException("Invalid formatting! Can't nest multiple substitutes!");
                        }
                        this.finishPart();
                        sub = false;
                    }
                }
                this.finishPart();
                return this.component;
            }
        }
    }

    private void finishPart() throws TextComponentParser.BadFormatException {
        String string = this.builder.toString();
        this.builder.setLength(0);
        if (!string.isEmpty()) {
            if (string.length() >= 2 && string.charAt(0) == '{') {
                Component component1 = (Component) this.substitutes.apply(string.substring(1));
                if (component1 != null) {
                    Style style0 = component1.getStyle();
                    Style style1 = this.style;
                    style1 = style1.withHoverEvent(style0.getHoverEvent());
                    style1 = style1.withClickEvent(style0.getClickEvent());
                    style1 = style1.withInsertion(style0.getInsertion());
                    Component var6 = Component.literal("").append(component1).withStyle(style1);
                    this.component.append(var6);
                } else {
                    throw new TextComponentParser.BadFormatException("Invalid formatting! Unknown substitute: " + string.substring(1));
                }
            } else {
                MutableComponent component1 = Component.literal(string);
                component1.setStyle(this.style);
                this.component.append(component1);
            }
        }
    }

    static {
        CODE_TO_FORMATTING.put('0', ChatFormatting.BLACK);
        CODE_TO_FORMATTING.put('1', ChatFormatting.DARK_BLUE);
        CODE_TO_FORMATTING.put('2', ChatFormatting.DARK_GREEN);
        CODE_TO_FORMATTING.put('3', ChatFormatting.DARK_AQUA);
        CODE_TO_FORMATTING.put('4', ChatFormatting.DARK_RED);
        CODE_TO_FORMATTING.put('5', ChatFormatting.DARK_PURPLE);
        CODE_TO_FORMATTING.put('6', ChatFormatting.GOLD);
        CODE_TO_FORMATTING.put('7', ChatFormatting.GRAY);
        CODE_TO_FORMATTING.put('8', ChatFormatting.DARK_GRAY);
        CODE_TO_FORMATTING.put('9', ChatFormatting.BLUE);
        CODE_TO_FORMATTING.put('a', ChatFormatting.GREEN);
        CODE_TO_FORMATTING.put('b', ChatFormatting.AQUA);
        CODE_TO_FORMATTING.put('c', ChatFormatting.RED);
        CODE_TO_FORMATTING.put('d', ChatFormatting.LIGHT_PURPLE);
        CODE_TO_FORMATTING.put('e', ChatFormatting.YELLOW);
        CODE_TO_FORMATTING.put('f', ChatFormatting.WHITE);
        CODE_TO_FORMATTING.put('k', ChatFormatting.OBFUSCATED);
        CODE_TO_FORMATTING.put('l', ChatFormatting.BOLD);
        CODE_TO_FORMATTING.put('m', ChatFormatting.STRIKETHROUGH);
        CODE_TO_FORMATTING.put('n', ChatFormatting.UNDERLINE);
        CODE_TO_FORMATTING.put('o', ChatFormatting.ITALIC);
        CODE_TO_FORMATTING.put('r', ChatFormatting.RESET);
    }

    private static class BadFormatException extends IllegalArgumentException {

        private BadFormatException(String s) {
            super(s);
        }
    }
}
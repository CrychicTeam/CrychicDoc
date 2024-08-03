package vazkii.patchouli.client.book.text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookCategory;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.book.Book;

public class BookTextParser {

    public static final MutableComponent EMPTY_STRING_COMPONENT = Component.literal("");

    private static final List<BookTextParser.CommandLookup> COMMAND_LOOKUPS = new ArrayList();

    private static final Map<String, BookTextParser.CommandProcessor> COMMANDS = new ConcurrentHashMap();

    private static final Map<String, BookTextParser.FunctionProcessor> FUNCTIONS = new ConcurrentHashMap();

    private final GuiBook gui;

    private final Book book;

    private final int x;

    private final int y;

    private final int width;

    private final int lineHeight;

    private final Style baseStyle;

    private Pattern COMMAND_PATTERN = Pattern.compile("\\$\\(([^)]*)\\)");

    private static void registerProcessor(BookTextParser.CommandLookup processor) {
        COMMAND_LOOKUPS.add(processor);
    }

    public static void register(BookTextParser.CommandProcessor handler, String... names) {
        for (String name : names) {
            COMMANDS.put(name, handler);
        }
    }

    public static void register(BookTextParser.FunctionProcessor function, String... names) {
        for (String name : names) {
            FUNCTIONS.put(name, function);
        }
    }

    public BookTextParser(GuiBook gui, Book book, int x, int y, int width, int lineHeight, Style baseStyle) {
        this.gui = gui;
        this.book = book;
        this.x = x;
        this.y = y;
        this.width = width;
        this.lineHeight = lineHeight;
        this.baseStyle = baseStyle;
    }

    public List<Span> parse(Component text) {
        List<Span> spans = new ArrayList();
        SpanState state = new SpanState(this.gui, this.book, this.baseStyle);
        text.visit((style, string) -> {
            spans.addAll(this.processCommands(this.expandMacros(string), state, style));
            return Optional.empty();
        }, this.baseStyle);
        return spans;
    }

    public String expandMacros(@Nullable String text) {
        String actualText = text;
        if (text == null) {
            actualText = "[ERROR]";
        }
        int i = 0;
        int expansionCap;
        for (expansionCap = 10; i < expansionCap; i++) {
            String newText = actualText;
            for (Entry<String, String> e : this.book.macros.entrySet()) {
                newText = newText.replace((CharSequence) e.getKey(), (CharSequence) e.getValue());
            }
            if (newText.equals(actualText)) {
                break;
            }
            actualText = newText;
        }
        if (i == expansionCap) {
            PatchouliAPI.LOGGER.warn("Expanded macros for {} iterations without reaching fixpoint, stopping. Make sure you don't have circular macro invocations", expansionCap);
        }
        return actualText;
    }

    private List<Span> processCommands(String text, SpanState state, Style style) {
        state.changeBaseStyle(style);
        List<Span> spans = new ArrayList();
        Matcher match = this.COMMAND_PATTERN.matcher(text);
        while (match.find()) {
            StringBuilder sb = new StringBuilder();
            match.appendReplacement(sb, "");
            spans.add(new Span(state, sb.toString()));
            try {
                String processed = this.processCommand(state, match.group(1));
                if (!processed.isEmpty()) {
                    spans.add(new Span(state, processed));
                    if (state.cluster == null) {
                        state.tooltip = EMPTY_STRING_COMPONENT;
                    }
                }
            } catch (Exception var8) {
                spans.add(Span.error(state, "[ERROR]"));
            }
        }
        spans.add(new Span(state, match.appendTail(new StringBuffer()).toString()));
        return spans;
    }

    private String processCommand(SpanState state, String cmd) {
        state.endingExternal = false;
        Optional<String> optResult = Optional.empty();
        for (BookTextParser.CommandLookup lookup : COMMAND_LOOKUPS) {
            optResult = lookup.process(cmd, state);
            if (optResult.isPresent()) {
                break;
            }
        }
        String result = (String) optResult.orElse("$(" + cmd + ")");
        if (state.endingExternal) {
            result = result + ChatFormatting.GRAY + "↪";
        }
        return result;
    }

    private static Optional<String> colorCodeProcessor(String functionName, SpanState state) {
        if (functionName.length() == 1 && functionName.matches("^[0123456789abcdef]$")) {
            state.modifyStyle(s -> s.applyFormat(ChatFormatting.getByCode(functionName.charAt(0))));
            return Optional.of("");
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> colorHexProcessor(String functionName, SpanState state) {
        if (functionName.startsWith("#") && (functionName.length() == 4 || functionName.length() == 7)) {
            String parse = functionName.substring(1);
            if (parse.length() == 3) {
                parse = "" + parse.charAt(0) + parse.charAt(0) + parse.charAt(1) + parse.charAt(1) + parse.charAt(2) + parse.charAt(2);
            }
            TextColor color;
            try {
                color = TextColor.fromRgb(Integer.parseInt(parse, 16));
            } catch (NumberFormatException var5) {
                color = state.getBase().getColor();
            }
            state.color(color);
            return Optional.of("");
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> listProcessor(String functionName, SpanState state) {
        if (functionName.matches("li\\d?")) {
            char c = functionName.length() > 2 ? functionName.charAt(2) : 49;
            int dist = Character.isDigit(c) ? Character.digit(c, 10) : 1;
            int pad = dist * 4;
            char bullet = (char) (dist % 2 == 0 ? 9702 : 8226);
            state.lineBreaks = 1;
            state.spacingLeft = pad;
            state.spacingRight = state.spaceWidth;
            return Optional.of(ChatFormatting.BLACK.toString() + bullet);
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> lookupFunctionProcessor(String functionName, SpanState state) {
        int index = functionName.indexOf(58);
        if (index > 0) {
            String fname = functionName.substring(0, index);
            String param = functionName.substring(index + 1);
            return Optional.of((String) Optional.ofNullable((BookTextParser.FunctionProcessor) FUNCTIONS.get(fname)).map(f -> f.process(param, state)).orElse("[MISSING FUNCTION: " + fname + "]"));
        } else {
            return Optional.empty();
        }
    }

    private static Optional<String> lookupCommandProcessor(String functionName, SpanState state) {
        return Optional.ofNullable((BookTextParser.CommandProcessor) COMMANDS.get(functionName)).map(c -> c.process(state));
    }

    private static KeyMapping getKeybindKey(SpanState state, String keybind) {
        String alt = "key." + keybind;
        KeyMapping[] keys = state.gui.getMinecraft().options.keyMappings;
        for (KeyMapping k : keys) {
            String name = k.getName();
            if (name.equals(keybind) || name.equals(alt)) {
                return k;
            }
        }
        return null;
    }

    static {
        registerProcessor(BookTextParser::colorCodeProcessor);
        registerProcessor(BookTextParser::colorHexProcessor);
        registerProcessor(BookTextParser::listProcessor);
        registerProcessor(BookTextParser::lookupFunctionProcessor);
        registerProcessor(BookTextParser::lookupCommandProcessor);
        register(state -> {
            state.lineBreaks = 1;
            return "";
        }, "br");
        register(state -> {
            state.lineBreaks = 2;
            return "";
        }, "br2", "2br", "p");
        register(state -> {
            state.endingExternal = state.isExternalLink;
            state.popStyle();
            state.cluster = null;
            state.tooltip = EMPTY_STRING_COMPONENT;
            state.onClick = null;
            state.isExternalLink = false;
            return "";
        }, "/l");
        register(state -> {
            state.cluster = null;
            state.tooltip = EMPTY_STRING_COMPONENT;
            return "";
        }, "/t");
        register(state -> state.gui.getMinecraft().player.m_7755_().getString(), "playername");
        register(state -> {
            state.modifyStyle(s -> s.applyFormat(ChatFormatting.OBFUSCATED));
            return "";
        }, "k", "obf");
        register(state -> {
            state.modifyStyle(s -> s.applyFormat(ChatFormatting.BOLD));
            return "";
        }, "l", "bold");
        register(state -> {
            state.modifyStyle(s -> s.applyFormat(ChatFormatting.STRIKETHROUGH));
            return "";
        }, "m", "strike");
        register(state -> {
            state.modifyStyle(s -> s.applyFormat(ChatFormatting.UNDERLINE));
            return "";
        }, "n", "underline");
        register(state -> {
            state.modifyStyle(s -> s.applyFormat(ChatFormatting.ITALIC));
            return "";
        }, "o", "italic", "italics");
        register(state -> {
            state.reset();
            return "";
        }, "", "reset", "clear");
        register(state -> {
            state.baseColor();
            return "";
        }, "nocolor");
        register((parameter, state) -> {
            KeyMapping result = getKeybindKey(state, parameter);
            if (result == null) {
                state.tooltip = Component.translatable("patchouli.gui.lexicon.keybind_missing", parameter);
                return "N/A";
            } else {
                state.tooltip = Component.translatable("patchouli.gui.lexicon.keybind", Component.translatable(result.getName()));
                return result.getTranslatedKeyMessage().getString();
            }
        }, "k");
        register((parameter, state) -> {
            state.cluster = new LinkedList();
            state.pushStyle(Style.EMPTY.withColor(TextColor.fromRgb(state.book.linkColor)));
            boolean isExternal = parameter.matches("^https?:.*");
            if (isExternal) {
                state.tooltip = Component.translatable("patchouli.gui.lexicon.external_link");
                state.isExternalLink = true;
                state.onClick = () -> {
                    GuiBook.openWebLink(state.gui, parameter);
                    return true;
                };
            } else {
                int hash = parameter.indexOf(35);
                String anchor = null;
                if (hash >= 0) {
                    anchor = parameter.substring(hash + 1);
                    parameter = parameter.substring(0, hash);
                }
                ResourceLocation href = parameter.contains(":") ? new ResourceLocation(parameter) : new ResourceLocation(state.book.id.getNamespace(), parameter);
                GuiBook gui = state.gui;
                Book book = state.book;
                BookEntry entry = (BookEntry) book.getContents().entries.get(href);
                BookCategory category = (BookCategory) book.getContents().categories.get(href);
                if (entry != null) {
                    state.tooltip = entry.isLocked() ? Component.translatable("patchouli.gui.lexicon.locked").withStyle(ChatFormatting.GRAY) : entry.getName();
                    int page = 0;
                    if (anchor != null) {
                        int anchorPage = entry.getPageFromAnchor(anchor);
                        if (anchorPage >= 0) {
                            page = anchorPage / 2;
                        } else {
                            state.tooltip.append(" (INVALID ANCHOR:" + anchor + ")");
                        }
                    }
                    int finalPage = page;
                    state.onClick = () -> {
                        GuiBookEntry entryGui = new GuiBookEntry(book, entry, finalPage);
                        gui.displayLexiconGui(entryGui, true);
                        GuiBook.playBookFlipSound(book);
                        return true;
                    };
                } else if (category != null) {
                    if (anchor != null) {
                        state.tooltip = Component.literal("BAD LINK: Cannot specify anchor when linking to a category");
                    } else {
                        state.tooltip = category.getName();
                        state.onClick = () -> {
                            gui.displayLexiconGui(new GuiBookCategory(book, category), true);
                            GuiBook.playBookFlipSound(book);
                            return true;
                        };
                    }
                } else {
                    state.tooltip = Component.literal("BAD LINK: " + parameter);
                }
            }
            return "";
        }, "l");
        register((parameter, state) -> {
            state.tooltip = Component.literal(parameter);
            state.cluster = new LinkedList();
            return "";
        }, "tooltip", "t");
        register((parameter, state) -> {
            state.pushStyle(Style.EMPTY.withColor(TextColor.fromRgb(state.book.linkColor)));
            state.cluster = new LinkedList();
            if (!parameter.startsWith("/")) {
                state.tooltip = Component.literal("INVALID COMMAND (must begin with /)");
            } else {
                state.tooltip = Component.literal(parameter.length() < 20 ? parameter : parameter.substring(0, 20) + "...");
            }
            state.onClick = () -> {
                state.gui.getMinecraft().player.connection.sendCommand(parameter.substring(1));
                return true;
            };
            return "";
        }, "command", "c");
        register(state -> {
            state.popStyle();
            state.cluster = null;
            state.tooltip = EMPTY_STRING_COMPONENT;
            state.onClick = null;
            return "";
        }, "/c");
    }

    public interface CommandLookup {

        Optional<String> process(String var1, SpanState var2);
    }

    public interface CommandProcessor {

        String process(SpanState var1);
    }

    public interface FunctionProcessor {

        String process(String var1, SpanState var2);
    }
}
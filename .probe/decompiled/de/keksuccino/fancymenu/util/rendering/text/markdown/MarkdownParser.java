package de.keksuccino.fancymenu.util.rendering.text.markdown;

import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.ObjectUtils;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarkdownParser {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String EMPTY_STRING = "";

    private static final char NEWLINE_CHAR = '\n';

    private static final String NEWLINE = "\n";

    private static final String HEADLINE_PREFIX_BIGGEST = "# ";

    private static final String HEADLINE_PREFIX_BIGGER = "## ";

    private static final String HEADLINE_PREFIX_BIG = "### ";

    private static final char HASHTAG_CHAR = '#';

    private static final String FORMATTING_CODE_FONT_PREFIX = "%!!";

    private static final String FORMATTING_CODE_FONT_SUFFIX = "%!!%";

    private static final String PERCENTAGE = "%";

    private static final char PERCENTAGE_CHAR = '%';

    private static final String FORMATTING_CODE_HEX_COLOR_PREFIX = "%#";

    private static final String FORMATTING_CODE_HEX_COLOR_SUFFIX = "%#%";

    private static final char ASTERISK_CHAR = '*';

    private static final String FORMATTING_CODE_BOLD_PREFIX_SUFFIX = "**";

    private static final char UNDERSCORE_CHAR = '_';

    private static final char TILDE_CHAR = '~';

    private static final char EXCLAMATION_MARK_CHAR = '!';

    private static final String FORMATTING_CODE_IMAGE_PREFIX = "![";

    private static final String FORMATTING_CODE_HYPERLINK_IMAGE_PREFIX = "[![";

    private static final String FORMATTING_CODE_HYPERLINK_INNER_PREFIX = "](";

    private static final char OPEN_ROUND_BRACKETS_CHAR = '(';

    private static final char CLOSE_ROUND_BRACKETS_CHAR = ')';

    private static final String CLOSE_ROUND_BRACKETS = ")";

    private static final char OPEN_SQUARE_BRACKETS_CHAR = '[';

    private static final char CLOSE_SQUARE_BRACKETS_CHAR = ']';

    private static final String OPEN_SQUARE_BRACKETS = "[";

    private static final char GREATER_THAN_CHAR = '>';

    private static final String FORMATTING_CODE_QUOTE_PREFIX = "> ";

    private static final String SPACE = " ";

    private static final char SPACE_CHAR = ' ';

    private static final String FORMATTING_CODE_BULLET_LIST_LEVEL_1_PREFIX = "- ";

    private static final String FORMATTING_CODE_BULLET_LIST_LEVEL_2_PREFIX = "  - ";

    private static final String MINUS = "-";

    private static final char MINUS_CHAR = '-';

    private static final String FORMATTING_CODE_SEPARATION_LINE_PREFIX = "---";

    private static final char GRAVE_ACCENT_CHAR = '`';

    private static final String FORMATTING_CODE_CODE_BLOCK_MULTI_LINE_PREFIX_SUFFIX = "```";

    private static final char CIRCUMFLEX_CHAR = '^';

    private static final String CIRCUMFLEX = "^";

    private static final String FORMATTING_CODE_ALIGNMENT_CENTERED_PREFIX_SUFFIX = "^^^";

    private static final char VERTICAL_BAR_CHAR = '|';

    private static final String FORMATTING_CODE_ALIGNMENT_RIGHT_PREFIX_SUFFIX = "|||";

    private static final String VERTICAL_BAR = "|";

    private static final CharacterFilter RESOURCE_NAME_FILTER = CharacterFilter.buildResourceNameFilter();

    private static final CharacterFilter MINUS_CHARACTER_FILTER = ObjectUtils.build(() -> {
        CharacterFilter filter = new CharacterFilter();
        filter.addAllowedCharacters('-');
        return filter;
    });

    @NotNull
    public static List<MarkdownTextFragment> parse(@NotNull MarkdownRenderer renderer, @NotNull String markdownText, boolean parseMarkdown) {
        Objects.requireNonNull(renderer);
        Objects.requireNonNull(markdownText);
        List<MarkdownTextFragment> fragments = new ArrayList();
        MarkdownParser.FragmentBuilder builder = new MarkdownParser.FragmentBuilder(renderer);
        boolean queueNewLine = true;
        boolean italicUnderscore = false;
        int charsToSkip = 0;
        boolean skipLine = false;
        MarkdownTextFragment lastBuiltFragment = null;
        boolean lastLineWasHeadline = false;
        String currentLine = "";
        int index = -1;
        for (char c : markdownText.toCharArray()) {
            boolean isStartOfLine = queueNewLine;
            queueNewLine = false;
            String subText = StringUtils.substring(markdownText, ++index);
            String subLine = getLine(subText);
            if (isStartOfLine) {
                currentLine = getLine(subText);
            }
            if (c == '\n' && lastLineWasHeadline) {
                String nextLine = null;
                try {
                    nextLine = StringUtils.substring(subText, 1).split("\n", 2)[0];
                } catch (Exception var29) {
                }
                if (nextLine != null && removeFromString(nextLine, "\n", " ").isEmpty()) {
                    lastLineWasHeadline = false;
                    continue;
                }
                lastLineWasHeadline = false;
            }
            if (charsToSkip > 0) {
                charsToSkip--;
            } else if (c == '\n' || !skipLine) {
                if (c == '\n' && skipLine) {
                    builder.headlineType = MarkdownTextFragment.HeadlineType.NONE;
                    builder.separationLine = false;
                    skipLine = false;
                    queueNewLine = true;
                } else {
                    if (parseMarkdown) {
                        if (isStartOfLine && c == '#' && builder.codeBlockContext == null && builder.headlineType == MarkdownTextFragment.HeadlineType.NONE) {
                            if (StringUtils.startsWith(subText, "# ")) {
                                builder.headlineType = MarkdownTextFragment.HeadlineType.BIGGEST;
                                charsToSkip = 1;
                            }
                            if (StringUtils.startsWith(subText, "## ")) {
                                builder.headlineType = MarkdownTextFragment.HeadlineType.BIGGER;
                                charsToSkip = 2;
                            }
                            if (StringUtils.startsWith(subText, "### ")) {
                                builder.headlineType = MarkdownTextFragment.HeadlineType.BIG;
                                charsToSkip = 3;
                            }
                            if (builder.headlineType != MarkdownTextFragment.HeadlineType.NONE) {
                                lastLineWasHeadline = true;
                                continue;
                            }
                        }
                        if (c == '%') {
                            if (StringUtils.startsWith(subLine, "%!!") && !StringUtils.startsWith(subLine, "%!!%") && builder.font == null && builder.codeBlockContext == null) {
                                String afterPrefix = StringUtils.substring(subLine, 3);
                                if (StringUtils.contains(afterPrefix, 37)) {
                                    String fontName = StringUtils.split(afterPrefix, "%", 2)[0];
                                    if (RESOURCE_NAME_FILTER.isAllowedText(fontName) && StringUtils.contains(subText, "%!!%")) {
                                        ResourceLocation font = null;
                                        try {
                                            font = new ResourceLocation(fontName);
                                        } catch (Exception var30) {
                                        }
                                        if (font != null) {
                                            if (isStartOfLine || !builder.text.isEmpty()) {
                                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                            }
                                            builder.font = font;
                                            charsToSkip = fontName.length() + 3;
                                            continue;
                                        }
                                    }
                                }
                            }
                            if (StringUtils.startsWith(subText, "%!!%") && builder.font != null) {
                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                builder.font = null;
                                charsToSkip = 3;
                                continue;
                            }
                        }
                        if (c == '%') {
                            if (StringUtils.startsWith(subLine, "%#") && !StringUtils.startsWith(subLine, "%#%") && builder.textColor == null && builder.codeBlockContext == null) {
                                String s = subLine.length() >= 11 ? StringUtils.substring(subLine, 1, 11) : "";
                                if (!StringUtils.endsWith(s, "%")) {
                                    s = subLine.length() >= 9 ? StringUtils.substring(subLine, 1, 9) : "";
                                }
                                if (StringUtils.endsWith(s, "%") && StringUtils.contains(subText, "%#%")) {
                                    DrawableColor color = DrawableColor.of(StringUtils.substring(s, 0, s.length() - 1));
                                    if (color != DrawableColor.EMPTY) {
                                        if (isStartOfLine || !builder.text.isEmpty()) {
                                            lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                        }
                                        builder.textColor = color;
                                        charsToSkip = s.length();
                                        continue;
                                    }
                                }
                            }
                            if (StringUtils.startsWith(subText, "%#%") && builder.textColor != null) {
                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                builder.textColor = null;
                                charsToSkip = 2;
                                continue;
                            }
                        }
                        if (c == '*') {
                            if (!builder.bold && builder.codeBlockContext == null) {
                                String s2 = StringUtils.substring(markdownText, Math.min(markdownText.length(), index + 2));
                                if (StringUtils.startsWith(subText, "**") && StringUtils.contains(s2, "**")) {
                                    if (isStartOfLine || !builder.text.isEmpty()) {
                                        lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                    }
                                    builder.bold = true;
                                    charsToSkip = 1;
                                    continue;
                                }
                            }
                            if (builder.bold && StringUtils.startsWith(subText, "**")) {
                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                builder.bold = false;
                                charsToSkip = 1;
                                continue;
                            }
                        }
                        int indexPlusOne = Math.min(markdownText.length(), index + 1);
                        if (c == '_') {
                            if (!builder.italic && builder.codeBlockContext == null) {
                                String sx = StringUtils.substring(markdownText, indexPlusOne);
                                if (StringUtils.contains(sx, 95)) {
                                    if (isStartOfLine || !builder.text.isEmpty()) {
                                        lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                    }
                                    builder.italic = true;
                                    italicUnderscore = true;
                                    continue;
                                }
                            }
                            if (builder.italic && italicUnderscore) {
                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                builder.italic = false;
                                italicUnderscore = false;
                                continue;
                            }
                        }
                        if (c == '*') {
                            if (!builder.italic && builder.codeBlockContext == null) {
                                String s2 = StringUtils.substring(markdownText, indexPlusOne);
                                if (!StringUtils.startsWith(subText, "**") && StringUtils.contains(s2, 42)) {
                                    boolean isEndSingleAsterisk = false;
                                    int index2 = 0;
                                    for (char c2 : s2.toCharArray()) {
                                        if (c2 == '*') {
                                            String s3 = StringUtils.substring(s2, index2);
                                            if (!StringUtils.startsWith(s3, "**")) {
                                                isEndSingleAsterisk = true;
                                                break;
                                            }
                                        }
                                        index2++;
                                    }
                                    if (isEndSingleAsterisk) {
                                        if (isStartOfLine || !builder.text.isEmpty()) {
                                            lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                        }
                                        builder.italic = true;
                                        continue;
                                    }
                                }
                            }
                            if (builder.italic && !italicUnderscore && !StringUtils.startsWith(subText, "**")) {
                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                builder.italic = false;
                                continue;
                            }
                        }
                        if (c == '~') {
                            if (!builder.strikethrough && builder.codeBlockContext == null) {
                                String sx = StringUtils.substring(markdownText, indexPlusOne);
                                if (StringUtils.contains(sx, 126)) {
                                    if (isStartOfLine || !builder.text.isEmpty()) {
                                        lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                    }
                                    builder.strikethrough = true;
                                    continue;
                                }
                            }
                            if (builder.strikethrough) {
                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                builder.strikethrough = false;
                                continue;
                            }
                        }
                        if (isStartOfLine && c == '[' && builder.codeBlockContext == null && StringUtils.startsWith(currentLine, "[![")) {
                            List<String> hyperlinkImage = getHyperlinkImageFromLine(currentLine);
                            if (hyperlinkImage != null) {
                                builder.hyperlink = new MarkdownTextFragment.Hyperlink();
                                builder.hyperlink.link = (String) hyperlinkImage.get(1);
                                setImageToBuilder(builder, (String) hyperlinkImage.get(0));
                                lastBuiltFragment = addFragment(fragments, builder.build(true, true));
                                builder.imageSupplier = null;
                                builder.hyperlink = null;
                                skipLine = true;
                                continue;
                            }
                        }
                        if (isStartOfLine && c == '!' && builder.codeBlockContext == null && StringUtils.startsWith(currentLine, "![")) {
                            String imageLink = getImageFromLine(currentLine);
                            if (imageLink != null) {
                                setImageToBuilder(builder, imageLink);
                                lastBuiltFragment = addFragment(fragments, builder.build(true, true));
                                builder.imageSupplier = null;
                                skipLine = true;
                                continue;
                            }
                        }
                        if (c == '[' && builder.hyperlink == null && builder.codeBlockContext == null) {
                            String s2 = StringUtils.substring(markdownText, indexPlusOne);
                            if (StringUtils.contains(s2, "](") && StringUtils.contains(s2, 41)) {
                                String hyperlink = getHyperlinkFromLine(subText);
                                if (hyperlink != null) {
                                    if (isStartOfLine || !builder.text.isEmpty()) {
                                        lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                    }
                                    builder.hyperlink = new MarkdownTextFragment.Hyperlink();
                                    builder.hyperlink.link = hyperlink;
                                    continue;
                                }
                            }
                        }
                        if (c == ']' && builder.hyperlink != null && StringUtils.startsWith(subText, "](")) {
                            lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                            charsToSkip = 2 + builder.hyperlink.link.length();
                            builder.hyperlink = null;
                            continue;
                        }
                        if (isStartOfLine && c == '>' && builder.quoteContext == null && builder.codeBlockContext == null && StringUtils.startsWith(subText, "> ")) {
                            builder.quoteContext = new MarkdownTextFragment.QuoteContext();
                            charsToSkip = 1;
                            continue;
                        }
                        if (isStartOfLine && builder.quoteContext != null && StringUtils.trim(currentLine).isEmpty()) {
                            builder.quoteContext = null;
                            lastBuiltFragment = addFragment(fragments, builder.build(true, true));
                            queueNewLine = true;
                            continue;
                        }
                        if (isStartOfLine && c == '-' && StringUtils.startsWith(subLine, "- ") && !removeFromString(subLine, "-", " ", "\n").isEmpty() && builder.codeBlockContext == null) {
                            builder.bulletListLevel = 1;
                            builder.bulletListItemStart = true;
                            charsToSkip = 1;
                            continue;
                        }
                        if (isStartOfLine && c == ' ' && StringUtils.startsWith(subLine, "  - ") && !removeFromString(subLine, "-", " ", "\n").isEmpty() && builder.codeBlockContext == null) {
                            builder.bulletListLevel = 2;
                            builder.bulletListItemStart = true;
                            charsToSkip = 3;
                            continue;
                        }
                        if (isStartOfLine && c == '-' && builder.codeBlockContext == null && StringUtils.startsWith(currentLine, "---") && MINUS_CHARACTER_FILTER.isAllowedText(StringUtils.replace(currentLine, " ", ""))) {
                            builder.separationLine = true;
                            builder.text = new StringBuilder("---");
                            lastBuiltFragment = addFragment(fragments, builder.build(true, true));
                            skipLine = true;
                            continue;
                        }
                        if (c == '`') {
                            if (builder.codeBlockContext == null && !StringUtils.startsWith(subLine, "```") && isFormattedBlock(subText, '`', true)) {
                                if (isStartOfLine || !builder.text.isEmpty()) {
                                    lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                }
                                builder.codeBlockContext = new MarkdownTextFragment.CodeBlockContext();
                                builder.codeBlockContext.singleLine = true;
                                continue;
                            }
                            if (builder.codeBlockContext != null && builder.codeBlockContext.singleLine && !StringUtils.startsWith(subLine, "```")) {
                                lastBuiltFragment = addFragment(fragments, builder.build(false, false));
                                builder.codeBlockContext = null;
                                continue;
                            }
                        }
                        if (isStartOfLine && c == '`' && currentLine.length() == 3) {
                            if (builder.codeBlockContext == null && StringUtils.startsWith(currentLine, "```") && isFormattedBlock(subText, '`', false)) {
                                builder.codeBlockContext = new MarkdownTextFragment.CodeBlockContext();
                                builder.codeBlockContext.singleLine = false;
                                skipLine = true;
                                continue;
                            }
                            if (builder.codeBlockContext != null && !builder.codeBlockContext.singleLine && StringUtils.startsWith(currentLine, "```")) {
                                builder.codeBlockContext = null;
                                skipLine = true;
                                continue;
                            }
                        }
                        if (isStartOfLine && c == '^' && currentLine.length() == 3 && StringUtils.startsWith(subLine, "^^^") && builder.codeBlockContext == null) {
                            if (builder.alignment == MarkdownRenderer.MarkdownLineAlignment.LEFT && isFormattedBlock(subText, '^', false)) {
                                builder.alignment = MarkdownRenderer.MarkdownLineAlignment.CENTERED;
                                skipLine = true;
                                continue;
                            }
                            if (builder.alignment == MarkdownRenderer.MarkdownLineAlignment.CENTERED && removeFromString(subLine, "^", " ", "\n").isEmpty()) {
                                builder.alignment = MarkdownRenderer.MarkdownLineAlignment.LEFT;
                                skipLine = true;
                                continue;
                            }
                        }
                        if (isStartOfLine && c == '|' && currentLine.length() == 3 && StringUtils.startsWith(subLine, "|||") && builder.codeBlockContext == null) {
                            if (builder.alignment == MarkdownRenderer.MarkdownLineAlignment.LEFT && isFormattedBlock(subText, '|', false)) {
                                builder.alignment = MarkdownRenderer.MarkdownLineAlignment.RIGHT;
                                skipLine = true;
                                continue;
                            }
                            if (builder.alignment == MarkdownRenderer.MarkdownLineAlignment.RIGHT && removeFromString(subLine, "|", " ", "\n").isEmpty()) {
                                builder.alignment = MarkdownRenderer.MarkdownLineAlignment.LEFT;
                                skipLine = true;
                                continue;
                            }
                        }
                    }
                    if (c != '\n') {
                        builder.text.append(c);
                    }
                    if (c == ' ') {
                        if (!isStartOfLine && lastBuiltFragment != null && builder.text.toString().equals(" ")) {
                            lastBuiltFragment.endOfWord = true;
                            lastBuiltFragment.text = lastBuiltFragment.text + " ";
                            lastBuiltFragment.updateWidth();
                            builder.clearText();
                        } else {
                            lastBuiltFragment = addFragment(fragments, builder.build(false, true));
                        }
                    } else if (c == '\n') {
                        lastBuiltFragment = addFragment(fragments, builder.build(true, true));
                        builder.headlineType = MarkdownTextFragment.HeadlineType.NONE;
                        builder.separationLine = false;
                        builder.bulletListLevel = 0;
                        queueNewLine = true;
                    }
                }
            }
        }
        fragments.add(builder.build(true, true));
        return fragments;
    }

    protected static String removeFromString(@NotNull String in, String... remove) {
        for (String s : remove) {
            in = StringUtils.replace(in, s, "");
        }
        return in;
    }

    protected static MarkdownTextFragment addFragment(List<MarkdownTextFragment> fragments, MarkdownTextFragment fragment) {
        fragments.add(fragment);
        return fragment;
    }

    protected static void setImageToBuilder(@NotNull MarkdownParser.FragmentBuilder builder, @NotNull String imageSource) {
        builder.imageSupplier = ResourceSupplier.image(imageSource);
    }

    @NotNull
    protected static String getLine(@NotNull String text) {
        try {
            if (!StringUtils.contains(text, "\n")) {
                return text;
            } else {
                return StringUtils.startsWith(text, "\n") ? "" : StringUtils.split(text, "\n", 2)[0];
            }
        } catch (Exception var2) {
            LOGGER.error("[FANCYMENU] Failed to get line of Markdown text!", var2);
            return text;
        }
    }

    protected static boolean isFormattedBlock(String text, char formatChar, boolean singleLine) {
        String longFormatCode = "" + formatChar + formatChar + formatChar;
        if (singleLine) {
            if (StringUtils.startsWith(text, formatChar + "") && !StringUtils.startsWith(text, longFormatCode)) {
                int i = -1;
                boolean endFound = false;
                for (char c : text.toCharArray()) {
                    if (++i != 0) {
                        if (c == '\n') {
                            break;
                        }
                        if (c == formatChar && !StringUtils.startsWith(StringUtils.substring(text, i), "" + formatChar + formatChar + formatChar)) {
                            endFound = true;
                            break;
                        }
                    }
                }
                return endFound;
            }
        } else if (StringUtils.startsWith(text, longFormatCode)) {
            int i = -1;
            boolean endFound = false;
            boolean newLine = false;
            for (char cx : text.toCharArray()) {
                if (++i >= 3) {
                    if (cx == formatChar && newLine && StringUtils.startsWith(StringUtils.substring(text, i), longFormatCode)) {
                        endFound = true;
                        break;
                    }
                    if (newLine) {
                        newLine = false;
                    }
                    if (cx == '\n') {
                        newLine = true;
                    }
                }
            }
            return endFound;
        }
        return false;
    }

    @Nullable
    protected static List<String> getHyperlinkImageFromLine(String line) {
        if (StringUtils.startsWith(line, "[![") && StringUtils.contains(line, "](") && StringUtils.contains(line, 41)) {
            String imageLink = null;
            String hyperLink = null;
            int index = -1;
            for (char ignored : line.toCharArray()) {
                String sub = StringUtils.substring(line, ++index);
                if (index >= 1 && StringUtils.startsWith(sub, "![")) {
                    imageLink = getImageFromLine(sub);
                    if (imageLink != null) {
                        String s = StringUtils.split(sub, ")", 2)[0] + ")";
                        hyperLink = getHyperlinkFromLine(line.replace(s, ""));
                        break;
                    }
                }
            }
            if (imageLink != null && hyperLink != null) {
                return ListUtils.of(imageLink, hyperLink);
            }
        }
        return null;
    }

    @Nullable
    protected static String getImageFromLine(String line) {
        if (StringUtils.startsWith(line, "![") && StringUtils.contains(line, "](") && StringUtils.contains(line, ")")) {
            boolean beforeClosedBrackets = true;
            boolean isInsideImageLink = false;
            StringBuilder imageLink = new StringBuilder();
            boolean openRoundBracketsFound = false;
            String s = StringUtils.substring(line, 2);
            int index = -1;
            for (char c : s.toCharArray()) {
                index++;
                if (c == '\n') {
                    return null;
                }
                if (c == ']') {
                    if (!beforeClosedBrackets) {
                        return null;
                    }
                    beforeClosedBrackets = false;
                    if (StringUtils.startsWith(StringUtils.substring(s, index), "](")) {
                        isInsideImageLink = true;
                        continue;
                    }
                }
                if (c == '[' && beforeClosedBrackets) {
                    return null;
                }
                if (isInsideImageLink) {
                    if (c == '(') {
                        if (openRoundBracketsFound) {
                            return null;
                        }
                        openRoundBracketsFound = true;
                    } else {
                        if (c == ')') {
                            return imageLink.toString();
                        }
                        imageLink.append(c);
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    protected static String getHyperlinkFromLine(String line) {
        if (StringUtils.startsWith(line, "[") && StringUtils.contains(line, "](") && StringUtils.contains(line, ")")) {
            boolean beforeClosedBrackets = true;
            boolean isInsideHyperlink = false;
            StringBuilder hyperlink = new StringBuilder();
            boolean openRoundBracketsFound = false;
            String s = StringUtils.substring(line, 1);
            int index = -1;
            for (char c : s.toCharArray()) {
                index++;
                if (c == '\n') {
                    return null;
                }
                if (c == ']') {
                    if (!beforeClosedBrackets) {
                        return null;
                    }
                    beforeClosedBrackets = false;
                    if (StringUtils.startsWith(StringUtils.substring(s, index), "](")) {
                        isInsideHyperlink = true;
                        continue;
                    }
                }
                if (c == '[' && beforeClosedBrackets) {
                    return null;
                }
                if (isInsideHyperlink) {
                    if (c == '(') {
                        if (openRoundBracketsFound) {
                            return null;
                        }
                        openRoundBracketsFound = true;
                    } else {
                        if (c == ')') {
                            return hyperlink.toString();
                        }
                        hyperlink.append(c);
                    }
                }
            }
        }
        return null;
    }

    protected static class FragmentBuilder {

        protected final MarkdownRenderer renderer;

        protected StringBuilder text = new StringBuilder();

        protected DrawableColor textColor = null;

        protected ResourceSupplier<ITexture> imageSupplier = null;

        protected boolean separationLine = false;

        protected boolean bold = false;

        protected boolean italic = false;

        protected boolean strikethrough = false;

        protected boolean bulletListItemStart = false;

        protected int bulletListLevel = 0;

        @NotNull
        protected MarkdownRenderer.MarkdownLineAlignment alignment = MarkdownRenderer.MarkdownLineAlignment.LEFT;

        protected MarkdownTextFragment.Hyperlink hyperlink = null;

        @NotNull
        protected MarkdownTextFragment.HeadlineType headlineType = MarkdownTextFragment.HeadlineType.NONE;

        protected MarkdownTextFragment.QuoteContext quoteContext = null;

        protected MarkdownTextFragment.CodeBlockContext codeBlockContext = null;

        protected ResourceLocation font = null;

        protected FragmentBuilder(MarkdownRenderer renderer) {
            this.renderer = renderer;
        }

        @NotNull
        protected MarkdownTextFragment build(boolean naturalLineBreakAfter, boolean endOfWord) {
            MarkdownTextFragment frag = new MarkdownTextFragment(this.renderer, this.text.toString());
            frag.font = this.font;
            frag.textColor = this.textColor;
            frag.imageSupplier = this.imageSupplier;
            frag.separationLine = this.separationLine;
            if (this.separationLine) {
                frag.unscaledTextHeight = 1.0F;
            }
            frag.bold = this.bold;
            frag.italic = this.italic;
            frag.strikethrough = this.strikethrough;
            frag.bulletListLevel = this.bulletListLevel;
            frag.bulletListItemStart = this.bulletListItemStart;
            this.bulletListItemStart = false;
            frag.alignment = this.alignment;
            frag.hyperlink = this.hyperlink;
            if (this.hyperlink != null) {
                this.hyperlink.hyperlinkFragments.add(frag);
            }
            frag.headlineType = this.headlineType;
            frag.quoteContext = this.quoteContext;
            if (this.quoteContext != null) {
                this.quoteContext.quoteFragments.add(frag);
            }
            frag.codeBlockContext = this.codeBlockContext;
            if (this.codeBlockContext != null) {
                this.codeBlockContext.codeBlockFragments.add(frag);
            }
            frag.naturalLineBreakAfter = naturalLineBreakAfter;
            frag.endOfWord = endOfWord;
            frag.updateWidth();
            this.clearText();
            return frag;
        }

        protected MarkdownParser.FragmentBuilder clearText() {
            this.text = new StringBuilder();
            return this;
        }
    }
}
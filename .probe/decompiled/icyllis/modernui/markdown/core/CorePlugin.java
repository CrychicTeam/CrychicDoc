package icyllis.modernui.markdown.core;

import com.vladsch.flexmark.ast.BlockQuote;
import com.vladsch.flexmark.ast.BulletList;
import com.vladsch.flexmark.ast.BulletListItem;
import com.vladsch.flexmark.ast.Code;
import com.vladsch.flexmark.ast.Emphasis;
import com.vladsch.flexmark.ast.FencedCodeBlock;
import com.vladsch.flexmark.ast.HardLineBreak;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.IndentedCodeBlock;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.ListBlock;
import com.vladsch.flexmark.ast.ListItem;
import com.vladsch.flexmark.ast.OrderedList;
import com.vladsch.flexmark.ast.OrderedListItem;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.SoftLineBreak;
import com.vladsch.flexmark.ast.StrongEmphasis;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.ast.ThematicBreak;
import com.vladsch.flexmark.util.ast.Block;
import com.vladsch.flexmark.util.ast.Node;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.markdown.MarkdownConfig;
import icyllis.modernui.markdown.MarkdownPlugin;
import icyllis.modernui.markdown.MarkdownTheme;
import icyllis.modernui.markdown.MarkdownVisitor;
import icyllis.modernui.markdown.core.style.CodeBlockSpan;
import icyllis.modernui.markdown.core.style.HeadingSpan;
import icyllis.modernui.markdown.core.style.OrderedListItemSpan;
import icyllis.modernui.markdown.core.style.ThematicBreakSpan;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.style.AbsoluteSizeSpan;
import icyllis.modernui.text.style.BackgroundColorSpan;
import icyllis.modernui.text.style.BulletSpan;
import icyllis.modernui.text.style.ForegroundColorSpan;
import icyllis.modernui.text.style.QuoteSpan;
import icyllis.modernui.text.style.RelativeSizeSpan;
import icyllis.modernui.text.style.StyleSpan;
import icyllis.modernui.text.style.TypefaceSpan;
import icyllis.modernui.text.style.URLSpan;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.widget.TextView;

public final class CorePlugin implements MarkdownPlugin {

    public static final String CORE_ORDERED_LIST_ITEM_NUMBER = "core:ordered_list_item_number";

    @NonNull
    public static CorePlugin create() {
        return new CorePlugin();
    }

    CorePlugin() {
    }

    @Override
    public void configureConfig(@NonNull MarkdownConfig.Builder builder) {
        builder.addVisitor(Text.class, this::visitText).addVisitor(StrongEmphasis.class, this::visitSimpleNode).addVisitor(Emphasis.class, this::visitSimpleNode).addVisitor(SoftLineBreak.class, this::visitSoftLineBreak).addVisitor(HardLineBreak.class, this::visitHardLineBreak).addVisitor(Heading.class, this::visitHeading).addVisitor(Paragraph.class, this::visitParagraph).addVisitor(BulletListItem.class, this::visitBulletListItem).addVisitor(OrderedListItem.class, this::visitOrderedListItem).addVisitor(BulletList.class, this::visitSimpleBlock).addVisitor(OrderedList.class, this::visitOrderedList).addVisitor(BlockQuote.class, this::visitBlockQuote).addVisitor(Code.class, this::visitCode).addVisitor(FencedCodeBlock.class, this::visitFencedCodeBlock).addVisitor(IndentedCodeBlock.class, this::visitIndentedCodeBlock).addVisitor(Link.class, this::visitSimpleNode).addVisitor(ThematicBreak.class, this::visitThematicBreak);
        builder.appendSpanFactory(StrongEmphasis.class, (config, node, props) -> new StyleSpan(1)).appendSpanFactory(Emphasis.class, (config, node, props) -> new StyleSpan(2)).appendSpanFactory(Heading.class, (config, node, props) -> new HeadingSpan(config.theme(), node.getLevel())).appendSpanFactory(BulletListItem.class, this::createBulletListItemSpans).appendSpanFactory(OrderedListItem.class, this::createOrderedListItemSpans).appendSpanFactory(BlockQuote.class, (config, node, props) -> new QuoteSpan(config.theme().getBlockQuoteMargin(), config.theme().getBlockQuoteWidth(), config.theme().getBlockQuoteColor())).appendSpanFactory(Code.class, this::createCodeSpans).appendSpanFactory(FencedCodeBlock.class, (config, node, args) -> new CodeBlockSpan(config.theme())).appendSpanFactory(IndentedCodeBlock.class, (config, node, args) -> new CodeBlockSpan(config.theme())).appendSpanFactory(Link.class, (config, node, args) -> new URLSpan(node.getUrl().toString())).appendSpanFactory(ThematicBreak.class, (config, node, args) -> new ThematicBreakSpan(config.theme()));
    }

    @Override
    public void beforeSetText(@NonNull TextView textView, @NonNull Spanned markdown) {
        OrderedListItemSpan.measure(textView, markdown);
    }

    private void visitSimpleBlock(@NonNull MarkdownVisitor visitor, @NonNull Block block) {
        visitor.blockStart(block);
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(block, offset);
        visitor.visitChildren(block);
        visitor.postSetSpans(spans, offset);
        visitor.blockEnd(block);
    }

    private void visitOrderedList(@NonNull MarkdownVisitor visitor, @NonNull OrderedList orderedList) {
        visitor.blockStart(orderedList);
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(orderedList, offset);
        visitor.getRenderArguments().putInt("core:ordered_list_item_number", orderedList.getStartNumber());
        visitor.visitChildren(orderedList);
        visitor.postSetSpans(spans, offset);
        visitor.blockEnd(orderedList);
    }

    private void visitCode(@NonNull MarkdownVisitor visitor, @NonNull Code code) {
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(code, offset);
        visitor.append(' ').append(code.getText()).append(' ');
        visitor.postSetSpans(spans, offset);
    }

    private void visitFencedCodeBlock(@NonNull MarkdownVisitor visitor, @NonNull FencedCodeBlock fencedCodeBlock) {
        this.visitCodeBlock(visitor, fencedCodeBlock.getInfo(), fencedCodeBlock.getContentChars(), fencedCodeBlock);
    }

    private void visitIndentedCodeBlock(@NonNull MarkdownVisitor visitor, @NonNull IndentedCodeBlock indentedCodeBlock) {
        this.visitCodeBlock(visitor, null, indentedCodeBlock.getContentChars(), indentedCodeBlock);
    }

    private void visitCodeBlock(@NonNull MarkdownVisitor visitor, @Nullable CharSequence info, @NonNull CharSequence code, @NonNull Block block) {
        visitor.blockStart(block);
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(block, offset);
        visitor.append(' ').append('\n').append(code);
        visitor.ensureNewLine();
        visitor.append(' ');
        visitor.postSetSpans(spans, offset);
        visitor.blockEnd(block);
    }

    @NonNull
    private Object createCodeSpans(@NonNull MarkdownConfig config, @NonNull Code code, @NonNull DataSet args) {
        MarkdownTheme theme = config.theme();
        boolean applyTextColor = theme.getCodeTextColor() != 0;
        boolean applyBackgroundColor = theme.getCodeBackgroundColor() != 0;
        boolean applyTextSize = theme.getCodeTextSize() != 0;
        int extra = 0;
        if (applyTextColor) {
            extra++;
        }
        if (applyBackgroundColor) {
            extra++;
        }
        Object[] spans = new Object[extra + 2];
        spans[0] = new TypefaceSpan(theme.getCodeTypeface());
        if (applyTextSize) {
            spans[1] = new AbsoluteSizeSpan(theme.getCodeTextSize());
        } else {
            spans[1] = new RelativeSizeSpan(0.875F);
        }
        if (extra > 0) {
            extra = 2;
            if (applyTextColor) {
                spans[extra++] = new ForegroundColorSpan(theme.getCodeTextColor());
            }
            if (applyBackgroundColor) {
                spans[extra++] = new BackgroundColorSpan(theme.getCodeBackgroundColor());
            }
        }
        return spans;
    }

    @NonNull
    private Object createBulletListItemSpans(@NonNull MarkdownConfig config, @NonNull BulletListItem bulletListItem, @NonNull DataSet args) {
        int level = listLevel(bulletListItem);
        return new BulletSpan(config.theme().getListItemMargin(), 0, config.theme().getListItemColor(), level);
    }

    private static int listLevel(@NonNull Node node) {
        int level = 0;
        for (Node parent = node.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof ListItem) {
                level++;
            }
        }
        return level;
    }

    @NonNull
    private Object createOrderedListItemSpans(@NonNull MarkdownConfig config, @NonNull OrderedListItem orderedListItem, @NonNull DataSet args) {
        String number = args.getInt("core:ordered_list_item_number") + ". ";
        return new OrderedListItemSpan(config.theme(), number);
    }

    private void visitText(@NonNull MarkdownVisitor visitor, @NonNull Text text) {
        visitor.append(text.getChars());
    }

    private void visitSimpleNode(@NonNull MarkdownVisitor visitor, @NonNull Node node) {
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(node, offset);
        visitor.visitChildren(node);
        visitor.postSetSpans(spans, offset);
    }

    private void visitSoftLineBreak(@NonNull MarkdownVisitor visitor, @NonNull SoftLineBreak softLineBreak) {
        visitor.append(" ");
    }

    private void visitHardLineBreak(@NonNull MarkdownVisitor visitor, @NonNull HardLineBreak softLineBreak) {
        visitor.ensureNewLine();
    }

    private void visitHeading(@NonNull MarkdownVisitor visitor, @NonNull Heading heading) {
        visitor.blockStart(heading);
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(heading, offset);
        visitor.visitChildren(heading);
        visitor.postSetSpans(spans, offset);
        visitor.blockEnd(heading);
    }

    private void visitParagraph(@NonNull MarkdownVisitor visitor, @NonNull Paragraph paragraph) {
        boolean inTightList = isInTightList(paragraph);
        if (!inTightList) {
            visitor.blockStart(paragraph);
        }
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(paragraph, offset);
        visitor.visitChildren(paragraph);
        visitor.postSetSpans(spans, offset);
        if (!inTightList) {
            visitor.blockEnd(paragraph);
        }
    }

    private void visitBlockQuote(@NonNull MarkdownVisitor visitor, @NonNull BlockQuote blockQuote) {
        visitor.blockStart(blockQuote);
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(blockQuote, offset);
        visitor.visitChildren(blockQuote);
        visitor.postSetSpans(spans, offset);
        visitor.blockEnd(blockQuote);
    }

    private static boolean isInTightList(@NonNull Paragraph paragraph) {
        Node parent = paragraph.getParent();
        return parent != null && parent.getParent() instanceof ListBlock list ? list.isTight() : false;
    }

    private void visitBulletListItem(@NonNull MarkdownVisitor visitor, @NonNull BulletListItem bulletListItem) {
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(bulletListItem, offset);
        visitor.visitChildren(bulletListItem);
        visitor.postSetSpans(spans, offset);
        if (visitor.hasNext(bulletListItem)) {
            visitor.ensureNewLine();
        }
    }

    private void visitOrderedListItem(@NonNull MarkdownVisitor visitor, @NonNull OrderedListItem orderedListItem) {
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(orderedListItem, offset);
        visitor.visitChildren(orderedListItem);
        visitor.postSetSpans(spans, offset);
        int number = visitor.getRenderArguments().getInt("core:ordered_list_item_number");
        visitor.getRenderArguments().putInt("core:ordered_list_item_number", number + 1);
        if (visitor.hasNext(orderedListItem)) {
            visitor.ensureNewLine();
        }
    }

    private void visitThematicBreak(@NonNull MarkdownVisitor visitor, @NonNull ThematicBreak thematicBreak) {
        visitor.blockStart(thematicBreak);
        int offset = visitor.length();
        Object spans = visitor.preSetSpans(thematicBreak, offset);
        visitor.append('\u200b');
        visitor.postSetSpans(spans, offset);
        visitor.blockEnd(thematicBreak);
    }
}
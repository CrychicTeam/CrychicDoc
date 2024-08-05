package icyllis.modernui.markdown;

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.NodeVisitHandler;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.SpannableStringBuilder;
import icyllis.modernui.util.DataSet;
import java.util.Map;

public final class MarkdownVisitor implements NodeVisitHandler {

    final MarkdownConfig mConfig;

    final Map<Class<? extends Node>, NodeVisitor<Node>> mVisitors;

    private final SpannableStringBuilder mBuilder = new SpannableStringBuilder();

    private final DataSet mRenderArguments = new DataSet();

    @Nullable
    private final BlockHandler mBlockHandler;

    MarkdownVisitor(MarkdownConfig config) {
        this.mConfig = config;
        this.mVisitors = config.mVisitors;
        this.mBlockHandler = config.mBlockHandler;
    }

    public void visit(@NonNull Node node) {
        NodeVisitor<Node> visitor = (NodeVisitor<Node>) this.mVisitors.get(node.getClass());
        if (visitor != null) {
            visitor.visit(this, node);
        } else {
            this.visitChildren(node);
        }
    }

    public void visitNodeOnly(@NonNull Node node) {
        NodeVisitor<Node> visitor = (NodeVisitor<Node>) this.mVisitors.get(node.getClass());
        if (visitor != null) {
            visitor.visit(this, node);
        }
    }

    public void visitChildren(@NonNull Node parent) {
        Node child = parent.getFirstChild();
        while (child != null) {
            Node next = child.getNext();
            this.visit(child);
            child = next;
        }
    }

    public SpannableStringBuilder builder() {
        return this.mBuilder;
    }

    public DataSet getRenderArguments() {
        return this.mRenderArguments;
    }

    public Editable append(char c) {
        return this.mBuilder.append(c);
    }

    public Editable append(CharSequence text) {
        return this.mBuilder.append(text);
    }

    public int length() {
        return this.mBuilder.length();
    }

    public boolean hasNext(@NonNull Node node) {
        return node.getNext() != null;
    }

    public void ensureNewLine() {
        int len = this.mBuilder.length();
        if (len > 0 && this.mBuilder.charAt(len - 1) != '\n') {
            this.mBuilder.append('\n');
        }
    }

    public void forceNewLine() {
        this.mBuilder.append('\n');
    }

    public void blockStart(@NonNull Node node) {
        if (this.mBlockHandler != null) {
            this.mBlockHandler.blockStart(this, node);
        } else {
            this.ensureNewLine();
        }
    }

    public void blockEnd(@NonNull Node node) {
        if (this.mBlockHandler != null) {
            this.mBlockHandler.blockEnd(this, node);
        } else if (this.hasNext(node)) {
            this.ensureNewLine();
            this.forceNewLine();
        }
    }

    @Nullable
    public <N extends Node> Object preSetSpans(@NonNull N node, int offset) {
        SpanFactory<Node> factory = this.mConfig.getSpanFactory(node.getClass());
        if (factory != null) {
            Object spans = factory.create(this.mConfig, node, this.mRenderArguments);
            this.setSpans0(spans, offset, offset, 34);
            return spans;
        } else {
            return null;
        }
    }

    public void postSetSpans(@Nullable Object spans, int offset) {
        this.setSpans0(spans, offset, this.mBuilder.length(), 33);
    }

    private void setSpans0(Object spans, int start, int end, int flags) {
        if (spans != null) {
            if (spans.getClass().isArray()) {
                for (Object span : (Object[]) spans) {
                    this.setSpans0(span, start, end, flags);
                }
            } else {
                this.mBuilder.setSpan(spans, start, end, flags);
            }
        }
    }
}
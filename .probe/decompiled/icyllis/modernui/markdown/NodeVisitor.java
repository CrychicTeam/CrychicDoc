package icyllis.modernui.markdown;

import com.vladsch.flexmark.util.ast.Node;
import icyllis.modernui.annotation.NonNull;

@FunctionalInterface
public interface NodeVisitor<N extends Node> {

    void visit(@NonNull MarkdownVisitor var1, @NonNull N var2);
}
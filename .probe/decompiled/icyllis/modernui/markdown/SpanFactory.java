package icyllis.modernui.markdown;

import com.vladsch.flexmark.util.ast.Node;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.util.DataSet;

@FunctionalInterface
public interface SpanFactory<N extends Node> {

    Object create(@NonNull MarkdownConfig var1, @NonNull N var2, @NonNull DataSet var3);
}
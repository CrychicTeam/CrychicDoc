package icyllis.modernui.markdown;

import com.vladsch.flexmark.util.ast.Node;
import icyllis.modernui.annotation.NonNull;

public interface BlockHandler {

    void blockStart(@NonNull MarkdownVisitor var1, @NonNull Node var2);

    void blockEnd(@NonNull MarkdownVisitor var1, @NonNull Node var2);
}
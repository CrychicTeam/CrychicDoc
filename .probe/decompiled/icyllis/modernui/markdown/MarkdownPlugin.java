package icyllis.modernui.markdown;

import com.vladsch.flexmark.parser.Parser.Builder;
import com.vladsch.flexmark.util.ast.Node;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.widget.TextView;
import java.util.function.Consumer;

public interface MarkdownPlugin {

    default void configure(@NonNull MarkdownPlugin.Registry registry) {
    }

    default void configureParser(@NonNull Builder builder) {
    }

    default void configureTheme(@NonNull MarkdownTheme.Builder builder) {
    }

    default void configureConfig(@NonNull MarkdownConfig.Builder builder) {
    }

    @NonNull
    default String processMarkdown(@NonNull String markdown) {
        return markdown;
    }

    default void beforeRender(@NonNull Node document) {
    }

    default void afterRender(@NonNull Node document, @NonNull MarkdownVisitor visitor) {
    }

    default void beforeSetText(@NonNull TextView textView, @NonNull Spanned markdown) {
    }

    default void afterSetText(@NonNull TextView textView) {
    }

    public interface Registry {

        @NonNull
        <P extends MarkdownPlugin> P require(@NonNull Class<P> var1);

        <P extends MarkdownPlugin> void require(@NonNull Class<P> var1, @NonNull Consumer<? super P> var2);
    }
}
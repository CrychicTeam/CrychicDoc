package icyllis.modernui.markdown;

import com.vladsch.flexmark.util.ast.Node;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.util.DataSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class MarkdownConfig {

    final MarkdownTheme mTheme;

    final Map<Class<? extends Node>, NodeVisitor<Node>> mVisitors;

    @Nullable
    final BlockHandler mBlockHandler;

    private final Map<Class<? extends Node>, SpanFactory<Node>> mSpanFactories;

    @NonNull
    public static MarkdownConfig.Builder builder() {
        return new MarkdownConfig.Builder();
    }

    private MarkdownConfig(MarkdownTheme theme, Map<Class<? extends Node>, NodeVisitor<Node>> visitors, @Nullable BlockHandler blockHandler, Map<Class<? extends Node>, SpanFactory<Node>> spanFactories) {
        this.mTheme = theme;
        this.mVisitors = new HashMap(visitors);
        this.mBlockHandler = blockHandler;
        this.mSpanFactories = new HashMap(spanFactories);
    }

    public MarkdownTheme theme() {
        return this.mTheme;
    }

    SpanFactory<Node> getSpanFactory(Class<? extends Node> clazz) {
        return (SpanFactory<Node>) this.mSpanFactories.get(clazz);
    }

    public static final class Builder {

        private final HashMap<Class<? extends Node>, NodeVisitor<Node>> mVisitors = new HashMap();

        private final HashMap<Class<? extends Node>, SpanFactory<Node>> mSpanFactories = new HashMap();

        private BlockHandler mBlockHandler;

        Builder() {
        }

        @NonNull
        public <N extends Node> MarkdownConfig.Builder addVisitor(@NonNull Class<? extends N> clazz, @Nullable NodeVisitor<? super N> visitor) {
            if (visitor == null) {
                this.mVisitors.remove(clazz);
            } else {
                this.mVisitors.put(clazz, visitor);
            }
            return this;
        }

        @NonNull
        public <N extends Node> MarkdownConfig.Builder setSpanFactory(@NonNull Class<? extends N> clazz, @Nullable SpanFactory<? super N> factory) {
            if (factory == null) {
                this.mSpanFactories.remove(clazz);
            } else {
                this.mSpanFactories.put(clazz, factory);
            }
            return this;
        }

        @NonNull
        public <N extends Node> MarkdownConfig.Builder appendSpanFactory(@NonNull Class<? extends N> clazz, @NonNull SpanFactory<? super N> factory) {
            Objects.requireNonNull(factory);
            SpanFactory<Node> oldFactory = (SpanFactory<Node>) this.mSpanFactories.get(clazz);
            if (oldFactory != null) {
                if (oldFactory instanceof MarkdownConfig.Builder.CompositeSpanFactory<Node> list) {
                    list.add(factory);
                } else {
                    this.mSpanFactories.put(clazz, new MarkdownConfig.Builder.CompositeSpanFactory<>(oldFactory, factory));
                }
            } else {
                this.mSpanFactories.put(clazz, factory);
            }
            return this;
        }

        @NonNull
        public <N extends Node> MarkdownConfig.Builder prependSpanFactory(@NonNull Class<? extends N> clazz, @NonNull SpanFactory<? super N> factory) {
            Objects.requireNonNull(factory);
            SpanFactory<Node> oldFactory = (SpanFactory<Node>) this.mSpanFactories.get(clazz);
            if (oldFactory != null) {
                if (oldFactory instanceof MarkdownConfig.Builder.CompositeSpanFactory<Node> list) {
                    list.add(0, factory);
                } else {
                    this.mSpanFactories.put(clazz, new MarkdownConfig.Builder.CompositeSpanFactory<>(factory, oldFactory));
                }
            } else {
                this.mSpanFactories.put(clazz, factory);
            }
            return this;
        }

        @Nullable
        public <N extends Node> SpanFactory<N> getSpanFactory(@NonNull Class<N> node) {
            return (SpanFactory<N>) this.mSpanFactories.get(node);
        }

        @NonNull
        public MarkdownConfig.Builder setBlockHandler(@Nullable BlockHandler blockHandler) {
            this.mBlockHandler = blockHandler;
            return this;
        }

        @NonNull
        public MarkdownConfig build(MarkdownTheme theme) {
            return new MarkdownConfig(theme, this.mVisitors, this.mBlockHandler, this.mSpanFactories);
        }

        static class CompositeSpanFactory<N extends Node> extends ArrayList<SpanFactory<N>> implements SpanFactory<N> {

            public CompositeSpanFactory(SpanFactory<N> first, SpanFactory<N> second) {
                super(3);
                this.add(first);
                this.add(second);
            }

            @Override
            public Object create(@NonNull MarkdownConfig config, @NonNull N node, @NonNull DataSet args) {
                int n = this.size();
                Object[] spans = new Object[n];
                for (int i = 0; i < n; i++) {
                    spans[i] = ((SpanFactory) this.get(i)).create(config, node, args);
                }
                return spans;
            }
        }
    }
}
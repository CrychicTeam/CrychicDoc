package icyllis.modernui.markdown;

import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.Node;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.core.Context;
import icyllis.modernui.markdown.core.CorePlugin;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import org.jetbrains.annotations.UnmodifiableView;

public final class Markdown {

    private final Parser mParser;

    private final MarkdownConfig mConfig;

    @UnmodifiableView
    private final List<MarkdownPlugin> mPlugins;

    private final TextView.BufferType mBufferType;

    @Nullable
    private final TextSetter mTextSetter;

    Markdown(Parser parser, MarkdownConfig config, List<MarkdownPlugin> plugins, TextView.BufferType bufferType, @Nullable TextSetter textSetter) {
        this.mParser = parser;
        this.mConfig = config;
        this.mPlugins = plugins;
        this.mBufferType = bufferType;
        this.mTextSetter = textSetter;
    }

    @NonNull
    public static Markdown create(@NonNull Context context) {
        return new Markdown.Builder(context).usePlugin(CorePlugin.create()).build();
    }

    @NonNull
    public static Markdown.Builder builder(@NonNull Context context) {
        return new Markdown.Builder(context);
    }

    @NonNull
    public Document parse(@NonNull String input) {
        for (MarkdownPlugin plugin : this.mPlugins) {
            input = plugin.processMarkdown(input);
        }
        return this.mParser.parse(input);
    }

    @NonNull
    public Spanned render(@NonNull Node document) {
        for (MarkdownPlugin plugin : this.mPlugins) {
            plugin.beforeRender(document);
        }
        MarkdownVisitor visitor = new MarkdownVisitor(this.mConfig);
        visitor.visit(document);
        for (MarkdownPlugin plugin : this.mPlugins) {
            plugin.afterRender(document, visitor);
        }
        return visitor.builder();
    }

    @NonNull
    public Spanned convert(@NonNull String input) {
        return this.render(this.parse(input));
    }

    @UiThread
    public void setMarkdown(@NonNull TextView textView, @NonNull String markdown) {
        this.setParsedMarkdown(textView, this.convert(markdown));
    }

    @UiThread
    public void setParsedMarkdown(@NonNull TextView textView, @NonNull Spanned markdown) {
        for (MarkdownPlugin plugin : this.mPlugins) {
            plugin.beforeSetText(textView, markdown);
        }
        if (this.mTextSetter != null) {
            this.mTextSetter.setText(textView, markdown, this.mBufferType, () -> {
                for (MarkdownPlugin pluginx : this.mPlugins) {
                    pluginx.afterSetText(textView);
                }
            });
        } else {
            textView.setText(markdown, this.mBufferType);
            for (MarkdownPlugin plugin : this.mPlugins) {
                plugin.afterSetText(textView);
            }
        }
    }

    @Nullable
    public <P extends MarkdownPlugin> P getPlugin(@NonNull Class<P> type) {
        MarkdownPlugin out = null;
        for (MarkdownPlugin plugin : this.mPlugins) {
            if (type.isAssignableFrom(plugin.getClass())) {
                out = plugin;
            }
        }
        return (P) out;
    }

    @NonNull
    public <P extends MarkdownPlugin> P requirePlugin(@NonNull Class<P> type) {
        return (P) Objects.requireNonNull(this.getPlugin(type));
    }

    @NonNull
    @UnmodifiableView
    public List<MarkdownPlugin> getPlugins() {
        return this.mPlugins;
    }

    @NonNull
    public MarkdownConfig getConfig() {
        return this.mConfig;
    }

    public static final class Builder {

        private final Context mContext;

        private final LinkedHashSet<MarkdownPlugin> mPlugins = new LinkedHashSet(3);

        private TextView.BufferType mBufferType = TextView.BufferType.EDITABLE;

        private TextSetter mTextSetter;

        Builder(Context context) {
            this.mContext = (Context) Objects.requireNonNull(context);
        }

        @NonNull
        public Markdown.Builder setBufferType(@NonNull TextView.BufferType bufferType) {
            this.mBufferType = (TextView.BufferType) Objects.requireNonNull(bufferType);
            return this;
        }

        @NonNull
        public Markdown.Builder setTextSetter(@Nullable TextSetter textSetter) {
            this.mTextSetter = textSetter;
            return this;
        }

        @NonNull
        public Markdown.Builder usePlugin(@NonNull MarkdownPlugin plugin) {
            this.mPlugins.add((MarkdownPlugin) Objects.requireNonNull(plugin));
            return this;
        }

        @NonNull
        public Markdown build() {
            List<MarkdownPlugin> plugins = new Markdown.Builder.Registry(this.mPlugins).process();
            com.vladsch.flexmark.parser.Parser.Builder parserBuilder = Parser.builder();
            MarkdownTheme.Builder themeBuilder = MarkdownTheme.builderWithDefaults(this.mContext);
            MarkdownConfig.Builder configBuilder = MarkdownConfig.builder();
            for (MarkdownPlugin plugin : plugins) {
                plugin.configureParser(parserBuilder);
                plugin.configureTheme(themeBuilder);
                plugin.configureConfig(configBuilder);
            }
            return new Markdown(parserBuilder.build(), configBuilder.build(themeBuilder.build()), Collections.unmodifiableList(plugins), this.mBufferType, this.mTextSetter);
        }

        static class Registry implements MarkdownPlugin.Registry {

            private final LinkedHashSet<MarkdownPlugin> mAll;

            private final HashSet<MarkdownPlugin> mLoaded;

            private final HashSet<MarkdownPlugin> mVisited;

            private final ArrayList<MarkdownPlugin> mResults = new ArrayList();

            Registry(LinkedHashSet<MarkdownPlugin> all) {
                this.mAll = all;
                this.mLoaded = new HashSet(all.size());
                this.mVisited = new HashSet();
            }

            ArrayList<MarkdownPlugin> process() {
                for (MarkdownPlugin plugin : this.mAll) {
                    this.load(plugin);
                }
                this.mResults.trimToSize();
                return this.mResults;
            }

            @NonNull
            @Override
            public <P extends MarkdownPlugin> P require(@NonNull Class<P> clazz) {
                return this.get(clazz);
            }

            @Override
            public <P extends MarkdownPlugin> void require(@NonNull Class<P> clazz, @NonNull Consumer<? super P> action) {
                action.accept(this.get(clazz));
            }

            private void load(MarkdownPlugin plugin) {
                if (!this.mLoaded.contains(plugin)) {
                    if (!this.mVisited.add(plugin)) {
                        throw new IllegalStateException("Cyclic dependency chain found: " + this.mVisited);
                    }
                    plugin.configure(this);
                    this.mVisited.remove(plugin);
                    if (this.mLoaded.add(plugin)) {
                        if (plugin.getClass() == CorePlugin.class) {
                            this.mResults.add(0, plugin);
                        } else {
                            this.mResults.add(plugin);
                        }
                    }
                }
            }

            @NonNull
            private <P extends MarkdownPlugin> P get(@NonNull Class<? extends P> clazz) {
                P plugin = this.find(this.mLoaded, clazz);
                if (plugin == null) {
                    plugin = this.find(this.mAll, clazz);
                    if (plugin == null) {
                        throw new IllegalStateException("Requested plugin is not added: " + clazz.getName() + ", plugins: " + this.mAll);
                    }
                    this.load(plugin);
                }
                return plugin;
            }

            @Nullable
            private <P extends MarkdownPlugin> P find(Set<MarkdownPlugin> set, @NonNull Class<? extends P> clazz) {
                for (MarkdownPlugin plugin : set) {
                    if (clazz.isAssignableFrom(plugin.getClass())) {
                        return (P) plugin;
                    }
                }
                return null;
            }
        }
    }
}
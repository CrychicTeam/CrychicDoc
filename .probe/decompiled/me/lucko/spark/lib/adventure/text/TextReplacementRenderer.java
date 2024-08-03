package me.lucko.spark.lib.adventure.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.renderer.ComponentRenderer;
import org.jetbrains.annotations.NotNull;

final class TextReplacementRenderer implements ComponentRenderer<TextReplacementRenderer.State> {

    static final TextReplacementRenderer INSTANCE = new TextReplacementRenderer();

    private TextReplacementRenderer() {
    }

    @NotNull
    public Component render(@NotNull final Component component, @NotNull final TextReplacementRenderer.State state) {
        if (!state.running) {
            return component;
        } else {
            boolean prevFirstMatch = state.firstMatch;
            state.firstMatch = true;
            List<Component> oldChildren = component.children();
            int oldChildrenSize = oldChildren.size();
            Style oldStyle = component.style();
            List<Component> children = null;
            Component modified = component;
            if (component instanceof TextComponent) {
                String content = ((TextComponent) component).content();
                Matcher matcher = state.pattern.matcher(content);
                int replacedUntil = 0;
                while (matcher.find()) {
                    PatternReplacementResult result = state.continuer.shouldReplace(matcher, ++state.matchCount, state.replaceCount);
                    if (result != PatternReplacementResult.CONTINUE) {
                        if (result == PatternReplacementResult.STOP) {
                            state.running = false;
                            break;
                        }
                        if (matcher.start() == 0) {
                            if (matcher.end() == content.length()) {
                                ComponentLike replacement = (ComponentLike) state.replacement.apply(matcher, Component.text().content(matcher.group()).style(component.style()));
                                modified = (Component) (replacement == null ? Component.empty() : replacement.asComponent());
                                if (modified.style().hoverEvent() != null) {
                                    oldStyle = oldStyle.hoverEvent(null);
                                }
                                modified = modified.style(modified.style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
                                if (children == null) {
                                    children = new ArrayList(oldChildrenSize + modified.children().size());
                                    children.addAll(modified.children());
                                }
                            } else {
                                modified = Component.text("", component.style());
                                ComponentLike child = (ComponentLike) state.replacement.apply(matcher, Component.text().content(matcher.group()));
                                if (child != null) {
                                    if (children == null) {
                                        children = new ArrayList(oldChildrenSize + 1);
                                    }
                                    children.add(child.asComponent());
                                }
                            }
                        } else {
                            if (children == null) {
                                children = new ArrayList(oldChildrenSize + 2);
                            }
                            if (state.firstMatch) {
                                modified = ((TextComponent) component).content(content.substring(0, matcher.start()));
                            } else if (replacedUntil < matcher.start()) {
                                children.add(Component.text(content.substring(replacedUntil, matcher.start())));
                            }
                            ComponentLike builder = (ComponentLike) state.replacement.apply(matcher, Component.text().content(matcher.group()));
                            if (builder != null) {
                                children.add(builder.asComponent());
                            }
                        }
                        state.replaceCount++;
                        state.firstMatch = false;
                        replacedUntil = matcher.end();
                    }
                }
                if (replacedUntil < content.length() && replacedUntil > 0) {
                    if (children == null) {
                        children = new ArrayList(oldChildrenSize);
                    }
                    children.add(Component.text(content.substring(replacedUntil)));
                }
            } else if (component instanceof TranslatableComponent) {
                List<Component> args = ((TranslatableComponent) component).args();
                List<Component> newArgs = null;
                int i = 0;
                for (int size = args.size(); i < size; i++) {
                    Component original = (Component) args.get(i);
                    Component replaced = this.render(original, state);
                    if (replaced != component && newArgs == null) {
                        newArgs = new ArrayList(size);
                        if (i > 0) {
                            newArgs.addAll(args.subList(0, i));
                        }
                    }
                    if (newArgs != null) {
                        newArgs.add(replaced);
                    }
                }
                if (newArgs != null) {
                    modified = ((TranslatableComponent) component).args(newArgs);
                }
            }
            if (state.running) {
                HoverEvent<?> event = oldStyle.hoverEvent();
                if (event != null) {
                    HoverEvent<?> rendered = event.withRenderedValue(this, state);
                    if (event != rendered) {
                        modified = modified.style(s -> s.hoverEvent(rendered));
                    }
                }
                boolean first = true;
                for (int i = 0; i < oldChildrenSize; i++) {
                    Component child = (Component) oldChildren.get(i);
                    Component replacedx = this.render(child, state);
                    if (replacedx != child) {
                        if (children == null) {
                            children = new ArrayList(oldChildrenSize);
                        }
                        if (first) {
                            children.addAll(oldChildren.subList(0, i));
                        }
                        first = false;
                    }
                    if (children != null) {
                        children.add(replacedx);
                        first = false;
                    }
                }
            } else if (children != null) {
                children.addAll(oldChildren);
            }
            state.firstMatch = prevFirstMatch;
            return children != null ? modified.children(children) : modified;
        }
    }

    static final class State {

        final Pattern pattern;

        final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement;

        final TextReplacementConfig.Condition continuer;

        boolean running = true;

        int matchCount = 0;

        int replaceCount = 0;

        boolean firstMatch = true;

        State(@NotNull final Pattern pattern, @NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement, @NotNull final TextReplacementConfig.Condition continuer) {
            this.pattern = pattern;
            this.replacement = replacement;
            this.continuer = continuer;
        }
    }
}
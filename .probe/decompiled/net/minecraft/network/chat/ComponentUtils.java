package net.minecraft.network.chat;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.DataFixUtils;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.Entity;

public class ComponentUtils {

    public static final String DEFAULT_SEPARATOR_TEXT = ", ";

    public static final Component DEFAULT_SEPARATOR = Component.literal(", ").withStyle(ChatFormatting.GRAY);

    public static final Component DEFAULT_NO_STYLE_SEPARATOR = Component.literal(", ");

    public static MutableComponent mergeStyles(MutableComponent mutableComponent0, Style style1) {
        if (style1.isEmpty()) {
            return mutableComponent0;
        } else {
            Style $$2 = mutableComponent0.getStyle();
            if ($$2.isEmpty()) {
                return mutableComponent0.setStyle(style1);
            } else {
                return $$2.equals(style1) ? mutableComponent0 : mutableComponent0.setStyle($$2.applyTo(style1));
            }
        }
    }

    public static Optional<MutableComponent> updateForEntity(@Nullable CommandSourceStack commandSourceStack0, Optional<Component> optionalComponent1, @Nullable Entity entity2, int int3) throws CommandSyntaxException {
        return optionalComponent1.isPresent() ? Optional.of(updateForEntity(commandSourceStack0, (Component) optionalComponent1.get(), entity2, int3)) : Optional.empty();
    }

    public static MutableComponent updateForEntity(@Nullable CommandSourceStack commandSourceStack0, Component component1, @Nullable Entity entity2, int int3) throws CommandSyntaxException {
        if (int3 > 100) {
            return component1.copy();
        } else {
            MutableComponent $$4 = component1.getContents().resolve(commandSourceStack0, entity2, int3 + 1);
            for (Component $$5 : component1.getSiblings()) {
                $$4.append(updateForEntity(commandSourceStack0, $$5, entity2, int3 + 1));
            }
            return $$4.withStyle(resolveStyle(commandSourceStack0, component1.getStyle(), entity2, int3));
        }
    }

    private static Style resolveStyle(@Nullable CommandSourceStack commandSourceStack0, Style style1, @Nullable Entity entity2, int int3) throws CommandSyntaxException {
        HoverEvent $$4 = style1.getHoverEvent();
        if ($$4 != null) {
            Component $$5 = $$4.getValue(HoverEvent.Action.SHOW_TEXT);
            if ($$5 != null) {
                HoverEvent $$6 = new HoverEvent(HoverEvent.Action.SHOW_TEXT, updateForEntity(commandSourceStack0, $$5, entity2, int3 + 1));
                return style1.withHoverEvent($$6);
            }
        }
        return style1;
    }

    public static Component getDisplayName(GameProfile gameProfile0) {
        if (gameProfile0.getName() != null) {
            return Component.literal(gameProfile0.getName());
        } else {
            return gameProfile0.getId() != null ? Component.literal(gameProfile0.getId().toString()) : Component.literal("(unknown)");
        }
    }

    public static Component formatList(Collection<String> collectionString0) {
        return formatAndSortList(collectionString0, p_130742_ -> Component.literal(p_130742_).withStyle(ChatFormatting.GREEN));
    }

    public static <T extends Comparable<T>> Component formatAndSortList(Collection<T> collectionT0, Function<T, Component> functionTComponent1) {
        if (collectionT0.isEmpty()) {
            return CommonComponents.EMPTY;
        } else if (collectionT0.size() == 1) {
            return (Component) functionTComponent1.apply((Comparable) collectionT0.iterator().next());
        } else {
            List<T> $$2 = Lists.newArrayList(collectionT0);
            $$2.sort(Comparable::compareTo);
            return formatList($$2, functionTComponent1);
        }
    }

    public static <T> Component formatList(Collection<? extends T> collectionExtendsT0, Function<T, Component> functionTComponent1) {
        return formatList(collectionExtendsT0, DEFAULT_SEPARATOR, functionTComponent1);
    }

    public static <T> MutableComponent formatList(Collection<? extends T> collectionExtendsT0, Optional<? extends Component> optionalExtendsComponent1, Function<T, Component> functionTComponent2) {
        return formatList(collectionExtendsT0, (Component) DataFixUtils.orElse(optionalExtendsComponent1, DEFAULT_SEPARATOR), functionTComponent2);
    }

    public static Component formatList(Collection<? extends Component> collectionExtendsComponent0, Component component1) {
        return formatList(collectionExtendsComponent0, component1, Function.identity());
    }

    public static <T> MutableComponent formatList(Collection<? extends T> collectionExtendsT0, Component component1, Function<T, Component> functionTComponent2) {
        if (collectionExtendsT0.isEmpty()) {
            return Component.empty();
        } else if (collectionExtendsT0.size() == 1) {
            return ((Component) functionTComponent2.apply(collectionExtendsT0.iterator().next())).copy();
        } else {
            MutableComponent $$3 = Component.empty();
            boolean $$4 = true;
            for (T $$5 : collectionExtendsT0) {
                if (!$$4) {
                    $$3.append(component1);
                }
                $$3.append((Component) functionTComponent2.apply($$5));
                $$4 = false;
            }
            return $$3;
        }
    }

    public static MutableComponent wrapInSquareBrackets(Component component0) {
        return Component.translatable("chat.square_brackets", component0);
    }

    public static Component fromMessage(Message message0) {
        return (Component) (message0 instanceof Component ? (Component) message0 : Component.literal(message0.getString()));
    }

    public static boolean isTranslationResolvable(@Nullable Component component0) {
        if (component0 != null && component0.getContents() instanceof TranslatableContents $$1) {
            String $$2 = $$1.getKey();
            String $$3 = $$1.getFallback();
            return $$3 != null || Language.getInstance().has($$2);
        } else {
            return true;
        }
    }

    public static MutableComponent copyOnClickText(String string0) {
        return wrapInSquareBrackets(Component.literal(string0).withStyle(p_258207_ -> p_258207_.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, string0)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.copy.click"))).withInsertion(string0)));
    }
}
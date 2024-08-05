package snownee.loquat.command.argument;

import com.google.common.collect.Maps;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.network.chat.Component;

public class AreaSelectorOptions {

    private static final Map<String, AreaSelectorOptions.Option> OPTIONS = Maps.newHashMap();

    private static void register(String name, AreaSelectorOptions.Modifier modifier, Predicate<AreaSelectorParser> canUse) {
        register(name, modifier, canUse, Component.translatable("loquat.argument.area.options.%s.description".formatted(name)));
    }

    public static void register(String name, AreaSelectorOptions.Modifier modifier, Predicate<AreaSelectorParser> canUse, Component description) {
        OPTIONS.put(name, new AreaSelectorOptions.Option(modifier, canUse, description));
    }

    public static AreaSelectorOptions.Modifier get(AreaSelectorParser parser, String name, int cursor) throws CommandSyntaxException {
        AreaSelectorOptions.Option option = (AreaSelectorOptions.Option) OPTIONS.get(name);
        if (option != null) {
            if (option.canUse.test(parser)) {
                return option.modifier;
            } else {
                throw EntitySelectorOptions.ERROR_INAPPLICABLE_OPTION.createWithContext(parser.getReader(), name);
            }
        } else {
            parser.getReader().setCursor(cursor);
            throw EntitySelectorOptions.ERROR_UNKNOWN_OPTION.createWithContext(parser.getReader(), name);
        }
    }

    public static void suggestNames(AreaSelectorParser parser, SuggestionsBuilder builder) {
        String s = builder.getRemaining().toLowerCase(Locale.ROOT);
        for (Entry<String, AreaSelectorOptions.Option> entry : OPTIONS.entrySet()) {
            if (((AreaSelectorOptions.Option) entry.getValue()).canUse.test(parser) && ((String) entry.getKey()).toLowerCase(Locale.ROOT).startsWith(s)) {
                builder.suggest((String) entry.getKey() + "=", ((AreaSelectorOptions.Option) entry.getValue()).description);
            }
        }
    }

    public static void bootstrap() {
        if (OPTIONS.isEmpty()) {
            register("tag", AreaSelectorParser::thenTag, p -> true);
            register("limit", AreaSelectorParser::thenLimit, p -> !p.isLimited());
            register("distance", AreaSelectorParser::thenDistance, p -> p.getDistance().m_55327_());
            register("sort", AreaSelectorParser::thenSort, p -> !p.isSelectedAreas() && !p.isSorted());
            register("nbt", AreaSelectorParser::thenNBT, p -> true);
            register("x", p -> p.setX(p.getReader().readDouble()), p -> p.getX() == null);
            register("y", p -> p.setY(p.getReader().readDouble()), p -> p.getY() == null);
            register("z", p -> p.setZ(p.getReader().readDouble()), p -> p.getZ() == null);
            register("dx", p -> p.setDeltaX(p.getReader().readDouble()), p -> p.getDeltaX() == null);
            register("dy", p -> p.setDeltaY(p.getReader().readDouble()), p -> p.getDeltaY() == null);
            register("dz", p -> p.setDeltaZ(p.getReader().readDouble()), p -> p.getDeltaZ() == null);
        }
    }

    public interface Modifier {

        void handle(AreaSelectorParser var1) throws CommandSyntaxException;
    }

    static record Option(AreaSelectorOptions.Modifier modifier, Predicate<AreaSelectorParser> canUse, Component description) {
    }
}
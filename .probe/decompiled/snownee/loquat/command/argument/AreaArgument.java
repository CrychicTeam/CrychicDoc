package snownee.loquat.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import snownee.loquat.core.area.Area;

public class AreaArgument implements ArgumentType<AreaSelector> {

    private static final Collection<String> EXAMPLES = List.of("@p", "@a[limit=1]", "dd12be42-52a9-4a91-a8a1-11c01849e498");

    public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_AREA = new SimpleCommandExceptionType(Component.translatable("loquat.argument.area.tooMany"));

    public static final SimpleCommandExceptionType NO_AREAS_FOUND = new SimpleCommandExceptionType(Component.translatable("loquat.argument.area.notFound"));

    private final boolean single;

    public static AreaArgument area() {
        return new AreaArgument(true);
    }

    public static Area getArea(CommandContext<CommandSourceStack> context, String s) throws CommandSyntaxException {
        return ((AreaSelector) context.getArgument(s, AreaSelector.class)).findSingleArea((CommandSourceStack) context.getSource());
    }

    public static AreaArgument areas() {
        return new AreaArgument(false);
    }

    public static Collection<? extends Area> getAreas(CommandContext<CommandSourceStack> context, String s) throws CommandSyntaxException {
        Collection<? extends Area> collection = getOptionalAreas(context, s);
        if (collection.isEmpty()) {
            throw NO_AREAS_FOUND.create();
        } else {
            return collection;
        }
    }

    public static Collection<? extends Area> getOptionalAreas(CommandContext<CommandSourceStack> context, String s) throws CommandSyntaxException {
        return ((AreaSelector) context.getArgument(s, AreaSelector.class)).findAreas((CommandSourceStack) context.getSource());
    }

    protected AreaArgument(boolean single) {
        this.single = single;
    }

    public AreaSelector parse(StringReader reader) throws CommandSyntaxException {
        AreaSelectorParser parser = new AreaSelectorParser(reader);
        AreaSelector selector = parser.parse();
        if (!selector.selectedAreas() && selector.maxResults() > 1 && this.single) {
            reader.setCursor(0);
            throw ERROR_NOT_SINGLE_AREA.createWithContext(reader);
        } else {
            return selector;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider suggestionProvider) {
            StringReader reader = new StringReader(builder.getInput());
            reader.setCursor(builder.getStart());
            AreaSelectorParser parser = new AreaSelectorParser(reader);
            try {
                parser.parse();
            } catch (CommandSyntaxException var8) {
            }
            return parser.fillSuggestions(builder, builder1 -> SharedSuggestionProvider.suggest(suggestionProvider.getSelectedEntities(), builder1));
        } else {
            return Suggestions.empty();
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info implements ArgumentTypeInfo<AreaArgument, AreaArgument.Template> {

        public void serializeToNetwork(AreaArgument.Template template, FriendlyByteBuf buf) {
            int i = 0;
            if (template.single) {
                i |= 1;
            }
            buf.writeByte(i);
        }

        public AreaArgument.Template deserializeFromNetwork(FriendlyByteBuf buf) {
            byte b = buf.readByte();
            boolean single = (b & 1) != 0;
            return new AreaArgument.Template(this, single);
        }

        public void serializeToJson(AreaArgument.Template template, JsonObject jsonObject) {
            jsonObject.addProperty("amount", template.single ? "single" : "multiple");
        }

        public AreaArgument.Template unpack(AreaArgument argument) {
            return new AreaArgument.Template(this, argument.single);
        }
    }

    public static final class Template implements ArgumentTypeInfo.Template<AreaArgument> {

        private final AreaArgument.Info info;

        private final boolean single;

        Template(AreaArgument.Info info, boolean single) {
            this.info = info;
            this.single = single;
        }

        public AreaArgument instantiate(CommandBuildContext context) {
            return new AreaArgument(this.single);
        }

        @Override
        public ArgumentTypeInfo<AreaArgument, ?> type() {
            return this.info;
        }
    }
}
package dev.ftb.mods.ftbquests.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class QuestObjectArgument implements ArgumentType<QuestObjectBase> {

    private static final List<String> examples = ImmutableList.of("1CF239D256879E6F", "#importantquests");

    public static final SimpleCommandExceptionType NO_FILE = new SimpleCommandExceptionType(Component.translatable("commands.ftbquests.command.error.no_file"));

    public static final DynamicCommandExceptionType NO_OBJECT = new DynamicCommandExceptionType(object -> Component.translatable("commands.ftbquests.command.error.no_object", object));

    public static final DynamicCommandExceptionType INVALID_ID = new DynamicCommandExceptionType(id -> Component.translatable("commands.ftbquests.command.error.invalid_id", id));

    private final Predicate<QuestObjectBase> filter;

    public QuestObjectArgument() {
        this(qo -> true);
    }

    public QuestObjectArgument(Predicate<QuestObjectBase> filter) {
        this.filter = filter;
    }

    public QuestObjectBase parse(StringReader reader) throws CommandSyntaxException {
        String id = reader.readString();
        BaseQuestFile file = findQuestFile();
        if (file != null) {
            if (id.startsWith("#")) {
                String val = id.substring(1);
                for (QuestObjectBase object : file.getAllObjects()) {
                    if (object.hasTag(val) && this.filter.test(object)) {
                        return object;
                    }
                }
                throw NO_OBJECT.createWithContext(reader, id);
            } else {
                try {
                    long num = file.getID(id);
                    QuestObjectBase objectx = file.getBase(num);
                    if (objectx != null && this.filter.test(objectx)) {
                        return objectx;
                    } else {
                        throw NO_OBJECT.createWithContext(reader, id);
                    }
                } catch (NumberFormatException var7) {
                    throw INVALID_ID.createWithContext(reader, id);
                }
            }
        } else {
            throw NO_FILE.create();
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        BaseQuestFile file = findQuestFile();
        return file != null ? SharedSuggestionProvider.suggest(file.getAllObjects().stream().filter(this.filter).map(QuestObjectBase::getCodeString), builder) : Suggestions.empty();
    }

    public Collection<String> getExamples() {
        return examples;
    }

    public static QuestObjectArgument questObject() {
        return new QuestObjectArgument();
    }

    public static QuestObjectArgument questObject(Predicate<QuestObjectBase> filter) {
        return new QuestObjectArgument(filter);
    }

    @Nullable
    private static BaseQuestFile findQuestFile() {
        if (!QuestObjectBase.isNull(ServerQuestFile.INSTANCE)) {
            return ServerQuestFile.INSTANCE;
        } else {
            return !QuestObjectBase.isNull(ClientQuestFile.INSTANCE) ? ClientQuestFile.INSTANCE : null;
        }
    }
}
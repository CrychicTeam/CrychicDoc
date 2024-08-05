package io.github.lightman314.lightmanscurrency.common.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class TraderArgument implements ArgumentType<TraderData> {

    private static final SimpleCommandExceptionType ERROR_NOT_FOUND = new SimpleCommandExceptionType(LCText.ARGUMENT_TRADER_NOT_FOUND.get());

    private final boolean acceptPersistentIDs;

    private TraderArgument(boolean acceptPersistentIDs) {
        this.acceptPersistentIDs = acceptPersistentIDs;
    }

    public static TraderArgument trader() {
        return new TraderArgument(false);
    }

    public static TraderArgument traderWithPersistent() {
        return new TraderArgument(true);
    }

    public static TraderData getTrader(CommandContext<CommandSourceStack> commandContext, String name) throws CommandSyntaxException {
        return (TraderData) commandContext.getArgument(name, TraderData.class);
    }

    public TraderData parse(StringReader reader) throws CommandSyntaxException {
        String traderID = reader.readUnquotedString();
        if (isNumerical(traderID)) {
            try {
                long id = Long.parseLong(traderID);
                if (id >= 0L) {
                    TraderData t = TraderSaveData.GetTrader(false, id);
                    if (t != null) {
                        return t;
                    }
                }
            } catch (Throwable var6) {
            }
        }
        if (this.acceptPersistentIDs) {
            for (TraderData t : TraderSaveData.GetAllTraders(false)) {
                if (t.isPersistent() && t.getPersistentID().equals(traderID)) {
                    return t;
                }
            }
        }
        throw ERROR_NOT_FOUND.createWithContext(reader);
    }

    private static boolean isNumerical(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        for (TraderData t : TraderSaveData.GetAllTraders(false)) {
            suggestionsBuilder.suggest(String.valueOf(t.getID()));
            if (this.acceptPersistentIDs && t.isPersistent()) {
                suggestionsBuilder.suggest(t.getPersistentID());
            }
        }
        return suggestionsBuilder.buildFuture();
    }

    public static class Info implements ArgumentTypeInfo<TraderArgument, TraderArgument.Info.Template> {

        public void serializeToNetwork(TraderArgument.Info.Template template, FriendlyByteBuf buffer) {
            buffer.writeBoolean(template.acceptPersistentIDs);
        }

        @Nonnull
        public TraderArgument.Info.Template deserializeFromNetwork(FriendlyByteBuf buffer) {
            return new TraderArgument.Info.Template(buffer.readBoolean());
        }

        public void serializeToJson(TraderArgument.Info.Template template, JsonObject json) {
            json.addProperty("acceptPersistentIDs", template.acceptPersistentIDs);
        }

        @Nonnull
        public TraderArgument.Info.Template unpack(TraderArgument argument) {
            return new TraderArgument.Info.Template(argument.acceptPersistentIDs);
        }

        public final class Template implements ArgumentTypeInfo.Template<TraderArgument> {

            final boolean acceptPersistentIDs;

            Template(boolean checkPersistentIDs) {
                this.acceptPersistentIDs = checkPersistentIDs;
            }

            @Nonnull
            public TraderArgument instantiate(@Nonnull CommandBuildContext context) {
                return new TraderArgument(this.acceptPersistentIDs);
            }

            @Nonnull
            @Override
            public ArgumentTypeInfo<TraderArgument, ?> type() {
                return Info.this;
            }
        }
    }
}
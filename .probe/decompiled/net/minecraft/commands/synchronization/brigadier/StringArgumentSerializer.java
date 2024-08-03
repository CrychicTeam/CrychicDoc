package net.minecraft.commands.synchronization.brigadier;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType.StringType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;

public class StringArgumentSerializer implements ArgumentTypeInfo<StringArgumentType, StringArgumentSerializer.Template> {

    public void serializeToNetwork(StringArgumentSerializer.Template stringArgumentSerializerTemplate0, FriendlyByteBuf friendlyByteBuf1) {
        friendlyByteBuf1.writeEnum(stringArgumentSerializerTemplate0.type);
    }

    public StringArgumentSerializer.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        StringType $$1 = friendlyByteBuf0.readEnum(StringType.class);
        return new StringArgumentSerializer.Template($$1);
    }

    public void serializeToJson(StringArgumentSerializer.Template stringArgumentSerializerTemplate0, JsonObject jsonObject1) {
        jsonObject1.addProperty("type", switch(stringArgumentSerializerTemplate0.type) {
            case SINGLE_WORD ->
                "word";
            case QUOTABLE_PHRASE ->
                "phrase";
            case GREEDY_PHRASE ->
                "greedy";
            default ->
                throw new IncompatibleClassChangeError();
        });
    }

    public StringArgumentSerializer.Template unpack(StringArgumentType stringArgumentType0) {
        return new StringArgumentSerializer.Template(stringArgumentType0.getType());
    }

    public final class Template implements ArgumentTypeInfo.Template<StringArgumentType> {

        final StringType type;

        public Template(StringType stringType0) {
            this.type = stringType0;
        }

        public StringArgumentType instantiate(CommandBuildContext commandBuildContext0) {
            return switch(this.type) {
                case SINGLE_WORD ->
                    StringArgumentType.word();
                case QUOTABLE_PHRASE ->
                    StringArgumentType.string();
                case GREEDY_PHRASE ->
                    StringArgumentType.greedyString();
                default ->
                    throw new IncompatibleClassChangeError();
            };
        }

        @Override
        public ArgumentTypeInfo<StringArgumentType, ?> type() {
            return StringArgumentSerializer.this;
        }
    }
}
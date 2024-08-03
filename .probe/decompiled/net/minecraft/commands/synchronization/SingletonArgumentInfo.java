package net.minecraft.commands.synchronization;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

public class SingletonArgumentInfo<A extends ArgumentType<?>> implements ArgumentTypeInfo<A, SingletonArgumentInfo<A>.Template> {

    private final SingletonArgumentInfo<A>.Template template;

    private SingletonArgumentInfo(Function<CommandBuildContext, A> functionCommandBuildContextA0) {
        this.template = new SingletonArgumentInfo.Template(functionCommandBuildContextA0);
    }

    public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> contextFree(Supplier<T> supplierT0) {
        return new SingletonArgumentInfo<>(p_235455_ -> (ArgumentType) supplierT0.get());
    }

    public static <T extends ArgumentType<?>> SingletonArgumentInfo<T> contextAware(Function<CommandBuildContext, T> functionCommandBuildContextT0) {
        return new SingletonArgumentInfo<>(functionCommandBuildContextT0);
    }

    public void serializeToNetwork(SingletonArgumentInfo<A>.Template singletonArgumentInfoATemplate0, FriendlyByteBuf friendlyByteBuf1) {
    }

    public void serializeToJson(SingletonArgumentInfo<A>.Template singletonArgumentInfoATemplate0, JsonObject jsonObject1) {
    }

    public SingletonArgumentInfo<A>.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
        return this.template;
    }

    public SingletonArgumentInfo<A>.Template unpack(A a0) {
        return this.template;
    }

    public final class Template implements ArgumentTypeInfo.Template<A> {

        private final Function<CommandBuildContext, A> constructor;

        public Template(Function<CommandBuildContext, A> functionCommandBuildContextA0) {
            this.constructor = functionCommandBuildContextA0;
        }

        @Override
        public A instantiate(CommandBuildContext commandBuildContext0) {
            return (A) this.constructor.apply(commandBuildContext0);
        }

        @Override
        public ArgumentTypeInfo<A, ?> type() {
            return SingletonArgumentInfo.this;
        }
    }
}
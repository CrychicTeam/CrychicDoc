package fuzs.puzzleslib.impl.network;

import fuzs.puzzleslib.api.core.v1.ReflectionHelper;
import fuzs.puzzleslib.api.network.v2.MessageV2;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;

public final class NetworkHandlerImplHelper {

    private NetworkHandlerImplHelper() {
    }

    static <T extends MessageV2<T>> Function<FriendlyByteBuf, T> getMessageDecoder(Class<T> clazz) {
        Supplier<T> supplier = () -> (MessageV2) ((Optional) ReflectionHelper.newDefaultInstanceFactory(clazz).get()).orElseThrow();
        return (Function<FriendlyByteBuf, T>) Stream.of(clazz.getConstructors()).filter(currentConstructor -> currentConstructor.getParameterCount() == 1).filter(currentConstructor -> currentConstructor.getParameterTypes()[0] == FriendlyByteBuf.class).findAny().map(constructor -> buf -> (MessageV2) ReflectionHelper.newInstance(constructor, buf).orElseThrow()).orElseGet(() -> getDirectMessageDecoder(supplier));
    }

    static <T extends MessageV2<T>> Function<FriendlyByteBuf, T> getDirectMessageDecoder(Supplier<T> supplier) {
        return buf -> Util.make((MessageV2) supplier.get(), message -> message.read(buf));
    }
}
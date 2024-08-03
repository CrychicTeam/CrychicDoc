package fuzs.puzzleslib.impl.core;

import fuzs.puzzleslib.api.network.v3.ClientboundMessage;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent;

public interface ForgeProxy extends ProxyImpl {

    <T extends Record & ClientboundMessage<T>> void registerClientReceiverV2(T var1, Supplier<NetworkEvent.Context> var2);

    <T extends Record & ServerboundMessage<T>> void registerServerReceiverV2(T var1, Supplier<NetworkEvent.Context> var2);
}
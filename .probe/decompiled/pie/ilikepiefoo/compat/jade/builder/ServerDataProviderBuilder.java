package pie.ilikepiefoo.compat.jade.builder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.Accessor;

public class ServerDataProviderBuilder<T extends Accessor<?>> extends JadeProviderBuilder {

    private ServerDataProviderBuilder.AppendServerDataCallback<T> callback = ServerDataProviderBuilder::doNothing;

    public ServerDataProviderBuilder(ResourceLocation uniqueIdentifier) {
        super(uniqueIdentifier);
    }

    public static <T extends Accessor<?>> void doNothing(CompoundTag compoundTag, T accessor) {
    }

    public ServerDataProviderBuilder.AppendServerDataCallback<T> getCallback() {
        return this.callback;
    }

    public ServerDataProviderBuilder<T> setCallback(ServerDataProviderBuilder.AppendServerDataCallback<T> callback) {
        this.callback = callback;
        return this;
    }

    public interface AppendServerDataCallback<ACCESSOR extends Accessor<?>> {

        void appendServerData(CompoundTag var1, ACCESSOR var2);
    }
}
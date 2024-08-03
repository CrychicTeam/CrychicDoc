package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import snownee.lychee.LycheeRegistries;

public abstract class ContextualConditionType<T extends ContextualCondition> {

    public abstract T fromJson(JsonObject var1);

    public abstract void toJson(T var1, JsonObject var2);

    public abstract T fromNetwork(FriendlyByteBuf var1);

    public abstract void toNetwork(T var1, FriendlyByteBuf var2);

    public ResourceLocation getRegistryName() {
        return LycheeRegistries.CONTEXTUAL.getKey(this);
    }
}
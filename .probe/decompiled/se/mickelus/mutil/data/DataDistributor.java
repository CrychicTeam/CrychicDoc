package se.mickelus.mutil.data;

import com.google.gson.JsonElement;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface DataDistributor {

    void sendToAll(String var1, Map<ResourceLocation, JsonElement> var2);

    void sendToPlayer(ServerPlayer var1, String var2, Map<ResourceLocation, JsonElement> var3);
}
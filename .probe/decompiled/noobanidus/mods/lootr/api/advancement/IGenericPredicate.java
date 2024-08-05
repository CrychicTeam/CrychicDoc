package noobanidus.mods.lootr.api.advancement;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;

public interface IGenericPredicate<T> {

    boolean test(ServerPlayer var1, T var2);

    IGenericPredicate<T> deserialize(@Nullable JsonObject var1);
}
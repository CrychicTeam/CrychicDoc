package noobanidus.mods.lootr.advancement;

import com.google.gson.JsonObject;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import noobanidus.mods.lootr.api.advancement.IGenericPredicate;
import noobanidus.mods.lootr.data.DataStorage;

public class ContainerPredicate implements IGenericPredicate<UUID> {

    public boolean test(ServerPlayer player, UUID condition) {
        if (DataStorage.isAwarded(player.m_20148_(), condition)) {
            return false;
        } else {
            DataStorage.award(player.m_20148_(), condition);
            return true;
        }
    }

    @Override
    public IGenericPredicate<UUID> deserialize(@Nullable JsonObject element) {
        return new ContainerPredicate();
    }
}
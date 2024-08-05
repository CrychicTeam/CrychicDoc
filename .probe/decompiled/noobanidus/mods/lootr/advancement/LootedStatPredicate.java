package noobanidus.mods.lootr.advancement;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import noobanidus.mods.lootr.api.advancement.IGenericPredicate;
import noobanidus.mods.lootr.init.ModStats;

public class LootedStatPredicate implements IGenericPredicate<Void> {

    private int score = -1;

    public LootedStatPredicate() {
    }

    public LootedStatPredicate(int score) {
        this.score = score;
    }

    public boolean test(ServerPlayer player, Void condition) {
        return player.getStats().m_13015_(ModStats.LOOTED_STAT) >= this.score;
    }

    @Override
    public IGenericPredicate<Void> deserialize(@Nullable JsonObject element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        } else {
            int score = element.getAsJsonPrimitive("score").getAsInt();
            return new LootedStatPredicate(score);
        }
    }
}
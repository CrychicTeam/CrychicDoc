package snownee.loquat.spawner;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.post.PostActionType;
import snownee.lychee.core.recipe.ILycheeRecipe;

public class SpawnMobAction extends PostAction {

    public static final SpawnMobAction.Type TYPE = new SpawnMobAction.Type();

    private final MobEntry mob;

    private final int count;

    private final String zone;

    public SpawnMobAction(MobEntry mob, int count, String zone) {
        this.mob = mob;
        this.count = count;
        this.zone = zone;
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Preconditions.checkArgument(recipe instanceof ActiveWave);
        ActiveWave wave = (ActiveWave) recipe;
        int count = (int) ((float) this.count * wave.getMobAmountMultiplier());
        for (int i = 0; i < count; i++) {
            wave.addPendingMob(this);
        }
    }

    @Override
    public PostActionType<?> getType() {
        return TYPE;
    }

    public MobEntry getMob() {
        return this.mob;
    }

    public int getCount() {
        return this.count;
    }

    public String getZone() {
        return this.zone;
    }

    public static class Type extends PostActionType<SpawnMobAction> {

        public SpawnMobAction fromJson(JsonObject jsonObject) {
            MobEntry mob = MobEntry.load(jsonObject.get("mob"));
            int count = GsonHelper.getAsInt(jsonObject, "count", 1);
            String zone = GsonHelper.getAsString(jsonObject, "zone", "0");
            return new SpawnMobAction(mob, count, zone);
        }

        public void toJson(SpawnMobAction spawnMobAction, JsonObject jsonObject) {
            jsonObject.add("mob", spawnMobAction.mob.save());
            jsonObject.addProperty("count", spawnMobAction.count);
            jsonObject.addProperty("zone", spawnMobAction.zone);
        }

        public SpawnMobAction fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            throw new UnsupportedOperationException();
        }

        public void toNetwork(SpawnMobAction spawnMobAction, FriendlyByteBuf friendlyByteBuf) {
            throw new UnsupportedOperationException();
        }
    }
}
package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;

public class FishingHookPredicate implements EntitySubPredicate {

    public static final FishingHookPredicate ANY = new FishingHookPredicate(false);

    private static final String IN_OPEN_WATER_KEY = "in_open_water";

    private final boolean inOpenWater;

    private FishingHookPredicate(boolean boolean0) {
        this.inOpenWater = boolean0;
    }

    public static FishingHookPredicate inOpenWater(boolean boolean0) {
        return new FishingHookPredicate(boolean0);
    }

    public static FishingHookPredicate fromJson(JsonObject jsonObject0) {
        JsonElement $$1 = jsonObject0.get("in_open_water");
        return $$1 != null ? new FishingHookPredicate(GsonHelper.convertToBoolean($$1, "in_open_water")) : ANY;
    }

    @Override
    public JsonObject serializeCustomData() {
        if (this == ANY) {
            return new JsonObject();
        } else {
            JsonObject $$0 = new JsonObject();
            $$0.add("in_open_water", new JsonPrimitive(this.inOpenWater));
            return $$0;
        }
    }

    @Override
    public EntitySubPredicate.Type type() {
        return EntitySubPredicate.Types.FISHING_HOOK;
    }

    @Override
    public boolean matches(Entity entity0, ServerLevel serverLevel1, @Nullable Vec3 vec2) {
        if (this == ANY) {
            return true;
        } else {
            return !(entity0 instanceof FishingHook $$3) ? false : this.inOpenWater == $$3.isOpenWaterFishing();
        }
    }
}
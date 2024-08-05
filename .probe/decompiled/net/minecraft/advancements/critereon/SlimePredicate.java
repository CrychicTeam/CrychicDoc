package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.Vec3;

public class SlimePredicate implements EntitySubPredicate {

    private final MinMaxBounds.Ints size;

    private SlimePredicate(MinMaxBounds.Ints minMaxBoundsInts0) {
        this.size = minMaxBoundsInts0;
    }

    public static SlimePredicate sized(MinMaxBounds.Ints minMaxBoundsInts0) {
        return new SlimePredicate(minMaxBoundsInts0);
    }

    public static SlimePredicate fromJson(JsonObject jsonObject0) {
        MinMaxBounds.Ints $$1 = MinMaxBounds.Ints.fromJson(jsonObject0.get("size"));
        return new SlimePredicate($$1);
    }

    @Override
    public JsonObject serializeCustomData() {
        JsonObject $$0 = new JsonObject();
        $$0.add("size", this.size.m_55328_());
        return $$0;
    }

    @Override
    public boolean matches(Entity entity0, ServerLevel serverLevel1, @Nullable Vec3 vec2) {
        return entity0 instanceof Slime $$3 ? this.size.matches($$3.getSize()) : false;
    }

    @Override
    public EntitySubPredicate.Type type() {
        return EntitySubPredicate.Types.SLIME;
    }
}
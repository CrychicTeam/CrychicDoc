package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.Vec3;

public class LighthingBoltPredicate implements EntitySubPredicate {

    private static final String BLOCKS_SET_ON_FIRE_KEY = "blocks_set_on_fire";

    private static final String ENTITY_STRUCK_KEY = "entity_struck";

    private final MinMaxBounds.Ints blocksSetOnFire;

    private final EntityPredicate entityStruck;

    private LighthingBoltPredicate(MinMaxBounds.Ints minMaxBoundsInts0, EntityPredicate entityPredicate1) {
        this.blocksSetOnFire = minMaxBoundsInts0;
        this.entityStruck = entityPredicate1;
    }

    public static LighthingBoltPredicate blockSetOnFire(MinMaxBounds.Ints minMaxBoundsInts0) {
        return new LighthingBoltPredicate(minMaxBoundsInts0, EntityPredicate.ANY);
    }

    public static LighthingBoltPredicate fromJson(JsonObject jsonObject0) {
        return new LighthingBoltPredicate(MinMaxBounds.Ints.fromJson(jsonObject0.get("blocks_set_on_fire")), EntityPredicate.fromJson(jsonObject0.get("entity_struck")));
    }

    @Override
    public JsonObject serializeCustomData() {
        JsonObject $$0 = new JsonObject();
        $$0.add("blocks_set_on_fire", this.blocksSetOnFire.m_55328_());
        $$0.add("entity_struck", this.entityStruck.serializeToJson());
        return $$0;
    }

    @Override
    public EntitySubPredicate.Type type() {
        return EntitySubPredicate.Types.LIGHTNING;
    }

    @Override
    public boolean matches(Entity entity0, ServerLevel serverLevel1, @Nullable Vec3 vec2) {
        return !(entity0 instanceof LightningBolt $$3) ? false : this.blocksSetOnFire.matches($$3.getBlocksSetOnFire()) && (this.entityStruck == EntityPredicate.ANY || $$3.getHitEntities().anyMatch(p_153245_ -> this.entityStruck.matches(serverLevel1, vec2, p_153245_)));
    }
}
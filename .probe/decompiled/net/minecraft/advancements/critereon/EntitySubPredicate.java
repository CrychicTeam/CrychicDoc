package net.minecraft.advancements.critereon;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;

public interface EntitySubPredicate {

    EntitySubPredicate ANY = new EntitySubPredicate() {

        @Override
        public boolean matches(Entity p_218841_, ServerLevel p_218842_, @Nullable Vec3 p_218843_) {
            return true;
        }

        @Override
        public JsonObject serializeCustomData() {
            return new JsonObject();
        }

        @Override
        public EntitySubPredicate.Type type() {
            return EntitySubPredicate.Types.ANY;
        }
    };

    static EntitySubPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "type_specific");
            String $$2 = GsonHelper.getAsString($$1, "type", null);
            if ($$2 == null) {
                return ANY;
            } else {
                EntitySubPredicate.Type $$3 = (EntitySubPredicate.Type) EntitySubPredicate.Types.TYPES.get($$2);
                if ($$3 == null) {
                    throw new JsonSyntaxException("Unknown sub-predicate type: " + $$2);
                } else {
                    return $$3.deserialize($$1);
                }
            }
        } else {
            return ANY;
        }
    }

    boolean matches(Entity var1, ServerLevel var2, @Nullable Vec3 var3);

    JsonObject serializeCustomData();

    default JsonElement serialize() {
        if (this.type() == EntitySubPredicate.Types.ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = this.serializeCustomData();
            String $$1 = (String) EntitySubPredicate.Types.TYPES.inverse().get(this.type());
            $$0.addProperty("type", $$1);
            return $$0;
        }
    }

    EntitySubPredicate.Type type();

    static EntitySubPredicate variant(CatVariant catVariant0) {
        return EntitySubPredicate.Types.CAT.createPredicate(catVariant0);
    }

    static EntitySubPredicate variant(FrogVariant frogVariant0) {
        return EntitySubPredicate.Types.FROG.createPredicate(frogVariant0);
    }

    public interface Type {

        EntitySubPredicate deserialize(JsonObject var1);
    }

    public static final class Types {

        public static final EntitySubPredicate.Type ANY = p_218860_ -> EntitySubPredicate.ANY;

        public static final EntitySubPredicate.Type LIGHTNING = LighthingBoltPredicate::m_220332_;

        public static final EntitySubPredicate.Type FISHING_HOOK = FishingHookPredicate::m_219719_;

        public static final EntitySubPredicate.Type PLAYER = PlayerPredicate::m_222491_;

        public static final EntitySubPredicate.Type SLIME = SlimePredicate::m_223428_;

        public static final EntityVariantPredicate<CatVariant> CAT = EntityVariantPredicate.create(BuiltInRegistries.CAT_VARIANT, p_218862_ -> p_218862_ instanceof Cat $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<FrogVariant> FROG = EntityVariantPredicate.create(BuiltInRegistries.FROG_VARIANT, p_218858_ -> p_218858_ instanceof Frog $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Axolotl.Variant> AXOLOTL = EntityVariantPredicate.create(Axolotl.Variant.CODEC, p_262508_ -> p_262508_ instanceof Axolotl $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Boat.Type> BOAT = EntityVariantPredicate.create(Boat.Type.CODEC, p_262507_ -> p_262507_ instanceof Boat $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Fox.Type> FOX = EntityVariantPredicate.create(Fox.Type.CODEC, p_262510_ -> p_262510_ instanceof Fox $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<MushroomCow.MushroomType> MOOSHROOM = EntityVariantPredicate.create(MushroomCow.MushroomType.CODEC, p_262513_ -> p_262513_ instanceof MushroomCow $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Holder<PaintingVariant>> PAINTING = EntityVariantPredicate.create(BuiltInRegistries.PAINTING_VARIANT.m_206110_(), p_262509_ -> p_262509_ instanceof Painting $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Rabbit.Variant> RABBIT = EntityVariantPredicate.create(Rabbit.Variant.CODEC, p_262511_ -> p_262511_ instanceof Rabbit $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Variant> HORSE = EntityVariantPredicate.create(Variant.CODEC, p_262516_ -> p_262516_ instanceof Horse $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Llama.Variant> LLAMA = EntityVariantPredicate.create(Llama.Variant.CODEC, p_262515_ -> p_262515_ instanceof Llama $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<VillagerType> VILLAGER = EntityVariantPredicate.create(BuiltInRegistries.VILLAGER_TYPE.m_194605_(), p_262512_ -> p_262512_ instanceof VillagerDataHolder $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<Parrot.Variant> PARROT = EntityVariantPredicate.create(Parrot.Variant.CODEC, p_262506_ -> p_262506_ instanceof Parrot $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final EntityVariantPredicate<TropicalFish.Pattern> TROPICAL_FISH = EntityVariantPredicate.create(TropicalFish.Pattern.CODEC, p_262517_ -> p_262517_ instanceof TropicalFish $$1 ? Optional.of($$1.getVariant()) : Optional.empty());

        public static final BiMap<String, EntitySubPredicate.Type> TYPES = ImmutableBiMap.builder().put("any", ANY).put("lightning", LIGHTNING).put("fishing_hook", FISHING_HOOK).put("player", PLAYER).put("slime", SLIME).put("cat", CAT.type()).put("frog", FROG.type()).put("axolotl", AXOLOTL.type()).put("boat", BOAT.type()).put("fox", FOX.type()).put("mooshroom", MOOSHROOM.type()).put("painting", PAINTING.type()).put("rabbit", RABBIT.type()).put("horse", HORSE.type()).put("llama", LLAMA.type()).put("villager", VILLAGER.type()).put("parrot", PARROT.type()).put("tropical_fish", TROPICAL_FISH.type()).buildOrThrow();
    }
}
package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.Items;

public class EntityEquipmentPredicate {

    public static final EntityEquipmentPredicate ANY = new EntityEquipmentPredicate(ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);

    public static final EntityEquipmentPredicate CAPTAIN = new EntityEquipmentPredicate(ItemPredicate.Builder.item().of(Items.WHITE_BANNER).hasNbt(Raid.getLeaderBannerInstance().getTag()).build(), ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);

    private final ItemPredicate head;

    private final ItemPredicate chest;

    private final ItemPredicate legs;

    private final ItemPredicate feet;

    private final ItemPredicate mainhand;

    private final ItemPredicate offhand;

    public EntityEquipmentPredicate(ItemPredicate itemPredicate0, ItemPredicate itemPredicate1, ItemPredicate itemPredicate2, ItemPredicate itemPredicate3, ItemPredicate itemPredicate4, ItemPredicate itemPredicate5) {
        this.head = itemPredicate0;
        this.chest = itemPredicate1;
        this.legs = itemPredicate2;
        this.feet = itemPredicate3;
        this.mainhand = itemPredicate4;
        this.offhand = itemPredicate5;
    }

    public boolean matches(@Nullable Entity entity0) {
        if (this == ANY) {
            return true;
        } else if (!(entity0 instanceof LivingEntity $$1)) {
            return false;
        } else if (!this.head.matches($$1.getItemBySlot(EquipmentSlot.HEAD))) {
            return false;
        } else if (!this.chest.matches($$1.getItemBySlot(EquipmentSlot.CHEST))) {
            return false;
        } else if (!this.legs.matches($$1.getItemBySlot(EquipmentSlot.LEGS))) {
            return false;
        } else if (!this.feet.matches($$1.getItemBySlot(EquipmentSlot.FEET))) {
            return false;
        } else {
            return !this.mainhand.matches($$1.getItemBySlot(EquipmentSlot.MAINHAND)) ? false : this.offhand.matches($$1.getItemBySlot(EquipmentSlot.OFFHAND));
        }
    }

    public static EntityEquipmentPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "equipment");
            ItemPredicate $$2 = ItemPredicate.fromJson($$1.get("head"));
            ItemPredicate $$3 = ItemPredicate.fromJson($$1.get("chest"));
            ItemPredicate $$4 = ItemPredicate.fromJson($$1.get("legs"));
            ItemPredicate $$5 = ItemPredicate.fromJson($$1.get("feet"));
            ItemPredicate $$6 = ItemPredicate.fromJson($$1.get("mainhand"));
            ItemPredicate $$7 = ItemPredicate.fromJson($$1.get("offhand"));
            return new EntityEquipmentPredicate($$2, $$3, $$4, $$5, $$6, $$7);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            $$0.add("head", this.head.serializeToJson());
            $$0.add("chest", this.chest.serializeToJson());
            $$0.add("legs", this.legs.serializeToJson());
            $$0.add("feet", this.feet.serializeToJson());
            $$0.add("mainhand", this.mainhand.serializeToJson());
            $$0.add("offhand", this.offhand.serializeToJson());
            return $$0;
        }
    }

    public static class Builder {

        private ItemPredicate head = ItemPredicate.ANY;

        private ItemPredicate chest = ItemPredicate.ANY;

        private ItemPredicate legs = ItemPredicate.ANY;

        private ItemPredicate feet = ItemPredicate.ANY;

        private ItemPredicate mainhand = ItemPredicate.ANY;

        private ItemPredicate offhand = ItemPredicate.ANY;

        public static EntityEquipmentPredicate.Builder equipment() {
            return new EntityEquipmentPredicate.Builder();
        }

        public EntityEquipmentPredicate.Builder head(ItemPredicate itemPredicate0) {
            this.head = itemPredicate0;
            return this;
        }

        public EntityEquipmentPredicate.Builder chest(ItemPredicate itemPredicate0) {
            this.chest = itemPredicate0;
            return this;
        }

        public EntityEquipmentPredicate.Builder legs(ItemPredicate itemPredicate0) {
            this.legs = itemPredicate0;
            return this;
        }

        public EntityEquipmentPredicate.Builder feet(ItemPredicate itemPredicate0) {
            this.feet = itemPredicate0;
            return this;
        }

        public EntityEquipmentPredicate.Builder mainhand(ItemPredicate itemPredicate0) {
            this.mainhand = itemPredicate0;
            return this;
        }

        public EntityEquipmentPredicate.Builder offhand(ItemPredicate itemPredicate0) {
            this.offhand = itemPredicate0;
            return this;
        }

        public EntityEquipmentPredicate build() {
            return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.mainhand, this.offhand);
        }
    }
}
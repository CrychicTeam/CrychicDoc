package top.theillusivec4.curios.api;

import javax.annotation.Nonnull;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.resources.ResourceLocation;

public final class CuriosTriggers {

    @Nonnull
    public static CuriosTriggers.EquipBuilder equip() {
        return new CuriosTriggers.EquipBuilder();
    }

    @Deprecated
    @Nonnull
    public static CriterionTriggerInstance equip(ItemPredicate.Builder itemPredicate) {
        CuriosApi.apiError();
        return new CuriosTriggers.EmptyInstance();
    }

    @Deprecated
    @Nonnull
    public static CriterionTriggerInstance equipAtLocation(ItemPredicate.Builder itemPredicate, LocationPredicate.Builder locationPredicate) {
        CuriosApi.apiError();
        return new CuriosTriggers.EmptyInstance();
    }

    private static final class EmptyInstance extends AbstractCriterionTriggerInstance {

        public EmptyInstance() {
            super(new ResourceLocation("curios", "empty"), ContextAwarePredicate.ANY);
        }
    }

    public static final class EquipBuilder {

        private ItemPredicate.Builder itemPredicate;

        private LocationPredicate.Builder locationPredicate;

        private SlotPredicate.Builder slotPredicate;

        private EquipBuilder() {
        }

        public CuriosTriggers.EquipBuilder withItem(ItemPredicate.Builder builder) {
            this.itemPredicate = builder;
            return this;
        }

        public CuriosTriggers.EquipBuilder withLocation(LocationPredicate.Builder builder) {
            this.locationPredicate = builder;
            return this;
        }

        public CuriosTriggers.EquipBuilder withSlot(SlotPredicate.Builder builder) {
            this.slotPredicate = builder;
            return this;
        }

        public CriterionTriggerInstance build() {
            return new CuriosTriggers.EmptyInstance();
        }
    }
}
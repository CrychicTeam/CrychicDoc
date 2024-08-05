package net.minecraft.world.entity.ai.attributes;

import com.mojang.logging.LogUtils;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;

public class AttributeModifier {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final double amount;

    private final AttributeModifier.Operation operation;

    private final Supplier<String> nameGetter;

    private final UUID id;

    public AttributeModifier(String string0, double double1, AttributeModifier.Operation attributeModifierOperation2) {
        this(Mth.createInsecureUUID(RandomSource.createNewThreadLocalInstance()), (Supplier<String>) (() -> string0), double1, attributeModifierOperation2);
    }

    public AttributeModifier(UUID uUID0, String string1, double double2, AttributeModifier.Operation attributeModifierOperation3) {
        this(uUID0, (Supplier<String>) (() -> string1), double2, attributeModifierOperation3);
    }

    public AttributeModifier(UUID uUID0, Supplier<String> supplierString1, double double2, AttributeModifier.Operation attributeModifierOperation3) {
        this.id = uUID0;
        this.nameGetter = supplierString1;
        this.amount = double2;
        this.operation = attributeModifierOperation3;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return (String) this.nameGetter.get();
    }

    public AttributeModifier.Operation getOperation() {
        return this.operation;
    }

    public double getAmount() {
        return this.amount;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 != null && this.getClass() == object0.getClass()) {
            AttributeModifier $$1 = (AttributeModifier) object0;
            return Objects.equals(this.id, $$1.id);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public String toString() {
        return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name='" + (String) this.nameGetter.get() + "', id=" + this.id + "}";
    }

    public CompoundTag save() {
        CompoundTag $$0 = new CompoundTag();
        $$0.putString("Name", this.getName());
        $$0.putDouble("Amount", this.amount);
        $$0.putInt("Operation", this.operation.toValue());
        $$0.putUUID("UUID", this.id);
        return $$0;
    }

    @Nullable
    public static AttributeModifier load(CompoundTag compoundTag0) {
        try {
            UUID $$1 = compoundTag0.getUUID("UUID");
            AttributeModifier.Operation $$2 = AttributeModifier.Operation.fromValue(compoundTag0.getInt("Operation"));
            return new AttributeModifier($$1, compoundTag0.getString("Name"), compoundTag0.getDouble("Amount"), $$2);
        } catch (Exception var3) {
            LOGGER.warn("Unable to create attribute: {}", var3.getMessage());
            return null;
        }
    }

    public static enum Operation {

        ADDITION(0), MULTIPLY_BASE(1), MULTIPLY_TOTAL(2);

        private static final AttributeModifier.Operation[] OPERATIONS = new AttributeModifier.Operation[] { ADDITION, MULTIPLY_BASE, MULTIPLY_TOTAL };

        private final int value;

        private Operation(int p_22234_) {
            this.value = p_22234_;
        }

        public int toValue() {
            return this.value;
        }

        public static AttributeModifier.Operation fromValue(int p_22237_) {
            if (p_22237_ >= 0 && p_22237_ < OPERATIONS.length) {
                return OPERATIONS[p_22237_];
            } else {
                throw new IllegalArgumentException("No operation with value " + p_22237_);
            }
        }
    }
}
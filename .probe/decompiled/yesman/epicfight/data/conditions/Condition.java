package yesman.epicfight.data.conditions;

import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface Condition<T> {

    void read(CompoundTag var1);

    CompoundTag serializePredicate();

    boolean predicate(T var1);

    @OnlyIn(Dist.CLIENT)
    Set<Entry<String, Object>> getAcceptingParameters();

    public static class ConditionBuilder<T extends Condition<?>> {

        final Function<CompoundTag, T> constructor;

        CompoundTag tag;

        private ConditionBuilder(Function<CompoundTag, T> constructor) {
            this.constructor = constructor;
            this.tag = new CompoundTag();
        }

        public Condition.ConditionBuilder<T> setTag(CompoundTag tag) {
            this.tag = tag;
            return this;
        }

        public CompoundTag getTag() {
            return this.tag;
        }

        public T build() {
            return (T) ((Condition) this.constructor.apply(this.tag));
        }

        public static <T extends Condition<?>> Condition.ConditionBuilder<T> builder(Function<CompoundTag, T> constructor) {
            return new Condition.ConditionBuilder(constructor);
        }
    }
}
package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.IntBoundsHelper;
import snownee.lychee.core.def.TimeCheckHelper;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.mixin.IntRangeAccess;
import snownee.lychee.mixin.TimeCheckAccess;
import snownee.lychee.util.CommonProxy;

public record Time(MinMaxBounds.Ints value, @Nullable Long period) implements ContextualCondition {

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.TIME;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        return this.test(ctx.getLevel()) ? times : 0;
    }

    @Override
    public InteractionResult testInTooltips(Level level, @Nullable Player player) {
        return CommonProxy.interactionResult(this.test(level));
    }

    public boolean test(LevelAccessor level) {
        long i = level.dayTime();
        if (this.period != null) {
            i %= this.period;
        }
        return this.value.matches((int) i);
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        return Component.translatable(this.makeDescriptionId(inverted));
    }

    public static class Type extends ContextualConditionType<Time> {

        public Time fromJson(JsonObject o) {
            TimeCheckAccess access = (TimeCheckAccess) TimeCheckHelper.fromJson(o);
            return new Time(IntBoundsHelper.fromIntRange((IntRangeAccess) access.getValue()), access.getPeriod());
        }

        public void toJson(Time condition, JsonObject o) {
            if (condition.period() != null) {
                o.addProperty("period", condition.period());
            }
            o.add("value", condition.value().m_55328_());
        }

        public Time fromNetwork(FriendlyByteBuf buf) {
            MinMaxBounds.Ints range = IntBoundsHelper.fromNetwork(buf);
            Long period = buf.readLong();
            if (period <= 0L) {
                period = null;
            }
            return new Time(range, period);
        }

        public void toNetwork(Time condition, FriendlyByteBuf buf) {
            IntBoundsHelper.toNetwork(condition.value(), buf);
            buf.writeLong(condition.period() == null ? Long.MIN_VALUE : condition.period());
        }
    }
}
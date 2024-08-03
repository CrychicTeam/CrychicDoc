package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BoundsHelper;
import snownee.lychee.core.def.DoubleBoundsHelper;
import snownee.lychee.core.recipe.ILycheeRecipe;

public record FallDistance(MinMaxBounds.Doubles range) implements ContextualCondition {

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.FALL_DISTANCE;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Entity entity = ctx.getParam(LootContextParams.THIS_ENTITY);
        double d = (double) entity.fallDistance;
        if (entity instanceof FallingBlockEntity block) {
            d = Math.max((double) (block.getStartPos().m_123342_() - block.m_146904_()), d);
        }
        return this.range.matches(d) ? times : 0;
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        return Component.translatable(this.makeDescriptionId(inverted), BoundsHelper.getDescription(this.range));
    }

    public static class Type extends ContextualConditionType<FallDistance> {

        public FallDistance fromJson(JsonObject o) {
            return new FallDistance(MinMaxBounds.Doubles.fromJson(o.get("range")));
        }

        public void toJson(FallDistance condition, JsonObject o) {
            o.add("range", condition.range().m_55328_());
        }

        public FallDistance fromNetwork(FriendlyByteBuf buf) {
            return new FallDistance(DoubleBoundsHelper.fromNetwork(buf));
        }

        public void toNetwork(FallDistance condition, FriendlyByteBuf buf) {
            DoubleBoundsHelper.toNetwork(condition.range(), buf);
        }
    }
}
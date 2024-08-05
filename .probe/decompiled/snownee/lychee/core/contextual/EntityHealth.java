package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BoundsHelper;
import snownee.lychee.core.def.DoubleBoundsHelper;
import snownee.lychee.core.recipe.ILycheeRecipe;

public record EntityHealth(MinMaxBounds.Doubles range) implements ContextualCondition {

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.ENTITY_HEALTH;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Entity entity = ctx.getParam(LootContextParams.THIS_ENTITY);
        double d = 0.0;
        if (entity instanceof LivingEntity living) {
            d = (double) living.getHealth();
        }
        return this.range.matches(d) ? times : 0;
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        return Component.translatable(this.makeDescriptionId(inverted), BoundsHelper.getDescription(this.range));
    }

    public static class Type extends ContextualConditionType<EntityHealth> {

        public EntityHealth fromJson(JsonObject o) {
            return new EntityHealth(MinMaxBounds.Doubles.fromJson(o.get("range")));
        }

        public void toJson(EntityHealth condition, JsonObject o) {
            o.add("range", condition.range().m_55328_());
        }

        public EntityHealth fromNetwork(FriendlyByteBuf buf) {
            return new EntityHealth(DoubleBoundsHelper.fromNetwork(buf));
        }

        public void toNetwork(EntityHealth condition, FriendlyByteBuf buf) {
            DoubleBoundsHelper.toNetwork(condition.range(), buf);
        }
    }
}
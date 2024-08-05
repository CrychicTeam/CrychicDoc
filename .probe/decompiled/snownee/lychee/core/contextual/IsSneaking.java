package snownee.lychee.core.contextual;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;

public enum IsSneaking implements ContextualCondition {

    INSTANCE;

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.IS_SNEAKING;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Entity entity = ctx.getParam(LootContextParams.THIS_ENTITY);
        return !entity.isCrouching() && !entity.isShiftKeyDown() ? 0 : times;
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        return Component.translatable(this.makeDescriptionId(inverted));
    }

    public static class Type extends ContextualConditionType<IsSneaking> {

        public IsSneaking fromJson(JsonObject o) {
            return IsSneaking.INSTANCE;
        }

        public void toJson(IsSneaking condition, JsonObject o) {
        }

        public IsSneaking fromNetwork(FriendlyByteBuf buf) {
            return IsSneaking.INSTANCE;
        }

        public void toNetwork(IsSneaking condition, FriendlyByteBuf buf) {
        }
    }
}
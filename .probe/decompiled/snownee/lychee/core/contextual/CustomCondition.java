package snownee.lychee.core.contextual;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.function.BiFunction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;

public class CustomCondition implements ContextualCondition {

    public final JsonObject data;

    public CustomCondition.Test testFunc;

    public BiFunction<Level, Player, InteractionResult> testInTooltipsFunc = (level, player) -> InteractionResult.PASS;

    public CustomCondition(JsonObject data) {
        this.data = data;
        CommonProxy.postCustomConditionEvent(GsonHelper.getAsString(data, "id"), this);
    }

    @Override
    public ContextualConditionType<?> getType() {
        return ContextualConditionTypes.CUSTOM;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        return this.testFunc != null ? this.testFunc.test(recipe, ctx, times) : 0;
    }

    @Override
    public InteractionResult testInTooltips(Level level, @Nullable Player player) {
        return (InteractionResult) this.testInTooltipsFunc.apply(level, player);
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        return Component.translatable(this.makeDescriptionId(inverted), GsonHelper.getAsString(this.data, "id"));
    }

    @FunctionalInterface
    public interface Test {

        int test(ILycheeRecipe<?> var1, LycheeContext var2, int var3);
    }

    public static class Type extends ContextualConditionType<CustomCondition> {

        public CustomCondition fromJson(JsonObject o) {
            return new CustomCondition(o);
        }

        public void toJson(CustomCondition condition, JsonObject o) {
            condition.data.entrySet().forEach(e -> o.add((String) e.getKey(), (JsonElement) e.getValue()));
        }

        public CustomCondition fromNetwork(FriendlyByteBuf buf) {
            return new CustomCondition(JsonParser.parseString(buf.readUtf()).getAsJsonObject());
        }

        public void toNetwork(CustomCondition condition, FriendlyByteBuf buf) {
            buf.writeUtf(condition.data.toString());
        }
    }
}
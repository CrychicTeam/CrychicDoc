package snownee.lychee.core.contextual;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;

public record IsWeather(String id, Predicate<Level> predicate) implements ContextualCondition {

    public static final Map<String, IsWeather> REGISTRY = Maps.newConcurrentMap();

    public static IsWeather CLEAR = create("clear", level -> !level.isRaining() && !level.isThundering());

    public static IsWeather RAIN = create("rain", level -> level.isRaining());

    public static IsWeather THUNDER = create("thunder", level -> level.isThundering());

    public static IsWeather create(String id, Predicate<Level> predicate) {
        IsWeather isWeather = new IsWeather(id, predicate);
        REGISTRY.put(id, isWeather);
        return isWeather;
    }

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.WEATHER;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        return this.predicate.test(ctx.getLevel()) ? times : 0;
    }

    @Override
    public InteractionResult testInTooltips(Level level, @Nullable Player player) {
        return this.predicate.test(level) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        String key = this.makeDescriptionId(inverted);
        MutableComponent weather = Component.translatable("weather.lychee." + this.id);
        return Component.translatable(key, weather.withStyle(ChatFormatting.WHITE));
    }

    public static class Type extends ContextualConditionType<IsWeather> {

        public IsWeather fromJson(JsonObject o) {
            return (IsWeather) IsWeather.REGISTRY.get(o.get("weather").getAsString());
        }

        public void toJson(IsWeather condition, JsonObject o) {
            o.addProperty("weather", condition.id());
        }

        public IsWeather fromNetwork(FriendlyByteBuf buf) {
            return (IsWeather) IsWeather.REGISTRY.get(buf.readUtf());
        }

        public void toNetwork(IsWeather condition, FriendlyByteBuf buf) {
            buf.writeUtf(condition.id());
        }
    }
}
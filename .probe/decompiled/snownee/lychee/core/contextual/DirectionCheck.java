package snownee.lychee.core.contextual;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;

public class DirectionCheck implements ContextualCondition {

    public static final Map<String, DirectionCheck> LOOKUP = Maps.newHashMap();

    private final String name;

    private final Predicate<LycheeContext> predicate;

    public static void create(String name, Predicate<LycheeContext> predicate) {
        LOOKUP.put(name, new DirectionCheck(name, predicate));
    }

    private DirectionCheck(String name, Predicate<LycheeContext> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.DIRECTION;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        return this.predicate.test(ctx) ? times : 0;
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        Component value = Component.translatable("direction.lychee." + this.name).withStyle(ChatFormatting.WHITE);
        return Component.translatable(this.makeDescriptionId(inverted), value);
    }

    static {
        for (Direction direction : Direction.values()) {
            create(direction.getName().toLowerCase(Locale.ENGLISH), ctx -> ctx.getParam(LycheeLootContextParams.DIRECTION) == direction);
        }
        create("sides", ctx -> ctx.getParam(LycheeLootContextParams.DIRECTION).getStepY() == 0);
        create("forward", ctx -> {
            Direction directionx = ctx.getParam(LycheeLootContextParams.DIRECTION);
            BlockState state = ctx.getParam(LootContextParams.BLOCK_STATE);
            Direction facing = (Direction) state.m_61145_(BlockStateProperties.FACING).or(() -> state.m_61145_(BlockStateProperties.HORIZONTAL_FACING)).or(() -> state.m_61145_(BlockStateProperties.VERTICAL_DIRECTION)).orElseThrow();
            return directionx == facing;
        });
        create("axis", ctx -> {
            Direction directionx = ctx.getParam(LycheeLootContextParams.DIRECTION);
            BlockState state = ctx.getParam(LootContextParams.BLOCK_STATE);
            Direction.Axis axis = (Direction.Axis) state.m_61145_(BlockStateProperties.AXIS).or(() -> state.m_61145_(BlockStateProperties.HORIZONTAL_AXIS)).orElseThrow();
            return axis.test(directionx);
        });
    }

    public static class Type extends ContextualConditionType<DirectionCheck> {

        public DirectionCheck fromJson(JsonObject o) {
            return (DirectionCheck) DirectionCheck.LOOKUP.get(o.get("direction").getAsString());
        }

        public void toJson(DirectionCheck condition, JsonObject o) {
            o.addProperty("direction", condition.name);
        }

        public DirectionCheck fromNetwork(FriendlyByteBuf buf) {
            return (DirectionCheck) DirectionCheck.LOOKUP.get(buf.readUtf());
        }

        public void toNetwork(DirectionCheck condition, FriendlyByteBuf buf) {
            buf.writeUtf(condition.name);
        }
    }
}
package snownee.lychee.core.contextual;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntImmutableList;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.ContextualConditionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;

public record IsDifficulty(IntImmutableList difficulties) implements ContextualCondition {

    @Override
    public ContextualConditionType<? extends ContextualCondition> getType() {
        return ContextualConditionTypes.DIFFICULTY;
    }

    @Override
    public int test(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        return this.difficulties.contains(ctx.getLevel().m_46791_().getId()) ? times : 0;
    }

    @Override
    public InteractionResult testInTooltips(Level level, @Nullable Player player) {
        return this.difficulties.contains(level.m_46791_().getId()) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
    }

    @Override
    public MutableComponent getDescription(boolean inverted) {
        String key = this.makeDescriptionId(inverted);
        List<String> names = this.difficulties.intParallelStream().mapToObj(Difficulty::m_19029_).map(Difficulty::m_19033_).map(Component::getString).toList();
        int size = names.size();
        if (size == 1) {
            return Component.translatable(key, CommonProxy.white((CharSequence) names.get(0)));
        } else {
            StringBuilder sb = new StringBuilder();
            key = key + ".more";
            for (int i = 0; i < size - 1; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append((String) names.get(i));
            }
            return Component.translatable(key, CommonProxy.white(sb), CommonProxy.white((CharSequence) names.get(size - 1)));
        }
    }

    public static class Type extends ContextualConditionType<IsDifficulty> {

        public IsDifficulty fromJson(JsonObject o) {
            JsonElement e = o.get("difficulty");
            if (e.isJsonPrimitive()) {
                return new IsDifficulty(IntImmutableList.of(new int[] { parseDifficulty(e.getAsJsonPrimitive()) }));
            } else {
                IntArrayList list = IntArrayList.of();
                e.getAsJsonArray().forEach($ -> list.add(parseDifficulty($.getAsJsonPrimitive())));
                return new IsDifficulty(new IntImmutableList(list));
            }
        }

        public void toJson(IsDifficulty condition, JsonObject o) {
            if (condition.difficulties().size() == 1) {
                o.addProperty("difficulty", condition.difficulties().getInt(0));
            } else {
                JsonArray array = new JsonArray();
                condition.difficulties().forEach(i -> array.add(i));
                o.add("difficulty", array);
            }
        }

        private static int parseDifficulty(JsonPrimitive e) {
            return e.isNumber() ? e.getAsInt() : Difficulty.byName(e.getAsString()).getId();
        }

        public IsDifficulty fromNetwork(FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            return new IsDifficulty(IntImmutableList.toList(IntStream.range(0, size).map($ -> buf.readVarInt())));
        }

        public void toNetwork(IsDifficulty condition, FriendlyByteBuf buf) {
            buf.writeVarInt(condition.difficulties().size());
            condition.difficulties().forEach(buf::m_130130_);
        }
    }
}
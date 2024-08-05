package snownee.lychee.core.post;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import snownee.lychee.LycheeRegistries;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.Job;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BoundsHelper;
import snownee.lychee.core.def.IntBoundsHelper;
import snownee.lychee.core.post.input.NBTPatch;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;
import snownee.lychee.util.json.JsonPointer;

public class RandomSelect extends PostAction implements CompoundAction {

    public final PostAction[] entries;

    public final int[] weights;

    public final MinMaxBounds.Ints rolls;

    public final boolean canRepeat;

    public final boolean hidden;

    public final boolean preventSync;

    public final int totalWeight;

    public final int emptyWeight;

    public RandomSelect(PostAction[] entries, int[] weights, int totalWeight, int emptyWeight, MinMaxBounds.Ints rolls) {
        Preconditions.checkArgument(entries.length == weights.length);
        this.entries = entries;
        this.weights = weights;
        this.totalWeight = totalWeight;
        this.emptyWeight = emptyWeight;
        this.rolls = rolls;
        this.canRepeat = Arrays.stream(entries).allMatch(PostAction::canRepeat);
        this.hidden = Arrays.stream(entries).allMatch(PostAction::isHidden);
        this.preventSync = Arrays.stream(entries).allMatch(PostAction::preventSync);
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.RANDOM;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        times *= IntBoundsHelper.random(this.rolls, ctx.getRandom());
        if (times != 0) {
            List<PostAction> validActions = Lists.newArrayList();
            int[] validWeights = new int[this.entries.length];
            int totalWeights = 0;
            for (int i = 0; i < this.entries.length; i++) {
                PostAction entry = this.entries[i];
                if (entry.checkConditions(recipe, ctx, 1) == 1) {
                    validWeights[validActions.size()] = this.weights[i];
                    validActions.add(entry);
                    totalWeights += this.weights[i];
                }
            }
            if (!validActions.isEmpty()) {
                totalWeights += this.emptyWeight;
                int[] childTimes = new int[validActions.size()];
                for (int ix = 0; ix < times; ix++) {
                    int index = this.getRandomEntry(ctx.getRandom(), validWeights, totalWeights);
                    if (index >= 0) {
                        childTimes[index]++;
                    }
                }
                for (int ixx = 0; ixx < validActions.size(); ixx++) {
                    if (childTimes[ixx] > 0) {
                        ctx.runtime.jobs.push(new Job((PostAction) validActions.get(ixx), childTimes[ixx]));
                    }
                }
            }
        }
    }

    private int getRandomEntry(RandomSource random, int[] weights, int totalWeights) {
        int j = random.nextInt(totalWeights);
        for (int i = 0; i < weights.length; i++) {
            j -= weights[i];
            if (j < 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
    }

    @Override
    public List<ItemStack> getItemOutputs() {
        return Stream.of(this.entries).map(PostAction::getItemOutputs).flatMap(Collection::stream).toList();
    }

    @Override
    public List<BlockPredicate> getBlockOutputs() {
        return Stream.of(this.entries).map(PostAction::getBlockOutputs).flatMap(Collection::stream).toList();
    }

    @Override
    public Component getDisplayName() {
        return (Component) (this.entries.length == 1 && this.emptyWeight == 0 ? Component.literal("%s × %s".formatted(this.entries[0].getDisplayName().getString(), BoundsHelper.getDescription(this.rolls).getString())) : CommonProxy.getCycledItem(List.of(this.entries), this.entries[0], 1000).getDisplayName());
    }

    @Override
    public boolean canRepeat() {
        return this.canRepeat;
    }

    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public boolean preventSync() {
        return this.preventSync;
    }

    @Override
    public void validate(ILycheeRecipe<?> recipe, ILycheeRecipe.NBTPatchContext patchContext) {
        for (PostAction action : this.entries) {
            Preconditions.checkArgument(action.getClass() != NBTPatch.class, "NBTPatch cannot be used in RandomSelect");
            action.validate(recipe, patchContext);
        }
    }

    @Override
    public void getUsedPointers(ILycheeRecipe<?> recipe, Consumer<JsonPointer> consumer) {
        for (PostAction action : this.entries) {
            action.getUsedPointers(recipe, consumer);
        }
    }

    @Override
    public JsonElement provideJsonInfo(ILycheeRecipe<?> recipe, JsonPointer pointer, JsonObject recipeObject) {
        int i = 0;
        JsonArray array = new JsonArray();
        for (PostAction action : this.entries) {
            array.add(action.provideJsonInfo(recipe, pointer.append("entries/" + i), recipeObject));
            i++;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("entries", array);
        return jsonObject;
    }

    @Override
    public Stream<PostAction> getChildActions() {
        return Arrays.stream(this.entries);
    }

    public static class Type extends PostActionType<RandomSelect> {

        public RandomSelect fromJson(JsonObject o) {
            JsonArray array = o.getAsJsonArray("entries");
            int size = array.size();
            Preconditions.checkArgument(size > 0, "entries can not be empty");
            PostAction[] entries = new PostAction[size];
            int[] weights = new int[size];
            for (int i = 0; i < size; i++) {
                JsonObject e = array.get(i).getAsJsonObject();
                weights[i] = GsonHelper.getAsInt(e, "weight", 1);
                Preconditions.checkArgument(weights[i] > 0, "weight should be greater than 0");
                entries[i] = PostAction.parse(e);
            }
            MinMaxBounds.Ints rolls;
            if (o.has("rolls")) {
                rolls = MinMaxBounds.Ints.fromJson(o.get("rolls"));
                Objects.requireNonNull((Integer) rolls.m_55305_(), "min");
                Objects.requireNonNull((Integer) rolls.m_55326_(), "max");
            } else {
                rolls = IntBoundsHelper.ONE;
            }
            int emptyWeight = GsonHelper.getAsInt(o, "empty_weight", 0);
            Preconditions.checkArgument(emptyWeight >= 0, "empty_weight should be greater or equal to 0");
            return new RandomSelect(entries, weights, IntStream.of(weights).sum() + emptyWeight, emptyWeight, rolls);
        }

        public void toJson(RandomSelect action, JsonObject o) {
            JsonArray entries = new JsonArray(action.entries.length);
            int i = 0;
            for (PostAction entry : action.entries) {
                JsonObject entryJson = entry.toJson();
                if (action.weights[i] != 1) {
                    entryJson.addProperty("weight", action.weights[i]);
                }
                entries.add(entryJson);
                i++;
            }
            o.add("entries", entries);
            if (action.rolls != IntBoundsHelper.ONE) {
                o.add("rolls", action.rolls.m_55328_());
            }
            if (action.emptyWeight != 0) {
                o.addProperty("empty_weight", action.emptyWeight);
            }
        }

        public RandomSelect fromNetwork(FriendlyByteBuf buf) {
            int totalWeight = buf.readVarInt();
            int emptyWeight = buf.readVarInt();
            int size = buf.readVarInt();
            PostAction[] entries = new PostAction[size];
            int[] weights = new int[size];
            for (int i = 0; i < size; i++) {
                weights[i] = buf.readVarInt();
                entries[i] = PostAction.read(buf);
            }
            return new RandomSelect(entries, weights, totalWeight, emptyWeight, IntBoundsHelper.fromNetwork(buf));
        }

        public void toNetwork(RandomSelect action, FriendlyByteBuf buf) {
            buf.writeVarInt(action.totalWeight);
            buf.writeVarInt(action.emptyWeight);
            buf.writeVarInt((int) Stream.of(action.entries).filter(Predicate.not(PostAction::preventSync)).count());
            for (int i = 0; i < action.entries.length; i++) {
                PostAction entry = action.entries[i];
                if (!entry.preventSync()) {
                    buf.writeVarInt(action.weights[i]);
                    PostActionType type = entry.getType();
                    CommonProxy.writeRegistryId(LycheeRegistries.POST_ACTION, type, buf);
                    type.toNetwork(entry, buf);
                    entry.conditionsToNetwork(buf);
                }
            }
            IntBoundsHelper.toNetwork(action.rolls, buf);
        }
    }
}
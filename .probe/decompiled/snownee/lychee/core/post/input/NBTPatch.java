package snownee.lychee.core.post.input;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import snownee.lychee.Lychee;
import snownee.lychee.LycheeConfig;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.ActionRuntime;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.post.PostActionType;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.json.JsonPatch;
import snownee.lychee.util.json.JsonPointer;

public class NBTPatch extends PostAction {

    private final JsonPatch patch;

    public NBTPatch(JsonPatch patch) {
        this.patch = patch;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.NBT_PATCH;
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
    }

    @Override
    public void preApply(ILycheeRecipe<?> recipe, LycheeContext ctx, ILycheeRecipe.NBTPatchContext patchContext) {
        Preconditions.checkNotNull(ctx.json);
        JsonPointer from = this.patch.from;
        if (from != null) {
            from = patchContext.convertPath(from, (first, second) -> "/" + recipe.getItemIndexes(new JsonPointer(first)).getInt(0) + second);
        }
        JsonPatch patchClone = new JsonPatch(this.patch.op, this.patch.path, from, this.patch.value);
        try {
            if (recipe.isActionPath(this.patch.path)) {
                patchClone.apply(ctx.json);
            } else {
                IntListIterator e = recipe.getItemIndexes(patchContext.convertPath(this.patch.path, (first, second) -> first)).iterator();
                while (e.hasNext()) {
                    Integer index = (Integer) e.next();
                    patchClone.path = patchContext.convertPath(this.patch.path, (first, second) -> "/" + index + second);
                    patchClone.apply(ctx.json);
                    if (LycheeConfig.debug) {
                        Lychee.LOGGER.info(ctx.json.toString());
                    }
                }
            }
        } catch (Exception var8) {
            if (this.patch.op != JsonPatch.Type.test) {
                Lychee.LOGGER.error("Ctx json: " + ctx.json);
                Lychee.LOGGER.error("Action json: " + this.toJson());
                throw var8;
            }
            ctx.runtime.state = ActionRuntime.State.STOPPED;
        }
    }

    @Override
    public void getUsedPointers(ILycheeRecipe<?> recipe, Consumer<JsonPointer> consumer) {
        consumer.accept(this.patch.path);
        if (this.patch.from != null) {
            consumer.accept(this.patch.from);
        }
    }

    @Override
    public void validate(ILycheeRecipe<?> recipe, ILycheeRecipe.NBTPatchContext patchContext) {
        Preconditions.checkArgument(patchContext.countTargets(recipe, this.patch.path) > 0, "No target found for %s", this.patch.path);
        if (this.patch.from != null) {
            int size = patchContext.countTargets(recipe, this.patch.from);
            Preconditions.checkArgument(size > 0, "No source found for %s", this.patch.from);
            Preconditions.checkArgument(size == 1, "Ambiguous source for %s", this.patch.from);
        }
    }

    @Override
    public boolean preventSync() {
        return true;
    }

    public static class Type extends PostActionType<NBTPatch> {

        public NBTPatch fromJson(JsonObject o) {
            JsonPatch patch = JsonPatch.parse(o);
            Preconditions.checkNotNull(patch);
            return new NBTPatch(patch);
        }

        public void toJson(NBTPatch action, JsonObject o) {
            o.add("patch", action.patch.toJson());
        }

        public NBTPatch fromNetwork(FriendlyByteBuf buf) {
            throw new UnsupportedOperationException();
        }

        public void toNetwork(NBTPatch action, FriendlyByteBuf buf) {
            throw new UnsupportedOperationException();
        }
    }
}
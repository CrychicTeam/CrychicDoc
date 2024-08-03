package snownee.lychee.core.post;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;

public class CustomAction extends PostAction {

    public final JsonObject data;

    public boolean canRepeat;

    public CustomAction.Apply applyFunc;

    public CustomAction(JsonObject data) {
        this.data = data;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.CUSTOM;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        this.apply(recipe, ctx, times);
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        if (this.applyFunc != null) {
            this.applyFunc.apply(recipe, ctx, times);
        }
    }

    @Override
    public boolean preventSync() {
        return true;
    }

    @Override
    public boolean canRepeat() {
        return this.canRepeat;
    }

    @Override
    public void validate(ILycheeRecipe<?> recipe, ILycheeRecipe.NBTPatchContext patchContext) {
        CommonProxy.postCustomActionEvent(GsonHelper.getAsString(this.data, "id"), this, recipe, patchContext);
    }

    @FunctionalInterface
    public interface Apply {

        void apply(ILycheeRecipe<?> var1, LycheeContext var2, int var3);
    }

    public static class Type extends PostActionType<CustomAction> {

        public CustomAction fromJson(JsonObject o) {
            return new CustomAction(o);
        }

        public void toJson(CustomAction action, JsonObject o) {
            action.data.entrySet().forEach(e -> o.add((String) e.getKey(), (JsonElement) e.getValue()));
        }

        public CustomAction fromNetwork(FriendlyByteBuf buf) {
            throw new UnsupportedOperationException();
        }

        public void toNetwork(CustomAction action, FriendlyByteBuf buf) {
            throw new UnsupportedOperationException();
        }
    }
}
package snownee.lychee.core.post;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.ActionRuntime;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;

public class Break extends PostAction {

    public static final Break CLIENT_DUMMY = new Break();

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.BREAK;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        ctx.runtime.state = ActionRuntime.State.STOPPED;
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    public static class Type extends PostActionType<Break> {

        public Break fromJson(JsonObject o) {
            return new Break();
        }

        public void toJson(Break action, JsonObject o) {
        }

        public Break fromNetwork(FriendlyByteBuf buf) {
            return Break.CLIENT_DUMMY;
        }

        public void toNetwork(Break action, FriendlyByteBuf buf) {
        }
    }
}
package snownee.lychee.core.post;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.ActionRuntime;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;

public class Delay extends PostAction {

    public final float seconds;

    public Delay(float seconds) {
        this.seconds = seconds;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.DELAY;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        this.apply(recipe, ctx, times);
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        if (ctx.runtime.marker == null) {
            makeMarker(recipe, ctx);
        }
        ctx.runtime.marker.lychee$addDelay((int) (this.seconds * 20.0F));
        ctx.runtime.state = ActionRuntime.State.PAUSED;
    }

    public static void makeMarker(ILycheeRecipe<?> recipe, LycheeContext ctx) {
        Marker marker = EntityType.MARKER.create(ctx.getLevel());
        Vec3 pos = ctx.getParamOrNull(LootContextParams.ORIGIN);
        if (pos != null) {
            marker.m_20219_(pos);
        }
        marker.m_6593_(Component.literal("lychee"));
        ctx.getLevel().m_7967_(marker);
        Delay.LycheeMarker lycheeMarker = (Delay.LycheeMarker) marker;
        lycheeMarker.lychee$setContext(recipe, ctx);
        ctx.runtime.marker = lycheeMarker;
    }

    @Override
    public boolean preventSync() {
        return true;
    }

    public interface LycheeMarker {

        void lychee$setContext(ILycheeRecipe<?> var1, LycheeContext var2);

        void lychee$addDelay(int var1);

        LycheeContext lychee$getContext();

        default Marker getEntity() {
            return (Marker) this;
        }
    }

    public static class Type extends PostActionType<Delay> {

        public Delay fromJson(JsonObject o) {
            return new Delay(o.get("s").getAsFloat());
        }

        public void toJson(Delay action, JsonObject o) {
            o.addProperty("s", action.seconds);
        }

        public Delay fromNetwork(FriendlyByteBuf buf) {
            throw new UnsupportedOperationException();
        }

        public void toNetwork(Delay action, FriendlyByteBuf buf) {
            throw new UnsupportedOperationException();
        }
    }
}
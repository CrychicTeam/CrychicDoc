package snownee.lychee.core.post;

import com.google.gson.JsonObject;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;

public class Explode extends PostAction {

    public final Explosion.BlockInteraction blockInteraction;

    public final BlockPos offset;

    public final boolean fire;

    public final float radius;

    public final float step;

    public Explode(Explosion.BlockInteraction blockInteraction, BlockPos offset, boolean fire, float radius, float step) {
        this.blockInteraction = blockInteraction;
        this.offset = offset;
        this.fire = fire;
        this.radius = radius;
        this.step = step;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.EXPLODE;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        this.apply(recipe, ctx, times);
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Vec3 pos = ctx.getParam(LootContextParams.ORIGIN);
        pos = pos.add((double) this.offset.m_123341_(), (double) this.offset.m_123342_(), (double) this.offset.m_123343_());
        float r = Math.min(this.radius + this.step * (Mth.sqrt((float) times) - 1.0F), this.radius * 4.0F);
        CommonProxy.explode(this, ctx.getServerLevel(), pos, ctx.getParamOrNull(LootContextParams.THIS_ENTITY), null, null, r);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(CommonProxy.makeDescriptionId("postAction", this.getType().getRegistryName()) + "." + this.blockInteraction.name().toLowerCase(Locale.ENGLISH));
    }

    public static class Type extends PostActionType<Explode> {

        public Explode fromJson(JsonObject o) {
            BlockPos offset = CommonProxy.parseOffset(o);
            boolean fire = GsonHelper.getAsBoolean(o, "fire", false);
            String s = GsonHelper.getAsString(o, "block_interaction", "destroy");
            Explosion.BlockInteraction blockInteraction = switch(s) {
                case "none", "keep" ->
                    Explosion.BlockInteraction.KEEP;
                case "break", "destroy_with_decay" ->
                    Explosion.BlockInteraction.DESTROY_WITH_DECAY;
                case "destroy" ->
                    Explosion.BlockInteraction.DESTROY;
                default ->
                    throw new IllegalArgumentException("Unexpected value: " + s);
            };
            float radius = GsonHelper.getAsFloat(o, "radius", 4.0F);
            float radiusStep = GsonHelper.getAsFloat(o, "radius_step", 0.5F);
            return new Explode(blockInteraction, offset, fire, radius, radiusStep);
        }

        public void toJson(Explode action, JsonObject o) {
            BlockPos offset = action.offset;
            if (offset.m_123341_() != 0) {
                o.addProperty("offsetX", offset.m_123341_());
            }
            if (offset.m_123342_() != 0) {
                o.addProperty("offsetY", offset.m_123342_());
            }
            if (offset.m_123343_() != 0) {
                o.addProperty("offsetZ", offset.m_123341_());
            }
            if (action.fire) {
                o.addProperty("fire", true);
            }
            if (action.blockInteraction != Explosion.BlockInteraction.DESTROY) {
                o.addProperty("block_interaction", action.blockInteraction.name().toLowerCase(Locale.ENGLISH));
            }
            if (action.radius != 4.0F) {
                o.addProperty("radius", action.radius);
            }
            if (action.step != 0.5F) {
                o.addProperty("radius_step", action.step);
            }
        }

        public Explode fromNetwork(FriendlyByteBuf buf) {
            Explosion.BlockInteraction blockInteraction = buf.readEnum(Explosion.BlockInteraction.class);
            BlockPos offset = buf.readBlockPos();
            boolean fire = buf.readBoolean();
            float radius = buf.readFloat();
            float step = buf.readFloat();
            return new Explode(blockInteraction, offset, fire, radius, step);
        }

        public void toNetwork(Explode action, FriendlyByteBuf buf) {
            buf.writeEnum(action.blockInteraction);
            buf.writeBlockPos(action.offset);
            buf.writeBoolean(action.fire);
            buf.writeFloat(action.radius);
            buf.writeFloat(action.step);
        }
    }
}
package snownee.lychee.core.post;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.Lychee;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.def.BoundsHelper;
import snownee.lychee.core.def.DoubleBoundsHelper;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;

public class Hurt extends PostAction {

    public final MinMaxBounds.Doubles damage;

    public final ResourceLocation source;

    public Hurt(MinMaxBounds.Doubles damage, ResourceLocation source) {
        this.damage = damage;
        this.source = source;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.HURT;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        this.apply(recipe, ctx, times);
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Entity entity = ctx.getParam(LootContextParams.THIS_ENTITY);
        entity.invulnerableTime = 0;
        try {
            entity.hurt(entity.damageSources().source(ResourceKey.create(Registries.DAMAGE_TYPE, this.source)), DoubleBoundsHelper.random(this.damage, ctx.getRandom()) * (float) times);
        } catch (Exception var6) {
            Lychee.LOGGER.error("Failed to hurt entity", var6);
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(CommonProxy.makeDescriptionId("postAction", this.getType().getRegistryName()), BoundsHelper.getDescription(this.damage));
    }

    public static class Type extends PostActionType<Hurt> {

        public Hurt fromJson(JsonObject o) {
            ResourceLocation source = new ResourceLocation(GsonHelper.getAsString(o, "source", "generic"));
            return new Hurt(MinMaxBounds.Doubles.fromJson(o.get("damage")), source);
        }

        public void toJson(Hurt action, JsonObject o) {
            o.add("damage", action.damage.m_55328_());
            o.addProperty("source", action.source.toString());
        }

        public Hurt fromNetwork(FriendlyByteBuf buf) {
            return new Hurt(DoubleBoundsHelper.fromNetwork(buf), buf.readResourceLocation());
        }

        public void toNetwork(Hurt action, FriendlyByteBuf buf) {
            DoubleBoundsHelper.toNetwork(action.damage, buf);
            buf.writeResourceLocation(action.source);
        }
    }
}
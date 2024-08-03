package snownee.lychee.core.post;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;

public class AddItemCooldown extends PostAction {

    public final float seconds;

    public AddItemCooldown(float seconds) {
        this.seconds = seconds;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.ADD_ITEM_COOLDOWN;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        this.apply(recipe, ctx, times);
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Player player = (Player) ctx.getParam(LootContextParams.THIS_ENTITY);
        ItemStack stack = ctx.getItem(0);
        player.getCooldowns().addCooldown(stack.getItem(), (int) (this.seconds * 20.0F * (float) times));
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    public static class Type extends PostActionType<AddItemCooldown> {

        public AddItemCooldown fromJson(JsonObject o) {
            return new AddItemCooldown(o.get("s").getAsFloat());
        }

        public void toJson(AddItemCooldown action, JsonObject o) {
            o.addProperty("s", action.seconds);
        }

        public AddItemCooldown fromNetwork(FriendlyByteBuf buf) {
            return new AddItemCooldown(buf.readFloat());
        }

        public void toNetwork(AddItemCooldown action, FriendlyByteBuf buf) {
            buf.writeFloat(action.seconds);
        }
    }
}
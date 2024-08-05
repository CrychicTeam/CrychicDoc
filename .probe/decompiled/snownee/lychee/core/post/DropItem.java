package snownee.lychee.core.post;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import snownee.lychee.LycheeLootContextParams;
import snownee.lychee.LycheeTags;
import snownee.lychee.PostActionTypes;
import snownee.lychee.block_crushing.BlockCrushingRecipe;
import snownee.lychee.block_exploding.BlockExplodingContext;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.mixin.ItemEntityAccess;
import snownee.lychee.util.CommonProxy;
import snownee.lychee.util.json.JsonPointer;

public class DropItem extends PostAction {

    public final ItemStack stack;

    public DropItem(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.DROP_ITEM;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        this.apply(recipe, ctx, times);
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        Vec3 pos = ctx.getParam(LootContextParams.ORIGIN);
        if (recipe instanceof BlockCrushingRecipe) {
            BlockState landingBlock = ctx.getParam(LootContextParams.BLOCK_STATE);
            if (landingBlock.m_204336_(LycheeTags.EXTEND_BOX)) {
                pos = Vec3.atCenterOf(ctx.getParam(LycheeLootContextParams.BLOCK_POS));
            }
        }
        ItemStack stack;
        if (this.path == null) {
            stack = this.stack.copy();
        } else {
            stack = ItemStack.of(CommonProxy.jsonToTag(new JsonPointer(this.path).find(ctx.json)));
        }
        stack.setCount(stack.getCount() * times);
        if (ctx.getClass() == BlockExplodingContext.class) {
            ctx.itemHolders.tempList.add(stack);
        } else {
            CommonProxy.dropItemStack(ctx.getLevel(), pos.x, pos.y, pos.z, stack, $ -> ((ItemEntityAccess) $).setHealth(80));
        }
    }

    @Override
    public Component getDisplayName() {
        return this.stack.getHoverName();
    }

    @Override
    public List<ItemStack> getItemOutputs() {
        return List.of(this.stack);
    }

    @Override
    public JsonElement provideJsonInfo(ILycheeRecipe<?> recipe, JsonPointer pointer, JsonObject recipeObject) {
        this.path = pointer.toString();
        return CommonProxy.tagToJson(this.stack.save(new CompoundTag()));
    }

    public static class Type extends PostActionType<DropItem> {

        public DropItem fromJson(JsonObject o) {
            return new DropItem(ShapedRecipe.itemStackFromJson(o));
        }

        public void toJson(DropItem action, JsonObject o) {
            CommonProxy.itemstackToJson(action.stack, o);
        }

        public DropItem fromNetwork(FriendlyByteBuf buf) {
            return new DropItem(buf.readItem());
        }

        public void toNetwork(DropItem action, FriendlyByteBuf buf) {
            buf.writeItem(action.stack);
        }
    }
}
package snownee.lychee.core.post.input;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import snownee.lychee.PostActionTypes;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.Reference;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.core.post.PostActionType;
import snownee.lychee.core.recipe.ILycheeRecipe;
import snownee.lychee.util.CommonProxy;
import snownee.lychee.util.json.JsonPointer;

public class SetItem extends PostAction {

    public final ItemStack stack;

    public final Reference target;

    public SetItem(ItemStack stack, Reference target) {
        this.stack = stack;
        this.target = target;
    }

    @Override
    public PostActionType<?> getType() {
        return PostActionTypes.SET_ITEM;
    }

    @Override
    public void doApply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        this.apply(recipe, ctx, times);
    }

    @Override
    protected void apply(ILycheeRecipe<?> recipe, LycheeContext ctx, int times) {
        IntList indexes = recipe.getItemIndexes(this.target);
        IntListIterator var5 = indexes.iterator();
        while (var5.hasNext()) {
            Integer index = (Integer) var5.next();
            CompoundTag tag = ctx.getItem(index).getTag();
            ItemStack stack;
            if (this.path == null) {
                stack = this.stack.copy();
            } else {
                stack = ItemStack.of(CommonProxy.jsonToTag(new JsonPointer(this.path).find(ctx.json)));
            }
            ctx.setItem(index, stack);
            if (tag != null && !stack.isEmpty()) {
                ctx.getItem(index).getOrCreateTag().merge(tag);
            }
            ctx.itemHolders.ignoreConsumptionFlags.set(index);
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
    public boolean canRepeat() {
        return false;
    }

    @Override
    public void validate(ILycheeRecipe<?> recipe, ILycheeRecipe.NBTPatchContext patchContext) {
        Preconditions.checkArgument(recipe.getItemIndexes(this.target).size() > 0, "No target found for %s", this.target);
    }

    @Override
    public JsonElement provideJsonInfo(ILycheeRecipe<?> recipe, JsonPointer pointer, JsonObject recipeObject) {
        this.path = pointer.toString();
        return CommonProxy.tagToJson(this.stack.save(new CompoundTag()));
    }

    public static class Type extends PostActionType<SetItem> {

        public SetItem fromJson(JsonObject o) {
            ItemStack stack;
            if ("minecraft:air".equals(Objects.toString(ResourceLocation.tryParse(o.get("item").getAsString())))) {
                stack = ItemStack.EMPTY;
            } else {
                stack = ShapedRecipe.itemStackFromJson(o);
            }
            return new SetItem(stack, Reference.fromJson(o, "target"));
        }

        public void toJson(SetItem action, JsonObject o) {
            CommonProxy.itemstackToJson(action.stack, o);
            Reference.toJson(action.target, o, "target");
        }

        public SetItem fromNetwork(FriendlyByteBuf buf) {
            return new SetItem(buf.readItem(), Reference.fromNetwork(buf));
        }

        public void toNetwork(SetItem action, FriendlyByteBuf buf) {
            buf.writeItem(action.stack);
            Reference.toNetwork(action.target, buf);
        }
    }
}
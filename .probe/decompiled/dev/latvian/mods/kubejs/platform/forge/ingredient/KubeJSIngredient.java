package dev.latvian.mods.kubejs.platform.forge.ingredient;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.core.IngredientKJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ItemStackSet;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;

public abstract class KubeJSIngredient extends AbstractIngredient implements IngredientKJS {

    private static final Ingredient.Value[] EMPTY_VALUES = new Ingredient.Value[0];

    public KubeJSIngredient() {
        super(Stream.empty());
        this.f_43902_ = EMPTY_VALUES;
    }

    @Override
    public ItemStack[] getItems() {
        if (this.f_43903_ == null) {
            this.dissolve();
        }
        return this.f_43903_;
    }

    protected void dissolve() {
        if (this.f_43903_ == null) {
            ItemStackSet stacks = new ItemStackSet();
            for (ItemStack stack : ItemStackJS.getList()) {
                if (this.test(stack)) {
                    stacks.add(stack);
                }
            }
            this.f_43903_ = stacks.toArray();
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public boolean kjs$canBeUsedForMatching() {
        return true;
    }

    public final JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(this.getSerializer()).toString());
        this.toJson(json);
        return json;
    }

    public abstract void toJson(JsonObject var1);

    public abstract void write(FriendlyByteBuf var1);
}
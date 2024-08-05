package org.violetmoon.zeta.recipe;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntLists;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.config.ConfigFlagManager;

public class FlagIngredient extends Ingredient implements IZetaIngredient<FlagIngredient> {

    private final Ingredient parent;

    private final ConfigFlagManager cfm;

    private final String flag;

    private final IZetaIngredientSerializer<FlagIngredient> serializer;

    public FlagIngredient(Ingredient parent, String flag, ConfigFlagManager cfm, IZetaIngredientSerializer<FlagIngredient> serializer) {
        super(Stream.of());
        this.parent = parent;
        this.cfm = cfm;
        this.flag = flag;
        this.serializer = serializer;
    }

    @NotNull
    @Override
    public ItemStack[] getItems() {
        return !this.cfm.getFlag(this.flag) ? new ItemStack[0] : this.parent.getItems();
    }

    @NotNull
    @Override
    public IntList getStackingIds() {
        return (IntList) (!this.cfm.getFlag(this.flag) ? IntLists.EMPTY_LIST : this.parent.getStackingIds());
    }

    @Override
    public boolean test(@Nullable ItemStack target) {
        return target != null && this.cfm.getFlag(this.flag) ? this.parent.test(target) : false;
    }

    protected void invalidate() {
    }

    public boolean isSimple() {
        return this.parent.isSimple();
    }

    @Override
    public IZetaIngredientSerializer<FlagIngredient> zetaGetSerializer() {
        return this.serializer;
    }

    public static record Serializer(ConfigFlagManager cfm) implements IZetaIngredientSerializer<FlagIngredient> {

        @Deprecated(forRemoval = true)
        public static FlagIngredient.Serializer INSTANCE;

        @NotNull
        public FlagIngredient parse(@NotNull FriendlyByteBuf buffer) {
            return new FlagIngredient(Ingredient.fromNetwork(buffer), buffer.readUtf(), this.cfm, this);
        }

        @NotNull
        public FlagIngredient parse(@NotNull JsonObject json) {
            Ingredient value = Ingredient.fromJson(json.get("value"));
            String flag = json.getAsJsonPrimitive("flag").getAsString();
            return new FlagIngredient(value, flag, this.cfm, this);
        }

        public void write(@NotNull FriendlyByteBuf buffer, @NotNull FlagIngredient ingredient) {
            ingredient.parent.toNetwork(buffer);
            buffer.writeUtf(ingredient.flag);
        }

        @Override
        public Zeta getZeta() {
            return this.cfm.zeta;
        }
    }
}
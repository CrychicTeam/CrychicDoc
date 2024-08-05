package snownee.lychee.compat.forge;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

public class AlwaysTrueIngredient extends AbstractIngredient {

    private AlwaysTrueIngredient() {
    }

    @Override
    public boolean test(@Nullable ItemStack itemStack) {
        return true;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return AlwaysTrueIngredient.Serializer.INSTANCE;
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(AlwaysTrueIngredient.Serializer.INSTANCE).toString());
        return json;
    }

    public static enum Serializer implements IIngredientSerializer<AlwaysTrueIngredient> {

        INSTANCE;

        private final Supplier<AlwaysTrueIngredient> supplier = Suppliers.memoize(AlwaysTrueIngredient::new);

        public AlwaysTrueIngredient parse(FriendlyByteBuf buffer) {
            return (AlwaysTrueIngredient) this.supplier.get();
        }

        public AlwaysTrueIngredient parse(JsonObject json) {
            return (AlwaysTrueIngredient) this.supplier.get();
        }

        public void write(FriendlyByteBuf buffer, AlwaysTrueIngredient ingredient) {
        }
    }
}
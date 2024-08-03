package dev.xkmc.l2library.serial.ingredients;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

@SerialClass
public abstract class BaseIngredient<T extends BaseIngredient<T>> extends AbstractIngredient {

    @Deprecated
    protected BaseIngredient() {
    }

    public BaseIngredient(ItemStack display) {
        super(Stream.of(new Ingredient.ItemValue(display)));
    }

    protected BaseIngredient(Stream<? extends Ingredient.Value> values) {
        super(values);
    }

    protected abstract T validate();

    @Override
    public abstract boolean test(ItemStack var1);

    @Override
    public boolean isSimple() {
        return false;
    }

    public abstract BaseIngredient.Serializer<T> getSerializer();

    @Override
    public JsonElement toJson() {
        JsonObject obj = JsonCodec.toJson(this).getAsJsonObject();
        obj.addProperty("type", this.getSerializer().id().toString());
        return obj;
    }

    public static record Serializer<T extends BaseIngredient<T>>(Class<T> cls, ResourceLocation id) implements IIngredientSerializer<T> {

        public T parse(FriendlyByteBuf buffer) {
            return (T) ((BaseIngredient) PacketCodec.from(buffer, this.cls, null)).validate();
        }

        public T parse(JsonObject json) {
            return (T) ((BaseIngredient) JsonCodec.from(json, this.cls, null)).validate();
        }

        public void write(FriendlyByteBuf buffer, T ingredient) {
            PacketCodec.to(buffer, ingredient, this.cls);
        }
    }
}
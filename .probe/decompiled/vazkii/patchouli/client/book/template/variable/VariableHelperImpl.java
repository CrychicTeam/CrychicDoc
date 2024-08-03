package vazkii.patchouli.client.book.template.variable;

import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableSerializer;
import vazkii.patchouli.api.VariableHelper;

public class VariableHelperImpl implements VariableHelper {

    public Map<Class<?>, IVariableSerializer<?>> serializers = new HashMap();

    public VariableHelperImpl() {
        this.registerSerializer(new ItemStackVariableSerializer(), ItemStack.class);
        this.registerSerializer(new ItemStackArrayVariableSerializer(), ItemStack[].class);
        this.registerSerializer(new IngredientVariableSerializer(), Ingredient.class);
        this.registerSerializer(new TextComponentVariableSerializer(), Component.class);
    }

    @Override
    public <T> IVariableSerializer<T> serializerForClass(Class<T> clazz) {
        IVariableSerializer<T> serializer = (IVariableSerializer<T>) this.serializers.get(clazz);
        if (serializer == null && clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            IVariableSerializer<?> componentSerializer = this.serializerForClass((Class<T>) componentType);
            if (componentSerializer != null) {
                IVariableSerializer<T> arraySerializer = (IVariableSerializer<T>) (new GenericArrayVariableSerializer<>(componentSerializer, componentType));
                this.serializers.put(clazz, arraySerializer);
                return arraySerializer;
            }
        }
        return serializer;
    }

    @Override
    public <T> IVariable createFromObject(T object) {
        Class<?> clazz = object.getClass();
        for (Entry<Class<?>, IVariableSerializer<?>> e : this.serializers.entrySet()) {
            if (((Class) e.getKey()).isAssignableFrom(clazz)) {
                return this.create(((IVariableSerializer) e.getValue()).toJson(object), clazz);
            }
        }
        throw new IllegalArgumentException(String.format("Can't serialize object %s of type %s to IVariable", object, clazz));
    }

    @Override
    public IVariable createFromJson(JsonElement elem) {
        return this.create(elem, null);
    }

    private IVariable create(JsonElement elem, Class<?> originator) {
        return new Variable(elem, originator);
    }

    @Override
    public <T> VariableHelper registerSerializer(IVariableSerializer<T> serializer, Class<T> clazz) {
        this.serializers.put(clazz, serializer);
        return this;
    }
}
package mezz.jei.library.ingredients;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.common.util.ErrorUtil;
import org.jetbrains.annotations.Unmodifiable;

public class RegisteredIngredients {

    @Unmodifiable
    private final List<IIngredientType<?>> orderedTypes;

    @Unmodifiable
    private final Map<IIngredientType<?>, IngredientInfo<?>> typeToInfo;

    private final Map<Class<?>, IIngredientType<?>> classToType;

    public RegisteredIngredients(List<IngredientInfo<?>> ingredientInfoList) {
        this.orderedTypes = ingredientInfoList.stream().map(IngredientInfo::getIngredientType).toList();
        this.typeToInfo = (Map<IIngredientType<?>, IngredientInfo<?>>) ingredientInfoList.stream().collect(Collectors.toUnmodifiableMap(IngredientInfo::getIngredientType, Function.identity()));
        this.classToType = (Map<Class<?>, IIngredientType<?>>) this.orderedTypes.stream().collect(Collectors.toMap(IIngredientType::getIngredientClass, Function.identity()));
    }

    public <V> IngredientInfo<V> getIngredientInfo(IIngredientType<V> ingredientType) {
        ErrorUtil.checkNotNull(ingredientType, "ingredientType");
        IngredientInfo<V> ingredientInfo = (IngredientInfo<V>) this.typeToInfo.get(ingredientType);
        if (ingredientInfo == null) {
            throw new IllegalArgumentException("Unknown ingredient type: " + ingredientType.getIngredientClass());
        } else {
            return ingredientInfo;
        }
    }

    @Unmodifiable
    public List<IIngredientType<?>> getIngredientTypes() {
        return this.orderedTypes;
    }

    public <V> Optional<IIngredientType<V>> getIngredientType(V ingredient) {
        ErrorUtil.checkNotNull(ingredient, "ingredient");
        Class<? extends V> ingredientClass = ingredient.getClass();
        return this.getIngredientType(ingredientClass);
    }

    public <V> Optional<IIngredientType<V>> getIngredientType(Class<? extends V> ingredientClass) {
        ErrorUtil.checkNotNull(ingredientClass, "ingredientClass");
        IIngredientType<V> ingredientType = (IIngredientType<V>) this.classToType.get(ingredientClass);
        if (ingredientType != null) {
            return Optional.of(ingredientType);
        } else {
            for (IIngredientType<?> type : this.orderedTypes) {
                if (type.getIngredientClass().isAssignableFrom(ingredientClass)) {
                    this.classToType.put(ingredientClass, type);
                    return Optional.of(type);
                }
            }
            return Optional.empty();
        }
    }
}
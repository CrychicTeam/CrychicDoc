package mezz.jei.library.ingredients.subtypes;

import java.util.Optional;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.core.collect.Table;

public class SubtypeInterpreters {

    private final Table<IIngredientTypeWithSubtypes<?, ?>, Object, IIngredientSubtypeInterpreter<?>> table = Table.identityHashBasedTable();

    public <B, I> void addInterpreter(IIngredientTypeWithSubtypes<B, I> type, B base, IIngredientSubtypeInterpreter<I> interpreter) {
        this.table.put(type, base, interpreter);
    }

    public <B, I> Optional<IIngredientSubtypeInterpreter<I>> get(IIngredientTypeWithSubtypes<B, I> type, I ingredient) {
        B base = type.getBase(ingredient);
        IIngredientSubtypeInterpreter<?> interpreter = this.table.get(type, base);
        return Optional.ofNullable(interpreter);
    }

    public <B> boolean contains(IIngredientTypeWithSubtypes<B, ?> type, B base) {
        return this.table.contains(type, base);
    }
}
package mezz.jei.library.ingredients;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.subtypes.UidContext;

public class IngredientSet<V> extends AbstractSet<V> {

    private final Function<V, String> uidGenerator;

    private final Map<String, V> ingredients;

    public static <V> IngredientSet<V> create(IIngredientHelper<V> ingredientHelper, UidContext context) {
        Function<V, String> uidGenerator = v -> ingredientHelper.getUniqueId((V) v, context);
        return new IngredientSet<>(uidGenerator);
    }

    private IngredientSet(Function<V, String> uidGenerator) {
        this.uidGenerator = uidGenerator;
        this.ingredients = new LinkedHashMap();
    }

    public boolean add(V v) {
        String uid = (String) this.uidGenerator.apply(v);
        return this.ingredients.put(uid, v) == null;
    }

    public boolean remove(Object o) {
        String uid = (String) this.uidGenerator.apply(o);
        return this.ingredients.remove(uid) != null;
    }

    public boolean removeAll(Collection<?> c) {
        if (c instanceof IngredientSet) {
            return super.removeAll(c);
        } else {
            boolean modified = false;
            for (Object aC : c) {
                modified |= this.remove(aC);
            }
            return modified;
        }
    }

    public boolean contains(Object o) {
        String uid = (String) this.uidGenerator.apply(o);
        return this.ingredients.containsKey(uid);
    }

    public Optional<V> getByUid(String uid) {
        V v = (V) this.ingredients.get(uid);
        return Optional.ofNullable(v);
    }

    public void clear() {
        this.ingredients.clear();
    }

    public Iterator<V> iterator() {
        return this.ingredients.values().iterator();
    }

    public int size() {
        return this.ingredients.size();
    }
}
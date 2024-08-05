package dev.latvian.mods.kubejs.recipe.schema;

import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.util.CustomJavaToJsWrapper;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RecipeComponentFactory extends CustomJavaToJsWrapper {

    @Nullable
    RecipeComponent<?> create(Context var1, Scriptable var2, Map<String, Object> var3);

    @Override
    default Scriptable convertJavaToJs(Context cx, Scriptable scope, Class<?> staticType) {
        return new RecipeComponentFactory.RecipeComponentFactoryJS(this);
    }

    public static record Dynamic(DynamicRecipeComponent component) implements RecipeComponentFactory {

        @Nullable
        @Override
        public RecipeComponent<?> create(Context cx, Scriptable scope, Map<String, Object> args) {
            return this.component.factory().create(cx, scope, args);
        }
    }

    public static class RecipeComponentFactoryJS extends BaseFunction {

        private final RecipeComponentFactory factory;

        public RecipeComponentFactoryJS(RecipeComponentFactory factory) {
            this.factory = factory;
        }

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            Map<? extends Object, ? extends Object> map = args.length == 0 ? Map.of() : MapJS.of(args[0]);
            RecipeComponent component = this.factory.create(cx, scope, map);
            if (component == null) {
                throw new RuntimeException("Invalid dynamic recipe component arguments: " + map);
            } else {
                return component;
            }
        }
    }

    public static record Simple(RecipeComponent<?> component) implements RecipeComponentFactory {

        @Override
        public RecipeComponent<?> create(Context cx, Scriptable scope, Map<String, Object> args) {
            return this.component;
        }
    }
}
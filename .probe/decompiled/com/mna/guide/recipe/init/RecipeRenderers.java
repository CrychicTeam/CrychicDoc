package com.mna.guide.recipe.init;

import com.mna.ManaAndArtifice;
import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.guide.recipe.Recipe3x3;
import com.mna.guide.recipe.RecipeArcaneFurnace;
import com.mna.guide.recipe.RecipeCrushing;
import com.mna.guide.recipe.RecipeEldrinAltar;
import com.mna.guide.recipe.RecipeEntity;
import com.mna.guide.recipe.RecipeFumeFilter;
import com.mna.guide.recipe.RecipeManaweavePattern;
import com.mna.guide.recipe.RecipeManaweaving;
import com.mna.guide.recipe.RecipeMaterialsList;
import com.mna.guide.recipe.RecipeRitual;
import com.mna.guide.recipe.RecipeRunescribing;
import com.mna.guide.recipe.RecipeRunesmith;
import com.mna.guide.recipe.RecipeSpellPart;
import com.mna.guide.recipe.RecipeTransmute;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class RecipeRenderers {

    public static final String CRAFTING = "crafting";

    public static final String MANAWEAVING_PATTERN = "manaweaving_pattern";

    public static final String MANAWEAVING_ALTAR = "manaweaving_altar";

    public static final String RITUAL = "ritual";

    public static final String RUNESMITHING = "runesmithing";

    public static final String ARCANE_FURNACE = "arcane_furnace";

    public static final String CRUSHING = "crushing";

    public static final String RUNESCRIBING = "runescribing";

    public static final String SPELL = "spell_part";

    public static final String ELDRIN_ALTAR = "eldrin_altar";

    public static final String MULTIBLOCK = "multiblock";

    public static final String ENTITY = "entity";

    public static final String TRANSMUTATION = "transmutation";

    public static final String FUME_FILTER = "fume_filter";

    private static final HashMap<String, Class<? extends RecipeRendererBase>> _registry = new HashMap();

    public static void registerRecipeRenderer(String identifier, Class<? extends RecipeRendererBase> clazz) {
        if (_registry.containsKey(identifier)) {
            ManaAndArtifice.LOGGER.warn("Registering new renderer for " + identifier + ", this will override what is already there.");
        }
        _registry.put(identifier, clazz);
    }

    public static RecipeRendererBase instantiate(String _type, int x, int y) {
        Class<? extends RecipeRendererBase> clazz = (Class<? extends RecipeRendererBase>) _registry.getOrDefault(_type, null);
        if (clazz == null) {
            return null;
        } else {
            try {
                Constructor<? extends RecipeRendererBase> ctor = clazz.getDeclaredConstructor(int.class, int.class);
                return ctor != null ? (RecipeRendererBase) ctor.newInstance(x, y) : null;
            } catch (NoSuchMethodException var5) {
                return null;
            } catch (SecurityException var6) {
                return null;
            } catch (InstantiationException var7) {
                return null;
            } catch (IllegalAccessException var8) {
                return null;
            } catch (IllegalArgumentException var9) {
                return null;
            } catch (InvocationTargetException var10) {
                return null;
            }
        }
    }

    static {
        registerRecipeRenderer("crafting", Recipe3x3.class);
        registerRecipeRenderer("manaweaving_pattern", RecipeManaweavePattern.class);
        registerRecipeRenderer("manaweaving_altar", RecipeManaweaving.class);
        registerRecipeRenderer("ritual", RecipeRitual.class);
        registerRecipeRenderer("runesmithing", RecipeRunesmith.class);
        registerRecipeRenderer("arcane_furnace", RecipeArcaneFurnace.class);
        registerRecipeRenderer("crushing", RecipeCrushing.class);
        registerRecipeRenderer("runescribing", RecipeRunescribing.class);
        registerRecipeRenderer("spell_part", RecipeSpellPart.class);
        registerRecipeRenderer("eldrin_altar", RecipeEldrinAltar.class);
        registerRecipeRenderer("multiblock", RecipeMaterialsList.class);
        registerRecipeRenderer("entity", RecipeEntity.class);
        registerRecipeRenderer("transmutation", RecipeTransmute.class);
        registerRecipeRenderer("fume_filter", RecipeFumeFilter.class);
    }
}
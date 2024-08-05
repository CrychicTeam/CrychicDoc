package mezz.jei.api.recipe;

import net.minecraft.resources.ResourceLocation;

public final class RecipeType<T> {

    private final ResourceLocation uid;

    private final Class<? extends T> recipeClass;

    public static <T> RecipeType<T> create(String nameSpace, String path, Class<? extends T> recipeClass) {
        ResourceLocation uid = new ResourceLocation(nameSpace, path);
        return new RecipeType<>(uid, recipeClass);
    }

    public RecipeType(ResourceLocation uid, Class<? extends T> recipeClass) {
        this.uid = uid;
        this.recipeClass = recipeClass;
    }

    public ResourceLocation getUid() {
        return this.uid;
    }

    public Class<? extends T> getRecipeClass() {
        return this.recipeClass;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return !(obj instanceof RecipeType<?> other) ? false : this.recipeClass == other.recipeClass && this.uid.equals(other.uid);
        }
    }

    public int hashCode() {
        return 31 * this.uid.hashCode() + this.recipeClass.hashCode();
    }

    public String toString() {
        return "RecipeType[uid=" + this.uid + ", recipeClass=" + this.recipeClass + "]";
    }
}
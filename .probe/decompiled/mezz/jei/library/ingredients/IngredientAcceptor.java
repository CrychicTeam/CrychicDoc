package mezz.jei.library.ingredients;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.platform.IPlatformFluidHelperInternal;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

public class IngredientAcceptor implements IIngredientAcceptor<IngredientAcceptor> {

    private final IIngredientManager ingredientManager;

    private final List<Optional<ITypedIngredient<?>>> ingredients = new ArrayList();

    private final Set<IIngredientType<?>> types = new HashSet();

    public IngredientAcceptor(IIngredientManager ingredientManager) {
        this.ingredientManager = ingredientManager;
    }

    public IngredientAcceptor addIngredientsUnsafe(List<?> ingredients) {
        Preconditions.checkNotNull(ingredients, "ingredients");
        for (Object ingredient : ingredients) {
            Optional<ITypedIngredient<?>> typedIngredient = TypedIngredient.createAndFilterInvalid(this.ingredientManager, ingredient);
            typedIngredient.ifPresent(i -> this.types.add(i.getType()));
            this.ingredients.add(typedIngredient);
        }
        return this;
    }

    public <T> IngredientAcceptor addIngredients(IIngredientType<T> ingredientType, List<T> ingredients) {
        ErrorUtil.checkNotNull(ingredientType, "ingredientType");
        Preconditions.checkNotNull(ingredients, "ingredients");
        for (T ingredient : ingredients) {
            this.addIngredientInternal(ingredientType, ingredient);
        }
        return this;
    }

    public <T> IngredientAcceptor addIngredient(IIngredientType<T> ingredientType, T ingredient) {
        ErrorUtil.checkNotNull(ingredientType, "ingredientType");
        ErrorUtil.checkNotNull(ingredient, "ingredient");
        this.addIngredientInternal(ingredientType, ingredient);
        return this;
    }

    public IngredientAcceptor addFluidStack(Fluid fluid, long amount) {
        IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
        return this.addFluidInternal(fluidHelper, fluid, amount, null);
    }

    public IngredientAcceptor addFluidStack(Fluid fluid, long amount, CompoundTag tag) {
        IPlatformFluidHelperInternal<?> fluidHelper = Services.PLATFORM.getFluidHelper();
        return this.addFluidInternal(fluidHelper, fluid, amount, tag);
    }

    private <T> IngredientAcceptor addFluidInternal(IPlatformFluidHelperInternal<T> fluidHelper, Fluid fluid, long amount, @Nullable CompoundTag tag) {
        T fluidStack = fluidHelper.create(fluid, amount, tag);
        IIngredientTypeWithSubtypes<Fluid, T> fluidIngredientType = fluidHelper.getFluidIngredientType();
        this.addIngredientInternal(fluidIngredientType, fluidStack);
        return this;
    }

    private <T> void addIngredientInternal(IIngredientType<T> ingredientType, @Nullable T ingredient) {
        Optional<ITypedIngredient<T>> typedIngredient = TypedIngredient.createAndFilterInvalid(this.ingredientManager, ingredientType, ingredient);
        typedIngredient.ifPresent(i -> this.types.add(i.getType()));
        this.ingredients.add(typedIngredient.map(Function.identity()));
    }

    public <T> Stream<T> getIngredients(IIngredientType<T> ingredientType) {
        return this.ingredients.stream().flatMap(Optional::stream).map(i -> i.getIngredient(ingredientType)).flatMap(Optional::stream);
    }

    public Stream<IIngredientType<?>> getIngredientTypes() {
        return this.types.stream();
    }

    @UnmodifiableView
    public List<Optional<ITypedIngredient<?>>> getAllIngredients() {
        return Collections.unmodifiableList(this.ingredients);
    }

    public IntSet getMatches(IFocusGroup focusGroup, RecipeIngredientRole role) {
        int[] matches = focusGroup.getFocuses(role).flatMapToInt(this::getMatches).distinct().toArray();
        return new IntArraySet(matches);
    }

    private <T> IntStream getMatches(IFocus<T> focus) {
        List<Optional<ITypedIngredient<?>>> ingredients = this.getAllIngredients();
        if (ingredients.isEmpty()) {
            return IntStream.empty();
        } else {
            ITypedIngredient<T> focusValue = focus.getTypedValue();
            IIngredientType<T> ingredientType = focusValue.getType();
            T focusIngredient = focusValue.getIngredient();
            IIngredientHelper<T> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
            String focusUid = ingredientHelper.getUniqueId(focusIngredient, UidContext.Ingredient);
            return IntStream.range(0, ingredients.size()).filter(i -> (Boolean) ((Optional) ingredients.get(i)).flatMap(typedIngredient -> typedIngredient.getIngredient(ingredientType)).map(ingredient -> {
                String uniqueId = ingredientHelper.getUniqueId((T) ingredient, UidContext.Ingredient);
                return focusUid.equals(uniqueId);
            }).orElse(false));
        }
    }
}
package mezz.jei.common.transfer;

import mezz.jei.api.recipe.transfer.IRecipeTransferError;

public class RecipeTransferErrorInternal implements IRecipeTransferError {

    public static final RecipeTransferErrorInternal INSTANCE = new RecipeTransferErrorInternal();

    private RecipeTransferErrorInternal() {
    }

    @Override
    public IRecipeTransferError.Type getType() {
        return IRecipeTransferError.Type.INTERNAL;
    }
}
package mezz.jei.common.transfer;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;

public class RecipeTransferOperationsResult {

    public final List<TransferOperation> results = new ArrayList();

    public final List<IRecipeSlotView> missingItems = new ArrayList();
}
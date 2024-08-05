package io.github.lightman314.lightmanscurrency.api.traders.trade.comparison;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ProductComparisonResult {

    private final boolean sameProduct;

    private final boolean sameNBT;

    private final int quantityDifference;

    public boolean Identical() {
        return this.SameProductType() && this.SameProductNBT() && this.SameProductQuantity();
    }

    public boolean SameProductType() {
        return this.sameProduct;
    }

    public boolean SameProductNBT() {
        return this.sameNBT;
    }

    public boolean SameProductQuantity() {
        return this.quantityDifference == 0;
    }

    public int ProductQuantityDifference() {
        return this.quantityDifference;
    }

    private ProductComparisonResult(boolean sameProduct, boolean sameNBT, int quantityDifference) {
        this.sameProduct = sameProduct;
        this.sameNBT = sameNBT;
        this.quantityDifference = quantityDifference;
    }

    public static ProductComparisonResult CompareItem(ItemStack trueItem, ItemStack expectedItem) {
        return CompareItem(trueItem, expectedItem, true);
    }

    public static ProductComparisonResult CompareItem(ItemStack trueItem, ItemStack expectedItem, boolean checkNBT) {
        boolean isItemEqual = trueItem.getItem() == expectedItem.getItem();
        boolean isTagEqual;
        if (checkNBT) {
            if (trueItem.getTag() != null) {
                isTagEqual = trueItem.getTag().equals(expectedItem.getTag());
            } else {
                isTagEqual = expectedItem.getTag() == null || expectedItem.getTag().isEmpty();
            }
        } else {
            isTagEqual = true;
        }
        int quantityDifference = trueItem.getCount() - expectedItem.getCount();
        return new ProductComparisonResult(isItemEqual, isTagEqual, quantityDifference);
    }

    public static ProductComparisonResult CreateRaw(boolean sameProduct, boolean sameNBT, int quantityDifference) {
        return new ProductComparisonResult(sameProduct, sameNBT, quantityDifference);
    }

    public static List<ProductComparisonResult> CompareTwoItems(ItemStack true1, ItemStack true2, ItemStack expected1, ItemStack expected2) {
        return CompareTwoItems(true1, true2, expected1, expected2, true);
    }

    public static List<ProductComparisonResult> CompareTwoItems(ItemStack true1, ItemStack true2, ItemStack expected1, ItemStack expected2, boolean checkNBT) {
        List<ProductComparisonResult> results = new ArrayList();
        boolean flipMatch = true1.getItem() == expected2.getItem() && true2.getItem() == expected1.getItem() && true1.getItem() != true2.getItem() && expected1.getItem() != expected2.getItem();
        if (flipMatch) {
            results.add(CompareItem(true1, expected2, checkNBT));
            results.add(CompareItem(true2, expected1));
        } else {
            results.add(CompareItem(true1, expected1));
            results.add(CompareItem(true2, expected2));
        }
        return results;
    }

    public static ProductComparisonResult CompareFluid(FluidStack trueFluid, FluidStack expectedFluid) {
        boolean isFluidEqual = trueFluid.getFluid() == expectedFluid.getFluid();
        boolean isTagEqual;
        if (trueFluid.getTag() != null) {
            isTagEqual = trueFluid.getTag().equals(expectedFluid.getTag());
        } else {
            isTagEqual = expectedFluid.getTag() == null;
        }
        int quantityDifference = trueFluid.getAmount() - expectedFluid.getAmount();
        return new ProductComparisonResult(isFluidEqual, isTagEqual, quantityDifference);
    }

    public static ProductComparisonResult CompareEnergy(int original, int query) {
        return new ProductComparisonResult(true, true, original - query);
    }
}
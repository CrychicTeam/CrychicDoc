package mezz.jei.common.config;

import java.util.function.Supplier;
import mezz.jei.common.config.file.IConfigCategoryBuilder;
import mezz.jei.common.config.file.IConfigSchemaBuilder;
import mezz.jei.common.util.HorizontalAlignment;
import mezz.jei.common.util.NavigationVisibility;
import mezz.jei.common.util.VerticalAlignment;

public class IngredientGridConfig implements IIngredientGridConfig {

    private static final int minNumRows = 1;

    private static final int defaultNumRows = 16;

    private static final int largestNumRows = 100;

    private static final int minNumColumns = 4;

    private static final int defaultNumColumns = 9;

    private static final int largestNumColumns = 100;

    private static final VerticalAlignment defaultVerticalAlignment = VerticalAlignment.TOP;

    private static final NavigationVisibility defaultButtonNavigationVisibility = NavigationVisibility.ENABLED;

    private static final boolean defaultDrawBackground = false;

    private final Supplier<Integer> maxRows;

    private final Supplier<Integer> maxColumns;

    private final Supplier<HorizontalAlignment> horizontalAlignment;

    private final Supplier<VerticalAlignment> verticalAlignment;

    private final Supplier<NavigationVisibility> buttonNavigationVisibility;

    private final Supplier<Boolean> drawBackground;

    public IngredientGridConfig(String categoryName, IConfigSchemaBuilder builder, HorizontalAlignment defaultHorizontalAlignment) {
        IConfigCategoryBuilder category = builder.addCategory(categoryName);
        this.maxRows = category.addInteger("MaxRows", 16, 1, 100, "Max number of rows shown");
        this.maxColumns = category.addInteger("MaxColumns", 9, 4, 100, "Max number of columns shown");
        this.horizontalAlignment = category.addEnum("HorizontalAlignment", defaultHorizontalAlignment, "Horizontal alignment of the ingredient grid inside the available area");
        this.verticalAlignment = category.addEnum("VerticalAlignment", defaultVerticalAlignment, "Vertical alignment of the ingredient grid inside the available area");
        this.buttonNavigationVisibility = category.addEnum("ButtonNavigationVisibility", defaultButtonNavigationVisibility, "Visibility of the top page buttons. Use AUTO_HIDE to only show it when there are multiple pages.");
        this.drawBackground = category.addBoolean("DrawBackground", false, "Set to true to draw a background texture behind the gui.");
    }

    @Override
    public int getMinColumns() {
        return 4;
    }

    @Override
    public int getMinRows() {
        return 1;
    }

    @Override
    public HorizontalAlignment getHorizontalAlignment() {
        return (HorizontalAlignment) this.horizontalAlignment.get();
    }

    @Override
    public VerticalAlignment getVerticalAlignment() {
        return (VerticalAlignment) this.verticalAlignment.get();
    }

    @Override
    public boolean drawBackground() {
        return (Boolean) this.drawBackground.get();
    }

    @Override
    public int getMaxColumns() {
        return (Integer) this.maxColumns.get();
    }

    @Override
    public int getMaxRows() {
        return (Integer) this.maxRows.get();
    }

    @Override
    public NavigationVisibility getButtonNavigationVisibility() {
        return (NavigationVisibility) this.buttonNavigationVisibility.get();
    }
}
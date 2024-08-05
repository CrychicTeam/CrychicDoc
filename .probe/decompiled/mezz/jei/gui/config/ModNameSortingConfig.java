package mezz.jei.gui.config;

import java.nio.file.Path;
import java.util.Comparator;
import mezz.jei.common.config.sorting.MappedSortingConfig;
import mezz.jei.common.config.sorting.serializers.SortingSerializers;
import mezz.jei.gui.ingredients.IListElementInfo;

public class ModNameSortingConfig extends MappedSortingConfig<IListElementInfo<?>, String> {

    public ModNameSortingConfig(Path path) {
        super(path, SortingSerializers.STRING, IListElementInfo::getModNameForSorting);
    }

    @Override
    protected Comparator<String> getDefaultSortOrder() {
        Comparator<String> minecraftFirst = Comparator.comparing(s -> s.equals("Minecraft")).reversed();
        Comparator<String> naturalOrder = Comparator.naturalOrder();
        return minecraftFirst.thenComparing(naturalOrder);
    }
}
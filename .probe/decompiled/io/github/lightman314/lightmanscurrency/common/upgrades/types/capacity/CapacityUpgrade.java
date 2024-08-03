package io.github.lightman314.lightmanscurrency.common.upgrades.types.capacity;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public abstract class CapacityUpgrade extends UpgradeType {

    public static String CAPACITY = "capacity";

    private static final List<String> DATA_TAGS = Lists.newArrayList(new String[] { CAPACITY });

    @Nonnull
    @Override
    protected List<String> getDataTags() {
        return DATA_TAGS;
    }

    @Override
    protected Object defaultTagValue(String tag) {
        return Objects.equals(tag, CAPACITY) ? 1 : null;
    }
}
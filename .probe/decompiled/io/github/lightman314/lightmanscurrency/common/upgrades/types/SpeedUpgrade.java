package io.github.lightman314.lightmanscurrency.common.upgrades.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeData;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;

public class SpeedUpgrade extends UpgradeType {

    public static String DELAY_AMOUNT = "delay";

    private static final List<String> DATA_TAGS = Lists.newArrayList(new String[] { DELAY_AMOUNT });

    @Nonnull
    @Override
    protected List<String> getDataTags() {
        return DATA_TAGS;
    }

    @Override
    protected Object defaultTagValue(String tag) {
        return Objects.equals(tag, DELAY_AMOUNT) ? 1 : null;
    }

    @Nonnull
    @Override
    public List<Component> getTooltip(@Nonnull UpgradeData data) {
        return Lists.newArrayList(new Component[] { LCText.TOOLTIP_UPGRADE_SPEED.get(data.getIntValue(DELAY_AMOUNT)) });
    }

    @Nonnull
    @Override
    protected List<Component> getBuiltInTargets() {
        return ImmutableList.of(LCText.TOOLTIP_UPGRADE_TARGET_TRADER_INTERFACE.get());
    }
}
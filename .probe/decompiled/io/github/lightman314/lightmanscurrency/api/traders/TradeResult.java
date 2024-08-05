package io.github.lightman314.lightmanscurrency.api.traders;

import io.github.lightman314.lightmanscurrency.LCText;
import javax.annotation.Nullable;
import net.minecraft.network.chat.MutableComponent;

public enum TradeResult {

    SUCCESS,
    FAIL_OUT_OF_STOCK,
    FAIL_CANNOT_AFFORD,
    FAIL_NO_OUTPUT_SPACE,
    FAIL_NO_INPUT_SPACE,
    FAIL_TRADE_RULE_DENIAL,
    FAIL_TAX_EXCEEDED_LIMIT,
    FAIL_INVALID_TRADE,
    FAIL_NOT_SUPPORTED,
    FAIL_NULL;

    public static final TradeResult[] ALL_WITH_MESSAGES = new TradeResult[] { FAIL_OUT_OF_STOCK, FAIL_CANNOT_AFFORD, FAIL_NO_OUTPUT_SPACE, FAIL_NO_INPUT_SPACE, FAIL_TRADE_RULE_DENIAL, FAIL_TAX_EXCEEDED_LIMIT, FAIL_INVALID_TRADE, FAIL_NOT_SUPPORTED, FAIL_NULL };

    public boolean isSuccess() {
        return this == SUCCESS;
    }

    public boolean hasMessage() {
        return this.getMessage() != null;
    }

    @Nullable
    public final MutableComponent getMessage() {
        return LCText.GUI_TRADE_RESULT.get(this).get();
    }
}
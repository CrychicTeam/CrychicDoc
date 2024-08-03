package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.coin_management;

import io.github.lightman314.lightmanscurrency.api.money.coins.data.coin.CoinEntry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;

public class EditableSideChain {

    @Nonnull
    public List<EditableCoinEntry> entries = new ArrayList();

    @Nullable
    public Item getParentCoin() {
        return this.entries.isEmpty() ? null : ((EditableCoinEntry) this.entries.get(0)).sideChainParent;
    }

    private EditableSideChain() {
    }

    public EditableSideChain(@Nonnull List<CoinEntry> sideChainList) {
        if (!sideChainList.isEmpty()) {
            for (CoinEntry e : sideChainList) {
                this.entries.add(new EditableCoinEntry(e));
            }
        }
    }

    public EditableSideChain(@Nonnull Item parentCoin, @Nonnull Item rootCoin, int exchangeRate) {
        this.entries.add(new EditableCoinEntry(rootCoin, exchangeRate, parentCoin));
    }

    @Nonnull
    public EditableSideChain copy() {
        EditableSideChain clone = new EditableSideChain();
        for (EditableCoinEntry e : this.entries) {
            clone.entries.add(e.copy());
        }
        return clone;
    }
}
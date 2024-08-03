package org.violetmoon.zetaimplforge.event.play.loading;

import java.util.List;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.WandererTradesEvent;
import org.violetmoon.zeta.event.play.loading.ZWandererTrades;

public class ForgeZWandererTrades implements ZWandererTrades {

    private final WandererTradesEvent e;

    public ForgeZWandererTrades(WandererTradesEvent e) {
        this.e = e;
    }

    @Override
    public List<VillagerTrades.ItemListing> getGenericTrades() {
        return this.e.getGenericTrades();
    }

    @Override
    public List<VillagerTrades.ItemListing> getRareTrades() {
        return this.e.getRareTrades();
    }
}
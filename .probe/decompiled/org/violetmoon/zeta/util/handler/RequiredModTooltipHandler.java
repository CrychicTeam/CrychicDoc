package org.violetmoon.zeta.util.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.config.ZetaGeneralConfig;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.ZItemTooltip;

public class RequiredModTooltipHandler {

    private final Map<Item, String> items = new HashMap();

    private final Map<Block, String> blocks = new HashMap();

    public void map(Item item, String mod) {
        this.items.put(item, mod);
    }

    public void map(Block block, String mod) {
        this.blocks.put(block, mod);
    }

    public List<ItemStack> disabledItems() {
        return (List<ItemStack>) (!ZetaGeneralConfig.hideDisabledContent ? new ArrayList() : this.items.entrySet().stream().filter(entry -> !ModList.get().isLoaded((String) entry.getValue())).map(entry -> new ItemStack((ItemLike) entry.getKey())).toList());
    }

    public static class Client {

        private final Zeta z;

        public Client(Zeta z) {
            this.z = z;
        }

        @PlayEvent
        public void onTooltip(ZItemTooltip event) {
            Map<Item, String> ITEMS = this.z.requiredModTooltipHandler.items;
            Map<Block, String> BLOCKS = this.z.requiredModTooltipHandler.blocks;
            if (!BLOCKS.isEmpty() && event.getEntity() != null && event.getEntity().m_9236_() != null) {
                for (Block b : BLOCKS.keySet()) {
                    ITEMS.put(b.asItem(), (String) BLOCKS.get(b));
                }
                BLOCKS.clear();
            }
            Item item = event.getItemStack().getItem();
            if (ITEMS.containsKey(item)) {
                String mod = (String) ITEMS.get(item);
                if (!this.z.isModLoaded(mod)) {
                    event.getToolTip().add(Component.translatable("quark.misc.mod_disabled", mod).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
}
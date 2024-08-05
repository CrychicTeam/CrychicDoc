package net.mehvahdjukaar.amendments.client;

import java.util.Set;
import java.util.stream.Collectors;
import net.mehvahdjukaar.amendments.client.renderers.CandleHolderRendererExtension;
import net.mehvahdjukaar.amendments.client.renderers.LanternRendererExtension;
import net.mehvahdjukaar.amendments.client.renderers.TorchRendererExtension;
import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.mehvahdjukaar.moonlight.api.item.IThirdPersonSpecialItemRenderer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class ItemHoldingAnimationsManager {

    private static boolean animAdded = false;

    public static void addAnimations() {
        if (!animAdded) {
            animAdded = true;
            if ((Boolean) ClientConfigs.LANTERN_HOLDING.get()) {
                LanternRendererExtension anim = new LanternRendererExtension();
                ((Set) BlockScanner.getLanterns().stream().map(Block::m_5456_).filter(i -> i != Items.AIR).collect(Collectors.toSet())).forEach(item -> IThirdPersonSpecialItemRenderer.attachToItem(item, anim));
            }
            if ((Boolean) ClientConfigs.TORCH_HOLDING.get()) {
                TorchRendererExtension anim = new TorchRendererExtension();
                ((Set) BlockScanner.getTorches().stream().map(Block::m_5456_).filter(i -> i != Items.AIR).collect(Collectors.toSet())).forEach(item -> IThirdPersonSpecialItemRenderer.attachToItem(item, anim));
            }
            if ((Boolean) ClientConfigs.CANDLE_HOLDER_HOLDING.get()) {
                CandleHolderRendererExtension anim = new CandleHolderRendererExtension();
                ((Set) BlockScanner.getCandleHolders().stream().map(Block::m_5456_).filter(i -> i != Items.AIR).collect(Collectors.toSet())).forEach(item -> IThirdPersonSpecialItemRenderer.attachToItem(item, anim));
            }
        }
    }
}
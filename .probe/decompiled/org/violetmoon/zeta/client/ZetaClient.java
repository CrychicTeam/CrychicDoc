package org.violetmoon.zeta.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.client.config.ClientConfigManager;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.event.bus.ZetaEventBus;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.util.zetalist.IZeta;
import org.violetmoon.zeta.util.zetalist.ZetaClientList;

public abstract class ZetaClient implements IZeta {

    public final Zeta zeta;

    public final ZetaEventBus<IZetaLoadEvent> loadBus;

    public final ZetaEventBus<IZetaPlayEvent> playBus;

    public ResourceLocation generalIcons = new ResourceLocation("zeta", "textures/gui/general_icons.png");

    public final ClientTicker ticker;

    public final ClientConfigManager clientConfigManager;

    public final TopLayerTooltipHandler topLayerTooltipHandler;

    public final ClientRegistryExtension clientRegistryExtension;

    public ZetaClient(Zeta zeta) {
        this.zeta = zeta;
        this.loadBus = zeta.loadBus;
        this.playBus = zeta.playBus;
        this.ticker = this.createClientTicker();
        this.clientConfigManager = this.createClientConfigManager();
        this.topLayerTooltipHandler = this.createTopLayerTooltipHandler();
        this.clientRegistryExtension = this.createClientRegistryExtension();
        this.loadBus.subscribe(this.clientRegistryExtension).subscribe(this.clientConfigManager);
        this.playBus.subscribe(this.ticker).subscribe(this.topLayerTooltipHandler);
        ZetaClientList.INSTANCE.register(this);
    }

    public ClientTicker createClientTicker() {
        return new ClientTicker();
    }

    public ClientConfigManager createClientConfigManager() {
        return new ClientConfigManager(this);
    }

    public TopLayerTooltipHandler createTopLayerTooltipHandler() {
        return new TopLayerTooltipHandler();
    }

    public void sendToServer(IZetaMessage msg) {
        if (Minecraft.getInstance().getConnection() != null) {
            this.zeta.network.sendToServer(msg);
        }
    }

    public abstract ClientRegistryExtension createClientRegistryExtension();

    @Nullable
    public abstract BlockColor getBlockColor(BlockColors var1, Block var2);

    @Nullable
    public abstract ItemColor getItemColor(ItemColors var1, ItemLike var2);

    public abstract void setBlockEntityWithoutLevelRenderer(Item var1, BlockEntityWithoutLevelRenderer var2);

    public abstract void setHumanoidArmorModel(Item var1, HumanoidArmorModelGetter var2);

    @Nullable
    public abstract RegistryAccess hackilyGetCurrentClientLevelRegistryAccess();

    public abstract void start();

    @Override
    public Zeta asZeta() {
        return this.zeta;
    }
}
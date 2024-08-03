package snownee.kiwi.customization;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.customization.block.GlassType;
import snownee.kiwi.customization.block.behavior.SitManager;
import snownee.kiwi.customization.block.family.BlockFamilies;
import snownee.kiwi.customization.block.loader.BlockDefinitionProperties;
import snownee.kiwi.customization.block.loader.KBlockDefinition;
import snownee.kiwi.customization.builder.BuilderRules;
import snownee.kiwi.customization.builder.BuildersButton;
import snownee.kiwi.customization.builder.ConvertScreen;
import snownee.kiwi.customization.command.ExportBlocksCommand;
import snownee.kiwi.customization.command.ExportCreativeTabsCommand;
import snownee.kiwi.customization.command.ExportShapesCommand;
import snownee.kiwi.customization.command.PrintFamiliesCommand;
import snownee.kiwi.customization.command.ReloadBlockSettingsCommand;
import snownee.kiwi.customization.command.ReloadFamiliesAndRulesCommand;
import snownee.kiwi.customization.command.ReloadSlotsCommand;
import snownee.kiwi.customization.item.loader.KItemDefinition;
import snownee.kiwi.util.ClientProxy;
import snownee.kiwi.util.ColorProviderUtil;
import snownee.kiwi.util.SmartKey;

public final class CustomizationClient {

    @Nullable
    public static SmartKey buildersButtonKey;

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        modEventBus.addListener(event -> {
            if (CustomizationHooks.kswitch || !BlockFamilies.all().isEmpty() || !BuilderRules.all().isEmpty()) {
                buildersButtonKey = new SmartKey.Builder("key.kiwi.builders_button", "key.categories.gameplay").key(InputConstants.getKey("key.mouse.4")).onLongPress(BuildersButton::onLongPress).onShortPress(BuildersButton::onShortPress).build();
                event.register(buildersButtonKey);
                ClientProxy.afterRegisterSmartKey(buildersButtonKey);
            }
        });
        forgeEventBus.addListener(event -> {
            if (event.phase == TickEvent.Phase.END) {
                ConvertScreen.tickLingering();
            }
        });
        forgeEventBus.addListener(event -> {
            LiteralArgumentBuilder<CommandSourceStack> kiwi = Commands.literal("kiwi");
            LiteralArgumentBuilder<CommandSourceStack> customization = (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("customization").requires(source -> source.hasPermission(2));
            LiteralArgumentBuilder<CommandSourceStack> export = Commands.literal("export");
            ExportBlocksCommand.register(export);
            ExportShapesCommand.register(export);
            ExportCreativeTabsCommand.register(export);
            LiteralArgumentBuilder<CommandSourceStack> reload = Commands.literal("reload");
            ReloadSlotsCommand.register(reload);
            ReloadBlockSettingsCommand.register(reload);
            ReloadFamiliesAndRulesCommand.register(reload);
            PrintFamiliesCommand.register(customization);
            event.getDispatcher().register((LiteralArgumentBuilder) kiwi.then(((LiteralArgumentBuilder) customization.then(export)).then(reload)));
        });
        forgeEventBus.addListener(event -> BuildersButton.renderDebugText(event.getLeft(), event.getRight()));
        forgeEventBus.addListener(event -> {
            if (BuildersButton.cancelRenderHighlight()) {
                event.setCanceled(true);
            }
        });
        forgeEventBus.addListener(event -> {
            if (event.phase == TickEvent.Phase.START) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null && SitManager.isSeatEntity(player.m_20202_())) {
                    SitManager.clampRotation(player, player.m_20202_());
                }
            }
        });
    }

    public static void afterRegister(Map<ResourceLocation, KItemDefinition> items, Map<ResourceLocation, KBlockDefinition> blocks, ClientProxy.Context context) {
        Map<Block, BlockColor> blockColors = Maps.newHashMap();
        Map<Item, ItemColor> itemColors = Maps.newHashMap();
        List<Pair<Block, BlockColor>> blocksToAdd = Lists.newArrayList();
        List<Pair<Item, ItemColor>> itemsToAdd = Lists.newArrayList();
        Set<Item> addedItems = Sets.newHashSet();
        for (Entry<ResourceLocation, KItemDefinition> entry : items.entrySet()) {
            KItemDefinition definition = (KItemDefinition) entry.getValue();
            if (!definition.properties().colorProvider().isEmpty()) {
                Item item = BuiltInRegistries.ITEM.get((ResourceLocation) entry.getKey());
                Item providerItem = BuiltInRegistries.ITEM.get((ResourceLocation) definition.properties().colorProvider().get());
                if (providerItem == Items.AIR) {
                    Kiwi.LOGGER.warn("Cannot find color provider item %s for item %s".formatted(definition.properties().colorProvider().get(), entry.getKey()));
                } else {
                    itemsToAdd.add(Pair.of(item, (ItemColor) itemColors.computeIfAbsent(providerItem, ColorProviderUtil::delegate)));
                    addedItems.add(item);
                }
            }
        }
        for (Entry<ResourceLocation, KBlockDefinition> entryx : blocks.entrySet()) {
            BlockDefinitionProperties properties = ((KBlockDefinition) entryx.getValue()).properties();
            if (context.loading()) {
                KiwiModule.RenderLayer.Layer renderType = (KiwiModule.RenderLayer.Layer) properties.renderType().orElse(null);
                if (renderType == null) {
                    renderType = (KiwiModule.RenderLayer.Layer) properties.glassType().map(GlassType::renderType).orElse(null);
                }
                if (renderType != null) {
                    Block block = BuiltInRegistries.BLOCK.get((ResourceLocation) entryx.getKey());
                    ItemBlockRenderTypes.setRenderLayer(block, (RenderType) renderType.value);
                }
            }
            if (!properties.colorProvider().isEmpty()) {
                Block block = BuiltInRegistries.BLOCK.get((ResourceLocation) entryx.getKey());
                Block providerBlock = BuiltInRegistries.BLOCK.get((ResourceLocation) properties.colorProvider().get());
                if (providerBlock == Blocks.AIR) {
                    Kiwi.LOGGER.warn("Cannot find color provider block %s for block %s".formatted(properties.colorProvider().get(), entryx.getKey()));
                } else {
                    blocksToAdd.add(Pair.of(block, (BlockColor) blockColors.computeIfAbsent(providerBlock, ColorProviderUtil::delegate)));
                }
                Item item = block.asItem();
                if (item != Items.AIR && !addedItems.contains(item)) {
                    addedItems.add(item);
                    Item providerItem = providerBlock.asItem();
                    if (providerItem != Items.AIR) {
                        itemsToAdd.add(Pair.of(item, (ItemColor) itemColors.computeIfAbsent(providerItem, ColorProviderUtil::delegate)));
                    } else if (providerBlock == Blocks.WATER) {
                        itemsToAdd.add(Pair.of(item, (ItemColor) (stack, i) -> 4159204));
                    } else {
                        itemsToAdd.add(Pair.of(item, (ItemColor) itemColors.computeIfAbsent(providerItem, $ -> ColorProviderUtil.delegateItemFallback(providerBlock))));
                    }
                }
            }
        }
        ClientProxy.registerColors(context, blocksToAdd, itemsToAdd);
    }
}
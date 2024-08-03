package fr.frinn.custommachinery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.RegisterGuiElementWidgetSupplierEvent;
import fr.frinn.custommachinery.api.integration.jei.RegisterGuiElementJEIRendererEvent;
import fr.frinn.custommachinery.api.integration.jei.RegisterWidgetToJeiIngredientGetterEvent;
import fr.frinn.custommachinery.api.machine.MachineAppearanceProperty;
import fr.frinn.custommachinery.client.element.BarGuiElementWidget;
import fr.frinn.custommachinery.client.element.ButtonGuiElementWidget;
import fr.frinn.custommachinery.client.element.ConfigGuiElementWidget;
import fr.frinn.custommachinery.client.element.DumpGuiElementWidget;
import fr.frinn.custommachinery.client.element.EmptyGuiElementWidget;
import fr.frinn.custommachinery.client.element.EnergyGuiElementWidget;
import fr.frinn.custommachinery.client.element.ExperienceGuiElementWidget;
import fr.frinn.custommachinery.client.element.FluidGuiElementWidget;
import fr.frinn.custommachinery.client.element.FuelGuiElementWidget;
import fr.frinn.custommachinery.client.element.PlayerInventoryGuiElementWidget;
import fr.frinn.custommachinery.client.element.ProgressGuiElementWidget;
import fr.frinn.custommachinery.client.element.ResetGuiElementWidget;
import fr.frinn.custommachinery.client.element.SlotGuiElementWidget;
import fr.frinn.custommachinery.client.element.StatusGuiElementWidget;
import fr.frinn.custommachinery.client.element.TextGuiElementWidget;
import fr.frinn.custommachinery.client.element.TextureGuiElementWidget;
import fr.frinn.custommachinery.client.integration.jei.FluidIngredientGetter;
import fr.frinn.custommachinery.client.integration.jei.element.EnergyGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.integration.jei.element.ExperienceGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.integration.jei.element.FluidGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.integration.jei.element.FuelGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.integration.jei.element.ProgressGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.integration.jei.element.SlotGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.integration.jei.element.TextGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.integration.jei.element.TextureGuiElementJeiRenderer;
import fr.frinn.custommachinery.client.render.CustomMachineRenderer;
import fr.frinn.custommachinery.client.screen.CustomMachineScreen;
import fr.frinn.custommachinery.client.screen.creation.appearance.AppearancePropertyBuilderRegistry;
import fr.frinn.custommachinery.client.screen.creation.appearance.RegisterAppearancePropertyBuilderEvent;
import fr.frinn.custommachinery.client.screen.creation.appearance.builder.ModelAppearancePropertyBuilder;
import fr.frinn.custommachinery.client.screen.creation.appearance.builder.NumberAppearancePropertyBuilder;
import fr.frinn.custommachinery.client.screen.creation.appearance.builder.TextAppearancePropertyBuilder;
import fr.frinn.custommachinery.client.screen.creation.component.MachineComponentBuilderRegistry;
import fr.frinn.custommachinery.client.screen.creation.component.RegisterComponentBuilderEvent;
import fr.frinn.custommachinery.client.screen.creation.component.builder.ChunkloadComponentBuilder;
import fr.frinn.custommachinery.client.screen.creation.component.builder.EnergyComponentBuilder;
import fr.frinn.custommachinery.client.screen.creation.component.builder.ExperienceComponentBuilder;
import fr.frinn.custommachinery.client.screen.creation.component.builder.FluidComponentBuilder;
import fr.frinn.custommachinery.client.screen.creation.component.builder.ItemComponentBuilder;
import fr.frinn.custommachinery.client.screen.creation.component.builder.RedstoneComponentBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderRegistry;
import fr.frinn.custommachinery.client.screen.creation.gui.RegisterGuiElementBuilderEvent;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.BarGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.ButtonGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.ConfigGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.DumpGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.EnergyGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.ExperienceGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.FluidGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.FuelGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.PlayerInventoryGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.ProgressBarGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.ResetGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.SlotGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.StatusGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.TextGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.builder.TextureGuiElementBuilder;
import fr.frinn.custommachinery.common.component.ChunkloadMachineComponent;
import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.component.ExperienceMachineComponent;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.component.RedstoneMachineComponent;
import fr.frinn.custommachinery.common.guielement.BarGuiElement;
import fr.frinn.custommachinery.common.guielement.ButtonGuiElement;
import fr.frinn.custommachinery.common.guielement.ConfigGuiElement;
import fr.frinn.custommachinery.common.guielement.DumpGuiElement;
import fr.frinn.custommachinery.common.guielement.EmptyGuiElement;
import fr.frinn.custommachinery.common.guielement.EnergyGuiElement;
import fr.frinn.custommachinery.common.guielement.ExperienceGuiElement;
import fr.frinn.custommachinery.common.guielement.FluidGuiElement;
import fr.frinn.custommachinery.common.guielement.FuelGuiElement;
import fr.frinn.custommachinery.common.guielement.PlayerInventoryGuiElement;
import fr.frinn.custommachinery.common.guielement.ProgressBarGuiElement;
import fr.frinn.custommachinery.common.guielement.ResetGuiElement;
import fr.frinn.custommachinery.common.guielement.SlotGuiElement;
import fr.frinn.custommachinery.common.guielement.StatusGuiElement;
import fr.frinn.custommachinery.common.guielement.TextGuiElement;
import fr.frinn.custommachinery.common.guielement.TextureGuiElement;
import fr.frinn.custommachinery.common.init.CustomMachineBlock;
import fr.frinn.custommachinery.common.init.CustomMachineContainer;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.upgrade.RecipeModifier;
import fr.frinn.custommachinery.common.util.MachineModelLocation;
import fr.frinn.custommachinery.impl.guielement.GuiElementWidgetSupplierRegistry;
import fr.frinn.custommachinery.impl.integration.jei.GuiElementJEIRendererRegistry;
import fr.frinn.custommachinery.impl.integration.jei.WidgetToJeiIngredientRegistry;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ClientHandler {

    public static void init() {
        ClientTooltipEvent.ITEM.register(ClientHandler::onItemTooltip);
        LifecycleEvent.SETUP.register(ClientHandler::clientSetup);
        RegisterGuiElementWidgetSupplierEvent.EVENT.register(ClientHandler::registerGuiElementWidgets);
        RegisterAppearancePropertyBuilderEvent.EVENT.register(ClientHandler::registerAppearancePropertyBuilders);
        RegisterComponentBuilderEvent.EVENT.register(ClientHandler::registerMachineComponentBuilders);
        RegisterGuiElementBuilderEvent.EVENT.register(ClientHandler::registerGuiElementBuilders);
        RegisterGuiElementJEIRendererEvent.EVENT.register(ClientHandler::registerGuiElementJEIRenderers);
        RegisterWidgetToJeiIngredientGetterEvent.EVENT.register(ClientHandler::registerWidgetToJeiIngredientGetters);
    }

    private static void onItemTooltip(ItemStack stack, List<Component> lines, TooltipFlag flag) {
        CustomMachinery.UPGRADES.getUpgradesForItem(stack.getItem()).forEach(upgrade -> {
            lines.addAll(upgrade.getTooltips());
            if (Screen.hasControlDown() || Screen.hasShiftDown()) {
                upgrade.getModifiers().stream().map(RecipeModifier::getTooltip).forEach(lines::add);
            }
        });
    }

    private static void clientSetup() {
        RenderTypeRegistry.register(getRenderType(((CustomMachineBlock) Registration.CUSTOM_MACHINE_BLOCK.get()).renderType), (Block) Registration.CUSTOM_MACHINE_BLOCK.get());
        CustomMachinery.CUSTOM_BLOCK_MACHINES.values().forEach(block -> RenderTypeRegistry.register(getRenderType(block.renderType), block));
        MenuRegistry.registerScreenFactory((MenuType<? extends CustomMachineContainer>) Registration.CUSTOM_MACHINE_CONTAINER.get(), CustomMachineScreen::new);
        GuiElementWidgetSupplierRegistry.init();
        AppearancePropertyBuilderRegistry.init();
        MachineComponentBuilderRegistry.init();
        GuiElementBuilderRegistry.init();
        if (Platform.isModLoaded("jei")) {
            GuiElementJEIRendererRegistry.init();
            WidgetToJeiIngredientRegistry.init();
        }
        BlockEntityRendererRegistry.register((BlockEntityType) Registration.CUSTOM_MACHINE_TILE.get(), CustomMachineRenderer::new);
        ColorHandlerRegistry.registerBlockColors(ClientHandler::blockColor, (Block) Registration.CUSTOM_MACHINE_BLOCK.get());
        CustomMachinery.CUSTOM_BLOCK_MACHINES.values().forEach(block -> ColorHandlerRegistry.registerBlockColors(ClientHandler::blockColor, block));
        ColorHandlerRegistry.registerItemColors(ClientHandler::itemColor, Registration.CUSTOM_MACHINE_ITEM::get);
        CustomMachinery.CUSTOM_BLOCK_MACHINES.values().forEach(block -> ColorHandlerRegistry.registerItemColors(ClientHandler::itemColor, block));
    }

    private static void registerGuiElementWidgets(RegisterGuiElementWidgetSupplierEvent event) {
        event.register((GuiElementType<BarGuiElement>) Registration.BAR_GUI_ELEMENT.get(), BarGuiElementWidget::new);
        event.register((GuiElementType<ButtonGuiElement>) Registration.BUTTON_GUI_ELEMENT.get(), ButtonGuiElementWidget::new);
        event.register((GuiElementType<ConfigGuiElement>) Registration.CONFIG_GUI_ELEMENT.get(), ConfigGuiElementWidget::new);
        event.register((GuiElementType<DumpGuiElement>) Registration.DUMP_GUI_ELEMENT.get(), DumpGuiElementWidget::new);
        event.register((GuiElementType<EmptyGuiElement>) Registration.EMPTY_GUI_ELEMENT.get(), EmptyGuiElementWidget::new);
        event.register((GuiElementType<EnergyGuiElement>) Registration.ENERGY_GUI_ELEMENT.get(), EnergyGuiElementWidget::new);
        event.register((GuiElementType<ExperienceGuiElement>) Registration.EXPERIENCE_GUI_ELEMENT.get(), ExperienceGuiElementWidget::new);
        event.register((GuiElementType<FluidGuiElement>) Registration.FLUID_GUI_ELEMENT.get(), FluidGuiElementWidget::new);
        event.register((GuiElementType<FuelGuiElement>) Registration.FUEL_GUI_ELEMENT.get(), FuelGuiElementWidget::new);
        event.register((GuiElementType<PlayerInventoryGuiElement>) Registration.PLAYER_INVENTORY_GUI_ELEMENT.get(), PlayerInventoryGuiElementWidget::new);
        event.register((GuiElementType<ProgressBarGuiElement>) Registration.PROGRESS_GUI_ELEMENT.get(), ProgressGuiElementWidget::new);
        event.register((GuiElementType<ResetGuiElement>) Registration.RESET_GUI_ELEMENT.get(), ResetGuiElementWidget::new);
        event.register((GuiElementType<SlotGuiElement>) Registration.SLOT_GUI_ELEMENT.get(), SlotGuiElementWidget::new);
        event.register((GuiElementType<StatusGuiElement>) Registration.STATUS_GUI_ELEMENT.get(), StatusGuiElementWidget::new);
        event.register((GuiElementType<TextGuiElement>) Registration.TEXT_GUI_ELEMENT.get(), TextGuiElementWidget::new);
        event.register((GuiElementType<TextureGuiElement>) Registration.TEXTURE_GUI_ELEMENT.get(), TextureGuiElementWidget::new);
    }

    private static void registerGuiElementJEIRenderers(RegisterGuiElementJEIRendererEvent event) {
        event.register((GuiElementType<EnergyGuiElement>) Registration.ENERGY_GUI_ELEMENT.get(), new EnergyGuiElementJeiRenderer());
        event.register((GuiElementType<ExperienceGuiElement>) Registration.EXPERIENCE_GUI_ELEMENT.get(), new ExperienceGuiElementJeiRenderer());
        event.register((GuiElementType<FluidGuiElement>) Registration.FLUID_GUI_ELEMENT.get(), new FluidGuiElementJeiRenderer());
        event.register((GuiElementType<FuelGuiElement>) Registration.FUEL_GUI_ELEMENT.get(), new FuelGuiElementJeiRenderer());
        event.register((GuiElementType<ProgressBarGuiElement>) Registration.PROGRESS_GUI_ELEMENT.get(), new ProgressGuiElementJeiRenderer());
        event.register((GuiElementType<SlotGuiElement>) Registration.SLOT_GUI_ELEMENT.get(), new SlotGuiElementJeiRenderer());
        event.register((GuiElementType<TextGuiElement>) Registration.TEXT_GUI_ELEMENT.get(), new TextGuiElementJeiRenderer());
        event.register((GuiElementType<TextureGuiElement>) Registration.TEXTURE_GUI_ELEMENT.get(), new TextureGuiElementJeiRenderer());
    }

    private static void registerWidgetToJeiIngredientGetters(RegisterWidgetToJeiIngredientGetterEvent event) {
        event.register((GuiElementType<FluidGuiElement>) Registration.FLUID_GUI_ELEMENT.get(), new FluidIngredientGetter());
    }

    private static void registerAppearancePropertyBuilders(RegisterAppearancePropertyBuilderEvent event) {
        event.register((MachineAppearanceProperty<MachineModelLocation>) Registration.BLOCK_MODEL_PROPERTY.get(), new ModelAppearancePropertyBuilder(Component.literal("Block"), (MachineAppearanceProperty<MachineModelLocation>) Registration.BLOCK_MODEL_PROPERTY.get()));
        event.register((MachineAppearanceProperty<MachineModelLocation>) Registration.ITEM_MODEL_PROPERTY.get(), new ModelAppearancePropertyBuilder(Component.literal("Item"), (MachineAppearanceProperty<MachineModelLocation>) Registration.ITEM_MODEL_PROPERTY.get()));
        event.register((MachineAppearanceProperty) Registration.LIGHT_PROPERTY.get(), new NumberAppearancePropertyBuilder(Component.literal("Light"), (MachineAppearanceProperty) Registration.LIGHT_PROPERTY.get(), 0, 15));
        event.register((MachineAppearanceProperty) Registration.COLOR_PROPERTY.get(), new TextAppearancePropertyBuilder(Component.literal("Color"), (MachineAppearanceProperty) Registration.COLOR_PROPERTY.get(), Integer::valueOf, Object::toString));
        event.register((MachineAppearanceProperty) Registration.HARDNESS_PROPERTY.get(), new NumberAppearancePropertyBuilder(Component.literal("Hardness"), (MachineAppearanceProperty) Registration.HARDNESS_PROPERTY.get(), -1.0F, 100.0F));
        event.register((MachineAppearanceProperty) Registration.RESISTANCE_PROPERTY.get(), new NumberAppearancePropertyBuilder(Component.literal("Resistance"), (MachineAppearanceProperty) Registration.RESISTANCE_PROPERTY.get(), 0.0F, 2000.0F));
    }

    private static void registerMachineComponentBuilders(RegisterComponentBuilderEvent event) {
        event.register((MachineComponentType<ChunkloadMachineComponent>) Registration.CHUNKLOAD_MACHINE_COMPONENT.get(), new ChunkloadComponentBuilder());
        event.register((MachineComponentType<EnergyMachineComponent>) Registration.ENERGY_MACHINE_COMPONENT.get(), new EnergyComponentBuilder());
        event.register((MachineComponentType<ExperienceMachineComponent>) Registration.EXPERIENCE_MACHINE_COMPONENT.get(), new ExperienceComponentBuilder());
        event.register((MachineComponentType<FluidMachineComponent>) Registration.FLUID_MACHINE_COMPONENT.get(), new FluidComponentBuilder());
        event.register((MachineComponentType<ItemMachineComponent>) Registration.ITEM_MACHINE_COMPONENT.get(), new ItemComponentBuilder());
        event.register((MachineComponentType<RedstoneMachineComponent>) Registration.REDSTONE_MACHINE_COMPONENT.get(), new RedstoneComponentBuilder());
    }

    private static void registerGuiElementBuilders(RegisterGuiElementBuilderEvent event) {
        event.register((GuiElementType<BarGuiElement>) Registration.BAR_GUI_ELEMENT.get(), new BarGuiElementBuilder());
        event.register((GuiElementType<ButtonGuiElement>) Registration.BUTTON_GUI_ELEMENT.get(), new ButtonGuiElementBuilder());
        event.register((GuiElementType<ConfigGuiElement>) Registration.CONFIG_GUI_ELEMENT.get(), new ConfigGuiElementBuilder());
        event.register((GuiElementType<DumpGuiElement>) Registration.DUMP_GUI_ELEMENT.get(), new DumpGuiElementBuilder());
        event.register((GuiElementType<EnergyGuiElement>) Registration.ENERGY_GUI_ELEMENT.get(), new EnergyGuiElementBuilder());
        event.register((GuiElementType<ExperienceGuiElement>) Registration.EXPERIENCE_GUI_ELEMENT.get(), new ExperienceGuiElementBuilder());
        event.register((GuiElementType<FluidGuiElement>) Registration.FLUID_GUI_ELEMENT.get(), new FluidGuiElementBuilder());
        event.register((GuiElementType<FuelGuiElement>) Registration.FUEL_GUI_ELEMENT.get(), new FuelGuiElementBuilder());
        event.register((GuiElementType<PlayerInventoryGuiElement>) Registration.PLAYER_INVENTORY_GUI_ELEMENT.get(), new PlayerInventoryGuiElementBuilder());
        event.register((GuiElementType<ProgressBarGuiElement>) Registration.PROGRESS_GUI_ELEMENT.get(), new ProgressBarGuiElementBuilder());
        event.register((GuiElementType<ResetGuiElement>) Registration.RESET_GUI_ELEMENT.get(), new ResetGuiElementBuilder());
        event.register((GuiElementType<SlotGuiElement>) Registration.SLOT_GUI_ELEMENT.get(), new SlotGuiElementBuilder());
        event.register((GuiElementType<StatusGuiElement>) Registration.STATUS_GUI_ELEMENT.get(), new StatusGuiElementBuilder());
        event.register((GuiElementType<TextGuiElement>) Registration.TEXT_GUI_ELEMENT.get(), new TextGuiElementBuilder());
        event.register((GuiElementType<TextureGuiElement>) Registration.TEXTURE_GUI_ELEMENT.get(), new TextureGuiElementBuilder());
    }

    private static int blockColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        if (level != null && pos != null) {
            switch(tintIndex) {
                case 1:
                    return level.getBlockTint(pos, BiomeColors.WATER_COLOR_RESOLVER);
                case 2:
                    return level.getBlockTint(pos, BiomeColors.GRASS_COLOR_RESOLVER);
                case 3:
                    return level.getBlockTint(pos, BiomeColors.FOLIAGE_COLOR_RESOLVER);
                case 4:
                    if (level.m_7702_(pos) instanceof CustomMachineTile machineTile) {
                        return machineTile.getAppearance().getColor();
                    }
                default:
                    return 16777215;
            }
        } else {
            return 0;
        }
    }

    private static int itemColor(ItemStack stack, int tintIndex) {
        BlockState state = ((CustomMachineBlock) Registration.CUSTOM_MACHINE_BLOCK.get()).m_49966_();
        Level level = Minecraft.getInstance().level;
        if (Minecraft.getInstance().player == null) {
            return 0;
        } else {
            BlockPos pos = Minecraft.getInstance().player.m_20183_();
            return Minecraft.getInstance().getBlockColors().getColor(state, level, pos, tintIndex);
        }
    }

    @NotNull
    public static CustomMachineTile getClientSideCustomMachineTile(BlockPos pos) {
        if (Minecraft.getInstance().level != null) {
            BlockEntity tile = Minecraft.getInstance().level.m_7702_(pos);
            if (tile instanceof CustomMachineTile) {
                return (CustomMachineTile) tile;
            }
        }
        throw new IllegalStateException("Trying to open a Custom Machine container without clicking on a Custom Machine block");
    }

    public static void renderSlotHighlight(GuiGraphics graphics, int x, int y, int width, int height) {
        RenderSystem.disableDepthTest();
        RenderSystem.colorMask(true, true, true, false);
        graphics.fill(x, y, x + width, y + height, -2130706433);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableDepthTest();
    }

    public static boolean isShiftKeyDown() {
        return Screen.hasShiftDown();
    }

    public static RenderType getRenderType(String renderType) {
        return switch(renderType) {
            case "solid" ->
                RenderType.solid();
            case "cutout" ->
                RenderType.cutout();
            case "translucent" ->
                RenderType.translucent();
            default ->
                throw new IllegalArgumentException("Invalid render type: " + renderType);
        };
    }

    public static int getLineHeight() {
        return 9;
    }

    public static int textWidth(Component text) {
        return Minecraft.getInstance().font.width(text);
    }
}
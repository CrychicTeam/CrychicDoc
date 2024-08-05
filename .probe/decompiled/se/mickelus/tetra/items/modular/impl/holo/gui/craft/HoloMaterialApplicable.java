package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.ClientScheduler;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.HoloGui;
import se.mickelus.tetra.module.schematic.SchematicRarity;
import se.mickelus.tetra.module.schematic.SchematicType;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloMaterialApplicable extends GuiElement {

    private final List<Component> emptyTooltip;

    private final GuiTexture icon = new GuiTexture(0, 0, 9, 9, 215, 0, GuiTextures.workbench);

    private List<Component> tooltip;

    private IModularItem item;

    private ItemStack itemStack;

    private String slot;

    private UpgradeSchematic schematic;

    public HoloMaterialApplicable(int x, int y) {
        super(x, y, 9, 9);
        this.addChild(this.icon);
        this.emptyTooltip = Collections.singletonList(Component.translatable("tetra.holo.craft.empty_applicable_materials"));
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }

    public void update(Level level, BlockPos pos, WorkbenchTile blockEntity, ItemStack itemStack, String slot, UpgradeSchematic schematic, Player playerEntity) {
        this.item = null;
        this.itemStack = null;
        this.slot = null;
        this.schematic = null;
        this.tooltip = new ArrayList();
        String[] materials = schematic.getApplicableMaterials();
        if (materials != null && materials.length > 0) {
            String materialsString = (String) Arrays.stream(materials).map(mat -> {
                if (mat.startsWith("#")) {
                    return I18n.get("tetra.variant_category." + mat.substring(1) + ".label");
                } else {
                    return mat.startsWith("!") ? I18n.get("tetra.material." + mat.substring(1)) : (String) Optional.ofNullable(ForgeRegistries.ITEMS.getValue(new ResourceLocation(mat))).map(Item::m_41466_).map(Component::getString).orElse(mat);
                }
            }).collect(Collectors.joining(", "));
            this.tooltip.add(Component.translatable("tetra.holo.craft.applicable_materials"));
            this.tooltip.add(Component.literal(materialsString).withStyle(ChatFormatting.GRAY));
            this.tooltip.add(Component.literal(""));
            ItemStack holosphereStack = ModularHolosphereItem.findHolosphere(playerEntity, level, pos);
            if ((schematic.getType() == SchematicType.major || schematic.getType() == SchematicType.minor) && schematic.getRarity() == SchematicRarity.basic) {
                if (!holosphereStack.isEmpty() && itemStack.getItem() instanceof IModularItem) {
                    this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut"));
                    this.item = (IModularItem) itemStack.getItem();
                    this.itemStack = itemStack;
                    this.slot = slot;
                    this.schematic = schematic;
                } else {
                    this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_disabled"));
                    this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_missing").withStyle(ChatFormatting.DARK_GRAY));
                }
            } else {
                this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_disabled"));
                this.tooltip.add(Component.translatable("tetra.holo.craft.holosphere_shortcut_unavailable").withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            this.tooltip = this.emptyTooltip;
        }
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        if (this.hasFocus() && this.item != null) {
            Screen currentScreen = Minecraft.getInstance().screen;
            HoloGui gui = HoloGui.getInstance();
            Minecraft.getInstance().setScreen(gui);
            gui.openSchematic(this.item, this.itemStack, this.slot, this.schematic, () -> ClientScheduler.schedule(0, () -> Minecraft.getInstance().setScreen(currentScreen)));
            return true;
        } else {
            return false;
        }
    }
}
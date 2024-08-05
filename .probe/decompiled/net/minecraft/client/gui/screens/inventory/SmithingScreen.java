package net.minecraft.client.gui.screens.inventory;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import org.joml.Quaternionf;

public class SmithingScreen extends ItemCombinerScreen<SmithingMenu> {

    private static final ResourceLocation SMITHING_LOCATION = new ResourceLocation("textures/gui/container/smithing.png");

    private static final ResourceLocation EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM = new ResourceLocation("item/empty_slot_smithing_template_armor_trim");

    private static final ResourceLocation EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE = new ResourceLocation("item/empty_slot_smithing_template_netherite_upgrade");

    private static final Component MISSING_TEMPLATE_TOOLTIP = Component.translatable("container.upgrade.missing_template_tooltip");

    private static final Component ERROR_TOOLTIP = Component.translatable("container.upgrade.error_tooltip");

    private static final List<ResourceLocation> EMPTY_SLOT_SMITHING_TEMPLATES = List.of(EMPTY_SLOT_SMITHING_TEMPLATE_ARMOR_TRIM, EMPTY_SLOT_SMITHING_TEMPLATE_NETHERITE_UPGRADE);

    private static final int TITLE_LABEL_X = 44;

    private static final int TITLE_LABEL_Y = 15;

    private static final int ERROR_ICON_WIDTH = 28;

    private static final int ERROR_ICON_HEIGHT = 21;

    private static final int ERROR_ICON_X = 65;

    private static final int ERROR_ICON_Y = 46;

    private static final int TOOLTIP_WIDTH = 115;

    public static final int ARMOR_STAND_Y_ROT = 210;

    public static final int ARMOR_STAND_X_ROT = 25;

    public static final Quaternionf ARMOR_STAND_ANGLE = new Quaternionf().rotationXYZ(0.43633232F, 0.0F, (float) Math.PI);

    public static final int ARMOR_STAND_SCALE = 25;

    public static final int ARMOR_STAND_OFFSET_Y = 75;

    public static final int ARMOR_STAND_OFFSET_X = 141;

    private final CyclingSlotBackground templateIcon = new CyclingSlotBackground(0);

    private final CyclingSlotBackground baseIcon = new CyclingSlotBackground(1);

    private final CyclingSlotBackground additionalIcon = new CyclingSlotBackground(2);

    @Nullable
    private ArmorStand armorStandPreview;

    public SmithingScreen(SmithingMenu smithingMenu0, Inventory inventory1, Component component2) {
        super(smithingMenu0, inventory1, component2, SMITHING_LOCATION);
        this.f_97728_ = 44;
        this.f_97729_ = 15;
    }

    @Override
    protected void subInit() {
        this.armorStandPreview = new ArmorStand(this.f_96541_.level, 0.0, 0.0, 0.0);
        this.armorStandPreview.setNoBasePlate(true);
        this.armorStandPreview.setShowArms(true);
        this.armorStandPreview.f_20883_ = 210.0F;
        this.armorStandPreview.m_146926_(25.0F);
        this.armorStandPreview.f_20885_ = this.armorStandPreview.m_146908_();
        this.armorStandPreview.f_20886_ = this.armorStandPreview.m_146908_();
        this.updateArmorStandPreview(((SmithingMenu) this.f_97732_).m_38853_(3).getItem());
    }

    @Override
    public void containerTick() {
        super.m_181908_();
        Optional<SmithingTemplateItem> $$0 = this.getTemplateItem();
        this.templateIcon.tick(EMPTY_SLOT_SMITHING_TEMPLATES);
        this.baseIcon.tick((List<ResourceLocation>) $$0.map(SmithingTemplateItem::m_266534_).orElse(List.of()));
        this.additionalIcon.tick((List<ResourceLocation>) $$0.map(SmithingTemplateItem::m_266326_).orElse(List.of()));
    }

    private Optional<SmithingTemplateItem> getTemplateItem() {
        ItemStack $$0 = ((SmithingMenu) this.f_97732_).m_38853_(0).getItem();
        return !$$0.isEmpty() && $$0.getItem() instanceof SmithingTemplateItem $$1 ? Optional.of($$1) : Optional.empty();
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        super.render(guiGraphics0, int1, int2, float3);
        this.renderOnboardingTooltips(guiGraphics0, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        super.renderBg(guiGraphics0, float1, int2, int3);
        this.templateIcon.render(this.f_97732_, guiGraphics0, float1, this.f_97735_, this.f_97736_);
        this.baseIcon.render(this.f_97732_, guiGraphics0, float1, this.f_97735_, this.f_97736_);
        this.additionalIcon.render(this.f_97732_, guiGraphics0, float1, this.f_97735_, this.f_97736_);
        InventoryScreen.renderEntityInInventory(guiGraphics0, this.f_97735_ + 141, this.f_97736_ + 75, 25, ARMOR_STAND_ANGLE, null, this.armorStandPreview);
    }

    @Override
    public void slotChanged(AbstractContainerMenu abstractContainerMenu0, int int1, ItemStack itemStack2) {
        if (int1 == 3) {
            this.updateArmorStandPreview(itemStack2);
        }
    }

    private void updateArmorStandPreview(ItemStack itemStack0) {
        if (this.armorStandPreview != null) {
            for (EquipmentSlot $$1 : EquipmentSlot.values()) {
                this.armorStandPreview.setItemSlot($$1, ItemStack.EMPTY);
            }
            if (!itemStack0.isEmpty()) {
                ItemStack $$2 = itemStack0.copy();
                if (itemStack0.getItem() instanceof ArmorItem $$3) {
                    this.armorStandPreview.setItemSlot($$3.getEquipmentSlot(), $$2);
                } else {
                    this.armorStandPreview.setItemSlot(EquipmentSlot.OFFHAND, $$2);
                }
            }
        }
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics0, int int1, int int2) {
        if (this.hasRecipeError()) {
            guiGraphics0.blit(SMITHING_LOCATION, int1 + 65, int2 + 46, this.f_97726_, 0, 28, 21);
        }
    }

    private void renderOnboardingTooltips(GuiGraphics guiGraphics0, int int1, int int2) {
        Optional<Component> $$3 = Optional.empty();
        if (this.hasRecipeError() && this.m_6774_(65, 46, 28, 21, (double) int1, (double) int2)) {
            $$3 = Optional.of(ERROR_TOOLTIP);
        }
        if (this.f_97734_ != null) {
            ItemStack $$4 = ((SmithingMenu) this.f_97732_).m_38853_(0).getItem();
            ItemStack $$5 = this.f_97734_.getItem();
            if ($$4.isEmpty()) {
                if (this.f_97734_.index == 0) {
                    $$3 = Optional.of(MISSING_TEMPLATE_TOOLTIP);
                }
            } else if ($$4.getItem() instanceof SmithingTemplateItem $$6 && $$5.isEmpty()) {
                if (this.f_97734_.index == 1) {
                    $$3 = Optional.of($$6.getBaseSlotDescription());
                } else if (this.f_97734_.index == 2) {
                    $$3 = Optional.of($$6.getAdditionSlotDescription());
                }
            }
        }
        $$3.ifPresent(p_280863_ -> guiGraphics0.renderTooltip(this.f_96547_, this.f_96547_.split(p_280863_, 115), int1, int2));
    }

    private boolean hasRecipeError() {
        return ((SmithingMenu) this.f_97732_).m_38853_(0).hasItem() && ((SmithingMenu) this.f_97732_).m_38853_(1).hasItem() && ((SmithingMenu) this.f_97732_).m_38853_(2).hasItem() && !((SmithingMenu) this.f_97732_).m_38853_(((SmithingMenu) this.f_97732_).m_266562_()).hasItem();
    }
}
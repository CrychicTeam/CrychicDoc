package net.mehvahdjukaar.supplementaries.integration.forge;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SafeBlockTile;
import net.mehvahdjukaar.supplementaries.common.inventories.SackContainerMenu;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.InventoryTooltip;
import net.mehvahdjukaar.supplementaries.common.utils.ItemsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.client.module.ChestSearchingModule;
import org.violetmoon.quark.content.client.tooltip.ShulkerBoxTooltips;

public class InventoryTooltipComponent implements ClientTooltipComponent {

    private static final int CORNER = 5;

    private static final int BUFFER = 1;

    private static final int EDGE = 18;

    private final CompoundTag tag;

    private final Item item;

    private final int[] dimensions;

    private final int size;

    private final boolean locked;

    protected ChestSearchingModule module = Quark.ZETA.modules.get(ChestSearchingModule.class);

    public InventoryTooltipComponent(InventoryTooltip tooltip) {
        this.tag = tooltip.tag();
        this.item = tooltip.item();
        this.dimensions = SackContainerMenu.getRatio(tooltip.size());
        this.size = tooltip.size();
        if (ItemsUtil.loadBlockEntityFromItem(this.tag, this.item) instanceof SafeBlockTile safe) {
            this.locked = !safe.canPlayerOpen(Minecraft.getInstance().player, false);
        } else {
            this.locked = false;
        }
    }

    @Override
    public void renderImage(Font font, int tooltipX, int tooltipY, GuiGraphics graphics) {
        if (!this.locked) {
            BlockEntity te = ItemsUtil.loadBlockEntityFromItem(this.tag, this.item);
            if (te != null) {
                if (te instanceof SafeBlockTile safe && !safe.canPlayerOpen(Minecraft.getInstance().player, false)) {
                    return;
                }
                LazyOptional<IItemHandler> handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
                handler.ifPresent(capability -> {
                    Minecraft mc = Minecraft.getInstance();
                    int currentX = tooltipX;
                    int texWidth = 10 + 18 * this.dimensions[0];
                    int right = tooltipX + texWidth;
                    Window window = mc.getWindow();
                    if (right > window.getGuiScaledWidth()) {
                        currentX = tooltipX - (right - window.getGuiScaledWidth());
                    }
                    PoseStack pose = graphics.pose();
                    pose.pushPose();
                    pose.translate(0.0, 0.0, 700.0);
                    int color = -1;
                    ShulkerBoxTooltips.ShulkerComponent.renderTooltipBackground(graphics, mc, pose, currentX, tooltipY, this.dimensions[0], this.dimensions[1], color);
                    for (int i = 0; i < this.size; i++) {
                        ItemStack itemstack = capability.getStackInSlot(i);
                        int xp = currentX + 6 + i % this.dimensions[0] * 18;
                        int yp = tooltipY + 6 + i / this.dimensions[0] * 18;
                        if (!itemstack.isEmpty()) {
                            graphics.renderFakeItem(itemstack, xp, yp);
                            graphics.renderItemDecorations(mc.font, itemstack, xp, yp);
                        }
                        if (!this.module.namesMatch(itemstack)) {
                            RenderSystem.disableDepthTest();
                            graphics.fill(xp, yp, xp + 16, yp + 16, -1442840576);
                        }
                    }
                    pose.popPose();
                });
            }
        }
    }

    @Override
    public int getHeight() {
        return this.locked ? 0 : 10 + 18 * this.dimensions[1] + 1;
    }

    @Override
    public int getWidth(Font font) {
        return this.locked ? 0 : 10 + 18 * this.dimensions[0];
    }
}
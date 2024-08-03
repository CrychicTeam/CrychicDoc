package net.minecraft.client.gui.screens.inventory;

import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class CartographyTableScreen extends AbstractContainerScreen<CartographyTableMenu> {

    private static final ResourceLocation BG_LOCATION = new ResourceLocation("textures/gui/container/cartography_table.png");

    public CartographyTableScreen(CartographyTableMenu cartographyTableMenu0, Inventory inventory1, Component component2) {
        super(cartographyTableMenu0, inventory1, component2);
        this.f_97729_ -= 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        this.m_280273_(guiGraphics0);
        int $$4 = this.f_97735_;
        int $$5 = this.f_97736_;
        guiGraphics0.blit(BG_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        ItemStack $$6 = ((CartographyTableMenu) this.f_97732_).m_38853_(1).getItem();
        boolean $$7 = $$6.is(Items.MAP);
        boolean $$8 = $$6.is(Items.PAPER);
        boolean $$9 = $$6.is(Items.GLASS_PANE);
        ItemStack $$10 = ((CartographyTableMenu) this.f_97732_).m_38853_(0).getItem();
        boolean $$11 = false;
        Integer $$12;
        MapItemSavedData $$13;
        if ($$10.is(Items.FILLED_MAP)) {
            $$12 = MapItem.getMapId($$10);
            $$13 = MapItem.getSavedData($$12, this.f_96541_.level);
            if ($$13 != null) {
                if ($$13.locked) {
                    $$11 = true;
                    if ($$8 || $$9) {
                        guiGraphics0.blit(BG_LOCATION, $$4 + 35, $$5 + 31, this.f_97726_ + 50, 132, 28, 21);
                    }
                }
                if ($$8 && $$13.scale >= 4) {
                    $$11 = true;
                    guiGraphics0.blit(BG_LOCATION, $$4 + 35, $$5 + 31, this.f_97726_ + 50, 132, 28, 21);
                }
            }
        } else {
            $$12 = null;
            $$13 = null;
        }
        this.renderResultingMap(guiGraphics0, $$12, $$13, $$7, $$8, $$9, $$11);
    }

    private void renderResultingMap(GuiGraphics guiGraphics0, @Nullable Integer integer1, @Nullable MapItemSavedData mapItemSavedData2, boolean boolean3, boolean boolean4, boolean boolean5, boolean boolean6) {
        int $$7 = this.f_97735_;
        int $$8 = this.f_97736_;
        if (boolean4 && !boolean6) {
            guiGraphics0.blit(BG_LOCATION, $$7 + 67, $$8 + 13, this.f_97726_, 66, 66, 66);
            this.renderMap(guiGraphics0, integer1, mapItemSavedData2, $$7 + 85, $$8 + 31, 0.226F);
        } else if (boolean3) {
            guiGraphics0.blit(BG_LOCATION, $$7 + 67 + 16, $$8 + 13, this.f_97726_, 132, 50, 66);
            this.renderMap(guiGraphics0, integer1, mapItemSavedData2, $$7 + 86, $$8 + 16, 0.34F);
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0F, 0.0F, 1.0F);
            guiGraphics0.blit(BG_LOCATION, $$7 + 67, $$8 + 13 + 16, this.f_97726_, 132, 50, 66);
            this.renderMap(guiGraphics0, integer1, mapItemSavedData2, $$7 + 70, $$8 + 32, 0.34F);
            guiGraphics0.pose().popPose();
        } else if (boolean5) {
            guiGraphics0.blit(BG_LOCATION, $$7 + 67, $$8 + 13, this.f_97726_, 0, 66, 66);
            this.renderMap(guiGraphics0, integer1, mapItemSavedData2, $$7 + 71, $$8 + 17, 0.45F);
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0F, 0.0F, 1.0F);
            guiGraphics0.blit(BG_LOCATION, $$7 + 66, $$8 + 12, 0, this.f_97727_, 66, 66);
            guiGraphics0.pose().popPose();
        } else {
            guiGraphics0.blit(BG_LOCATION, $$7 + 67, $$8 + 13, this.f_97726_, 0, 66, 66);
            this.renderMap(guiGraphics0, integer1, mapItemSavedData2, $$7 + 71, $$8 + 17, 0.45F);
        }
    }

    private void renderMap(GuiGraphics guiGraphics0, @Nullable Integer integer1, @Nullable MapItemSavedData mapItemSavedData2, int int3, int int4, float float5) {
        if (integer1 != null && mapItemSavedData2 != null) {
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate((float) int3, (float) int4, 1.0F);
            guiGraphics0.pose().scale(float5, float5, 1.0F);
            this.f_96541_.gameRenderer.getMapRenderer().render(guiGraphics0.pose(), guiGraphics0.bufferSource(), integer1, mapItemSavedData2, true, 15728880);
            guiGraphics0.flush();
            guiGraphics0.pose().popPose();
        }
    }
}
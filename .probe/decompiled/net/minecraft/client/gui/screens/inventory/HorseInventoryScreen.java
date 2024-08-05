package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HorseInventoryMenu;

public class HorseInventoryScreen extends AbstractContainerScreen<HorseInventoryMenu> {

    private static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation("textures/gui/container/horse.png");

    private final AbstractHorse horse;

    private float xMouse;

    private float yMouse;

    public HorseInventoryScreen(HorseInventoryMenu horseInventoryMenu0, Inventory inventory1, AbstractHorse abstractHorse2) {
        super(horseInventoryMenu0, inventory1, abstractHorse2.m_5446_());
        this.horse = abstractHorse2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics0, float float1, int int2, int int3) {
        int $$4 = (this.f_96543_ - this.f_97726_) / 2;
        int $$5 = (this.f_96544_ - this.f_97727_) / 2;
        guiGraphics0.blit(HORSE_INVENTORY_LOCATION, $$4, $$5, 0, 0, this.f_97726_, this.f_97727_);
        if (this.horse instanceof AbstractChestedHorse $$6 && $$6.hasChest()) {
            guiGraphics0.blit(HORSE_INVENTORY_LOCATION, $$4 + 79, $$5 + 17, 0, this.f_97727_, $$6.getInventoryColumns() * 18, 54);
        }
        if (this.horse.isSaddleable()) {
            guiGraphics0.blit(HORSE_INVENTORY_LOCATION, $$4 + 7, $$5 + 35 - 18, 18, this.f_97727_ + 54, 18, 18);
        }
        if (this.horse.canWearArmor()) {
            if (this.horse instanceof Llama) {
                guiGraphics0.blit(HORSE_INVENTORY_LOCATION, $$4 + 7, $$5 + 35, 36, this.f_97727_ + 54, 18, 18);
            } else {
                guiGraphics0.blit(HORSE_INVENTORY_LOCATION, $$4 + 7, $$5 + 35, 0, this.f_97727_ + 54, 18, 18);
            }
        }
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics0, $$4 + 51, $$5 + 60, 17, (float) ($$4 + 51) - this.xMouse, (float) ($$5 + 75 - 50) - this.yMouse, this.horse);
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        this.m_280273_(guiGraphics0);
        this.xMouse = (float) int1;
        this.yMouse = (float) int2;
        super.render(guiGraphics0, int1, int2, float3);
        this.m_280072_(guiGraphics0, int1, int2);
    }
}
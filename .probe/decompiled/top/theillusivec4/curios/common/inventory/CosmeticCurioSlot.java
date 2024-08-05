package top.theillusivec4.curios.common.inventory;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CosmeticCurioSlot extends CurioSlot {

    public CosmeticCurioSlot(Player player, IDynamicStackHandler handler, int index, String identifier, int xPosition, int yPosition) {
        super(player, handler, index, identifier, xPosition, yPosition, NonNullList.create(), true);
        this.setBackground(InventoryMenu.BLOCK_ATLAS, new ResourceLocation("curios", "slot/empty_cosmetic_slot"));
    }

    @Override
    public boolean getRenderStatus() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getSlotName() {
        return I18n.get("curios.cosmetic") + " " + super.getSlotName();
    }
}
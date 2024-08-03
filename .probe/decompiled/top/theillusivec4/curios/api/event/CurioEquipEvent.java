package top.theillusivec4.curios.api.event;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event.HasResult;
import top.theillusivec4.curios.api.SlotContext;

@HasResult
public class CurioEquipEvent extends LivingEvent {

    private final SlotContext slotContext;

    private final ItemStack stack;

    public CurioEquipEvent(ItemStack stack, SlotContext slotContext) {
        super(slotContext.entity());
        this.slotContext = slotContext;
        this.stack = stack;
    }

    public SlotContext getSlotContext() {
        return this.slotContext;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}
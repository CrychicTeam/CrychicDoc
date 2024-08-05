package club.iananderson.seasonhud.impl.curios.item;

import club.iananderson.seasonhud.Common;
import club.iananderson.seasonhud.impl.curios.CuriosCalendar;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import sereneseasons.item.CalendarItem;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class CalendarSlot extends CalendarItem implements ICurioItem {

    public CalendarSlot(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @SubscribeEvent
    public static void sendImc(InterModEnqueueEvent event) {
        if (Common.curiosLoaded()) {
            CuriosCalendar.registerSlots();
        }
    }

    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag unused) {
        return Common.curiosLoaded() ? CuriosCalendar.initCapabilities() : super.initCapabilities(stack, unused);
    }

    @Nonnull
    @Override
    public ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.BOOK_PUT, 1.0F, 1.0F);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
package com.mna.interop;

import com.mna.items.ItemInit;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class CuriosInterop {

    @SubscribeEvent
    public static void initCuriosSlots(InterModEnqueueEvent event) {
        IMCRegisterCurioSlot(SlotTypePreset.HEAD);
        IMCRegisterCurioSlot(SlotTypePreset.BRACELET);
        IMCRegisterCurioSlot(SlotTypePreset.BELT);
        IMCRegisterCurioSlot(SlotTypePreset.NECKLACE);
        IMCRegisterCurioSlot(SlotTypePreset.CHARM);
        IMCRegisterCurioSlot(SlotTypePreset.CURIO);
        IMCRegisterCurioSlot(SlotTypePreset.BACK);
        IMCRegisterCurioSlot(SlotTypePreset.RING, 2);
    }

    @SubscribeEvent
    public static void clientCurioSetup(FMLClientSetupEvent evt) {
        CuriosRendererRegistry.register(ItemInit.FLUID_JUG.get(), () -> new CurioRenderer());
        CuriosRendererRegistry.register(ItemInit.FLUID_JUG_INFINITE_LAVA.get(), () -> new CurioRenderer());
        CuriosRendererRegistry.register(ItemInit.FLUID_JUG_INFINITE_WATER.get(), () -> new CurioRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_PHYLACTERY.get(), () -> new CurioRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_AMETHYST.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_AUM.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_CERUBLOSSOM.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_CHIMERITE.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_DESERTNOVA.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_EMERALD.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_GLASS.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_GOLD.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_IRON.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_LAPIS.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_NETHERQUARTZ.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_PRISMARINECRYSTAL.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_PRISMARINESHARD.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_REDSTONE.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_SKULL.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_SKULL_ALT.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_TARMA.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_VINTEUM.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.STAFF_WAKEBLOOM.get(), () -> new StaffRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_AMETHYST.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_AUM.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_CERUBLOSSOM.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_CHIMERITE.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_DESERTNOVA.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_EMERALD.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_GLASS.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_GOLD.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_IRON.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_LAPIS.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_NETHERQUARTZ.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_PRISMARINECRYSTAL.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_PRISMARINESHARD.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_REDSTONE.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_SKULL.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_SKULL_ALT.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_TARMA.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_VINTEUM.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.WAND_WAKEBLOOM.get(), () -> new WandRenderer());
        CuriosRendererRegistry.register(ItemInit.HAT_FEZ.get(), () -> new HatRenderer());
        CuriosRendererRegistry.register(ItemInit.HAT_LARGE_WITCH.get(), () -> new HatRenderer());
        CuriosRendererRegistry.register(ItemInit.HAT_STYLED_SKULL.get(), () -> new HatRenderer());
        CuriosRendererRegistry.register(ItemInit.HAT_TALL_COWBOY.get(), () -> new HatRenderer());
    }

    private static void IMCRegisterCurioSlot(SlotTypePreset slot) {
        InterModComms.sendTo("curios", "register_type", () -> new SlotTypeMessage.Builder(slot.getIdentifier()).build());
    }

    public static void IMCRegisterCurioSlot(SlotTypePreset slot, int quantity) {
        InterModComms.sendTo("curios", "register_type", () -> new SlotTypeMessage.Builder(slot.getIdentifier()).size(quantity).build());
    }

    public static void IMCModifyCurioSlot(SlotTypePreset slot, int quantity) {
        InterModComms.sendTo("curios", "modify_type", () -> new SlotTypeMessage.Builder(slot.getIdentifier()).size(quantity).build());
    }

    public static boolean IsItemInCurioSlot(Item item, LivingEntity entity, SlotTypePreset slot) {
        Optional<SlotResult> equipped = CuriosApi.getCuriosHelper().findFirstCurio(entity, item);
        return equipped.isPresent() && ((SlotResult) equipped.get()).slotContext().identifier().equals(slot.getIdentifier());
    }

    public static void DamageCurioInSlot(Item item, LivingEntity entity, SlotTypePreset slot, int damage) {
        CuriosApi.getCuriosHelper().findFirstCurio(entity, item).ifPresent(t -> t.stack().hurtAndBreak(damage, entity, damager -> CuriosApi.getCuriosHelper().onBrokenCurio(t.slotContext())));
    }
}
package noobanidus.mods.lootr.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = "lootr", bus = Bus.MOD)
public class ModTabs {

    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "lootr");

    public static final RegistryObject<CreativeModeTab> LOOTR = REGISTER.register("lootr", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.lootr")).icon(() -> new ItemStack(ModBlocks.TROPHY.get())).displayItems((p, output) -> output.accept(ModBlocks.TROPHY.get())).build());

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
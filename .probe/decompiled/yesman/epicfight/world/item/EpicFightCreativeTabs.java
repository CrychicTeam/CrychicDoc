package yesman.epicfight.world.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EpicFightCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "epicfight");

    public static final RegistryObject<CreativeModeTab> ITEMS = TABS.register("items", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.epicfight.items")).icon(() -> new ItemStack(EpicFightItems.SKILLBOOK.get())).withTabsBefore(new ResourceKey[] { CreativeModeTabs.SPAWN_EGGS }).withBackgroundLocation(new ResourceLocation("epicfight", "textures/gui/container/epicfight_creative_tab.png")).hideTitle().displayItems((params, output) -> EpicFightItems.ITEMS.getEntries().forEach(it -> {
        if (it != EpicFightItems.UCHIGATANA_SHEATH) {
            if (it == EpicFightItems.SKILLBOOK) {
                if (it.get() instanceof SkillBookItem skillbookItem) {
                    skillbookItem.fillItemCategory(output::m_246342_);
                }
            } else {
                output.accept((ItemLike) it.get());
            }
        }
    })).build());
}
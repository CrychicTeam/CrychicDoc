package noppes.npcs;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CustomTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "customnpcs");

    public static final RegistryObject<CreativeModeTab> CNPCS = CREATIVE_TABS.register("cnpcs", () -> CreativeModeTab.builder().title(Component.literal("cnpcs")).icon(() -> CustomItems.wand.getDefaultInstance()).displayItems((params, output) -> {
        output.accept(CustomItems.wand.getDefaultInstance());
        output.accept(CustomItems.cloner.getDefaultInstance());
        output.accept(CustomItems.scripter.getDefaultInstance());
        output.accept(CustomItems.moving.getDefaultInstance());
        output.accept(CustomItems.mount.getDefaultInstance());
        output.accept(CustomItems.teleporter.getDefaultInstance());
        output.accept(CustomItems.scripted_item.m_7968_());
        output.accept(CustomItems.nbt_book.m_7968_());
        output.accept(CustomItems.soulstoneEmpty.getDefaultInstance());
        output.accept(CustomBlocks.redstone_item.getDefaultInstance());
        output.accept(CustomBlocks.waypoint_item.getDefaultInstance());
        output.accept(CustomBlocks.border_item.getDefaultInstance());
        output.accept(CustomBlocks.scripted_item.getDefaultInstance());
        output.accept(CustomBlocks.scripted_door_item.getDefaultInstance());
        output.accept(CustomBlocks.builder_item.getDefaultInstance());
        output.accept(CustomBlocks.copy_item.getDefaultInstance());
    }).withTabsBefore(new ResourceKey[] { CreativeModeTabs.SPAWN_EGGS }).build());

    public static final CreativeModeTab tab = new CreativeModeTab(CreativeModeTab.builder().title(Component.literal("cnpcs"))) {

        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack getIconItem() {
            return new ItemStack(CustomItems.wand);
        }
    };
}
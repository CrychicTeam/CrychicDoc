package noppes.npcs;

import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;
import noppes.npcs.containers.ContainerCarpentryBench;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.containers.ContainerMerchantAdd;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCBankLarge;
import noppes.npcs.containers.ContainerNPCBankSmall;
import noppes.npcs.containers.ContainerNPCBankUnlock;
import noppes.npcs.containers.ContainerNPCBankUpgrade;
import noppes.npcs.containers.ContainerNPCCompanion;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.containers.ContainerNPCFollowerSetup;
import noppes.npcs.containers.ContainerNPCInv;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.containers.ContainerNpcItemGiver;
import noppes.npcs.containers.ContainerNpcQuestReward;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;

@EventBusSubscriber(bus = Bus.MOD, modid = "customnpcs")
public class CustomContainer {

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_carpentrybench")
    public static MenuType<ContainerCarpentryBench> container_carpentrybench;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_customgui")
    public static MenuType<ContainerCustomGui> container_customgui;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_mail")
    public static MenuType<ContainerMail> container_mail;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_managebanks")
    public static MenuType<ContainerManageBanks> container_managebanks;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_managerecipes")
    public static MenuType<ContainerManageRecipes> container_managerecipes;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_merchantadd")
    public static MenuType<ContainerManageRecipes> container_merchantadd;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_banklarge")
    public static MenuType<ContainerNPCBankInterface> container_banklarge;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_banksmall")
    public static MenuType<ContainerNPCBankInterface> container_banksmall;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_bankunlock")
    public static MenuType<ContainerNPCBankInterface> container_bankunlock;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_bankupgrade")
    public static MenuType<ContainerNPCBankInterface> container_bankupgrade;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_companion")
    public static MenuType<ContainerNPCCompanion> container_companion;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_follower")
    public static MenuType<ContainerNPCFollower> container_follower;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_followerhire")
    public static MenuType<ContainerNPCFollowerHire> container_followerhire;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_followersetup")
    public static MenuType<ContainerNPCFollowerSetup> container_followersetup;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_inv")
    public static MenuType<ContainerNPCInv> container_inv;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_itemgiver")
    public static MenuType<ContainerNpcItemGiver> container_itemgiver;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_questreward")
    public static MenuType<ContainerNpcQuestReward> container_questreward;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_questtypeitem")
    public static MenuType<ContainerNpcQuestTypeItem> container_questtypeitem;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_trader")
    public static MenuType<ContainerNPCTrader> container_trader;

    @ObjectHolder(registryName = "menu", value = "customnpcs:container_tradersetup")
    public static MenuType<ContainerNPCTraderSetup> container_tradersetup;

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        if (event.getRegistryKey() == ForgeRegistries.Keys.MENU_TYPES) {
            event.<MenuType>getForgeRegistry().register("customnpcs:container_carpentrybench", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerCarpentryBench(containerId, inv, data.readBlockPos())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_customgui", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerCustomGui(containerId, data.readNbt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_mail", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerMail(containerId, inv, data.readBoolean(), data.readBoolean())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_managebanks", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerManageBanks(containerId, inv)));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_managerecipes", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerManageRecipes(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_merchantadd", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerMerchantAdd(containerId, inv)));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_banklarge", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCBankLarge(containerId, inv, data.readInt(), data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_banksmall", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCBankSmall(containerId, inv, data.readInt(), data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_bankunlock", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCBankUnlock(containerId, inv, data.readInt(), data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_bankupgrade", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCBankUpgrade(containerId, inv, data.readInt(), data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_companion", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCCompanion(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_follower", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCFollower(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_followerhire", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCFollowerHire(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_followersetup", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCFollowerSetup(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_inv", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCInv(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_itemgiver", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNpcItemGiver(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_questreward", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNpcQuestReward(containerId, inv)));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_questtypeitem", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNpcQuestTypeItem(containerId, inv)));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_trader", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCTrader(containerId, inv, data.readInt())));
            event.<MenuType>getForgeRegistry().register("customnpcs:container_tradersetup", createContainer((IContainerFactory<AbstractContainerMenu>) (containerId, inv, data) -> new ContainerNPCTraderSetup(containerId, inv, data.readInt())));
        }
    }

    private static MenuType createContainer(MenuType.MenuSupplier factoryIn) {
        return new MenuType(factoryIn, FeatureFlags.VANILLA_SET);
    }
}
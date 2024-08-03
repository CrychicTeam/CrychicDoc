package fr.frinn.custommachinery.client;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.machine.MachineStatus;
import fr.frinn.custommachinery.api.network.IData;
import fr.frinn.custommachinery.client.screen.CustomMachineScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineCreationScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.common.init.CustomMachineContainer;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.machine.MachineAppearance;
import fr.frinn.custommachinery.common.machine.builder.CustomMachineBuilder;
import fr.frinn.custommachinery.common.network.SyncableContainer;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class ClientPacketHandler {

    public static void handleMachineStatusChangedPacket(BlockPos pos, MachineStatus status) {
        if (Minecraft.getInstance().level != null) {
            BlockEntity tile = Minecraft.getInstance().level.m_7702_(pos);
            if (tile instanceof CustomMachineTile machineTile && status != machineTile.getStatus()) {
                machineTile.setStatus(status);
                machineTile.refreshClientData();
                Minecraft.getInstance().level.sendBlockUpdated(pos, tile.getBlockState(), tile.getBlockState(), 3);
            }
        }
    }

    public static void handleRefreshCustomMachineTilePacket(BlockPos pos, ResourceLocation machine) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.m_7702_(pos) instanceof CustomMachineTile machineTile) {
            machineTile.setId(machine);
            machineTile.refreshClientData();
            Minecraft.getInstance().level.sendBlockUpdated(pos, machineTile.m_58900_(), machineTile.m_58900_(), 3);
        }
    }

    public static void handleUpdateContainerPacket(int windowId, List<IData<?>> data) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.f_36096_ instanceof SyncableContainer container && player.f_36096_.containerId == windowId) {
            data.forEach(container::handleData);
        }
    }

    public static void handleUpdateMachinesPacket(Map<ResourceLocation, CustomMachine> machines) {
        CustomMachinery.MACHINES.clear();
        CustomMachinery.MACHINES.putAll(machines);
        Minecraft mc = Minecraft.getInstance();
        CreativeModeTab.ItemDisplayParameters params = new CreativeModeTab.ItemDisplayParameters(mc.player.connection.enabledFeatures(), mc.player.m_36337_() && mc.options.operatorItemsTab().get(), mc.level.m_9598_());
        ((CreativeModeTab) Registration.CUSTOM_MACHINE_TAB.get()).buildContents(params);
        if (Minecraft.getInstance().screen instanceof MachineCreationScreen creationScreen) {
            creationScreen.reloadList();
        }
    }

    public static void handleUpdateMachineAppearancePacket(BlockPos pos, @Nullable MachineAppearance appearance) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.m_7702_(pos) instanceof CustomMachineTile machineTile) {
            machineTile.setCustomAppearance(appearance);
            machineTile.refreshClientData();
            Minecraft.getInstance().level.sendBlockUpdated(pos, machineTile.m_58900_(), machineTile.m_58900_(), 3);
        }
    }

    public static void handleUpdateMachineGuiElementsPacket(BlockPos pos, List<IGuiElement> elements) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.m_7702_(pos) instanceof CustomMachineTile machineTile) {
            machineTile.setCustomGuiElements(elements);
            if (mc.screen instanceof CustomMachineScreen screen) {
                screen.m_6574_(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
                ((CustomMachineContainer) screen.m_6262_()).init();
            }
        }
    }

    public static void handleOpenCreationScreenPacket() {
        Minecraft.getInstance().setScreen(new MachineCreationScreen());
    }

    public static void handleOpenEditScreenPacket(ResourceLocation machineId) {
        CustomMachine machine = (CustomMachine) CustomMachinery.MACHINES.get(machineId);
        if (machine != null) {
            Minecraft.getInstance().setScreen(new MachineEditScreen(new MachineCreationScreen(), 288, 210, new CustomMachineBuilder(machine)));
        }
    }
}
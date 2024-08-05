package fr.frinn.custommachinery.forge.integration.jade;

import fr.frinn.custommachinery.api.machine.MachineStatus;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.impl.util.TextComponentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;

public class CustomMachineComponentProvider implements IBlockComponentProvider {

    public static final CustomMachineComponentProvider INSTANCE = new CustomMachineComponentProvider();

    public static final ResourceLocation ID = new ResourceLocation("custommachinery", "machine_component_provider");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof CustomMachineTile) {
            CompoundTag nbt = accessor.getServerData().getCompound("custommachinery");
            if (nbt.isEmpty()) {
                return;
            }
            boolean errored = false;
            if (nbt.contains("owner", 8)) {
                Component ownerName = TextComponentUtils.fromJsonString(nbt.getString("owner"));
                if (ownerName != null && !ownerName.getString().isEmpty()) {
                    tooltip.add(Component.translatable("custommachinery.machine.info.owner", ownerName));
                }
            }
            if (nbt.contains("status", 1)) {
                MachineStatus machineStatus = MachineStatus.values()[nbt.getByte("status")];
                MutableComponent status = machineStatus.getTranslatedName();
                switch(machineStatus) {
                    case ERRORED:
                        status.withStyle(ChatFormatting.RED);
                        break;
                    case RUNNING:
                        status.withStyle(ChatFormatting.GREEN);
                        break;
                    case PAUSED:
                        status.withStyle(ChatFormatting.GOLD);
                }
                tooltip.add(status);
                if (machineStatus == MachineStatus.ERRORED) {
                    errored = true;
                }
            }
            if (nbt.contains("recipeProgressTime", 6) && nbt.contains("recipeTotalTime", 6)) {
                double recipeProgressTime = nbt.getDouble("recipeProgressTime");
                double recipeTotalTime = nbt.getDouble("recipeTotalTime");
                float progress = (float) (recipeProgressTime / recipeTotalTime);
                Component component = Component.literal((int) recipeProgressTime + " / " + (int) recipeTotalTime);
                tooltip.add(tooltip.getElementHelper().progress(progress, component, tooltip.getElementHelper().progressStyle(), BoxStyle.DEFAULT, true));
            }
            if (errored && nbt.contains("errorMessage", 8)) {
                tooltip.add(Component.Serializer.fromJson(nbt.getString("errorMessage")));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}
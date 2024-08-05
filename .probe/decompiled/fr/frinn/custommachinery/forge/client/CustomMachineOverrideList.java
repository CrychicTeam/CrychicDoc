package fr.frinn.custommachinery.forge.client;

import fr.frinn.custommachinery.api.machine.MachineStatus;
import fr.frinn.custommachinery.common.init.CustomMachineItem;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class CustomMachineOverrideList extends ItemOverrides {

    @Nullable
    @Override
    public BakedModel resolve(BakedModel model, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity livingEntity, int seed) {
        return model instanceof CustomMachineBakedModel machineModel ? (BakedModel) CustomMachineItem.getMachine(stack).map(machine -> machineModel.getMachineItemModel(machine.getAppearance(MachineStatus.IDLE))).orElse(model) : super.resolve(model, stack, world, livingEntity, seed);
    }
}
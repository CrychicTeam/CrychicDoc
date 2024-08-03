package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.api.requirement.ITickableRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.common.component.SkyMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class SkyRequirement extends AbstractRequirement<SkyMachineComponent> implements ITickableRequirement<SkyMachineComponent> {

    public static final NamedCodec<SkyRequirement> CODEC = NamedCodec.unit(SkyRequirement::new, "Sky requirement");

    public SkyRequirement() {
        super(RequirementIOMode.INPUT);
    }

    @Override
    public RequirementType<SkyRequirement> getType() {
        return (RequirementType<SkyRequirement>) Registration.SKY_REQUIREMENT.get();
    }

    @Override
    public MachineComponentType<SkyMachineComponent> getComponentType() {
        return (MachineComponentType<SkyMachineComponent>) Registration.SKY_MACHINE_COMPONENT.get();
    }

    public boolean test(SkyMachineComponent component, ICraftingContext context) {
        return component.canSeeSky();
    }

    public CraftingResult processStart(SkyMachineComponent component, ICraftingContext context) {
        return this.test(component, context) ? CraftingResult.success() : CraftingResult.error(Component.translatable("custommachinery.requirements.sky.error"));
    }

    public CraftingResult processEnd(SkyMachineComponent component, ICraftingContext context) {
        return this.test(component, context) ? CraftingResult.success() : CraftingResult.error(Component.translatable("custommachinery.requirements.sky.error"));
    }

    public CraftingResult processTick(SkyMachineComponent component, ICraftingContext context) {
        return this.test(component, context) ? CraftingResult.success() : CraftingResult.error(Component.translatable("custommachinery.requirements.sky.error"));
    }

    @Override
    public void getDisplayInfo(IDisplayInfo info) {
        info.setItemIcon(Items.DAYLIGHT_DETECTOR);
        info.addTooltip(Component.translatable("custommachinery.requirements.sky.error"));
    }
}
package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfo;
import fr.frinn.custommachinery.api.integration.jei.IDisplayInfoRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.common.component.PositionMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import fr.frinn.custommachinery.impl.util.IntRange;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class PositionRequirement extends AbstractRequirement<PositionMachineComponent> implements IDisplayInfoRequirement {

    public static final NamedCodec<PositionRequirement> CODEC = NamedCodec.record(positionRequirementInstance -> positionRequirementInstance.group(IntRange.CODEC.optionalFieldOf("x", IntRange.ALL).forGetter(requirement -> requirement.x), IntRange.CODEC.optionalFieldOf("y", IntRange.ALL).forGetter(requirement -> requirement.y), IntRange.CODEC.optionalFieldOf("z", IntRange.ALL).forGetter(requirement -> requirement.z)).apply(positionRequirementInstance, PositionRequirement::new), "Position requirement");

    private final IntRange x;

    private final IntRange y;

    private final IntRange z;

    public PositionRequirement(IntRange x, IntRange y, IntRange z) {
        super(RequirementIOMode.INPUT);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public RequirementType<PositionRequirement> getType() {
        return (RequirementType<PositionRequirement>) Registration.POSITION_REQUIREMENT.get();
    }

    public boolean test(PositionMachineComponent component, ICraftingContext context) {
        BlockPos pos = component.getPosition();
        return this.x.contains(Integer.valueOf(pos.m_123341_())) && this.y.contains(Integer.valueOf(pos.m_123342_())) && this.z.contains(Integer.valueOf(pos.m_123343_()));
    }

    public CraftingResult processStart(PositionMachineComponent component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    public CraftingResult processEnd(PositionMachineComponent component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    @Override
    public MachineComponentType<PositionMachineComponent> getComponentType() {
        return (MachineComponentType<PositionMachineComponent>) Registration.POSITION_MACHINE_COMPONENT.get();
    }

    @Override
    public void getDisplayInfo(IDisplayInfo info) {
        info.addTooltip(Component.translatable("custommachinery.requirements.position.info.pos").withStyle(ChatFormatting.AQUA));
        info.addTooltip(Component.literal("X: ").append(Component.literal(this.x.toFormattedString())));
        info.addTooltip(Component.literal("Y: ").append(Component.literal(this.y.toFormattedString())));
        info.addTooltip(Component.literal("Z: ").append(Component.literal(this.z.toFormattedString())));
        info.setItemIcon(Items.COMPASS);
    }
}
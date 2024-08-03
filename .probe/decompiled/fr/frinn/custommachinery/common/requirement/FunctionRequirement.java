package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.requirement.ITickableRequirement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.common.component.FunctionMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.requirement.AbstractDelayedChanceableRequirement;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.chat.Component;

public class FunctionRequirement extends AbstractDelayedChanceableRequirement<FunctionMachineComponent> implements ITickableRequirement<FunctionMachineComponent> {

    public static final NamedCodec<FunctionRequirement> CODEC = NamedCodec.unit(new FunctionRequirement(FunctionRequirement.Phase.START, ctx -> CraftingResult.pass(), error -> {
    }));

    private FunctionRequirement.Phase phase;

    private final Function<ICraftingContext, CraftingResult> function;

    private final Consumer<Throwable> logger;

    private boolean errored = false;

    public FunctionRequirement(FunctionRequirement.Phase phase, Function<ICraftingContext, CraftingResult> function, Consumer<Throwable> logger) {
        super(RequirementIOMode.INPUT);
        this.phase = phase;
        this.function = function;
        this.logger = logger;
    }

    private CraftingResult processFunction(ICraftingContext context) {
        if (this.errored) {
            return CraftingResult.error(Component.translatable("custommachinery.requirements.function.error"));
        } else {
            try {
                return (CraftingResult) this.function.apply(context);
            } catch (Throwable var3) {
                this.errored = true;
                this.logger.accept(var3);
                return CraftingResult.error(Component.translatable("custommachinery.requirements.function.error"));
            }
        }
    }

    @Override
    public RequirementType<FunctionRequirement> getType() {
        return (RequirementType<FunctionRequirement>) Registration.FUNCTION_REQUIREMENT.get();
    }

    public CraftingResult execute(FunctionMachineComponent component, ICraftingContext context) {
        return this.phase != FunctionRequirement.Phase.DELAY ? CraftingResult.pass() : this.processFunction(context);
    }

    @Override
    public MachineComponentType<FunctionMachineComponent> getComponentType() {
        return (MachineComponentType<FunctionMachineComponent>) Registration.FUNCTION_MACHINE_COMPONENT.get();
    }

    public boolean test(FunctionMachineComponent component, ICraftingContext context) {
        return this.phase != FunctionRequirement.Phase.CHECK ? true : this.processFunction(context).isSuccess();
    }

    public CraftingResult processStart(FunctionMachineComponent component, ICraftingContext context) {
        return this.phase != FunctionRequirement.Phase.START ? CraftingResult.pass() : this.processFunction(context);
    }

    public CraftingResult processEnd(FunctionMachineComponent component, ICraftingContext context) {
        return this.phase != FunctionRequirement.Phase.END ? CraftingResult.pass() : this.processFunction(context);
    }

    public CraftingResult processTick(FunctionMachineComponent component, ICraftingContext context) {
        return this.phase != FunctionRequirement.Phase.TICK ? CraftingResult.pass() : this.processFunction(context);
    }

    @Override
    public void setDelay(double delay) {
        super.setDelay(delay);
        this.phase = FunctionRequirement.Phase.DELAY;
    }

    public static enum Phase {

        CHECK, START, TICK, END, DELAY
    }
}
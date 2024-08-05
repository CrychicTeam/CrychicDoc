package fr.frinn.custommachinery.common.requirement;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.crafting.CraftingResult;
import fr.frinn.custommachinery.api.crafting.ICraftingContext;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientRequirement;
import fr.frinn.custommachinery.api.integration.jei.IJEIIngredientWrapper;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.api.requirement.RequirementType;
import fr.frinn.custommachinery.client.integration.jei.wrapper.LootTableIngredientWrapper;
import fr.frinn.custommachinery.common.component.handler.ItemComponentHandler;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.LootTableHelper;
import fr.frinn.custommachinery.impl.codec.DefaultCodecs;
import fr.frinn.custommachinery.impl.requirement.AbstractRequirement;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LootTableRequirement extends AbstractRequirement<ItemComponentHandler> implements IJEIIngredientRequirement<ItemStack> {

    public static final NamedCodec<LootTableRequirement> CODEC = NamedCodec.record(lootTableRequirementInstance -> lootTableRequirementInstance.group(DefaultCodecs.RESOURCE_LOCATION.fieldOf("table").forGetter(requirement -> requirement.lootTable), NamedCodec.FLOAT.optionalFieldOf("luck", 0.0F).forGetter(requirement -> requirement.luck)).apply(lootTableRequirementInstance, LootTableRequirement::new), "Loottable requirement");

    private final ResourceLocation lootTable;

    private final float luck;

    private List<ItemStack> toOutput = Collections.emptyList();

    public LootTableRequirement(ResourceLocation lootTable, float luck) {
        super(RequirementIOMode.OUTPUT);
        this.lootTable = lootTable;
        this.luck = luck;
        LootTableHelper.addTable(lootTable);
    }

    @Override
    public RequirementType<LootTableRequirement> getType() {
        return (RequirementType<LootTableRequirement>) Registration.LOOT_TABLE_REQUIREMENT.get();
    }

    public boolean test(ItemComponentHandler component, ICraftingContext context) {
        return true;
    }

    public CraftingResult processStart(ItemComponentHandler component, ICraftingContext context) {
        return CraftingResult.pass();
    }

    public CraftingResult processEnd(ItemComponentHandler component, ICraftingContext context) {
        if (this.getMode() != RequirementIOMode.INPUT && context.getMachineTile().m_58904_() != null && context.getMachineTile().m_58904_().getServer() != null) {
            if (this.toOutput.isEmpty()) {
                LootTable table = context.getMachineTile().m_58904_().getServer().getLootData().m_278676_(this.lootTable);
                LootParams params = new LootParams.Builder((ServerLevel) context.getMachineTile().m_58904_()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(context.getMachineTile().m_58899_())).withParameter(LootContextParams.BLOCK_ENTITY, context.getMachineTile()).withLuck((float) context.getModifiedValue((double) this.luck, this, "luck")).create(Registration.CUSTOM_MACHINE_LOOT_PARAMETER_SET);
                this.toOutput = table.getRandomItems(params);
            }
            Iterator<ItemStack> iterator = this.toOutput.iterator();
            while (iterator.hasNext()) {
                ItemStack stack = (ItemStack) iterator.next();
                if (component.getSpaceForItem("", stack.getItem(), stack.getTag()) < stack.getCount()) {
                    return CraftingResult.error(Component.translatable("custommachinery.requirements.item.error.output", stack.getCount(), Component.translatable(stack.getDescriptionId())));
                }
                component.addToOutputs("", stack.getItem(), stack.getCount(), stack.getTag());
                iterator.remove();
            }
            return CraftingResult.success();
        } else {
            return CraftingResult.pass();
        }
    }

    @Override
    public MachineComponentType getComponentType() {
        return (MachineComponentType) Registration.ITEM_MACHINE_COMPONENT.get();
    }

    @Override
    public List<IJEIIngredientWrapper<ItemStack>> getJEIIngredientWrappers(IMachineRecipe recipe) {
        return Collections.singletonList(new LootTableIngredientWrapper(this.lootTable));
    }
}
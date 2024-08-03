package fr.frinn.custommachinery.common.init;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.api.component.ISideConfigComponent;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigurationCardItem extends Item {

    private static final String MACHINE_ID = "machineId";

    private static final String SIDE_CONFIG = "sideConfig";

    public ConfigurationCardItem(Item.Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        } else {
            BlockPos pos = context.getClickedPos();
            Level level = context.getLevel();
            return level.getBlockEntity(pos) instanceof CustomMachineTile machine ? this.copyConfiguration(level, player, machine, player.m_21120_(context.getHand())) : InteractionResult.FAIL;
        }
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!level.isClientSide && player.m_6047_()) {
            stack.removeTagKey("custommachinery");
            player.m_213846_(Component.translatable("custommachinery.configuration_card.cleared").withStyle(ChatFormatting.GREEN));
            return InteractionResultHolder.success(stack);
        } else {
            return super.use(level, player, hand);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        getMachineId(stack).flatMap(id -> Optional.ofNullable((CustomMachine) CustomMachinery.MACHINES.get(id))).ifPresent(machine -> tooltip.add(Component.translatable("custommachinery.configuration_card.configured", machine.getName()).withStyle(ChatFormatting.AQUA)));
        tooltip.add(Component.translatable("custommachinery.configuration_card.copy").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("custommachinery.configuration_card.paste").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("custommachinery.configuration_card.reset").withStyle(ChatFormatting.GOLD));
    }

    private InteractionResult copyConfiguration(Level level, Player player, CustomMachineTile machine, ItemStack stack) {
        if (!level.isClientSide && player.m_6047_()) {
            setMachineId(stack, machine.getId());
            for (ISideConfigComponent component : machine.getComponentManager().getConfigComponents()) {
                serializeSideConfig(stack, component);
            }
            player.m_213846_(Component.translatable("custommachinery.configuration_card.copied").withStyle(ChatFormatting.GREEN));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static InteractionResult pasteConfiguration(Level level, Player player, CustomMachineTile machine, ItemStack stack) {
        if (!level.isClientSide) {
            Optional<ResourceLocation> machineId = getMachineId(stack);
            if (!(Boolean) machineId.map(id -> id.equals(machine.getId())).orElse(false)) {
                player.m_213846_(Component.translatable("custommachinery.configuration_card.different_machine").withStyle(ChatFormatting.RED));
                return InteractionResult.FAIL;
            }
            for (ISideConfigComponent component : machine.getComponentManager().getConfigComponents()) {
                deserializeSideConfig(stack, component);
            }
            player.m_213846_(Component.translatable("custommachinery.configuration_card.pasted").withStyle(ChatFormatting.GREEN));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static Optional<CompoundTag> getDataHolder(ItemStack stack) {
        return Optional.ofNullable(stack.getTagElement("custommachinery"));
    }

    private static Optional<ResourceLocation> getMachineId(ItemStack stack) {
        return getDataHolder(stack).map(nbt -> ResourceLocation.tryParse(nbt.getString("machineId")));
    }

    private static void setMachineId(ItemStack stack, ResourceLocation id) {
        stack.getOrCreateTagElement("custommachinery").putString("machineId", id.toString());
    }

    private static void deserializeSideConfig(ItemStack stack, ISideConfigComponent component) {
        getDataHolder(stack).ifPresent(nbt -> {
            if (nbt.contains("sideConfig", 10)) {
                CompoundTag sideConfig = nbt.getCompound("sideConfig");
                if (sideConfig.contains(component.getId())) {
                    component.getConfig().deserialize(sideConfig.get(component.getId()));
                }
            }
        });
    }

    private static void serializeSideConfig(ItemStack stack, ISideConfigComponent component) {
        CompoundTag sideConfig = stack.getOrCreateTagElement("custommachinery").getCompound("sideConfig");
        if (!stack.getOrCreateTagElement("custommachinery").contains("sideConfig", 10)) {
            stack.getOrCreateTagElement("custommachinery").put("sideConfig", sideConfig);
        }
        sideConfig.put(component.getId(), component.getConfig().serialize());
        stack.getOrCreateTagElement("custommachinery").put("sideConfig", sideConfig);
    }
}
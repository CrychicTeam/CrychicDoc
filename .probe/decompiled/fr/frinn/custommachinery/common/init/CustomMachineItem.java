package fr.frinn.custommachinery.common.init;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.List;
import java.util.Optional;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class CustomMachineItem extends BlockItem {

    public static final String MACHINE_TAG_KEY = "machine";

    @Nullable
    private final ResourceLocation machineID;

    public CustomMachineItem(Block block, Item.Properties properties, @Nullable ResourceLocation machineID) {
        super(block, properties);
        this.machineID = machineID;
    }

    public static Optional<CustomMachine> getMachine(ItemStack stack) {
        if (stack.getItem() instanceof CustomMachineItem customMachineItem && customMachineItem.machineID != null) {
            return Optional.ofNullable((CustomMachine) CustomMachinery.MACHINES.get(customMachineItem.machineID));
        }
        if (stack.getItem() == Registration.CUSTOM_MACHINE_ITEM.get() && stack.getTag() != null && stack.getTag().contains("machine", 8) && Utils.isResourceNameValid(stack.getTag().getString("machine"))) {
            ResourceLocation machineID = new ResourceLocation(stack.getTag().getString("machine"));
            return machineID.equals(CustomMachine.DUMMY.getId()) ? Optional.of(CustomMachine.DUMMY) : Optional.ofNullable((CustomMachine) CustomMachinery.MACHINES.get(machineID));
        } else {
            return Optional.empty();
        }
    }

    public static ItemStack makeMachineItem(ResourceLocation machineID) {
        if (CustomMachinery.CUSTOM_BLOCK_MACHINES.containsKey(machineID)) {
            return ((CustomMachineBlock) CustomMachinery.CUSTOM_BLOCK_MACHINES.get(machineID)).m_5456_().getDefaultInstance();
        } else {
            ItemStack stack = ((CustomMachineItem) Registration.CUSTOM_MACHINE_ITEM.get()).m_7968_();
            CompoundTag nbt = new CompoundTag();
            nbt.putString("machine", machineID.toString());
            stack.setTag(nbt);
            return stack;
        }
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        if (stack.getTag() == null || !stack.getTag().contains("machine", 8)) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("machine", CustomMachine.DUMMY.getId().toString());
            stack.setTag(nbt);
        }
        super.m_7836_(stack, worldIn, playerIn);
    }

    @Override
    public Component getName(ItemStack stack) {
        return (Component) getMachine(stack).map(CustomMachine::getName).orElse(super.m_7626_(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        getMachine(stack).map(CustomMachine::getTooltips).ifPresent(tooltip::addAll);
    }

    @Override
    public InteractionResult place(BlockPlaceContext blockPlaceContext) {
        if (!blockPlaceContext.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockPlaceContext2 = this.m_7732_(blockPlaceContext);
            if (blockPlaceContext2 == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockState = this.m_5965_(blockPlaceContext2);
                if (blockState == null) {
                    return InteractionResult.FAIL;
                } else if (!this.m_7429_(blockPlaceContext2, blockState)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockPos = blockPlaceContext2.getClickedPos();
                    Level level = blockPlaceContext2.m_43725_();
                    Player player = blockPlaceContext2.m_43723_();
                    ItemStack itemStack = blockPlaceContext2.m_43722_();
                    BlockState blockState2 = level.getBlockState(blockPos);
                    if (blockState2.m_60713_(blockState.m_60734_())) {
                        this.m_7274_(blockPos, level, player, itemStack, blockState2);
                        blockState2.m_60734_().setPlacedBy(level, blockPos, blockState2, player, itemStack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, blockPos, itemStack);
                        }
                    }
                    if (blockState2.m_60734_() instanceof CustomMachineBlock machineBlock) {
                        SoundType soundType = machineBlock.getSoundType(blockState, level, blockPos, player);
                        level.playSound(player, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                        level.m_142346_(player, GameEvent.BLOCK_PLACE, blockPos);
                    }
                    if (player == null || !player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }
}
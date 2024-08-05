package dev.xkmc.l2hostility.content.item.consumable;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.chunk.ChunkDifficulty;
import dev.xkmc.l2hostility.content.capability.chunk.SectionDifficulty;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class HostilityOrb extends Item {

    public HostilityOrb(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!LHConfig.COMMON.allowHostilityOrb.get()) {
            return InteractionResultHolder.pass(stack);
        } else if (CurioCompat.hasItemInCurioOrSlot(player, (Item) LHItems.DETECTOR.get()) && CurioCompat.hasItemInCurioOrSlot(player, (Item) LHItems.DETECTOR_GLASSES.get())) {
            if (!level.isClientSide()) {
                boolean success = false;
                int r = LHConfig.COMMON.orbRadius.get();
                for (int x = -r; x <= r; x++) {
                    for (int y = -r; y <= r; y++) {
                        for (int z = -r; z <= r; z++) {
                            BlockPos pos = player.m_20183_().offset(x * 16, y * 16, z * 16);
                            if (!level.m_151570_(pos)) {
                                Optional<ChunkDifficulty> opt = ChunkDifficulty.at(level, pos);
                                if (opt.isPresent()) {
                                    SectionDifficulty sec = ((ChunkDifficulty) opt.get()).getSection(pos.m_123342_());
                                    if (!sec.isCleared()) {
                                        success = true;
                                        sec.setClear((ChunkDifficulty) opt.get(), pos);
                                    }
                                }
                            }
                        }
                    }
                }
                if (success) {
                    stack.shrink(1);
                }
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (LHConfig.COMMON.allowHostilityOrb.get()) {
            int r = LHConfig.COMMON.orbRadius.get() * 2 + 1;
            list.add(LangData.orbUse().withStyle(ChatFormatting.DARK_GREEN));
            list.add(LangData.ITEM_ORB.get(r, r, r).withStyle(ChatFormatting.GRAY));
        }
    }
}
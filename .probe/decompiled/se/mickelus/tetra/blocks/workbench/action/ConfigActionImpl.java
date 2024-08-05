package se.mickelus.tetra.blocks.workbench.action;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.blocks.workbench.WorkbenchTile;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class ConfigActionImpl extends ConfigAction {

    private static final LootContextParamSet lootParameters = new LootContextParamSet.Builder().required(LootContextParams.ORIGIN).optional(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY).build();

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public boolean canPerformOn(Player player, WorkbenchTile tile, ItemStack itemStack) {
        return this.requirement != null && this.requirement.matches(itemStack);
    }

    @Override
    public Collection<ToolAction> getRequiredToolActions(ItemStack itemStack) {
        return this.requiredTools.getValues();
    }

    @Override
    public int getRequiredToolLevel(ItemStack itemStack, ToolAction toolAction) {
        return this.requiredTools.getLevel(toolAction);
    }

    @Override
    public Map<ToolAction, Integer> getRequiredTools(ItemStack itemStack) {
        return this.requiredTools.getLevelMap();
    }

    @Override
    public void perform(Player player, ItemStack targetStack, WorkbenchTile workbench) {
        if (player != null && !player.m_9236_().isClientSide) {
            ServerLevel world = (ServerLevel) player.m_9236_();
            LootTable table = world.getServer().getLootData().m_278676_(this.lootTable);
            ItemStack toolStack = (ItemStack) this.requiredTools.getLevelMap().entrySet().stream().min(Entry.comparingByValue()).map(entry -> {
                ItemStack providingStack = PropertyHelper.getPlayerProvidingItemStack((ToolAction) entry.getKey(), (Integer) entry.getValue(), player);
                if (providingStack.isEmpty()) {
                    providingStack = PropertyHelper.getToolbeltProvidingItemStack((ToolAction) entry.getKey(), (Integer) entry.getValue(), player);
                }
                return providingStack;
            }).orElse(ItemStack.EMPTY);
            LootParams context = new LootParams.Builder(world).withLuck(player.getLuck()).withParameter(LootContextParams.TOOL, toolStack).withParameter(LootContextParams.THIS_ENTITY, player).withParameter(LootContextParams.ORIGIN, player.m_20182_()).create(lootParameters);
            table.getRandomItems(context).forEach(itemStack -> {
                if (!player.getInventory().add(itemStack)) {
                    player.drop(itemStack, false);
                }
            });
            BlockPos pos = workbench.m_58899_();
            world.m_5594_(null, pos, SoundEvents.STONE_BREAK, player.getSoundSource(), 1.0F, 1.5F + (float) Math.random() * 0.5F);
            world.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, targetStack), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 1.1, (double) pos.m_123343_() + 0.5, 4, 0.0, 0.0, 0.0, 0.1F);
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, targetStack);
            targetStack.setCount(targetStack.getCount() - 1);
            workbench.setChanged();
        } else if (!workbench.m_58904_().isClientSide) {
            ServerLevel world = (ServerLevel) workbench.m_58904_();
            LootTable table = world.getServer().getLootData().m_278676_(this.lootTable);
            LootParams context = new LootParams.Builder(world).withParameter(LootContextParams.ORIGIN, Vec3.upFromBottomCenterOf(workbench.m_58899_(), 1.1F)).create(lootParameters);
            table.getRandomItems(context).forEach(itemStack -> Block.popResource(world, workbench.m_58899_().above(), itemStack));
            BlockPos pos = workbench.m_58899_();
            world.m_5594_(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.5F + (float) Math.random() * 0.5F);
            world.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, targetStack), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 1.1, (double) pos.m_123343_() + 0.5, 4, 0.0, 0.0, 0.0, 0.1F);
            targetStack.setCount(targetStack.getCount() - 1);
            workbench.setChanged();
        }
    }

    @Override
    public boolean allowInWorldInteraction() {
        return this.inWorld;
    }
}
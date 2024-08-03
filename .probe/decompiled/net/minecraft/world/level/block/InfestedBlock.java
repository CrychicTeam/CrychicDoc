package net.minecraft.world.level.block;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class InfestedBlock extends Block {

    private final Block hostBlock;

    private static final Map<Block, Block> BLOCK_BY_HOST_BLOCK = Maps.newIdentityHashMap();

    private static final Map<BlockState, BlockState> HOST_TO_INFESTED_STATES = Maps.newIdentityHashMap();

    private static final Map<BlockState, BlockState> INFESTED_TO_HOST_STATES = Maps.newIdentityHashMap();

    public InfestedBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1.destroyTime(block0.m_155943_() / 2.0F).explosionResistance(0.75F));
        this.hostBlock = block0;
        BLOCK_BY_HOST_BLOCK.put(block0, this);
    }

    public Block getHostBlock() {
        return this.hostBlock;
    }

    public static boolean isCompatibleHostBlock(BlockState blockState0) {
        return BLOCK_BY_HOST_BLOCK.containsKey(blockState0.m_60734_());
    }

    private void spawnInfestation(ServerLevel serverLevel0, BlockPos blockPos1) {
        Silverfish $$2 = EntityType.SILVERFISH.create(serverLevel0);
        if ($$2 != null) {
            $$2.m_7678_((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_() + 0.5, 0.0F, 0.0F);
            serverLevel0.addFreshEntity($$2);
            $$2.m_21373_();
        }
    }

    @Override
    public void spawnAfterBreak(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, ItemStack itemStack3, boolean boolean4) {
        super.m_213646_(blockState0, serverLevel1, blockPos2, itemStack3, boolean4);
        if (serverLevel1.m_46469_().getBoolean(GameRules.RULE_DOBLOCKDROPS) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack3) == 0) {
            this.spawnInfestation(serverLevel1, blockPos2);
        }
    }

    public static BlockState infestedStateByHost(BlockState blockState0) {
        return getNewStateWithProperties(HOST_TO_INFESTED_STATES, blockState0, () -> ((Block) BLOCK_BY_HOST_BLOCK.get(blockState0.m_60734_())).defaultBlockState());
    }

    public BlockState hostStateByInfested(BlockState blockState0) {
        return getNewStateWithProperties(INFESTED_TO_HOST_STATES, blockState0, () -> this.getHostBlock().defaultBlockState());
    }

    private static BlockState getNewStateWithProperties(Map<BlockState, BlockState> mapBlockStateBlockState0, BlockState blockState1, Supplier<BlockState> supplierBlockState2) {
        return (BlockState) mapBlockStateBlockState0.computeIfAbsent(blockState1, p_153429_ -> {
            BlockState $$2 = (BlockState) supplierBlockState2.get();
            for (Property $$3 : p_153429_.m_61147_()) {
                $$2 = $$2.m_61138_($$3) ? (BlockState) $$2.m_61124_($$3, p_153429_.m_61143_($$3)) : $$2;
            }
            return $$2;
        });
    }
}
package vectorwing.farmersdelight.common.item;

import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

public class KnifeItem extends DiggerItem {

    public KnifeItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(attackDamage, attackSpeed, tier, ModTags.MINEABLE_WITH_KNIFE, properties);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        ItemStack toolStack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Direction facing = context.getClickedFace();
        if (state.m_60734_() == Blocks.PUMPKIN && toolStack.is(ModTags.KNIVES)) {
            Player player = context.getPlayer();
            if (player != null && !level.isClientSide) {
                Direction direction = facing.getAxis() == Direction.Axis.Y ? player.m_6350_().getOpposite() : facing;
                level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlock(pos, (BlockState) Blocks.CARVED_PUMPKIN.defaultBlockState().m_61124_(CarvedPumpkinBlock.FACING, direction), 11);
                ItemEntity itemEntity = new ItemEntity(level, (double) pos.m_123341_() + 0.5 + (double) direction.getStepX() * 0.65, (double) pos.m_123342_() + 0.1, (double) pos.m_123343_() + 0.5 + (double) direction.getStepZ() * 0.65, new ItemStack(Items.PUMPKIN_SEEDS, 4));
                itemEntity.m_20334_(0.05 * (double) direction.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * (double) direction.getStepZ() + level.random.nextDouble() * 0.02);
                level.m_7967_(itemEntity);
                toolStack.hurtAndBreak(1, player, playerIn -> playerIn.m_21190_(context.getHand()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        Set<Enchantment> ALLOWED_ENCHANTMENTS = Sets.newHashSet(new Enchantment[] { Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.MOB_LOOTING });
        if (ALLOWED_ENCHANTMENTS.contains(enchantment)) {
            return true;
        } else {
            Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(new Enchantment[] { Enchantments.BLOCK_FORTUNE });
            return DENIED_ENCHANTMENTS.contains(enchantment) ? false : enchantment.category.canEnchant(stack.getItem());
        }
    }

    @EventBusSubscriber(modid = "farmersdelight", bus = Bus.FORGE)
    public static class KnifeEvents {

        @SubscribeEvent
        public static void onKnifeKnockback(LivingKnockBackEvent event) {
            LivingEntity attacker = event.getEntity().getKillCredit();
            ItemStack toolStack = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
            if (toolStack.getItem() instanceof KnifeItem) {
                event.setStrength(event.getOriginalStrength() - 0.1F);
            }
        }

        @SubscribeEvent
        public static void onCakeInteraction(PlayerInteractEvent.RightClickBlock event) {
            ItemStack toolStack = event.getEntity().m_21120_(event.getHand());
            if (toolStack.is(ModTags.KNIVES)) {
                Level level = event.getLevel();
                BlockPos pos = event.getPos();
                BlockState state = event.getLevel().getBlockState(pos);
                Block block = state.m_60734_();
                if (state.m_204336_(ModTags.DROPS_CAKE_SLICE)) {
                    level.setBlock(pos, (BlockState) Blocks.CAKE.defaultBlockState().m_61124_(CakeBlock.BITES, 1), 3);
                    Block.dropResources(state, level, pos);
                    ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.CAKE_SLICE.get()), (double) pos.m_123341_(), (double) pos.m_123342_() + 0.2, (double) pos.m_123343_() + 0.5, -0.05, 0.0, 0.0);
                    level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
                if (block == Blocks.CAKE) {
                    int bites = (Integer) state.m_61143_(CakeBlock.BITES);
                    if (bites < 6) {
                        level.setBlock(pos, (BlockState) state.m_61124_(CakeBlock.BITES, bites + 1), 3);
                    } else {
                        level.removeBlock(pos, false);
                    }
                    ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.CAKE_SLICE.get()), (double) pos.m_123341_() + (double) bites * 0.1, (double) pos.m_123342_() + 0.2, (double) pos.m_123343_() + 0.5, -0.05, 0.0, 0.0);
                    level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }
    }
}
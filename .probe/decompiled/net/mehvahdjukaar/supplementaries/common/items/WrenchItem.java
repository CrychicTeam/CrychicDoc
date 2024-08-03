package net.mehvahdjukaar.supplementaries.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.Optional;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.FlanCompat;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class WrenchItem extends Item {

    private final float attackDamage = 2.5F;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public WrenchItem(Item.Properties pProperties) {
        super(pProperties);
        float pAttackSpeedModifier = -2.0F;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", (double) pAttackSpeedModifier, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean isValidRepairItem(ItemStack pStack, ItemStack pRepairCandidate) {
        return pRepairCandidate.is(Items.COPPER_INGOT);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pAttacker.m_9236_().playSound(null, pTarget, SoundEvents.ANVIL_PLACE, SoundSource.NEUTRAL, 0.5F, 1.8F);
        pStack.hurtAndBreak(1, pAttacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            if (CompatHandler.FLAN && !FlanCompat.canPlace(player, pos)) {
                return InteractionResult.FAIL;
            }
            ItemStack itemstack = context.getItemInHand();
            Direction dir = context.getClickedFace();
            boolean shiftDown = player.m_6144_();
            Optional<Direction> success = BlockUtil.tryRotatingBlockAndConnected(dir, shiftDown, pos, level, context.getClickLocation());
            if (success.isPresent()) {
                dir = (Direction) success.get();
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemstack);
                    level.m_142346_(player, GameEvent.BLOCK_CHANGE, pos);
                } else {
                    playTurningEffects(pos, shiftDown, dir, level, player);
                }
                itemstack.hurtAndBreak(1, player, p -> p.m_21190_(context.getHand()));
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            if (level.isClientSide) {
                level.playSound(context.getPlayer(), player, SoundEvents.SPYGLASS_STOP_USING, SoundSource.PLAYERS, 1.4F, 0.8F);
            }
        }
        return InteractionResult.FAIL;
    }

    public static void playTurningEffects(BlockPos pos, boolean shiftDown, Direction dir, Level level, Player player) {
        if ((Boolean) ClientConfigs.Items.WRENCH_PARTICLES.get()) {
            if (dir == Direction.DOWN) {
                shiftDown = !shiftDown;
            }
            level.addParticle((ParticleOptions) ModParticles.ROTATION_TRAIL_EMITTER.get(), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, (double) dir.get3DDataValue(), 0.71, shiftDown ? 1.0 : -1.0);
        }
        level.playSound(player, pos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 0.6F);
        level.playSound(player, player, SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1.0F, 1.4F);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand pUsedHand) {
        if (!(entity instanceof ArmorStand) && !entity.m_6095_().is(ModTags.ROTATABLE)) {
            return InteractionResult.PASS;
        } else {
            boolean shiftDown = player.m_6144_();
            float inc = 22.5F * (float) (shiftDown ? -1 : 1);
            entity.m_146922_(entity.m_146908_() + inc);
            Level level = player.m_9236_();
            if (level.isClientSide) {
                playTurningEffects(entity.m_20097_().above(), shiftDown, Direction.UP, level, player);
            }
            stack.hurtAndBreak(1, player, p -> p.m_21190_(pUsedHand));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
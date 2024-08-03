package se.mickelus.tetra.items.modular;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.effect.AbilityUseResult;
import se.mickelus.tetra.effect.ChargedAbilityEffect;
import se.mickelus.tetra.effect.CritEffect;
import se.mickelus.tetra.effect.EffectHelper;
import se.mickelus.tetra.effect.ExecuteEffect;
import se.mickelus.tetra.effect.ExtractionEffect;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.effect.ItemEffectHandler;
import se.mickelus.tetra.effect.JankEffect;
import se.mickelus.tetra.effect.LungeEffect;
import se.mickelus.tetra.effect.OverpowerEffect;
import se.mickelus.tetra.effect.PiercingEffect;
import se.mickelus.tetra.effect.PryChargedEffect;
import se.mickelus.tetra.effect.PryEffect;
import se.mickelus.tetra.effect.PunctureEffect;
import se.mickelus.tetra.effect.ReapEffect;
import se.mickelus.tetra.effect.SculkTaintEffect;
import se.mickelus.tetra.effect.SlamEffect;
import se.mickelus.tetra.effect.SweepingEffect;
import se.mickelus.tetra.effect.UnboundExtractionEffect;
import se.mickelus.tetra.effect.howling.HowlingEffect;
import se.mickelus.tetra.effect.potion.StunPotionEffect;
import se.mickelus.tetra.items.modular.impl.ModularSingleHeadedItem;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.properties.AttributeHelper;
import se.mickelus.tetra.util.TierHelper;
import se.mickelus.tetra.util.ToolActionHelper;

@ParametersAreNonnullByDefault
public class ItemModularHandheld extends ModularItem {

    public static final TagKey<Block> nailedTag = BlockTags.create(new ResourceLocation("tetra", "nailed"));

    public static final int blockingDurationLimit = 16;

    static final ChargedAbilityEffect[] abilities = new ChargedAbilityEffect[] { ExecuteEffect.instance, LungeEffect.instance, SlamEffect.instance, PunctureEffect.instance, OverpowerEffect.instance, ReapEffect.instance, PryChargedEffect.instance };

    protected int blockDestroyDamage = 1;

    protected int entityHitDamage = 1;

    public ItemModularHandheld(Item.Properties properties) {
        super(properties);
    }

    public static boolean canDenail(BlockState blockState) {
        return blockState.m_204336_(nailedTag);
    }

    public static void handleChargedAbility(Player player, InteractionHand hand, @Nullable LivingEntity target, @Nullable BlockPos targetPos, @Nullable Vec3 hitVec, int ticksUsed) {
        ItemStack activeStack = player.m_21120_(hand);
        if (!activeStack.isEmpty() && activeStack.getItem() instanceof ItemModularHandheld) {
            ItemModularHandheld item = (ItemModularHandheld) activeStack.getItem();
            Arrays.stream(abilities).filter(ability -> ability.canPerform(player, item, activeStack, target, targetPos, ticksUsed)).findFirst().ifPresent(ability -> ability.perform(player, hand, item, activeStack, target, targetPos, hitVec, ticksUsed));
            player.m_5810_();
        }
    }

    public static void handleSecondaryAbility(Player player, InteractionHand hand, @Nullable LivingEntity target) {
        ItemStack activeStack = player.m_21120_(hand);
        if (!activeStack.isEmpty() && activeStack.getItem() instanceof ItemModularHandheld item && target != null) {
            item.itemInteractionForEntitySecondary(activeStack, player, target, hand);
            player.m_5810_();
            player.getCooldowns().addCooldown(item, (int) Math.round(item.getCooldownBase(activeStack) * 20.0 * 1.5));
            player.m_6674_(hand);
        }
    }

    public static double getCounterWeightBonus(int counterWeightLevel, int integrityCost) {
        return Math.max(0.0, 0.15 - (double) Math.abs(counterWeightLevel - integrityCost) * 0.05);
    }

    public static double getAttackSpeedHarvestModifier(double attackSpeed) {
        return attackSpeed * 0.5 + 0.5;
    }

    public static double getEfficiencyEnchantmentBonus(int level) {
        return level > 0 ? (double) (level * level + 1) : 0.0;
    }

    public int getBlockDestroyDamage() {
        return this.blockDestroyDamage;
    }

    public int getEntityHitDamage() {
        return this.entityHitDamage;
    }

    public boolean isDamageable(ItemStack stack) {
        return this.getMaxDamage(stack) > 0;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if (state.m_60800_(world, pos) > 0.0F) {
            this.applyDamage(this.blockDestroyDamage, itemStack, entity);
            if (!this.isBroken(itemStack)) {
                this.applyUsageEffects(entity, itemStack, 1.0);
            }
        }
        this.applyBlockBreakEffects(itemStack, world, state, pos, entity);
        if (!world.isClientSide && !this.isBroken(itemStack)) {
            if (this.getEffectLevel(itemStack, ItemEffect.piercingHarvest) > 0) {
                PiercingEffect.pierceBlocks(this, itemStack, this.getEffectLevel(itemStack, ItemEffect.piercing), (ServerLevel) world, state, pos, entity);
            }
            int extractorLevel = this.getEffectLevel(itemStack, ItemEffect.extraction);
            if (extractorLevel > 0) {
                ExtractionEffect.breakBlocks(this, itemStack, extractorLevel, (ServerLevel) world, state, pos, entity);
            }
            int unboundExtractionLevel = this.getEffectLevel(itemStack, ItemEffect.unboundExtraction);
            if (unboundExtractionLevel > 0) {
                UnboundExtractionEffect.breakBlocks(this, itemStack, unboundExtractionLevel, (ServerLevel) world, state, pos, entity);
            }
            CritEffect.onBlockBreak(entity);
        }
        return true;
    }

    public void applyBlockBreakEffects(ItemStack itemStack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!world.isClientSide) {
            int intuitLevel = this.getEffectLevel(itemStack, ItemEffect.intuit);
            if (intuitLevel > 0) {
                int xp = state.getExpDrop(world, world.getRandom(), pos, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, itemStack), EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack));
                if (xp > 0) {
                    this.tickHoningProgression(entity, itemStack, xp * intuitLevel);
                }
            }
            int jankLevel = this.getEffectLevel(itemStack, ItemEffect.janking);
            if (jankLevel > 0) {
                JankEffect.jankItemsDelayed((ServerLevel) world, pos, jankLevel, this.getEffectEfficiency(itemStack, ItemEffect.janking), entity);
            }
            int skulkTaintLevel = this.getEffectLevel(itemStack, ItemEffect.sculkTaint);
            if (skulkTaintLevel > 0) {
                SculkTaintEffect.perform((ServerLevel) world, pos, skulkTaintLevel, this.getEffectEfficiency(itemStack, ItemEffect.sculkTaint));
            }
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        this.applyDamage(this.entityHitDamage, itemStack, attacker);
        if (!this.isBroken(itemStack)) {
            float attackStrength = (Float) CastOptional.cast(attacker, Player.class).map(EffectHelper::getCooledAttackStrength).orElse(1.0F);
            if ((double) attackStrength > 0.9) {
                int sweepingLevel = SweepingEffect.getSweepingLevel(itemStack);
                if (sweepingLevel > 0 && attacker.m_20096_() && !EffectHelper.getSprinting(attacker)) {
                    SweepingEffect.sweepAttack(itemStack, target, attacker, sweepingLevel);
                }
                int howlingLevel = EffectHelper.getEffectLevel(itemStack, ItemEffect.howling);
                if (howlingLevel > 0) {
                    HowlingEffect.trigger(itemStack, attacker, howlingLevel);
                }
                ItemEffectHandler.applyHitEffects(itemStack, target, attacker);
                this.applyPositiveUsageEffects(attacker, itemStack, 1.0);
            }
            int skulkTaintLevel = this.getEffectLevel(itemStack, ItemEffect.sculkTaint);
            if (skulkTaintLevel > 0) {
                SculkTaintEffect.perform((ServerLevel) target.m_9236_(), target.m_20183_(), skulkTaintLevel, this.getEffectEfficiency(itemStack, ItemEffect.sculkTaint));
            }
            this.applyNegativeUsageEffects(attacker, itemStack, 1.0);
        }
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        ItemStack itemStack = player.m_21120_(hand);
        BlockState blockState = world.getBlockState(pos);
        if (this.isBroken(itemStack)) {
            return InteractionResult.PASS;
        } else {
            boolean canChannel = this.getUseDuration(itemStack) > 0;
            if (!canChannel || player.m_6047_()) {
                ToolData toolData = this.getToolData(itemStack);
                Collection<ToolAction> tools = (Collection<ToolAction>) toolData.getValues().stream().filter(toolx -> toolData.getLevel(toolx) > 0).sorted(player.m_6047_() ? Comparator.comparing(ToolAction::name).reversed() : Comparator.comparing(ToolAction::name)).collect(Collectors.toList());
                for (ToolAction tool : tools) {
                    BlockState block = blockState.getToolModifiedState(context, tool, false);
                    if (block != null) {
                        if (ToolActions.AXE_STRIP.equals(tool)) {
                            world.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                        } else if (ToolActions.AXE_SCRAPE.equals(tool)) {
                            world.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            world.m_5898_(player, 3005, pos, 0);
                        } else if (ToolActions.AXE_WAX_OFF.equals(tool)) {
                            world.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                            world.m_5898_(player, 3005, pos, 0);
                        } else if (ToolActions.HOE_DIG.equals(tool)) {
                            world.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        } else if (ToolActions.SHOVEL_DIG.equals(tool)) {
                            world.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                        } else {
                            SoundEvent sound = blockState.getSoundType(world, pos, player).getHitSound();
                            world.playSound(player, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                        }
                        if (!world.isClientSide) {
                            world.setBlock(pos, block, 11);
                            this.applyDamage(this.blockDestroyDamage, context.getItemInHand(), player);
                            this.applyUsageEffects(player, itemStack, 2.0);
                        }
                        return InteractionResult.sidedSuccess(world.isClientSide);
                    }
                }
                if (tools.contains(TetraToolActions.dowse) && this.dowseBlock(player, world, blockState, pos)) {
                    this.applyDamage(this.blockDestroyDamage, itemStack, player);
                    this.applyUsageEffects(player, itemStack, 2.0);
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
                int denailingLevel = this.getEffectLevel(itemStack, ItemEffect.denailing);
                if (denailingLevel > 0 && (double) player.getAttackStrengthScale(0.0F) > 0.9 && this.denailBlock(player, world, pos, hand, facing)) {
                    this.applyDamage(this.blockDestroyDamage, itemStack, player);
                    this.applyUsageEffects(player, itemStack, 2.0);
                    player.resetAttackStrengthTicker();
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
            return super.m_6225_(context);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        if (this.getUseDuration(itemStack) > 0) {
            player.m_6672_(hand);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        } else {
            if (InteractionHand.OFF_HAND.equals(hand)) {
                int jabLevel = this.getEffectLevel(itemStack, ItemEffect.jab);
                if (jabLevel > 0) {
                    if (!world.isClientSide) {
                        if (this.getEffectLevel(itemStack, ItemEffect.truesweep) > 0 && player.m_20096_() && !player.m_20142_()) {
                            SweepingEffect.truesweep(itemStack, player, true);
                        }
                        int howlingLevel = this.getEffectLevel(itemStack, ItemEffect.howling);
                        if (howlingLevel > 0) {
                            HowlingEffect.trigger(itemStack, player, howlingLevel);
                        }
                    }
                    player.getCooldowns().addCooldown(this, (int) Math.round(this.getCooldownBase(itemStack) * 20.0));
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
                }
            }
            return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.getCooldowns().isOnCooldown(this) && !this.isBroken(itemStack) && (this.getUseDuration(itemStack) == 0 || player.m_6047_())) {
            int bashingLevel = this.getEffectLevel(itemStack, ItemEffect.bashing);
            if (bashingLevel > 0) {
                this.bashEntity(itemStack, bashingLevel, player, target);
                this.tickProgression(player, itemStack, 2);
                this.applyDamage(2, itemStack, player);
                return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
            }
            int pryLevel = this.getEffectLevel(itemStack, ItemEffect.pry);
            if (pryLevel > 0) {
                PryEffect.perform(player, hand, this, itemStack, pryLevel, target);
                return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
            }
            if (InteractionHand.OFF_HAND.equals(hand)) {
                int jabLevel = this.getEffectLevel(itemStack, ItemEffect.jab);
                if (jabLevel > 0) {
                    this.jabEntity(itemStack, jabLevel, player, target);
                    if (!player.m_9236_().isClientSide) {
                        if (this.getEffectLevel(itemStack, ItemEffect.truesweep) > 0 && player.m_20096_() && !player.m_20142_()) {
                            SweepingEffect.truesweep(itemStack, player, true);
                        }
                        int howlingLevel = this.getEffectLevel(itemStack, ItemEffect.howling);
                        if (howlingLevel > 0) {
                            HowlingEffect.trigger(itemStack, player, howlingLevel);
                        }
                    }
                    this.tickProgression(player, itemStack, 2);
                    this.applyDamage(2, itemStack, player);
                    return InteractionResult.sidedSuccess(player.m_9236_().isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    public boolean itemInteractionForEntitySecondary(ItemStack itemStack, Player player, LivingEntity target, InteractionHand hand) {
        int bashingLevel = this.getEffectLevel(itemStack, ItemEffect.bashing);
        if (bashingLevel > 0) {
            this.bashEntity(itemStack, bashingLevel, player, target);
            this.tickProgression(player, itemStack, 2);
            this.applyDamage(2, itemStack, player);
            return true;
        } else {
            return false;
        }
    }

    public AbilityUseResult hitEntity(ItemStack itemStack, Player player, LivingEntity target, double damageMultiplier, float knockbackBase, float knockbackMultiplier) {
        return this.hitEntity(itemStack, player, target, damageMultiplier, 0.0, knockbackBase, knockbackMultiplier);
    }

    public AbilityUseResult hitEntity(ItemStack itemStack, Player player, LivingEntity target, double damageMultiplier, double damageBonus, float knockbackBase, float knockbackMultiplier) {
        float targetModifier = EnchantmentHelper.getDamageBonus(itemStack, target.getMobType());
        float critMultiplier = (Float) Optional.ofNullable(ForgeHooks.getCriticalHit(player, target, false, 1.5F)).map(CriticalHitEvent::getDamageModifier).orElse(1.0F);
        double damage = (1.0 + this.getAbilityBaseDamage(itemStack) + (double) targetModifier) * (double) critMultiplier * damageMultiplier + damageBonus;
        boolean success = target.hurt(player.m_269291_().playerAttack(player), (float) damage);
        if (success) {
            EnchantmentHelper.doPostHurtEffects(target, player);
            EffectHelper.applyEnchantmentHitEffects(itemStack, target, player);
            ItemEffectHandler.applyHitEffects(itemStack, target, player);
            float knockbackFactor = knockbackBase + (float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, itemStack);
            target.knockback((double) (knockbackFactor * knockbackMultiplier), player.m_20185_() - target.m_20185_(), player.m_20189_() - target.m_20189_());
            if (targetModifier > 1.0F) {
                player.magicCrit(target);
                return AbilityUseResult.magicCrit;
            } else if (critMultiplier > 1.0F) {
                player.crit(target);
                return AbilityUseResult.crit;
            } else {
                return AbilityUseResult.hit;
            }
        } else {
            return AbilityUseResult.fail;
        }
    }

    public void jabEntity(ItemStack itemStack, int jabLevel, Player player, LivingEntity target) {
        AbilityUseResult result = this.hitEntity(itemStack, player, target, (double) ((float) jabLevel / 100.0F), 0.5F, 0.2F);
        if (result == AbilityUseResult.crit) {
            player.m_20193_().playSound(player, target.m_20183_(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.3F);
        } else {
            player.m_20193_().playSound(player, target.m_20183_(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 1.3F);
        }
        player.getCooldowns().addCooldown(this, (int) Math.round(this.getCooldownBase(itemStack) * 20.0));
    }

    public void bashEntity(ItemStack itemStack, int bashingLevel, Player player, LivingEntity target) {
        AbilityUseResult result = this.hitEntity(itemStack, player, target, 1.0, (float) (bashingLevel + (player.m_20142_() ? 1 : 0)), 0.5F);
        if (result != AbilityUseResult.fail) {
            double stunDuration = (double) this.getEffectEfficiency(itemStack, ItemEffect.bashing);
            if (stunDuration > 0.0) {
                target.addEffect(new MobEffectInstance(StunPotionEffect.instance, (int) Math.round(stunDuration * 20.0), 0, false, false));
            }
            player.m_20193_().playSound(player, target.m_20183_(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0F, 0.7F);
        } else {
            player.m_20193_().playSound(player, target.m_20183_(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0F, 0.7F);
        }
        player.getCooldowns().addCooldown(this, (int) Math.round(this.getCooldownBase(itemStack) * 20.0));
    }

    public void throwItem(Player player, ItemStack stack, int riptideLevel, float cooldownBase) {
        Level world = player.m_9236_();
        if (!world.isClientSide) {
            this.applyDamage(1, stack, player);
            this.applyUsageEffects(player, stack, 1.0);
            ThrownModularItemEntity projectileEntity = new ThrownModularItemEntity(world, player, stack);
            if (player.getAbilities().instabuild) {
                projectileEntity.f_36705_ = AbstractArrow.Pickup.CREATIVE_ONLY;
            } else {
                player.getInventory().removeItem(stack);
            }
            projectileEntity.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, 2.5F + (float) riptideLevel * 0.5F, 1.0F);
            world.m_7967_(projectileEntity);
            if (this instanceof ModularSingleHeadedItem) {
                world.playSound(null, projectileEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            } else if (this instanceof ModularShieldItem) {
                world.playSound(null, projectileEntity, SoundEvents.DISPENSER_LAUNCH, SoundSource.PLAYERS, 1.0F, 2.0F);
            } else {
                world.playSound(null, projectileEntity, SoundEvents.FISHING_BOBBER_THROW, SoundSource.PLAYERS, 1.0F, 0.7F);
            }
        }
        player.getCooldowns().addCooldown(this, Math.round(cooldownBase * 20.0F));
    }

    public void causeRiptideEffect(Player player, int riptideLevel) {
        float yaw = player.m_146908_();
        float pitch = player.m_146909_();
        float x = -Mth.sin(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
        float y = -Mth.sin(pitch * (float) (Math.PI / 180.0));
        float z = Mth.cos(yaw * (float) (Math.PI / 180.0)) * Mth.cos(pitch * (float) (Math.PI / 180.0));
        float velocityMultiplier = 3.0F * ((1.0F + (float) riptideLevel) / 4.0F);
        x *= velocityMultiplier;
        y *= velocityMultiplier;
        z *= velocityMultiplier;
        player.m_5997_((double) x, (double) y, (double) z);
        player.startAutoSpinAttack(20);
        if (player.m_20096_()) {
            player.m_6478_(MoverType.SELF, new Vec3(0.0, 1.1999999, 0.0));
        }
        SoundEvent soundEvent;
        if (riptideLevel >= 3) {
            soundEvent = SoundEvents.TRIDENT_RIPTIDE_3;
        } else if (riptideLevel == 2) {
            soundEvent = SoundEvents.TRIDENT_RIPTIDE_2;
        } else {
            soundEvent = SoundEvents.TRIDENT_RIPTIDE_1;
        }
        player.m_9236_().playSound(null, player, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    public boolean dowseBlock(Player player, Level world, BlockState blockState, BlockPos pos) {
        if (blockState.m_60734_() instanceof CampfireBlock && (Boolean) blockState.m_61143_(CampfireBlock.LIT)) {
            CampfireBlock.dowse(player, world, pos, blockState);
            if (!world.isClientSide()) {
                world.m_5898_(null, 1009, pos, 0);
                world.setBlock(pos, (BlockState) blockState.m_61124_(CampfireBlock.LIT, false), 11);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean denailBlock(Player player, Level world, BlockPos pos, InteractionHand hand, Direction facing) {
        ItemStack itemStack = player.m_21120_(hand);
        if (!player.mayUseItemAt(pos.relative(facing), facing, itemStack)) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(pos);
            if (canDenail(blockState)) {
                boolean success = EffectHelper.breakBlock(world, player, player.m_21120_(hand), pos, blockState, true, false);
                if (success) {
                    player.resetAttackStrengthTicker();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        if (this.getEffectLevel(stack, ItemEffect.blocking) > 0) {
            return UseAnim.BLOCK;
        } else if (this.getEffectLevel(stack, ItemEffect.throwable) <= 0 && EnchantmentHelper.getRiptide(stack) <= 0) {
            ChargedAbilityEffect ability = this.getChargeableAbility(stack);
            return ability != null ? ability.getPose() : super.m_6164_(stack);
        } else {
            return UseAnim.SPEAR;
        }
    }

    public float getBlockProgress(ItemStack itemStack, @Nullable LivingEntity entity) {
        int blockingLevel = this.getEffectLevel(itemStack, ItemEffect.blocking);
        return blockingLevel > 0 && blockingLevel < 16 ? (Float) Optional.ofNullable(entity).filter(e -> e.getUseItemRemainingTicks() > 0).filter(e -> itemStack.equals(e.getUseItem())).map(e -> (float) e.getUseItemRemainingTicks() * 1.0F / (float) this.getUseDuration(itemStack)).orElse(0.0F) : 0.0F;
    }

    public boolean isThrowing(ItemStack itemStack, @Nullable LivingEntity entity) {
        return UseAnim.SPEAR.equals(this.getUseAnimation(itemStack)) && (Boolean) Optional.ofNullable(entity).filter(e -> itemStack.equals(e.getUseItem())).map(e -> e.getUseItemRemainingTicks() > 0).orElse(false);
    }

    public boolean isBlocking(ItemStack itemStack, @Nullable LivingEntity entity) {
        return UseAnim.BLOCK.equals(this.getUseAnimation(itemStack)) && (Boolean) Optional.ofNullable(entity).filter(e -> itemStack.equals(e.getUseItem())).map(e -> e.getUseItemRemainingTicks() > 0).orElse(false);
    }

    public boolean isShield(ItemStack itemStack) {
        return this.getEffectLevel(itemStack, ItemEffect.blocking) > 0;
    }

    public void onShieldDisabled(Player player, ItemStack itemStack) {
        player.getCooldowns().addCooldown(this, (int) (this.getCooldownBase(itemStack) * 20.0 * 0.75));
    }

    public boolean canDisableShield(ItemStack itemStack, ItemStack shieldStack, LivingEntity target, LivingEntity attacker) {
        return this.getEffectLevel(itemStack, ItemEffect.shieldbreaker) > 0;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        int blockingLevel = this.getEffectLevel(itemStack, ItemEffect.blocking);
        if (blockingLevel > 0) {
            int duration = blockingLevel * 20;
            return blockingLevel < 16 ? duration : 72000;
        } else {
            return this.getEffectLevel(itemStack, ItemEffect.throwable) <= 0 && EnchantmentHelper.getRiptide(itemStack) <= 0 && !Arrays.stream(abilities).anyMatch(ability -> ability.isAvailable(this, itemStack)) ? 0 : 72000;
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
        CastOptional.cast(entity, Player.class).ifPresent(player -> {
            int blockingLevel = this.getEffectLevel(itemStack, ItemEffect.blocking);
            if (blockingLevel > 0) {
                double blockingCooldown = (double) this.getEffectEfficiency(itemStack, ItemEffect.blocking);
                if (blockingCooldown > 0.0) {
                    player.getCooldowns().addCooldown(this, (int) Math.round(blockingCooldown * this.getCooldownBase(itemStack) * 20.0));
                }
                if (player.m_6047_() && world.isClientSide) {
                    this.onPlayerStoppedUsingSecondary(itemStack, world, entity, 0);
                }
            }
        });
        return super.m_5922_(itemStack, world, entity);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level world, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int ticksUsed = this.getUseDuration(itemStack) - timeLeft;
            double cooldownBase = this.getCooldownBase(itemStack);
            int blockingLevel = this.getEffectLevel(itemStack, ItemEffect.blocking);
            int throwingLevel = this.getEffectLevel(itemStack, ItemEffect.throwable);
            int riptideLevel = EnchantmentHelper.getRiptide(itemStack);
            if (blockingLevel > 0) {
                double blockingCooldown = (double) this.getEffectEfficiency(itemStack, ItemEffect.blocking);
                if (blockingCooldown > 0.0) {
                    player.getCooldowns().addCooldown(this, (int) Math.round(blockingCooldown * cooldownBase * 20.0));
                }
                if (player.m_6047_()) {
                    if (ticksUsed >= 10 && riptideLevel > 0 && player.m_20070_()) {
                        this.causeRiptideEffect(player, riptideLevel);
                    } else if (ticksUsed >= 10 && throwingLevel > 0) {
                        this.throwItem(player, itemStack, riptideLevel, (float) cooldownBase);
                    } else if (world.isClientSide) {
                        this.onPlayerStoppedUsingSecondary(itemStack, world, entityLiving, timeLeft);
                    }
                }
            } else {
                if (riptideLevel > 0 && ticksUsed >= 10 && player.m_20070_()) {
                    this.causeRiptideEffect(player, riptideLevel);
                } else if (throwingLevel > 0 && ticksUsed >= 10) {
                    this.throwItem(player, itemStack, riptideLevel, (float) cooldownBase);
                }
                if (world.isClientSide) {
                    this.triggerChargedAbility(itemStack, world, entityLiving, ticksUsed);
                }
            }
        }
    }

    public ChargedAbilityEffect getChargeableAbility(ItemStack itemStack) {
        return (ChargedAbilityEffect) Arrays.stream(abilities).filter(ability -> ability.canCharge(this, itemStack)).findFirst().orElse(null);
    }

    @OnlyIn(Dist.CLIENT)
    public void triggerChargedAbility(ItemStack itemStack, Level world, LivingEntity entity, int ticksUsed) {
        if (entity instanceof Player) {
            HitResult rayTrace = Minecraft.getInstance().hitResult;
            LivingEntity target = (LivingEntity) Optional.ofNullable(rayTrace).filter(rayTraceResult -> rayTraceResult.getType() == HitResult.Type.ENTITY).map(rayTraceResult -> ((EntityHitResult) rayTraceResult).getEntity()).flatMap(hitEntity -> CastOptional.cast(hitEntity, LivingEntity.class)).orElse(null);
            BlockPos targetPos = (BlockPos) Optional.ofNullable(rayTrace).filter(rayTraceResult -> rayTraceResult.getType() == HitResult.Type.BLOCK).map(rayTraceResult -> ((BlockHitResult) rayTraceResult).getBlockPos()).orElse(null);
            Vec3 hitVec = (Vec3) Optional.ofNullable(rayTrace).map(HitResult::m_82450_).orElse(null);
            InteractionHand activeHand = entity.getUsedItemHand();
            TetraMod.packetHandler.sendToServer(new ChargedAbilityPacket(target, targetPos, hitVec, activeHand, ticksUsed));
            handleChargedAbility((Player) entity, activeHand, target, targetPos, hitVec, ticksUsed);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void onPlayerStoppedUsingSecondary(ItemStack itemStack, Level world, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            LivingEntity target = (LivingEntity) Optional.ofNullable(Minecraft.getInstance().hitResult).filter(rayTraceResult -> rayTraceResult.getType() == HitResult.Type.ENTITY).map(rayTraceResult -> ((EntityHitResult) rayTraceResult).getEntity()).flatMap(hitEntity -> CastOptional.cast(hitEntity, LivingEntity.class)).orElse(null);
            InteractionHand activeHand = entity.getUsedItemHand();
            TetraMod.packetHandler.sendToServer(new SecondaryAbilityPacket(target, activeHand));
            handleSecondaryAbility(player, activeHand, target);
        }
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        EffectHelper.setCooledAttackStrength(player, player.getAttackStrengthScale(0.5F));
        EffectHelper.setSprinting(player, player.m_20142_());
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (this.isBroken(itemStack)) {
            return AttributeHelper.emptyMap;
        } else if (slot == EquipmentSlot.MAINHAND) {
            return this.getAttributeModifiersCached(itemStack);
        } else {
            return slot == EquipmentSlot.OFFHAND ? (Multimap) this.getAttributeModifiersCached(itemStack).entries().stream().filter(entry -> ((Attribute) entry.getKey()).equals(Attributes.ARMOR) || ((Attribute) entry.getKey()).equals(Attributes.ARMOR_TOUGHNESS)).collect(Multimaps.toMultimap(Entry::getKey, Entry::getValue, ArrayListMultimap::create)) : AttributeHelper.emptyMap;
        }
    }

    public double getAbilityBaseDamage(ItemStack itemStack) {
        return this.getAttributeValue(itemStack, Attributes.ATTACK_DAMAGE) + 1.0;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getEffectAttributes(ItemStack itemStack) {
        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
        Optional.of(this.getCounterWeightBonus(itemStack)).filter(bonus -> bonus > 0.0).map(bonus -> new AttributeModifier("counterweight", bonus, AttributeModifier.Operation.ADDITION)).ifPresent(modifier -> result.put(Attributes.ATTACK_SPEED, modifier));
        return result;
    }

    public double getCounterWeightBonus(ItemStack itemStack) {
        int counterWeightLevel = this.getEffectLevel(itemStack, ItemEffect.counterweight);
        if (counterWeightLevel > 0) {
            int integrityCost = IModularItem.getIntegrityCost(itemStack);
            return getCounterWeightBonus(counterWeightLevel, integrityCost);
        } else {
            return 0.0;
        }
    }

    public double getCooldownBase(ItemStack itemStack) {
        return 1.0 / Math.max(0.1, this.getAttributeValue(itemStack, Attributes.ATTACK_SPEED, 4.0) + this.getCounterWeightBonus(itemStack));
    }

    public Set<ToolAction> getToolActions(ItemStack stack) {
        return !this.isBroken(stack) ? (Set) this.getToolLevels(stack).entrySet().stream().filter(entry -> (Integer) entry.getValue() > 0).map(Entry::getKey).collect(Collectors.toUnmodifiableSet()) : Collections.emptySet();
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        if (this.getToolActions(stack).contains(toolAction)) {
            return true;
        } else {
            return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction) && this.isShield(stack) ? true : super.canPerformAction(stack, toolAction);
        }
    }

    public int getHarvestTier(ItemStack stack, ToolAction tool) {
        if (!this.isBroken(stack)) {
            int toolTier = this.getToolLevel(stack, tool);
            if (toolTier > 0) {
                return toolTier - 1;
            }
        }
        return -1;
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return !state.m_60834_() ? true : ToolActionHelper.getAppropriateTools(state).stream().map(requiredTool -> this.getHarvestTier(stack, requiredTool)).map(TierHelper::getTier).filter(Objects::nonNull).anyMatch(tier -> TierSortingRegistry.isCorrectTierForDrops(tier, state));
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        if (!this.isBroken(itemStack)) {
            float speed = (float) getAttackSpeedHarvestModifier(this.getAttributeValue(itemStack, Attributes.ATTACK_SPEED, 4.0));
            Set<ToolAction> appropriateTools = ToolActionHelper.getAppropriateTools(blockState);
            if (!appropriateTools.isEmpty()) {
                speed *= (float) appropriateTools.stream().mapToDouble(tool -> (double) this.getToolEfficiency(itemStack, tool)).max().orElse(0.0);
            } else {
                speed *= this.getToolActions(itemStack).stream().filter(toolAction -> ToolActionHelper.isEffectiveOn(toolAction, blockState)).map(toolAction -> this.getToolEfficiency(itemStack, toolAction)).max(Comparator.naturalOrder()).orElse(0.0F);
            }
            if (this.getToolLevel(itemStack, TetraToolActions.cut) > 0) {
                if (blockState.m_60734_().equals(Blocks.COBWEB)) {
                    speed *= 10.0F;
                }
                if (blockState.m_60734_().equals(Blocks.BAMBOO)) {
                    speed = 30.0F;
                }
            }
            return speed < 1.0F ? 1.0F : speed;
        } else {
            return 1.0F;
        }
    }

    @Override
    public ItemStack onCraftConsume(ItemStack providerStack, ItemStack targetStack, Player player, ToolAction tool, int toolLevel, boolean consumeResources) {
        if (consumeResources) {
            this.applyDamage(toolLevel, providerStack, player);
            this.applyUsageEffects(player, providerStack, (double) (10 + toolLevel * 5));
        }
        return super.onCraftConsume(providerStack, targetStack, player, tool, toolLevel, consumeResources);
    }

    @Override
    public ItemStack onActionConsume(ItemStack providerStack, ItemStack targetStack, Player player, ToolAction tool, int toolLevel, boolean consumeResources) {
        if (consumeResources) {
            this.applyDamage(toolLevel, providerStack, player);
            this.applyUsageEffects(player, providerStack, (double) (4 + toolLevel * 3));
        }
        return super.onCraftConsume(providerStack, targetStack, player, tool, toolLevel, consumeResources);
    }
}
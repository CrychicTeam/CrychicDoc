package com.mna.spells;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.ISpellInteractibleBlock;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.entities.ISpellInteractibleEntity;
import com.mna.api.events.ComponentApplyingEvent;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.ISpellHelper;
import com.mna.api.spells.SpellCastingResult;
import com.mna.api.spells.SpellCastingResultCode;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.base.SpellBlacklistResult;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.chunkdata.ChunkMagicProvider;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.targeting.SpellProjectile;
import com.mna.entities.utility.SpellFX;
import com.mna.events.EventDispatcher;
import com.mna.factions.Factions;
import com.mna.inventory.InventoryRitualKit;
import com.mna.items.ItemInit;
import com.mna.items.artifice.charms.ItemContingencyCharm;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.network.ServerMessageDispatcher;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;
import top.theillusivec4.curios.api.CuriosApi;

public class SpellCaster implements ISpellHelper {

    private static ArrayList<SpellAdjuster> _adjusters = new ArrayList();

    private static ArrayList<Item> _cooldownItems = new ArrayList();

    public static SpellCastingResult PlayerCast(ItemStack stack, Player caster, InteractionHand hand, Vec3 casterPosition, Vec3 casterLook, Level world, boolean consumeMana) {
        try {
            IPlayerMagic magic = null;
            IPlayerProgression progression = null;
            if (caster.m_21124_(EffectInit.SILENCE.get()) != null) {
                return SpellCastingResultCode.SILENCED.createResult();
            } else {
                magic = (IPlayerMagic) caster.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                progression = (IPlayerProgression) caster.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                if (magic == null || progression == null) {
                    return SpellCastingResultCode.CAPABILITY_MISSING.createResult();
                } else if (!magic.isMagicUnlocked() && consumeMana) {
                    if (!world.isClientSide) {
                        caster.m_213846_(Component.translatable("item.mna.spell.no-magic"));
                    }
                    return SpellCastingResultCode.NOT_UNLOCKED_MAGIC.createResult();
                } else {
                    SpellRecipe recipe = SpellRecipe.fromNBT(stack.getTag());
                    if (!recipe.isValid()) {
                        return SpellCastingResultCode.INVALID_RECIPE.createResult();
                    } else if (consumeMana && !caster.isCreative() && recipe.getTier(world) > progression.getTier()) {
                        if (!world.isClientSide) {
                            caster.m_213846_(Component.translatable("item.mna.spell.tier-fail"));
                        }
                        return SpellCastingResultCode.INSUFFICIENT_TIER.createResult();
                    } else if (consumeMana && !caster.isCreative() && recipe.getComplexity() > (float) progression.getTierMaxComplexity()) {
                        if (!world.isClientSide) {
                            caster.m_213846_(Component.translatable("item.mna.spell.complexity-fail"));
                        }
                        return SpellCastingResultCode.TOO_COMPLEX.createResult();
                    } else {
                        HashMap<Item, Boolean> missing = checkReagents(caster, hand, recipe);
                        if (consumeMana && !caster.isCreative()) {
                            List<Entry<Item, Boolean>> missingRequired = (List<Entry<Item, Boolean>>) missing.entrySet().stream().filter(e -> !(Boolean) e.getValue()).collect(Collectors.toList());
                            if (missingRequired.size() > 0) {
                                if (!world.isClientSide) {
                                    if (missing.size() > 1) {
                                        caster.m_213846_(Component.translatable("item.mna.spell.reagents-missing.multi"));
                                    } else {
                                        caster.m_213846_(Component.translatable("item.mna.spell.reagents-missing.single").append(Component.translatable(((Item) ((Entry) missingRequired.get(0)).getKey()).getDescriptionId())));
                                    }
                                }
                                return SpellCastingResultCode.MISSING_REAGENTS.createResult();
                            }
                        }
                        if (!EventDispatcher.DispatchSpellCast(recipe, caster)) {
                            return SpellCastingResultCode.CANCELED_BY_EVENT.createResult();
                        } else {
                            if (caster instanceof ServerPlayer) {
                                CustomAdvancementTriggers.CAST_SPELL.trigger((ServerPlayer) caster, recipe);
                            }
                            applyAdjusters(stack, caster, recipe, SpellCastStage.CASTING);
                            if (!world.isClientSide && recipe.isMysterious()) {
                                recipe.setMysterious(false);
                                recipe.writeToNBT(stack.getOrCreateTag());
                            }
                            float manaCost = recipe.getManaCost();
                            if (!caster.isCreative() && !magic.getCastingResource().hasEnough(caster, manaCost) && consumeMana) {
                                return SpellCastingResultCode.NOT_ENOUGH_MANA.createResult();
                            } else {
                                SpellContext context = new SpellContext(world, recipe);
                                context.setMissingReagents(missing.keySet().stream().toList());
                                SpellCastingResult subCast = Affect(stack, recipe, world, new SpellSource(caster, hand), null, context);
                                if (!subCast.getCode().isConsideredSuccess()) {
                                    return subCast;
                                } else {
                                    if (consumeMana) {
                                        if (!recipe.canFactionCraft(progression)) {
                                            recipe.usedByPlayer(caster);
                                        }
                                        if (recipe.getShape().getPart().isChanneled()) {
                                            magic.getCastingResource().consume(caster, manaCost * 0.25F);
                                        } else {
                                            magic.getCastingResource().consume(caster, manaCost);
                                            AddAffinityAndMagicXP(recipe, caster);
                                        }
                                        if (caster instanceof ServerPlayer && magic.getCastingResource().getAmount() <= magic.getCastingResource().getMaxAmount() * 0.1F) {
                                            ItemContingencyCharm.CheckAndConsumeCharmCharge((ServerPlayer) caster, ItemContingencyCharm.ContingencyEvent.LOW_CASTING_RESOURCE);
                                        }
                                        consumeReagents(caster, hand, recipe, subCast);
                                    }
                                    return subCast;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable var15) {
            try {
                caster.m_213846_(Component.translatable("item.mna.spell.fatal_error"));
            } catch (Throwable var14) {
                ManaAndArtifice.LOGGER.error("Error notifying caster about spell cast failure:");
                ManaAndArtifice.LOGGER.error(var14);
            }
            ManaAndArtifice.LOGGER.error(var15);
            return SpellCastingResultCode.EXCEPTION_THROWN.createResult();
        }
    }

    public static SpellCastingResult Affect(@Nullable ItemStack stack, ISpellDefinition recipe, Level world, SpellSource source) {
        return Affect(stack, recipe, world, source, null, null);
    }

    public static SpellCastingResult Affect(@Nullable ItemStack stack, ISpellDefinition recipe, Level world, SpellSource source, SpellTarget targetHint) {
        return Affect(stack, recipe, world, source, targetHint, null);
    }

    public static SpellCastingResult Affect(@Nullable ItemStack stack, ISpellDefinition recipe, Level world, SpellSource source, SpellTarget targetHint, SpellContext context) {
        try {
            if (!recipe.isValid()) {
                return SpellCastingResultCode.INVALID_RECIPE.createResult();
            } else {
                Shape shape = recipe.getShape().getPart();
                if (shape != null && recipe.countComponents() != 0) {
                    SpellBlacklistResult result = shape.canBeCastAt(world, source.getOrigin());
                    if (result != SpellBlacklistResult.ALLOWED) {
                        if (source.isPlayerCaster() && !world.isClientSide) {
                            source.getPlayer().m_213846_(Component.translatable(result.getMessageTranslationKey().toString()));
                        }
                        return SpellCastingResultCode.BLOCKED_BY_CONFIG.createResult();
                    } else {
                        for (int i = 0; i < recipe.countComponents(); i++) {
                            result = recipe.getComponent(i).getPart().canBeCastAt(world, source.getOrigin());
                            if (result != SpellBlacklistResult.ALLOWED) {
                                if (source.isPlayerCaster() && !world.isClientSide) {
                                    source.getPlayer().m_213846_(Component.translatable(result.getMessageTranslationKey().toString()));
                                }
                                return SpellCastingResultCode.BLOCKED_BY_CONFIG.createResult();
                            }
                        }
                        List<SpellTarget> targets = null;
                        if (targetHint != null) {
                            targets = shape.TargetNPCCast(source, world, recipe.getShape(), recipe, targetHint);
                        }
                        if (targetHint == null || targets == null || targets.size() == 0 || targets.get(0) == SpellTarget.NPC_CAST_ASSIST_NOT_IMPLEMENTED) {
                            targets = shape.Target(source, world, recipe.getShape(), recipe);
                        }
                        if (targets != null && targets.size() != 0 && targets.get(0) != SpellTarget.NONE) {
                            if (!world.isClientSide && !((ServerLevel) world).getServer().isPvpAllowed() && recipe.isHarmful()) {
                                MutableBoolean pvpdenied = new MutableBoolean(false);
                                recipe.iterateComponents(c -> {
                                    if (((SpellEffect) c.getPart()).getUseTag() == SpellPartTags.HARMFUL) {
                                        pvpdenied.setTrue();
                                    }
                                });
                                if (pvpdenied.booleanValue()) {
                                    targets = (List<SpellTarget>) targets.stream().filter(tx -> !tx.isEntity() || !(tx.getEntity() instanceof Player) || tx.getEntity() == source.getCaster()).collect(Collectors.toList());
                                }
                                if (targets.size() == 0) {
                                    return SpellCastingResultCode.NO_TARGET.createResult();
                                }
                            }
                            SoundEvent spellSound = SFX.Spell.Cast.ForAffinity(recipe.getHighestAffinity());
                            if (stack != null) {
                                spellSound = NameProcessors.checkAndOverrideSound(recipe, stack.getHoverName().getString(), spellSound);
                            }
                            if (spellSound != null && source.getCaster() != null) {
                                float pitch = (float) (0.9F + Math.random() * 0.1F);
                                MobEffectInstance efct = source.getCaster().getEffect(EffectInit.REDUCE.get());
                                if (efct != null) {
                                    float pitchChange = 0.1F * (float) efct.getAmplifier();
                                    pitch += pitchChange;
                                }
                                efct = source.getCaster().getEffect(EffectInit.ENLARGE.get());
                                if (efct != null) {
                                    float pitchChange = 0.05F * (float) efct.getAmplifier();
                                    pitch -= pitchChange;
                                }
                                world.playSound(null, source.getCaster(), spellSound, SoundSource.PLAYERS, 0.5F, pitch);
                            }
                            if (!world.isClientSide) {
                                world.getChunkAt(BlockPos.containing(source.getOrigin())).getCapability(ChunkMagicProvider.MAGIC).ifPresent(cm -> cm.addResidualMagic(recipe.getManaCost()));
                            }
                            if (!shape.spawnsTargetEntity()) {
                                if (targets.size() == 1) {
                                    SpellTarget target = (SpellTarget) targets.get(0);
                                    BlockState state = world.getBlockState(target.getBlock());
                                    if (state.m_60734_() instanceof ISpellInteractibleBlock && ((ISpellInteractibleBlock) state.m_60734_()).onHitBySpell(world, target.getBlock(), recipe)) {
                                        return SpellCastingResultCode.SPELL_INTERACTIBLE_BLOCK_HIT.createResult();
                                    }
                                    if (target.isEntity() && target.getEntity() instanceof ISpellInteractibleEntity) {
                                        boolean cancel = ((ISpellInteractibleEntity) target.getEntity()).onShapeTarget(recipe, source);
                                        if (cancel) {
                                            return SpellCastingResultCode.SPELL_INTERACTIBLE_ENTITY_HIT.createResult();
                                        }
                                    }
                                }
                                HashMap<SpellEffect, ComponentApplicationResult> mergedComponentResults = new HashMap();
                                if (!world.isClientSide) {
                                    SpellContext iteratorContext = context == null ? new SpellContext(world, recipe) : context;
                                    targets.forEach(targetx -> {
                                        HashMap<SpellEffect, ComponentApplicationResult> componentResults = ApplyComponents(recipe, source, targetx, iteratorContext);
                                        mergeComponentResults(mergedComponentResults, componentResults);
                                        List<SpellEffect> appliedEffects = (List<SpellEffect>) componentResults.entrySet().stream().map(e -> e.getValue() == ComponentApplicationResult.SUCCESS ? (SpellEffect) e.getKey() : null).filter(e -> e != null).collect(Collectors.toList());
                                        if (appliedEffects.size() > 0) {
                                            spawnClientFX(world, targetx.getPosition(), Vec3.ZERO, source, appliedEffects);
                                        }
                                    });
                                }
                                return mergedComponentResults.values().stream().anyMatch(c -> c == ComponentApplicationResult.SUCCESS || c == ComponentApplicationResult.DELAYED) ? SpellCastingResultCode.SUCCESS.createResult(mergedComponentResults) : SpellCastingResultCode.NO_TARGET.createResult(mergedComponentResults);
                            } else {
                                HashMap<SpellEffect, ComponentApplicationResult> targetResult = new HashMap();
                                for (IModifiedSpellPart<SpellEffect> c : recipe.getComponents()) {
                                    targetResult.put(c.getPart(), ComponentApplicationResult.TARGET_ENTITY_SPAWNED);
                                }
                                return shape.isChanneled() ? SpellCastingResultCode.CHANNEL.createResult(targetResult) : SpellCastingResultCode.SUCCESS.createResult(targetResult);
                            }
                        } else {
                            return SpellCastingResultCode.NO_TARGET.createResult();
                        }
                    }
                } else {
                    return SpellCastingResultCode.INVALID_RECIPE.createResult();
                }
            }
        } catch (Throwable var14) {
            try {
                if (source.getCaster() != null && source.getCaster() instanceof Player) {
                    ((Player) source.getCaster()).m_213846_(Component.translatable("item.mna.spell.fatal_error"));
                }
            } catch (Throwable var13) {
                ManaAndArtifice.LOGGER.error("Error notifying caster about spell cast failure:");
                ManaAndArtifice.LOGGER.error(var13);
            }
            ManaAndArtifice.LOGGER.error(var14);
            return SpellCastingResultCode.EXCEPTION_THROWN.createResult();
        }
    }

    public static void mergeComponentResults(HashMap<SpellEffect, ComponentApplicationResult> mainList, HashMap<SpellEffect, ComponentApplicationResult> toMerge) {
        toMerge.forEach((c, r) -> {
            if (mainList.containsKey(c)) {
                if (r.is_success) {
                    mainList.put(c, r);
                }
            } else {
                mainList.put(c, r);
            }
        });
    }

    public static HashMap<SpellEffect, ComponentApplicationResult> ApplyComponents(ISpellDefinition recipe, SpellSource source, SpellTarget target, SpellContext context) {
        HashMap<SpellEffect, ComponentApplicationResult> results = new HashMap();
        if (!context.getServerLevel().getServer().isPvpAllowed() && recipe.isHarmful()) {
            MutableBoolean pvpdenied = new MutableBoolean(false);
            recipe.iterateComponents(c -> {
                if (((SpellEffect) c.getPart()).getUseTag() == SpellPartTags.HARMFUL) {
                    pvpdenied.setTrue();
                }
            });
            if (pvpdenied.booleanValue() && target.isEntity() && target.getEntity() instanceof Player && target.getEntity() != source.getCaster()) {
                recipe.iterateComponents(c -> results.put((SpellEffect) c.getPart(), ComponentApplicationResult.FAIL));
                return results;
            }
        }
        recipe.iterateComponents(c -> {
            int delayInSeconds = (int) (c.getValue(Attribute.DELAY) * 20.0F);
            if (!MinecraftForge.EVENT_BUS.post(new ComponentApplyingEvent(source, context, target, (SpellEffect) c.getPart()))) {
                if (target.isBlock() && ((SpellEffect) c.getPart()).targetsBlocks() && !context.hasBlockBeenAffected((SpellEffect) c.getPart(), target.getBlock())) {
                    if (delayInSeconds == 0) {
                        ComponentApplicationResult cRes = null;
                        try {
                            cRes = ((SpellEffect) c.getPart()).ApplyEffect(source, target, c, context);
                        } catch (Throwable var9) {
                            ManaAndArtifice.LOGGER.error(var9);
                            cRes = ComponentApplicationResult.FAIL;
                        }
                        results.put((SpellEffect) c.getPart(), cRes);
                        context.addAffectedBlock((SpellEffect) c.getPart(), target.getBlock());
                    } else {
                        DelayedEventQueue.pushEvent(context.getServerLevel(), new TimedDelayedSpellEffect(context.getServerLevel().m_46467_() + "", delayInSeconds, source, target, c, context));
                        results.put((SpellEffect) c.getPart(), ComponentApplicationResult.DELAYED);
                    }
                } else if (target.isEntity() && ((SpellEffect) c.getPart()).targetsEntities() && !context.hasEntityBeenAffected((SpellEffect) c.getPart(), target.getEntity())) {
                    if (delayInSeconds == 0) {
                        ComponentApplicationResult cRes = null;
                        try {
                            cRes = ((SpellEffect) c.getPart()).ApplyEffect(source, target, c, context);
                        } catch (Throwable var8) {
                            ManaAndArtifice.LOGGER.error(var8);
                            cRes = ComponentApplicationResult.FAIL;
                        }
                        results.put((SpellEffect) c.getPart(), cRes);
                        context.addAffectedEntity((SpellEffect) c.getPart(), target.getEntity());
                    } else {
                        DelayedEventQueue.pushEvent(context.getServerLevel(), new TimedDelayedSpellEffect(context.getServerLevel().m_46467_() + "", delayInSeconds, source, target, c, context));
                        results.put((SpellEffect) c.getPart(), ComponentApplicationResult.DELAYED);
                    }
                }
            }
        });
        Level world = context.getServerLevel();
        if (!world.isClientSide && !recipe.getShape().getPart().isChanneled()) {
            List<SpellEffect> appliedEffects = (List<SpellEffect>) results.entrySet().stream().map(e -> e.getValue() == ComponentApplicationResult.SUCCESS ? (SpellEffect) e.getKey() : null).filter(e -> e != null).collect(Collectors.toList());
            if (appliedEffects.size() > 0) {
                spawnClientFX(context.getServerLevel(), target.getPosition(), Vec3.ZERO, source, appliedEffects);
            }
        }
        return results;
    }

    public static void AddAffinityAndMagicXP(SpellRecipe recipe, Player caster) {
        AddAffinityAndMagicXP(recipe, caster, -1);
    }

    public static void AddAffinityAndMagicXP(SpellRecipe recipe, Player caster, int channelDuration) {
        if (caster != null && recipe != null && recipe.isValid()) {
            caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                caster.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(progression -> {
                    int xp = (int) recipe.getComplexity();
                    float affinity = 0.1F;
                    if (channelDuration > -1) {
                        int maxChannelTime = recipe.getShape().getPart().maxChannelTime(recipe.getShape());
                        xp = (int) ((float) xp * ((float) channelDuration / (float) Math.max(1, maxChannelTime)));
                        affinity *= (float) channelDuration / (float) Math.max(1, maxChannelTime);
                    }
                    magic.addMagicXP(xp, caster, progression);
                    if (recipe.getShape().getPart().grantComponentRoteXP() && !CuriosApi.getCuriosHelper().findFirstCurio(caster, ItemInit.BELT_AFFINITY_LOCK.get()).isPresent()) {
                        for (Entry<Affinity, Float> e : recipe.getAffinity().entrySet()) {
                            magic.shiftAffinity(caster, (Affinity) e.getKey(), affinity * (Float) e.getValue());
                        }
                    }
                });
                if (!caster.m_9236_().isClientSide()) {
                    caster.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(rote -> {
                        if (rote.addRoteXP(caster, recipe.getShape().getPart(), channelDuration == -1 ? 1.0F : (float) channelDuration)) {
                            sendRoteMessage(caster, recipe.getShape().getPart());
                        }
                        if (recipe.getShape().getPart().grantComponentRoteXP()) {
                            recipe.iterateComponents(c -> addComponentXP(caster, magic, rote, (SpellEffect) c.getPart()));
                        }
                        for (int i = 0; i < recipe.countModifiers(); i++) {
                            if (rote.addRoteXP(caster, recipe.getModifier(i))) {
                                sendRoteMessage(caster, recipe.getModifier(i));
                            }
                        }
                    });
                }
            });
        }
    }

    public static void addComponentRoteProgress(Player caster, SpellEffect c) {
        addComponentXP(caster, (IPlayerMagic) caster.getCapability(PlayerMagicProvider.MAGIC).orElse(null), (IPlayerRoteSpells) caster.getCapability(PlayerRoteSpellsProvider.ROTE).orElse(null), c);
    }

    public static void addComponentXP(Player caster, IPlayerMagic magic, IPlayerRoteSpells rote, SpellEffect c) {
        float mainDepth = Math.min(magic.getAffinityDepth(c.getAffinity()), 90.0F);
        float oppositeDepth = Math.min(magic.getAffinityDepth(c.getAffinity().getOpposite()), 90.0F);
        float bonus = mainDepth / 100.0F;
        float detriment = oppositeDepth / 100.0F;
        float xpAdd = 1.0F + bonus - detriment;
        if (rote.addRoteXP(caster, c, xpAdd)) {
            sendRoteMessage(caster, c);
        }
    }

    public static void sendRoteMessage(Player caster, ISpellComponent component) {
        MutableComponent roteMessage = Component.translatable("event.mna.spell_part_rote", Component.translatable(component.getRegistryName().toString())).withStyle(ChatFormatting.GOLD);
        caster.m_9236_().playSound(null, caster.m_20185_(), caster.m_20186_(), caster.m_20189_(), SFX.Event.Player.SPELL_CREATED, SoundSource.PLAYERS, 1.0F, 1.0F);
        caster.m_213846_(roteMessage);
    }

    public static void spawnClientFX(Level world, Vec3 position, Vec3 normal, SpellSource source, SpellEffect... effects) {
        spawnClientFX(world, position, normal, source, Arrays.asList(effects));
    }

    public static void spawnClientFX(Level world, Vec3 position, Vec3 normal, SpellSource source, List<SpellEffect> effects) {
        if (world.isLoaded(BlockPos.containing(position))) {
            SpellRecipe recipe = new SpellRecipe();
            effects.forEach(e -> recipe.addComponent(e));
            SpellFX fx = new SpellFX(EntityInit.SPELL_FX.get(), world);
            fx.setCasterUUID(source.getPlayer());
            fx.setRecipe(recipe);
            fx.m_6034_(position.x, position.y, position.z);
            fx.m_20256_(normal);
            world.m_7967_(fx);
        }
    }

    public static float getComplexity(ItemStack spellStack) {
        return getComplexity(spellStack.getTag());
    }

    public static float getComplexity(CompoundTag spellNBT) {
        SpellRecipe recipe = SpellRecipe.fromNBT(spellNBT);
        return recipe != null && recipe.isValid() ? recipe.getComplexity() : 0.0F;
    }

    public static void registerAdjuster(Predicate<SpellAdjustingContext> executeCheck, Consumer<SpellAdjustingContext> adjuster) {
        _adjusters.add(new SpellAdjuster(executeCheck, adjuster));
    }

    public static void registerAdjuster(Predicate<SpellAdjustingContext> executeCheck, BiConsumer<ISpellDefinition, LivingEntity> adjuster) {
        _adjusters.add(new SpellAdjuster(executeCheck, adjuster));
    }

    public static void applyAdjusters(ItemStack stack, LivingEntity caster, ISpellDefinition recipe, SpellCastStage stage) {
        for (SpellAdjuster adjuster : _adjusters) {
            if (adjuster.check(stage, stack, recipe, caster)) {
                adjuster.modify(stack, recipe, caster);
            }
        }
    }

    public static boolean consumeChanneledMana(Player player, ItemStack stack) {
        IPlayerMagic magic = (IPlayerMagic) player.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (magic == null) {
            return false;
        } else {
            SpellRecipe spell = SpellRecipe.fromNBT(stack.getTag());
            if (spell == null || !spell.isValid()) {
                return false;
            } else if (player.isCreative()) {
                return true;
            } else {
                applyAdjusters(stack, player, spell, SpellCastStage.CALCULATING_MANA_COST);
                float manaCost = spell.getManaCost();
                if (!magic.getCastingResource().hasEnough(player, manaCost)) {
                    return false;
                } else {
                    magic.getCastingResource().consume(player, manaCost);
                    return true;
                }
            }
        }
    }

    public static final void setCooldown(ItemStack stack, Player player, int durationInTicks) {
        Item item = stack.getItem();
        if (!_cooldownItems.contains(item)) {
            _cooldownItems.add(item);
        }
        IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (progression != null && progression.getAlliedFaction() == Factions.DEMONS) {
            int flameLevel = stack.getEnchantmentLevel(Enchantments.FIRE_ASPECT);
            if (player.m_6060_() && flameLevel > 0) {
                durationInTicks = (int) ((float) durationInTicks * 0.333F);
            } else {
                durationInTicks = (int) ((double) durationInTicks * 0.75);
            }
        }
        int quickCharge = stack.getEnchantmentLevel(Enchantments.QUICK_CHARGE);
        durationInTicks = (int) ((float) durationInTicks * (1.0F - 0.1F * (float) quickCharge));
        for (Item cdItem : _cooldownItems) {
            player.getCooldowns().addCooldown(cdItem, durationInTicks);
        }
    }

    private static List<Pair<IItemHandler, Direction>> getReagentSearchInventories(Player caster) {
        ArrayList<Pair<IItemHandler, Direction>> output = new ArrayList();
        for (int i = 0; i < caster.getInventory().getContainerSize(); i++) {
            ItemStack invStack = caster.getInventory().getItem(i);
            if (!invStack.isEmpty() && invStack.getItem() instanceof ItemPractitionersPouch) {
                ItemPractitionersPouch item = (ItemPractitionersPouch) invStack.getItem();
                Pair<IItemHandler, Direction> remoteInv = item.resolveRemoteInventory(invStack, caster.m_9236_());
                if (remoteInv.getFirst() != null) {
                    output.add(remoteInv);
                }
                if (item.getPatchLevel(invStack, PractitionersPouchPatches.RIFT) > 0) {
                    caster.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> output.add(new Pair(new InvWrapper(m.getRiftInventory()), Direction.UP)));
                }
                InventoryRitualKit kit = new InventoryRitualKit(invStack);
                output.add(new Pair(kit, Direction.UP));
            }
        }
        output.add(new Pair(new InvWrapper(caster.getInventory()), Direction.UP));
        return output;
    }

    private static final HashMap<Item, Boolean> checkReagents(Player caster, InteractionHand hand, SpellRecipe recipe) {
        HashMap<Item, Boolean> missing = new HashMap();
        if (caster.isCreative()) {
            return missing;
        } else {
            List<Pair<IItemHandler, Direction>> inventories = getReagentSearchInventories(caster);
            for (SpellReagent reagent : recipe.getReagents(caster, hand, null)) {
                if (!InventoryUtilities.consumeAcrossInventories(reagent.getReagentStack(), reagent.getIgnoreDurability(), reagent.getCompareNBT(), true, inventories)) {
                    missing.put(reagent.getReagentStack().getItem(), reagent.getOptional());
                }
            }
            return missing;
        }
    }

    public static final boolean consumeReagents(Player caster, InteractionHand hand, List<SpellReagent> reagents) {
        if (!caster.isCreative() && !caster.m_9236_().isClientSide()) {
            List<Pair<IItemHandler, Direction>> inventories = getReagentSearchInventories(caster);
            boolean output = true;
            for (SpellReagent reagent : reagents) {
                output &= InventoryUtilities.consumeAcrossInventories(reagent.getReagentStack(), reagent.getIgnoreDurability(), reagent.getCompareNBT(), false, inventories);
            }
            return output;
        } else {
            return true;
        }
    }

    public static final boolean consumeReagents(Player caster, InteractionHand hand, ISpellDefinition recipe, SpellCastingResult componentResults) {
        if (!caster.isCreative() && !caster.m_9236_().isClientSide()) {
            List<Pair<IItemHandler, Direction>> inventories = getReagentSearchInventories(caster);
            boolean output = true;
            for (SpellReagent reagent : recipe.getReagents(caster, hand, componentResults)) {
                if (reagent.getConsume()) {
                    output &= InventoryUtilities.consumeAcrossInventories(reagent.getReagentStack(), reagent.getIgnoreDurability(), reagent.getCompareNBT(), false, inventories);
                }
            }
            return output;
        } else {
            return true;
        }
    }

    @Override
    public SpellCastingResult affect(ItemStack stack, ISpellDefinition spell, Level world, SpellSource source) {
        return Affect(stack, spell, world, source, null, null);
    }

    @Override
    public SpellCastingResult affect(ItemStack stack, ISpellDefinition spell, Level world, SpellSource source, SpellTarget targetHint) {
        return Affect(stack, spell, world, source, targetHint, null);
    }

    @Override
    public SpellCastingResult affect(ItemStack stack, ISpellDefinition spell, Level world, SpellSource source, SpellTarget targetHint, SpellContext context) {
        return Affect(stack, spell, world, source, targetHint, context);
    }

    @Override
    public ISpellDefinition createSpell(Shape shape, SpellEffect component, Modifier... modifiers) {
        SpellRecipe recipe = new SpellRecipe();
        recipe.setShape(shape);
        recipe.addComponent(component);
        for (int i = 0; i < Math.min(3, modifiers.length); i++) {
            recipe.setModifier(modifiers[i], i);
        }
        return recipe;
    }

    @Override
    public ISpellDefinition parseSpellDefinition(ItemStack stack) {
        return this.parseSpellDefinition(stack, null);
    }

    @Override
    public ISpellDefinition parseSpellDefinition(ItemStack stack, @Nullable Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        if (stack.getItem() == ItemInit.ROTE_BOOK.get()) {
            if (player == null) {
                tag = ItemInit.ROTE_BOOK.get().getSelectedStack(stack, player).getOrCreateTag();
            } else {
                tag = ItemInit.ROTE_BOOK.get().getSpellCompound(stack, player);
            }
        }
        return SpellRecipe.fromNBT(tag);
    }

    @Override
    public SpellCastingResult playerCast(ItemStack stack, Player caster, InteractionHand hand, boolean consumeMana) {
        return PlayerCast(stack, caster, hand, caster.m_20182_(), caster.m_20154_(), caster.m_9236_(), consumeMana);
    }

    @Override
    public void writeSpellDefinition(ISpellDefinition spell, ItemStack stack) {
        spell.writeToNBT(stack.getOrCreateTag());
    }

    @Override
    public void registerSpellCastingItem(Item item) {
        _cooldownItems.add(item);
    }

    @Override
    public void registerSpellAdjuster(Predicate<SpellAdjustingContext> predicate, BiConsumer<ISpellDefinition, LivingEntity> adjuster) {
        registerAdjuster(predicate, adjuster);
    }

    @Override
    public void registerSpellAdjuster(Predicate<SpellAdjustingContext> predicate, Consumer<SpellAdjustingContext> adjuster) {
        registerAdjuster(predicate, adjuster);
    }

    @Override
    public IForgeRegistry<Shape> getShapeRegistry() {
        return (IForgeRegistry<Shape>) Registries.Shape.get();
    }

    @Override
    public IForgeRegistry<SpellEffect> getComponentRegistry() {
        return (IForgeRegistry<SpellEffect>) Registries.SpellEffect.get();
    }

    @Override
    public IForgeRegistry<Modifier> getModifierRegistry() {
        return (IForgeRegistry<Modifier>) Registries.Modifier.get();
    }

    @Override
    public boolean containsSpell(ItemStack stack) {
        return SpellRecipe.stackContainsSpell(stack);
    }

    @Override
    public boolean reflectSpell(Level level, @Nullable LivingEntity reflector, ISpellDefinition spell, SpellSource source, Vec3 position, Vec3 forward, boolean forwardOnly) {
        if (!spell.isValid()) {
            return true;
        } else if (!source.hasCasterReference()) {
            return true;
        } else {
            Vec3 direction = position.subtract(source.getOrigin()).normalize();
            float angleDeg = (float) (Math.acos(forward.dot(direction)) * 180.0 / Math.PI);
            if (!forwardOnly || angleDeg > 120.0F && angleDeg < 170.0F) {
                if (!level.isClientSide()) {
                    Vec3 reflect = MathUtils.reflect(forward, direction);
                    Shape shape = spell.getShape().getPart();
                    CompoundTag spellData = new CompoundTag();
                    spell.writeToNBT(spellData);
                    if (shape != Shapes.BEAM) {
                        if (shape == Shapes.PROJECTILE) {
                            SpellProjectile newProjectile = new SpellProjectile(reflector, level);
                            newProjectile.setSpellRecipe(spellData);
                            newProjectile.shoot(reflector, reflect, 1.0F, 0.1F);
                            level.m_7967_(newProjectile);
                        } else if (shape == Shapes.BOLT) {
                            ServerMessageDispatcher.sendParticleSpawn(position.x(), position.y(), position.z(), position.x() + reflect.x * 8.0, position.y() + reflect.y * 8.0, position.z() + reflect.z * 8.0, 0, 64.0F, level.dimension(), ParticleInit.LIGHTNING_BOLT.get());
                        }
                    }
                }
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void spawnSpellVFX(Level world, Vec3 position, Vec3 normal, SpellSource source, SpellEffect part) {
        spawnClientFX(world, position, normal, source, part);
    }

    static {
        _cooldownItems.add(ItemInit.SPELL.get());
        _cooldownItems.add(ItemInit.SPELL_BOOK.get());
        _cooldownItems.add(ItemInit.GRIMOIRE.get());
        _cooldownItems.add(ItemInit.GRIMOIRE_COUNCIL.get());
        _cooldownItems.add(ItemInit.GRIMOIRE_DEMON.get());
        _cooldownItems.add(ItemInit.GRIMOIRE_FEY.get());
        _cooldownItems.add(ItemInit.GRIMOIRE_UNDEAD.get());
        _cooldownItems.add(ItemInit.ROTE_BOOK.get());
        _cooldownItems.add(ItemInit.PUNKIN_STAFF.get());
        _cooldownItems.add(ItemInit.STAFF_AMETHYST.get());
        _cooldownItems.add(ItemInit.STAFF_AUM.get());
        _cooldownItems.add(ItemInit.STAFF_CERUBLOSSOM.get());
        _cooldownItems.add(ItemInit.STAFF_CHIMERITE.get());
        _cooldownItems.add(ItemInit.STAFF_DESERTNOVA.get());
        _cooldownItems.add(ItemInit.STAFF_EMERALD.get());
        _cooldownItems.add(ItemInit.STAFF_GLASS.get());
        _cooldownItems.add(ItemInit.STAFF_GOLD.get());
        _cooldownItems.add(ItemInit.STAFF_IRON.get());
        _cooldownItems.add(ItemInit.STAFF_LAPIS.get());
        _cooldownItems.add(ItemInit.STAFF_NETHERQUARTZ.get());
        _cooldownItems.add(ItemInit.STAFF_PRISMARINECRYSTAL.get());
        _cooldownItems.add(ItemInit.STAFF_PRISMARINESHARD.get());
        _cooldownItems.add(ItemInit.STAFF_REDSTONE.get());
        _cooldownItems.add(ItemInit.STAFF_SKULL.get());
        _cooldownItems.add(ItemInit.STAFF_TARMA.get());
        _cooldownItems.add(ItemInit.STAFF_VINTEUM.get());
        _cooldownItems.add(ItemInit.STAFF_WAKEBLOOM.get());
        _cooldownItems.add(ItemInit.WAND_AMETHYST.get());
        _cooldownItems.add(ItemInit.WAND_AUM.get());
        _cooldownItems.add(ItemInit.WAND_CERUBLOSSOM.get());
        _cooldownItems.add(ItemInit.WAND_CHIMERITE.get());
        _cooldownItems.add(ItemInit.WAND_DESERTNOVA.get());
        _cooldownItems.add(ItemInit.WAND_EMERALD.get());
        _cooldownItems.add(ItemInit.WAND_GLASS.get());
        _cooldownItems.add(ItemInit.WAND_GOLD.get());
        _cooldownItems.add(ItemInit.WAND_IRON.get());
        _cooldownItems.add(ItemInit.WAND_LAPIS.get());
        _cooldownItems.add(ItemInit.WAND_NETHERQUARTZ.get());
        _cooldownItems.add(ItemInit.WAND_PRISMARINECRYSTAL.get());
        _cooldownItems.add(ItemInit.WAND_PRISMARINESHARD.get());
        _cooldownItems.add(ItemInit.WAND_REDSTONE.get());
        _cooldownItems.add(ItemInit.WAND_SKULL.get());
        _cooldownItems.add(ItemInit.WAND_TARMA.get());
        _cooldownItems.add(ItemInit.WAND_VINTEUM.get());
        _cooldownItems.add(ItemInit.WAND_WAKEBLOOM.get());
        _cooldownItems.add(ItemInit.HELLFIRE_STAFF.get());
        _cooldownItems.add(ItemInit.ASTRO_BLADE.get());
    }
}
package net.liopyu.entityjs.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Function;
import net.liopyu.entityjs.builders.nonliving.vanilla.EyeOfEnderJSBuilder;
import net.liopyu.entityjs.entities.nonliving.vanilla.EyeOfEnderEntityJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class EyeOfEnderItemBuilder extends ItemBuilder {

    public final transient EyeOfEnderJSBuilder parent;

    public transient boolean triggersCriteria;

    public transient Player sPlayer;

    public transient SoundEvent soundEvent;

    public transient SoundSource soundSource;

    public transient float soundVolume;

    public transient float soundPitch;

    public transient boolean overrideSound;

    public transient Function<ContextUtils.ItemUseContext, Object> signalTo;

    public EyeOfEnderItemBuilder(ResourceLocation i, EyeOfEnderJSBuilder parent) {
        super(i);
        this.parent = parent;
        this.texture = i.getNamespace() + ":item/" + i.getPath();
        this.triggersCriteria = true;
        this.overrideSound = false;
    }

    @Info("A function to determine where the thrown ender eye item will head towards.\n\nExample usage:\n```javascript\nbuilder.signalTo(context => {\n    const { level, player, hand } = context\n    return // Some BlockPos for the eye to navigate to when thrown\n});\n```\n")
    public EyeOfEnderItemBuilder signalTo(Function<ContextUtils.ItemUseContext, Object> f) {
        this.signalTo = f;
        return this;
    }

    @Info("Sets the sound to play when the eye item is thrown at the coordinates of the player\n\n@param sPlayer The player to play the sound to, can be null.\n@param soundEvent The sound to play when the eye item is thrown\n@param soundSource The source of the sound in the mixer.\n@param soundVolume The volume of the sound.\n@param soundPitch The pitch of the sound.\n\n```javascript\nitem.playSoundOverride(null,\"ambient.basalt_deltas.additions\",\"ambient\",1,1)\n```\n")
    public EyeOfEnderItemBuilder playSoundOverride(@Nullable Player player, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        this.sPlayer = player;
        this.soundEvent = soundEvent;
        this.soundSource = soundSource;
        this.soundVolume = volume;
        this.soundPitch = pitch;
        this.overrideSound = true;
        return this;
    }

    @Info("Sets if the eye of ender triggers the USED_ENDER_EYE Criteria")
    public EyeOfEnderItemBuilder triggersCriteria(boolean triggersCriteria) {
        this.triggersCriteria = triggersCriteria;
        return this;
    }

    public Item createObject() {
        return new EnderEyeItem(this.createItemProperties()) {

            @Override
            public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
                ItemStack $$3 = pPlayer.m_21120_(pHand);
                BlockHitResult $$4 = m_41435_(pLevel, pPlayer, ClipContext.Fluid.NONE);
                if ($$4.getType() == HitResult.Type.BLOCK && pLevel.getBlockState($$4.getBlockPos()).m_60713_(Blocks.END_PORTAL_FRAME)) {
                    return InteractionResultHolder.pass($$3);
                } else {
                    pPlayer.m_6672_(pHand);
                    if (pLevel instanceof ServerLevel $$5) {
                        BlockPos $$6 = $$5.findNearestMapStructure(StructureTags.EYE_OF_ENDER_LOCATED, pPlayer.m_20183_(), 100, false);
                        if (EyeOfEnderItemBuilder.this.signalTo != null) {
                            ContextUtils.ItemUseContext context = new ContextUtils.ItemUseContext(pLevel, pPlayer, pHand);
                            Object obj = EyeOfEnderItemBuilder.this.signalTo.apply(context);
                            if (obj instanceof BlockPos b) {
                                EyeOfEnderEntityJS $$7 = new EyeOfEnderEntityJS(EyeOfEnderItemBuilder.this.parent, pLevel, (EntityType<? extends EyeOfEnder>) EyeOfEnderItemBuilder.this.parent.get(), pPlayer.m_20185_(), pPlayer.m_20227_(0.5), pPlayer.m_20189_());
                                $$7.m_36972_($$3);
                                $$7.m_36967_(b);
                                pLevel.m_214171_(GameEvent.PROJECTILE_SHOOT, $$7.m_20182_(), GameEvent.Context.of(pPlayer));
                                pLevel.m_7967_($$7);
                                if (pPlayer instanceof ServerPlayer && EyeOfEnderItemBuilder.this.triggersCriteria) {
                                    CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) pPlayer, $$6);
                                }
                                if (EyeOfEnderItemBuilder.this.overrideSound) {
                                    pLevel.playSound(EyeOfEnderItemBuilder.this.sPlayer, pPlayer.m_20185_(), pPlayer.m_20186_(), pPlayer.m_20189_(), EyeOfEnderItemBuilder.this.soundEvent, EyeOfEnderItemBuilder.this.soundSource, EyeOfEnderItemBuilder.this.soundVolume, EyeOfEnderItemBuilder.this.soundPitch);
                                } else {
                                    pLevel.playSound((Player) null, pPlayer.m_20185_(), pPlayer.m_20186_(), pPlayer.m_20189_(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
                                }
                                pLevel.m_5898_((Player) null, 1003, pPlayer.m_20183_(), 0);
                                if (!pPlayer.getAbilities().instabuild) {
                                    $$3.shrink(1);
                                }
                                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                                pPlayer.m_21011_(pHand, true);
                                return InteractionResultHolder.success($$3);
                            }
                            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for signalTo in ender eye item builder: " + obj + ". Must be a BlockPos. Defaulting to null.");
                            return InteractionResultHolder.consume($$3);
                        }
                        if ($$6 != null) {
                            EyeOfEnderEntityJS $$7x = new EyeOfEnderEntityJS(EyeOfEnderItemBuilder.this.parent, pLevel, (EntityType<? extends EyeOfEnder>) EyeOfEnderItemBuilder.this.parent.get(), pPlayer.m_20185_(), pPlayer.m_20227_(0.5), pPlayer.m_20189_());
                            $$7x.m_36972_($$3);
                            $$7x.m_36967_($$6);
                            pLevel.m_214171_(GameEvent.PROJECTILE_SHOOT, $$7x.m_20182_(), GameEvent.Context.of(pPlayer));
                            pLevel.m_7967_($$7x);
                            if (pPlayer instanceof ServerPlayer && EyeOfEnderItemBuilder.this.triggersCriteria) {
                                CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) pPlayer, $$6);
                            }
                            if (EyeOfEnderItemBuilder.this.overrideSound) {
                                pLevel.playSound(EyeOfEnderItemBuilder.this.sPlayer, pPlayer.m_20185_(), pPlayer.m_20186_(), pPlayer.m_20189_(), EyeOfEnderItemBuilder.this.soundEvent, EyeOfEnderItemBuilder.this.soundSource, EyeOfEnderItemBuilder.this.soundVolume, EyeOfEnderItemBuilder.this.soundPitch);
                            } else {
                                pLevel.playSound((Player) null, pPlayer.m_20185_(), pPlayer.m_20186_(), pPlayer.m_20189_(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
                            }
                            pLevel.m_5898_((Player) null, 1003, pPlayer.m_20183_(), 0);
                            if (!pPlayer.getAbilities().instabuild) {
                                $$3.shrink(1);
                            }
                            pPlayer.awardStat(Stats.ITEM_USED.get(this));
                            pPlayer.m_21011_(pHand, true);
                            return InteractionResultHolder.success($$3);
                        }
                    }
                    return InteractionResultHolder.consume($$3);
                }
            }
        };
    }
}
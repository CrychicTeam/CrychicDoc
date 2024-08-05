package com.mna.items.ritual;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.items.TieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.api.rituals.RitualEffect;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.rituals.Ritual;
import com.mna.recipes.rituals.RitualRecipe;
import com.mna.rituals.MatchedRitual;
import com.mna.rituals.contexts.RitualCheckContext;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ItemPurifiedVinteumDust extends TieredItem {

    public ItemPurifiedVinteumDust(Item.Properties properties) {
        super(new Item.Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        IPlayerMagic magic = (IPlayerMagic) context.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        IPlayerProgression progression = (IPlayerProgression) context.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
        if (!magic.isMagicUnlocked()) {
            return InteractionResult.FAIL;
        } else if (!context.getLevel().isClientSide && context.getPlayer().m_6144_() && context.getLevel().getBlockState(context.getClickedPos()).m_60734_() == BlockInit.CHALK_RUNE.get()) {
            this.commandRitualEntity(context, NonNullList.create());
            return InteractionResult.SUCCESS;
        } else {
            MatchedRitual ritualMatch = RitualRecipe.matchAnyInWorld(context.getClickedPos(), context.getLevel());
            if (ritualMatch != null) {
                if (context.getLevel().isClientSide) {
                    this.spawnClientParticles(context);
                } else {
                    RitualEffect matchedEffect = (RitualEffect) ((IForgeRegistry) Registries.RitualEffect.get()).getValues().stream().filter(r -> r.matchRitual(ritualMatch.getRitual().m_6423_())).findFirst().orElse(null);
                    if (matchedEffect == null && !ritualMatch.getRitual().hasCommand()) {
                        context.getPlayer().m_213846_(Component.translatable("mna:ritual-no-handler"));
                        return InteractionResult.FAIL;
                    }
                    if (ritualMatch.getRitual().getTier() > progression.getTier()) {
                        context.getPlayer().m_213846_(Component.translatable("mna:ritual-start-tier-fail"));
                        return InteractionResult.FAIL;
                    }
                    if (matchedEffect != null && (!context.getPlayer().isCreative() || matchedEffect.applyStartCheckInCreative())) {
                        Component cantStartReason = matchedEffect.canRitualStart(new RitualCheckContext(context.getPlayer(), (ServerLevel) context.getLevel(), ritualMatch.getRitual(), context.getClickedPos(), this.getRitualAbove(context)));
                        if (cantStartReason != null) {
                            context.getPlayer().m_213846_(cantStartReason);
                            return InteractionResult.FAIL;
                        }
                    }
                    this.updateBlockStates(context, ritualMatch.getPositions(), true);
                    ItemPurifiedVinteumDust.RitualInteractResult result = this.commandRitualEntity(context, ritualMatch.getPositions());
                    if (result == ItemPurifiedVinteumDust.RitualInteractResult.SPAWN) {
                        this.spawnRitualEntity(context, ritualMatch.getRitual(), ritualMatch.getPositions());
                    } else if (result == ItemPurifiedVinteumDust.RitualInteractResult.STARTED && !context.getPlayer().m_6144_()) {
                        context.getItemInHand().shrink(1);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    private void spawnClientParticles(UseOnContext context) {
        for (int i = 0; i < 50; i++) {
            context.getLevel().addParticle(new MAParticleType(ParticleInit.SPARKLE_RANDOM.get()), (double) ((float) context.getClickedPos().m_123341_() + context.getLevel().getRandom().nextFloat() * 10.0F - 5.0F), (double) ((float) context.getClickedPos().m_123342_() + context.getLevel().getRandom().nextFloat()), (double) ((float) context.getClickedPos().m_123343_() + context.getLevel().getRandom().nextFloat() * 10.0F - 5.0F), 0.0, 0.0, 0.0);
        }
    }

    private void updateBlockStates(UseOnContext context, NonNullList<RitualBlockPos> matchedPositions, boolean activated) {
        for (RitualBlockPos position : matchedPositions) {
            BlockState state = context.getLevel().getBlockState(position.getBlockPos());
            BlockState newState = (BlockState) state.m_61124_(ChalkRuneBlock.ACTIVATED, activated);
            context.getLevel().setBlockAndUpdate(position.getBlockPos(), newState);
            context.getLevel().sendBlockUpdated(position.getBlockPos(), state, newState, 2);
        }
    }

    private Ritual getRitualAbove(UseOnContext context) {
        List<Entity> entities = context.getLevel().getEntities(context.getPlayer(), new AABB(context.getClickedPos()).inflate(2.0), e -> e.getType() == EntityInit.RITUAL_ENTITY.get());
        Entity ritualEntity = null;
        if (entities != null && entities.size() == 1) {
            ritualEntity = (Entity) entities.get(0);
            if (ritualEntity instanceof Ritual) {
                return (Ritual) ritualEntity;
            }
        }
        return null;
    }

    private ItemPurifiedVinteumDust.RitualInteractResult commandRitualEntity(UseOnContext context, NonNullList<RitualBlockPos> matchedPositions) {
        Ritual ritualEntity = this.getRitualAbove(context);
        if (ritualEntity != null) {
            if (context.getPlayer().m_6047_()) {
                if (ritualEntity.cancelRitual()) {
                    return ItemPurifiedVinteumDust.RitualInteractResult.CANCELED;
                }
            } else if (ritualEntity.confirmRitualReagents()) {
                return ItemPurifiedVinteumDust.RitualInteractResult.STARTED;
            }
            return ItemPurifiedVinteumDust.RitualInteractResult.NO_ACTION;
        } else {
            return ItemPurifiedVinteumDust.RitualInteractResult.SPAWN;
        }
    }

    private boolean spawnRitualEntity(UseOnContext context, RitualRecipe ritual, NonNullList<RitualBlockPos> matchedPositions) {
        if (context.getPlayer() == null) {
            ManaAndArtifice.LOGGER.error("Received a null player reference when trying to spawn a ritual.  This shouln't happen and something has messed with vanilla code!  Aborting.");
            return false;
        } else if (context.getPlayer().m_20148_() == null) {
            ManaAndArtifice.LOGGER.error("Unable to resolve UUID for a player.  This shouln't happen and something has messed with vanilla code!  Aborting.");
            return false;
        } else {
            MutableBoolean success = new MutableBoolean(false);
            context.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                float speed = Math.max(1.0F - 0.2F * (float) (p.getTier() - ritual.getTier()), 0.2F);
                Ritual ritualEntity = new Ritual(EntityInit.RITUAL_ENTITY.get(), context.getLevel());
                BlockPos ritualSpawnPos = context.getClickedPos();
                ritualEntity.m_6034_((double) ((float) ritualSpawnPos.m_123341_() + 0.5F), (double) ritualSpawnPos.m_123342_(), (double) ((float) ritualSpawnPos.m_123343_() + 0.5F));
                ritualEntity.setRitualBlockLocations(matchedPositions);
                ritualEntity.setRitualName(ritual.m_6423_());
                ritualEntity.setSpeed(speed);
                ritualEntity.setCasterUUID(context.getPlayer().m_20148_());
                context.getLevel().m_7967_(ritualEntity);
                success.setTrue();
            });
            return success.getValue();
        }
    }

    static enum RitualInteractResult {

        SPAWN, UPDATED, STARTED, CANCELED, NO_ACTION
    }
}
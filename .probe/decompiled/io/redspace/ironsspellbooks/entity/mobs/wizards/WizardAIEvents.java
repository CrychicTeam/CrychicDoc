package io.redspace.ironsspellbooks.entity.mobs.wizards;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.NeutralWizard;
import io.redspace.ironsspellbooks.util.ModTags;
import java.util.List;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class WizardAIEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getState().m_204336_(ModTags.GUARDED_BY_WIZARDS)) {
            angerNearbyWizards(event.getPlayer(), 3, false, true);
        }
    }

    @SubscribeEvent
    public static void onBlockUsed(PlayerInteractEvent.RightClickBlock event) {
        BlockState blockstate = event.getLevel().getBlockState(event.getHitVec().getBlockPos());
        if (blockstate.m_204336_(ModTags.GUARDED_BY_WIZARDS) && (!(event.getLevel().getBlockEntity(event.getPos()) instanceof RandomizableContainerBlockEntity randomizableContainerBlockEntity) || randomizableContainerBlockEntity.lootTable != null)) {
            angerNearbyWizards(event.getEntity(), 1, false, true);
        }
    }

    public static void angerNearbyWizards(Player player, int angerLevel, boolean requireLineOfSight, boolean blockRelated) {
        if (!player.getAbilities().instabuild) {
            List<NeutralWizard> list = player.f_19853_.m_45976_(NeutralWizard.class, player.m_20191_().inflate(16.0));
            list.stream().filter(neutralWizard -> (neutralWizard.guardsBlocks() || !blockRelated) && (!requireLineOfSight || BehaviorUtils.canSee(neutralWizard, player))).forEach(neutralWizard -> {
                neutralWizard.increaseAngerLevel(angerLevel);
                neutralWizard.setPersistentAngerTarget(player.m_20148_());
            });
        }
    }
}
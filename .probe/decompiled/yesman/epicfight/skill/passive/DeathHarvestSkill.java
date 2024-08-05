package yesman.epicfight.skill.passive;

import java.util.UUID;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.entity.DeathHarvestOrb;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class DeathHarvestSkill extends PassiveSkill {

    private static final UUID EVENT_UUID = UUID.fromString("816118e6-b902-11ed-afa1-0242ac120002");

    public DeathHarvestSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, event -> {
            PlayerPatch<?> playerpatch = container.getExecuter();
            Player original = playerpatch.getOriginal();
            LivingEntity target = event.getTarget();
            if (event.getDamageSource().is(EpicFightDamageType.WEAPON_INNATE) && !target.isAlive()) {
                original.m_9236_().playSound(null, original.m_20185_(), original.m_20186_(), original.m_20189_(), SoundEvents.WITHER_AMBIENT, original.getSoundSource(), 0.3F, 1.25F);
                int damage = (int) original.m_21133_(Attributes.ATTACK_DAMAGE);
                DeathHarvestOrb harvestOrb = new DeathHarvestOrb(original, target.m_20185_(), target.m_20186_() + (double) target.m_20206_() * 0.5, target.m_20189_(), damage);
                original.m_9236_().m_7967_(harvestOrb);
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
    }
}
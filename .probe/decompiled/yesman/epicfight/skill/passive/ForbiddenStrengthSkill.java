package yesman.epicfight.skill.passive;

import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class ForbiddenStrengthSkill extends PassiveSkill {

    private static final UUID EVENT_UUID = UUID.fromString("0cfd60ba-b900-11ed-afa1-0242ac120002");

    public ForbiddenStrengthSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, EVENT_UUID, event -> {
            if (event.getResourceType() == Skill.Resource.STAMINA) {
                float staminaConsume = event.getAmount();
                if (!container.getExecuter().hasStamina(staminaConsume) && !container.getExecuter().getOriginal().isCreative()) {
                    event.setResourceType(Skill.Resource.HEALTH);
                    event.setAmount(staminaConsume);
                    if (event.shouldConsume()) {
                        Player player = container.getExecuter().getOriginal();
                        player.m_9236_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), EpicFightSounds.FORBIDDEN_STRENGTH.get(), player.getSoundSource(), 1.0F, 1.0F);
                        ((ServerLevel) player.m_9236_()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, player.m_20185_(), player.m_20227_(0.5), player.m_20189_(), (int) staminaConsume, 0.1, 0.0, 0.1, 0.2);
                    }
                }
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, EVENT_UUID);
    }
}
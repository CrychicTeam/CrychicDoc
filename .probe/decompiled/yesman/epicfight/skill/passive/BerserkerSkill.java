package yesman.epicfight.skill.passive;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class BerserkerSkill extends PassiveSkill {

    private static final UUID EVENT_UUID = UUID.fromString("fdc09ee8-fcfc-11eb-9a03-0242ac130003");

    private float speedBonus;

    private float damageBonus;

    public BerserkerSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        this.speedBonus = parameters.getFloat("speed_bonus");
        this.damageBonus = parameters.getFloat("damage_bonus");
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        PlayerEventListener listener = container.getExecuter().getEventListener();
        listener.addEventListener(PlayerEventListener.EventType.MODIFY_ATTACK_SPEED_EVENT, EVENT_UUID, event -> {
            Player player = event.getPlayerPatch().getOriginal();
            float health = player.m_21223_();
            float maxHealth = player.m_21233_();
            float lostHealthPercentage = (maxHealth - health) / maxHealth;
            lostHealthPercentage = (float) Math.floor((double) (lostHealthPercentage * 100.0F)) * this.speedBonus * 0.01F;
            float attackSpeed = event.getAttackSpeed();
            event.setAttackSpeed(Math.min(5.0F, attackSpeed * (1.0F + lostHealthPercentage)));
        });
        listener.addEventListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID, event -> {
            Player player = (Player) event.getPlayerPatch().getOriginal();
            float health = player.m_21223_();
            float maxHealth = player.m_21233_();
            float lostHealthPercentage = (maxHealth - health) / maxHealth;
            lostHealthPercentage = (float) Math.floor((double) (lostHealthPercentage * 100.0F)) * this.damageBonus * 0.01F;
            float attackDamage = event.getDamage();
            event.setDamage(attackDamage * (1.0F + lostHealthPercentage));
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MODIFY_ATTACK_SPEED_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldDraw(SkillContainer container) {
        Player player = container.getExecuter().getOriginal();
        float health = player.m_21223_();
        float maxHealth = player.m_21233_();
        return maxHealth - health > 0.0F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0F, (float) gui.getSlidingProgression(), 0.0F);
        guiGraphics.blit(this.getSkillTexture(), (int) x, (int) y, 24, 24, 0.0F, 0.0F, 1, 1, 1, 1);
        Player player = container.getExecuter().getOriginal();
        float health = player.m_21223_();
        float maxHealth = player.m_21233_();
        float lostHealthPercentage = (maxHealth - health) / maxHealth;
        lostHealthPercentage = (float) Math.floor((double) (lostHealthPercentage * 100.0F));
        guiGraphics.drawString(gui.font, String.format("%.0f%%", lostHealthPercentage), x + 4.0F, y + 6.0F, 16777215, true);
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Object> getTooltipArgsOfScreen(List<Object> list) {
        list.add(String.format("%.1f", this.speedBonus));
        list.add(String.format("%.1f", this.damageBonus));
        return list;
    }
}
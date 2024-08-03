package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.message.MessageSendVisualFlagFromServer;
import com.github.alexthe666.alexsmobs.misc.AMDamageTypes;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.EnumSet;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

public class GrizzlyBearAIAprilFools extends Goal {

    private final EntityGrizzlyBear bear;

    private Player target;

    private int runDelay = 0;

    private final double maxDistance = 13.0;

    private int powerOutTimer = 0;

    private int musicBoxTimer = 0;

    private int maxMusicBoxTime = 0;

    private int leapTimer = 0;

    public GrizzlyBearAIAprilFools(EntityGrizzlyBear bear) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.bear = bear;
    }

    @Override
    public boolean canUse() {
        if (!this.bear.m_6162_() && AlexsMobs.isAprilFools() && this.runDelay-- <= 0 && this.bear.m_217043_().nextInt(30) == 0) {
            this.runDelay = 400 + this.bear.m_217043_().nextInt(350);
            Player nearestPlayer = this.bear.m_9236_().m_5788_(this.bear.m_20185_(), this.bear.m_20186_(), this.bear.m_20189_(), 13.0, entity -> this.bear.m_142582_(entity) && (!(entity instanceof Player) || !((Player) entity).m_21023_(AMEffectRegistry.POWER_DOWN.get())));
            if (nearestPlayer != null) {
                this.target = nearestPlayer;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.target != null && (double) this.bear.m_20270_(this.target) < 26.0;
    }

    @Override
    public void start() {
        this.maxMusicBoxTime = 100 + this.bear.m_217043_().nextInt(130);
    }

    @Override
    public void tick() {
        super.tick();
        double dist = (double) this.bear.m_20270_(this.target);
        this.bear.m_21563_().setLookAt(this.target.m_20185_(), this.target.m_20188_(), this.target.m_20189_());
        if (dist <= 6.0 && this.bear.m_142582_(this.target)) {
            this.bear.m_21573_().stop();
            if (this.bear.getAprilFoolsFlag() == 5) {
                this.leapTimer++;
                if (this.leapTimer == 7) {
                    AlexsMobs.sendMSGToAll(new MessageSendVisualFlagFromServer(this.target.m_19879_(), 87));
                }
                if (this.leapTimer >= 10) {
                    this.bear.setAprilFoolsFlag(0);
                    if (this.bear.m_9236_().getLevelData().isHardcore()) {
                        this.target.hurt(AMDamageTypes.causeFreddyBearDamage(this.bear), this.target.m_21233_() - 1.0F);
                        this.target.m_21153_(1.0F);
                    } else {
                        this.target.hurt(AMDamageTypes.causeFreddyBearDamage(this.bear), this.target.m_21233_() + 1000.0F);
                    }
                    this.stop();
                    return;
                }
            } else if (this.bear.getAprilFoolsFlag() < 4) {
                if (this.powerOutTimer == 0) {
                    this.target.m_7292_(new MobEffectInstance(AMEffectRegistry.POWER_DOWN.get(), 2 * (this.maxMusicBoxTime + 100), 0, false, false, true));
                }
                this.powerOutTimer++;
                if (this.powerOutTimer >= 60) {
                    this.bear.setAprilFoolsFlag(4);
                    this.powerOutTimer = 0;
                } else {
                    this.bear.setAprilFoolsFlag(3);
                }
            } else {
                if (this.musicBoxTimer == 0) {
                    this.bear.m_9236_().broadcastEntityEvent(this.bear, (byte) 67);
                }
                this.musicBoxTimer++;
                if (this.musicBoxTimer >= this.maxMusicBoxTime && this.bear.getAprilFoolsFlag() != 5) {
                    this.bear.m_9236_().broadcastEntityEvent(this.bear, (byte) 68);
                    this.bear.setAprilFoolsFlag(5);
                    this.bear.m_146850_(GameEvent.ENTITY_ROAR);
                    this.bear.m_5496_(AMSoundRegistry.APRIL_FOOLS_SCREAM.get(), 3.0F, 1.0F);
                    this.musicBoxTimer = 0;
                }
            }
            if (this.bear.getAprilFoolsFlag() < 2) {
                this.bear.setAprilFoolsFlag(2);
            }
        } else {
            this.bear.m_21573_().moveTo(this.target, 1.2F);
            if (this.bear.getAprilFoolsFlag() < 1) {
                this.bear.setAprilFoolsFlag(1);
            }
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.runDelay = 100 + this.bear.m_217043_().nextInt(100);
        this.bear.setAprilFoolsFlag(0);
        this.powerOutTimer = 0;
        this.musicBoxTimer = 0;
        this.leapTimer = 0;
    }
}
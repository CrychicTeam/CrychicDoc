package noppes.npcs.ai.target;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import noppes.npcs.entity.EntityNPCInterface;

public class NpcNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public NpcNearestAttackableTargetGoal(EntityNPCInterface npc, Class<T> c, int range, boolean b, boolean b2, @Nullable Predicate<LivingEntity> selector) {
        super(npc, c, range, b, b2, selector);
        if (npc.ais.attackInvisible) {
            this.f_26051_.ignoreInvisibilityTesting();
        }
    }
}
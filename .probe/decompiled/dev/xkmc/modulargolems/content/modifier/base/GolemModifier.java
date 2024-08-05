package dev.xkmc.modulargolems.content.modifier.base;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.CreateSourceEvent;
import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.modulargolems.content.config.GolemPartConfig;
import dev.xkmc.modulargolems.content.core.GolemType;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class GolemModifier extends NamedEntry<GolemModifier> {

    public static final int MAX_LEVEL = 5;

    public final StatFilterType type;

    public final int maxLevel;

    public GolemModifier(StatFilterType type, int maxLevel) {
        super(GolemTypes.MODIFIERS);
        this.type = type;
        this.maxLevel = maxLevel;
    }

    public Component getTooltip(int v) {
        MutableComponent ans = this.getDesc();
        if (this.maxLevel > 1) {
            ans = ans.append(" ").append(Component.translatable("potion.potency." + (v - 1)));
        }
        return ans.withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    public List<MutableComponent> getDetail(int v) {
        return List.of(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GREEN));
    }

    public void onGolemSpawn(AbstractGolemEntity<?, ?> entity, int level) {
    }

    public void onAttackTarget(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
    }

    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
    }

    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
    }

    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
    }

    public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
    }

    public double onHealTick(double heal, AbstractGolemEntity<?, ?> entity, int level) {
        return this.onInventoryHealTick(heal, new GolemModifier.HealingContext(entity.m_21223_(), entity.m_21233_(), entity), level);
    }

    public double onInventoryHealTick(double heal, GolemModifier.HealingContext ctx, int level) {
        return heal;
    }

    public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
    }

    public int addSlot(List<UpgradeItem> upgrades, int lv) {
        return 0;
    }

    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
    }

    public void onRegisterFlag(Consumer<GolemFlags> addFlag) {
    }

    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
    }

    @OnlyIn(Dist.CLIENT)
    public void onClientTick(AbstractGolemEntity<?, ?> entity, int level) {
    }

    public boolean canExistOn(GolemPart<?, ?> part) {
        return (Double) GolemPartConfig.get().getFilter(part).getOrDefault(this.type, 0.0) > 0.0;
    }

    public void onSetTarget(AbstractGolemEntity<?, ?> golem, Mob mob, int level) {
    }

    public boolean fitsOn(GolemType<?, ?> type) {
        return true;
    }

    public void modifySource(AbstractGolemEntity<?, ?> golem, CreateSourceEvent event, int value) {
    }

    public void handleEvent(AbstractGolemEntity<?, ?> golem, int value, byte event) {
    }

    public InteractionResult interact(Player player, AbstractGolemEntity<?, ?> golem, InteractionHand hand) {
        return InteractionResult.PASS;
    }

    public static record HealingContext(float health, float maxHealth, Entity owner) {
    }
}
package yesman.epicfight.world.entity.eventlistener;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraftforge.fml.LogicalSide;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class PlayerEventListener {

    private final Map<PlayerEventListener.EventType<? extends PlayerEvent<?>>, TreeMultimap<Integer, EventTrigger<? extends PlayerEvent<?>>>> events;

    private final PlayerPatch<?> playerpatch;

    public PlayerEventListener(PlayerPatch<?> playerpatch) {
        this.playerpatch = playerpatch;
        this.events = Maps.newHashMap();
    }

    public <T extends PlayerEvent<?>> void addEventListener(PlayerEventListener.EventType<T> eventType, UUID uuid, Consumer<T> function) {
        this.addEventListener(eventType, uuid, function, -1);
    }

    public <T extends PlayerEvent<?>> void addEventListener(PlayerEventListener.EventType<T> eventType, UUID uuid, Consumer<T> function, int priority) {
        if (eventType.shouldActive(this.playerpatch.isLogicalClient())) {
            if (!this.events.containsKey(eventType)) {
                this.events.put(eventType, TreeMultimap.create());
            }
            priority = Math.max(priority, -1);
            this.removeListener(eventType, uuid, priority);
            TreeMultimap<Integer, EventTrigger<? extends PlayerEvent<?>>> map = (TreeMultimap<Integer, EventTrigger<? extends PlayerEvent<?>>>) this.events.get(eventType);
            map.put(priority, EventTrigger.makeEvent(uuid, function, priority));
        }
    }

    public <T extends PlayerEvent<?>> void removeListener(PlayerEventListener.EventType<T> eventType, UUID uuid) {
        this.removeListener(eventType, uuid, -1);
    }

    public <T extends PlayerEvent<?>> void removeListener(PlayerEventListener.EventType<T> eventType, UUID uuid, int priority) {
        Multimap<Integer, EventTrigger<? extends PlayerEvent<?>>> map = (Multimap<Integer, EventTrigger<? extends PlayerEvent<?>>>) this.events.get(eventType);
        if (map != null) {
            priority = Math.max(priority, -1);
            map.get(priority).removeIf(trigger -> trigger.is(uuid));
        }
    }

    public <T extends PlayerEvent<?>> boolean triggerEvents(PlayerEventListener.EventType<T> eventType, T event) {
        boolean cancel = false;
        TreeMultimap<Integer, EventTrigger<? extends PlayerEvent<?>>> map = (TreeMultimap<Integer, EventTrigger<? extends PlayerEvent<?>>>) this.events.get(eventType);
        if (map != null) {
            for (int i : map.keySet().descendingSet()) {
                if (!cancel || i == -1) {
                    for (EventTrigger<?> eventTrigger : map.get(i)) {
                        if (eventType.shouldActive(this.playerpatch.isLogicalClient())) {
                            ((EventTrigger<T>) eventTrigger).trigger(event);
                            cancel |= event.isCanceled();
                        }
                    }
                }
            }
        }
        return cancel;
    }

    public static class EventType<T extends PlayerEvent<?>> {

        public static final PlayerEventListener.EventType<ActionEvent<LocalPlayerPatch>> ACTION_EVENT_CLIENT = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<ActionEvent<ServerPlayerPatch>> ACTION_EVENT_SERVER = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<AttackSpeedModifyEvent> MODIFY_ATTACK_SPEED_EVENT = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<ModifyBaseDamageEvent<PlayerPatch<?>>> MODIFY_DAMAGE_EVENT = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<DealtDamageEvent> DEALT_DAMAGE_EVENT_PRE = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<DealtDamageEvent> DEALT_DAMAGE_EVENT_POST = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<HurtEvent.Pre> HURT_EVENT_PRE = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<HurtEvent.Post> HURT_EVENT_POST = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<AttackEndEvent> ATTACK_ANIMATION_END_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<BasicAttackEvent> BASIC_ATTACK_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<MovementInputEvent> MOVEMENT_INPUT_EVENT = new PlayerEventListener.EventType<>(LogicalSide.CLIENT);

        public static final PlayerEventListener.EventType<RightClickItemEvent<LocalPlayerPatch>> CLIENT_ITEM_USE_EVENT = new PlayerEventListener.EventType<>(LogicalSide.CLIENT);

        public static final PlayerEventListener.EventType<RightClickItemEvent<ServerPlayerPatch>> SERVER_ITEM_USE_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<ItemUseEndEvent> SERVER_ITEM_STOP_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<ProjectileHitEvent> PROJECTILE_HIT_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<SkillExecuteEvent> SKILL_EXECUTE_EVENT = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<SkillCancelEvent> SKILL_CANCEL_EVENT = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<SkillConsumeEvent> SKILL_CONSUME_EVENT = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<ComboCounterHandleEvent> COMBO_COUNTER_HANDLE_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<TargetIndicatorCheckEvent> TARGET_INDICATOR_ALERT_CHECK_EVENT = new PlayerEventListener.EventType<>(LogicalSide.CLIENT);

        public static final PlayerEventListener.EventType<FallEvent> FALL_EVENT = new PlayerEventListener.EventType<>(null);

        public static final PlayerEventListener.EventType<SetTargetEvent> SET_TARGET_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        public static final PlayerEventListener.EventType<DodgeSuccessEvent> DODGE_SUCCESS_EVENT = new PlayerEventListener.EventType<>(LogicalSide.SERVER);

        LogicalSide side;

        EventType(LogicalSide side) {
            this.side = side;
        }

        public boolean shouldActive(boolean isRemote) {
            return this.side == null || this.side.isClient() == isRemote;
        }
    }
}
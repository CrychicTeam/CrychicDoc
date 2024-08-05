package noppes.npcs.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.IDamageSource;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.entity.IEntityLiving;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.handler.data.IFaction;
import noppes.npcs.api.item.IItemStack;

public class PlayerEvent extends CustomNPCsEvent {

    public final IPlayer player;

    public PlayerEvent(IPlayer player) {
        this.player = player;
    }

    @Cancelable
    public static class AttackEvent extends PlayerEvent {

        public final int type;

        public final Object target;

        public final IDamageSource damageSource;

        public AttackEvent(IPlayer player, int type, Object target) {
            super(player);
            this.type = type;
            this.target = target;
            this.damageSource = null;
        }

        public AttackEvent(IPlayer player, IEntity target, IDamageSource damageSource) {
            super(player);
            this.type = 1;
            this.target = target;
            this.damageSource = damageSource;
        }
    }

    @Cancelable
    public static class BreakEvent extends PlayerEvent {

        public final IBlock block;

        public int exp;

        public BreakEvent(IPlayer player, IBlock block, int exp) {
            super(player);
            this.block = block;
            this.exp = exp;
        }
    }

    @Cancelable
    public static class ChatEvent extends PlayerEvent {

        public String message;

        public ChatEvent(IPlayer player, String message) {
            super(player);
            this.message = message;
        }
    }

    public static class ContainerClosed extends PlayerEvent {

        public final IContainer container;

        public ContainerClosed(IPlayer player, IContainer container) {
            super(player);
            this.container = container;
        }
    }

    public static class ContainerOpen extends PlayerEvent {

        public final IContainer container;

        public ContainerOpen(IPlayer player, IContainer container) {
            super(player);
            this.container = container;
        }
    }

    @Cancelable
    public static class DamagedEntityEvent extends PlayerEvent {

        public final IDamageSource damageSource;

        public final IEntity target;

        public float damage;

        public DamagedEntityEvent(IPlayer player, Entity target, float damage, DamageSource damagesource) {
            super(player);
            this.target = NpcAPI.Instance().getIEntity(target);
            this.damage = damage;
            this.damageSource = NpcAPI.Instance().getIDamageSource(damagesource);
        }
    }

    @Cancelable
    public static class DamagedEvent extends PlayerEvent {

        public final IDamageSource damageSource;

        public final IEntity source;

        public float damage;

        public boolean clearTarget = false;

        public DamagedEvent(IPlayer player, Entity source, float damage, DamageSource damagesource) {
            super(player);
            this.source = NpcAPI.Instance().getIEntity(source);
            this.damage = damage;
            this.damageSource = NpcAPI.Instance().getIDamageSource(damagesource);
        }
    }

    @Cancelable
    public static class DiedEvent extends PlayerEvent {

        public final IDamageSource damageSource;

        public final String type;

        public final IEntity source;

        public DiedEvent(IPlayer player, DamageSource damagesource, Entity entity) {
            super(player);
            this.damageSource = NpcAPI.Instance().getIDamageSource(damagesource);
            this.type = damagesource.getMsgId();
            this.source = NpcAPI.Instance().getIEntity(entity);
        }
    }

    public static class FactionUpdateEvent extends PlayerEvent {

        public final IFaction faction;

        public int points;

        public boolean init;

        public FactionUpdateEvent(IPlayer player, IFaction faction, int points, boolean init) {
            super(player);
            this.faction = faction;
            this.points = points;
            this.init = init;
        }
    }

    public static class InitEvent extends PlayerEvent {

        public InitEvent(IPlayer player) {
            super(player);
        }
    }

    @Cancelable
    public static class InteractEvent extends PlayerEvent {

        public final int type;

        public final Object target;

        public InteractEvent(IPlayer player, int type, Object target) {
            super(player);
            this.type = type;
            this.target = target;
        }
    }

    public static class KeyPressedEvent extends PlayerEvent {

        public final int key;

        public final boolean isCtrlPressed;

        public final boolean isAltPressed;

        public final boolean isShiftPressed;

        public final boolean isMetaPressed;

        public final String openGui;

        public KeyPressedEvent(IPlayer player, int key, boolean isCtrlPressed, boolean isAltPressed, boolean isShiftPressed, boolean isMetaPressed, String openGui) {
            super(player);
            this.key = key;
            this.isCtrlPressed = isCtrlPressed;
            this.isAltPressed = isAltPressed;
            this.isShiftPressed = isShiftPressed;
            this.isMetaPressed = isMetaPressed;
            this.openGui = openGui;
        }
    }

    public static class KeyReleasedEvent extends PlayerEvent {

        public final int key;

        public final boolean isCtrlPressed;

        public final boolean isAltPressed;

        public final boolean isShiftPressed;

        public final boolean isMetaPressed;

        public final String openGui;

        public KeyReleasedEvent(IPlayer player, int key, boolean isCtrlPressed, boolean isAltPressed, boolean isShiftPressed, boolean isMetaPressed, String openGui) {
            super(player);
            this.key = key;
            this.isCtrlPressed = isCtrlPressed;
            this.isAltPressed = isAltPressed;
            this.isShiftPressed = isShiftPressed;
            this.isMetaPressed = isMetaPressed;
            this.openGui = openGui;
        }
    }

    public static class KilledEntityEvent extends PlayerEvent {

        public final IEntityLiving entity;

        public KilledEntityEvent(IPlayer player, LivingEntity entity) {
            super(player);
            this.entity = (IEntityLiving) NpcAPI.Instance().getIEntity(entity);
        }
    }

    public static class LevelUpEvent extends PlayerEvent {

        public final int change;

        public LevelUpEvent(IPlayer player, int change) {
            super(player);
            this.change = change;
        }
    }

    public static class LoginEvent extends PlayerEvent {

        public LoginEvent(IPlayer player) {
            super(player);
        }
    }

    public static class LogoutEvent extends PlayerEvent {

        public LogoutEvent(IPlayer player) {
            super(player);
        }
    }

    @Cancelable
    public static class PickUpEvent extends PlayerEvent {

        public final IItemStack item;

        public PickUpEvent(IPlayer player, IItemStack item) {
            super(player);
            this.item = item;
        }
    }

    public static class PlaySoundEvent extends PlayerEvent {

        public final String sound;

        public final String category;

        public final boolean looping;

        public PlaySoundEvent(IPlayer player, String sound, String category, boolean looping) {
            super(player);
            this.sound = sound;
            this.category = category;
            this.looping = looping;
        }
    }

    @Cancelable
    public static class RangedLaunchedEvent extends PlayerEvent {

        public RangedLaunchedEvent(IPlayer player) {
            super(player);
        }
    }

    public static class TimerEvent extends PlayerEvent {

        public final int id;

        public TimerEvent(IPlayer player, int id) {
            super(player);
            this.id = id;
        }
    }

    @Cancelable
    public static class TossEvent extends PlayerEvent {

        public final IItemStack item;

        public TossEvent(IPlayer player, IItemStack item) {
            super(player);
            this.item = item;
        }
    }

    public static class UpdateEvent extends PlayerEvent {

        public UpdateEvent(IPlayer player) {
            super(player);
        }
    }
}
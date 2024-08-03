package dev.architectury.event.events.common;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public interface PlayerEvent {

    Event<PlayerEvent.PlayerJoin> PLAYER_JOIN = EventFactory.createLoop();

    Event<PlayerEvent.PlayerQuit> PLAYER_QUIT = EventFactory.createLoop();

    Event<PlayerEvent.PlayerRespawn> PLAYER_RESPAWN = EventFactory.createLoop();

    Event<PlayerEvent.PlayerAdvancement> PLAYER_ADVANCEMENT = EventFactory.createLoop();

    Event<PlayerEvent.PlayerClone> PLAYER_CLONE = EventFactory.createLoop();

    Event<PlayerEvent.CraftItem> CRAFT_ITEM = EventFactory.createLoop();

    Event<PlayerEvent.SmeltItem> SMELT_ITEM = EventFactory.createLoop();

    Event<PlayerEvent.PickupItemPredicate> PICKUP_ITEM_PRE = EventFactory.createEventResult();

    Event<PlayerEvent.PickupItem> PICKUP_ITEM_POST = EventFactory.createLoop();

    Event<PlayerEvent.ChangeDimension> CHANGE_DIMENSION = EventFactory.createLoop();

    Event<PlayerEvent.DropItem> DROP_ITEM = EventFactory.createEventResult();

    Event<PlayerEvent.OpenMenu> OPEN_MENU = EventFactory.createLoop();

    Event<PlayerEvent.CloseMenu> CLOSE_MENU = EventFactory.createLoop();

    Event<PlayerEvent.FillBucket> FILL_BUCKET = EventFactory.createCompoundEventResult();

    Event<PlayerEvent.AttackEntity> ATTACK_ENTITY = EventFactory.createEventResult();

    public interface AttackEntity {

        EventResult attack(Player var1, Level var2, Entity var3, InteractionHand var4, @Nullable EntityHitResult var5);
    }

    public interface ChangeDimension {

        void change(ServerPlayer var1, ResourceKey<Level> var2, ResourceKey<Level> var3);
    }

    public interface CloseMenu {

        void close(Player var1, AbstractContainerMenu var2);
    }

    public interface CraftItem {

        void craft(Player var1, ItemStack var2, Container var3);
    }

    public interface DropItem {

        EventResult drop(Player var1, ItemEntity var2);
    }

    public interface FillBucket {

        CompoundEventResult<ItemStack> fill(Player var1, Level var2, ItemStack var3, @Nullable HitResult var4);
    }

    public interface OpenMenu {

        void open(Player var1, AbstractContainerMenu var2);
    }

    public interface PickupItem {

        void pickup(Player var1, ItemEntity var2, ItemStack var3);
    }

    public interface PickupItemPredicate {

        EventResult canPickup(Player var1, ItemEntity var2, ItemStack var3);
    }

    public interface PlayerAdvancement {

        void award(ServerPlayer var1, Advancement var2);
    }

    public interface PlayerClone {

        void clone(ServerPlayer var1, ServerPlayer var2, boolean var3);
    }

    public interface PlayerJoin {

        void join(ServerPlayer var1);
    }

    public interface PlayerQuit {

        void quit(ServerPlayer var1);
    }

    public interface PlayerRespawn {

        void respawn(ServerPlayer var1, boolean var2);
    }

    public interface SmeltItem {

        void smelt(Player var1, ItemStack var2);
    }
}
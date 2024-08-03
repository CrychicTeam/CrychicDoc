package dev.architectury.event.events.common;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface InteractionEvent {

    Event<InteractionEvent.LeftClickBlock> LEFT_CLICK_BLOCK = EventFactory.createEventResult();

    Event<InteractionEvent.RightClickBlock> RIGHT_CLICK_BLOCK = EventFactory.createEventResult();

    Event<InteractionEvent.RightClickItem> RIGHT_CLICK_ITEM = EventFactory.createCompoundEventResult();

    Event<InteractionEvent.ClientLeftClickAir> CLIENT_LEFT_CLICK_AIR = EventFactory.createLoop();

    Event<InteractionEvent.ClientRightClickAir> CLIENT_RIGHT_CLICK_AIR = EventFactory.createLoop();

    Event<InteractionEvent.InteractEntity> INTERACT_ENTITY = EventFactory.createEventResult();

    Event<InteractionEvent.FarmlandTrample> FARMLAND_TRAMPLE = EventFactory.createEventResult();

    public interface ClientLeftClickAir {

        void click(Player var1, InteractionHand var2);
    }

    public interface ClientRightClickAir {

        void click(Player var1, InteractionHand var2);
    }

    public interface FarmlandTrample {

        EventResult trample(Level var1, BlockPos var2, BlockState var3, float var4, Entity var5);
    }

    public interface InteractEntity {

        EventResult interact(Player var1, Entity var2, InteractionHand var3);
    }

    public interface LeftClickBlock {

        EventResult click(Player var1, InteractionHand var2, BlockPos var3, Direction var4);
    }

    public interface RightClickBlock {

        EventResult click(Player var1, InteractionHand var2, BlockPos var3, Direction var4);
    }

    public interface RightClickItem {

        CompoundEventResult<ItemStack> click(Player var1, InteractionHand var2);
    }
}
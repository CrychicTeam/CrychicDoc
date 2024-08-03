package com.simibubi.create;

import com.simibubi.create.content.contraptions.behaviour.BellMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.CampfireMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.dispenser.DispenserMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.dispenser.DropperMovementBehaviour;
import com.simibubi.create.foundation.utility.AttachedRegistry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class AllMovementBehaviours {

    private static final AttachedRegistry<Block, MovementBehaviour> BLOCK_BEHAVIOURS = new AttachedRegistry<>(ForgeRegistries.BLOCKS);

    private static final List<AllMovementBehaviours.BehaviourProvider> GLOBAL_BEHAVIOURS = new ArrayList();

    public static void registerBehaviour(ResourceLocation block, MovementBehaviour behaviour) {
        BLOCK_BEHAVIOURS.register(block, behaviour);
    }

    public static void registerBehaviour(Block block, MovementBehaviour behaviour) {
        BLOCK_BEHAVIOURS.register(block, behaviour);
    }

    public static void registerBehaviourProvider(AllMovementBehaviours.BehaviourProvider provider) {
        GLOBAL_BEHAVIOURS.add(provider);
    }

    @Nullable
    public static MovementBehaviour getBehaviour(BlockState state) {
        MovementBehaviour behaviour = BLOCK_BEHAVIOURS.get(state.m_60734_());
        if (behaviour != null) {
            return behaviour;
        } else {
            for (AllMovementBehaviours.BehaviourProvider provider : GLOBAL_BEHAVIOURS) {
                behaviour = provider.getBehaviour(state);
                if (behaviour != null) {
                    return behaviour;
                }
            }
            return null;
        }
    }

    public static <B extends Block> NonNullConsumer<? super B> movementBehaviour(MovementBehaviour behaviour) {
        return b -> registerBehaviour(b, behaviour);
    }

    static void registerDefaults() {
        registerBehaviour(Blocks.BELL, new BellMovementBehaviour());
        registerBehaviour(Blocks.CAMPFIRE, new CampfireMovementBehaviour());
        registerBehaviour(Blocks.SOUL_CAMPFIRE, new CampfireMovementBehaviour());
        DispenserMovementBehaviour.gatherMovedDispenseItemBehaviours();
        registerBehaviour(Blocks.DISPENSER, new DispenserMovementBehaviour());
        registerBehaviour(Blocks.DROPPER, new DropperMovementBehaviour());
    }

    public interface BehaviourProvider {

        @Nullable
        MovementBehaviour getBehaviour(BlockState var1);
    }
}
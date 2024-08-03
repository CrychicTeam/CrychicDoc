package com.simibubi.create;

import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.LeverMovingInteraction;
import com.simibubi.create.content.contraptions.behaviour.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.behaviour.TrapdoorMovingInteraction;
import com.simibubi.create.foundation.utility.AttachedRegistry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class AllInteractionBehaviours {

    private static final AttachedRegistry<Block, MovingInteractionBehaviour> BLOCK_BEHAVIOURS = new AttachedRegistry<>(ForgeRegistries.BLOCKS);

    private static final List<AllInteractionBehaviours.BehaviourProvider> GLOBAL_BEHAVIOURS = new ArrayList();

    public static void registerBehaviour(ResourceLocation block, MovingInteractionBehaviour provider) {
        BLOCK_BEHAVIOURS.register(block, provider);
    }

    public static void registerBehaviour(Block block, MovingInteractionBehaviour provider) {
        BLOCK_BEHAVIOURS.register(block, provider);
    }

    public static void registerBehaviourProvider(AllInteractionBehaviours.BehaviourProvider provider) {
        GLOBAL_BEHAVIOURS.add(provider);
    }

    @Nullable
    public static MovingInteractionBehaviour getBehaviour(BlockState state) {
        MovingInteractionBehaviour behaviour = BLOCK_BEHAVIOURS.get(state.m_60734_());
        if (behaviour != null) {
            return behaviour;
        } else {
            for (AllInteractionBehaviours.BehaviourProvider provider : GLOBAL_BEHAVIOURS) {
                behaviour = provider.getBehaviour(state);
                if (behaviour != null) {
                    return behaviour;
                }
            }
            return null;
        }
    }

    public static <B extends Block> NonNullConsumer<? super B> interactionBehaviour(MovingInteractionBehaviour behaviour) {
        return b -> registerBehaviour(b, behaviour);
    }

    static void registerDefaults() {
        registerBehaviour(Blocks.LEVER, new LeverMovingInteraction());
        DoorMovingInteraction doorBehaviour = new DoorMovingInteraction();
        registerBehaviourProvider(state -> state.m_204336_(BlockTags.WOODEN_DOORS) ? doorBehaviour : null);
        TrapdoorMovingInteraction trapdoorBehaviour = new TrapdoorMovingInteraction();
        registerBehaviourProvider(state -> state.m_204336_(BlockTags.WOODEN_TRAPDOORS) ? trapdoorBehaviour : null);
    }

    public interface BehaviourProvider {

        @Nullable
        MovingInteractionBehaviour getBehaviour(BlockState var1);
    }
}
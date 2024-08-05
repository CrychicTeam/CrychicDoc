package com.simibubi.create.foundation.blockEntity.behaviour.edgeInteraction;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class EdgeInteractionBehaviour extends BlockEntityBehaviour {

    public static final BehaviourType<EdgeInteractionBehaviour> TYPE = new BehaviourType<>();

    EdgeInteractionBehaviour.ConnectionCallback connectionCallback;

    EdgeInteractionBehaviour.ConnectivityPredicate connectivityPredicate;

    Optional<Item> requiredItem;

    public EdgeInteractionBehaviour(SmartBlockEntity be, EdgeInteractionBehaviour.ConnectionCallback callback) {
        super(be);
        this.connectionCallback = callback;
        this.requiredItem = Optional.empty();
        this.connectivityPredicate = (world, pos, face, face2) -> true;
    }

    public EdgeInteractionBehaviour connectivity(EdgeInteractionBehaviour.ConnectivityPredicate pred) {
        this.connectivityPredicate = pred;
        return this;
    }

    public EdgeInteractionBehaviour require(Item item) {
        this.requiredItem = Optional.of(item);
        return this;
    }

    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }

    @FunctionalInterface
    public interface ConnectionCallback {

        void apply(Level var1, BlockPos var2, BlockPos var3);
    }

    @FunctionalInterface
    public interface ConnectivityPredicate {

        boolean test(Level var1, BlockPos var2, Direction var3, Direction var4);
    }
}
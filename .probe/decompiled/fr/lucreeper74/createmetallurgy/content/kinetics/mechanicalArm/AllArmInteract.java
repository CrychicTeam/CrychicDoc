package fr.lucreeper74.createmetallurgy.content.kinetics.mechanicalArm;

import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import fr.lucreeper74.createmetallurgy.registries.CMBlocks;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AllArmInteract extends AllArmInteractionPointTypes {

    public static final AllArmInteract.FoundryBasinType FOUNDRY_BASIN = register("foundry_basin", AllArmInteract.FoundryBasinType::new);

    public static final AllArmInteract.CastingBasinType CASTING_BASIN = register("casting_basin", AllArmInteract.CastingBasinType::new);

    public static final AllArmInteract.CastingTableType CASTING_TABLE = register("casting_table", AllArmInteract.CastingTableType::new);

    public static final AllArmInteract.BeltGrinderType BELT_GRINDER = register("belt_grinder", AllArmInteract.BeltGrinderType::new);

    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = (T) factory.apply(CreateMetallurgy.genRL(id));
        ArmInteractionPointType.register(type);
        return type;
    }

    public static void register() {
    }

    public static class BeltGrinderType extends ArmInteractionPointType {

        public BeltGrinderType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return CMBlocks.BELT_GRINDER_BLOCK.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmInteractionPoint(this, level, pos, state);
        }
    }

    public static class CastingBasinType extends ArmInteractionPointType {

        public CastingBasinType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return CMBlocks.CASTING_BASIN_BLOCK.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmInteractionPoint(this, level, pos, state);
        }
    }

    public static class CastingTableType extends ArmInteractionPointType {

        public CastingTableType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return CMBlocks.CASTING_TABLE_BLOCK.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmInteractionPoint(this, level, pos, state);
        }
    }

    public static class FoundryBasinType extends ArmInteractionPointType {

        public FoundryBasinType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return CMBlocks.FOUNDRY_BASIN_BLOCK.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return new ArmInteractionPoint(this, level, pos, state);
        }
    }
}
package fr.lucreeper74.createmetallurgy.registries;

import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CMArmInteract extends AllArmInteractionPointTypes {

    public static final CMArmInteract.FoundryBasinType FOUNDRY_BASIN = register("foundry_basin", CMArmInteract.FoundryBasinType::new);

    public static final CMArmInteract.CastingBasinType CASTING_BASIN = register("casting_basin", CMArmInteract.CastingBasinType::new);

    public static final CMArmInteract.CastingTableType CASTING_TABLE = register("casting_table", CMArmInteract.CastingTableType::new);

    public static final CMArmInteract.BeltGrinderType BELT_GRINDER = register("belt_grinder", CMArmInteract.BeltGrinderType::new);

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
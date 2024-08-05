package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.BlockStructure;
import fr.frinn.custommachinery.common.util.PartialBlockState;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class StructureMachineComponent extends AbstractMachineComponent {

    public StructureMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.NONE);
    }

    @Override
    public MachineComponentType<StructureMachineComponent> getType() {
        return (MachineComponentType<StructureMachineComponent>) Registration.STRUCTURE_MACHINE_COMPONENT.get();
    }

    public boolean checkStructure(BlockStructure pattern) {
        return pattern.match(this.getManager().getTile().m_58904_(), this.getManager().getTile().m_58899_(), (Direction) this.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING));
    }

    public void destroyStructure(BlockStructure pattern, boolean drops) {
        pattern.getBlocks((Direction) this.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING)).forEach((pos, ingredient) -> {
            if (!ingredient.test(PartialBlockState.MACHINE) && !ingredient.test(PartialBlockState.ANY)) {
                this.getManager().getLevel().m_46961_(pos.offset(this.getManager().getTile().m_58899_()), drops);
            }
        });
    }

    public void placeStructure(BlockStructure pattern, boolean drops) {
        pattern.getBlocks((Direction) this.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING)).forEach((pos, ingredient) -> {
            BlockPos worldPos = pos.offset(this.getManager().getTile().m_58899_());
            if (pos != BlockPos.ZERO && !ingredient.test(PartialBlockState.ANY)) {
                if (!this.getManager().getLevel().getBlockState(worldPos).m_60795_()) {
                    this.getManager().getLevel().m_46961_(worldPos, drops);
                }
                this.setBlock(this.getManager().getLevel(), worldPos, (PartialBlockState) ingredient.getAll().get(0));
            }
        });
    }

    private void setBlock(Level world, BlockPos pos, PartialBlockState state) {
        world.setBlockAndUpdate(pos, state.getBlockState());
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null && state.getNbt() != null && !state.getNbt().isEmpty()) {
            CompoundTag nbt = state.getNbt().copy();
            nbt.putInt("x", pos.m_123341_());
            nbt.putInt("y", pos.m_123342_());
            nbt.putInt("z", pos.m_123343_());
            tile.load(nbt);
        }
    }
}
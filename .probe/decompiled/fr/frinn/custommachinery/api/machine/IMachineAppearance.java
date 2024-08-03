package fr.frinn.custommachinery.api.machine;

import fr.frinn.custommachinery.impl.util.IMachineModelLocation;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface IMachineAppearance {

    <T> T getProperty(MachineAppearanceProperty<T> var1);

    IMachineModelLocation getBlockModel();

    IMachineModelLocation getItemModel();

    SoundEvent getAmbientSound();

    SoundType getInteractionSound();

    int getLightLevel();

    int getColor();

    float getHardness();

    float getResistance();

    List<TagKey<Block>> getTool();

    TagKey<Block> getMiningLevel();

    boolean requiresCorrectToolForDrops();

    Function<Direction, VoxelShape> getShape();

    Function<Direction, VoxelShape> getCollisionShape();

    IMachineAppearance copy();
}
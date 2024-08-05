package se.mickelus.tetra.blocks.forged.chthonic;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

@ParametersAreNonnullByDefault
public class ChthonicExtractorTile extends BlockEntity {

    private static final String damageKey = "dmg";

    public static RegistryObject<BlockEntityType<ChthonicExtractorTile>> type;

    private int damage = 0;

    public ChthonicExtractorTile(BlockPos blockPos0, BlockState blockState1) {
        super(type.get(), blockPos0, blockState1);
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
        this.m_6596_();
    }

    public void damage(int amount) {
        int newDamage = this.getDamage() + amount;
        if (newDamage < 1024) {
            this.setDamage(newDamage);
        } else {
            this.f_58857_.m_5898_(null, 2001, this.m_58899_(), Block.getId(this.f_58857_.getBlockState(this.m_58899_())));
            this.f_58857_.setBlock(this.m_58899_(), Blocks.AIR.defaultBlockState(), 2);
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (compound.contains("dmg")) {
            this.damage = compound.getInt("dmg");
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("dmg", this.damage);
    }
}
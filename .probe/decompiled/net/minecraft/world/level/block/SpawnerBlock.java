package net.minecraft.world.level.block;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SpawnerBlock extends BaseEntityBlock {

    protected SpawnerBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new SpawnerBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.MOB_SPAWNER, level0.isClientSide ? SpawnerBlockEntity::m_155754_ : SpawnerBlockEntity::m_155761_);
    }

    @Override
    public void spawnAfterBreak(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, ItemStack itemStack3, boolean boolean4) {
        super.m_213646_(blockState0, serverLevel1, blockPos2, itemStack3, boolean4);
        if (boolean4) {
            int $$5 = 15 + serverLevel1.f_46441_.nextInt(15) + serverLevel1.f_46441_.nextInt(15);
            this.m_49805_(serverLevel1, blockPos2, $$5);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable BlockGetter blockGetter1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.m_5871_(itemStack0, blockGetter1, listComponent2, tooltipFlag3);
        Optional<Component> $$4 = this.getSpawnEntityDisplayName(itemStack0);
        if ($$4.isPresent()) {
            listComponent2.add((Component) $$4.get());
        } else {
            listComponent2.add(CommonComponents.EMPTY);
            listComponent2.add(Component.translatable("block.minecraft.spawner.desc1").withStyle(ChatFormatting.GRAY));
            listComponent2.add(CommonComponents.space().append(Component.translatable("block.minecraft.spawner.desc2").withStyle(ChatFormatting.BLUE)));
        }
    }

    private Optional<Component> getSpawnEntityDisplayName(ItemStack itemStack0) {
        CompoundTag $$1 = BlockItem.getBlockEntityData(itemStack0);
        if ($$1 != null && $$1.contains("SpawnData", 10)) {
            String $$2 = $$1.getCompound("SpawnData").getCompound("entity").getString("id");
            ResourceLocation $$3 = ResourceLocation.tryParse($$2);
            if ($$3 != null) {
                return BuiltInRegistries.ENTITY_TYPE.m_6612_($$3).map(p_255782_ -> Component.translatable(p_255782_.getDescriptionId()).withStyle(ChatFormatting.GRAY));
            }
        }
        return Optional.empty();
    }
}
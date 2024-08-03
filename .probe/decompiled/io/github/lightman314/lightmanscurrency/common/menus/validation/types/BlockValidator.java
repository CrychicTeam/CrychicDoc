package io.github.lightman314.lightmanscurrency.common.menus.validation.types;

import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidatorType;
import io.github.lightman314.lightmanscurrency.common.util.TagUtil;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockValidator extends MenuValidator {

    public static final MenuValidatorType TYPE = new BlockValidator.Type();

    private final BlockPos pos;

    private final Block block;

    protected BlockValidator(@Nonnull BlockPos pos, @Nonnull Block block) {
        super(TYPE);
        this.pos = pos;
        this.block = block;
    }

    public static MenuValidator of(@Nonnull BlockPos pos, @Nonnull Block block) {
        return new BlockValidator(pos, block);
    }

    @Override
    protected void encodeAdditional(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeUtf(ForgeRegistries.BLOCKS.getKey(this.block).toString());
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.put("Position", TagUtil.saveBlockPos(this.pos));
        tag.putString("Block", ForgeRegistries.BLOCKS.getKey(this.block).toString());
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return player.m_9236_().getBlockState(this.pos).m_60713_(this.block) && player.m_20275_((double) this.pos.m_123341_() + 0.5, (double) this.pos.m_123342_() + 0.5, (double) this.pos.m_123343_() + 0.5) <= 64.0;
    }

    private static final class Type extends MenuValidatorType {

        private Type() {
            super(new ResourceLocation("lightmanscurrency", "block"));
        }

        @Nonnull
        @Override
        public MenuValidator decode(@Nonnull FriendlyByteBuf buffer) {
            return BlockValidator.of(buffer.readBlockPos(), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(buffer.readUtf())));
        }

        @Nonnull
        @Override
        public MenuValidator load(@Nonnull CompoundTag tag) {
            return BlockValidator.of(TagUtil.loadBlockPos(tag.getCompound("Position")), ForgeRegistries.BLOCKS.getValue(new ResourceLocation(tag.getString("Block"))));
        }
    }
}
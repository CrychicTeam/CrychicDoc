package net.mehvahdjukaar.supplementaries.common.items;

import net.mehvahdjukaar.supplementaries.common.entities.BombEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class BombItem extends Item {

    private final BombEntity.BombType type;

    private final boolean glint;

    public BombItem(Item.Properties builder) {
        this(builder, BombEntity.BombType.NORMAL, false);
    }

    public BombItem(Item.Properties builder, BombEntity.BombType type, boolean glint) {
        super(builder);
        this.type = type;
        this.glint = glint;
    }

    public BombEntity.BombType getType() {
        return this.type;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.glint;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.type == BombEntity.BombType.BLUE ? Rarity.EPIC : Rarity.RARE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        worldIn.playSound(null, playerIn.m_20185_(), playerIn.m_20186_(), playerIn.m_20189_(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
        playerIn.getCooldowns().addCooldown(this, 30);
        if (!worldIn.isClientSide) {
            BombEntity bombEntity = new BombEntity(worldIn, playerIn, this.type);
            float pitch = -10.0F;
            bombEntity.m_37251_(playerIn, playerIn.m_146909_(), playerIn.m_146908_(), pitch, 1.25F, 0.9F);
            worldIn.m_7967_(bombEntity);
        }
        playerIn.awardStat(Stats.ITEM_USED.get(this));
        if (!playerIn.getAbilities().instabuild) {
            itemstack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
    }
}
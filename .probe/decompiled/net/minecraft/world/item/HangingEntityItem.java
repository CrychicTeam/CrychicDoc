package net.minecraft.world.item;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class HangingEntityItem extends Item {

    private static final Component TOOLTIP_RANDOM_VARIANT = Component.translatable("painting.random").withStyle(ChatFormatting.GRAY);

    private final EntityType<? extends HangingEntity> type;

    public HangingEntityItem(EntityType<? extends HangingEntity> entityTypeExtendsHangingEntity0, Item.Properties itemProperties1) {
        super(itemProperties1);
        this.type = entityTypeExtendsHangingEntity0;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        BlockPos $$1 = useOnContext0.getClickedPos();
        Direction $$2 = useOnContext0.getClickedFace();
        BlockPos $$3 = $$1.relative($$2);
        Player $$4 = useOnContext0.getPlayer();
        ItemStack $$5 = useOnContext0.getItemInHand();
        if ($$4 != null && !this.mayPlace($$4, $$2, $$5, $$3)) {
            return InteractionResult.FAIL;
        } else {
            Level $$6 = useOnContext0.getLevel();
            HangingEntity $$8;
            if (this.type == EntityType.PAINTING) {
                Optional<Painting> $$7 = Painting.create($$6, $$3, $$2);
                if ($$7.isEmpty()) {
                    return InteractionResult.CONSUME;
                }
                $$8 = (HangingEntity) $$7.get();
            } else if (this.type == EntityType.ITEM_FRAME) {
                $$8 = new ItemFrame($$6, $$3, $$2);
            } else {
                if (this.type != EntityType.GLOW_ITEM_FRAME) {
                    return InteractionResult.sidedSuccess($$6.isClientSide);
                }
                $$8 = new GlowItemFrame($$6, $$3, $$2);
            }
            CompoundTag $$12 = $$5.getTag();
            if ($$12 != null) {
                EntityType.updateCustomEntityTag($$6, $$4, $$8, $$12);
            }
            if ($$8.survives()) {
                if (!$$6.isClientSide) {
                    $$8.playPlacementSound();
                    $$6.m_220400_($$4, GameEvent.ENTITY_PLACE, $$8.m_20182_());
                    $$6.m_7967_($$8);
                }
                $$5.shrink(1);
                return InteractionResult.sidedSuccess($$6.isClientSide);
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    protected boolean mayPlace(Player player0, Direction direction1, ItemStack itemStack2, BlockPos blockPos3) {
        return !direction1.getAxis().isVertical() && player0.mayUseItemAt(blockPos3, direction1, itemStack2);
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.appendHoverText(itemStack0, level1, listComponent2, tooltipFlag3);
        if (this.type == EntityType.PAINTING) {
            CompoundTag $$4 = itemStack0.getTag();
            if ($$4 != null && $$4.contains("EntityTag", 10)) {
                CompoundTag $$5 = $$4.getCompound("EntityTag");
                Painting.loadVariant($$5).ifPresentOrElse(p_270767_ -> {
                    p_270767_.unwrapKey().ifPresent(p_270217_ -> {
                        listComponent2.add(Component.translatable(p_270217_.location().toLanguageKey("painting", "title")).withStyle(ChatFormatting.YELLOW));
                        listComponent2.add(Component.translatable(p_270217_.location().toLanguageKey("painting", "author")).withStyle(ChatFormatting.GRAY));
                    });
                    listComponent2.add(Component.translatable("painting.dimensions", Mth.positiveCeilDiv(((PaintingVariant) p_270767_.value()).getWidth(), 16), Mth.positiveCeilDiv(((PaintingVariant) p_270767_.value()).getHeight(), 16)));
                }, () -> listComponent2.add(TOOLTIP_RANDOM_VARIANT));
            } else if (tooltipFlag3.isCreative()) {
                listComponent2.add(TOOLTIP_RANDOM_VARIANT);
            }
        }
    }
}
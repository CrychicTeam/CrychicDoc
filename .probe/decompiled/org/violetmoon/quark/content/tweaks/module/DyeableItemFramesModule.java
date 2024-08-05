package org.violetmoon.quark.content.tweaks.module;

import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tweaks.client.render.entity.DyedItemFrameRenderer;
import org.violetmoon.quark.content.tweaks.entity.DyedItemFrame;
import org.violetmoon.zeta.client.event.load.ZAddModels;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickBlock;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tweaks")
public class DyeableItemFramesModule extends ZetaModule {

    public static EntityType<DyedItemFrame> entityType;

    @Hint(key = "item_frame_dyeing")
    List<Item> itemFrames = Arrays.asList(Items.ITEM_FRAME, Items.GLOW_ITEM_FRAME);

    @LoadEvent
    public final void register(ZRegister event) {
        entityType = EntityType.Builder.of(DyedItemFrame::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE).setShouldReceiveVelocityUpdates(false).setCustomClientFactory((spawnEntity, world) -> new DyedItemFrame(entityType, world)).build("dyed_item_frame");
        event.getRegistry().register(entityType, "dyed_item_frame", Registries.ENTITY_TYPE);
        Quark.ZETA.dyeables.register(Items.ITEM_FRAME, this);
        Quark.ZETA.dyeables.register(Items.GLOW_ITEM_FRAME, this);
    }

    @PlayEvent
    public void onUse(ZRightClickBlock event) {
        Player player = event.getPlayer();
        InteractionHand hand = event.getHand();
        ItemStack stack = player.m_21120_(hand);
        if ((stack.is(Items.ITEM_FRAME) || stack.is(Items.GLOW_ITEM_FRAME)) && Quark.ZETA.dyeables.isDyed(stack)) {
            BlockHitResult blockhit = event.getHitVec();
            UseOnContext context = new UseOnContext(player, hand, blockhit);
            Level level = player.m_9236_();
            BlockPos pos = event.getPos();
            BlockState state = level.getBlockState(pos);
            InteractionResult result = player.m_6047_() ? InteractionResult.PASS : state.m_60664_(level, player, hand, blockhit);
            if (result == InteractionResult.PASS) {
                result = this.useOn(context);
            }
            if (result != InteractionResult.PASS) {
                event.setCanceled(true);
                event.setCancellationResult(result);
            }
        }
    }

    private InteractionResult useOn(UseOnContext context) {
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos blockpos1 = blockpos.relative(direction);
        Player player = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();
        if (player != null && !this.mayPlace(player, direction, itemstack, blockpos1)) {
            return InteractionResult.FAIL;
        } else {
            Level level = context.getLevel();
            HangingEntity hangingentity = new DyedItemFrame(level, blockpos1, direction, Quark.ZETA.dyeables.getDye(itemstack), itemstack.is(Items.GLOW_ITEM_FRAME));
            CompoundTag compoundtag = itemstack.getTag();
            if (compoundtag != null) {
                EntityType.updateCustomEntityTag(level, player, hangingentity, compoundtag);
            }
            if (hangingentity.survives()) {
                if (!level.isClientSide) {
                    hangingentity.playPlacementSound();
                    level.m_220400_(player, GameEvent.ENTITY_PLACE, hangingentity.m_20182_());
                    level.m_7967_(hangingentity);
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.CONSUME;
            }
        }
    }

    protected boolean mayPlace(Player player, Direction direction, ItemStack stack, BlockPos pos) {
        return !player.m_9236_().m_151570_(pos) && player.mayUseItemAt(pos, direction, stack);
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends DyeableItemFramesModule {

        @LoadEvent
        public void registerAdditionalModels(ZAddModels event) {
            event.register(new ModelResourceLocation("quark", "extra/dyed_item_frame", "inventory"));
            event.register(new ModelResourceLocation("quark", "extra/dyed_item_frame_map", "inventory"));
        }

        @LoadEvent
        public final void clientSetup(ZClientSetup event) {
            EntityRenderers.register(entityType, DyedItemFrameRenderer::new);
        }
    }
}
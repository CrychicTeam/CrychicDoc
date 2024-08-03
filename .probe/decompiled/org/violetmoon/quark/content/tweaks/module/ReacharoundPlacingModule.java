package org.violetmoon.quark.content.tweaks.module;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.client.event.play.ZClientTick;
import org.violetmoon.zeta.client.event.play.ZRenderGuiOverlay;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZPhase;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickItem;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class ReacharoundPlacingModule extends ZetaModule {

    public static final ResourceLocation OVERLAY_HORIZONTAL = new ResourceLocation("quark", "textures/gui/reacharound_overlay_horizontal.png");

    public static final ResourceLocation OVERLAY_VERTICAL = new ResourceLocation("quark", "textures/gui/reacharound_overlay_vertical.png");

    @Config
    @Config.Min(0.0)
    @Config.Max(1.0)
    public double leniency = 0.5;

    @Config
    public List<String> whitelist = Lists.newArrayList();

    @Config
    public List<String> blacklist = Lists.newArrayList();

    protected ReacharoundPlacingModule.ReacharoundTarget currentTarget;

    protected int ticksDisplayed;

    public static TagKey<Item> reacharoundTag;

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        reacharoundTag = ItemTags.create(new ResourceLocation("quark", "reacharound_able"));
    }

    @PlayEvent
    public void onRightClick(ZRightClickItem event) {
        Player player = event.getEntity();
        ReacharoundPlacingModule.ReacharoundTarget target = this.getPlayerReacharoundTarget(player);
        if (target != null && event.getHand() == target.hand) {
            ItemStack stack = event.getItemStack();
            if (!player.mayUseItemAt(target.pos, target.dir, stack) || !player.m_9236_().mayInteract(player, target.pos)) {
                return;
            }
            if (!Quark.FLAN_INTEGRATION.canPlace(player, target.pos)) {
                return;
            }
            int count = stack.getCount();
            InteractionHand hand = event.getHand();
            UseOnContext context = new UseOnContext(player, hand, new BlockHitResult(new Vec3(0.5, 1.0, 0.5), target.dir, target.pos, false));
            boolean remote = player.m_9236_().isClientSide;
            InteractionResult res = remote ? InteractionResult.SUCCESS : stack.useOn(context);
            if (res != InteractionResult.PASS) {
                event.setCanceled(true);
                event.setCancellationResult(res);
                if (res == InteractionResult.SUCCESS) {
                    player.m_6674_(hand);
                } else if (res == InteractionResult.CONSUME) {
                    BlockPos placedPos = target.pos;
                    BlockState state = player.m_9236_().getBlockState(placedPos);
                    SoundType soundtype = state.getSoundType(player.m_9236_(), placedPos, context.getPlayer());
                    if (player.m_9236_() instanceof ServerLevel) {
                        player.m_9236_().playSound(null, placedPos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    }
                }
                if (player.getAbilities().instabuild && stack.getCount() < count && !remote) {
                    stack.setCount(count);
                }
            }
        }
    }

    protected ReacharoundPlacingModule.ReacharoundTarget getPlayerReacharoundTarget(Player player) {
        InteractionHand hand = null;
        if (this.validateReacharoundStack(player.m_21205_())) {
            hand = InteractionHand.MAIN_HAND;
        } else if (this.validateReacharoundStack(player.m_21206_())) {
            hand = InteractionHand.OFF_HAND;
        }
        if (hand == null) {
            return null;
        } else {
            Level world = player.m_9236_();
            Pair<Vec3, Vec3> params = Quark.ZETA.raytracingUtil.getEntityParams(player);
            double range = Quark.ZETA.raytracingUtil.getEntityRange(player);
            Vec3 rayPos = (Vec3) params.getLeft();
            Vec3 ray = ((Vec3) params.getRight()).scale(range);
            HitResult normalRes = Quark.ZETA.raytracingUtil.rayTrace(player, world, rayPos, ray, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
            if (normalRes.getType() == HitResult.Type.MISS) {
                ReacharoundPlacingModule.ReacharoundTarget target = this.getPlayerVerticalReacharoundTarget(player, hand, world, rayPos, ray);
                return target != null ? target : this.getPlayerHorizontalReacharoundTarget(player, hand, world, rayPos, ray);
            } else {
                return null;
            }
        }
    }

    private ReacharoundPlacingModule.ReacharoundTarget getPlayerVerticalReacharoundTarget(Player player, InteractionHand hand, Level world, Vec3 rayPos, Vec3 ray) {
        if (player.m_146909_() < 0.0F) {
            return null;
        } else {
            rayPos = rayPos.add(0.0, this.leniency, 0.0);
            HitResult take2Res = Quark.ZETA.raytracingUtil.rayTrace(player, world, rayPos, ray, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
            if (take2Res.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = ((BlockHitResult) take2Res).getBlockPos().below();
                BlockState state = world.getBlockState(pos);
                if (player.m_20182_().y - (double) pos.m_123342_() > 1.0 && (world.m_46859_(pos) || state.m_247087_())) {
                    return new ReacharoundPlacingModule.ReacharoundTarget(pos, Direction.DOWN, hand);
                }
            }
            return null;
        }
    }

    private ReacharoundPlacingModule.ReacharoundTarget getPlayerHorizontalReacharoundTarget(Player player, InteractionHand hand, Level world, Vec3 rayPos, Vec3 ray) {
        Direction dir = Direction.fromYRot((double) player.m_146908_());
        rayPos = rayPos.subtract(this.leniency * (double) dir.getStepX(), 0.0, this.leniency * (double) dir.getStepZ());
        HitResult take2Res = Quark.ZETA.raytracingUtil.rayTrace(player, world, rayPos, ray, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE);
        if (take2Res.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) take2Res).getBlockPos().relative(dir);
            BlockState state = world.getBlockState(pos);
            if (world.m_46859_(pos) || state.m_247087_()) {
                return new ReacharoundPlacingModule.ReacharoundTarget(pos, dir.getOpposite(), hand);
            }
        }
        return null;
    }

    private boolean validateReacharoundStack(ItemStack stack) {
        Item item = stack.getItem();
        String name = BuiltInRegistries.ITEM.getKey(item).toString();
        return this.blacklist.contains(name) ? false : item instanceof BlockItem || stack.is(reacharoundTag) || this.whitelist.contains(name);
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends ReacharoundPlacingModule {

        @PlayEvent
        public void onRender(ZRenderGuiOverlay.Crosshair.Post event) {
            GuiGraphics guiGraphics = event.getGuiGraphics();
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (!mc.options.hideGui) {
                if (mc.hitResult instanceof BlockHitResult bhr) {
                    BlockPos hitPos = bhr.getBlockPos();
                    BlockState stateAt = player.m_9236_().getBlockState(hitPos);
                    if (!stateAt.m_60795_()) {
                        return;
                    }
                }
                if (player != null && this.currentTarget != null) {
                    Window res = event.getWindow();
                    PoseStack matrix = event.getGuiGraphics().pose();
                    boolean vertical = this.currentTarget.dir.getAxis() == Direction.Axis.Y;
                    ResourceLocation texture = vertical ? OVERLAY_VERTICAL : OVERLAY_HORIZONTAL;
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    matrix.pushPose();
                    int x = (res.getGuiScaledWidth() - 15) / 2;
                    int y = (res.getGuiScaledHeight() - 15) / 2;
                    guiGraphics.blit(texture, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
                    matrix.popPose();
                    RenderSystem.defaultBlendFunc();
                }
            }
        }

        @PlayEvent
        public void clientTick(ZClientTick event) {
            if (event.getPhase() == ZPhase.END) {
                this.currentTarget = null;
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    this.currentTarget = this.getPlayerReacharoundTarget(player);
                }
                if (this.currentTarget != null) {
                    if (this.ticksDisplayed < 5) {
                        this.ticksDisplayed++;
                    }
                } else {
                    this.ticksDisplayed = 0;
                }
            }
        }
    }

    protected static record ReacharoundTarget(BlockPos pos, Direction dir, InteractionHand hand) {
    }
}
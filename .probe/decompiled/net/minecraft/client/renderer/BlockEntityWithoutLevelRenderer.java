package net.minecraft.client.renderer;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityWithoutLevelRenderer implements ResourceManagerReloadListener {

    private static final ShulkerBoxBlockEntity[] SHULKER_BOXES = (ShulkerBoxBlockEntity[]) Arrays.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::m_41060_)).map(p_172557_ -> new ShulkerBoxBlockEntity(p_172557_, BlockPos.ZERO, Blocks.SHULKER_BOX.defaultBlockState())).toArray(ShulkerBoxBlockEntity[]::new);

    private static final ShulkerBoxBlockEntity DEFAULT_SHULKER_BOX = new ShulkerBoxBlockEntity(BlockPos.ZERO, Blocks.SHULKER_BOX.defaultBlockState());

    private final ChestBlockEntity chest = new ChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());

    private final ChestBlockEntity trappedChest = new TrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());

    private final EnderChestBlockEntity enderChest = new EnderChestBlockEntity(BlockPos.ZERO, Blocks.ENDER_CHEST.defaultBlockState());

    private final BannerBlockEntity banner = new BannerBlockEntity(BlockPos.ZERO, Blocks.WHITE_BANNER.defaultBlockState());

    private final BedBlockEntity bed = new BedBlockEntity(BlockPos.ZERO, Blocks.RED_BED.defaultBlockState());

    private final ConduitBlockEntity conduit = new ConduitBlockEntity(BlockPos.ZERO, Blocks.CONDUIT.defaultBlockState());

    private final DecoratedPotBlockEntity decoratedPot = new DecoratedPotBlockEntity(BlockPos.ZERO, Blocks.DECORATED_POT.defaultBlockState());

    private ShieldModel shieldModel;

    private TridentModel tridentModel;

    private Map<SkullBlock.Type, SkullModelBase> skullModels;

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    private final EntityModelSet entityModelSet;

    public BlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher0, EntityModelSet entityModelSet1) {
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher0;
        this.entityModelSet = entityModelSet1;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
        this.tridentModel = new TridentModel(this.entityModelSet.bakeLayer(ModelLayers.TRIDENT));
        this.skullModels = SkullBlockRenderer.createSkullRenderers(this.entityModelSet);
    }

    public void renderByItem(ItemStack itemStack0, ItemDisplayContext itemDisplayContext1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        Item $$6 = itemStack0.getItem();
        if ($$6 instanceof BlockItem) {
            Block $$7 = ((BlockItem) $$6).getBlock();
            if ($$7 instanceof AbstractSkullBlock) {
                GameProfile $$8 = null;
                if (itemStack0.hasTag()) {
                    CompoundTag $$9 = itemStack0.getTag();
                    if ($$9.contains("SkullOwner", 10)) {
                        $$8 = NbtUtils.readGameProfile($$9.getCompound("SkullOwner"));
                    } else if ($$9.contains("SkullOwner", 8) && !Util.isBlank($$9.getString("SkullOwner"))) {
                        $$8 = new GameProfile(null, $$9.getString("SkullOwner"));
                        $$9.remove("SkullOwner");
                        SkullBlockEntity.updateGameprofile($$8, p_172560_ -> $$9.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_172560_)));
                    }
                }
                SkullBlock.Type $$10 = ((AbstractSkullBlock) $$7).getType();
                SkullModelBase $$11 = (SkullModelBase) this.skullModels.get($$10);
                RenderType $$12 = SkullBlockRenderer.getRenderType($$10, $$8);
                SkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack2, multiBufferSource3, int4, $$11, $$12);
            } else {
                BlockState $$13 = $$7.defaultBlockState();
                BlockEntity $$14;
                if ($$7 instanceof AbstractBannerBlock) {
                    this.banner.fromItem(itemStack0, ((AbstractBannerBlock) $$7).getColor());
                    $$14 = this.banner;
                } else if ($$7 instanceof BedBlock) {
                    this.bed.setColor(((BedBlock) $$7).getColor());
                    $$14 = this.bed;
                } else if ($$13.m_60713_(Blocks.CONDUIT)) {
                    $$14 = this.conduit;
                } else if ($$13.m_60713_(Blocks.CHEST)) {
                    $$14 = this.chest;
                } else if ($$13.m_60713_(Blocks.ENDER_CHEST)) {
                    $$14 = this.enderChest;
                } else if ($$13.m_60713_(Blocks.TRAPPED_CHEST)) {
                    $$14 = this.trappedChest;
                } else if ($$13.m_60713_(Blocks.DECORATED_POT)) {
                    this.decoratedPot.setFromItem(itemStack0);
                    $$14 = this.decoratedPot;
                } else {
                    if (!($$7 instanceof ShulkerBoxBlock)) {
                        return;
                    }
                    DyeColor $$21 = ShulkerBoxBlock.getColorFromItem($$6);
                    if ($$21 == null) {
                        $$14 = DEFAULT_SHULKER_BOX;
                    } else {
                        $$14 = SHULKER_BOXES[$$21.getId()];
                    }
                }
                this.blockEntityRenderDispatcher.renderItem($$14, poseStack2, multiBufferSource3, int4, int5);
            }
        } else {
            if (itemStack0.is(Items.SHIELD)) {
                boolean $$25 = BlockItem.getBlockEntityData(itemStack0) != null;
                poseStack2.pushPose();
                poseStack2.scale(1.0F, -1.0F, -1.0F);
                Material $$26 = $$25 ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
                VertexConsumer $$27 = $$26.sprite().wrap(ItemRenderer.getFoilBufferDirect(multiBufferSource3, this.shieldModel.m_103119_($$26.atlasLocation()), true, itemStack0.hasFoil()));
                this.shieldModel.handle().render(poseStack2, $$27, int4, int5, 1.0F, 1.0F, 1.0F, 1.0F);
                if ($$25) {
                    List<Pair<Holder<BannerPattern>, DyeColor>> $$28 = BannerBlockEntity.createPatterns(ShieldItem.getColor(itemStack0), BannerBlockEntity.getItemPatterns(itemStack0));
                    BannerRenderer.renderPatterns(poseStack2, multiBufferSource3, int4, int5, this.shieldModel.plate(), $$26, false, $$28, itemStack0.hasFoil());
                } else {
                    this.shieldModel.plate().render(poseStack2, $$27, int4, int5, 1.0F, 1.0F, 1.0F, 1.0F);
                }
                poseStack2.popPose();
            } else if (itemStack0.is(Items.TRIDENT)) {
                poseStack2.pushPose();
                poseStack2.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer $$29 = ItemRenderer.getFoilBufferDirect(multiBufferSource3, this.tridentModel.m_103119_(TridentModel.TEXTURE), false, itemStack0.hasFoil());
                this.tridentModel.renderToBuffer(poseStack2, $$29, int4, int5, 1.0F, 1.0F, 1.0F, 1.0F);
                poseStack2.popPose();
            }
        }
    }
}
package com.mna.items.artifice;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.capabilities.IRitualTeleportLocation;
import com.mna.api.items.TieredItem;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.BlockInit;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.gui.containers.providers.NamedEnderDisc;
import com.mna.items.base.IItemWithGui;
import com.mna.items.base.IRadialMenuItem;
import com.mna.items.renderers.obj_gecko.EnderDiscRenderer;
import com.mna.tools.TeleportHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ItemEnderDisk extends TieredItem implements IItemWithGui<ItemEnderDisk>, IRadialMenuItem, GeoItem {

    private static final String KEY_INDEX = "ender_disk_index";

    private static final String KEY_NBT = "ender_disk_data";

    private static final String KEY_COUNT = "count";

    private static final String KEY_NAME = "name";

    private static final String KEY_DIMENSION = "dimension";

    private static final String KEY_ENTRY_PREFIX = "reagent_";

    private static final String CONTROLLER_NAME = "enderDiscController";

    public static final int MAX_PATTERNS = 8;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public ItemEnderDisk() {
        super(new Item.Properties());
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private final NonNullLazy<BlockEntityWithoutLevelRenderer> ister = NonNullLazy.of(() -> new EnderDiscRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.ister.get();
            }
        });
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public MenuProvider getProvider(ItemStack stack) {
        return new NamedEnderDisc();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.m_21120_(handIn);
        IPlayerMagic magic = (IPlayerMagic) playerIn.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (!magic.isMagicUnlocked()) {
            return InteractionResultHolder.fail(stack);
        } else {
            if (!this.openGuiIfModifierPressed(playerIn.m_21120_(handIn), playerIn, worldIn)) {
                this.teleport(worldIn, playerIn, stack);
            }
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        IPlayerMagic magic = (IPlayerMagic) context.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
        if (!magic.isMagicUnlocked()) {
            return InteractionResult.FAIL;
        } else {
            BlockState state = context.getLevel().getBlockState(context.getClickedPos());
            if (state.m_60734_() == BlockInit.RITUAL_TELEPORT_DESTINATION.get() && context.getPlayer().m_6047_()) {
                if (!context.getLevel().isClientSide()) {
                    context.getLevel().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                        List<ResourceLocation> pattern = m.getRitualTeleportBlockReagents(context.getClickedPos());
                        int curIndex = getIndex(context.getItemInHand());
                        Component name = getPatternName(context.getItemInHand(), curIndex);
                        ResourceLocation dimensionID = context.getLevel().dimension().location();
                        setPattern(context.getItemInHand(), pattern, dimensionID, curIndex, name.getString());
                        context.getPlayer().m_213846_(Component.translatable("item.mna.ender_disc.copied"));
                    });
                }
                return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
            } else {
                if (!this.openGuiIfModifierPressed(context.getItemInHand(), context.getPlayer(), context.getLevel())) {
                    this.teleport(context.getLevel(), context.getPlayer(), context.getItemInHand());
                }
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("mna:ender_disc.set_to", getPatternName(stack, getIndex(stack))).withStyle(ChatFormatting.GOLD));
        IItemWithGui.super.appendHoverText(stack, worldIn, tooltip, flagIn);
        IRadialMenuItem.super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    private void teleport(Level world, Player player, ItemStack stack) {
        if (!world.isClientSide) {
            ArrayList<ResourceLocation> pattern = getPattern(stack);
            if (pattern.size() != 0) {
                ResourceLocation dimensionID = getDimension(stack);
                ServerLevel resolvedWorld = TeleportHelper.resolveRegistryKey((ServerLevel) world, dimensionID);
                ServerLevel targetWorld = resolvedWorld != null ? resolvedWorld : (ServerLevel) world;
                targetWorld.getCapability(WorldMagicProvider.MAGIC).ifPresent(worldMagicContainer -> {
                    IRitualTeleportLocation teleportPosition = worldMagicContainer.getRitualTeleportBlockLocation(pattern, world.dimension());
                    if (teleportPosition != null) {
                        if (!teleportPosition.getWorldType().equals(targetWorld.m_46472_())) {
                            player.m_213846_(Component.translatable("mna:rituals/return.wrong_dimension"));
                        } else {
                            ((ServerLevel) world).m_5594_(null, player.m_20183_(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.7F + Math.random() * 0.3F));
                            targetWorld.m_5594_(null, teleportPosition.getPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, (float) (0.7F + Math.random() * 0.3F));
                            TeleportHelper.teleportEntity(player, targetWorld.m_46472_(), Vec3.atBottomCenterOf(teleportPosition.getPos().above()));
                        }
                    }
                });
            }
        } else {
            float particle_spread = 1.0F;
            float v = 1.0F;
            int particleCount = 25;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3(0.0, Math.random() * (double) v, 0.0);
                world.addParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()), player.m_20182_().x() + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, player.m_20182_().y() + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, player.m_20182_().z() + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    private static CompoundTag getCurrentCompound(ItemStack stack, int index) {
        if (stack != null && stack.hasTag() && stack.getTag().contains("ender_disk_data")) {
            CompoundTag tag = stack.getTag();
            if (tag.contains("ender_disk_data", 10)) {
                return tag.getCompound("ender_disk_data");
            } else {
                if (tag.contains("ender_disk_data", 9)) {
                    ListTag list = (ListTag) tag.get("ender_disk_data");
                    if (index < list.size()) {
                        return list.getCompound(index);
                    }
                }
                return null;
            }
        } else {
            return null;
        }
    }

    public static ArrayList<ResourceLocation> getPattern(ItemStack stack) {
        return getPattern(stack, getIndex(stack));
    }

    public static ArrayList<ResourceLocation> getPattern(ItemStack stack, int index) {
        ArrayList<ResourceLocation> output = new ArrayList(8);
        CompoundTag nbt = getCurrentCompound(stack, index);
        if (nbt != null) {
            int count = Math.min(nbt.getInt("count"), 8);
            for (int i = 0; i < count; i++) {
                output.add(new ResourceLocation(nbt.getString("reagent_" + i)));
            }
        }
        while (output.size() < 8) {
            output.add(new ResourceLocation("minecraft:air"));
        }
        return output;
    }

    public static ResourceLocation getDimension(ItemStack stack) {
        return getDimension(stack, getIndex(stack));
    }

    public static ResourceLocation getDimension(ItemStack stack, int index) {
        CompoundTag nbt = getCurrentCompound(stack, index);
        return nbt != null && nbt.contains("dimension") ? new ResourceLocation(nbt.getString("dimension")) : new ResourceLocation("mna:dimension_current");
    }

    public static Component getPatternName(ItemStack stack, int index) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = null;
        if (tag.contains("ender_disk_data", 9)) {
            list = tag.getList("ender_disk_data", 10);
            CompoundTag nbt = null;
            if (index < list.size()) {
                nbt = list.getCompound(index);
                String str = nbt.getString("name");
                return str.length() > 0 ? Component.literal(str) : Component.translatable("mna:ender_disc.unused");
            } else {
                return Component.translatable("mna:ender_disc.unused");
            }
        } else {
            return Component.translatable("mna:ender_disc.unused");
        }
    }

    public static void setPattern(ItemStack stack, List<ResourceLocation> locations, ResourceLocation dimensionID, int index, String name) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = null;
        if (tag.contains("ender_disk_data", 9)) {
            list = tag.getList("ender_disk_data", 10);
        } else {
            list = new ListTag();
        }
        while (list.size() < 8) {
            list.add(new CompoundTag());
        }
        CompoundTag nbt = null;
        if (index < list.size()) {
            nbt = list.getCompound(index);
        } else {
            nbt = new CompoundTag();
        }
        nbt.putInt("count", locations.size());
        nbt.putString("name", name);
        nbt.putString("dimension", dimensionID != null ? dimensionID.toString() : "");
        int count = 0;
        for (ResourceLocation loc : locations) {
            nbt.putString("reagent_" + count++, loc.toString());
        }
        list.set(index, (Tag) nbt);
        stack.getTag().put("ender_disk_data", list);
    }

    public static void setIndex(ItemStack stack, int index) {
        stack.getOrCreateTag().putInt("ender_disk_index", index);
    }

    public static int getIndex(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getInt("ender_disk_index") : 0;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.ender_disc_armature.idle"))));
    }
}
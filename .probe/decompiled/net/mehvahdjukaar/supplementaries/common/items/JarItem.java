package net.mehvahdjukaar.supplementaries.common.items;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.ICustomItemRendererProvider;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.moonlight.api.fluids.FoodProvider;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.util.PotionNBTHelper;
import net.mehvahdjukaar.supplementaries.client.renderers.items.JarItemRenderer;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.BucketHelper;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.QuarkCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class JarItem extends AbstractMobContainerItem implements ICustomItemRendererProvider {

    public JarItem(Block blockIn, Item.Properties properties) {
        super(blockIn, properties, 0.625F, 0.875F, true);
    }

    @Override
    public boolean canItemCatch(Entity e) {
        EntityType<?> type = e.getType();
        return CommonConfigs.Functional.JAR_AUTO_DETECT.get() && this.canFitEntity(e) && !(e instanceof Monster) ? true : type.is(ModTags.JAR_CATCHABLE) || type.is(ModTags.JAR_BABY_CATCHABLE) && e instanceof LivingEntity le && le.isBaby() || this.isBoat(e) || BucketHelper.isModdedFish(e);
    }

    @Override
    public void playReleaseSound(Level world, Vec3 v) {
        world.playSound(null, v.x(), v.y(), v.z(), SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS, 1.0F, 0.05F);
    }

    @Override
    public void playCatchSound(Player player) {
        player.m_9236_().playSound(null, player.m_20183_(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public ItemStack saveEntityInItem(Entity entity, ItemStack currentStack, ItemStack bucket) {
        return this.isBoat(entity) ? new ItemStack((ItemLike) ModRegistry.JAR_BOAT.get()) : super.saveEntityInItem(entity, currentStack, bucket);
    }

    public boolean isBoat(Entity e) {
        return e instanceof Boat;
    }

    @Override
    public InteractionResult doInteract(ItemStack stack, Player player, Entity entity, InteractionHand hand) {
        return !this.captureEnabled() ? InteractionResult.PASS : super.doInteract(stack, player, entity, hand);
    }

    private Boolean captureEnabled() {
        return (Boolean) CommonConfigs.Functional.JAR_CAPTURE.get();
    }

    @Override
    public boolean blocksPlacement() {
        return this.captureEnabled();
    }

    @Override
    public void addPlacementTooltip(List<Component> tooltip) {
        if (this.captureEnabled()) {
            super.addPlacementTooltip(tooltip);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag compoundTag = stack.getTagElement("BlockEntityTag");
        if (compoundTag == null) {
            if (!MiscUtils.showsHints(worldIn, flagIn)) {
                return;
            }
            tooltip.add(Component.translatable("message.supplementaries.jar").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        } else {
            if (compoundTag.contains("LootTable", 8)) {
                tooltip.add(Component.literal("???????").withStyle(ChatFormatting.GRAY));
            }
            if (compoundTag.contains("FluidHolder")) {
                CompoundTag com = compoundTag.getCompound("FluidHolder");
                SoftFluidStack fluid = SoftFluidStack.load(com);
                if (!fluid.isEmpty()) {
                    CompoundTag nbt = null;
                    String add = "";
                    if (fluid.hasTag()) {
                        nbt = fluid.getTag();
                        if (nbt.contains("Bottle")) {
                            String bottle = nbt.getString("Bottle").toLowerCase(Locale.ROOT);
                            if (!bottle.equals("regular")) {
                                add = "_" + bottle;
                            }
                        }
                    }
                    tooltip.add(Component.translatable("message.supplementaries.fluid_tooltip", Component.translatable(fluid.fluid().getTranslationKey() + add), fluid.getCount()).withStyle(ChatFormatting.GRAY));
                    if (nbt != null) {
                        PotionNBTHelper.addPotionTooltip(nbt, tooltip, 1.0F);
                        return;
                    }
                }
            }
            if (compoundTag.contains("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(compoundTag, nonnulllist);
                int i = 0;
                int j = 0;
                for (ItemStack itemstack : nonnulllist) {
                    if (!itemstack.isEmpty()) {
                        j++;
                        if (i <= 4) {
                            i++;
                            MutableComponent iformattabletextcomponent = itemstack.getHoverName().copy();
                            String s = iformattabletextcomponent.getString();
                            s = s.replace(" Bucket", "");
                            s = s.replace(" Bottle", "");
                            s = s.replace("Bucket of ", "");
                            MutableComponent str = Component.literal(s);
                            str.append(" x").append(String.valueOf(itemstack.getCount()));
                            tooltip.add(str.withStyle(ChatFormatting.GRAY));
                        }
                    }
                }
                if (j - i > 0) {
                    tooltip.add(Component.translatable("container.shulkerBox.more", j - i).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        if (tag != null && entity instanceof Player player) {
            JarBlockTile temp = new JarBlockTile(entity.m_20097_(), ((Block) ModRegistry.JAR.get()).defaultBlockState());
            temp.load(tag);
            SoftFluidTank fh = temp.getSoftFluidTank();
            if (fh.containsFood() && fh.tryDrinkUpFluid(player, world)) {
                CompoundTag newTag = new CompoundTag();
                temp.saveAdditional(newTag);
                stack.addTagElement("BlockEntityTag", newTag);
                return stack;
            }
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
        return this.getUseDuration(playerEntity.m_21120_(hand)) != 0 ? ItemUtils.startUsingInstantly(world, playerEntity, hand) : super.m_7203_(world, playerEntity, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if ((Boolean) CommonConfigs.Functional.JAR_ITEM_DRINK.get()) {
            CompoundTag tag = stack.getTagElement("BlockEntityTag");
            if (tag != null) {
                JarBlockTile jarBlockTile = new JarBlockTile(BlockPos.ZERO, ((Block) ModRegistry.JAR.get()).defaultBlockState());
                jarBlockTile.load(tag);
                SoftFluidTank fh = jarBlockTile.getSoftFluidTank();
                FoodProvider provider = fh.getFluid().getFoodProvider();
                Item food = provider.getFood();
                return food.getUseDuration(food.getDefaultInstance()) / provider.getDivider();
            }
        }
        return 0;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return CommonConfigs.Functional.JAR_ITEM_DRINK.get() ? UseAnim.DRINK : UseAnim.NONE;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (CompatHandler.QUARK && this == ModRegistry.JAR_ITEM.get()) {
            InteractionResult r = QuarkCompat.tryCaptureTater(this, context);
            if (r.consumesAction()) {
                return r;
            }
        }
        return super.useOn(context);
    }

    @Override
    public Supplier<ItemStackRenderer> getRendererFactory() {
        return JarItemRenderer::new;
    }
}
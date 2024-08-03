package se.mickelus.tetra.blocks.scroll;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.blocks.scroll.gui.ScrollScreen;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;
import se.mickelus.tetra.items.InitializableItem;

@ParametersAreNonnullByDefault
public class ScrollItem extends BlockItem implements InitializableItem {

    public static final String identifier = "scroll_rolled";

    @ObjectHolder(registryName = "item", value = "tetra:scroll_rolled")
    public static ScrollItem instance;

    public static ItemStack gemExpertise;

    public static ItemStack metalExpertise;

    public static ItemStack woodExpertise;

    public static ItemStack stoneExpertise;

    public static ItemStack fibreExpertise;

    public static ItemStack skinExpertise;

    public static ItemStack boneExpertise;

    public static ItemStack fabricExpertise;

    public static ItemStack scaleExpertise;

    public static ItemStack hammerEfficiency;

    public static ItemStack axeEfficiency;

    public static ItemStack cutEfficiency;

    public static ItemStack sturdyGuard;

    public static ItemStack throwingKnife;

    public static ItemStack howlingBlade;

    public ScrollItem(Block block) {
        super(block, new Item.Properties().stacksTo(1));
        MinecraftForge.EVENT_BUS.register(new ScrollDrops());
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        gemExpertise = this.setupTreatise("gem_expertise", false, 0, 2883566, 14, 13, 14, 15);
        metalExpertise = this.setupTreatise("metal_expertise", false, 0, 16777215, 4, 6, 6, 5);
        woodExpertise = this.setupTreatise("wood_expertise", false, 0, 12555083, 2, 1, 2, 1);
        stoneExpertise = this.setupTreatise("stone_expertise", false, 0, 10132122, 2);
        fibreExpertise = this.setupTreatise("fibre_expertise", false, 0, 11042900, 5, 10, 11, 4);
        skinExpertise = this.setupTreatise("skin_expertise", false, 0, 12544304, 0, 1, 1, 1);
        boneExpertise = this.setupTreatise("bone_expertise", false, 0, 16773523, 12, 14, 12, 14);
        fabricExpertise = this.setupTreatise("fabric_expertise", false, 0, 16724787, 5, 3, 6, 4);
        scaleExpertise = this.setupTreatise("scale_expertise", false, 0, 7708734, 6, 7, 6, 8);
        hammerEfficiency = this.setupTreatise("hammer_efficiency", false, 0, 16737894, 6, 7, 6, 11);
        axeEfficiency = this.setupTreatise("axe_efficiency", false, 0, 6750054, 0, 1, 3, 3);
        cutEfficiency = this.setupTreatise("cut_efficiency", false, 0, 6711039, 4, 0, 3, 5);
        sturdyGuard = this.setupSchematic("sword/sturdy_guard", null, false, 1, 12368053, 3, 2, 2, 1);
        throwingKnife = this.setupSchematic("sword/throwing_knife", null, false, 1, 12111577, 4, 1, 0, 5);
        howlingBlade = this.setupSchematic("sword/howling", null, false, 1, 16446358, 8, 9, 10, 5);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientInit() {
        Minecraft.getInstance().getItemColors().register(new ScrollItemColor(), instance);
        ItemProperties.register(instance, new ResourceLocation("tetra", "scroll_mat"), (itemStack, world, livingEntity, i) -> (float) ScrollData.readMaterialFast(itemStack));
    }

    public Collection<ItemStack> getCreativeTabItems() {
        return Lists.newArrayList(new ItemStack[] { sturdyGuard, throwingKnife, howlingBlade, gemExpertise, metalExpertise, woodExpertise, stoneExpertise, fibreExpertise, skinExpertise, boneExpertise, fabricExpertise, scaleExpertise, hammerEfficiency, axeEfficiency, cutEfficiency, this.setupSchematic("hone/gild_1", null, true, 2, 13217385, 15, 14, 15, 15), this.setupSchematic("hone/gild_5", null, new String[] { "hone/gild_1", "hone/gild_2", "hone/gild_3", "hone/gild_4", "hone/gild_5" }, true, 2, 15905555, 12, 12, 12, 12), this.setupSchematic("warforge/adze", "warforge", false, 2, 8739251, 6, 7, 11, 7), this.setupSchematic("warforge/axe", "warforge", false, 2, 11753843, 5, 10, 8, 9), this.setupSchematic("warforge/hammer", "warforge", false, 2, 4014745, 9, 8, 11, 10), this.setupSchematic("warforge/pickaxe", "warforge", false, 2, 5278899, 6, 11, 8, 7), this.setupSchematic("warforge/claw", "warforge", false, 2, 1910319, 8, 10, 5, 11), this.setupSchematic("warforge/hoe", "warforge", false, 2, 9679696, 10, 7, 9, 5), this.setupSchematic("warforge/sickle", "warforge", false, 2, 14261836, 5, 9, 6, 10), this.setupSchematic("warforge/butt", "warforge", false, 2, 11744822, 11, 5, 8, 9) });
    }

    private ItemStack setupSchematic(String key, String details, boolean isIntricate, int material, int tint, Integer... glyphs) {
        return this.setupSchematic(key, details, new String[] { key }, isIntricate, material, tint, glyphs);
    }

    private ItemStack setupSchematic(String key, String details, String[] schematics, boolean isIntricate, int material, int tint, Integer... glyphs) {
        ScrollData data = new ScrollData(key, Optional.ofNullable(details), isIntricate, material, tint, Arrays.asList(glyphs), (List<ResourceLocation>) Arrays.stream(schematics).map(s -> new ResourceLocation("tetra", s)).collect(Collectors.toList()), Collections.emptyList());
        ItemStack itemStack = new ItemStack(this);
        data.write(itemStack);
        return itemStack;
    }

    private ItemStack setupTreatise(String key, boolean isIntricate, int material, int tint, Integer... glyphs) {
        ScrollData data = new ScrollData(key, Optional.empty(), isIntricate, material, tint, Arrays.asList(glyphs), Collections.emptyList(), ImmutableList.of(new ResourceLocation("tetra", key)));
        ItemStack itemStack = new ItemStack(this);
        data.write(itemStack);
        return itemStack;
    }

    @Override
    public Component getName(ItemStack stack) {
        String key = ScrollData.read(stack).key;
        if (!Environment.get().getDist().isDedicatedServer()) {
            String prefixKey = "item.tetra.scroll." + key + ".prefix";
            if (I18n.exists(prefixKey)) {
                return Component.translatable("item.tetra.scroll." + key + ".prefix").append(Component.literal(": ")).append(Component.translatable("item.tetra.scroll." + key + ".name"));
            }
        }
        return Component.translatable("item.tetra.scroll." + key + ".name");
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ScrollData data = ScrollData.read(itemStack);
        StringJoiner attributes = new StringJoiner(" ");
        if (!ScrollData.read(itemStack).schematics.isEmpty()) {
            attributes.add(ChatFormatting.DARK_PURPLE + I18n.get("item.tetra.scroll.schematics"));
        }
        if (!ScrollData.read(itemStack).craftingEffects.isEmpty()) {
            attributes.add(ChatFormatting.DARK_AQUA + I18n.get("item.tetra.scroll.effects"));
        }
        if (data.isIntricate) {
            attributes.add(ChatFormatting.GOLD + I18n.get("item.tetra.scroll.intricate"));
        }
        tooltip.add(Component.literal(attributes.toString()));
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.translatable("item.tetra.scroll." + data.key + ".description").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" "));
        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            if (!ScrollData.read(itemStack).schematics.isEmpty()) {
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("item.tetra.scroll.schematics").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.DARK_PURPLE));
                tooltip.add(Component.translatable("item.tetra.scroll.schematics.description").withStyle(ChatFormatting.GRAY));
            }
            if (!ScrollData.read(itemStack).craftingEffects.isEmpty()) {
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("item.tetra.scroll.effects").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.DARK_AQUA));
                tooltip.add(Component.translatable("item.tetra.scroll.effects.description").withStyle(ChatFormatting.GRAY));
            }
            if (data.isIntricate) {
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("item.tetra.scroll.intricate").withStyle(ChatFormatting.UNDERLINE, ChatFormatting.GOLD));
                tooltip.add(Component.translatable("item.tetra.scroll.intricate.description").withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("item.tetra.scroll.range.description").withStyle(ChatFormatting.GRAY));
            }
            if (I18n.exists("item.tetra.scroll." + data.key + ".description_extended")) {
                tooltip.add(Component.literal(" "));
                tooltip.add(Component.translatable("item.tetra.scroll." + data.key + ".description_extended").withStyle(ChatFormatting.GRAY));
            }
        } else {
            tooltip.add(Tooltips.expand);
        }
        if (flagIn.isAdvanced()) {
            tooltip.add(Component.literal("s: " + data.schematics + ",e: " + data.craftingEffects));
        }
    }

    private boolean openScroll(ItemStack itemStack, boolean isRemote) {
        ScrollData data = ScrollData.read(itemStack);
        if (data.details != null) {
            if (isRemote) {
                this.showDetailsScreen(data.details);
            }
            return true;
        } else {
            return false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void showDetailsScreen(String detailsKey) {
        ScrollScreen screen = new ScrollScreen(detailsKey);
        Minecraft.getInstance().setScreen(screen);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        return this.openScroll(player.m_21120_(hand), world.isClientSide) ? InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide()) : InteractionResultHolder.pass(itemstack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack itemStack = context.getItemInHand();
        Player player = context.getPlayer();
        Block block = context.getLevel().getBlockState(context.getClickedPos()).m_60734_();
        if (!(block instanceof AbstractWorkbenchBlock) && player != null && player.m_6047_() && this.openScroll(itemStack, world.isClientSide)) {
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            if (RolledScrollBlock.instance.equals(block)) {
                boolean success = (Boolean) TileEntityOptional.from(world, pos, ScrollTile.class).map(tile -> tile.addScroll(itemStack)).orElse(false);
                if (success) {
                    if (player == null || !player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                    return InteractionResult.sidedSuccess(world.isClientSide);
                }
            }
            return this.m_40576_(new BlockPlaceContext(context));
        }
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = this.m_40614_().defaultBlockState();
        if (context.m_43719_().getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
            state = (BlockState) WallScrollBlock.instance.m_49966_().m_61124_(BlockStateProperties.HORIZONTAL_FACING, context.m_43719_());
        } else {
            if (context.m_43725_().getBlockState(context.getClickedPos().relative(context.m_43719_().getOpposite())).m_60734_() instanceof AbstractWorkbenchBlock) {
                state = OpenScrollBlock.instance.m_49966_();
            }
            state = (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_FACING, context.m_8125_());
        }
        return this.m_40610_(context, state) ? state : null;
    }
}
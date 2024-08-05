package se.mickelus.tetra.items.modular.impl.holo;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.blocks.holo.HolosphereBlock;
import se.mickelus.tetra.blocks.holo.HolosphereBlockEntity;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.HoloGui;
import se.mickelus.tetra.items.modular.impl.holo.gui.scan.ScannerOverlayGui;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;
import se.mickelus.tetra.properties.TetraAttributes;

@ParametersAreNonnullByDefault
public class ModularHolosphereItem extends ModularItem {

    public static final String coreKey = "holo/core";

    public static final String frameKey = "holo/frame";

    public static final String scannerKey = "holo/scanner";

    public static final String repositoryKey = "holo/repo";

    public static final String identifier = "holo";

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(-14, 0, -14, 18, 4, 0, 4, 18);

    @ObjectHolder(registryName = "item", value = "tetra:holo")
    public static ModularHolosphereItem instance;

    public ModularHolosphereItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.canHone = false;
        this.majorModuleKeys = new String[] { "holo/core", "holo/frame", "holo/scanner", "holo/repo" };
        this.minorModuleKeys = new String[0];
        this.requiredModules = new String[] { "holo/core", "holo/frame", "holo/scanner", "holo/repo" };
    }

    public static ItemStack findHolosphere(Player player, Level level, BlockPos pos) {
        ItemStack itemStack = findHolosphere(player);
        return itemStack.isEmpty() ? findHolosphere(level, pos) : itemStack;
    }

    public static ItemStack findHolosphere(Level level, BlockPos pos) {
        return (ItemStack) BlockPos.betweenClosedStream(pos.offset(-2, 0, -2), pos.offset(2, 4, 2)).map(offsetPos -> new Pair(offsetPos, level.getBlockState(offsetPos))).filter(pair -> ((BlockState) pair.getSecond()).m_60734_() instanceof HolosphereBlock).findFirst().flatMap(pair -> level.m_141902_((BlockPos) pair.getFirst(), HolosphereBlockEntity.type.get())).map(HolosphereBlockEntity::getItemStack).orElse(ItemStack.EMPTY);
    }

    public static ItemStack findHolosphere(Player player) {
        return (ItemStack) Stream.of(player.getInventory().offhand.stream(), player.getInventory().items.stream(), ToolbeltHelper.getToolbeltItems(player).stream()).flatMap(Function.identity()).filter(stack -> stack.getItem() instanceof ModularHolosphereItem).findFirst().orElse(ItemStack.EMPTY);
    }

    @OnlyIn(Dist.CLIENT)
    public static void showGui() {
        HoloGui gui = HoloGui.getInstance();
        Minecraft.getInstance().setScreen(gui);
        gui.onShow();
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("holo/"));
    }

    public static ItemStack getCreativeItemStack() {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "holo/core", "holo/core", "frame/dim");
        IModularItem.putModuleInSlot(itemStack, "holo/frame", "holo/frame", "core/ancient");
        IModularItem.putModuleInSlot(itemStack, "holo/scanner", "holo/scanner", "scanner/default");
        IModularItem.putModuleInSlot(itemStack, "holo/repo", "holo/repo", "repo/default");
        return itemStack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.tetra.holo.tooltip1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" "));
        if (ScannerOverlayGui.instance != null && ScannerOverlayGui.instance.isAvailable()) {
            tooltip.add(Component.translatable("tetra.holo.scan.status", ScannerOverlayGui.instance.getStatus()).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("tetra.holo.place"));
        }
        tooltip.add(Component.translatable("item.tetra.holo.tooltip2"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide) {
            showGui();
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.m_21120_(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return context.getPlayer() != null && context.getPlayer().m_6047_() ? HolosphereBlock.place(new BlockPlaceContext(context)) : super.m_6225_(context);
    }

    @Override
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    public double getCooldownBase(ItemStack itemStack) {
        return Math.max(0.0, this.getAttributeValue(itemStack, TetraAttributes.abilityCooldown.get()));
    }
}
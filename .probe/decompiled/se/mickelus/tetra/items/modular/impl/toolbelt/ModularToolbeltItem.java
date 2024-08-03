package se.mickelus.tetra.items.modular.impl.toolbelt;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.booster.JumpHandlerBooster;
import se.mickelus.tetra.items.modular.impl.toolbelt.booster.TickHandlerBooster;
import se.mickelus.tetra.items.modular.impl.toolbelt.booster.UpdateBoosterPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.gui.screen.ToolbeltScreen;
import se.mickelus.tetra.items.modular.impl.toolbelt.suspend.JumpHandlerSuspend;
import se.mickelus.tetra.items.modular.impl.toolbelt.suspend.ToggleSuspendPacket;

@ParametersAreNonnullByDefault
public class ModularToolbeltItem extends ModularItem implements MenuProvider {

    public static final String identifier = "modular_toolbelt";

    public static final String slot1Key = "toolbelt/slot1";

    public static final String slot2Key = "toolbelt/slot2";

    public static final String slot3Key = "toolbelt/slot3";

    public static final String beltKey = "toolbelt/belt";

    public static final String slot1Suffix = "_slot1";

    public static final String slot2Suffix = "_slot2";

    public static final String slot3Suffix = "_slot3";

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(-14, 18, 4, 0, 4, 18);

    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-13, 0);

    public static RegistryObject<ModularToolbeltItem> instance;

    public ModularToolbeltItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.canHone = false;
        this.majorModuleKeys = new String[] { "toolbelt/slot1", "toolbelt/slot2", "toolbelt/slot3" };
        this.minorModuleKeys = new String[] { "toolbelt/belt" };
        this.requiredModules = new String[] { "toolbelt/belt" };
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        packetHandler.registerPacket(EquipToolbeltItemPacket.class, EquipToolbeltItemPacket::new);
        packetHandler.registerPacket(StoreToolbeltItemPacket.class, StoreToolbeltItemPacket::new);
        packetHandler.registerPacket(OpenToolbeltItemPacket.class, OpenToolbeltItemPacket::new);
        packetHandler.registerPacket(UpdateBoosterPacket.class, UpdateBoosterPacket::new);
        packetHandler.registerPacket(ToggleSuspendPacket.class, ToggleSuspendPacket::new);
        MinecraftForge.EVENT_BUS.register(new TickHandlerBooster());
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("toolbelt/"));
    }

    @Override
    public void clientInit() {
        super.clientInit();
        MinecraftForge.EVENT_BUS.register(new JumpHandlerBooster(Minecraft.getInstance()));
        MinecraftForge.EVENT_BUS.register(new JumpHandlerSuspend(Minecraft.getInstance()));
        MenuScreens.register(ToolbeltContainer.type.get(), ToolbeltScreen::new);
    }

    public static Collection<ItemStack> getCreativeTabItemStacks() {
        return Lists.newArrayList(new ItemStack[] { createStack("belt/rope"), createStack("belt/inlaid") });
    }

    private static ItemStack createStack(String beltMaterial) {
        ItemStack itemStack = new ItemStack(instance.get());
        IModularItem.putModuleInSlot(itemStack, "toolbelt/belt", "toolbelt/belt", "toolbelt/belt_material", beltMaterial);
        IModularItem.putModuleInSlot(itemStack, "toolbelt/slot1", "toolbelt/strap_slot1", "toolbelt/strap_slot1_material", "strap1/leather");
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, this);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.m_21120_(hand));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(this.toString());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
        ItemStack itemStack = player.m_21205_();
        if (!this.equals(itemStack.getItem())) {
            itemStack = player.m_21206_();
        }
        if (!this.equals(itemStack.getItem())) {
            itemStack = ToolbeltHelper.findToolbelt(player);
        }
        return new ToolbeltContainer(windowId, inventory, itemStack, player);
    }

    public int getNumSlots(ItemStack itemStack, SlotType slotType) {
        return (Integer) this.getAllModules(itemStack).stream().map(module -> module.getEffectLevel(itemStack, slotType.effect)).reduce(0, Integer::sum);
    }

    public List<Collection<ItemEffect>> getSlotEffects(ItemStack itemStack, SlotType slotType) {
        return (List<Collection<ItemEffect>>) this.getAllModules(itemStack).stream().map(module -> module.getEffectData(itemStack)).filter(Objects::nonNull).filter(effects -> effects.contains(slotType.effect)).map(effects -> {
            Map<ItemEffect, Integer> effectLevels = effects.getLevelMap();
            int slotCount = (Integer) effectLevels.get(slotType.effect);
            Collection<Collection<ItemEffect>> result = new ArrayList(slotCount);
            for (int i = 0; i < slotCount; i++) {
                int index = i;
                result.add((Collection) effectLevels.entrySet().stream().filter(entry -> !((ItemEffect) entry.getKey()).equals(slotType.effect)).filter(entry -> (Integer) entry.getValue() > index).map(Entry::getKey).collect(Collectors.toList()));
            }
            return result;
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }
}
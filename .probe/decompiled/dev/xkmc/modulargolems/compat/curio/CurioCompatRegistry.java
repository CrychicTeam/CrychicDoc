package dev.xkmc.modulargolems.compat.curio;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2tabs.compat.CuriosEventHandler;
import dev.xkmc.l2tabs.init.data.L2TabsLangData;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import dev.xkmc.modulargolems.content.menu.registry.EquipmentGroup;
import dev.xkmc.modulargolems.content.menu.registry.GolemTabRegistry;
import dev.xkmc.modulargolems.content.menu.registry.IMenuPvd;
import dev.xkmc.modulargolems.content.menu.tabs.GolemTabToken;
import dev.xkmc.modulargolems.content.menu.tabs.ITabScreen;
import dev.xkmc.modulargolems.init.ModularGolems;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CurioCompatRegistry {

    public static CurioCompatRegistry INSTANCE;

    public MenuEntry<GolemCuriosListMenu> menuType;

    public GolemTabToken<EquipmentGroup, GolemCurioTab> tab;

    @Nullable
    public static CurioCompatRegistry get() {
        if (ModList.get().isLoaded("curios")) {
            if (INSTANCE == null) {
                INSTANCE = new CurioCompatRegistry();
            }
            return INSTANCE;
        } else {
            return null;
        }
    }

    public static Optional<ItemStack> getItem(LivingEntity e, String slot) {
        return CuriosApi.getCuriosInventory(e).resolve().flatMap(x -> x.findCurio(slot, 0).map(SlotResult::stack));
    }

    public static void register() {
        CurioCompatRegistry ins = get();
        if (ins != null) {
            ins.registerImpl();
        }
    }

    public static void clientRegister() {
        CurioCompatRegistry ins = get();
        if (ins != null) {
            ins.clientRegisterImpl();
        }
    }

    public static <T> void onJEIRegistry(Consumer<Class<? extends ITabScreen>> consumer) {
        CurioCompatRegistry ins = get();
        if (ins != null) {
            ins.onJEIRegistryImpl(consumer);
        }
    }

    public static IMenuPvd create(AbstractGolemEntity<?, ?> entity) {
        return new GolemCuriosMenuPvd(entity, 0);
    }

    public static void tryOpen(ServerPlayer player, LivingEntity target) {
        if (get() != null) {
            Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(target).resolve();
            if (!opt.isEmpty()) {
                if (((ICuriosItemHandler) opt.get()).getSlots() != 0) {
                    GolemCuriosMenuPvd pvd = new GolemCuriosMenuPvd(target, 0);
                    CuriosEventHandler.openMenuWrapped(player, () -> NetworkHooks.openScreen(player, pvd, pvd::writeBuffer));
                }
            }
        }
    }

    public void registerImpl() {
        this.menuType = ModularGolems.REGISTRATE.menu("golem_curios", GolemCuriosListMenu::fromNetwork, () -> GolemCuriosListScreen::new).register();
    }

    public void clientRegisterImpl() {
        this.tab = new GolemTabToken<>(GolemCurioTab::new, () -> Items.AIR, L2TabsLangData.CURIOS.get(new Object[0]));
        GolemTabRegistry.LIST_EQUIPMENT.add(this.tab);
    }

    private void onJEIRegistryImpl(Consumer<Class<? extends ITabScreen>> consumer) {
        consumer.accept(GolemCuriosListScreen.class);
    }

    public ItemStack getSkin(HumanoidGolemEntity le) {
        return (ItemStack) CuriosApi.getCuriosInventory(le).resolve().flatMap(e -> e.getStacksHandler("golem_skin")).map(ICurioStacksHandler::getStacks).map(e -> e.getSlots() == 0 ? null : e.getStackInSlot(0)).orElse(ItemStack.EMPTY);
    }
}
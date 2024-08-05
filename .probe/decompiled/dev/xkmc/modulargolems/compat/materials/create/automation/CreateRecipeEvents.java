package dev.xkmc.modulargolems.compat.materials.create.automation;

import com.simibubi.create.content.kinetics.deployer.DeployerRecipeSearchEvent;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.equipments.GolemEquipmentItem;
import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.events.CraftEventListeners;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CreateRecipeEvents {

    @SubscribeEvent
    public static void addRecipe(DeployerRecipeSearchEvent event) {
        ItemStack first = event.getInventory().getItem(0);
        ItemStack second = event.getInventory().getItem(1);
        if (first.getItem() instanceof GolemHolder<?, ?> holder) {
            if (event.getBlockEntity().m_58904_() instanceof ServerLevel sl) {
                ItemStack result;
                if (second.getItem() instanceof UpgradeItem upgrade) {
                    result = CraftEventListeners.appendUpgrade(first, holder, upgrade);
                } else if (isGolemCurio(holder, second)) {
                    result = equipCurio(holder, first, second, sl);
                } else if (holder.getEntityType() == GolemTypes.TYPE_GOLEM.get()) {
                    if (!(second.getItem() instanceof GolemEquipmentItem equipment)) {
                        return;
                    }
                    if (!equipment.isFor((EntityType<?>) GolemTypes.ENTITY_GOLEM.get())) {
                        return;
                    }
                    EquipmentSlot slot = equipment.getSlot();
                    result = equip(holder, first, second, slot, sl);
                } else {
                    if (holder.getEntityType() != GolemTypes.TYPE_HUMANOID.get()) {
                        return;
                    }
                    EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(second);
                    result = equip(holder, first, second, slot, sl);
                }
                if (!result.isEmpty()) {
                    event.addRecipe(() -> Optional.of(new DeployerUpgradeRecipe(result)), 1000);
                }
            }
        }
    }

    private static boolean isGolemCurio(GolemHolder<?, ?> holder, ItemStack stack) {
        if (!ModList.get().isLoaded("curios")) {
            return false;
        } else {
            Set<String> set = CuriosApi.getEntitySlots(holder.getEntityType().type()).keySet();
            return (Boolean) ForgeRegistries.ITEMS.tags().getReverseTag(stack.getItem()).map(e -> e.getTagKeys().anyMatch(t -> t.location().getNamespace().equals("curios") && set.contains(t.location().getPath()))).orElse(false);
        }
    }

    private static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> ItemStack equipCurio(GolemHolder<T, P> holder, ItemStack golem, ItemStack equip, Level level) {
        if (!ModList.get().isLoaded("curios")) {
            return ItemStack.EMPTY;
        } else {
            T entity = holder.createDummy(golem, level);
            if (entity == null) {
                return ItemStack.EMPTY;
            } else {
                Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(entity).resolve();
                if (opt.isEmpty()) {
                    return ItemStack.EMPTY;
                } else {
                    equip = equip.copy();
                    equip.setCount(1);
                    for (String slot : CuriosApi.getItemStackSlots(equip, entity).keySet()) {
                        Optional<ICurioStacksHandler> handler = ((ICuriosItemHandler) opt.get()).getStacksHandler(slot);
                        if (!handler.isEmpty()) {
                            IDynamicStackHandler stacks = ((ICurioStacksHandler) handler.get()).getStacks();
                            for (int i = 0; i < stacks.getSlots(); i++) {
                                if (stacks.getStackInSlot(i).isEmpty()) {
                                    stacks.setStackInSlot(i, equip);
                                    return GolemHolder.setEntity(entity);
                                }
                            }
                        }
                    }
                    return ItemStack.EMPTY;
                }
            }
        }
    }

    private static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> ItemStack equip(GolemHolder<T, P> holder, ItemStack golem, ItemStack equip, EquipmentSlot slot, Level level) {
        T entity = holder.createDummy(golem, level);
        if (entity == null) {
            return ItemStack.EMPTY;
        } else if (!entity.m_6844_(slot).isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            equip = equip.copy();
            equip.setCount(1);
            entity.m_8061_(slot, equip);
            return GolemHolder.setEntity(entity);
        }
    }
}
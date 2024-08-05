package software.bernie.example.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.example.client.renderer.armor.WolfArmorRenderer;
import software.bernie.example.registry.ItemRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class WolfArmorItem extends ArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WolfArmorItem(ArmorMaterial armorMaterial, ArmorItem.Type type, Item.Properties properties) {
        super(armorMaterial, type, properties);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            private GeoArmorRenderer<?> renderer;

            @NotNull
            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new WolfArmorRenderer();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 20, state -> {
            state.setAnimation(DefaultAnimations.IDLE);
            Entity entity = state.getData(DataTickets.ENTITY);
            if (entity instanceof ArmorStand) {
                return PlayState.CONTINUE;
            } else {
                Set<Item> wornArmor = new ObjectOpenHashSet();
                for (ItemStack stack : entity.getArmorSlots()) {
                    if (stack.isEmpty()) {
                        return PlayState.STOP;
                    }
                    wornArmor.add(stack.getItem());
                }
                boolean isFullSet = wornArmor.containsAll(ObjectArrayList.of(new WolfArmorItem[] { ItemRegistry.WOLF_ARMOR_BOOTS.get(), ItemRegistry.WOLF_ARMOR_LEGGINGS.get(), ItemRegistry.WOLF_ARMOR_CHESTPLATE.get(), ItemRegistry.WOLF_ARMOR_HELMET.get() }));
                return isFullSet ? PlayState.CONTINUE : PlayState.STOP;
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
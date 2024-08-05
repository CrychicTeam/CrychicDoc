package dev.xkmc.l2complements.content.item.base;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.l2complements.init.data.LangData;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerBook extends Item implements MenuProvider {

    private final Supplier<MenuEntry<?>> cont;

    public ContainerBook(Item.Properties props, Supplier<MenuEntry<?>> cont) {
        super(props);
        this.cont = cont;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!world.isClientSide()) {
            player.openMenu(this);
        } else {
            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public Component getDisplayName() {
        return LangData.translate(getLangKey((MenuType<?>) ((MenuEntry) this.cont.get()).get()));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int wid, Inventory plInv, Player pl) {
        return ((MenuEntry) this.cont.get()).create(wid, plInv);
    }

    public static String getLangKey(MenuType<?> menu) {
        ResourceLocation rl = (ResourceLocation) Objects.requireNonNull(ForgeRegistries.MENU_TYPES.getKey(menu));
        return "container." + rl.getNamespace() + "." + rl.getPath();
    }

    public interface IFac {

        AbstractContainerMenu create(int var1, Inventory var2, Player var3);
    }
}
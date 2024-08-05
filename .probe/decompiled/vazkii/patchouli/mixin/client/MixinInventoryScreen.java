package vazkii.patchouli.mixin.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.client.gui.GuiButtonInventoryBook;
import vazkii.patchouli.common.base.PatchouliConfig;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

@Mixin({ InventoryScreen.class })
public abstract class MixinInventoryScreen extends EffectRenderingInventoryScreen<InventoryMenu> {

    public MixinInventoryScreen(InventoryMenu container, Inventory playerInventory, Component text) {
        super(container, playerInventory, text);
    }

    @Inject(at = { @At("RETURN") }, method = { "init()V" })
    public void onGuiInitPost(CallbackInfo info) {
        ResourceLocation bookID = new ResourceLocation(PatchouliConfig.get().inventoryButtonBook());
        Book book = (Book) BookRegistry.INSTANCE.books.get(bookID);
        if (book != null) {
            Renderable replaced = null;
            Button replacement = null;
            for (int i = 0; i < ((AccessorScreen) this).getRenderables().size(); i++) {
                Renderable button = (Renderable) ((AccessorScreen) this).getRenderables().get(i);
                if (button instanceof ImageButton tex) {
                    replaced = button;
                    replacement = new GuiButtonInventoryBook(book, tex.m_252754_(), tex.m_252907_() - 1);
                    ((AccessorScreen) this).getRenderables().set(i, replacement);
                    break;
                }
            }
            int ix = this.m_6702_().indexOf(replaced);
            if (ix >= 0) {
                this.m_6702_().set(ix, replacement);
            }
            ix = ((AccessorScreen) this).getNarratables().indexOf(replaced);
            if (ix >= 0) {
                ((AccessorScreen) this).getNarratables().set(ix, replacement);
            }
        }
    }
}
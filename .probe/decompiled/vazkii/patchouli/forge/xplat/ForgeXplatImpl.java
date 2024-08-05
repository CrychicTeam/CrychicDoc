package vazkii.patchouli.forge.xplat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.BookContentsReloadEvent;
import vazkii.patchouli.api.BookDrawScreenEvent;
import vazkii.patchouli.forge.client.ForgeClientInitializer;
import vazkii.patchouli.forge.network.ForgeMessageOpenBookGui;
import vazkii.patchouli.forge.network.ForgeMessageReloadBookContents;
import vazkii.patchouli.xplat.IXplatAbstractions;
import vazkii.patchouli.xplat.XplatModContainer;

public class ForgeXplatImpl implements IXplatAbstractions {

    @Override
    public void fireDrawBookScreen(ResourceLocation book, Screen gui, int mouseX, int mouseY, float partialTicks, GuiGraphics graphics) {
        MinecraftForge.EVENT_BUS.post(new BookDrawScreenEvent(book, gui, mouseX, mouseY, partialTicks, graphics));
    }

    @Override
    public void fireBookReload(ResourceLocation book) {
        MinecraftForge.EVENT_BUS.post(new BookContentsReloadEvent(book));
    }

    @Override
    public void sendReloadContentsMessage(MinecraftServer server) {
        ForgeMessageReloadBookContents.sendToAll(server);
    }

    @Override
    public void sendOpenBookGui(ServerPlayer player, ResourceLocation book, @Nullable ResourceLocation entry, int page) {
        ForgeMessageOpenBookGui.send(player, book, entry, page);
    }

    @Override
    public Collection<XplatModContainer> getAllMods() {
        List<XplatModContainer> ret = new ArrayList();
        for (IModInfo info : ModList.get().getMods()) {
            ret.add(new ForgeXplatModContainer((ModContainer) ModList.get().getModContainerById(info.getModId()).get()));
        }
        return ret;
    }

    @Override
    public XplatModContainer getModContainer(String modId) {
        return new ForgeXplatModContainer((ModContainer) ModList.get().getModContainerById(modId).get());
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public void signalBooksLoaded() {
        if (this.isPhysicalClient()) {
            ForgeClientInitializer.signalBooksLoaded();
        }
    }

    @Override
    public boolean handleRecipeKeybind(int keyCode, int scanCode, @Nullable ItemStack stack) {
        return false;
    }
}
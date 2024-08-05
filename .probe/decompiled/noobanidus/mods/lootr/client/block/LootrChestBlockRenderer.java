package noobanidus.mods.lootr.client.block;

import java.util.UUID;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.ChestType;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.util.Getter;

public class LootrChestBlockRenderer<T extends LootrChestBlockEntity & ILootBlockEntity> extends ChestRenderer<T> {

    public static final Material MATERIAL = new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "chest"));

    public static final Material MATERIAL2 = new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "chest_opened"));

    public static final Material MATERIAL3 = new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "chest_trapped"));

    public static final Material MATERIAL4 = new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "chest_trapped_opened"));

    public static final Material OLD_MATERIAL = new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "old_chest"));

    public static final Material OLD_MATERIAL2 = new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "old_chest_opened"));

    private UUID playerId = null;

    public LootrChestBlockRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        super(blockEntityRendererProviderContext0);
    }

    protected Material getMaterial(T tile, ChestType type) {
        if (ConfigManager.isVanillaTextures()) {
            return Sheets.chooseMaterial(tile, type, false);
        } else {
            boolean trapped = tile.m_58903_().equals(ModBlockEntities.LOOTR_TRAPPED_CHEST.get());
            if (this.playerId == null) {
                Player player = Getter.getPlayer();
                if (player == null) {
                    if (ConfigManager.isOldTextures()) {
                        return OLD_MATERIAL;
                    }
                    return trapped ? MATERIAL3 : MATERIAL;
                }
                this.playerId = player.m_20148_();
            }
            if (tile.isOpened()) {
                if (ConfigManager.isOldTextures()) {
                    return OLD_MATERIAL2;
                } else {
                    return trapped ? MATERIAL4 : MATERIAL2;
                }
            } else if (tile.getOpeners().contains(this.playerId)) {
                if (ConfigManager.isOldTextures()) {
                    return OLD_MATERIAL2;
                } else {
                    return trapped ? MATERIAL4 : MATERIAL2;
                }
            } else if (ConfigManager.isOldTextures()) {
                return OLD_MATERIAL;
            } else {
                return trapped ? MATERIAL3 : MATERIAL;
            }
        }
    }
}
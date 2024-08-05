package com.simibubi.create.content.schematics;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.schematics.client.SchematicEditScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.GZIPInputStream;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.slf4j.Logger;

public class SchematicItem extends Item {

    private static final Logger LOGGER = LogUtils.getLogger();

    public SchematicItem(Item.Properties properties) {
        super(properties);
    }

    public static ItemStack create(HolderGetter<Block> lookup, String schematic, String owner) {
        ItemStack blueprint = AllItems.SCHEMATIC.asStack();
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("Deployed", false);
        tag.putString("Owner", owner);
        tag.putString("File", schematic);
        tag.put("Anchor", NbtUtils.writeBlockPos(BlockPos.ZERO));
        tag.putString("Rotation", Rotation.NONE.name());
        tag.putString("Mirror", Mirror.NONE.name());
        blueprint.setTag(tag);
        writeSize(lookup, blueprint);
        return blueprint;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag()) {
            if (stack.getTag().contains("File")) {
                tooltip.add(Components.literal(ChatFormatting.GOLD + stack.getTag().getString("File")));
            }
        } else {
            tooltip.add(Lang.translateDirect("schematic.invalid").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static void writeSize(HolderGetter<Block> lookup, ItemStack blueprint) {
        CompoundTag tag = blueprint.getTag();
        StructureTemplate t = loadSchematic(lookup, blueprint);
        tag.put("Bounds", NBTHelper.writeVec3i(t.getSize()));
        blueprint.setTag(tag);
        SchematicInstances.clearHash(blueprint);
    }

    public static StructurePlaceSettings getSettings(ItemStack blueprint) {
        return getSettings(blueprint, true);
    }

    public static StructurePlaceSettings getSettings(ItemStack blueprint, boolean processNBT) {
        CompoundTag tag = blueprint.getTag();
        StructurePlaceSettings settings = new StructurePlaceSettings();
        settings.setRotation(Rotation.valueOf(tag.getString("Rotation")));
        settings.setMirror(Mirror.valueOf(tag.getString("Mirror")));
        if (processNBT) {
            settings.addProcessor(SchematicProcessor.INSTANCE);
        }
        return settings;
    }

    public static StructureTemplate loadSchematic(HolderGetter<Block> lookup, ItemStack blueprint) {
        StructureTemplate t = new StructureTemplate();
        String owner = blueprint.getTag().getString("Owner");
        String schematic = blueprint.getTag().getString("File");
        if (!schematic.endsWith(".nbt")) {
            return t;
        } else {
            Path dir;
            Path file;
            if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
                dir = Paths.get("schematics", "uploaded").toAbsolutePath();
                file = Paths.get(owner, schematic);
            } else {
                dir = Paths.get("schematics").toAbsolutePath();
                file = Paths.get(schematic);
            }
            Path path = dir.resolve(file).normalize();
            if (!path.startsWith(dir)) {
                return t;
            } else {
                try {
                    DataInputStream stream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(Files.newInputStream(path, StandardOpenOption.READ))));
                    try {
                        CompoundTag nbt = NbtIo.read(stream, new NbtAccounter(536870912L));
                        t.load(lookup, nbt);
                    } catch (Throwable var12) {
                        try {
                            stream.close();
                        } catch (Throwable var11) {
                            var12.addSuppressed(var11);
                        }
                        throw var12;
                    }
                    stream.close();
                } catch (IOException var13) {
                    LOGGER.warn("Failed to read schematic", var13);
                }
                return t;
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        return context.getPlayer() != null && !this.onItemUse(context.getPlayer(), context.getHand()) ? super.useOn(context) : InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        return !this.onItemUse(playerIn, handIn) ? super.use(worldIn, playerIn, handIn) : new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.m_21120_(handIn));
    }

    private boolean onItemUse(Player player, InteractionHand hand) {
        if (!player.m_6144_() || hand != InteractionHand.MAIN_HAND) {
            return false;
        } else if (!player.m_21120_(hand).hasTag()) {
            return false;
        } else {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::displayBlueprintScreen);
            return true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void displayBlueprintScreen() {
        ScreenOpener.open(new SchematicEditScreen());
    }
}
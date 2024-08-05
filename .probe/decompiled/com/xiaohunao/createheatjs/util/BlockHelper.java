package com.xiaohunao.createheatjs.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.xiaohunao.createheatjs.HeatData;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Optional;
import java.util.Map.Entry;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockHelper {

    public static BlockState parseBlockState(String string) {
        if (string.isEmpty()) {
            return Blocks.AIR.defaultBlockState();
        } else {
            int i = string.indexOf(91);
            boolean hasProperties = i >= 0 && string.indexOf(93) == string.length() - 1;
            String blockName = hasProperties ? string.substring(0, i) : string;
            Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(blockName));
            if (block == null) {
                return Blocks.AIR.defaultBlockState();
            } else {
                BlockState state = block.defaultBlockState();
                if (hasProperties) {
                    String properties = string.substring(i + 1, string.length() - 1);
                    for (String s : properties.split(",")) {
                        String[] s1 = s.split("=", 2);
                        if (s1.length == 2 && !s1[0].isEmpty() && !s1[1].isEmpty()) {
                            Optional<?> o = state.m_60734_().getStateDefinition().getProperty(s1[0]).getValue(s1[1]);
                            if (o.isPresent()) {
                                state = (BlockState) state.m_61124_(state.m_60734_().getStateDefinition().getProperty(s1[0]), UtilsJS.cast(o.get()));
                            }
                        }
                    }
                }
                return state;
            }
        }
    }

    public static boolean hetaSourceRender(GuiGraphics graphics, IDrawable drawable, HeatCondition requiredHeat) {
        HeatData heatData = HeatData.getHeatData(requiredHeat.toString());
        Entry<Pair<String, String>, TriPredicate<Level, BlockPos, BlockState>>[] heatSource = (Entry<Pair<String, String>, TriPredicate<Level, BlockPos, BlockState>>[]) heatData.getHeatSource().entrySet().toArray(new Entry[0]);
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return false;
        } else {
            long dayTime = level.m_46468_();
            PoseStack pose = graphics.pose();
            pose.pushPose();
            pose.translate((float) (drawable.getWidth() / 2 + 3), 55.0F, 200.0F);
            pose.mulPose(Axis.XP.rotationDegrees(-15.5F));
            pose.mulPose(Axis.YP.rotationDegrees(22.5F));
            int scale = 23;
            int itemIndexToShow = (int) (dayTime / 25L % (long) heatSource.length);
            Entry<Pair<String, String>, TriPredicate<Level, BlockPos, BlockState>> pair = heatSource[itemIndexToShow];
            if (pair == null) {
                return false;
            } else {
                BlockState heatBlockState = parseBlockState((String) ((Pair) pair.getKey()).getSecond());
                if (heatBlockState != null && heatBlockState.m_60734_() != AllBlocks.BLAZE_BURNER.get()) {
                    AnimatedKinetics.defaultBlockElement(heatBlockState).atLocal(1.0, 1.65, 1.0).rotate(0.0, 180.0, 0.0).scale((double) scale).render(graphics);
                    pose.popPose();
                    return true;
                } else {
                    pose.popPose();
                    return false;
                }
            }
        }
    }
}
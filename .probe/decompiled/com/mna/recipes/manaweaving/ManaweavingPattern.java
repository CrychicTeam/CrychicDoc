package com.mna.recipes.manaweaving;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.recipes.IManaweavePattern;
import com.mna.items.ItemInit;
import com.mna.recipes.AMRecipeBase;
import com.mna.recipes.RecipeInit;
import com.mna.tools.manaweave.RecognitionEngine;
import com.mojang.math.Axis;
import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class ManaweavingPattern extends AMRecipeBase implements IManaweavePattern {

    private byte[][] pattern;

    public static final int xBound = 11;

    public static final int yBound = 11;

    public ManaweavingPattern(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public void parseExtraJson(JsonObject jsonobject) {
        JsonArray arr = jsonobject.getAsJsonArray("pattern");
        this.pattern = new byte[arr.size()][];
        for (int i = 0; i < arr.size(); i++) {
            JsonElement elem = arr.get(i);
            if (elem.isJsonArray()) {
                JsonArray subArr = elem.getAsJsonArray();
                this.pattern[i] = new byte[subArr.size()];
                for (int j = 0; j < subArr.size(); j++) {
                    this.pattern[i][j] = subArr.get(j).getAsByte();
                }
            }
        }
        this.initializePattern();
    }

    private void initializePattern() {
        if (this.pattern.length == 0) {
            ManaAndArtifice.LOGGER.error("Manaweaving pattern {0} has a length of 0 - this won't work right!", this.m_6423_());
        } else if (this.pattern.length != 11) {
            throw new MalformedParametersException("Manaweave Pattern Array Bounds must be 11x11");
        } else {
            for (int i = 1; i < this.pattern.length; i++) {
                if (this.pattern[i].length != 11) {
                    throw new MalformedParametersException("Manaweave Pattern Array Bounds must be 11x11");
                }
            }
            RecognitionEngine.instance.registerTrainingDataSample(this.m_6423_(), this.pattern);
        }
    }

    @Override
    public byte[][] get() {
        return this.pattern;
    }

    public void setPatternBytes(byte[][] value) {
        this.pattern = value;
        this.initializePattern();
    }

    private static final byte[][] blank() {
        byte[][] comp = new byte[11][];
        for (int i = 0; i < 11; i++) {
            comp[i] = new byte[11];
        }
        return comp;
    }

    public static final ManaweavingPattern match(Level world, int tier, Vector3f[] points, Vector3f[] directions) {
        if (points.length == 1) {
            return null;
        } else {
            if (points.length == 2) {
                Vector3f[] newPoints = new Vector3f[] { points[0], null, points[1] };
                newPoints[1] = new Vector3f((points[0].x() + points[1].x()) / 2.0F, (points[0].y() + points[1].y()) / 2.0F, (points[0].z() + points[1].z()) / 2.0F);
                points = newPoints;
            }
            while (points.length < 100) {
                ArrayList<Vector3f> newPoints = new ArrayList();
                for (int i = 0; i < points.length; i++) {
                    int next = i + 1;
                    if (next == points.length) {
                        break;
                    }
                    newPoints.add(points[i]);
                    newPoints.add(new Vector3f((points[i].x() + points[next].x()) / 2.0F, (points[i].y() + points[next].y()) / 2.0F, (points[i].z() + points[next].z()) / 2.0F));
                }
                points = (Vector3f[]) newPoints.toArray(new Vector3f[0]);
            }
            byte[][] transformedPattern = normalizePoints(world, points, directions);
            if (ManaAndArtifice.instance.isDebug) {
                ManaAndArtifice.LOGGER.debug("Pattern From Player Drawing:");
                for (int i = 0; i < 11; i++) {
                    String str = "[ ";
                    for (int j = 0; j < 11; j++) {
                        str = str + (transformedPattern[i][j] == 0 ? ". " : "O ");
                    }
                    str = str + "]";
                    ManaAndArtifice.LOGGER.debug(str);
                }
            }
            boolean[][] bwPattern = new boolean[transformedPattern.length][transformedPattern[0].length];
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 11; j++) {
                    bwPattern[i][j] = transformedPattern[i][j] != 0;
                }
            }
            ResourceLocation matched = RecognitionEngine.instance.recognize(bwPattern);
            if (matched == null) {
                return null;
            } else {
                Optional<? extends Recipe<?>> recipe = world.getRecipeManager().byKey(matched);
                return recipe != null && recipe.isPresent() && recipe.get() instanceof ManaweavingPattern ? (ManaweavingPattern) recipe.get() : null;
            }
        }
    }

    private static byte[][] normalizePoints(Level world, Vector3f[] points, Vector3f[] directions) {
        Vector3f[] __points = new Vector3f[points.length];
        for (int i = 0; i < points.length; i++) {
            __points[i] = new Vector3f(points[i].x, points[i].y, points[i].z);
        }
        Vector3f min = new Vector3f(2.1474836E9F, 2.1474836E9F, 2.1474836E9F);
        Vector3f max = new Vector3f(-2.1474836E9F, -2.1474836E9F, -2.1474836E9F);
        projectPointsAlongZ(__points, directions, min, max);
        Vector3f delta = new Vector3f(max.x(), max.y(), max.z());
        delta.sub(min);
        Vector3f centerPoint = new Vector3f(0.0F, 0.0F, 0.0F);
        for (int i = 0; i < __points.length; i++) {
            centerPoint.add(__points[i].x(), __points[i].y(), 0.0F);
            __points[i].x = (__points[i].x() - min.x()) / delta.x();
            __points[i].y = (__points[i].y() - min.y()) / delta.y();
        }
        centerPoint.mul(1.0F / (float) __points.length);
        byte[][] comp = blank();
        for (int i = 0; i < __points.length; i++) {
            int x = (int) Math.floor((double) (__points[i].x() * 11.0F));
            int y = (int) Math.floor((double) (__points[i].y() * 11.0F));
            if (x < 11 && x >= 0 && y < 11 && y >= 0) {
                comp[x][y] = 1;
            }
        }
        int M = comp.length;
        int N = comp[0].length;
        byte[][] ret = new byte[N][M];
        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[r][c] = comp[r][M - 1 - c];
            }
        }
        return ret;
    }

    private static void projectPointsAlongZ(Vector3f[] points, Vector3f[] directions, Vector3f out_min, Vector3f out_max) {
        Vector3f c = new Vector3f(0.0F, 0.0F, 0.0F);
        for (int i = 0; i < points.length; i++) {
            c.add(points[i].x(), points[i].y(), points[i].z());
        }
        c.mul(1.0F / (float) points.length);
        Vector3f d = directions[0];
        float yaw = d.x();
        float pitch = d.y();
        for (int i = 0; i < points.length; i++) {
            points[i].sub(c);
            points[i].rotate(Axis.YP.rotationDegrees(yaw));
            points[i].rotate(Axis.XP.rotationDegrees(pitch));
            points[i].add(c);
            points[i].z = c.z();
            out_min.x = Math.min(out_min.x(), points[i].x());
            out_min.y = Math.min(out_min.y(), points[i].y());
            out_min.z = Math.min(out_min.z(), points[i].z());
            out_max.x = Math.max(out_max.x(), points[i].x());
            out_max.y = Math.max(out_max.y(), points[i].y());
            out_max.z = Math.max(out_max.z(), points[i].z());
        }
    }

    public boolean matches(CraftingContainer inv, Level worldIn) {
        return true;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.MANAWEAVING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.MANAWEAVING_PATTERN_TYPE.get();
    }

    @Override
    public ItemStack getGuiRepresentationStack() {
        ItemStack stack = new ItemStack(ItemInit.MANAWEAVER_WAND.get());
        stack.setHoverName(Component.translatable(this.m_6423_().toString()));
        return stack;
    }

    public ManaweavingPattern copy() {
        ManaweavingPattern clone = new ManaweavingPattern(this.m_6423_());
        clone.setPatternBytes(this.pattern);
        return clone;
    }

    @Override
    public ResourceLocation getRegistryId() {
        return this.m_6423_();
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }
}
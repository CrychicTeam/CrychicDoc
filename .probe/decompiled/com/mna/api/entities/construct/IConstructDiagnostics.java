package com.mna.api.entities.construct;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public interface IConstructDiagnostics {

    void pushDiagnosticMessage(String var1, @Nullable ResourceLocation var2, boolean var3);

    void pushTaskUpdate(String var1, ResourceLocation var2, IConstructDiagnostics.Status var3, Vec3 var4);

    void pushTaskUpdate(String var1, ResourceLocation var2, IConstructDiagnostics.Status var3, AABB var4);

    void pushTaskUpdate(String var1, ResourceLocation var2, IConstructDiagnostics.Status var3, int var4);

    void setMovePos(Vec3 var1);

    boolean needsUpdate();

    public static enum Status {

        RUNNING, SUCCESS, FAILURE
    }
}
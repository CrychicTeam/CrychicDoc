package com.rekindled.embers.render;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class PipeModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    T center;

    T connection;

    T connection2;

    T end;

    T end2;

    public static <T extends ModelBuilder<T>> PipeModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
        return new PipeModelBuilder<>(parent, existingFileHelper);
    }

    protected PipeModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
        super(new ResourceLocation("embers", "pipe"), parent, existingFileHelper);
    }

    public PipeModelBuilder<T> parts(T center, T connection, T connection2, T end, T end2) {
        Preconditions.checkNotNull(center, "center must not be null");
        Preconditions.checkNotNull(connection, "connection must not be null");
        Preconditions.checkNotNull(connection2, "connection2 must not be null");
        Preconditions.checkNotNull(end, "end must not be null");
        Preconditions.checkNotNull(end2, "end2 must not be null");
        this.center = center;
        this.connection = connection;
        this.connection2 = connection2;
        this.end = end;
        this.end2 = end2;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);
        json.add("center", this.center.toJson());
        json.add("connection", this.connection.toJson());
        json.add("connection2", this.connection2.toJson());
        json.add("end", this.end.toJson());
        json.add("end2", this.end2.toJson());
        return json;
    }
}
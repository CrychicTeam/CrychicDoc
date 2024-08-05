package net.mehvahdjukaar.moonlight.api.util.math.kmeans;

import java.util.List;

public interface IDataEntry<T> {

    IDataEntry<T> average(List<IDataEntry<T>> var1);

    void setClusterNo(int var1);

    int getClusterNo();

    float distTo(IDataEntry<T> var1);

    T cast();
}
package net.mehvahdjukaar.moonlight.api.util.math.kmeans;

import java.util.LinkedList;
import java.util.List;

public class KMeans {

    static final Double PRECISION = 0.01;

    public static <A> LinkedList<IDataEntry<A>> kMeansPP(DataSet<A> data, int K) {
        LinkedList<IDataEntry<A>> centroids = new LinkedList();
        centroids.add(data.randomFromDataSet());
        for (int i = 1; i < K; i++) {
            centroids.add(data.calculateWeighedCentroid());
        }
        return centroids;
    }

    public static <A> void kMeans(DataSet<A> data, int K) {
        List<IDataEntry<A>> centroids = kMeansPP(data, K);
        Double SSE = Double.MAX_VALUE;
        while (true) {
            for (IDataEntry<A> point : data.getColorPoints()) {
                float minDist = Float.MAX_VALUE;
                for (int i = 0; i < centroids.size(); i++) {
                    float dist = ((IDataEntry) centroids.get(i)).distTo(point);
                    if (dist < minDist) {
                        minDist = dist;
                        point.setClusterNo(i);
                    }
                }
            }
            centroids = data.recomputeCentroids(K);
            Double newSSE = data.calculateTotalSSE(centroids);
            if (SSE - newSSE <= PRECISION) {
                return;
            }
            SSE = newSSE;
        }
    }
}
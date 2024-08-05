package net.mehvahdjukaar.moonlight.api.util.math.kmeans;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.PaletteColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.BaseColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.LABColor;
import net.mehvahdjukaar.moonlight.api.util.math.colors.RGBColor;

public class DataSet<A> {

    private final List<IDataEntry<A>> colorPoints = new LinkedList();

    private final List<IDataEntry<A>> lastCentroids = new LinkedList();

    private final List<Integer> indicesOfCentroids = new LinkedList();

    private final Random random;

    public <T extends IDataEntry<A>> DataSet(List<T> colors) {
        this.colorPoints.addAll(colors);
        this.random = new Random((long) Objects.hash(new Object[] { ((IDataEntry) this.colorPoints.get(0)).distTo((IDataEntry<T>) this.colorPoints.get(this.colorPoints.size() - 1)) }));
    }

    public static DataSet<DataSet.ColorPoint> fromPalette(Palette palette) {
        return new DataSet<>(palette.getValues().stream().map(DataSet.ColorPoint::new).toList());
    }

    public IDataEntry<A> calculateCentroid(int clusterNo) {
        List<IDataEntry<A>> colorsInCluster = new LinkedList();
        for (IDataEntry<A> colorPoint : this.colorPoints) {
            if (colorPoint.getClusterNo() == clusterNo) {
                colorsInCluster.add(colorPoint);
            }
        }
        return (IDataEntry<A>) (colorsInCluster.size() == 0 ? new DataSet.ColorPoint(new PaletteColor(new RGBColor(0))) : ((IDataEntry) colorsInCluster.get(0)).average(colorsInCluster));
    }

    public List<IDataEntry<A>> recomputeCentroids(int clusterSize) {
        this.lastCentroids.clear();
        for (int i = 0; i < clusterSize; i++) {
            this.lastCentroids.add(this.calculateCentroid(i));
        }
        return this.lastCentroids;
    }

    public IDataEntry<A> randomFromDataSet() {
        int index = this.random.nextInt(this.colorPoints.size());
        return (IDataEntry<A>) this.colorPoints.get(index);
    }

    public Double calculateClusterSSE(IDataEntry<A> centroid, int clusterNo) {
        double SSE = 0.0;
        for (IDataEntry<A> colorPoint : this.colorPoints) {
            if (colorPoint.getClusterNo() == clusterNo) {
                float dist = centroid.distTo(colorPoint);
                SSE += (double) (dist * dist);
            }
        }
        return SSE;
    }

    public Double calculateTotalSSE(List<IDataEntry<A>> centroids) {
        Double SSE = 0.0;
        for (int i = 0; i < centroids.size(); i++) {
            SSE = SSE + this.calculateClusterSSE((IDataEntry<A>) centroids.get(i), i);
        }
        return SSE;
    }

    public IDataEntry<A> calculateWeighedCentroid() {
        double sum = 0.0;
        for (int i = 0; i < this.colorPoints.size(); i++) {
            if (!this.indicesOfCentroids.contains(i)) {
                double minDist = Double.MAX_VALUE;
                for (int ind : this.indicesOfCentroids) {
                    double dist = (double) ((IDataEntry) this.colorPoints.get(i)).distTo((IDataEntry) this.colorPoints.get(ind));
                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
                if (this.indicesOfCentroids.isEmpty()) {
                    sum = 0.0;
                }
                sum += minDist;
            }
        }
        double threshold = sum * this.random.nextDouble();
        for (int ix = 0; ix < this.colorPoints.size(); ix++) {
            if (!this.indicesOfCentroids.contains(ix)) {
                double minDist = Double.MAX_VALUE;
                for (int indx : this.indicesOfCentroids) {
                    double dist = (double) ((IDataEntry) this.colorPoints.get(ix)).distTo((IDataEntry) this.colorPoints.get(indx));
                    if (dist < minDist) {
                        minDist = dist;
                    }
                }
                sum += minDist;
                if (sum > threshold) {
                    this.indicesOfCentroids.add(ix);
                    return (IDataEntry<A>) this.colorPoints.get(ix);
                }
            }
        }
        throw new UnsupportedOperationException("Something bad happened");
    }

    public List<IDataEntry<A>> getColorPoints() {
        return this.colorPoints;
    }

    public List<IDataEntry<A>> getLastCentroids() {
        return this.lastCentroids;
    }

    public static class ColorPoint implements IDataEntry<DataSet.ColorPoint> {

        private final int weight;

        private final PaletteColor color;

        private int clusterNo;

        public ColorPoint(PaletteColor color) {
            this.color = color;
            this.weight = color.getOccurrence();
        }

        @Override
        public IDataEntry<DataSet.ColorPoint> average(List<IDataEntry<DataSet.ColorPoint>> others) {
            List<LABColor> pixels = new ArrayList();
            for (int i = 0; i < this.weight; i++) {
                pixels.add(this.color.lab());
            }
            for (IDataEntry<DataSet.ColorPoint> c : others) {
                if (c != this) {
                    for (int i = 0; i < c.cast().weight; i++) {
                        pixels.add(c.cast().color.lab());
                    }
                }
            }
            return new DataSet.ColorPoint(new PaletteColor(BaseColor.mixColors(pixels)));
        }

        @Override
        public void setClusterNo(int clusterNo) {
            this.clusterNo = clusterNo;
        }

        @Override
        public int getClusterNo() {
            return this.clusterNo;
        }

        @Override
        public float distTo(IDataEntry<DataSet.ColorPoint> a) {
            return this.color.lab().distTo(a.cast().color.lab());
        }

        public DataSet.ColorPoint cast() {
            return this;
        }

        public PaletteColor getColor() {
            return this.color;
        }
    }
}
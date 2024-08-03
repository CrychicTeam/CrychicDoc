package journeymap.client.model;

import java.util.HashMap;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class BlockDataArrays {

    private HashMap<MapType, BlockDataArrays.Dataset> datasets = new HashMap(8);

    public void clearAll() {
        this.datasets.clear();
    }

    public BlockDataArrays.Dataset get(MapType mapType) {
        BlockDataArrays.Dataset dataset = (BlockDataArrays.Dataset) this.datasets.get(mapType);
        if (dataset == null) {
            dataset = new BlockDataArrays.Dataset();
            this.datasets.put(mapType, dataset);
        }
        return dataset;
    }

    public static boolean areIdentical(int[][] arr, int[][] arr2) {
        boolean match = true;
        for (int j = 0; j < arr.length; j++) {
            int[] row = arr[j];
            int[] row2 = arr2[j];
            match = IntStream.range(0, row.length).map(i -> ~row[i] | row2[i]).allMatch(n -> n == -1);
            if (!match) {
                break;
            }
        }
        return match;
    }

    public static class DataArray<T> {

        private final HashMap<String, T[][]> map = new HashMap(4);

        private final Supplier<T[][]> initFn;

        protected DataArray(Supplier<T[][]> initFn) {
            this.initFn = initFn;
        }

        public boolean has(String name) {
            return this.map.containsKey(name);
        }

        public T[][] get(String name) {
            return (T[][]) ((Object[][]) this.map.computeIfAbsent(name, s -> (Object[][]) this.initFn.get()));
        }

        public T get(String name, int x, int z) {
            return this.get(name)[z][x];
        }

        public boolean set(String name, int x, int z, T value) {
            T[][] arr = this.get(name);
            T old = arr[z][x];
            arr[z][x] = value;
            return value != old;
        }

        public T[][] copy(String name) {
            T[][] src = this.get(name);
            T[][] dest = (T[][]) ((Object[][]) this.initFn.get());
            for (int i = 0; i < src.length; i++) {
                System.arraycopy(src[i], 0, dest[i], 0, src[0].length);
            }
            return dest;
        }

        public void copyTo(String srcName, String dstName) {
            this.map.put(dstName, this.copy(srcName));
        }

        public void clear(String name) {
            this.map.remove(name);
        }
    }

    public static class Dataset {

        private BlockDataArrays.DataArray<Integer> ints;

        private BlockDataArrays.DataArray<Float> floats;

        private BlockDataArrays.DataArray<Boolean> booleans;

        private BlockDataArrays.DataArray<Object> objects;

        Dataset() {
        }

        public Dataset(MapType mapType) {
        }

        protected void clear() {
            this.ints = null;
            this.floats = null;
            this.booleans = null;
            this.objects = null;
        }

        public BlockDataArrays.DataArray<Integer> ints() {
            if (this.ints == null) {
                this.ints = new BlockDataArrays.DataArray<>(() -> new Integer[16][16]);
            }
            return this.ints;
        }

        public BlockDataArrays.DataArray<Float> floats() {
            if (this.floats == null) {
                this.floats = new BlockDataArrays.DataArray<>(() -> new Float[16][16]);
            }
            return this.floats;
        }

        public BlockDataArrays.DataArray<Boolean> booleans() {
            if (this.booleans == null) {
                this.booleans = new BlockDataArrays.DataArray<>(() -> new Boolean[16][16]);
            }
            return this.booleans;
        }

        public BlockDataArrays.DataArray<Object> objects() {
            if (this.objects == null) {
                this.objects = new BlockDataArrays.DataArray<>(() -> new Object[16][16]);
            }
            return this.objects;
        }
    }
}
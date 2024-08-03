package net.minecraft.util.profiling;

public final class ResultField implements Comparable<ResultField> {

    public final double percentage;

    public final double globalPercentage;

    public final long count;

    public final String name;

    public ResultField(String string0, double double1, double double2, long long3) {
        this.name = string0;
        this.percentage = double1;
        this.globalPercentage = double2;
        this.count = long3;
    }

    public int compareTo(ResultField resultField0) {
        if (resultField0.percentage < this.percentage) {
            return -1;
        } else {
            return resultField0.percentage > this.percentage ? 1 : resultField0.name.compareTo(this.name);
        }
    }

    public int getColor() {
        return (this.name.hashCode() & 11184810) + 4473924;
    }
}
package com.corosus.mobtimizations;

public interface MobtimizationEntityFields {

    long getlastWanderTime();

    void setlastWanderTime(long var1);

    long getlastPlayerScanTime();

    void setlastPlayerScanTime(long var1);

    boolean isplayerInRange();

    void setplayerInRange(boolean var1);
}
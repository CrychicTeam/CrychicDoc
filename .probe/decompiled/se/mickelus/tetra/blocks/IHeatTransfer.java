package se.mickelus.tetra.blocks;

public interface IHeatTransfer {

    boolean canRecieve();

    boolean canSend();

    boolean isReceiving();

    void setReceiving(boolean var1);

    boolean isSending();

    void setSending(boolean var1);

    int getReceiveLimit();

    int getSendLimit();

    int drain(int var1);

    int fill(int var1);

    int getCharge();

    float getEfficiency();

    void updateTransferState();
}
package net.minecraftforge.client.settings;

public interface IKeyConflictContext {

    boolean isActive();

    boolean conflicts(IKeyConflictContext var1);
}
package com.wynntils.mc.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraftforge.eventbus.api.Event;

import java.util.Objects;

public final class AccessPackRepositoryEvent extends Event {
    private final Minecraft minecraft;
    private PackRepository repository;

    public AccessPackRepositoryEvent(Minecraft minecraft, PackRepository repository) {
        this.minecraft = minecraft;
        this.repository = repository;
    }

    public void setRepository(PackRepository packRepository){
        this.repository = packRepository;
    }

    public Minecraft minecraft() {
        return minecraft;
    }

    public PackRepository repository() {
        return repository;
    }
}

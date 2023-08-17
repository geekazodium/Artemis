package com.wynntils.mc.event;

import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraftforge.eventbus.api.Event;

public class PackSelectionInitEvent extends Event {
    private final PackSelectionScreen selectionScreen;

    public PackSelectionInitEvent(PackSelectionScreen selectionScreen) {
        this.selectionScreen = selectionScreen;
    }

    public PackSelectionScreen getSelectionScreen() {
        return selectionScreen;
    }
}

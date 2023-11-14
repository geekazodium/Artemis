/*
 * Copyright © Wynntils 2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.event;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraftforge.eventbus.api.Event;

public class GetPackRepositoryEvent extends Event {
    private PackRepository overridePackRepository = null;

    public void setOverridePackRepo(PackRepository packRepository) {
        this.overridePackRepository = packRepository;
    }

    public PackRepository getOverridePackRepository() {
        return this.overridePackRepository;
    }
}

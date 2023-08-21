/*
 * Copyright © Wynntils 2023.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.features.utilities;

import com.wynntils.WynntilsPackRepository;
import com.wynntils.core.WynntilsMod;
import com.wynntils.core.consumers.features.Feature;
import com.wynntils.core.persisted.Persisted;
import com.wynntils.core.persisted.config.Config;
import com.wynntils.mc.event.PackSelectionInitEvent;
import com.wynntils.mc.event.ScreenInitEvent;
import com.wynntils.mc.mixin.accessors.PackSourceAccessor;
import com.wynntils.utils.mc.McUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourcePackProfilesFeature extends Feature {

    @Persisted
    public final Config<List<String>> wynntilsResources = new Config<>(McUtils.mc().options.resourcePacks);

    @Override
    protected void onConfigUpdate(Config<?> config) {
        WynntilsPackRepository packRepository = McUtils.getPackRepository();
        packRepository.loadNewPackConfig(wynntilsResources.get());
        super.onConfigUpdate(config);
        System.out.println(Arrays.toString(wynntilsResources.get().stream().toArray()));
    }

    @SubscribeEvent
    public void onScreenInit(PackSelectionInitEvent event) {
        PackSelectionScreen screen = event.getSelectionScreen();
        WynntilsPackRepository packRepository = McUtils.getPackRepository();
        screen.addRenderableWidget(new Button.Builder(
                        Component.literal("wynncraft")
                                .withStyle(
                                        packRepository.isUsingWynntilsPack()
                                                ? ChatFormatting.GREEN
                                                : ChatFormatting.RED),
                        button -> {
                            try {
                                ((PackSourceAccessor) screen).watcher().pollForChanges();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            McUtils.getPackRepository().setUsingWynntilsPack(!packRepository.isUsingWynntilsPack());
                            McUtils.mc().reloadResourcePacks();
                        })
                .bounds(10, screen.height - 30, 100, 20)
                .build());
    }

    @SubscribeEvent
    public void onTargetScreenInit(ScreenInitEvent event) {
        if (event.getScreen() instanceof PackSelectionScreen selectionScreen) {
            this.onScreenInit(new PackSelectionInitEvent(selectionScreen));
        }
    }
}

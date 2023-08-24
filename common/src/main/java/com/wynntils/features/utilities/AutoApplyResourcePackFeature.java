/*
 * Copyright © Wynntils 2022-2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.features.utilities;

import com.wynntils.core.components.Services;
import com.wynntils.core.consumers.features.Feature;
import com.wynntils.core.persisted.config.Category;
import com.wynntils.core.persisted.config.ConfigCategory;
import com.wynntils.features.ui.WynncraftButtonFeature;
import com.wynntils.mc.event.ResourcePackEvent;
import com.wynntils.mc.event.ScreenInitEvent;
import com.wynntils.mc.event.TitleScreenInitEvent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ConfigCategory(Category.UTILITIES)
public class AutoApplyResourcePackFeature extends Feature {
    @Override
    public void onDisable() {
        Services.ResourcePack.setRequestedPreloadHash("");
    }

    @SubscribeEvent
    public void onResourcePackLoad(ResourcePackEvent event) {
        String packHash = Services.ResourcePack.calculateHash(event.getUrl());

        String currentHash = Services.ResourcePack.getRequestedPreloadHash();
        if (!packHash.equals(currentHash)) {
            // Use this resource pack as our preloaded pack
            Services.ResourcePack.setRequestedPreloadHash(packHash);
        }
    }

    private int enableTexturesButtonHashcode = -1;
    private Button enableWynntilsTexturesButton = null;

    @SubscribeEvent
    public void onInitTitle(TitleScreenInitEvent event){
        TitleScreen titleScreen = event.getTitleScreen();
        AddWynnTexturesButton(titleScreen);
    }

    @SubscribeEvent
    public void onInitScreen(ScreenInitEvent event){
        if(event.getScreen() instanceof TitleScreen titleScreen){
            AddWynnTexturesButton(titleScreen);
        }
    }

    private void AddWynnTexturesButton(TitleScreen titleScreen) {
        int titleScreenWynncraftButtonY = titleScreen.height / 4 + 48 + 24;
        int buttonY = titleScreenWynncraftButtonY + 24;
        int buttonX = titleScreen.width / 2 + 104;
        if (screenHasEnableTexturesButton(titleScreen)) {
            //titleScreen.removeWidget(enableWynntilsTexturesButton);
            enableWynntilsTexturesButton.setPosition(buttonX,buttonY);
            return;
        }

        enableWynntilsTexturesButton = titleScreen.addRenderableWidget(Button.builder(Component.literal("textures"), button ->{

        }).bounds(buttonX, buttonY,20,20).build());
        enableTexturesButtonHashcode = enableWynntilsTexturesButton.hashCode();
    }

    private boolean screenHasEnableTexturesButton(TitleScreen titleScreen) {
        return titleScreen.children.stream().anyMatch(child -> {
            if (!(child instanceof Button button)) return false;
            if (button.hashCode() != enableTexturesButtonHashcode) return false;
            return button == enableWynntilsTexturesButton;
        });
    }
}

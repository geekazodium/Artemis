/*
 * Copyright © Wynntils 2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.features.utilities;

import com.wynntils.core.consumers.features.Feature;
import com.wynntils.core.persisted.Persisted;
import com.wynntils.core.persisted.config.Category;
import com.wynntils.core.persisted.config.Config;
import com.wynntils.core.persisted.config.ConfigCategory;
import com.wynntils.mc.event.GetPackRepositoryEvent;
import com.wynntils.mc.event.ScreenInitEvent;
import com.wynntils.mc.event.TitleScreenInitEvent;
import com.wynntils.utils.mc.McUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ConfigCategory(Category.UTILITIES)
public class ResourcePackProfilesFeature extends Feature {
    @Persisted
    private final Config<Boolean> usingWynncraftTexturesTitleScreen = new Config<>(true);

    @Persisted
    private final Config<Boolean> usingWynncraftTexturesOtherServers = new Config<>(false);

    @Persisted
    private final Config<Boolean> usingWynncraftTexturesSinglePlayer = new Config<>(false);

    @Persisted
    private final Config<List<String>> WynncraftSelectedTextures = new Config<>(new ArrayList<>());

    public PackRepository wynncraftPackRepository;

    private int enableTexturesButtonHashcode = -1;
    private Button enableWynntilsTexturesButton = null;

    @SubscribeEvent
    public void onInitTitle(TitleScreenInitEvent event) {
        TitleScreen titleScreen = event.getTitleScreen();
        AddWynnTexturesButton(titleScreen);
    }

    @SubscribeEvent
    public void onInitScreen(ScreenInitEvent event) {
        if (event.getScreen() instanceof TitleScreen titleScreen) {
            AddWynnTexturesButton(titleScreen);
        }
    }

    private void toggleTitleScreenPack() {
        usingWynncraftTexturesTitleScreen.setValue(!usingWynncraftTexturesTitleScreen.get());
        this.setUsingWynncraftTextures(usingWynncraftTexturesTitleScreen.get());
    }

    private void AddWynnTexturesButton(TitleScreen titleScreen) {
        int titleScreenWynncraftButtonY = titleScreen.height / 4 + 48 + 24;
        int buttonY = titleScreenWynncraftButtonY + 24;
        int buttonX = titleScreen.width / 2 + 104;
        if (screenHasEnableTexturesButton(titleScreen)) {
            enableWynntilsTexturesButton.setPosition(buttonX, buttonY);
            return;
        }

        enableWynntilsTexturesButton = titleScreen.addRenderableWidget(
                Button.builder(Component.literal("textures"), button -> toggleTitleScreenPack())
                        .bounds(buttonX, buttonY, 20, 20)
                        .build());
        enableTexturesButtonHashcode = enableWynntilsTexturesButton.hashCode();
    }

    private boolean screenHasEnableTexturesButton(Screen screen) {
        return screen.children.stream().anyMatch(this::isGuiElementButton);
    }

    private boolean isGuiElementButton(GuiEventListener child) {
        if (!(child instanceof Button button)) return false;
        if (button.hashCode() != enableTexturesButtonHashcode) return false;
        return button == enableWynntilsTexturesButton;
    }

    @SubscribeEvent
    public void onGetResourcePack(GetPackRepositoryEvent event) {
        if (usingWynncraftTexturesTitleScreen.get()) event.setOverridePackRepo(this.wynncraftPackRepository);
    }

    private boolean usingWynncraftTextures = false;

    public boolean getUsingWynncraftTextures() {
        return usingWynncraftTextures;
    }

    private void setUsingWynncraftTextures(boolean usingWynncraftTextures) {
        boolean pendingPackReload = this.usingWynncraftTextures != usingWynncraftTextures;
        this.usingWynncraftTextures = usingWynncraftTextures;
        if (pendingPackReload) {
            McUtils.mc().reloadResourcePacks();
        }
    }

    @Override
    public void onEnable() {}
}

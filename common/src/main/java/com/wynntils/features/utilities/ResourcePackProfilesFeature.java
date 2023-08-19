package com.wynntils.features.utilities;

import com.wynntils.WynntilsPackRepository;
import com.wynntils.core.consumers.features.Feature;
import com.wynntils.mc.event.PackSelectionInitEvent;
import com.wynntils.mc.event.ScreenInitEvent;
import com.wynntils.mc.mixin.accessors.PackSourceAccessor;
import com.wynntils.utils.mc.McUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ResourcePackProfilesFeature extends Feature {
    @SubscribeEvent
    public void onScreenInit(PackSelectionInitEvent event){
        PackSelectionScreen screen = event.getSelectionScreen();
        WynntilsPackRepository packRepository = McUtils.getPackRepository();
        screen.addRenderableWidget(new Button.Builder(Component.literal("wynncraft").withStyle(packRepository.isUsingWynntilsPack()? ChatFormatting.GREEN:ChatFormatting.RED), button->{
            screen.onClose();
            McUtils.mc().reloadResourcePacks();

        }).bounds(10,screen.height-30,100,20).build());
    }

    @SubscribeEvent
    public void onTargetScreenInit(ScreenInitEvent event){
        if(event.getScreen()instanceof PackSelectionScreen selectionScreen) {
            this.onScreenInit(new PackSelectionInitEvent(selectionScreen));
        }
    }
}

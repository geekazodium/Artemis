package com.wynntils.features.utilities;

import com.wynntils.mc.event.AccessPackRepositoryEvent;
import com.wynntils.mc.event.PackSelectionInitEvent;
import com.wynntils.mc.event.ScreenInitEvent;
import com.wynntils.mc.mixin.accessors.PackSourceAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ResourcePackProfilesFeature {

    private PackRepository wynncraftPackRepository;

    @SubscribeEvent
    public void onPackRepositoryAccess(AccessPackRepositoryEvent event){
        if(wynncraftPackRepository == null){
            wynncraftPackRepository = new PackRepository(((PackSourceAccessor) event.repository()).sources().toArray(new RepositorySource[]{}));
            wynncraftPackRepository.reload();
        }
        if(this.shouldUseWynncraftResources()){
            event.setRepository(wynncraftPackRepository);
        }
    }

    @SubscribeEvent
    public void onScreenInit(PackSelectionInitEvent event){
        PackSelectionScreen screen = event.getSelectionScreen();
        screen.addRenderableWidget(new Button.Builder(Component.literal("wynncraft").withStyle(this.usingWynncraftResources? ChatFormatting.GREEN:ChatFormatting.RED), button->{
            screen.onClose();
            this.usingWynncraftResources = !this.usingWynncraftResources;
            // Minecraft.getInstance().reloadResourcePacks();

        }).bounds(10,screen.height-30,100,20).build());
    }

    @SubscribeEvent
    public void onTargetScreenInit(ScreenInitEvent event){
        if(event.getScreen()instanceof PackSelectionScreen selectionScreen) {
            this.onScreenInit(new PackSelectionInitEvent(selectionScreen));
        }
    }

    private boolean usingWynncraftResources = false;
    public boolean shouldUseWynncraftResources(){
        return usingWynncraftResources;
    }
}

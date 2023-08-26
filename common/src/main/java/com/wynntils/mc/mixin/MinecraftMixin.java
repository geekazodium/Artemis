/*
 * Copyright © Wynntils 2021-2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.mixin;

import com.wynntils.core.events.MixinHelper;
import com.wynntils.mc.event.DisplayResizeEvent;
import com.wynntils.mc.event.GetPackRepositoryEvent;
import com.wynntils.mc.event.ScreenClosedEvent;
import com.wynntils.mc.event.ScreenOpenedEvent;
import com.wynntils.mc.event.TickAlwaysEvent;
import com.wynntils.mc.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraftforge.eventbus.api.Event;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Final private PackRepository resourcePackRepository;

    @Inject(method = "setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("RETURN"))
    private void setScreenPost(Screen screen, CallbackInfo ci) {
        if (screen == null) {
            MixinHelper.post(new ScreenClosedEvent());
        } else {
            MixinHelper.post(new ScreenOpenedEvent.Post(screen));
        }
    }

    @Inject(method = "setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"), cancellable = true)
    private void setScreenPre(Screen screen, CallbackInfo ci) {
        if (screen == null) return;

        ScreenOpenedEvent.Pre event = new ScreenOpenedEvent.Pre(screen);
        MixinHelper.postAlways(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void tickPost(CallbackInfo ci) {
        MixinHelper.post(new TickEvent());
        MixinHelper.postAlways(new TickAlwaysEvent());
    }

    @Inject(method = "resizeDisplay()V", at = @At("RETURN"))
    private void resizeDisplayPost(CallbackInfo ci) {
        MixinHelper.postAlways(new DisplayResizeEvent());
    }

    @Redirect(method = {"rollbackResourcePacks","clearResourcePacksOnError","getResourcePackRepository","reloadResourcePacks(Z)Ljava/util/concurrent/CompletableFuture;"},at = @At(value = "FIELD",target = "Lnet/minecraft/client/Minecraft;resourcePackRepository:Lnet/minecraft/server/packs/repository/PackRepository;",opcode = Opcodes.GETFIELD))
    private PackRepository overrideGetPackRepository(Minecraft minecraft){
        System.out.println("reeeeeeeeeeeeeeeeeee");
        GetPackRepositoryEvent getPackRepositoryEvent = new GetPackRepositoryEvent();
        MixinHelper.postAlways(getPackRepositoryEvent);
        PackRepository overrideValue = getPackRepositoryEvent.getOverridePackRepository();
        return (overrideValue == null)?resourcePackRepository:overrideValue;
    }

}

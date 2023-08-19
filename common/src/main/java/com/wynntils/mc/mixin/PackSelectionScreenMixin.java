package com.wynntils.mc.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(targets = "net.minecraft.client.gui.screens.packs.PackSelectionScreen$Watcher")
public class PackSelectionScreenMixin {

    @Shadow @Final private Path packPath;

    @Inject(method = "close",at = @At("HEAD"))
    private void onClose(CallbackInfo ci){
        System.out.println(this.packPath);
    }
}

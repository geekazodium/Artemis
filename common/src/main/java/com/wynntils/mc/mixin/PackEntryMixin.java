package com.wynntils.mc.mixin;

import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(TransferableSelectionList.PackEntry.class)
public abstract class PackEntryMixin extends ObjectSelectionList.Entry<TransferableSelectionList.PackEntry> {
    @Shadow public abstract String getPackId();

    @Inject(method = "<init>",at = @At("RETURN"))
    public void onInit(){
        System.out.println(this.getPackId());
    }
}

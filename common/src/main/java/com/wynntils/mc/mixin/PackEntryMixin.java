/*
 * Copyright © Wynntils 2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.client.gui.screens.packs.TransferableSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TransferableSelectionList.PackEntry.class)
public abstract class PackEntryMixin extends ObjectSelectionList.Entry<TransferableSelectionList.PackEntry> {
    @Shadow
    public abstract String getPackId();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(
            Minecraft minecraft, TransferableSelectionList parent, PackSelectionModel.Entry pack, CallbackInfo ci) {}
}

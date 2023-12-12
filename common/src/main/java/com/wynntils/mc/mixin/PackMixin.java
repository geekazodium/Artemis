/*
 * Copyright © Wynntils 2023.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.mixin;

import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Pack.class)
public abstract class PackMixin {
    @Shadow
    public abstract String getId();

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 1)
    private static boolean onSetFixedPosition(boolean fixedPosition, String id) {
        if (id.equals("server")) return false;
        return fixedPosition;
    }
}

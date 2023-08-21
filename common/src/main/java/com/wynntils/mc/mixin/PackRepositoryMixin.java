/*
 * Copyright © Wynntils 2023.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.mixin;

import com.wynntils.WynntilsPackRepository;
import com.wynntils.utils.mc.McUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PackRepository.class)
public abstract class PackRepositoryMixin implements WynntilsPackRepository {
    @Unique
    private boolean usingWynntilsPacks = false;

    @Shadow
    private List<Pack> selected;

    @Shadow public abstract @Nullable Pack getPack(String id);

    @Unique
    private List<Pack> wynntilsSelected = new ArrayList<>();

    @Redirect(
            method = "*",
            at =
                    @At(
                            target = "Lnet/minecraft/server/packs/repository/PackRepository;selected:Ljava/util/List;",
                            value = "FIELD",
                            opcode = Opcodes.GETFIELD))
    private List<Pack> onGetSeleceted(PackRepository packRepository) {
        System.out.println(usingWynntilsPacks);
        return usingWynntilsPacks ? this.wynntilsSelected : this.selected;
    }

    @Override
    public void setUsingWynntilsPack(boolean b) {
        if (usingWynntilsPacks != b) {
            usingWynntilsPacks = b;
            McUtils.mc().reloadResourcePacks();
        }
    }

    @Override
    public boolean isUsingWynntilsPack() {
        return usingWynntilsPacks;
    }

    @Override
    public void loadNewPackConfig(List<String> packsConfig) {
        List<Pack> loadedPacks = new ArrayList<>();
        for (int i = 0; i < packsConfig.size(); i++) {
            Pack pack = this.getPack(packsConfig.get(i));
            loadedPacks.add(pack);
        }
        this.selected = loadedPacks;
    }
}

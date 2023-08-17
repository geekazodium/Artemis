package com.wynntils.mc.mixin.accessors;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;


@Mixin(PackRepository.class)
public interface PackSourceAccessor {

    @Accessor("sources")
    Set<RepositorySource> sources();
}

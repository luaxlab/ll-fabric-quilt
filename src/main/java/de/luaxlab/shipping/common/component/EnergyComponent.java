package de.luaxlab.shipping.common.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;

public interface EnergyComponent extends Component {
    EnergyStorage getHandler();

    @Override
    default void readFromNbt(@NotNull CompoundTag tag) {
        //Empty on purpose
    }
    @Override
    default void writeToNbt(@NotNull CompoundTag tag) {
        //Empty on purpose
    }
}

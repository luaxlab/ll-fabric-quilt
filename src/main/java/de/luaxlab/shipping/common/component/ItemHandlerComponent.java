package de.luaxlab.shipping.common.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.nbt.CompoundTag;

public interface ItemHandlerComponent extends Component {
    ItemStackHandler getHandler();

    @Override
    default void readFromNbt(CompoundTag tag) {
        //Empty on purpose
    }
    @Override
    default void writeToNbt(CompoundTag tag) {
        //Empty on purpose
    }
}

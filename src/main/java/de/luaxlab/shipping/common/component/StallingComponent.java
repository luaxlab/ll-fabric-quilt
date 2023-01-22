package de.luaxlab.shipping.common.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.nbt.CompoundTag;

public interface StallingComponent extends Component {

	boolean isDocked();
	void dock(double x, double y, double z);
	void undock();

	boolean isStalled();
	void stall();
	void unstall();

	boolean isFrozen();
	void freeze();
	void unfreeze();

	@Override
	default void readFromNbt(CompoundTag tag) {
		//Empty on purpose
	}
	@Override
	default void writeToNbt(CompoundTag tag) {
		//Empty on purpose
	}
}

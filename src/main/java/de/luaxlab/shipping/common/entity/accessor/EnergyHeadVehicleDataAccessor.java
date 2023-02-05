package de.luaxlab.shipping.common.entity.accessor;

import net.minecraft.world.inventory.ContainerData;

import java.util.function.LongSupplier;

public class EnergyHeadVehicleDataAccessor extends HeadVehicleDataAccessor {
    private static final int SHORT_MASK = 0xFFFF;

    public EnergyHeadVehicleDataAccessor(ContainerData data) {
        super(data);
    }

    /**
     * Lil-endian
	 * Apparently mojang is stupid and the values aint actual integers but shorts.
     */

    public long getEnergy() {
		long st = this.data.get(15) & SHORT_MASK;
		long nd = this.data.get(16) & SHORT_MASK;
		long rd = this.data.get(17) & SHORT_MASK;
		long th = this.data.get(18) & SHORT_MASK;
        return st | nd << 16 | rd << 32 | th << 48;
    }

    public long getCapacity() {
		long st = this.data.get(19) & SHORT_MASK;
		long nd = this.data.get(20) & SHORT_MASK;
		long rd = this.data.get(21) & SHORT_MASK;
		long th = this.data.get(22) & SHORT_MASK;
		return st | nd << 16 | rd << 32 | th << 48;
    }

    public static class Builder extends HeadVehicleDataAccessor.Builder {
        public Builder() {
            this.arr = new SupplierIntArray(23);
        }

        public Builder withEnergy(LongSupplier energy) {
            this.arr.setSupplier(15, () -> (int) (energy.getAsLong() & SHORT_MASK));
            this.arr.setSupplier(16, () -> (int) ((energy.getAsLong() >> 16) & SHORT_MASK));
			this.arr.setSupplier(17, () -> (int) ((energy.getAsLong() >> 32) & SHORT_MASK));
			this.arr.setSupplier(18, () -> (int) ((energy.getAsLong() >> 48) & SHORT_MASK));
            return this;
        }

        public Builder withCapacity(LongSupplier capacity) {
            this.arr.setSupplier(19, () -> (int) (capacity.getAsLong() & SHORT_MASK));
            this.arr.setSupplier(20, () -> (int) ((capacity.getAsLong() >> 16) & SHORT_MASK));
			this.arr.setSupplier(21, () -> (int) ((capacity.getAsLong() >> 32) & SHORT_MASK));
			this.arr.setSupplier(22, () -> (int) ((capacity.getAsLong() >> 48) & SHORT_MASK));
            return this;
        }

        public EnergyHeadVehicleDataAccessor build() {
            return new EnergyHeadVehicleDataAccessor(this.arr);
        }
    }
}

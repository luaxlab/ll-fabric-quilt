package de.luaxlab.shipping.common.energy;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.base.SimpleEnergyStorage;

/**
 * As with the forge version we re-implement this to add read/save functionality.
 * Kind of pointless, but why not.
 */
public class SimpleReadWriteEnergyStorage extends SimpleEnergyStorage {

	public static final String ENERGY_TAG = "energy";

	public SimpleReadWriteEnergyStorage(long capacity, long maxInsert, long maxExtract) {
		super(capacity, maxInsert, maxExtract);
	}

	@NotNull
	public CompoundTag addAdditionalSaveData(CompoundTag tag)
	{
		tag.putLong(ENERGY_TAG, amount);
		return tag;
	}

	public void readAdditionalSaveData(CompoundTag tag)
	{
		// Returns 0 if no value
		amount = tag.getLong(ENERGY_TAG);
	}

	public boolean isFull()
	{
		return amount >= capacity;
	}

	/**
	 * Consumes up to the specified amount of energy and returns the actual consumed energy.
	 * @param maxEnergy Maximum energy to consume.
	 * @return The actual amount that was consumed
	 */
	public long consume(long maxEnergy)
	{
		long actual = Math.min(amount, maxEnergy);
		this.amount -= actual;
		return actual;
	}

}

/*
 Little Logistics: Quilt Edition, a mod about transportation for Minecraft
 Copyright © 2022 EDToaster, Murad Akhundov, LuaX, Abbie

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
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

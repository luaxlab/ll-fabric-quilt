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

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;

import javax.annotation.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class EnergyUtils {

	public static @Nullable EnergyStorage getEnergyCapabilityInSlot(int slot, @NotNull ItemStackHandler handler) {
		ItemStack stack = handler.getStackInSlot(slot);
		if (!stack.isEmpty()) {
			return EnergyStorage.ITEM.find(stack, null);
		}
		return null;
	}

	public static final EnergyStorage CREATIVE = new EnergyStorage() {
		@Override
		public boolean supportsInsertion() {
			return true;
		}

		@Override
		public long insert(long maxAmount, TransactionContext transaction) {
			return maxAmount;
		}

		@Override
		public boolean supportsExtraction() {
			return true;
		}

		@Override
		public long extract(long maxAmount, TransactionContext transaction) {
			return maxAmount;
		}

		@Override
		public long getAmount() {
			return Long.MAX_VALUE/2;
		}

		@Override
		public long getCapacity() {
			return Long.MAX_VALUE;
		}
	};

	public static final EnergyStorage CREATIVE_SUPPLY = new EnergyStorage() {
		@Override
		public boolean supportsInsertion() {
			return false;
		}

		@Override
		public long insert(long maxAmount, TransactionContext transaction) {
			return 0;
		}

		@Override
		public boolean supportsExtraction() {
			return true;
		}

		@Override
		public long extract(long maxAmount, TransactionContext transaction) {
			return maxAmount;
		}

		@Override
		public long getAmount() {
			return Long.MAX_VALUE;
		}

		@Override
		public long getCapacity() {
			return Long.MAX_VALUE;
		}
	};
}


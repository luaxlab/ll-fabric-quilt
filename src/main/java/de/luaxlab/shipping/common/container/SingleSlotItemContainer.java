/**
 Little Logistics: Quilt Edition, a mod about transportation for Minecraft
 Copyright © 2022 EDToaster, LuaX, Murad Akhundov

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
package de.luaxlab.shipping.common.container;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SingleSlotItemContainer extends SimpleContainer {

	@NotNull
	protected final SingleSlotCondition condition;

	public SingleSlotItemContainer(@NotNull SingleSlotCondition allowed)
	{
		super(1);
		condition = allowed;
	}

	@Override
	public boolean canAddItem(ItemStack stack) {
		return super.canAddItem(stack);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return condition.isValidFor(stack);
	}

	@FunctionalInterface
	public interface SingleSlotCondition {

		static SingleSlotCondition NONE = (stack -> true);
		static SingleSlotCondition of(Item item) { return (stack -> stack.is(item)); }

		boolean isValidFor(ItemStack stack);
	}
}

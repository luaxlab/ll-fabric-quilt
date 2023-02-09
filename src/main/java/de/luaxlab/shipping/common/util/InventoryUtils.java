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
package de.luaxlab.shipping.common.util;

import de.luaxlab.shipping.common.core.ModComponents;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

public class InventoryUtils {

    public static boolean mayMoveIntoInventory(Container target, Container source) {
        if (source.isEmpty()){
            return false;
        }

        HashMap<Item, List<ItemStack>> map = new HashMap<>();
        List<Integer> airList = new ArrayList<>();
        for (int i = 0; i < target.getContainerSize(); i++) {
            ItemStack stack = target.getItem(i);
            if((stack.isEmpty() || stack.getItem().equals(Items.AIR)) && target.canPlaceItem(i, stack)){
                airList.add(i);
            }
            else if (stack.getMaxStackSize() != stack.getCount() && target.canPlaceItem(i, stack)) {
                if (map.containsKey(stack.getItem())) {
                    map.get(stack.getItem()).add(stack);
                } else {
                    map.put(stack.getItem(), new ArrayList<>(Collections.singleton(stack)));
                }
            }
        }

        for (int i = 0; i < source.getContainerSize(); i++) {
            ItemStack stack = source.getItem(i);
            if (!stack.isEmpty() && map.containsKey(stack.getItem())) {
                for (ItemStack targetStack : map.get(stack.getItem())){
                    if (canMergeItems(targetStack, stack))
                        return true;
                }
            } else if (!airList.isEmpty() && target instanceof Entity){
                Entity e = (Entity) target;
                boolean validSlot = ModComponents.ITEM_HANDLER.maybeGet(e)
                        .map(itemHandler -> airList.stream()
                                .map(j -> itemHandler.getHandler().isItemValid(j, ItemVariant.of(stack)))
                                .reduce(false, Boolean::logicalOr)).orElse(true);
                if(validSlot) {
                    return true;
                }
            } else if (!airList.isEmpty()){
                return true;
            }
        }
        return false;
    }

    public static int findSlotFotItem(Container target, ItemStack itemStack) {
        for (int i = 0; i < target.getContainerSize(); i++) {
            ItemStack stack = target.getItem(i);
            if(stack.isEmpty() || stack.getItem().equals(Items.AIR)){
                return i;
            }
            else if (canMergeItems(stack, itemStack)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean canMergeItems(ItemStack first, ItemStack second) {
        if (first.getItem() != second.getItem()) {
            return false;
        } else if (first.getDamageValue() != second.getDamageValue()) {
            return false;
        } else if (first.getCount() > first.getMaxStackSize()) {
            return false;
        } else {
            return ItemStack.tagMatches(first, second);
        }
    }

	public static boolean allowMergeInSlot(ItemStack slot, ItemStack canidate) {
		if (slot.getItem() != canidate.getItem()) {
			return false;
		} else if (slot.getDamageValue() != canidate.getDamageValue()) {
			return false;
		} else if (slot.getCount() >= slot.getMaxStackSize()) {
			return false;
		} else {
			return ItemStack.tagMatches(slot, canidate);
		}
	}

	public static ItemStack inventoryAutoMergeStacks(ItemStack[] stacks, ItemStack stack)
	{
		for (ItemStack canidate : stacks)
		{
			if(allowMergeInSlot(canidate, stack))
			{
				int num = Math.min(canidate.getMaxStackSize() - canidate.getCount(),stack.getCount());
				canidate.setCount(canidate.getCount()+num);
				stack.setCount(stack.getCount()-num);
				if(stack.isEmpty()) return ItemStack.EMPTY;
			}
		}
		return stack;
	}

	/*
    @Nullable
    public static IEnergyStorage getEnergyCapabilityInSlot(int slot, ItemStackHandler handler) {
        ItemStack stack = handler.getStackInSlot(slot);
        if (!stack.isEmpty()) {
            LazyOptional<IEnergyStorage> capabilityLazyOpt = stack.getCapability(CapabilityEnergy.ENERGY);
            if (capabilityLazyOpt.isPresent()) {
                Optional<IEnergyStorage> capabilityOpt = capabilityLazyOpt.resolve();
                if (capabilityOpt.isPresent()) {
                    return capabilityOpt.get();
                }
            }
        }
        return null;
    }

	 */
}

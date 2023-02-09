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
package de.luaxlab.shipping.common.entity.vessel.barge;

import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.core.ModItems;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class ChestBargeEntity extends AbstractBargeEntity implements Container, MenuProvider, WorldlyContainer {
    private final int SIZE = 27;
    private final ItemStackHandler itemHandler = new ItemStackHandler(SIZE);

    public ChestBargeEntity(EntityType<? extends ChestBargeEntity> type, Level world) {
        super(type, world);
    }

    public ChestBargeEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.CHEST_BARGE.get(), worldIn, x, y, z);
    }

    @Override
    public void remove(RemovalReason r) {
        if (!level.isClientSide) {
            Containers.dropContents(level, this, this);
        }
        super.remove(r);
    }

    @Override
    public Item getDropItem() {
        return ModItems.CHEST_BARGE.get();
    }


    protected void doInteract(Player player) {
        player.openMenu(this);
    }


    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : itemHandler.stacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        return slot >= 0 && slot < itemHandler.getSlots() && !itemHandler.getStackInSlot(slot).isEmpty() && count > 0
                ? (itemHandler.getStackInSlot(slot)).split(count)
                : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemstack = itemHandler.getStackInSlot(slot);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(Player player) {
        if (isRemoved()) {
            return false;
        } else {
            return !(player.distanceToSqr(this) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        itemHandler.setSize(SIZE);
    }

    @Nullable
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player pPlayer) {
        if (pPlayer.isSpectator()) {
            return null;
        } else {
            return ChestMenu.threeRows(pContainerId, pInventory, this);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Items", itemHandler.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        itemHandler.deserializeNBT(tag.getCompound("Items"));
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction face) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, @NotNull ItemStack stack, @Nullable Direction p_180462_3_) {
        return isDockable();
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, @NotNull ItemStack stack, @NotNull Direction face) {
        return isDockable();
    }

    public static ItemHandlerComponent createItemHandlerComponent(ChestBargeEntity entity) {
        return () -> entity.itemHandler;
    }
}

package de.luaxlab.shipping.common.entity.vessel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public abstract class PartedContainerVesselEntity extends VesselEntity implements Container {

	private final ArrayList<Container> containerParts = new ArrayList<>();
	private final ArrayList<Integer> indexStart = new ArrayList<>();

	protected PartedContainerVesselEntity(EntityType<? extends WaterAnimal> type, Level world) {
		super(type, world);
	}

	@Override
	public int getContainerSize() {
		return containerParts.stream().mapToInt(Container::getContainerSize).sum();
	}

	@Override
	public boolean isEmpty() {
		return containerParts.stream().anyMatch(Container::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot) {
		int i = bySlot(slot);
		return containerParts.get(i).getItem(slot - indexStart.get(i));
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		int i = bySlot(slot);
		return containerParts.get(i).removeItem(slot - indexStart.get(i), amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		int i = bySlot(slot);
		return containerParts.get(i).removeItemNoUpdate(slot - indexStart.get(i));
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		setItemInternal(slot, stack);
	}


	@Override
	public boolean stillValid(Player player) {
		return player.distanceTo(this) < 6;
	}



	@Override
	public void clearContent() {
		for (Container container :
				containerParts) {
			container.clearContent();
		}
	}

	/**
	 * I am pretty sure this can be improved.
	 * Returns the container index at the specified slot.
	 * @param index The index of the slot
	 * @return the container index at the specified slot.
	 * @exception java.util.NoSuchElementException The index is out of range
	 */
	protected int bySlot(int index)
	{
		return indexStart.stream().filter(x -> x >= index).findFirst().orElseThrow();
	}

	public void setItemInternal(int slot, ItemStack stack) {
		int i = bySlot(slot);
		containerParts.get(i).setItem(slot - indexStart.get(i), stack);
	}


	protected void addContainer(Container container)
	{
		containerParts.add(container);
		indexStart.add(containerParts.stream().mapToInt(Container::getContainerSize).sum());
	}

	protected ListTag writeInventoryToNbt(ListTag tag)
	{
		int index=0;
		for (Container container : containerParts) {
			for(int i = 0; i < container.getContainerSize(); i++)
			{
				ItemStack itemStack = container.getItem(i);
				if (!itemStack.isEmpty()) {
					CompoundTag compoundTag = new CompoundTag();
					compoundTag.putByte("Slot", (byte)index);
					itemStack.save(compoundTag);
					tag.add(compoundTag);
				}
				index++;
			}
		}
		return tag;
	}

	protected void loadInventoryFromNbt(ListTag tag)
	{
		for(int i = 0; i < tag.size(); ++i) {
			CompoundTag compoundTag = tag.getCompound(i);
			int j = compoundTag.getByte("Slot") & 255;
			setItemInternal(j, ItemStack.of(compoundTag));
		}

	}
}

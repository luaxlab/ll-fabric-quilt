package de.luaxlab.shipping.client.screen;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class FixedAbstractContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

	public FixedAbstractContainerScreen(T abstractContainerMenu, Inventory inventory, Component component) {
		super(abstractContainerMenu, inventory, component);
	}

	/* Why */

	public int getGuiLeft() { return leftPos; }
	public int getGuiTop() { return topPos; }
	public int getXSize() { return imageWidth; }
	public int getYSize() { return imageHeight; }
}

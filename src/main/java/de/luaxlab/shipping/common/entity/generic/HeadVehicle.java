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
package de.luaxlab.shipping.common.entity.generic;

import de.luaxlab.shipping.common.container.SingleSlotItemContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface HeadVehicle  {

    void setEngineOn(boolean state);

	//TODO: ItemStackHandler
    SingleSlotItemContainer getRouteContainer();

    boolean isValid(Player pPlayer);

    ResourceLocation getRouteIcon();
}

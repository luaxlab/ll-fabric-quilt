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
package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.item.SpringItem;
import de.luaxlab.shipping.common.item.TugRouteItem;
import net.minecraft.client.renderer.item.ItemProperties;

public class ModItemModelProperties {

    public static void register() {
        ItemProperties.register(ModItems.SPRING.get(),
                ModCommon.identifier("springstate"),
                (stack, world, entity, i) -> SpringItem.getState(stack).equals(SpringItem.State.READY) ? 0 : 1);

        ItemProperties.register(ModItems.TUG_ROUTE.get(),
                ModCommon.identifier("routestate"),
                (stack, world, entity, i) -> !TugRouteItem.getRoute(stack).isEmpty() ? 0 : 1);
    }
}

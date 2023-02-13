/*
 Little Logistics: Quilt Edition Integration for "What The Hell is This?"
 Copyright © 2023 LuaX, Abbie

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
package de.luaxlab.shipping.integration.wthit;

import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.ItemComponent;
import org.jetbrains.annotations.Nullable;

public class ModExtensionWailaVessel implements IWailaPlugin, IEntityComponentProvider {
@Override
	public void register(IRegistrar registrar) {
		registrar.addIcon(this, VesselEntity.class);
	}

	@Override
	public @Nullable ITooltipComponent getIcon(IEntityAccessor accessor, IPluginConfig config) {
		return new ItemComponent(((VesselEntity)accessor.getEntity()).getDropItem());
	}

}

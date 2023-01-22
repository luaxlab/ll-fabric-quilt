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
package de.luaxlab.shipping.common.core;

import me.lortseam.completeconfig.api.ConfigContainer;
import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;

import java.util.*;

public class ModConfig implements ConfigContainer {

	@ConfigEntries(includeAll = true)
	@Transitive
	public static class Client implements ConfigGroup
	{

		public static double FISHING_TREASURE_CHANCE_MODIFIER = 0.02;
		public static String FISHING_LOOT_TABLE = "minecraft:gameplay/fishing/fish";

		@ConfigEntry.BoundedDouble(min = 0, max= 1)
		public static double TUG_SMOKE_MODIFIER = 0.4;

		public static boolean DISABLE_TUG_ROUTE_BEACONS = false;
	}

	@ConfigEntries(includeAll = true)
	@Transitive
	public static class Common implements ConfigGroup
	{

		public static double FISHING_TREASURE_CHANCE_MODIFIER = 0.02;
		public static String FISHING_LOOT_TABLE = "minecraft:gameplay/fishing/fish";

		@ConfigEntry.BoundedDouble(min = 0.1d, max= 10d)
		public static double TUG_BASE_SPEED = 2.4;

		@ConfigEntry.BoundedInteger(min=1)
		public static int STEAM_TUG_FUEL_MULTIPLIER = 4;

		@ConfigEntry.BoundedInteger(min=1)
		public static int ENERGY_TUG_BASE_CAPACITY = 10_000;

		@ConfigEntry.BoundedInteger(min=1)
		public static int ENERGY_TUG_BASE_ENERGY_USAGE = 1;

		@ConfigEntry.BoundedInteger(min=1)
		public static int ENERGY_TUG_BASE_MAX_CHARGE_RATE = 100;

		@ConfigEntry.BoundedInteger(min=1)
		public static int VESSEL_CHARGER_BASE_CAPACITY=10_000;

		@ConfigEntry.BoundedInteger(min=1, max=10)
		public static int TUG_PATHFINDING_MULTIPLIER=1;

		//@ConfigEntry(comment = "Damage sources that vessels are invulnerable to")
		@ConfigEntry
		public static List<String> VESSEL_EXEMPT_DAMAGE_SOURCES = List.of("create.mechanical_saw", "create.mechanical_drill");


	}

}

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
package de.luaxlab.shipping.common.network;

import de.luaxlab.shipping.common.core.ModCommon;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

public final class VehiclePacketHandler {
    public static final ResourceLocation LOCATION = ModCommon.identifier("locomotive_channel");
    public static final SimpleChannel INSTANCE = new SimpleChannel(
            LOCATION
    );

    private static int id = 0;
    public static void register() {
        // Class<MSG> messageType, int index
        INSTANCE.registerC2SPacket(SetEnginePacket.class, id++);

        INSTANCE.initServerListener();
    }
}

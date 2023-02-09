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

import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import lombok.RequiredArgsConstructor;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@RequiredArgsConstructor
public class SetEnginePacket implements C2SPacket {
    public final int locoId;
    public final boolean state;

    public SetEnginePacket(FriendlyByteBuf buffer) {
        this.locoId = buffer.readInt();
        this.state = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(locoId);
        buf.writeBoolean(state);
    }

    @Override
    public void handle(MinecraftServer server, ServerPlayer serverPlayer, ServerGamePacketListenerImpl listener, PacketSender sender, SimpleChannel channel) {
        if (serverPlayer != null) {
            var loco = serverPlayer.level.getEntity(locoId);
            if (loco != null && loco.distanceTo(serverPlayer) < 6 && loco instanceof HeadVehicle l) {
                l.setEngineOn(state);
            }
        }
    }
}

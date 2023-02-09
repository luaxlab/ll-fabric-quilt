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

import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.item.TugRouteItem;
import de.luaxlab.shipping.common.util.TugRoute;
import lombok.RequiredArgsConstructor;
import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import static de.luaxlab.shipping.common.network.TugRoutePacketHandler.LOGGER;

@RequiredArgsConstructor
public class SetRouteTagPacket implements C2SPacket {
    public final int routeChecksum;
    public final boolean isOffhand;
    public final CompoundTag tag;

    public SetRouteTagPacket(FriendlyByteBuf buffer) {
        this.routeChecksum = buffer.readInt();
        this.isOffhand = buffer.readBoolean();
        this.tag = buffer.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(routeChecksum);
        buf.writeBoolean(isOffhand);
        buf.writeNbt(tag);
    }

    @Override
    public void handle(MinecraftServer minecraftServer, ServerPlayer serverPlayer, ServerGamePacketListenerImpl serverGamePacketListener, PacketSender packetSender, SimpleChannel simpleChannel) {
        if (serverPlayer == null) {
            LOGGER.error("Received packet not from player, dropping packet");
            return;
        }

        ItemStack heldStack = serverPlayer.getItemInHand(isOffhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        LOGGER.info("Item in hand is {}", heldStack);
        if (heldStack.getItem() != ModItems.TUG_ROUTE.get()) {
            LOGGER.error("Item held in hand was not tug_route item, perhaps client has de-synced? Dropping packet");
            return;
        }

        CompoundTag routeTag = tag;
        LOGGER.info(routeTag);
        TugRouteItem.saveRoute(TugRoute.fromNBT(routeTag), heldStack);
    }
}

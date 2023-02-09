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
package de.luaxlab.shipping.common.item;

import de.luaxlab.shipping.common.entity.accessor.TugRouteScreenDataAccessor;
import de.luaxlab.shipping.common.item.container.TugRouteContainer;
import de.luaxlab.shipping.common.util.LegacyTugRouteUtil;
import de.luaxlab.shipping.common.util.TugRoute;
import de.luaxlab.shipping.common.util.TugRouteNode;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TugRouteItem extends Item {
    private static final Logger LOGGER = LogManager.getLogger(TugRouteItem.class);

    private static final String ROUTE_NBT = "route";
    public TugRouteItem(Properties properties) {
        super(properties);
    }

    protected ExtendedScreenHandlerFactory createContainerProvider(InteractionHand hand) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                getDataAccessor(player, hand).write(buf);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return Component.translatable("screen.littlelogistics.tug_route");
            }


            @Override
            public AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory, @NotNull Player Player) {
                return new TugRouteContainer(i, Player.level, getDataAccessor(Player, hand), playerInventory, Player);
            }
        };
    }

    public TugRouteScreenDataAccessor getDataAccessor(Player entity, InteractionHand hand) {
        return new TugRouteScreenDataAccessor.Builder(entity.getId())
                .withOffHand(hand == InteractionHand.OFF_HAND)
                .build();
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if(!player.level.isClientSide){
            if (player.isShiftKeyDown()) {
                //TODO NetworkHooks.openGui((ServerPlayer) player, createContainerProvider(hand), getDataAccessor(player, hand)::write);
                player.openMenu(createContainerProvider(hand));
            } else {
                int x = (int) Math.floor(player.getX());
                int z = (int) Math.floor(player.getZ());
                if (!tryRemoveSpecific(itemstack, x, z)) {
                    player.displayClientMessage(Component.translatable("item.littlelogistics.tug_route.added", x, z), false);
                    pushRoute(itemstack, x, z);
                } else {
                    player.displayClientMessage(Component.translatable("item.littlelogistics.tug_route.removed", x, z), false);
                }
            }
        }

        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void verifyTagAfterLoad(@Nonnull CompoundTag nbt) {
        super.verifyTagAfterLoad(nbt);
        // convert old nbt format of route: "" into compound format
        // Precond: nbt is non-null, and nbt.tag is nonnull type 10
        CompoundTag tag = nbt.getCompound("tag");
        if (tag.contains(ROUTE_NBT, 8)) {
            LOGGER.info("Found legacy tug route tag, replacing now");
            String routeString = tag.getString(ROUTE_NBT);
            List<Vec2> legacyRoute = LegacyTugRouteUtil.parseLegacyRouteString(routeString);
            TugRoute route = LegacyTugRouteUtil.convertLegacyRoute(legacyRoute);
            tag.put(ROUTE_NBT, route.toNBT());
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("item.littlelogistics.tug_route.description"));
        tooltip.add(
                Component.translatable("item.littlelogistics.tug_route.num_nodes", getRoute(stack).size())
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)));
    }

    public static TugRoute getRoute(ItemStack itemStack) {
        CompoundTag nbt = getTag(itemStack);
        if(nbt == null || !nbt.contains(ROUTE_NBT, 10)) {
            // don't write tag from client side, just return empty route
            return new TugRoute();
        }

        return TugRoute.fromNBT(nbt.getCompound(ROUTE_NBT));
    }

    public static boolean popRoute(ItemStack itemStack) {
        TugRoute route = getRoute(itemStack);
        if(route.size() == 0) {
            return false;
        }
        route.remove(route.size() - 1);
        saveRoute(route, itemStack);
        return true;
    }

    public static boolean tryRemoveSpecific(ItemStack itemStack, int x, int z) {
        TugRoute route = getRoute(itemStack);
        if(route.size() == 0) {
            return false;
        }
        boolean removed = route.removeIf(v -> v.getX() == x && v.getZ() == z);
        saveRoute(route, itemStack);
        return removed;
    }

    public static void pushRoute(ItemStack itemStack, int x, int y) {
        TugRoute route = getRoute(itemStack);
        route.add(new TugRouteNode(x, y));
        saveRoute(route, itemStack);
    }

    // should only be called server side
    public static void saveRoute(TugRoute route, ItemStack itemStack){
        CompoundTag nbt = getTag(itemStack);
        if (nbt == null) {
            nbt = new CompoundTag();
            itemStack.setTag(nbt);
        }
        nbt.put(ROUTE_NBT, route.toNBT());
    }

    @Nullable
    private static CompoundTag getTag(ItemStack stack)  {
        return stack.getTag();
    }
}

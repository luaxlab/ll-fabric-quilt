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
package de.luaxlab.shipping.common.event;

import de.luaxlab.shipping.common.entity.vessel.tug.VehicleFrontPart;
import de.luaxlab.shipping.common.item.SpringItem;
import de.luaxlab.shipping.common.util.LinkableEntity;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class CommonEventHandlerImpl implements UseEntityCallback {
    public static final CommonEventHandlerImpl INSTANCE = new CommonEventHandlerImpl();

    @Override
    public InteractionResult interact(Player player, Level level, InteractionHand hand, Entity target, @Nullable EntityHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if(!stack.isEmpty()) {
            Item item = stack.getItem();
            if(item instanceof SpringItem springItem) {
                if(target instanceof LinkableEntity || target instanceof VehicleFrontPart) {
                    springItem.onUsedOnEntity(stack, player, level, target);
                    return InteractionResult.SUCCESS;
                }
            }

            if(item instanceof ShearsItem) {
                if(target instanceof LinkableEntity v) {
                    v.handleShearsCut();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}

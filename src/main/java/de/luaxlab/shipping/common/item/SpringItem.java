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

import de.luaxlab.shipping.common.entity.vessel.tug.VehicleFrontPart;
import de.luaxlab.shipping.common.util.LinkableEntity;
import lombok.extern.log4j.Log4j2;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Log4j2
public class SpringItem extends Item {

    private final Component springInfo = Component.translatable("item.littlelogistics.spring.description");

    public SpringItem(Properties properties) {
        super(properties);

//        addProperty(new ResourceLocation("first_selected"), (stack, a, b) -> getState(stack) == State.WAITING_NEXT ? 1f : 0f);
    }

    // because 'itemInteractionForEntity' is only for Living entities
    public void onUsedOnEntity(ItemStack stack, Player player, Level world, Entity target) {
        if(target instanceof VehicleFrontPart){
            target = ((VehicleFrontPart) target).getParent();
        }
        if(world.isClientSide)
            return;
		if (getState(stack) == State.WAITING_NEXT) {
			createSpringHelper(stack, player, world, target);
		} else {
			setDominant(world, stack, target);
		}
    }

    private void createSpringHelper(ItemStack stack, Player player, Level world, Entity target) {
        Entity dominant = getDominant(world, stack);
        if(dominant == null)
            return;
        if(dominant == target) {
            player.displayClientMessage(Component.translatable("item.littlelogistics.spring.notToSelf"), true);
        } else if(dominant instanceof LinkableEntity d) {
            if(d.linkEntities(player, target) && !player.isCreative())
                stack.shrink(1);
        }
        resetLinked(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(springInfo);
    }

    private void setDominant(Level worldIn, ItemStack stack, Entity entity) {
        stack.getOrCreateTag().putInt("linked", entity.getId());
    }

    @Nullable
    private Entity getDominant(Level worldIn, ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("linked")) {
            int id = stack.getTag().getInt("linked");
            return worldIn.getEntity(id);
        }
        resetLinked(stack);
        return null;
    }

    private void resetLinked(ItemStack itemstack) {
        itemstack.removeTagKey("linked");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        resetLinked(playerIn.getItemInHand(handIn));
        return super.use(worldIn, playerIn, handIn);
    }

    public static State getState(ItemStack stack) {
        if(stack.getTag() != null && stack.getTag().contains("linked"))
            return State.WAITING_NEXT;
        return State.READY;
    }



    public enum State {
        WAITING_NEXT,
        READY
    }
}

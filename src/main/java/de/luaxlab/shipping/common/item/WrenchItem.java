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

import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class WrenchItem extends Item {
    private static final Map<RailShape, RailShape> nextShapes = Map.ofEntries(
            Map.entry(RailShape.EAST_WEST, RailShape.NORTH_SOUTH),
            Map.entry(RailShape.NORTH_SOUTH, RailShape.NORTH_EAST),
            Map.entry(RailShape.NORTH_EAST, RailShape.NORTH_WEST),
            Map.entry(RailShape.NORTH_WEST, RailShape.SOUTH_WEST),
            Map.entry(RailShape.SOUTH_WEST, RailShape.SOUTH_EAST),
            Map.entry(RailShape.SOUTH_EAST, RailShape.EAST_WEST)
    );

    private Component wrenchInfo = Component.translatable("item.littlelogistics.conductors_wrench.description");

    public WrenchItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(wrenchInfo);
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        var state = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if(state.is(Blocks.RAIL)){
            var shape = state.getValue(RailBlock.SHAPE);
            if(shape.isAscending()){
                return InteractionResult.PASS;
            }
            if(!pContext.getLevel().isClientSide()){
                pContext.getLevel().setBlock(pContext.getClickedPos(),
                        state.setValue(RailBlock.SHAPE, nextShapes.getOrDefault(shape, shape)), 2);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(pContext);
        }
    }
}

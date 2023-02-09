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

import com.mojang.datafixers.util.Function4;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VesselItem extends Item {

    private final Function4<Level, Double, Double, Double, Entity> addEntity;
    private Optional<String> tooltipLocation = Optional.empty();

    public VesselItem(Properties properties, Function4<Level, Double, Double, Double, Entity> addEntity, String tooltip) {
        super(properties);
        this.addEntity = addEntity;
        this.tooltipLocation = Optional.of(tooltip);
    }

    public VesselItem(Properties properties, Function4<Level, Double, Double, Double, Entity> addEntity) {
        super(properties);
        this.addEntity = addEntity;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult raytraceresult = getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);
        if (raytraceresult.getType() == BlockHitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 vector3d = player.getViewVector(1.0F);
            List<Entity> list = world.getEntities(player, player.getBoundingBox().expandTowards(vector3d.scale(5.0D)).inflate(1.0D),
                    EntitySelector.NO_SPECTATORS.and(Entity::isPickable));
            if (!list.isEmpty()) {
                Vec3 vector3d1 = player.getEyePosition(1.0F);

                for(Entity entity : list) {
                    AABB axisalignedbb = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (axisalignedbb.contains(vector3d1)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }

            if (raytraceresult.getType() == BlockHitResult.Type.BLOCK) {
                Entity entity = getEntity(world, raytraceresult);
                entity.setYRot(player.getYRot());
				if(itemstack.hasCustomHoverName())
					entity.setCustomName(itemstack.getHoverName());
                if (!world.noCollision(entity, entity.getBoundingBox().inflate(-0.1D))) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!world.isClientSide) {
                        world.addFreshEntity(entity);
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, world.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }

    protected Entity getEntity(Level world, BlockHitResult raytraceresult) {
        return addEntity.apply(world, raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(Objects.requireNonNull(stack), worldIn, tooltip, flagIn);
        tooltipLocation.ifPresent(loc ->
                tooltip.add(Component.translatable(loc))
        );
    }

}

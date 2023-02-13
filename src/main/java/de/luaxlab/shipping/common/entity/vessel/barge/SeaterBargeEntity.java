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
package de.luaxlab.shipping.common.entity.vessel.barge;

import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.core.ModItems;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SeaterBargeEntity extends AbstractBargeEntity{
    public SeaterBargeEntity(EntityType<? extends SeaterBargeEntity> type, Level world) {
        super(type, world);
    }

    public SeaterBargeEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.SEATER_BARGE.get(), worldIn, x, y, z);
    }


    @Override
    public Item getDropItem() {
        return ModItems.SEATER_BARGE.get();
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity entity) {
        return this.getPassengers().size() < 1;
    }

    private void clampRotation(Entity p_184454_1_) {
        p_184454_1_.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(p_184454_1_.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        p_184454_1_.yRotO += f1 - f;
        p_184454_1_.setYRot(p_184454_1_.getYRot() + f1 - f);
        p_184454_1_.setYHeadRot(p_184454_1_.getYRot());
    }

    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public void positionRider(Entity entity) {
        if (this.hasPassenger(entity)) {
            float f = -0.1F;
            float f1 = (float)((this.dead ? (double)0.01F : this.getPassengersRidingOffset()) + entity.getMyRidingOffset());
            Vec3 vector3d = (new Vec3(f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            entity.setPos(this.getX() + vector3d.x, this.getY() - 0.5 + (double)f1, this.getZ() + vector3d.z);
            if (entity instanceof Animal && this.getPassengers().size() > 1) {
                int j = entity.getId() % 2 == 0 ? 90 : 270;
                entity.setYBodyRot(((Animal)entity).yBodyRot + (float)j);
                entity.setYHeadRot(entity.getYHeadRot() + (float)j);
            }

        }
    }


    @Override
    protected void doInteract(Player player) {
        player.startRiding(this);
    }
}

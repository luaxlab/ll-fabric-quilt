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
package de.luaxlab.shipping.common.entity.navigator;

import de.luaxlab.shipping.common.core.ModConfig;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class TugPathNavigator extends WaterBoundPathNavigation {
    public TugPathNavigator(Mob p_i45873_1_, Level p_i45873_2_) {
        super(p_i45873_1_, p_i45873_2_);
        setMaxVisitedNodesMultiplier(ModConfig.Server.TUG_PATHFINDING_MULTIPLIER.get());
    }

    @Override
    protected PathFinder createPathFinder(int p_179679_1_) {
        this.nodeEvaluator = new TugNodeProcessor();
        return new PathFinder(this.nodeEvaluator, p_179679_1_);
    }

    @Override
    public boolean moveTo(double p_75492_1_, double p_75492_3_, double p_75492_5_, double p_75492_7_) {
        return this.moveTo(this.createPath(p_75492_1_, p_75492_3_, p_75492_5_, 0), p_75492_7_);
    }

    @Override
    protected void doStuckDetection(@NotNull Vec3 currentPos) {
        if (this.tick - this.lastStuckCheck > 100) {
            if (currentPos.distanceToSqr(this.lastStuckCheckPos) < 2.25D) {
                this.stop();
            }

            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = currentPos;
        }

        if (this.path != null && !this.path.isDone()) {
            BlockPos vector3i = this.path.getNextNodePos();
            if (vector3i.equals(this.timeoutCachedNode)) {
                this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
            } else {
                this.timeoutCachedNode = vector3i;
                double d0 = currentPos.distanceTo(Vec3.atCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = this.mob.getSpeed() > 0.0F ? (d0 / (double)this.mob.getSpeed()) * 1000 : 0.0D;
            }

            if (this.timeoutLimit > 0.0D && (double)this.timeoutTimer > this.timeoutLimit * 2.0D) {
                this.timeoutCachedNode = BlockPos.ZERO;
                this.timeoutTimer = 0L;
                this.timeoutLimit = 0.0D;
                this.stop();
            }

            this.lastTimeoutCheck = Util.getMillis();
        }

    }
}

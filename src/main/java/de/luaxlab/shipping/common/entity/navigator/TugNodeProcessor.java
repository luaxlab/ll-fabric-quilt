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

import de.luaxlab.shipping.common.block.guiderail.TugGuideRailBlock;
import de.luaxlab.shipping.common.core.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.*;

import java.util.Arrays;

public class TugNodeProcessor extends SwimNodeEvaluator {
    public TugNodeProcessor() {
        super(false);
    }

    private boolean isOppositeGuideRail(Node Node, Direction direction){
        BlockState state = this.level.getBlockState(Node.asBlockPos().below());
        if (state.is(ModBlocks.GUIDE_RAIL_TUG.get())){
            return TugGuideRailBlock.getArrowsDirection(state).getOpposite().equals(direction);
        }
        return false;
    }

    @Override
    public int getNeighbors(Node[] nodes, Node node) {
        int i = 0;

        for(Direction direction : Arrays.asList(Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH)) {
            Node Node = this.getWaterNode(node.x + direction.getStepX(), node.y + direction.getStepY(), node.z + direction.getStepZ());
            if (Node != null && !Node.closed && !isOppositeGuideRail(Node, direction)) {
                nodes[i++] = Node;
            }
        }

        return i;
    }

    private Node getNodeSimple(int p_176159_1_, int p_176159_2_, int p_176159_3_) {
        return this.nodes.computeIfAbsent(Node.createHash(p_176159_1_, p_176159_2_, p_176159_3_), (p_215743_3_) -> {
            return new Node(p_176159_1_, p_176159_2_, p_176159_3_);
        });
    }

    @Override
    public Target getGoal(double p_224768_1_, double p_224768_3_, double p_224768_5_) {
        return new Target(getNodeSimple(Mth.floor(p_224768_1_), Mth.floor(p_224768_3_), Mth.floor(p_224768_5_)));
    }

    @Override
    protected Node getNode(int p_176159_1_, int p_176159_2_, int p_176159_3_) {
        Node Node = super.getNode(p_176159_1_, p_176159_2_, p_176159_3_);
        if (Node != null) {
            BlockPos pos = Node.asBlockPos();
            float penalty = 0;
            for (BlockPos surr : Arrays.asList(
                    pos.east(),
                    pos.west(),
                    pos.south(),
                    pos.north(),
                    pos.north().west(),
                    pos.north().east(),
                    pos.south().east(),
                    pos.south().west(),
                    pos.north().west().north().west(),
                    pos.north().east().north().east(),
                    pos.south().west().south().west(),
                    pos.south().east().south().east()
            )
            ){
                // if the point's neighbour has land, penalty is 5 unless there is a dock
                if(!level.getFluidState(surr).is(Fluids.WATER)){
                    penalty = 5f;
                }
                if(
                        level.getBlockState(surr).is(ModBlocks.GUIDE_RAIL_CORNER.get()) ||
                                level.getBlockState(surr).is(ModBlocks.BARGE_DOCK.get()) ||
                                level.getBlockState(surr).is(ModBlocks.TUG_DOCK.get())

                ){
                    penalty = 0;
                    break;
                }
            }
            Node.costMalus += penalty;
        }


        return Node;
    }

    private Node getWaterNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        BlockPathTypes pathTypes = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return  pathTypes != BlockPathTypes.WATER ? null : this.getNode(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    private BlockPathTypes isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

        for(int i = p_186327_1_; i < p_186327_1_ + this.entityWidth; ++i) {
            for(int j = p_186327_2_; j < p_186327_2_ + this.entityHeight; ++j) {
                for(int k = p_186327_3_; k < p_186327_3_ + this.entityDepth; ++k) {
                    FluidState fluidstate = this.level.getFluidState(blockpos$mutable.set(i, j, k));
                    BlockState blockstate = this.level.getBlockState(blockpos$mutable.set(i, j, k));
                    if (fluidstate.isEmpty() && blockstate.isPathfindable(this.level, blockpos$mutable.below(), PathComputationType.WATER) && blockstate.isAir()) {
                        return BlockPathTypes.BREACH;
                    }

                    if (!fluidstate.is(FluidTags.WATER)) {
                        return BlockPathTypes.BLOCKED;
                    }
                }
            }
        }

        BlockState blockstate1 = this.level.getBlockState(blockpos$mutable);
        return blockstate1.isPathfindable(this.level, blockpos$mutable, PathComputationType.WATER) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
    }

}

/**
 Little Logistics: Quilt Edition, a mod about transportation for Minecraft
 Copyright © 2022 EDToaster, LuaX, Murad Akhundov

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
package de.luaxlab.shipping.common.entity.vessel.tug;

import de.luaxlab.shipping.common.block.dock.TugDockTileEntity;
import de.luaxlab.shipping.common.block.guiderail.TugGuideRailBlock;
import de.luaxlab.shipping.common.component.StallingComponent;
import de.luaxlab.shipping.common.container.SingleSlotItemContainer;
import de.luaxlab.shipping.common.core.*;
import de.luaxlab.shipping.common.entity.accessor.DataAccessor;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import de.luaxlab.shipping.common.entity.navigator.TugPathNavigator;
import de.luaxlab.shipping.common.entity.vessel.PartedContainerVesselEntity;
import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import de.luaxlab.shipping.common.item.TugRouteItem;
import de.luaxlab.shipping.common.util.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class AbstractTugEntity extends PartedContainerVesselEntity implements LinkableEntityHead<VesselEntity>, WorldlyContainer, MultipartEntity, HeadVehicle {

    // CONTAINER STUFF
	@Getter
	protected final SingleSlotItemContainer routeContainer = new SingleSlotItemContainer(SingleSlotItemContainer.SingleSlotCondition.of(ModItems.TUG_ROUTE));
    protected boolean contentsChanged = false;
    @Getter
    protected boolean docked = false;
    @Getter
    protected int remainingStallTime = 0;

    @Setter
    protected boolean engineOn = true;

    private int dockCheckCooldown = 0;
    private boolean independentMotion = false;
    private int pathfindCooldown = 0;
    private VehicleFrontPart frontHitbox;
    private static final EntityDataAccessor<Boolean> INDEPENDENT_MOTION = SynchedEntityData.defineId(AbstractTugEntity.class, EntityDataSerializers.BOOLEAN);



    public boolean allowDockInterface(){
        return isDocked();
    }

    protected TugRoute path;
    protected int nextStop;

    public AbstractTugEntity(EntityType<? extends WaterAnimal> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
        linkingHandler.train = (new Train<>(this));
        this.path = new TugRoute();
        frontHitbox = new VehicleFrontPart(this);

    }

    public AbstractTugEntity(EntityType type, Level worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public ResourceLocation getRouteIcon() {
        return ModItems.TUG_ROUTE_ICON;
    }


    // CONTAINER STUFF
    @Override
    public void dropLeash(boolean p_110160_1_, boolean p_110160_2_) {
        navigation.recomputePath();
        super.dropLeash(p_110160_1_, p_110160_2_);
    }


    public abstract DataAccessor getDataAccessor();

    @Override
    public boolean isPushedByFluid() {
        return true;
    }

    protected abstract MenuProvider createContainerProvider();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        /*if(compound.contains("inv")){
            SimpleContainer old = new SimpleContainer();
            old.fromTag(compound.getList("inv", Tag.TAG_COMPOUND));
            routeContainer.setItem(0, old.getItem(0));
        }else{
			routeContainer.fromTag(compound.getList("routeHandler", Tag.TAG_COMPOUND));
        }*/
		if(compound.contains("inventory"))
			loadInventoryFromNbt(compound.getList("inventory", Tag.TAG_LIST));

        nextStop = compound.contains("next_stop") ? compound.getInt("next_stop") : 0;
        engineOn = !compound.contains("engineOn") || compound.getBoolean("engineOn");
        contentsChanged = true;
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("next_stop", nextStop);
        compound.putBoolean("engineOn", engineOn);
		compound.put("inventory", writeInventoryToNbt(new ListTag()));
        super.addAdditionalSaveData(compound);
    }

    private void tickRouteCheck() {
        if (contentsChanged) {
            ItemStack stack = routeContainer.getItem(0);
            this.setPath(TugRouteItem.getRoute(stack));
            contentsChanged = false;
        }

        // fix for currently borked worlds
        if (nextStop >= this.path.size()) {
            this.nextStop = 0;
        }
    }

    protected abstract boolean tickFuel();

    public static AttributeSupplier.Builder setCustomAttributes() {
        return VesselEntity.setCustomAttributes()
                .add(Attributes.FOLLOW_RANGE, 200);
    }

    protected void onDock() {
        this.playSound(ModSounds.TUG_DOCKING, 0.6f, 1.0f);
    }

    protected void onUndock() {
        this.playSound(ModSounds.TUG_UNDOCKING, 0.6f, 1.5f);
    }

    // MOB STUFF

    private List<Direction> getSideDirections() {
        return this.getDirection() == Direction.NORTH || this.getDirection() == Direction.SOUTH ?
                Arrays.asList(Direction.EAST, Direction.WEST) :
                Arrays.asList(Direction.NORTH, Direction.SOUTH);
    }


    private void tickCheckDock() {
		StallingComponent cap = getComponent(ModComponents.STALLING);
		if(cap != null)
		{
			int x = (int) Math.floor(this.getX());
			int y = (int) Math.floor(this.getY());
			int z = (int) Math.floor(this.getZ());

			boolean docked = cap.isDocked();

			if (docked && dockCheckCooldown > 0){
				dockCheckCooldown--;
				this.setDeltaMovement(Vec3.ZERO);
				this.moveTo(x + 0.5 ,getY(),z + 0.5);
				return;
			}

			// Check docks
			boolean shouldDock = this.getSideDirections()
					.stream()
					.map((curr) ->
							Optional.ofNullable(level.getBlockEntity(new BlockPos(x + curr.getStepX(), y, z + curr.getStepZ())))
									.filter(entity -> entity instanceof TugDockTileEntity)
									.map(entity -> (TugDockTileEntity) entity)
									.map(dock -> dock.hold(this, curr))
									.orElse(false))
					.reduce(false, (acc, curr) -> acc || curr);

			boolean changedDock = !docked && shouldDock;
			boolean changedUndock = docked && !shouldDock;

			if(shouldDock) {
				dockCheckCooldown = 20; // todo: magic number
				cap.dock(x + 0.5 ,getY(),z + 0.5);
			} else {
				dockCheckCooldown = 0;
				cap.undock();
			}

			if (changedDock) onDock();
			if (changedUndock) onUndock();
		}
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    protected void makeSmoke() {
        Level world = this.level;
        if (world != null) {
            BlockPos blockpos = this.getOnPos().above().above();
            RandomSource random = world.random;
            if (random.nextFloat() < ModConfig.Client.TUG_SMOKE_MODIFIER.get()) {
                for(int i = 0; i < random.nextInt(2) + 2; ++i) {
                    makeParticles(world, blockpos, true, false);
                }
            }
        }
    }

    public static void makeParticles(Level p_220098_0_, BlockPos p_220098_1_, boolean p_220098_2_, boolean p_220098_3_) {
        RandomSource random = p_220098_0_.getRandom();
        Supplier<Boolean> h = () -> random.nextDouble() < 0.5;
        SimpleParticleType basicparticletype = p_220098_2_ ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        double xdrift = (h.get() ? 1 : -1) * random.nextDouble() * 2;
        double zdrift = (h.get() ? 1 : -1) * random.nextDouble() * 2;
        p_220098_0_.addAlwaysVisibleParticle(basicparticletype, true, (double)p_220098_1_.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)p_220098_1_.getY() + random.nextDouble() + random.nextDouble(), (double)p_220098_1_.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.007D * xdrift, 0.05D, 0.007D * zdrift);
    }

    @Override
    protected PathNavigation createNavigation(Level p_175447_1_) {
        return new TugPathNavigator(this, p_175447_1_);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!player.level.isClientSide()) {

			//TODO: network gui
            //NetworkHooks.openGui((ServerPlayer) player, createContainerProvider(), getDataAccessor()::write);
			player.openMenu(createContainerProvider());
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);

        if(level.isClientSide) {
            if(INDEPENDENT_MOTION.equals(key)) {
                independentMotion = entityData.get(INDEPENDENT_MOTION);
            }
        }
    }


    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MovementGoal());
    }

    class MovementGoal extends Goal {
        @Override
        public boolean canUse() {
            return AbstractTugEntity.this.path != null;
        }

        public void tick() {
            if(!AbstractTugEntity.this.level.isClientSide) {
                tickRouteCheck();
                tickCheckDock();

                    followPath();
                    followGuideRail();

            }

        }
    }

	//Not needed here, we implement this ourselves.
	/*
    @Override
    public boolean isMultipartEntity() {
        return true;
    }
    */

    @Override
    public PartEntity<?>[] getParts()
    {
        return new PartEntity<?>[]{frontHitbox};
    }

    @Override
    public void aiStep(){
        super.aiStep();
        if(!isDeadOrDying() && !this.isNoAi()){
            frontHitbox.updatePosition(this);
        }

    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket p_149572_) {
        super.recreateFromPacket(p_149572_);
        frontHitbox.setId(p_149572_.getId());
    }

    public void tick() {


        if(this.level.isClientSide
                && independentMotion){
            makeSmoke();
        }

        super.tick();

    }

    private void followGuideRail(){
        // do not follow guide rail if stalled
        /*var dockcap = getCapability(StallingCapability.STALLING_CAPABILITY);
        if(dockcap.isPresent() && dockcap.resolve().isPresent()){
            var cap = dockcap.resolve().get();
            if(cap.isDocked() || cap.isFrozen() || cap.isStalled())
                return;
        }*/ //TODO: Stalling cap

        List<BlockState> belowList = Arrays.asList(this.level.getBlockState(getOnPos().below()),
                this.level.getBlockState(getOnPos().below().below()));
        BlockState water = this.level.getBlockState(getOnPos());
        for (BlockState below : belowList) {
            if (below.is(ModBlocks.GUIDE_RAIL_TUG) && water.is(Blocks.WATER)) {
                Direction arrows = TugGuideRailBlock.getArrowsDirection(below);
                this.setYRot(arrows.toYRot());
                double modifier = 0.03;
                this.setDeltaMovement(this.getDeltaMovement().add(
                        new Vec3(arrows.getStepX() * modifier, 0, arrows.getStepZ() * modifier)));
            }
        }
    }

    // todo: someone said you could prevent mobs from getting stuck on blocks by override this
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    private void followPath() {
        pathfindCooldown--;
        if (!this.path.isEmpty() && !this.docked && engineOn && !shouldFreezeTrain() && tickFuel()) {
            TugRouteNode stop = path.get(nextStop);
            if (navigation.getPath() == null || navigation.getPath().isDone()
            ) {
                if(pathfindCooldown < 0 || navigation.getPath() != null){  //only go on cooldown when the path was not completed
                    navigation.moveTo(stop.getX(), this.getY(), stop.getZ(), 0.3);
                    pathfindCooldown = 20;
                } else {
                    return;
                }
            }
            double distance = Math.abs(Math.hypot(this.getX() - (stop.getX() + 0.5), this.getZ() - (stop.getZ() + 0.5)));
            independentMotion = true;
            entityData.set(INDEPENDENT_MOTION, true);

            if (distance < 0.9) {
                incrementStop();
            }

        } else{
            entityData.set(INDEPENDENT_MOTION, false);
            this.navigation.stop();
            if (remainingStallTime > 0){
                remainingStallTime--;
            }

            if (this.path.isEmpty()){
                this.nextStop = 0;
            }
        }
    }

    public boolean shouldFreezeTrain() {
        return (stalling.isStalled() && !docked) || linkingHandler.train.asList().stream().anyMatch(VesselEntity::isFrozen);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(INDEPENDENT_MOTION, false);
    }


    public void setPath(TugRoute path) {
        if (!this.path.isEmpty() && !this.path.equals(path)){
            this.nextStop = 0;
        }
        this.path = path;
    }

    private void incrementStop() {
        if (this.path.size() == 1) {
            nextStop = 0;
        } else if (!this.path.isEmpty()) {
            nextStop = (nextStop + 1) % (this.path.size());
        }
    }

    @Override
    public void setDominated(VesselEntity entity) {
        linkingHandler.dominated = (Optional.of(entity));
    }

    @Override
    public void setDominant(VesselEntity entity) {

    }

    @Override
    public void removeDominated() {
        linkingHandler.dominated = (Optional.empty());
        linkingHandler.train.setTail(this);
    }

    @Override
    public void removeDominant() {

    }

    @Override
    public void setTrain(Train<VesselEntity> train) {
        linkingHandler.train = train;
    }

    @Override
    public void remove(Entity.RemovalReason r) {
        if (!this.level.isClientSide) {
            this.spawnAtLocation(this.getDropItem());
            Containers.dropContents(this.level, this, this);
            this.spawnAtLocation(routeContainer.getItem((0)));
        }
        super.remove(r);
    }


    // Have to implement IInventory to work with hoppers

    @Override
    public void setChanged() {
        contentsChanged = true;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_) {
        return false;
    }

    @Override
    public int[] getSlotsForFace(Direction p_180463_1_) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_) {
        return isDocked();
    }

    @Override
    public boolean canBeLeashed(Player p_184652_1_) {
        return true;
    }

    /*
            Stalling Capability
     */
    private final StallingComponent stalling = new StallingComponent() {
        @Override
        public boolean isDocked() {
            return docked;
        }

        @Override
        public void dock(double x, double y, double z) {
            docked = true;
            setDeltaMovement(Vec3.ZERO);
            moveTo(x, y, z);
        }

        @Override
        public void undock() {
            docked = false;
        }

        @Override
        public boolean isStalled() {
            return remainingStallTime > 0;
        }

        @Override
        public void stall() {
            remainingStallTime = 20;
        }

        @Override
        public void unstall() {
            remainingStallTime = 0;
        }

        @Override
        public boolean isFrozen() {
            return AbstractTugEntity.super.isFrozen();
        }

        @Override
        public void freeze() {
            setFrozen(true);
        }

        @Override
        public void unfreeze() {
            setFrozen(false);
        }
    };

	public static @NotNull StallingComponent CreateStallingComponent(AbstractTugEntity entity)
	{
		return new StallingComponent() {
			@Override
			public boolean isDocked() {
				return entity.docked;
			}

			@Override
			public void dock(double x, double y, double z) {
				entity.docked = true;
				entity.setDeltaMovement(Vec3.ZERO);
				entity.moveTo(x, y, z);
			}

			@Override
			public void undock() {
				entity.docked = false;
			}

			@Override
			public boolean isStalled() {
				return entity.remainingStallTime > 0;
			}

			@Override
			public void stall() {
				entity.remainingStallTime = 20;
			}

			@Override
			public void unstall() {
				entity.remainingStallTime = 0;
			}

			@Override
			public boolean isFrozen() {
				return entity.isFrozen();
			}

			@Override
			public void freeze() {
				entity.setFrozen(true);
			}

			@Override
			public void unfreeze() {
				entity.setFrozen(false);
			}

		};
	}


   /* @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == StallingCapability.STALLING_CAPABILITY) {
            return stallingComponent.cast();
        }
        return super.getCapability(cap);
    }*/
}

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
package de.luaxlab.shipping.common.entity.vessel.tug;

import de.luaxlab.shipping.common.block.dock.TugDockTileEntity;
import de.luaxlab.shipping.common.block.guiderail.TugGuideRailBlock;
import de.luaxlab.shipping.common.component.StallingComponent;
import de.luaxlab.shipping.common.core.*;
import de.luaxlab.shipping.common.entity.accessor.DataAccessor;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import de.luaxlab.shipping.common.entity.navigator.TugPathNavigator;
import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import de.luaxlab.shipping.common.item.TugRouteItem;
import de.luaxlab.shipping.common.util.*;
import io.github.fabricators_of_create.porting_lib.entity.MultiPartEntity;
import io.github.fabricators_of_create.porting_lib.entity.PartEntity;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public abstract class AbstractTugEntity extends VesselEntity implements LinkableEntityHead<VesselEntity>, Container, WorldlyContainer, HeadVehicle, MultiPartEntity {

    protected final EnrollmentHandler enrollmentHandler;

    // CONTAINER STUFF
    @Getter
    protected final ItemStackHandler routeItemHandler = createRouteItemHandler();
    protected boolean contentsChanged = false;
    @Getter
    protected boolean docked = false;
    @Getter
    protected int remainingStallTime = 0;
    private double swimSpeedMult = 1;

    @Setter
    protected boolean engineOn = true;

    private int dockCheckCooldown = 0;
    private boolean independentMotion = false;
    private int pathfindCooldown = 0;
    private final VehicleFrontPart frontHitbox;
    private static final EntityDataAccessor<Boolean> INDEPENDENT_MOTION = SynchedEntityData.defineId(AbstractTugEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(AbstractTugEntity.class, EntityDataSerializers.STRING);

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
        enrollmentHandler = new EnrollmentHandler(this);
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

    private ItemStackHandler createRouteItemHandler() {
        return new ItemStackHandler(1) {

			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
            protected void onContentsChanged(int slot) {
                contentsChanged = true;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemVariant stack) {
                return stack.getItem() instanceof TugRouteItem;
            }


            @Override
            public long insertSlot(int slot, @Nonnull ItemVariant stack, long maxAmount, TransactionContext transaction) {
                if (!isItemValid(slot, stack)) {
                    return 0;
                }

                return super.insertSlot(slot, stack, maxAmount, transaction);
            }
        };
    }

    @Override
    public String owner() {
        return entityData.get(OWNER);
    }

    @Override
    public boolean isPushedByFluid() {
        return true;
    }

    protected abstract ExtendedScreenHandlerFactory createContainerProvider();

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if(compound.contains("inv")){
            ItemStackHandler old = new ItemStackHandler();
            old.deserializeNBT(compound.getCompound("inv"));
            routeItemHandler.setStackInSlot(0, old.getStackInSlot(0));
        }else{
            routeItemHandler.deserializeNBT(compound.getCompound("routeHandler"));
        }
        nextStop = compound.contains("next_stop") ? compound.getInt("next_stop") : 0;
        engineOn = !compound.contains("engineOn") || compound.getBoolean("engineOn");
        contentsChanged = true;
        enrollmentHandler.load(compound);
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("next_stop", nextStop);
        compound.putBoolean("engineOn", engineOn);
        compound.put("routeHandler", routeItemHandler.serializeNBT());
        enrollmentHandler.save(compound);
        super.addAdditionalSaveData(compound);
    }

    private void tickRouteCheck() {
        if (contentsChanged) {
            ItemStack stack = routeItemHandler.getStackInSlot(0);
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
        this.playSound(ModSounds.TUG_DOCKING.get(), 0.6f, 1.0f);
    }

    protected void onUndock() {
        this.playSound(ModSounds.TUG_UNDOCKING.get(), 0.6f, 1.5f);
    }

    // MOB STUFF

    private List<Direction> getSideDirections() {
        return this.getDirection() == Direction.NORTH || this.getDirection() == Direction.SOUTH ?
                Arrays.asList(Direction.EAST, Direction.WEST) :
                Arrays.asList(Direction.NORTH, Direction.SOUTH);
    }


    private void tickCheckDock() {
		ModComponents.STALLING.maybeGet(this).ifPresent(cap -> {
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
		});
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
    protected @NotNull PathNavigation createNavigation(@NotNull Level world) {
        return new TugPathNavigator(this, world);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (!player.level.isClientSide()) {

			player.openMenu(createContainerProvider());
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void enroll(UUID uuid) {
        enrollmentHandler.enroll(uuid);
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

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

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
    public void recreateFromPacket(ClientboundAddEntityPacket addEntityPacket) {
        super.recreateFromPacket(addEntityPacket);
        frontHitbox.setId(addEntityPacket.getId());
    }

    public void tick() {


        if(this.level.isClientSide
                && independentMotion){
            makeSmoke();
        }
        if(!this.level.isClientSide) {
            enrollmentHandler.tick();
            enrollmentHandler.getPlayerName().ifPresent(name ->
                    entityData.set(OWNER, name));
        }

        super.tick();

    }

    private void followGuideRail(){
        // do not follow guide rail if stalled
        var dockcap = ModComponents.STALLING.maybeGet(this);
        if(dockcap.isPresent()){
            var cap = dockcap.get();
            if(cap.isDocked() || cap.isFrozen() || cap.isStalled())
                return;
        }

        List<BlockState> belowList = Arrays.asList(this.level.getBlockState(getOnPos().below()),
                this.level.getBlockState(getOnPos().below().below()));
        BlockState water = this.level.getBlockState(getOnPos());
        for (BlockState below : belowList) {
            if (below.is(ModBlocks.GUIDE_RAIL_TUG.get()) && water.is(Blocks.WATER)) {
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
        return !enrollmentHandler.mayMove() || (remainingStallTime > 0 && !docked) || linkingHandler.train.asList().stream().anyMatch(VesselEntity::isFrozen);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(INDEPENDENT_MOTION, false);
        entityData.define(OWNER, "");
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
    public boolean hasOwner(){
        return enrollmentHandler.hasOwner();
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
            Containers.dropContents(this.level, this, this);
            this.spawnAtLocation(routeItemHandler.getStackInSlot(0));
        }
        super.remove(r);
    }


    // Have to implement IInventory to work with hoppers
    @Override
    public @NotNull ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    public boolean canPlaceItem(int p_94041_1_, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public void setChanged() {
        contentsChanged = true;
    }

    @Override
    public boolean isValid(Player p_70300_1_) {
        if (this.isRemoved()) {
            return false;
        } else {
            return !(p_70300_1_.distanceToSqr(this) > 64.0D);
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.isRemoved()) {
            return false;
        } else {
            return !(player.distanceToSqr(this) > 64.0D);
        }
    }

    @Override
    public void clearContent() {

    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, Direction face) {
        return false;
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction face) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, @NotNull ItemStack stack, @Nullable Direction p_180462_3_) {
        return isDocked();
    }
    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean canBeLeashed(@NotNull Player player) {
        return true;
    }

    @Override
    protected double swimSpeed() {
        if(this.level.isClientSide){
            return super.swimSpeed();
        }

        if (this.tickCount % 10 == 0){
           swimSpeedMult = computeSpeedMult();
        }

        return swimSpeedMult * super.swimSpeed();
    }

    private double computeSpeedMult(){
        double mult = 1;
        boolean doBreak = false;
        for (int i = 0; i < 10 && !doBreak; i++) {
            for (Direction direction: List.of(Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH)) {
                BlockPos pos = this.getOnPos().relative(direction, i);
                if(!this.level.getFluidState(pos).isSource()){
                    doBreak = true;
                    break;
                }
            }
            if(i > 3) {
                mult = 1 + ((i / 10f) * 1.8);
            }
        }
        if(mult < swimSpeedMult) return mult;
        else return (mult + swimSpeedMult * 20) / 21;
    }

    /*
                Stalling Capability
         */
    public static StallingComponent createStallingComponent(AbstractTugEntity entity) {
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

	/** internal API **/

	@ApiStatus.Internal
	public int getNextStop()
	{
		return nextStop;
	}

	@ApiStatus.Internal
	public int getTotalStops()
	{
		return path != null ? path.size() : 0;
	}
}

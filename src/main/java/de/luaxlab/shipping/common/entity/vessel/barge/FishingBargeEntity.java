package de.luaxlab.shipping.common.entity.vessel.barge;

import com.mojang.datafixers.util.Pair;
import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.core.ModConfig;
import de.luaxlab.shipping.common.core.ModEntities;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.entity.container.FishingBargeContainer;
import de.luaxlab.shipping.common.entity.container.SteamHeadVehicleContainer;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import de.luaxlab.shipping.common.util.InventoryUtils;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import de.luaxlab.shipping.common.util.LinkableEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.IntStream;

public class FishingBargeEntity extends AbstractBargeEntity implements Container, WorldlyContainer {
    protected final ItemStackHandler itemHandler = createHandler();
    protected boolean contentsChanged = false;
    private int ticksDeployable = 0;
    private int fishCooldown = 0;
    private final Set<Pair<Integer, Integer>> overFishedCoords = new HashSet<>();
    private final Queue<Pair<Integer, Integer>> overFishedQueue = new LinkedList<>();

    private static final ResourceLocation FISHING_LOOT_TABLE =
            new ResourceLocation(ModConfig.Server.FISHING_LOOT_TABLE.get());

    private static final int FISHING_COOLDOWN =
			ModConfig.Server.FISHING_COOLDOWN.get();

    private static final double FISHING_TREASURE_CHANCE =
			ModConfig.Server.FISHING_TREASURE_CHANCE_MODIFIER.get();


    public FishingBargeEntity(EntityType<? extends FishingBargeEntity> type, Level world) {
        super(type, world);
    }
    public FishingBargeEntity(Level worldIn, double x, double y, double z) {
        super(ModEntities.FISHING_BARGE.get(), worldIn, x, y, z);
    }


    @Override
    protected void doInteract(Player player) {
		player.openMenu(createContainerProvider());
    }

	protected ExtendedScreenHandlerFactory createContainerProvider() {
		return new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
				buf.writeInt(FishingBargeEntity.this.getId());
			}

			@Override
			public @NotNull Component getDisplayName() {
				return FishingBargeEntity.this.hasCustomName() ? Objects.requireNonNull(FishingBargeEntity.this.getCustomName())
						: Component.translatable("screen.littlelogistics.fishing_barge");
			}


			@Override
			public AbstractContainerMenu createMenu(int i, @NotNull Inventory playerInventory, @NotNull Player Player) {
				return new FishingBargeContainer(i, level, FishingBargeEntity.this.getId(), playerInventory, Player);
			}
		};
	}

    @Override
    public void remove(RemovalReason r) {
        if (!this.level.isClientSide) {
            Containers.dropContents(this.level, this, this);
        }
        super.remove(r);
    }


    private ItemStackHandler createHandler() {
        return new ItemStackHandler(27);
    }


    @Override
    public void tick(){
        super.tick();
        tickWaterOnSidesCheck();
        if(!this.level.isClientSide && this.getStatus() == Status.DEPLOYED){
            if(fishCooldown < 0) {
                tickFish();
                fishCooldown = FISHING_COOLDOWN;
            }  else {
                fishCooldown--;
            }
        } else if(this.level.isClientSide)
		{
			tickAnimation();
		}

    }


    private void tickWaterOnSidesCheck(){
        if(hasWaterOnSides()){
            ticksDeployable++;
        }else {
            ticksDeployable = 0;
        }
    }


    private double computeDepthPenalty(){
        int count = 0;
        for (BlockPos pos = this.getOnPos(); this.level.getBlockState(pos).getBlock().equals(Blocks.WATER); pos = pos.below()){
            count ++;
        }
        count = Math.min(count, 20);
        return ((double) count) / 20.0;
    }

    private void tickFish(){
        double overFishPenalty = isOverFished() ? 0.05 : 1;
        double shallowPenalty = computeDepthPenalty();
        double chance = 0.25 * overFishPenalty * shallowPenalty;
        double treasure_chance = shallowPenalty > 0.4 ? chance * (shallowPenalty / 2)
                *  FISHING_TREASURE_CHANCE : 0;
        double r = Math.random();
        if(r < chance){
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level))
                    .withParameter(LootContextParams.ORIGIN, this.position())
                    .withParameter(LootContextParams.THIS_ENTITY, this)
                    .withParameter(LootContextParams.TOOL, new ItemStack(Items.FISHING_ROD))
                    .withRandom(this.random);

            //lootcontext$builder.withParameter(LootContextParams.KILLER_ENTITY, this).withParameter(LootContextParams.THIS_ENTITY, this);
            LootTable loottable = this.level.getServer().getLootTables().get(r < treasure_chance ? BuiltInLootTables.FISHING_TREASURE : FISHING_LOOT_TABLE);
            List<ItemStack> list = loottable.getRandomItems(lootcontext$builder.create(LootContextParamSets.FISHING));
            for (ItemStack stack : list) {
                InventoryUtils.inventoryAutoMergeStacks(itemHandler.stacks, stack);

                if(!isOverFished()) {
                    addOverFish();
                }
            }
        }
    }

    private String overFishedString(){
        return overFishedQueue.stream().map(t -> t.getFirst() + ":" + t.getSecond()).reduce("", (acc, curr) -> String.join(",", acc, curr));
    }

    private void populateOverfish(String string){
        Arrays.stream(string.split(","))
                .filter(s -> !s.isEmpty())
                .map(s -> s.split(":"))
                .map(arr -> new Pair(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])))
                .forEach(overFishedQueue::add);
        overFishedCoords.addAll(overFishedQueue);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        //backwards compat
        CompoundTag inv = compound.getCompound("inv");
        inv.remove("Size");

        itemHandler.deserializeNBT(inv);


        populateOverfish(compound.getString("overfish"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.put("inv", itemHandler.serializeNBT());
        compound.putString("overfish", overFishedString());
        super.addAdditionalSaveData(compound);
    }

    private void addOverFish(){
        int x = (int) Math.floor(this.getX());
        int z = (int) Math.floor(this.getZ());
        overFishedCoords.add(new Pair<>(x, z));
        overFishedQueue.add(new Pair<>(x, z));
        if(overFishedQueue.size() > 30){
            overFishedCoords.remove(overFishedQueue.poll());
        }
    }

    private boolean isOverFished(){
        int x = (int) Math.floor(this.getX());
        int z = (int) Math.floor(this.getZ());
        return overFishedCoords.contains(new Pair<>(x, z));
    }

    @Override
    public Item getDropItem() {
        return ModItems.FISHING_BARGE.get();
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction direction) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack stack, @Nullable Direction p_180462_3_) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack stack, Direction direction) {
        return isDockable();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++){
            if(!itemHandler.getStackInSlot(i).isEmpty() && !itemHandler.getStackInSlot(i).getItem().equals(Items.AIR)){
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

	@Override
	public @NotNull ItemStack removeItem(int slot, int count) {
		return slot >= 0 && slot < itemHandler.getSlots() && !itemHandler.getStackInSlot(slot).isEmpty() && count > 0
				? (itemHandler.getStackInSlot(slot)).split(count)
				: ItemStack.EMPTY;
	}

	@Override
	public @NotNull ItemStack removeItemNoUpdate(int slot) {
		ItemStack itemstack = itemHandler.getStackInSlot(slot);
		if (itemstack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
			return itemstack;
		}
	}


    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public void setChanged() {
        contentsChanged = true;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.dead) {
            return false;
        } else {
            return !(player.distanceToSqr(this) > 64.0D);
        }
    }

    public Status getStatus(){
        return hasWaterOnSides() ? getNonStashedStatus() : Status.STASHED;
    }

    private Status getNonStashedStatus(){
        if (ticksDeployable < 40){
            return Status.TRANSITION;
        } else {
            return this.applyWithDominant(LinkableEntity::hasWaterOnSides)
                    .reduce(true, Boolean::logicalAnd)
                    ? Status.DEPLOYED : Status.TRANSITION;
        }
    }

	@Override
	public void clearContent() {
		//TODO: Clear inventory
	}

	public enum Status {
        STASHED,
        DEPLOYED,
        TRANSITION
    }

	public static ItemHandlerComponent createItemHandlerComponent(FishingBargeEntity entity) {
		return () -> entity.itemHandler;
	}

	/* Client only stuff */

	private static final float ANIMATION_PROGRESS = 1/40f; //Finish in 40 ticks (=2s)
	@Environment(EnvType.CLIENT)
	private float deployAnimation = 0, prevDeployAnimation = 0;

	@Environment(EnvType.CLIENT)
	private void tickAnimation()
	{
		prevDeployAnimation = deployAnimation;
		if(ticksDeployable > 0 && deployAnimation < 1f)
			deployAnimation = Mth.clamp(deployAnimation+ANIMATION_PROGRESS, 0f, 1f);
		else if (ticksDeployable == 0 && deployAnimation > 0f)
			deployAnimation = Mth.clamp(deployAnimation-ANIMATION_PROGRESS, 0f, 1f);
	}

	@Environment(EnvType.CLIENT)
	public float getAnimationProgress(float delta)
	{
		return (-Mth.cos(Mth.lerp(delta, prevDeployAnimation, deployAnimation)*Mth.PI)+1)/2;
	}

}

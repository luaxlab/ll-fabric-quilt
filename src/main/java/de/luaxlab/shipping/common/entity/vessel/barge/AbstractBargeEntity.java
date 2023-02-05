package de.luaxlab.shipping.common.entity.vessel.barge;


import de.luaxlab.shipping.common.component.StallingComponent;
import de.luaxlab.shipping.common.core.ModComponents;
import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.AbstractTugEntity;
import de.luaxlab.shipping.common.util.Train;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class AbstractBargeEntity extends VesselEntity {
    public AbstractBargeEntity(EntityType<? extends AbstractBargeEntity> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
        linkingHandler.train = new Train<>(this);
    }

    public AbstractBargeEntity(EntityType<? extends AbstractBargeEntity> type, Level worldIn, double x, double y, double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return false;
    }


    public abstract Item getDropItem();


    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (!this.level.isClientSide) {
            doInteract(player);
        }
        // don't interact *and* use current item
        return InteractionResult.CONSUME;
    }

    abstract protected void doInteract(Player player);

    public boolean hasWaterOnSides(){
        return super.hasWaterOnSides();
    }

    @Override
    public void setDominated(VesselEntity entity) {
        linkingHandler.dominated = Optional.of(entity);
    }

    @Override
    public void setDominant(VesselEntity entity) {
        this.setTrain(entity.getTrain());
        linkingHandler.dominant = Optional.of(entity);
    }

    @Override
    public void removeDominated() {
        if(!this.isAlive()){
            return;
        }
        linkingHandler.dominated = Optional.empty();
        linkingHandler.train.setTail(this);
    }

    @Override
    public void removeDominant() {
        if(!this.isAlive()){
            return;
        }
        linkingHandler.dominant = Optional.empty();
        this.setTrain(new Train(this));
    }

    @Override
    public void setTrain(Train<VesselEntity> train) {
        linkingHandler.train = train;
        train.setTail(this);
        linkingHandler.dominated.ifPresent(dominated -> {
            // avoid recursion loops
            if(!dominated.getTrain().equals(train)){
                dominated.setTrain(train);
            }
        });
    }

    // hack to disable hoppers
    public boolean isDockable() {
        return this.linkingHandler.dominant.map(dom -> this.distanceToSqr(dom) < 1.1).orElse(true);
    }

    public boolean allowDockInterface(){
        return isDockable();
    }

    public static StallingComponent createStallingComponent(AbstractBargeEntity entity) {
        return new StallingComponent() {
            @Override
            public boolean isDocked() {
                return delegate().map(StallingComponent::isDocked).orElse(false);
            }

            @Override
            public void dock(double x, double y, double z) {
                delegate().ifPresent(s -> s.dock(x, y, z));
            }

            @Override
            public void undock() {
                delegate().ifPresent(StallingComponent::undock);
            }

            @Override
            public boolean isStalled() {
                return delegate().map(StallingComponent::isStalled).orElse(false);
            }

            @Override
            public void stall() {
                delegate().ifPresent(StallingComponent::stall);
            }

            @Override
            public void unstall() {
                delegate().ifPresent(StallingComponent::unstall);
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

            private Optional<StallingComponent> delegate() {
                if (entity.linkingHandler.train.getHead() instanceof AbstractTugEntity e) {
                    return Optional.of(e.getComponent(ModComponents.STALLING));
                }
                return Optional.empty();
            }
        };
    }
}

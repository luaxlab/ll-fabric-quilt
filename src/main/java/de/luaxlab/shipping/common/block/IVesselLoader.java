package de.luaxlab.shipping.common.block;

import de.luaxlab.shipping.common.util.LinkableEntity;
import net.minecraft.world.entity.Entity;

public interface IVesselLoader {
    enum Mode {
        EXPORT,
        IMPORT
    }

    /*static <T> Optional<T> getEntityCapability(BlockPos pos, Capability<T> capability, Level level){
        List<Entity> fluidEntities = level.getEntities((Entity) null,
                getSearchBox(pos),
                (e -> entityPredicate(e, pos, capability))
        );

        if(fluidEntities.isEmpty()){
            return Optional.empty();
        } else {
            Entity entity = fluidEntities.get(0);
            return entity.getCapability(capability).resolve();
        }
    }

    static boolean entityPredicate(Entity entity, BlockPos pos, Capability<?> capability) {
        return entity.getCapability(capability).resolve().map(cap -> {
            if (entity instanceof LinkableEntity l){
                return l.allowDockInterface() && (l.getBlockPos().getX() == pos.getX() && l.getBlockPos().getZ() == pos.getZ());
            } else {
                return true;
            }
        }).orElse(false);
    }

    static AABB getSearchBox(BlockPos pos) {
        return new AABB(
                pos.getX() ,
                pos.getY(),
                pos.getZ(),
                pos.getX() + 1D,
                pos.getY() + 1D,
                pos.getZ() + 1D);
    }*/

    <T extends Entity & LinkableEntity<T>> boolean hold(T vehicle, Mode mode);

}

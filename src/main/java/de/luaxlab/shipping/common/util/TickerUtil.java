package de.luaxlab.shipping.common.util;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;

public class TickerUtil {
    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> typeA, BlockEntityType<E> typeB, BlockEntityTicker<? super E> ticker) {
        return typeB == typeA ? (BlockEntityTicker<A>)ticker : null;
    }
}

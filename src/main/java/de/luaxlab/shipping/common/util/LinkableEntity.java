/*
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
package de.luaxlab.shipping.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public interface LinkableEntity<V extends LinkableEntity<V>> {

    Optional<V> getDominated();
    Optional<V> getDominant();
    void setDominated(V entity);
    void setDominant(V entity);
    void removeDominated();
    void removeDominant();
    void handleShearsCut();
    Train<V> getTrain();
    boolean linkEntities(Player player, Entity target);
    void setTrain(Train<V> train);
    boolean hasWaterOnSides();

    default void handleLinkableKill(){
        this.getDominated().ifPresent(LinkableEntity::removeDominant);
        this.getDominant().ifPresent(LinkableEntity::removeDominated);
    }

    default boolean checkNoLoopsDominated(){
        return checkNoLoopsHelper(this, (LinkableEntity::getDominated), new HashSet<>());
    }

    default boolean checkNoLoopsDominant(){
        return checkNoLoopsHelper(this, (LinkableEntity::getDominant), new HashSet<>());
    }

    default boolean checkNoLoopsHelper(LinkableEntity<V> entity, Function<LinkableEntity<V>, Optional<V>> next, Set<LinkableEntity<V>> set){
        if(set.contains(entity)){
            return true;
        }
        set.add(entity);
        Optional<V> nextEntity = next.apply(entity);
        return nextEntity.map(e -> this.checkNoLoopsHelper(e, next, set)).orElse(false);
    }

    default<U> Stream<U> applyWithAll(Function<LinkableEntity<V>, U> function){
        return this.getTrain().getHead().applyWithDominated(function);
    }

    default<U> Stream<U> applyWithDominant(Function<LinkableEntity<V>, U> function){
        Stream<U> ofThis = Stream.of(function.apply(this));

        return checkNoLoopsDominant() ? ofThis : this.getDominant().map(dom ->
                Stream.concat(ofThis, dom.applyWithDominant(function))
        ).orElse(ofThis);

    }

    default<U> Stream<U> applyWithDominated(Function<LinkableEntity<V>, U> function){
        Stream<U> ofThis = Stream.of(function.apply(this));

        return checkNoLoopsDominated() ? ofThis : this.getDominated().map(dom ->
                Stream.concat(ofThis, dom.applyWithDominated(function))
        ).orElse(ofThis);

    }

    boolean allowDockInterface();

    BlockPos getBlockPos();

    enum LinkSide {
        DOMINANT, DOMINATED
    }
}

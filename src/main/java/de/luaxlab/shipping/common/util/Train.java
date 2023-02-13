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
package de.luaxlab.shipping.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Train <V extends LinkableEntity<V>> {
    private final Optional<V> tug;
    private V tail;
    private V head;

    public Train(V entity){
        head = entity;
        tail = entity;
        this.tug = entity instanceof LinkableEntityHead ? Optional.of(entity) : Optional.empty();
    }

    public Optional<V> getTug() {
        return tug;
    }

    public V getTail() {
        return tail;
    }

    public void setTail(V tail) {
        this.tail = tail;
    }

    public V getHead() {
        return head;
    }

    public List<V> asListOfTugged(){
        if(this.head.checkNoLoopsDominated()) {
            // just in case - to avoid crashing the world.
            this.head.removeDominated();
            this.head.getDominated().ifPresent(LinkableEntity::removeDominant);
            return new ArrayList<>();
        }
        return tug.map(tugEntity -> {
            List<V> barges = new ArrayList<>();
            for (Optional<V> barge = getNext(tugEntity); barge.isPresent(); barge = getNext(barge.get())){
                barges.add(barge.get());
            }
            return barges;
        }).orElse(new ArrayList<>());
    }

    public List<V> asList(){
        if(this.head.checkNoLoopsDominated()) {
            // just in case - to avoid crashing the world.
            this.head.removeDominated();
            this.head.getDominated().ifPresent(LinkableEntity::removeDominant);
            return new ArrayList<>();
        }

        List<V> barges = new ArrayList<>();
        for (Optional<V> barge = Optional.of(head); barge.isPresent(); barge = getNext(barge.get())){
            barges.add(barge.get());
        }
        return barges;
    }

    public Optional<V> getNext(V entity){
        return entity.getDominated().map(t -> (V) t);
    }

    public void setHead(V head) {
        this.head = head;
    }
}

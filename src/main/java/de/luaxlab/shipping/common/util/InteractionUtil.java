package de.luaxlab.shipping.common.util;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class InteractionUtil {
    public static boolean doConfigure(Player player, InteractionHand hand) {
        return  (player.getPose().equals(Pose.CROUCHING) && player.getItemInHand(hand).isEmpty());
    }
}

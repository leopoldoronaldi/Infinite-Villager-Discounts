package net.openscore.infinitevillagerdiscounts.mixin;

import net.minecraft.world.entity.ai.gossip.GossipType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.ai.gossip.GossipContainer$EntityGossips")
public class EntityGossipsMixin {

    @Redirect(
        method = "makeSureValueIsntTooLowOrTooHigh(Lnet/minecraft/world/entity/ai/gossip/GossipType;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/gossip/GossipType;max:I")
    )
    private int infinite_villager_discounts$overrideMaxPerVillager(GossipType instance) {
        if (instance == GossipType.MAJOR_POSITIVE) {
            return Integer.MAX_VALUE;
        }
        return instance.max;
    }
}

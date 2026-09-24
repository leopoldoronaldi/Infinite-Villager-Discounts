package net.openscore.infinitevillagerdiscounts.mixin;

import net.minecraft.world.entity.ai.gossip.GossipType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.ai.gossip.GossipContainer")
public class GossipContainerMixin {

    @Redirect(
        method = "mergeValuesForAddition(Lnet/minecraft/world/entity/ai/gossip/GossipType;II)I",
        at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/gossip/GossipType;max:I")
    )
    private int infinite_villager_discounts$removePositiveReputationLimit(GossipType type) {
        if (type == GossipType.MAJOR_POSITIVE) {
            return Integer.MAX_VALUE;
        }
        return type.max;
    }
}

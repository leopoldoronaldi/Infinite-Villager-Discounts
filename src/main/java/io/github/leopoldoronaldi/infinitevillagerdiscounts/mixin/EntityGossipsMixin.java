package io.github.leopoldoronaldi.infinitevillagerdiscounts.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.entity.ai.gossip.GossipType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.entity.ai.gossip.GossipContainer$EntityGossips")
public class EntityGossipsMixin {

    @Shadow
    @Final
    private Object2IntMap<GossipType> entries;

    @Shadow
    public void remove(GossipType type) {
        throw new AssertionError();
    }

    @Inject(method = "makeSureValueIsntTooLowOrTooHigh", at = @At("HEAD"), cancellable = true)
    private void infinite_villager_discounts$removePositiveClamp(GossipType type, CallbackInfo ci) {
        if (entries.getInt(type) < 2) {
            remove(type);
        }
        ci.cancel();
    }
}

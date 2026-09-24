package net.openscore.infinitevillagerdiscounts.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.entity.ai.gossip.GossipType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.entity.ai.gossip.GossipContainer$EntityGossips")
public class EntityGossipsMixin {

    @Shadow
    @Final
    private Object2IntMap<GossipType> entries;

    @Overwrite
    public void makeSureValueIsntTooLowOrTooHigh(GossipType type) {
        if (entries.getInt(type) < 2) {
            remove(type);
        }
    }

    @Shadow
    public void remove(GossipType type) {
        throw new AssertionError();
    }
}

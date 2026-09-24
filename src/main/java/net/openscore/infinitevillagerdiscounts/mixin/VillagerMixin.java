package net.openscore.infinitevillagerdiscounts.mixin;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Villager.class)
public class VillagerMixin {

    @Overwrite
    private void updateSpecialPrices(Player player) {
        Villager villager = (Villager) (Object) this;
        int reputation = villager.getPlayerReputation(player);

        for (MerchantOffer offer : villager.getOffers()) {
            offer.setSpecialPriceDiff(-Math.max(0,
                (int) Math.floor(reputation * offer.getPriceMultiplier())));
        }

        if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
            int amplifier = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE).getAmplifier();
            double heroDiscount = 0.3D + 0.0625D * amplifier;

            for (MerchantOffer offer : villager.getOffers()) {
                int discount = Math.max(1, (int) Math.floor(
                    heroDiscount * offer.getBaseCostA().getCount()));
                offer.addToSpecialPriceDiff(-discount);
            }
        }
    }
}

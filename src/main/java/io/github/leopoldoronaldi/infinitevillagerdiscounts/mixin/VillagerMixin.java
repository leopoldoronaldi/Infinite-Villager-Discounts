package io.github.leopoldoronaldi.infinitevillagerdiscounts.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public class VillagerMixin {

    @Shadow
    private void resetSpecialPrices() {
        throw new AssertionError();
    }

    @Inject(method = "updateSpecialPrices", at = @At("HEAD"), cancellable = true)
    private void infinite_villager_discounts$stackDiscounts(Player player, CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;

        resetSpecialPrices();

        int reputation = villager.getPlayerReputation(player);
        MobEffectInstance hero = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
        double heroDiscount = hero == null ? 0.0D : 0.3D + 0.0625D * hero.getAmplifier();

        MerchantOffers offers = villager.getOffers();
        for (MerchantOffer offer : offers) {
            if (reputation != 0) {
                offer.addToSpecialPriceDiff(-Math.max(0,
                    (int) Math.floor(reputation * offer.getPriceMultiplier())));
            }

            if (heroDiscount > 0.0D) {
                int discount = Math.max(1, (int) Math.floor(
                    heroDiscount * offer.getBaseCostA().getCount()));
                offer.addToSpecialPriceDiff(-discount);
            }
        }

        Player tradingPlayer = villager.getTradingPlayer();
        if (tradingPlayer != null && !offers.isEmpty()) {
            AbstractContainerMenu menu = tradingPlayer.containerMenu;
            if (menu instanceof MerchantMenu merchantMenu) {
                merchantMenu.updateSellItem();
            }
            tradingPlayer.sendMerchantOffers(menu.containerId, offers,
                villager.getVillagerData().level(), villager.getVillagerXp(),
                villager.showProgressBar(), villager.canRestock());
        }

        ci.cancel();
    }
}

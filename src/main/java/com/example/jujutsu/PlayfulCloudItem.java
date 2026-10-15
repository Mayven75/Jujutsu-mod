package com.example.jujutsu;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

/** Senjata Special Grade: 10% peluang Black Flash (damage besar + isi ulang energi kutukan). */
public class PlayfulCloudItem extends SwordItem {
    public PlayfulCloudItem() {
        super(Tiers.NETHERITE, new Item.Properties()
                .attributes(SwordItem.createAttributes(Tiers.NETHERITE, 4, -2.2f))
                .fireResistant());
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide && attacker.getRandom().nextFloat() < 0.10f) {
            target.hurt(attacker.damageSources().magic(), 12f);
            if (attacker.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + 1, target.getZ(), 40, 0.3, 0.5, 0.3, 0.4);
                sl.playSound(null, target.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.5f, 1.6f);
            }
            if (attacker instanceof Player p) {
                p.setData(JJKMod.CE, JJKMod.MAX_CE);
                p.displayClientMessage(Component.literal("§8§lBlack Flash!"), true);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}

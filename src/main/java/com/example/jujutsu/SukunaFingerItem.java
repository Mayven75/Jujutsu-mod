package com.example.jujutsu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Jari Sukuna: dimakan -> kekuatan besar sementara, tapi ada harganya. */
public class SukunaFingerItem extends Item {
    public SukunaFingerItem() {
        super(new Item.Properties().rarity(net.minecraft.world.item.Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand hand) {
        ItemStack stack = p.getItemInHand(hand);
        if (!level.isClientSide) {
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 2));
            p.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200, 1));
            p.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
            p.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 1));
            p.setData(JJKMod.CE, JJKMod.MAX_CE);
            p.displayClientMessage(Component.literal("§4Kekuatan Sukuna mengalir di tubuhmu..."), true);
            if (!p.getAbilities().instabuild) stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}

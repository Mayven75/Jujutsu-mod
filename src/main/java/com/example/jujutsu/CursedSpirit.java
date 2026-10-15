package com.example.jujutsu;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

/** Kutukan (Cursed Spirit): lebih kuat dari zombie dan tidak terbakar matahari. */
public class CursedSpirit extends Zombie {
    public CursedSpirit(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 7.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28);
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }
}

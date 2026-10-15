package com.example.jujutsu;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class TechniqueItem extends Item {
    public enum Type { BLUE, RED, PURPLE }

    private final Type type;
    private final int cost;
    private final int cooldown;

    public TechniqueItem(Type type, int cost, int cooldown) {
        super(new Item.Properties().stacksTo(1));
        this.type = type;
        this.cost = cost;
        this.cooldown = cooldown;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player p, InteractionHand hand) {
        ItemStack stack = p.getItemInHand(hand);
        int ce = p.getData(JJKMod.CE);
        if (ce < cost) {
            p.displayClientMessage(Component.literal("§cEnergi kutukan tidak cukup! (butuh " + cost + ")"), true);
            return InteractionResultHolder.fail(stack);
        }
        if (level instanceof ServerLevel sl) {
            p.setData(JJKMod.CE, ce - cost);
            Vec3 look = p.getLookAngle();
            Vec3 center = p.getEyePosition().add(look.scale(6));
            AABB box = new AABB(center, center).inflate(type == Type.PURPLE ? 8 : 5);
            var targets = sl.getEntitiesOfClass(LivingEntity.class, box, e -> e != p);

            switch (type) {
                case BLUE -> {
                    for (LivingEntity e : targets) {
                        e.setDeltaMovement(center.subtract(e.position()).scale(0.35));
                        e.hurtMarked = true;
                        e.hurt(sl.damageSources().playerAttack(p), 3f);
                    }
                    particles(sl, center, 0.15f, 0.35f, 1f, 80);
                }
                case RED -> {
                    for (LivingEntity e : targets) {
                        e.setDeltaMovement(look.scale(2.5).add(0, 0.5, 0));
                        e.hurtMarked = true;
                        e.hurt(sl.damageSources().playerAttack(p), 10f);
                    }
                    particles(sl, center, 1f, 0.1f, 0.1f, 80);
                }
                case PURPLE -> {
                    for (LivingEntity e : targets) e.hurt(sl.damageSources().playerAttack(p), 40f);
                    particles(sl, center, 0.6f, 0.1f, 1f, 200);
                    sl.explode(p, center.x, center.y, center.z, 5f, Level.ExplosionInteraction.TNT);
                }
            }
            sl.playSound(null, p.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 0.4f, 1.6f);
            p.getCooldowns().addCooldown(this, cooldown);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private static void particles(ServerLevel sl, Vec3 c, float r, float g, float b, int n) {
        sl.sendParticles(new DustParticleOptions(new Vector3f(r, g, b), 2f), c.x, c.y, c.z, n, 1.2, 1.2, 1.2, 0.05);
    }
                                           }

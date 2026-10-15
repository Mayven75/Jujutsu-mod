package com.example.jujutsu;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

@Mod(JJKMod.MODID)
public class JJKMod {
    public static final String MODID = "jujutsu";
    public static final int MAX_CE = 200;

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    /** Energi kutukan pemain (tersimpan & ikut saat mati). */
    public static final Supplier<AttachmentType<Integer>> CE = ATTACHMENTS.register("cursed_energy",
            () -> AttachmentType.builder(() -> MAX_CE).serialize(Codec.INT).copyOnDeath().build());

    public static final DeferredItem<Item> SUKUNA_FINGER = ITEMS.register("sukuna_finger", SukunaFingerItem::new);
    public static final DeferredItem<Item> PLAYFUL_CLOUD = ITEMS.register("playful_cloud", PlayfulCloudItem::new);
    public static final DeferredItem<Item> BLUE = ITEMS.register("blue_technique",
            () -> new TechniqueItem(TechniqueItem.Type.BLUE, 20, 40));
    public static final DeferredItem<Item> RED = ITEMS.register("red_technique",
            () -> new TechniqueItem(TechniqueItem.Type.RED, 40, 60));
    public static final DeferredItem<Item> PURPLE = ITEMS.register("hollow_purple",
            () -> new TechniqueItem(TechniqueItem.Type.PURPLE, 120, 400));

    public static final DeferredHolder<EntityType<?>, EntityType<CursedSpirit>> SPIRIT = ENTITIES.register("cursed_spirit",
            () -> EntityType.Builder.of(CursedSpirit::new, MobCategory.MONSTER).sized(0.6f, 1.95f).build("cursed_spirit"));

    public static final DeferredItem<Item> SPIRIT_EGG = ITEMS.register("cursed_spirit_spawn_egg",
            () -> new DeferredSpawnEggItem(SPIRIT, 0x2b0a3d, 0x7a1fa2, new Item.Properties()));

    public static final Supplier<CreativeModeTab> TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.jujutsu"))
            .icon(() -> SUKUNA_FINGER.get().getDefaultInstance())
            .displayItems((params, out) -> ITEMS.getEntries().forEach(e -> out.accept(e.get())))
            .build());

    public JJKMod(IEventBus modBus) {
        ITEMS.register(modBus);
        ENTITIES.register(modBus);
        ATTACHMENTS.register(modBus);
        TABS.register(modBus);
        modBus.addListener(this::attributes);
        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    private void attributes(EntityAttributeCreationEvent e) {
        e.put(SPIRIT.get(), CursedSpirit.createAttributes().build());
    }

    /** Regenerasi energi kutukan + tampilan di action bar saat memegang item JJK. */
    private void onPlayerTick(PlayerTickEvent.Post e) {
        if (!(e.getEntity() instanceof ServerPlayer p) || p.tickCount % 10 != 0) return;
        int ce = p.getData(CE);
        if (ce < MAX_CE) {
            ce = Math.min(MAX_CE, ce + 2);
            p.setData(CE, ce);
        }
        if (p.getMainHandItem().getItem() instanceof TechniqueItem) {
            p.displayClientMessage(Component.literal("§9Energi Kutukan: §b" + ce + "§7/" + MAX_CE), true);
        }
    }
                   }

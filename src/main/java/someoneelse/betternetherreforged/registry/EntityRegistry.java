package someoneelse.betternetherreforged.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.netty.util.AttributeMap;
import net.java.games.input.Component;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import someoneelse.betternetherreforged.BetterNether;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import someoneelse.betternetherreforged.config.Configs;
import someoneelse.betternetherreforged.entity.EntityChair;
import someoneelse.betternetherreforged.entity.EntityFlyingPig;
import someoneelse.betternetherreforged.entity.EntityHydrogenJellyfish;
import someoneelse.betternetherreforged.entity.EntityJungleSkeleton;
import someoneelse.betternetherreforged.entity.EntityNaga;
import someoneelse.betternetherreforged.entity.EntityNagaProjectile;
import someoneelse.betternetherreforged.entity.EntitySkull;

import someoneelse.betternetherreforged.entity.*;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {

	public static final Map<EntityType<? extends LivingEntity>, AttributeModifierMap> ATTRIBUTES = Maps.newHashMap();
	private static final List<EntityType<?>> NETHER_ENTITIES = Lists.newArrayList();

	public static final DeferredRegister<EntityType<?>> DEFERRED = DeferredRegister.create(ForgeRegistries.ENTITIES, BetterNether.MOD_ID);


	public static final EntityType<EntityChair> CHAIR = //registerEntity("chair", EntityTypeBuilder.create(EntityClassification.MISC, EntityChair::new).size(EntitySize.fixed(0.0F, 0.0F)).fireImmune().disableSummon().build(), EntityChair.getAttributeContainer());
	EntityType.Builder
		.<EntityChair>create(EntityChair::new, EntityClassification.MISC)
		.size(0, 0)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.disableSummoning()
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");


	public static final EntityType<EntityNagaProjectile> NAGA_PROJECTILE = //registerEntity("naga_projectile", EntityTypeBuilder.create(EntityClassification.MISC, EntityNagaProjectile::new).size(EntitySize.fixed(1F, 1F)).disableSummon().build());
	EntityType.Builder
		.<EntityNagaProjectile>create(EntityNagaProjectile::new, EntityClassification.MISC)
		.size(1f, 1f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.disableSummoning()
		.setShouldReceiveVelocityUpdates(true)
		.build("");



	public static final EntityType<EntityFirefly> FIREFLY = //registerEntity("firefly", EntityTypeBuilder.create(EntityClassification.AMBIENT, EntityFirefly::new).size(EntitySize.fixed(0.5F, 0.5F)).fireImmune().build(), EntityFirefly.getAttributeContainer());
	EntityType.Builder
		.<EntityFirefly>create(EntityFirefly::new, EntityClassification.AMBIENT)
		.size(0.5f, 0.5f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");


	public static final EntityType<EntityHydrogenJellyfish> HYDROGEN_JELLYFISH = //registerEntity("hydrogen_jellyfish", EntityTypeBuilder.create(EntityClassification.AMBIENT, EntityHydrogenJellyfish::new).size(EntitySize.flexible(2F, 5F)).fireImmune().build(), EntityHydrogenJellyfish.getAttributeContainer());
	EntityType.Builder
		.<EntityHydrogenJellyfish>create(EntityHydrogenJellyfish::new, EntityClassification.AMBIENT)
		.size(2f, 5f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");

	public static final EntityType<EntityNaga> NAGA = //registerEntity("naga", EntityTypeBuilder.create(EntityClassification.MONSTER, EntityNaga::new).size(EntitySize.fixed(0.625F, 2.75F)).fireImmune().build(), EntityNaga.getAttributeContainer());
	EntityType.Builder
		.<EntityNaga>create(EntityNaga::new, EntityClassification.MONSTER)
		.size(0.625f, 2.75f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");

	public static final EntityType<EntityFlyingPig> FLYING_PIG = //registerEntity("flying_pig", EntityTypeBuilder.create(EntityClassification.AMBIENT, EntityFlyingPig::new).size(EntitySize.fixed(1.0F, 1.25F)).fireImmune().build(), EntityFlyingPig.getAttributeContainer());
	EntityType.Builder
		.<EntityFlyingPig>create(EntityFlyingPig::new, EntityClassification.AMBIENT)
		.size(1.000f, 1.25f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");

	public static final EntityType<EntityJungleSkeleton> JUNGLE_SKELETON = //registerEntity("jungle_skeleton", EntityTypeBuilder.create(EntityClassification.MONSTER, EntityJungleSkeleton::new).size(EntitySize.fixed(0.6F, 1.99F)).fireImmune().build(), AbstractSkeletonEntity.getAttributeContainer());
	EntityType.Builder
		.<EntityJungleSkeleton>create(EntityJungleSkeleton::new, EntityClassification.MONSTER)
		.size(0.600f, 1.99f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");

	public static final EntityType<EntitySkull> SKULL = //registerEntity("skull", EntityTypeBuilder.create(EntityClassification.MONSTER, EntitySkull::new).size(EntitySize.fixed(0.625F, 0.625F)).fireImmune().build(), EntitySkull.getAttributeContainer());
	EntityType.Builder
		.<EntitySkull>create(EntitySkull::new, EntityClassification.MONSTER)
		.size(0.625f, 0.625f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");

	public static void registerAll(RegistryEvent.Register<EntityType<?>> evt) {
		IForgeRegistry<EntityType<?>> r = evt.getRegistry();
		r.register(CHAIR.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "chair")));
		if (Configs.MOBS.getBoolean("mobs", "naga", true)) {
		r.register(NAGA_PROJECTILE.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "naga_projectile")));
		}
		if (Configs.MOBS.getBoolean("mobs", "firefly", true)) {
		r.register(FIREFLY.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "firefly")));
		}
		if (Configs.MOBS.getBoolean("mobs", "naga", true)) {
			r.register(NAGA.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "naga")));
		}
		if (Configs.MOBS.getBoolean("mobs", "flying_pig", true)) {
			r.register(FLYING_PIG.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "flying_pig")));
		}
		if (Configs.MOBS.getBoolean("mobs", "jungle_skeleton", true)) {
		r.register(JUNGLE_SKELETON.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "jungle_skeleton")));
		}
		if (Configs.MOBS.getBoolean("mobs", "hydrogen_jellyfish", true)) {
			r.register(HYDROGEN_JELLYFISH.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "hydrogen_jellyfish")));
		}
		if (Configs.MOBS.getBoolean("mobs", "skull", true)) {
			r.register(SKULL.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "skull")));
		}

		registerEntityAttributes();
		registerSpawnPlacementData();
	}

	public static void registerSpawnPlacementData() {
		EntitySpawnPlacementRegistry.register(NAGA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityNaga::canSpawn);
		EntitySpawnPlacementRegistry.register(HYDROGEN_JELLYFISH, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityHydrogenJellyfish::canSpawn);
		EntitySpawnPlacementRegistry.register(JUNGLE_SKELETON, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityJungleSkeleton::canSpawn);



	}

	public static void registerEntityAttributes() {
		GlobalEntityTypeAttributes.put(CHAIR, EntityChair.getAttributeContainer());
		GlobalEntityTypeAttributes.put(NAGA_PROJECTILE, MobEntity.func_233666_p_().create());
		GlobalEntityTypeAttributes.put(FIREFLY, EntityFirefly.getAttributeContainer());
		GlobalEntityTypeAttributes.put(NAGA, EntityNaga.getAttributeContainer());
		GlobalEntityTypeAttributes.put(FLYING_PIG, EntityFlyingPig.getAttributeContainer());
		GlobalEntityTypeAttributes.put(JUNGLE_SKELETON, AbstractSkeletonEntity.registerAttributes().create());
		GlobalEntityTypeAttributes.put(HYDROGEN_JELLYFISH, EntityHydrogenJellyfish.getAttributeContainer());
		GlobalEntityTypeAttributes.put(SKULL, EntitySkull.getAttributeContainer());
	}

	public static void registerNetherEntities() {
		NETHER_ENTITIES.add(EntityType.GHAST);
		NETHER_ENTITIES.add(EntityType.ZOMBIFIED_PIGLIN);
		NETHER_ENTITIES.add(EntityType.PIGLIN);
		NETHER_ENTITIES.add(EntityType.HOGLIN);
		NETHER_ENTITIES.add(EntityType.BLAZE);
		NETHER_ENTITIES.add(EntityType.STRIDER);
	}



    public static void registerEntity(String name, EntityType<? extends LivingEntity> entity) {
		if (Configs.MOBS.getBoolean("mobs", name, true)) {
			Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BetterNether.MOD_ID, name), entity);
			ATTRIBUTES.put(entity, MobEntity.registerAttributes().create());
		}
    }

    public static void registerEntity(String name, EntityType<? extends LivingEntity> entity, AttributeModifierMap container) {
        if (Configs.MOBS.getBoolean("mobs", name, true)) {
            Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BetterNether.MOD_ID, name), entity);
            ATTRIBUTES.put(entity, container);
        }
    }

	public static boolean isNetherEntity(Entity entity) {
		return NETHER_ENTITIES.contains(entity.getType());
	}

    /*
    public static final RegistryObject<EntityType<EntityChair>> CHAIR = registerEntity("chair", () -> EntityType.Builder.create(EntityChair::new, EntityClassification.MISC)
            .size(0, 0)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("chair"));

    public static final RegistryObject<EntityType<EntityNagaProjectile>> NAGA_PROJECTILE = registerEntity("naga_projectile", () -> EntityType.Builder.create(EntityNagaProjectile::new, EntityClassification.MISC)
            .size(1, 1)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("naga_projectile"));

    public static final RegistryObject<EntityType<EntityFirefly>> FIREFLY = registerEntity("firefly", () -> EntityType.Builder.create(EntityFirefly::new, EntityClassification.AMBIENT)
            .size(1, 1)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("firefly"));

    public static final RegistryObject<EntityType<EntityHydrogenJellyfish>> HYDROGEN_JELLYFISH = registerEntity("hydrogen_jellyfish", () -> EntityType.Builder.create(EntityHydrogenJellyfish::new, EntityClassification.AMBIENT)
            .size(2, 5)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("hydrogen_jellyfish"));

    public static final RegistryObject<EntityType<EntityNaga>> NAGA = registerEntity("naga", () -> EntityType.Builder.create(EntityNaga::new, EntityClassification.MONSTER)
            .size(0.625f, 2.75f)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("naga"));

    public static final RegistryObject<EntityType<EntityFlyingPig>> FLYING_PIG = registerEntity("flying_pig", () -> EntityType.Builder.create(EntityFlyingPig::new, EntityClassification.AMBIENT)
            .size(1.000f, 1.25f)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("flying_pig"));

    public static final RegistryObject<EntityType<EntityJungleSkeleton>> JUNGLE_SKELETON = registerEntity("jungle_skeleton", () -> EntityType.Builder.create(EntityJungleSkeleton::new, EntityClassification.MONSTER)
            .size(0.6F, 1.99F)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("jungle_skeleton"));

    public static final RegistryObject<EntityType<EntitySkull>> SKULL = registerEntity("skull", () -> EntityType.Builder.create(EntitySkull::new, EntityClassification.MONSTER)
            .size(0.625f, 0.625f)
            .setTrackingRange(5)
            .setShouldReceiveVelocityUpdates(true)
            .build("skull"));

    public static final EntityType<EntitySkull> SKULL = //registerEntity("skull", EntityTypeBuilder.create(EntityClassification.MONSTER, EntitySkull::new).size(EntitySize.fixed(0.625F, 0.625F)).fireImmune().build(), EntitySkull.getAttributeContainer());
            EntityType.Builder
                    .<EntitySkull>create(EntitySkull::new, EntityClassification.MONSTER)
                    .size(0.625f, 0.625f)
                    .setUpdateInterval(3)
                    .setTrackingRange(5)
                    .immuneToFire()
                    .setShouldReceiveVelocityUpdates(true)
                    .build("");

    private static <E extends EntityType<?>> RegistryObject<E> registerEntity(String name, Supplier<? extends E> supplier) {
        RegistryObject<E> entity = ENTITIES.register(name, supplier);
        //BNItems.ITEMS.register(name, () -> new BNSpawnEggItem((Supplier<EntityType<?>>) supplier, 0, 0, new Item.Properties().group(BNItemGroup.ITEM_GROUP)));
        return entity;
    }

     */

}


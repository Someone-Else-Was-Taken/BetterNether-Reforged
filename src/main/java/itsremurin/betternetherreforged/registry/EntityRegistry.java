package itsremurin.betternetherreforged.registry;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import itsremurin.betternetherreforged.BetterNether;
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
import itsremurin.betternetherreforged.entity.EntityChair;
import itsremurin.betternetherreforged.entity.EntityFlyingPig;
import itsremurin.betternetherreforged.entity.EntityHydrogenJellyfish;
import itsremurin.betternetherreforged.entity.EntityJungleSkeleton;
import itsremurin.betternetherreforged.entity.EntityNaga;
import itsremurin.betternetherreforged.entity.EntityNagaProjectile;
import itsremurin.betternetherreforged.entity.EntitySkull;

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
	
	/*
	 * Firefly's renderer doesn't register properly
	 * 
	public static final EntityType<EntityFirefly> FIREFLY = //registerEntity("firefly", EntityTypeBuilder.create(EntityClassification.AMBIENT, EntityFirefly::new).size(EntitySize.fixed(0.5F, 0.5F)).fireImmune().build(), EntityFirefly.getAttributeContainer());
	EntityType.Builder
		.<EntityFirefly>create(EntityFirefly::new, EntityClassification.AMBIENT)
		.size(0.5f, 0.5f)
		.setUpdateInterval(3)
		.setTrackingRange(5)
		.immuneToFire()
		.setShouldReceiveVelocityUpdates(true)
		.build("");
		*/
	
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
		r.register(NAGA_PROJECTILE.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "naga_projectile")));
		//r.register(FIREFLY.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "firefly")));
		r.register(NAGA.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "naga")));
		r.register(FLYING_PIG.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "flying_pig")));
		r.register(JUNGLE_SKELETON.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "jungle_skeleton")));
		r.register(HYDROGEN_JELLYFISH.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "hydrogen_jellyfish")));
		r.register(SKULL.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, "skull")));
		
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
		//GlobalEntityTypeAttributes.put(FIREFLY, EntityFirefly.getAttributeContainer());
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

	public static void registerExtraNetherEntities()
	{
		NETHER_ENTITIES.add(NAGA);
		NETHER_ENTITIES.add(FLYING_PIG);
		NETHER_ENTITIES.add(HYDROGEN_JELLYFISH);
		NETHER_ENTITIES.add(SKULL);
	}

	public static <T extends LivingEntity> EntityType<T> registerEntity(String name, EntityType<T> entity) {
		DEFERRED.register(name, () -> entity);
		ATTRIBUTES.put(entity, MobEntity.func_233666_p_().create());
		return entity;
	}

	public static <T extends LivingEntity> EntityType<T> registerEntity(String name, EntityType<T> entity, AttributeModifierMap container) {
		DEFERRED.register(name, () -> entity);
		ATTRIBUTES.put(entity, container);
		return entity;
	}

	public static boolean isNetherEntity(Entity entity) {
		return NETHER_ENTITIES.contains(entity.getType());
	}
}


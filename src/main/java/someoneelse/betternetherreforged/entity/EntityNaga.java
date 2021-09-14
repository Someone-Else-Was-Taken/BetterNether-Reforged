package someoneelse.betternetherreforged.entity;

import java.util.Random;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.EntityRegistry;
import someoneelse.betternetherreforged.registry.SoundsRegistry;

public class EntityNaga extends MonsterEntity implements IRangedAttackMob, IMob {
	public EntityNaga(EntityType<? extends EntityNaga> type, World world) {
		super(type, world);
		this.experienceValue = 10;
	}

	@Override
	protected void registerGoals() {
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
		this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.0D, 40, 20.0F));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
	}

	public static AttributeModifierMap getAttributeContainer() {
		return MobEntity
				.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.23)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0)
				.createMutableAttribute(Attributes.ARMOR, 2.0)
				.create();
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float f) {
		EntityNagaProjectile projectile = EntityRegistry.NAGA_PROJECTILE.create(world);
		projectile.setPositionAndRotation(getPosX(), getPosYEye(), getPosZ(), 0, 0);
		projectile.setParams(this, target);
		world.addEntity(projectile);
		this.playSound(SoundsRegistry.MOB_NAGA_ATTACK, MHelper.randRange(3F, 5F, rand), MHelper.randRange(0.75F, 1.25F, rand));
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
	}

	@Override
	public boolean isPotionApplicable(EffectInstance effect) {
		return effect.getPotion() == Effects.WITHER ? false : super.isPotionApplicable(effect);
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundsRegistry.MOB_NAGA_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}

	@Override
	protected boolean isDespawnPeaceful() {
		return true;
	}

	@Override
	public int getHorizontalFaceSpeed() {
		return 1;
	}

	public static boolean canSpawn(EntityType<? extends EntityNaga> type, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.getDifficulty() != Difficulty.PEACEFUL && world.getLight(pos) < 8;
	}
}

package someoneelse.betternetherreforged.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityNagaProjectile extends FlyingEntity {
	private static final int MAX_LIFE_TIME = 60; // 3 seconds * 20 ticks
	private int lifeTime = 0;

	public EntityNagaProjectile(EntityType<? extends EntityNagaProjectile> type, World world) {
		super(type, world);
		this.experienceValue = 0;
	}

	public void setParams(LivingEntity owner, Entity target) {
		this.setPosition(getPosX(), getPosYEye() - this.getHeight(), getPosZ());
		Vector3d dir = target.getPositionVec().add(0, target.getHeight() * 0.25, 0).subtract(getPositionVec()).normalize().scale(2);
		this.setMotion(dir);
		this.prevPosX = getPosX() - dir.x;
		this.prevPosY = getPosY() - dir.y;
		this.prevPosZ = getPosZ() - dir.z;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 128;
	}

	@Override
	public void tick() {
		super.tick();
		world.addParticle(ParticleTypes.LARGE_SMOKE,
				getPosX() + rand.nextGaussian() * 0.2,
				getPosY() + rand.nextGaussian() * 0.2,
				getPosZ() + rand.nextGaussian() * 0.2,
				0, 0, 0);
		world.addParticle(ParticleTypes.SMOKE,
				getPosX() + rand.nextGaussian() * 0.2,
				getPosY() + rand.nextGaussian() * 0.2,
				getPosZ() + rand.nextGaussian() * 0.2,
				0, 0, 0);

		RayTraceResult hitResult = ProjectileHelper.func_234618_a_(this, (entity) -> {
			return entity.isAlive() && entity instanceof LivingEntity;
		});
		if (hitResult.getType() != RayTraceResult.Type.MISS) {
			this.onCollision(hitResult);
		}

		lifeTime++;
		if (lifeTime > MAX_LIFE_TIME)
			effectKill();

		if (isSame(this.prevPosX, this.getPosX()) && isSame(this.prevPosY, this.getPosY()) && isSame(this.prevPosZ, this.getPosZ()))
			effectKill();
	}

	private boolean isSame(double a, double b) {
		return Math.abs(a - b) < 0.1;
	}

	protected void onCollision(RayTraceResult hitResult) {
		RayTraceResult.Type type = hitResult.getType();
		if (type == RayTraceResult.Type.BLOCK) {
			for (int i = 0; i < 10; i++) {
				world.addParticle(ParticleTypes.LARGE_SMOKE,
						getPosX() + rand.nextGaussian() * 0.5,
						getPosY() + rand.nextGaussian() * 0.5,
						getPosZ() + rand.nextGaussian() * 0.5,
						rand.nextGaussian() * 0.2,
						rand.nextGaussian() * 0.2,
						rand.nextGaussian() * 0.2);
			}
			effectKill();
		}
		else if (type == RayTraceResult.Type.ENTITY) {
			Entity entity = ((EntityRayTraceResult) hitResult).getEntity();
			if (entity != this && entity instanceof LivingEntity && !(entity instanceof EntityNaga)) {
				LivingEntity living = (LivingEntity) entity;
				if (!(living.isPotionActive(Effects.WITHER))) {
					living.addPotionEffect(new EffectInstance(Effects.WITHER, 40));
					// living.damage(DamageSource.GENERIC, 0.5F);
				}
				effectKill();
			}
		}
	}

	private void effectKill() {
		for (int i = 0; i < 10; i++) {
			world.addParticle(ParticleTypes.ENTITY_EFFECT,
					getPosX() + rand.nextGaussian() * 0.5,
					getPosY() + rand.nextGaussian() * 0.5,
					getPosZ() + rand.nextGaussian() * 0.5,
					0.1, 0.1, 0.1);
		}
		this.onKillCommand();
	}

	@Override
	public boolean isPotionApplicable(EffectInstance effect) {
		return false;
	}

	@Override
	public boolean isSilent() {
		return true;
	}

	@Override
	public void writeAdditional(CompoundNBT tag) {
		super.writeAdditional(tag);
		tag.putInt("life", lifeTime);
	}

	@Override
	public void readAdditional(CompoundNBT tag) {
		super.readAdditional(tag);
		if (tag.contains("life")) {
			lifeTime = tag.getInt("life");
		}
	}
}
package someoneelse.betternetherreforged.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import someoneelse.betternetherreforged.registry.SoundsRegistry;

public class EntityHydrogenJellyfish extends AnimalEntity implements IFlyingAnimal {
	private static final DataParameter<Float> SCALE = EntityDataManager.createKey(EntityHydrogenJellyfish.class, DataSerializers.FLOAT);

	private Vector3d preVelocity;
	private Vector3d newVelocity = new Vector3d(0, 0, 0);
	private int timer;
	private int timeOut;
	private float prewYaw;
	private float nextYaw;

	public EntityHydrogenJellyfish(EntityType<? extends EntityHydrogenJellyfish> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(SCALE, 0.5F + rand.nextFloat());
	}

	public static AttributeModifierMap getAttributeContainer() {
		return MobEntity
				.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 0.5)
				.createMutableAttribute(Attributes.FLYING_SPEED, 0.05)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 20.0)
				.create();
	}

	@Override
	protected boolean makeFlySound() {
		return true;
	}

	@Override
	protected void handleFluidJump(ITag<Fluid> fluid) {
		this.setMotion(this.getMotion().add(0.0D, 0.01D, 0.0D));
	}

	@Override
	public boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public void writeAdditional(CompoundNBT tag) {
		super.writeAdditional(tag);

		tag.putFloat("Scale", getScale());
	}

	@Override
	public void readAdditional(CompoundNBT tag) {
		super.readAdditional(tag);

		if (tag.contains("Scale")) {
			this.dataManager.set(SCALE, tag.getFloat("Scale"));
		}

		this.recalculateSize();
	}

	public float getScale() {
		return this.dataManager.get(SCALE);
	}

	public EntitySize getDimensions(Pose pose) {
		return super.getSize(pose).scale(this.getScale());
	}

	@Override
	public void onCollideWithPlayer(PlayerEntity player) {
		player.attackEntityFrom(DamageSource.GENERIC, 3);
	}

	@Override
	public void recalculateSize() {
		double x = this.getPosX();
		double y = this.getPosY();
		double z = this.getPosZ();
		super.recalculateSize();
		this.setPosition(x, y, z);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> data) {
		if (SCALE.equals(data)) {
			this.recalculateSize();
		}
	}

	@Override
	protected void updateAITasks() {
		timer++;
		if (timer > timeOut) {
			prewYaw = this.rotationYaw;
			nextYaw = rand.nextFloat() * 360;

			double rads = Math.toRadians(nextYaw + 90);

			double vx = Math.cos(rads) * this.jumpMovementFactor;
			double vz = Math.sin(rads) * this.jumpMovementFactor;

			BlockPos bp = getPosition();
			double vy = rand.nextDouble() * this.jumpMovementFactor * 0.75;
			if (world.getBlockState(bp).getBlock().isAir(world.getBlockState(bp), world, bp) &&
					world.getBlockState(bp.down(2)).getBlock().isAir(world.getBlockState(bp.down(2)), world, bp.down(2)) &&
					world.getBlockState(bp.down(3)).getBlock().isAir(world.getBlockState(bp.down(3)), world, bp.down(3)) &&
					world.getBlockState(bp.down(4)).getBlock().isAir(world.getBlockState(bp.down(4)), world, bp.down(4))) {
				vy = -vy;
			}

			preVelocity = newVelocity;
			newVelocity = new Vector3d(vx, vy, vz);
			timer = 0;
			timeOut = rand.nextInt(300) + 120;
		}
		if (timer <= 120) {
			if (this.rotationYaw != nextYaw) {
				float delta = timer / 120F;
				this.rotationYaw = lerpAngleDegrees(delta, prewYaw, nextYaw);
				this.setVelocity(
						MathHelper.lerp(delta, preVelocity.x, newVelocity.x),
						MathHelper.lerp(delta, preVelocity.y, newVelocity.y),
						MathHelper.lerp(delta, preVelocity.z, newVelocity.z));
			}
		}
		else {
			this.setMotion(newVelocity);
		}
	}

	public static float lerpAngleDegrees(float delta, float first, float second) {
		return first + delta * MathHelper.wrapDegrees(second - first);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 1;
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (world.isRemote) {
			float scale = getScale() * 3;
			for (int i = 0; i < 20; i++)
				this.world.addParticle(ParticleTypes.EXPLOSION,
						getPosX() + rand.nextGaussian() * scale,
						getEyeHeight() + rand.nextGaussian() * scale,
						getPosZ() + rand.nextGaussian() * scale,
						0, 0, 0);
		}
		else {
			Explosion.Mode destructionType = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
			this.world.createExplosion(this, getPosX(), getEyeHeight(), getPosZ(), 7 * getScale(), destructionType);
		}
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundsRegistry.MOB_JELLYFISH;
	}

	@Override
	protected float getSoundVolume() {
		return 0.1F;
	}

	@Override
	public AgeableEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
		return null;
	}

	@Override
	public boolean onLivingFall(float fallDistance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void updateFallState(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source == DamageSource.WITHER || source instanceof EntityDamageSource) {
			return super.attackEntityFrom(source, amount);
		}
		return false;
	}

	public static boolean canSpawn(EntityType<? extends EntityHydrogenJellyfish> type, IWorld world, SpawnReason spawnReason, BlockPos pos, Random rand) {
		AxisAlignedBB box = new AxisAlignedBB(pos).expand(64, 256, 64);
		List<EntityHydrogenJellyfish> list = world.getEntitiesWithinAABB(EntityHydrogenJellyfish.class, box, (entity) -> {
			return true;
		});
		return list.size() < 4;
	}
}
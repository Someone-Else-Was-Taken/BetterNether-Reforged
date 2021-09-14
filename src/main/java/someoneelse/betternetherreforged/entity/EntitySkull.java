package someoneelse.betternetherreforged.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.SoundsRegistry;


public class EntitySkull extends MonsterEntity implements IFlyingAnimal {
	private static double particleX;
	private static double particleY;
	private static double particleZ;
	private int attackTick;
	private int dirTickTick;
	private int collideTick;

	public EntitySkull(EntityType<? extends EntitySkull> type, World world) {
		super(type, world);
		this.moveController = new FlyingMovementController(this, 20, true);
		this.lookController = new SkullLookControl(this);
		this.setPathPriority(PathNodeType.LAVA, -1.0F);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.experienceValue = 1;
		this.jumpMovementFactor = 0.5F;
	}

	public static AttributeModifierMap getAttributeContainer() {
		return MobEntity
				.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 4.0)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 20.0)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5)
				.createMutableAttribute(Attributes.FLYING_SPEED, 0.5)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 1.0)
				.create();
	}

	class SkullLookControl extends LookController {
		SkullLookControl(MobEntity entity) {
			super(entity);
		}

		protected boolean shouldStayHorizontal() {
			return false;
		}
	}

	@Override
	public void onCollideWithPlayer(PlayerEntity player) {
		collideTick++;
		if (collideTick > 3) {
			collideTick = 0;

			boolean shield = player.getActiveItemStack().getItem() instanceof ShieldItem && player.isHandActive();
			if (shield) {
				player.playSound(SoundEvents.ITEM_SHIELD_BLOCK, MHelper.randRange(0.8F, 1.2F, rand), MHelper.randRange(0.8F, 1.2F, rand));
				this.setMotion(new Vector3d(0, 0, 0).subtract(getMotion()));
			}
			if (player instanceof ServerPlayerEntity) {
				if (shield) {
					player.getActiveItemStack().attemptDamageItem(1, rand, (ServerPlayerEntity) player);
					if (player.getActiveItemStack().getDamage() > player.getActiveItemStack().getMaxDamage()) {
						player.sendBreakAnimation(player.getActiveHand());
						if (player.getActiveHand().equals(Hand.MAIN_HAND))
							player.inventory.mainInventory.clear();
						else if (player.getActiveHand().equals(Hand.OFF_HAND))
							player.inventory.offHandInventory.clear();
						player.resetActiveHand();
					}
					return;
				}
				player.attackEntityFrom(DamageSource.GENERIC, 1);
				if (rand.nextInt(16) == 0)
					player.setFire(3);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (rand.nextInt(3) == 0) {
			updateParticlePos();
			this.world.addParticle(ParticleTypes.SMOKE, particleX, particleY, particleZ, 0, 0, 0);
		}
		if (rand.nextInt(3) == 0) {
			updateParticlePos();
			this.world.addParticle(ParticleTypes.DRIPPING_LAVA, particleX, particleY, particleZ, 0, 0, 0);
		}
		if (rand.nextInt(3) == 0) {
			updateParticlePos();
			this.world.addParticle(ParticleTypes.FLAME, particleX, particleY, particleZ, 0, 0, 0);
		}

		if (attackTick > 40 && this.isAlive()) {
			PlayerEntity target = EntitySkull.this.world.getClosestPlayer(getPosX(), getPosY(), getPosZ(), 20, true);
			if (target != null && this.canEntityBeSeen(target)) {
				attackTick = 0;
				Vector3d velocity = target
						.getPositionVec()
						.add(0, target.getHeight() * 0.5F, 0)
						.subtract(EntitySkull.this.getPositionVec())
						.normalize()
						.scale(EntitySkull.this.jumpMovementFactor);
				setMotion(velocity);
				this.faceEntity(target, 360, 360);
				this.playSound(SoundsRegistry.MOB_SKULL_FLIGHT, MHelper.randRange(0.15F, 0.3F, rand), MHelper.randRange(0.9F, 1.5F, rand));
			}
			else if (dirTickTick < 0) {
				dirTickTick = MHelper.randRange(20, 60, rand);
				moveRandomDir();
			}
		}
		else {
			if (dirTickTick < 0) {
				dirTickTick = MHelper.randRange(20, 60, rand);
				moveRandomDir();
			}
		}
		attackTick++;
		dirTickTick--;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SKELETON_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_SKELETON_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SKELETON_DEATH;
	}

	private void moveRandomDir() {
		double dx = rand.nextDouble() - 0.5;
		double dy = rand.nextDouble() - 0.5;
		double dz = rand.nextDouble() - 0.5;
		double l = dx * dx + dy * dy + dz * dz;
		if (l == 0)
			l = 1;
		else
			l = (float) Math.sqrt(l);
		l /= this.jumpMovementFactor;
		dx /= l;
		dy /= l;
		dz /= l;
		setVelocity(dx, dy, dz);
		lookAt(this.getPositionVec().add(this.getMotion()));
		this.playSound(SoundsRegistry.MOB_SKULL_FLIGHT, MHelper.randRange(0.15F, 0.3F, rand), MHelper.randRange(0.75F, 1.25F, rand));
	}

	private void lookAt(Vector3d target) {
		double d = target.getX() - this.getPosX();
		double e = target.getZ() - this.getPosZ();
		double g = target.getY() - this.getPosY();

		double h = MathHelper.sqrt(d * d + e * e);
		float i = (float) (MathHelper.atan2(e, d) * 57.2957763671875D) - 90.0F;
		float j = (float) (-(MathHelper.atan2(g, h) * 57.2957763671875D));

		this.rotationPitch = j;
		this.rotationYaw = i;
	}

	private void updateParticlePos() {
		particleX = rand.nextDouble() - 0.5;
		particleY = rand.nextDouble() - 0.5;
		particleZ = rand.nextDouble() - 0.5;
		double l = particleX * particleX + particleY * particleY + particleZ * particleZ;
		if (l == 0)
			l = 1;
		else
			l = (float) Math.sqrt(l);
		particleX = particleX * 0.5 / l + getPosX();
		particleY = particleY * 0.5 / l + getPosY();
		particleZ = particleZ * 0.5 / l + getPosZ();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getEyeHeight(Pose pose) {
		return this.getSize(pose).height * 0.5F;
	}

	@Override
	protected boolean makeFlySound() {
		return true;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
	}

	@Override
	public boolean onLivingFall(float fallDistance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void updateFallState(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {}

	@Override
	public boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	public static boolean canSpawn(EntityType<? extends EntitySkull> type, IWorld world, SpawnReason spawnReason, BlockPos pos, Random rand) {
		if (world.getDifficulty() == Difficulty.PEACEFUL || world.getLight(pos) > 7)
			return false;

		if (pos.getY() >= world.getDimensionType().getLogicalHeight()) return false;

		AxisAlignedBB box = new AxisAlignedBB(pos).expand(256, 256, 256);
		List<EntitySkull> list = world.getEntitiesWithinAABB(EntitySkull.class, box, (entity) -> {
			return true;
		});
		return list.size() < 4;
	}
}
package someoneelse.betternetherreforged.entity;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.EntityRegistry;

public class EntityFlyingPig extends AnimalEntity implements IFlyingAnimal {
	private static final DataParameter<Byte> FLAGS;
	private static final int BIT_ROOSTING = 0;
	private static final int BIT_WARTED = 1;
	private Goal preGoal;

	public EntityFlyingPig(EntityType<? extends EntityFlyingPig> type, World world) {
		super(type, world);
		this.moveController = new FlyingMovementController(this, 20, true);
		this.setPathPriority(PathNodeType.LAVA, 0.0F);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
		this.experienceValue = 2;
		this.jumpMovementFactor = 0.3F;
	}

	@Override
	protected void registerGoals() {
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));
		this.goalSelector.addGoal(2, new FindFoodGoal());
		this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new SittingGoal());
		this.goalSelector.addGoal(5, new RoostingGoal());
		this.goalSelector.addGoal(6, new WanderAroundGoal());
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
	}

	public static AttributeModifierMap getAttributeContainer() {
		return MobEntity
				.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 6.0)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 12.0)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3)
				.createMutableAttribute(Attributes.FLYING_SPEED, 0.3)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0)
				.createMutableAttribute(Attributes.ARMOR, 1.0)
				.create();
	}

	@Override
	protected PathNavigator createNavigator(World world) {
		FlyingPathNavigator birdnavigator = new FlyingPathNavigator(this, world) {
			public boolean canEntityStandOnPos(BlockPos pos) {
				return this.world.isAirBlock(pos);
			}
		};
		birdnavigator.setCanOpenDoors(false);
		birdnavigator.setCanSwim(true);
		birdnavigator.setCanEnterDoors(true);
		return birdnavigator;
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(FLAGS, MHelper.setBit((byte) 0, BIT_WARTED, rand.nextInt(4) == 0));
	}

	@Override
	public void writeAdditional(CompoundNBT tag) {
		super.writeAdditional(tag);

		tag.putByte("byteData", this.dataManager.get(FLAGS));
	}

	@Override
	public void readAdditional(CompoundNBT tag) {
		super.readAdditional(tag);

		if (tag.contains("byteData")) {
			this.dataManager.set(FLAGS, tag.getByte("byteData"));
		}
	}

	public boolean isRoosting() {
		byte b = this.dataManager.get(FLAGS);
		return MHelper.getBit(b, BIT_ROOSTING);
	}

	public void setRoosting(boolean roosting) {
		byte b = this.dataManager.get(FLAGS);
		this.dataManager.set(FLAGS, MHelper.setBit(b, BIT_ROOSTING, roosting));
	}

	public boolean isWarted() {
		byte b = this.dataManager.get(FLAGS);
		return MHelper.getBit(b, BIT_WARTED);
	}

	public void setWarted(boolean warted) {
		byte b = this.dataManager.get(FLAGS);
		this.dataManager.set(FLAGS, MHelper.setBit(b, BIT_WARTED, warted));
	}

	@Override
	protected float getSoundVolume() {
		return MHelper.randRange(0.85F, 1.15F, rand);
	}

	@Override
	protected float getSoundPitch() {
		return MHelper.randRange(0.3F, 0.4F, rand);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_PIG_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_PIG_DEATH;
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_PIG_AMBIENT;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entity) {}

	@Override
	protected void collideWithNearbyEntities() {}

	@Override
	protected boolean makeFlySound() {
		return true;
	}

	@Override
	public boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean onLivingFall(float fallDistance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void updateFallState(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {}

	@Override
	protected void onDeathUpdate() {
		if (!world.isRemote && this.isWarted() && world.getServer().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
			this.entityDropItem(new ItemStack(Items.NETHER_WART, MHelper.randRange(1, 3, rand)));
		}
		super.onDeathUpdate();

	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 5;
	}

	static {
		FLAGS = EntityDataManager.createKey(EntityFlyingPig.class, DataSerializers.BYTE);
	}

	class WanderAroundGoal extends Goal {
		WanderAroundGoal() {
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		public boolean shouldExecute() {
			return EntityFlyingPig.this.navigator.noPath() && !EntityFlyingPig.this.isRoosting();
		}

		public boolean shouldContinueExecuting() {
			return EntityFlyingPig.this.navigator.hasPath() && EntityFlyingPig.this.rand.nextInt(32) > 0;
		}

		public void startExecuting() {
			if (EntityFlyingPig.this.world.getFluidState(EntityFlyingPig.this.getPosition()).isEmpty()) {
				BlockPos pos = this.getRandomLocation();
				Path path = EntityFlyingPig.this.navigator.getPathToPos(pos, 1);
				if (path != null)
					EntityFlyingPig.this.navigator.setPath(path, EntityFlyingPig.this.jumpMovementFactor);
				else
					EntityFlyingPig.this.setMotion(0, -0.2, 0);
				EntityFlyingPig.this.setRoosting(false);
			}
			else
				EntityFlyingPig.this.setMotion(0, 1, 0);
			super.startExecuting();
		}

		private BlockPos getRandomLocation() {
			Mutable bpos = new Mutable();
			bpos.setPos(EntityFlyingPig.this.getPosX(), EntityFlyingPig.this.getPosY(), EntityFlyingPig.this.getPosZ());

			Vector3d angle = EntityFlyingPig.this.getLook(0.0F);
			Vector3d airTarget = RandomPositionGenerator.findAirTarget(EntityFlyingPig.this, 8, 7, angle, 1.5707964F, 2, 1);

			if (airTarget == null) {
				airTarget = RandomPositionGenerator.findAirTarget(EntityFlyingPig.this, 32, 10, angle, 1.5707964F, 3, 1);
			}

			if (airTarget == null) {
				bpos.setX(bpos.getX() + randomRange(32));
				bpos.setZ(bpos.getZ() + randomRange(32));
				bpos.setY(bpos.getY() + randomRange(32));
				return bpos;
			}

			bpos.setPos(airTarget.getX(), airTarget.getY(), airTarget.getZ());
			BlockPos down = bpos.down();
			if (EntityFlyingPig.this.world.getBlockState(down).hasOpaqueCollisionShape(EntityFlyingPig.this.world, down))
				bpos.move(Direction.UP);

			while (!EntityFlyingPig.this.world.getFluidState(bpos).isEmpty())
				bpos.move(Direction.UP);

			return bpos;
		}

		private int randomRange(int side) {
			Random rand = EntityFlyingPig.this.rand;
			return rand.nextInt(side + 1) - (side >> 1);
		}

		@Override
		public void resetTask() {
			EntityFlyingPig.this.preGoal = this;
			super.resetTask();
		}
	}

	class RoostingGoal extends Goal {
		BlockPos roosting;

		@Override
		public boolean shouldExecute() {
			return !(EntityFlyingPig.this.preGoal instanceof SittingGoal) &&
					EntityFlyingPig.this.navigator.noPath() &&
					!EntityFlyingPig.this.isRoosting() &&
					EntityFlyingPig.this.rand.nextInt(4) == 0;
		}

		@Override
		public boolean shouldContinueExecuting() {
			return EntityFlyingPig.this.navigator.hasPath();
		}

		@Override
		public void startExecuting() {
			BlockPos pos = this.getRoostingLocation();
			if (pos != null) {
				Path path = EntityFlyingPig.this.navigator.getPathToPos(pos, 1);
				if (path != null) {
					EntityFlyingPig.this.navigator.setPath(path, EntityFlyingPig.this.jumpMovementFactor);
					this.roosting = pos;
				}
			}
			super.startExecuting();
		}

		@Override
		public void resetTask() {
			if (this.roosting != null) {
				EntityFlyingPig.this.setPosition(roosting.getX() + 0.5, roosting.getY() - 0.25, roosting.getZ() + 0.5);
				EntityFlyingPig.this.setRoosting(true);
				EntityFlyingPig.this.preGoal = this;
			}
			super.resetTask();
		}

		private BlockPos getRoostingLocation() {
			BlockPos pos = EntityFlyingPig.this.getPosition();
			World world = EntityFlyingPig.this.world;
			int up = BlocksHelper.upRay(world, pos, 16);
			pos = pos.offset(Direction.UP, up);
			if (world.getBlockState(pos.up()).getBlock() == Blocks.NETHER_WART_BLOCK)
				return pos;
			else
				return null;
		}
	}

	class SittingGoal extends Goal {
		int timer;
		int ammount;

		@Override
		public boolean shouldExecute() {
			return EntityFlyingPig.this.isRoosting();
		}

		@Override
		public boolean shouldContinueExecuting() {
			return timer < ammount;
		}

		@Override
		public void startExecuting() {
			timer = 0;
			ammount = MHelper.randRange(80, 160, EntityFlyingPig.this.rand);
			EntityFlyingPig.this.setMotion(0, 0, 0);
			EntityFlyingPig.this.setRenderYawOffset(EntityFlyingPig.this.rand.nextFloat() * MHelper.PI2);
			super.startExecuting();
		}

		@Override
		public void resetTask() {
			EntityFlyingPig.this.setRoosting(false);
			EntityFlyingPig.this.setMotion(0, -0.1F, 0);
			EntityFlyingPig.this.preGoal = this;
			super.resetTask();
		}

		@Override
		public void tick() {
			timer++;
			super.tick();
		}
	}

	class FindFoodGoal extends Goal {
		private List<ItemEntity> foods;
		private ItemEntity target;

		@Override
		public boolean shouldExecute() {
			return hasNearFood();
		}

		@Override
		public void startExecuting() {
			BlockPos pos = getFood();
			Path path = EntityFlyingPig.this.navigator.getPathToPos(pos, 1);
			if (path != null) {
				EntityFlyingPig.this.navigator.setPath(path, EntityFlyingPig.this.jumpMovementFactor);
				EntityFlyingPig.this.setRoosting(false);
			}
			super.startExecuting();
		}

		@Override
		public boolean shouldContinueExecuting() {
			return target.isAlive() && EntityFlyingPig.this.navigator.hasPath();
		}

		@Override
		public void resetTask() {
			if (target.isAlive() && target.getDistance(EntityFlyingPig.this) < 1.3) {
				ItemStack stack = ((ItemEntity) target).getItem();

				ItemParticleData effect = new ItemParticleData(ParticleTypes.ITEM, new ItemStack(stack.getItem()));

				Iterator<?> var14 = world.getPlayers().iterator();

				while (var14.hasNext()) {
					ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) var14.next();
					if (serverPlayerEntity.getDistanceSq(target.getPosX(), target.getPosY(), target.getPosZ()) < 4096.0D) {
						serverPlayerEntity.connection.sendPacket(new SSpawnParticlePacket(effect, false,
								target.getPosX(),
								target.getPosY() + 0.2,
								target.getPosZ(),
								0.2F, 0.2F, 0.2F, 0, 16));
					}
				}

				EntityFlyingPig.this.onFoodEaten(world, stack);
				target.onKillCommand();
				EntityFlyingPig.this.heal(stack.getCount());
				EntityFlyingPig.this.setVelocity(0, 0.2F, 0);
			}
			EntityFlyingPig.this.preGoal = this;
			super.resetTask();
		}

		private BlockPos getFood() {
			target = foods.get(EntityFlyingPig.this.rand.nextInt(foods.size()));
			return target.getPosition();
		}

		private boolean hasNearFood() {
			AxisAlignedBB AxisAlignedBB = new AxisAlignedBB(EntityFlyingPig.this.getPosition()).grow(16);
			foods = EntityFlyingPig.this.world.getEntitiesWithinAABB(ItemEntity.class, AxisAlignedBB, (entity) -> {
				return ((ItemEntity) entity).getItem().isFood();
			});
			return !foods.isEmpty();
		}
	}

	@Override
	public AgeableEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
		EntityFlyingPig pig = EntityRegistry.FLYING_PIG.create(this.world);
		pig.setWarted(pig.isWarted());
		return pig;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.NETHER_WART;
	}
}

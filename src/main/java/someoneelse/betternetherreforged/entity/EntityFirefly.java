package someoneelse.betternetherreforged.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.registry.SoundsRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.ITag;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EntityFirefly extends AnimalEntity implements IFlyingAnimal {
	private static final HashSet<Block> FLOWERS;
	private static final Vector3i[] SEARCH;

	private static final DataParameter<Float> COLOR_RED = EntityDataManager.createKey(EntityFirefly.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> COLOR_GREEN = EntityDataManager.createKey(EntityFirefly.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> COLOR_BLUE = EntityDataManager.createKey(EntityFirefly.class, DataSerializers.FLOAT);

	private boolean mustSit = false;

	public EntityFirefly(EntityType<? extends EntityFirefly> type, World world) {
		super(type, world);
		this.moveController = new FlyingMovementController(this, 20, true);
		this.lookController = new FreflyLookControl(this);
		this.setPathPriority(PathNodeType.LAVA, -1.0F);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.experienceValue = 1;
	}

	@Override
	protected void registerData() {
		super.registerData();
		makeColor(rand.nextFloat(), rand.nextFloat() * 0.5F + 0.25F, 1);
	}

	public static AttributeModifierMap getAttributeContainer() {
		return MobEntity
				.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 1.0)
				.createMutableAttribute(Attributes.FLYING_SPEED, 0.6)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 48.0)
				.create();
	}

	@Override
	protected PathNavigator createNavigator(World world) {
		FlyingPathNavigator birdNavigation = new FlyingPathNavigator(this, world) {
			public boolean canEntityStandOnPos(BlockPos pos) {
				BlockState state = this.world.getBlockState(pos.down());
				boolean valid = !state.getBlock().isAir(state, this.world, pos.down()) && state.getMaterial() != Material.LAVA;
				if (valid) {
					state = this.world.getBlockState(pos);
					valid = state.getBlock().isAir(state, this.world, pos) || !state.getMaterial().blocksMovement();
					valid = valid && state.getBlock() != BlocksRegistry.EGG_PLANT;
					valid = valid && !state.getMaterial().blocksMovement();
				}
				return valid;
			}

			public void tick() {
				super.tick();
			}
		};
		birdNavigation.setCanOpenDoors(false);
		birdNavigation.setCanSwim(false);
		birdNavigation.setCanEnterDoors(true);
		return birdNavigation;
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new SittingGoal());
		this.goalSelector.addGoal(5, new MoveToFlowersGoal());
		this.goalSelector.addGoal(6, new WanderAroundGoal());
		this.goalSelector.addGoal(7, new MoveRandomGoal());
	}

	@Override
	public float getBlockPathWeight(BlockPos pos, IWorldReader worldView) {
		return worldView.getBlockState(pos).getBlock().isAir(worldView.getBlockState(pos), worldView, pos) ? 10.0F : 0.0F;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == Items.GLOWSTONE_DUST;
	}

	@Override
	protected boolean makeFlySound() {
		return true;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	protected void handleFluidJump(ITag<Fluid> fluid) {
		this.setMotion(this.getMotion().add(0.0D, 0.01D, 0.0D));
	}

	@Override
	public float getBrightness() {
		return 1.0F;
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

	public float getRed() {
		return this.dataManager.get(COLOR_RED);
	}

	public float getGreen() {
		return this.dataManager.get(COLOR_GREEN);
	}

	public float getBlue() {
		return this.dataManager.get(COLOR_BLUE);
	}

	@Override
	public void writeAdditional(CompoundNBT tag) {
		super.writeAdditional(tag);

		tag.putFloat("ColorRed", getRed());
		tag.putFloat("ColorGreen", getGreen());
		tag.putFloat("ColorBlue", getBlue());
	}

	@Override
	public void readAdditional(CompoundNBT tag) {
		super.readAdditional(tag);

		if (tag.contains("ColorRed")) {
			this.dataManager.set(COLOR_RED, tag.getFloat("ColorRed"));
		}

		if (tag.contains("ColorGreen")) {
			this.dataManager.set(COLOR_GREEN, tag.getFloat("ColorGreen"));
		}

		if (tag.contains("ColorBlue")) {
			this.dataManager.set(COLOR_BLUE, tag.getFloat("ColorBlue"));
		}
	}

	@Override
	public AgeableEntity func_241840_a(ServerWorld world, AgeableEntity mate) {
		return null; //EntityRegistry.FIREFLY.create(world);
	}

	class FreflyLookControl extends LookController {
		FreflyLookControl(MobEntity entity) {
			super(entity);
		}

		protected boolean shouldStayHorizontal() {
			return true;
		}
	}

	class WanderAroundGoal extends Goal {
		WanderAroundGoal() {
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean shouldExecute() {
			return EntityFirefly.this.navigator.noPath() && EntityFirefly.this.rand.nextInt(10) == 0;
		}

		@Override
		public boolean shouldContinueExecuting() {
			return EntityFirefly.this.navigator.hasPath();
		}

		@Override
		public void startExecuting() {
			BlockPos pos = this.getRandomLocation();
			// if (pos != null)
			// {
			Path path = EntityFirefly.this.navigator.getPathToPos(pos, 1);
			if (path != null)
				EntityFirefly.this.navigator.setPath(path, 1.0D);
			else
				EntityFirefly.this.setVelocity(0, -0.2, 0);
			// }
			super.startExecuting();
		}

		private BlockPos getRandomLocation() {
			World w = EntityFirefly.this.world;
			Mutable bpos = new Mutable();
			bpos.setPos(EntityFirefly.this.getPosX(), EntityFirefly.this.getPosY(), EntityFirefly.this.getPosZ());

			if (w.isAirBlock(bpos.down(2)) && w.isAirBlock(bpos.down())) {
				int y = bpos.getY() - 1;
				while (w.isAirBlock(bpos.down(2)) && y > 0)
					bpos.setY(y--);
				return bpos;
			}

			Vector3d angle = EntityFirefly.this.getLook(0.0F);
			Vector3d airTarget = RandomPositionGenerator.findAirTarget(EntityFirefly.this, 8, 7, angle, 1.5707964F, 2, 1);

			if (airTarget == null) {
				airTarget = RandomPositionGenerator.findAirTarget(EntityFirefly.this, 16, 10, angle, 1.5707964F, 3, 1);
			}

			if (airTarget == null) {
				bpos.setX(bpos.getX() + randomRange(8));
				bpos.setZ(bpos.getZ() + randomRange(8));
				bpos.setY(bpos.getY() + randomRange(2));
				return bpos;
			}

			bpos.setPos(airTarget.getX(), airTarget.getY(), airTarget.getZ());

			return bpos;
		}

		private int randomRange(int side) {
			Random rand = EntityFirefly.this.rand;
			return rand.nextInt(side + 1) - (side >> 1);
		}

		@Override
		public void tick() {
			checkMovement();
			super.tick();
		}
	}

	class MoveToFlowersGoal extends Goal {
		MoveToFlowersGoal() {
			this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
		}

		@Override
		public boolean shouldExecute() {
			return EntityFirefly.this.navigator.noPath() && EntityFirefly.this.rand.nextInt(30) == 0;
		}

		@Override
		public boolean shouldContinueExecuting() {
			return EntityFirefly.this.navigator.hasPath();
		}

		@Override
		public void startExecuting() {
			BlockPos pos = this.getFlowerLocation();
			if (pos != null) {
				Path path = EntityFirefly.this.navigator.getPathToPos((BlockPos) (new BlockPos(pos)), 1);
				EntityFirefly.this.navigator.setPath(path, 1.0D);
			}
			super.startExecuting();
		}

		@Override
		public void resetTask() {
			if (isFlower(EntityFirefly.this.getBlockState()))
				EntityFirefly.this.mustSit = true;
			super.resetTask();
		}

		private BlockPos getFlowerLocation() {
			World w = EntityFirefly.this.world;
			Mutable bpos = new Mutable();

			for (Vector3i offset : SEARCH) {
				bpos.setPos(
						EntityFirefly.this.getPosX() + offset.getX(),
						EntityFirefly.this.getPosY() + offset.getY(),
						EntityFirefly.this.getPosZ() + offset.getZ());
				if (isFlower(w.getBlockState(bpos)))
					return bpos;
			}

			return null;
		}

		private boolean isFlower(BlockState state) {
			Block b = state.getBlock();
			return FLOWERS.contains(b);
		}

		@Override
		public void tick() {
			checkMovement();
			super.tick();
		}
	}

	private void checkMovement() {
		Vector3d vel = EntityFirefly.this.getMotion();
		if (Math.abs(vel.x) > 0.1 || Math.abs(vel.z) > 0.1) {
			double d = Math.abs(EntityFirefly.this.prevPosX - EntityFirefly.this.getPosX());
			d += Math.abs(EntityFirefly.this.prevPosZ - EntityFirefly.this.getPosZ());
			if (d < 0.1)
				EntityFirefly.this.navigator.clearPath();
		}
	}

	class SittingGoal extends Goal {
		int timer;
		int ammount;

		SittingGoal() {}

		@Override
		public boolean shouldExecute() {
			if (EntityFirefly.this.mustSit && EntityFirefly.this.navigator.noPath()) {
				BlockPos pos = new BlockPos(EntityFirefly.this.getPosX(), EntityFirefly.this.getPosY(), EntityFirefly.this.getPosZ());
				BlockState state = EntityFirefly.this.world.getBlockState(pos.down());
				return !state.getBlock().isAir(state, EntityFirefly.this.world, pos.down()) && !state.getMaterial().isLiquid();
			}
			return false;
		}

		@Override
		public boolean shouldContinueExecuting() {
			return timer < ammount;
		}

		@Override
		public void startExecuting() {
			timer = 0;
			ammount = EntityFirefly.this.rand.nextInt(21) + 20;
			EntityFirefly.this.mustSit = false;
			EntityFirefly.this.setVelocity(0, -0.1, 0);
			super.startExecuting();
		}

		@Override
		public void resetTask() {
			EntityFirefly.this.setVelocity(0, 0.1, 0);
			super.resetTask();
		}

		@Override
		public void tick() {
			timer++;
			super.tick();
		}
	}

	class MoveRandomGoal extends Goal {
		int timer;
		int ammount;

		MoveRandomGoal() {}

		@Override
		public boolean shouldExecute() {
			return EntityFirefly.this.navigator.noPath() && EntityFirefly.this.rand.nextInt(20) == 0;
		}

		@Override
		public boolean shouldContinueExecuting() {
			return timer < ammount;
		}

		@Override
		public void startExecuting() {
			timer = 0;
			ammount = EntityFirefly.this.rand.nextInt(30) + 10;
			Vector3d velocity = new Vector3d(
					EntityFirefly.this.rand.nextDouble(),
					EntityFirefly.this.rand.nextDouble(),
					EntityFirefly.this.rand.nextDouble());
			if (velocity.lengthSquared() == 0)
				velocity = new Vector3d(1, 0, 0);
			EntityFirefly.this.setMotion(velocity.normalize().scale(EntityFirefly.this.jumpMovementFactor));
			super.startExecuting();
		}

		@Override
		public void tick() {
			timer++;
			super.tick();
		}
	}

	@Override
	public SoundEvent getAmbientSound() {
		return SoundsRegistry.MOB_FIREFLY_FLY;
	}

	@Override
	protected float getSoundVolume() {
		return MHelper.randRange(0.1F, 0.3F, rand);
	}

	private void makeColor(float hue, float saturation, float brightness) {
		float red = 0;
		float green = 0;
		float blue = 0;
		float f3 = (hue - (float) Math.floor(hue)) * 6F;
		float f4 = f3 - (float) Math.floor(f3);
		float f5 = brightness * (1.0F - saturation);
		float f6 = brightness * (1.0F - saturation * f4);
		float f7 = brightness * (1.0F - saturation * (1.0F - f4));
		switch ((int) f3) {
			case 0:
				red = (byte) (brightness * 255F + 0.5F);
				green = (byte) (f7 * 255F + 0.5F);
				blue = (byte) (f5 * 255F + 0.5F);
				break;
			case 1:
				red = (byte) (f6 * 255F + 0.5F);
				green = (byte) (brightness * 255F + 0.5F);
				blue = (byte) (f5 * 255F + 0.5F);
				break;
			case 2:
				red = (byte) (f5 * 255F + 0.5F);
				green = (byte) (brightness * 255F + 0.5F);
				blue = (byte) (f7 * 255F + 0.5F);
				break;
			case 3:
				red = (byte) (f5 * 255F + 0.5F);
				green = (byte) (f6 * 255F + 0.5F);
				blue = (byte) (brightness * 255F + 0.5F);
				break;
			case 4:
				red = (byte) (f7 * 255F + 0.5F);
				green = (byte) (f5 * 255F + 0.5F);
				blue = (byte) (brightness * 255F + 0.5F);
				break;
			case 5:
				red = (byte) (brightness * 255F + 0.5F);
				green = (byte) (f5 * 255F + 0.5F);
				blue = (byte) (f6 * 255F + 0.5F);
				break;
		}
		this.dataManager.register(COLOR_RED, red / 255F);
		this.dataManager.register(COLOR_GREEN, green / 255F);
		this.dataManager.register(COLOR_BLUE, blue / 255F);
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 5;
	}

	static {
		ArrayList<Vector3i> points = new ArrayList<Vector3i>();
		int radius = 6;
		int r2 = radius * radius;
		for (int x = -radius; x <= radius; x++)
			for (int y = -radius; y <= radius; y++)
				for (int z = -radius; z <= radius; z++)
					if (x * x + y * y + z * z <= r2)
						points.add(new Vector3i(x, y, z));
		points.sort(new Comparator<Vector3i>() {
			@Override
			public int compare(Vector3i v1, Vector3i v2) {
				int d1 = v1.getX() * v1.getX() + v1.getY() * v1.getY() + v1.getZ() * v1.getZ();
				int d2 = v2.getX() * v2.getX() + v2.getY() * v2.getY() + v2.getZ() * v2.getZ();
				return d1 - d2;
			}
		});
		SEARCH = points.toArray(new Vector3i[] {});

		FLOWERS = new HashSet<Block>();
		FLOWERS.add(BlocksRegistry.NETHER_GRASS);
		FLOWERS.add(BlocksRegistry.SOUL_GRASS);
		FLOWERS.add(BlocksRegistry.SWAMP_GRASS);
		FLOWERS.add(BlocksRegistry.BLACK_APPLE);
		FLOWERS.add(BlocksRegistry.MAGMA_FLOWER);
		FLOWERS.add(BlocksRegistry.SOUL_VEIN);
		FLOWERS.add(BlocksRegistry.NETHER_REED);
		FLOWERS.add(BlocksRegistry.INK_BUSH);
		FLOWERS.add(BlocksRegistry.INK_BUSH_SEED);
		FLOWERS.add(BlocksRegistry.POTTED_PLANT);
		FLOWERS.add(Blocks.NETHER_WART);
	}

	public static boolean canSpawn(EntityType<? extends EntityFirefly> type, IWorld world, SpawnReason spawnReason, BlockPos pos, Random rand) {
		if (pos.getY() >= world.getDimensionType().getLogicalHeight()) return false;
		int h = BlocksHelper.downRay(world, pos, 10);
		if (h > 8)
			return false;
		for (int i = 1; i <= h; i++)
			if (BlocksHelper.isLava(world.getBlockState(pos.down(i))))
				return false;
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}
}

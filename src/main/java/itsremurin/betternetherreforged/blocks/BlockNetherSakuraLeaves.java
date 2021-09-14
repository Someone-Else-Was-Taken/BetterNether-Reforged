package itsremurin.betternetherreforged.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import itsremurin.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockNetherSakuraLeaves extends BlockBaseNotFull {
	private static final Random RANDOM = new Random();
	private static final int COLOR = MHelper.color(251, 113, 143);

	public BlockNetherSakuraLeaves() {
		super(MaterialBuilder.makeLeaves(MaterialColor.PINK).setLightLevel((state) -> {
			return 13;
		}));
		this.setDropItself(false);
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader view, BlockPos pos) {
		return super.getAmbientOcclusionLightValue(state, view, pos) * 0.5F + 0.5F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader view, BlockPos pos) {
		return true;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool != null && tool.getItem().isIn(Tags.Items.SHEARS) || EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this.asItem()));
		}
		else {
			return RANDOM.nextInt(5) == 0 ? Lists.newArrayList(new ItemStack(BlocksRegistry.NETHER_SAKURA_SAPLING)) : super.getDrops(state, builder);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(10) == 0) {
			BlockPos blockPos = pos.down();
			if (world.isAirBlock(blockPos)) {
				double x = (double) pos.getX() + random.nextDouble();
				double y = (double) pos.getY() - 0.05D;
				double z = (double) pos.getZ() + random.nextDouble();
				world.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, state), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public int getColor(BlockState state, IBlockReader world, BlockPos pos) {
		return COLOR;
	}
}
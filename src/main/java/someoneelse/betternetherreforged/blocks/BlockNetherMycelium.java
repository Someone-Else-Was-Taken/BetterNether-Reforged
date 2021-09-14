package someoneelse.betternetherreforged.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockNetherMycelium extends BlockBase {
	public static final BooleanProperty IS_BLUE = BooleanProperty.create("blue");

	public BlockNetherMycelium() {
		super(AbstractBlock.Properties.from(Blocks.NETHERRACK).setRequiresTool());
		this.setDefaultState(getStateContainer().getBaseState().with(IS_BLUE, false));
		this.setDropItself(false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(IS_BLUE);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		super.animateTick(state, world, pos, random);
		world.addParticle(ParticleTypes.MYCELIUM,
				pos.getX() + random.nextDouble(),
				pos.getY() + 1.1D,
				pos.getZ() + random.nextDouble(),
				0.0D, 0.0D, 0.0D);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool.canHarvestBlock(state)) {
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0)
				return Collections.singletonList(new ItemStack(this.asItem()));
			else
				return Collections.singletonList(new ItemStack(Blocks.NETHERRACK));
		}
		else
			return super.getDrops(state, builder);
	}
}

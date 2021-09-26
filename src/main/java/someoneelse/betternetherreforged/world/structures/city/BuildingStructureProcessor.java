package someoneelse.betternetherreforged.world.structures.city;

import someoneelse.betternetherreforged.blocks.BNPlanks;
import someoneelse.betternetherreforged.blocks.BlockBNPot;
import someoneelse.betternetherreforged.blocks.BlockPottedPlant;
import someoneelse.betternetherreforged.blocks.BlockSmallLantern;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraftforge.registries.ForgeRegistries;
import someoneelse.betternetherreforged.world.structures.city.palette.CityPalette;

public class BuildingStructureProcessor extends StructureProcessor {
	protected final CityPalette palette;

	public BuildingStructureProcessor(CityPalette palette) {
		this.palette = palette;
	}

	private BlockInfo setState(BlockState state, BlockInfo info) {
		return new BlockInfo(info.pos, state, info.nbt);
	}

	@Override
	public BlockInfo process(IWorldReader worldView, BlockPos pos, BlockPos blockPos, BlockInfo structureBlockInfo, BlockInfo structureBlockInfo2, PlacementSettings structurePlacementData, Template template) {
		BlockState state = structureBlockInfo.state;

		if (state.isAir())
			return structureBlockInfo2;

		Block block = state.getBlock();
		String name = ForgeRegistries.BLOCKS.getKey(block).getPath();

		if (name.startsWith("roof_tile")) {
			if (block instanceof StairsBlock) {
				return setState(palette.getRoofStair(state), structureBlockInfo2);
			}
			else if (block instanceof SlabBlock) {
				return setState(palette.getRoofSlab(state), structureBlockInfo2);
			}
			return setState(palette.getRoofBlock(state), structureBlockInfo2);
		}
		else if (name.contains("nether") && name.contains("brick")) {
			if (block instanceof StairsBlock) {
				return setState(palette.getFoundationStair(state), structureBlockInfo2);
			}
			else if (block instanceof SlabBlock) {
				return setState(palette.getFoundationSlab(state), structureBlockInfo2);
			}
			else if (block instanceof WallBlock) {
				return setState(palette.getFoundationWall(state), structureBlockInfo2);
			}
			return setState(palette.getFoundationBlock(state), structureBlockInfo2);
		}
		else if (name.contains("plank") || name.contains("reed") || block instanceof BNPlanks) {
			if (block instanceof StairsBlock) {
				return setState(palette.getPlanksStair(state), structureBlockInfo2);
			}
			else if (block instanceof SlabBlock) {
				return setState(palette.getPlanksSlab(state), structureBlockInfo2);
			}
			return setState(palette.getPlanksBlock(state), structureBlockInfo2);
		}
		else if (name.contains("glass") || name.contains("frame")) {
			if (block instanceof PaneBlock)
				return setState(palette.getGlassPane(state), structureBlockInfo2);
			return setState(palette.getGlassBlock(state), structureBlockInfo2);
		}
		else if (block instanceof RotatedPillarBlock) {
			if (name.contains("log")) {
				return setState(palette.getLog(state), structureBlockInfo2);
			}
			return setState(palette.getBark(state), structureBlockInfo2);
		}
		else if (block instanceof StairsBlock) {
			return setState(palette.getStoneStair(state), structureBlockInfo2);
		}
		else if (block instanceof SlabBlock) {
			return setState(palette.getStoneSlab(state), structureBlockInfo2);
		}
		else if (block instanceof WallBlock) {
			return setState(palette.getWall(state), structureBlockInfo2);
		}
		else if (block instanceof FenceBlock) {
			return setState(palette.getFence(state), structureBlockInfo2);
		}
		else if (block instanceof FenceGateBlock) {
			return setState(palette.getGate(state), structureBlockInfo2);
		}
		else if (block instanceof DoorBlock) {
			return setState(palette.getDoor(state), structureBlockInfo2);
		}
		else if (block instanceof TrapDoorBlock) {
			return setState(palette.getTrapdoor(state), structureBlockInfo2);
		}
		else if (block instanceof PressurePlateBlock) {
			if (block.getSoundType(state) == SoundType.WOOD)
				return setState(palette.getWoodenPlate(state), structureBlockInfo2);
			else
				return setState(palette.getStonePlate(state), structureBlockInfo2);
		}
		else if (block instanceof BlockSmallLantern) {
			if (state.get(BlockSmallLantern.FACING) == Direction.UP)
				return setState(palette.getCeilingLight(state), structureBlockInfo2);
			else if (state.get(BlockSmallLantern.FACING) != Direction.DOWN)
				return setState(palette.getWallLight(state), structureBlockInfo2);
			else
				return setState(palette.getFloorLight(state), structureBlockInfo2);
		}
		else if (block instanceof BlockBNPot) {
			return setState(palette.getPot(state), structureBlockInfo2);
		}
		else if (block instanceof BlockPottedPlant) {
			return setState(palette.getPlant(state), structureBlockInfo2);
		}
		else if (block instanceof StructureBlock) {
			return setState(Blocks.AIR.getDefaultState(), structureBlockInfo2);
		}
		else if (!name.contains("nether") && !name.contains("mycelium") && state.hasOpaqueCollisionShape(worldView, structureBlockInfo.pos) && state.isSolid() && !(state.getBlock() instanceof ContainerBlock)) {
			if (state.getLightValue() > 0)
				return setState(palette.getGlowingBlock(state), structureBlockInfo2);
			return setState(palette.getStoneBlock(state), structureBlockInfo2);
		}

		return structureBlockInfo2;
	}

	@Override
	protected IStructureProcessorType<?> getType() {
		return IStructureProcessorType.NOP;
	}
}
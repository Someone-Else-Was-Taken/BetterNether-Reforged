package someoneelse.betternetherreforged.world.structures.piece;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import someoneelse.betternetherreforged.BetterNether;

public class StructureTypes {
	public static final IStructurePieceType NETHER_CITY = register(CityPiece::new, "bncity");
	public static final IStructurePieceType CAVE = register(CavePiece::new, "bncave");
	public static final IStructurePieceType DESTRUCTION = register(DestructionPiece::new, "bndestr");
	public static final IStructurePieceType ANCHOR_TREE = register(DestructionPiece::new, "anchor_tree");

	public static void init() {}

	protected static IStructurePieceType register(IStructurePieceType pieceType, String id) {
		return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(BetterNether.MOD_ID, id), pieceType);
	}
}

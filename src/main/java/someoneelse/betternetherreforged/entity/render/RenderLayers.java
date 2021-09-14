package someoneelse.betternetherreforged.entity.render;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderLayers {
	public static RenderState.TransparencyState translucentTransparency;
	public static RenderState.WriteMaskState colorMask;
	public static RenderState.FogState fog;
	public static RenderState.DepthTestState lEqualDepthTest;
	public static RenderState.LayerState polygonZLayering;
	public static RenderState.TargetState translucentTarget;

	public static RenderType getFirefly(ResourceLocation texture) {
		RenderType.State multiPhaseParameters = RenderType.State.getBuilder()
				.texture(new RenderState.TextureState(texture, false, false))
				.transparency(translucentTransparency)
				.writeMask(colorMask)
				.fog(fog)
				.depthTest(lEqualDepthTest)
				.layer(polygonZLayering)
				.target(translucentTarget)
				.build(true);
		return RenderType.makeType("firefly", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256, false, true, multiPhaseParameters);
	}
}
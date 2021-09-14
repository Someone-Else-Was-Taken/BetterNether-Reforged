package someoneelse.betternetherreforged.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.entity.EntityNagaProjectile;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class RenderNagaProjectile extends EntityRenderer<EntityNagaProjectile> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNether.MOD_ID, "textures/entity/naga_projectile.png");
	private static final RenderType LAYER = RenderType.getEntityCutoutNoCull(TEXTURE);

	public RenderNagaProjectile(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityNagaProjectile entity) {
		return TEXTURE;
	}

	@Override
	public void render(EntityNagaProjectile dragonFireballEntity, float f, float g, MatrixStack matrixStack, IRenderTypeBuffer vertexConsumerProvider, int i) {
		int frame = (int) (System.currentTimeMillis() / 150) & 3;
		float start = frame * 0.25F;
		float end = start + 0.25F;
		matrixStack.push();
		matrixStack.scale(2.0F, 2.0F, 2.0F);
		matrixStack.rotate(this.renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		MatrixStack.Entry entry = matrixStack.getLast();
		Matrix4f matrix4f = entry.getMatrix();
		Matrix3f matrix3f = entry.getNormal();
		IVertexBuilder vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
		vertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 0, 0, end);
		vertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 0, 1, end);
		vertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 1, 1, start);
		vertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 1, 0, start);
		matrixStack.pop();
		super.render(dragonFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	private static void vertex(IVertexBuilder vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int i, float f, int j, float u, float v) {
		vertexConsumer.pos(matrix4f, f - 0.5F, (float) j - 0.25F, 0.0F).color(255, 255, 255, 255).tex(u, v).overlay(OverlayTexture.NO_OVERLAY).lightmap(i).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
	}
}
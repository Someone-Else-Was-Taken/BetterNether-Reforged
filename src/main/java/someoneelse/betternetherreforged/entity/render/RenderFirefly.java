package someoneelse.betternetherreforged.entity.render;

import java.util.Iterator;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

<<<<<<< Updated upstream:src/main/java/someoneelse/betternetherreforged/entity/render/RenderFirefly.java
=======
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
>>>>>>> Stashed changes:src/main/java/itsremurin/betternetherreforged/entity/render/RenderFirefly.java
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.entity.EntityFirefly;
import someoneelse.betternetherreforged.entity.model.ModelEntityFirefly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
/*
public class RenderFirefly extends MobRenderer<EntityFirefly, AgeableModel<EntityFirefly>> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNether.MOD_ID, "textures/entity/firefly.png");
	private static final RenderType LAYER = RenderLayers.getFirefly(TEXTURE); // getEntityTranslucent
																				// getEntityNoOutline
																				// getBeaconBeam
																				// getEntityShadow
	private static final int LIT = 15728880;

	public RenderFirefly(EntityRendererManager renderManager) {
		super(renderManager, new ModelEntityFirefly(), 0);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityFirefly entity) {
		return TEXTURE;
	}

	@Override
	public void render(EntityFirefly entity, float f, float g, MatrixStack matrixStack, IRenderTypeBuffer vertexConsumerProvider, int i) {

		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();

		float red = entity.getRed();
		float green = entity.getGreen();
		float blue = entity.getBlue();

		matrixStack.push();
		matrixStack.translate(0, 0.125, 0);
		matrixStack.rotate(this.renderManager.getCameraOrientation());
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		MatrixStack.Entry entry = matrixStack.getLast();
		Matrix4f matrix4f = entry.getMatrix();
		Matrix3f matrix3f = entry.getNormal();
		IVertexBuilder vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
		addVertex(matrix4f, matrix3f, vertexConsumer, -1, -1, 0F, 0.5F, red, green, blue);
		addVertex(matrix4f, matrix3f, vertexConsumer, 1, -1, 1F, 0.5F, red, green, blue);
		addVertex(matrix4f, matrix3f, vertexConsumer, 1, 1, 1F, 1F, red, green, blue);
		addVertex(matrix4f, matrix3f, vertexConsumer, -1, 1, 0F, 1F, red, green, blue);
		matrixStack.pop();

		matrixStack.push();
		this.entityModel.swingProgress = this.getSwingProgress(entity, g);
		this.entityModel.isSitting = entity.isPassenger();
		this.entityModel.isChild = entity.isChild();
		float h = MathHelper.interpolateAngle(g, entity.prevRenderYawOffset, entity.renderYawOffset);
		float j = MathHelper.interpolateAngle(g, entity.prevRotationYawHead, entity.rotationYawHead);
		float k = j - h;
		float o;
		if (entity.isPassenger() && entity.getRidingEntity() instanceof LivingEntity) {
			LivingEntity mobEntity2 = (LivingEntity) entity.getRidingEntity();
			h = MathHelper.interpolateAngle(g, mobEntity2.prevRenderYawOffset, mobEntity2.renderYawOffset);
			k = j - h;
			o = MathHelper.wrapDegrees(k);
			if (o < -85.0F) {
				o = -85.0F;
			}

			if (o >= 85.0F) {
				o = 85.0F;
			}

			h = j - o;
			if (o * o > 2500.0F) {
				h += o * 0.2F;
			}

			k = j - h;
		}

		float m = MathHelper.lerp(g, entity.prevRotationPitch, entity.rotationPitch);
		float p;
		if (entity.getPose() == Pose.SLEEPING) {
			Direction direction = entity.getBedDirection();
			if (direction != null) {
				p = entity.getEyeHeight(Pose.STANDING) - 0.1F;
				matrixStack.translate((double) ((float) (-direction.getXOffset()) * p), 0.0D,
						(double) ((float) (-direction.getZOffset()) * p));
			}
		}

		o = this.handleRotationFloat(entity, g);
		this.applyRotations(entity, matrixStack, o, h, g);
		matrixStack.scale(-1.0F, -1.0F, 1.0F);
		this.preRenderCallback(entity, matrixStack, g);
		matrixStack.translate(0.0D, -1.5010000467300415D, 0.0D);
		p = 0.0F;
		float q = 0.0F;
		if (!entity.isPassenger() && entity.isAlive()) {
			p = MathHelper.lerp(g, entity.prevLimbSwingAmount, entity.limbSwingAmount);
			q = entity.limbSwing - entity.limbSwingAmount * (1.0F - g);
			if (entity.isChild()) {
				q *= 3.0F;
			}

			if (p > 1.0F) {
				p = 1.0F;
			}
		}

		this.entityModel.setLivingAnimations(entity, q, p, g);
		this.entityModel.setRotationAngles(entity, q, p, o, k, m);
		boolean visible = this.isVisible(entity);
		boolean ghost = !visible && !entity.isInvisibleToPlayer(Minecraft.getInstance().player);
		Minecraft client = Minecraft.getInstance();
		boolean outline = client.isEntityGlowing(entity);
		RenderType layer = this.func_230496_a_(entity, visible, ghost, outline);
		if (layer != null) {
			int r = getPackedOverlay(entity, 0);
			this.entityModel.render(matrixStack, vertexConsumer, i, r, red, green, blue, ghost ? 0.15F : 1.0F);

		}

		if (!entity.isSpectator()) {
			Iterator<?> var21 = this.layerRenderers.iterator();
			while (var21.hasNext()) {
				@SuppressWarnings("unchecked")
				LayerRenderer<EntityFirefly, AgeableModel<EntityFirefly>> feature = (LayerRenderer<EntityFirefly, AgeableModel<EntityFirefly>>) var21.next();
				feature.render(matrixStack, vertexConsumerProvider, i, entity, q, p, g, o, k, m);
			}
		}

		matrixStack.pop();

		if (this.canRenderName(entity)) {
			this.renderName(entity, entity.getDisplayName(), matrixStack, vertexConsumerProvider, i);
		}

	}

	public void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, IVertexBuilder vertexConsumer, float posX, float posY, float u, float v, float red, float green, float blue) {
		vertexConsumer.pos(matrix4f, posX, posY, 0).color(red, green, blue, 1F).tex(u, v).overlay(OverlayTexture.NO_OVERLAY).lightmap(LIT).normal(matrix3f, 0, 1, 0).endVertex();
	}

	@Override
	protected int getBlockLight(EntityFirefly entity, BlockPos blockPos) {
		return 15;
	}



}

*/
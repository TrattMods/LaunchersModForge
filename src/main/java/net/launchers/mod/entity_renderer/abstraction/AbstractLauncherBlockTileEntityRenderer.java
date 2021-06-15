package net.launchers.mod.entity_renderer.abstraction;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.utils.MathUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Vector3d;


import java.util.Random;

public class AbstractLauncherBlockTileEntityRenderer<T extends AbstractLauncherBlockTileEntity> extends TileEntityRenderer<T>
{
    public AbstractLauncherBlockTileEntityRenderer(TileEntityRendererDispatcher p_i226006_1_)
    {
        super(p_i226006_1_);
    }
    protected final BlockRendererDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
    @Override
    public void render(T blockEntity, float tickDelta, MatrixStack matrices, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
    {
        BlockState entityState = blockEntity.getBlockState();
        matrices.pushPose();

        float extension = blockEntity.getDeltaProgress(tickDelta);
        //LLoader.LOGGER.info(extension);
        IBakedModel model = null;
        if(extension < 0.35F)
        {
            model = blockRenderManager.getBlockModel(entityState.setValue(AbstractLauncherBlock.MODELS, 2).setValue(AbstractLauncherBlock.FACING, entityState.getValue(AbstractLauncherBlock.FACING)));
        }
        else
        {
            model = blockRenderManager.getBlockModel(entityState.setValue(AbstractLauncherBlock.MODELS, 1).setValue(AbstractLauncherBlock.FACING, entityState.getValue(AbstractLauncherBlock.FACING)));
        }
        Vector3d translation = MathUtils.fromDirection(entityState.getValue(AbstractLauncherBlock.FACING));
        matrices.translate(translation.x * extension, translation.y * extension, translation.z * extension);

        IVertexBuilder vertexConsumer = renderTypeBuffer.getBuffer(RenderType.solid());
        this.blockRenderManager.getModelRenderer().tesselateBlock(blockEntity.getLevel(), model, entityState, blockEntity.getBlockPos(), matrices, vertexConsumer, true, new Random(), (long)4, overlay);

        matrices.popPose();
    }
}

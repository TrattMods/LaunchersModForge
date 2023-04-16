package net.launchers.mod.entity_renderer.abstraction;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.launchers.mod.block.abstraction.AbstractLauncherBlock;
import net.launchers.mod.entity.abstraction.AbstractLauncherBlockTileEntity;
import net.launchers.mod.utils.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AbstractLauncherBlockTileEntityRenderer implements BlockEntityRenderer {
    private final BlockEntityRendererProvider.Context context ;

    public AbstractLauncherBlockTileEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.context = context;
    }


    @Override
    public void render(BlockEntity blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource renderTypeBuffer, int light, int overlay) {
        AbstractLauncherBlockTileEntity lEntity = (AbstractLauncherBlockTileEntity) blockEntity;
        BlockState entityState = blockEntity.getBlockState();
        matrices.pushPose();
        BlockRenderDispatcher dispatcher = context.getBlockRenderDispatcher();
        float extension = lEntity.getDeltaProgress(tickDelta);
        extension=0;
        //LLoader.LOGGER.info(extension);
        BakedModel model = null;
        if(extension < 0.35F)
        {
            model = dispatcher.getBlockModel(entityState.setValue(AbstractLauncherBlock.MODELS, 2)
                    .setValue(AbstractLauncherBlock.FACING, entityState.getValue(AbstractLauncherBlock.FACING)));
        }
        else
        {
            model = dispatcher.getBlockModel(entityState.setValue(AbstractLauncherBlock.MODELS, 1)
                    .setValue(AbstractLauncherBlock.FACING, entityState.getValue(AbstractLauncherBlock.FACING)));
        }
        Vec3 translation = MathUtils.fromDirection(entityState.getValue(AbstractLauncherBlock.FACING));
        matrices.translate(translation.x * extension, translation.y * extension, translation.z * extension);

        VertexConsumer vertexConsumer = renderTypeBuffer.getBuffer(RenderType.solid());
        dispatcher.getModelRenderer()
                .tesselateBlock(blockEntity.getLevel(), model, entityState, blockEntity.getBlockPos(), matrices, vertexConsumer, true, RandomSource.create(), (long)4, overlay);

        matrices.popPose();
    }

}

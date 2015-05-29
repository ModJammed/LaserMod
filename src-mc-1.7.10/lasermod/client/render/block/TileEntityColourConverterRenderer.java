package lasermod.client.render.block;

import lasermod.api.LaserCollisionBoxes;
import lasermod.api.LaserInGame;
import lasermod.api.LaserToRender;
import lasermod.client.render.LaserRenderer;
import lasermod.helper.ClientHelper;
import lasermod.tileentity.TileEntityColourConverter;
import lasermod.util.LaserUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

/**
 * @author ProPercivalalb
 */
public class TileEntityColourConverterRenderer extends TileEntitySpecialRenderer {

    public void renderColourConverter(TileEntityColourConverter colourConverter, double x, double y, double z, float tick) {
    	if(colourConverter.getOutputLaser(colourConverter.getBlockMetadata()) == null)
    		return;
    	LaserInGame laserInGame = colourConverter.getOutputLaser(colourConverter.getBlockMetadata());
    	float alpha = laserInGame.shouldRenderLaser(ClientHelper.getPlayer());

    	if(alpha == 0.0F)
    		return;

		AxisAlignedBB boundingBox = LaserUtil.getLaserOutline(colourConverter, colourConverter.getBlockMetadata(), x, y, z);
		LaserCollisionBoxes.addLaserCollision(new LaserToRender(laserInGame, boundingBox, x, y, z, colourConverter.xCoord, colourConverter.yCoord, colourConverter.zCoord, colourConverter.getBlockMetadata(), alpha));

    }
    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float tick) {
        renderColourConverter((TileEntityColourConverter)tileEntity, x, y, z, tick);
    }
}

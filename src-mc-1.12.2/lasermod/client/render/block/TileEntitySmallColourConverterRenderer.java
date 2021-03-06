package lasermod.client.render.block;

import lasermod.api.LaserCollisionBoxes;
import lasermod.api.LaserInGame;
import lasermod.api.LaserToRender;
import lasermod.helper.ClientHelper;
import lasermod.tileentity.TileEntitySmallColourConverter;
import lasermod.util.LaserUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(value = Side.CLIENT)
public class TileEntitySmallColourConverterRenderer extends TileEntitySpecialRenderer<TileEntitySmallColourConverter> {

	@Override
	public void render(TileEntitySmallColourConverter colourConverter, double x, double y, double z, float partialTicks, int destroyStage, float a) {
    	if(colourConverter.getOutputLaser(EnumFacing.byIndex(colourConverter.getBlockMetadata())) == null)
    		return;
    	LaserInGame laserInGame = colourConverter.getOutputLaser(EnumFacing.byIndex(colourConverter.getBlockMetadata()));
    	float alpha = laserInGame.shouldRenderLaser(ClientHelper.getPlayer());

    	if(alpha == 0.0F)
    		return;
    	
		AxisAlignedBB boundingBox = LaserUtil.getLaserOutline(colourConverter, EnumFacing.byIndex(colourConverter.getBlockMetadata()), x, y, z);
		LaserCollisionBoxes.addLaserCollision(new LaserToRender(laserInGame, boundingBox, x, y, z, colourConverter.getPos(), EnumFacing.byIndex(colourConverter.getBlockMetadata()), alpha, true));

    }
	
	@Override
	public boolean isGlobalRenderer(TileEntitySmallColourConverter te) {
        return true;
    }
}

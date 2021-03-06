package lasermod.tileentity;

import lasermod.api.base.TileEntityMultiSidedReceiver;
import lasermod.block.BlockLaserDetector;
import lasermod.network.PacketDispatcher;
import lasermod.network.packet.client.LaserDetectorMessage;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class TileEntityLaserDetector extends TileEntityMultiSidedReceiver {
	
	@Override
	public void sendUpdateDescription() {
		PacketDispatcher.sendToAllTracking(new LaserDetectorMessage(this), this);
	}

	@Override
	public void onLaserPass(World world) {
		world.setBlockState(this.pos, world.getBlockState(pos).withProperty(BlockLaserDetector.ACTIVE, true));
	}

	@Override
	public void onLaserRemoved(World world) {
		if(this.lasers.isEmpty())
			world.setBlockState(this.pos, world.getBlockState(pos).withProperty(BlockLaserDetector.ACTIVE, false));
	}
	
	//TODO
		/**
	@Override
	public Packet getDescriptionPacket() {
	    return PacketDispatcher.getPacket(new LaserDetectorMessage(this));
	}**/
}
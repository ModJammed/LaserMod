package lasermod.network.packet.client;

import java.io.IOException;
import java.util.ArrayList;

import lasermod.api.LaserInGame;
import lasermod.network.AbstractMessage.AbstractClientMessage;
import lasermod.tileentity.TileEntityLuminousLamp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author ProPercivalalb
 */
public class LuminousLampMessage extends AbstractClientMessage<LuminousLampMessage> {
	
	public BlockPos pos;
	public ArrayList<LaserInGame> lasers;
	
	public LuminousLampMessage() {}
	public LuminousLampMessage(TileEntityLuminousLamp luminousPanel) {
		this.pos = luminousPanel.getPos();
	    this.lasers = luminousPanel.lasers;
	}

	@Override
	protected LuminousLampMessage encode(PacketBuffer buffer) throws IOException {
		this.pos = buffer.readBlockPos();

	    this.lasers = new ArrayList<LaserInGame>();
	    int count = buffer.readInt();
	    for(int i = 0; i < count; ++i)
	    	this.lasers.add(LaserInGame.readFromPacket(buffer));
	    return this;
		
	}
	@Override
	protected void decode(LuminousLampMessage msg, PacketBuffer buffer) throws IOException {
		buffer.writeBlockPos(msg.pos);
		
		buffer.writeInt(msg.lasers.size());
		
		for(int i = 0; i < msg.lasers.size(); ++i) 
			msg.lasers.get(i).writeToPacket(buffer);
		
	}
	
	@Override
	public void process(LuminousLampMessage msg, EntityPlayer player, Side side) {
		World world = player.world;
		TileEntity tileEntity = world.getTileEntity(msg.pos);
		
		if(!(tileEntity instanceof TileEntityLuminousLamp)) 
			return;
		TileEntityLuminousLamp colourConverter = (TileEntityLuminousLamp)tileEntity;
		colourConverter.lasers = msg.lasers;
		colourConverter.setUpdateRequired();
		world.markAndNotifyBlock(this.pos, world.getChunk(msg.pos), world.getBlockState(msg.pos), world.getBlockState(msg.pos), 2);
		world.checkLightFor(EnumSkyBlock.BLOCK, msg.pos);
		
	}
}

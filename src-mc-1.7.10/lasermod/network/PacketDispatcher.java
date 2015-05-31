package lasermod.network;

import lasermod.lib.Reference;
import lasermod.network.packet.client.AdvancedLaserMessage;
import lasermod.network.packet.client.ColourConverterMessage;
import lasermod.network.packet.client.LaserDetectorMessage;
import lasermod.network.packet.client.LuminousLampMessage;
import lasermod.network.packet.client.ReflectorMessage;
import lasermod.network.packet.client.SmallColourConverterMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * @author ProPercivalalb
 * Thanks to coolAlias for providing the tutorial 
 * that contains most of this network handler code
 * https://github.com/coolAlias/Tutorial-Demo
 */
public class PacketDispatcher {
	
	private static int packetId = 0;

	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL_NAME);

	public static final void registerPackets() {
		registerMessage(AdvancedLaserMessage.Handler.class, AdvancedLaserMessage.class);
		registerMessage(ColourConverterMessage.Handler.class, ColourConverterMessage.class);
		registerMessage(LaserDetectorMessage.Handler.class, LaserDetectorMessage.class);
		registerMessage(LuminousLampMessage.Handler.class, LuminousLampMessage.class);
		registerMessage(ReflectorMessage.Handler.class, ReflectorMessage.class);
		registerMessage(SmallColourConverterMessage.Handler.class, SmallColourConverterMessage.class);
	}

	private static final <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handlerClass, Class<REQ> messageClass, Side side) {
		PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
	}

	private static final <REQ extends IMessage, REPLY extends IMessage> void registerBiMessage(Class<? extends IMessageHandler<REQ, REPLY>> handlerClass, Class<REQ> messageClass) {
		PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId, Side.CLIENT);
		PacketDispatcher.dispatcher.registerMessage(handlerClass, messageClass, packetId++, Side.SERVER);
	}

	private static final <REQ extends IMessage> void registerMessage(Class<? extends AbstractMessageHandler<REQ>> handlerClass, Class<REQ> messageClass) {
		if(AbstractClientMessageHandler.class.isAssignableFrom(handlerClass))
			registerMessage(handlerClass, messageClass, Side.CLIENT);
		else if (AbstractServerMessageHandler.class.isAssignableFrom(handlerClass))
			registerMessage(handlerClass, messageClass, Side.SERVER);
		else if (AbstractBiMessageHandler.class.isAssignableFrom(handlerClass))
			registerBiMessage(handlerClass, messageClass);
		else
			throw new IllegalArgumentException("Cannot determine on which Side(s) to register " + handlerClass.getName() + " - unrecognized handler class!");
	}

	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		PacketDispatcher.dispatcher.sendTo(message, player);
	}

	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		PacketDispatcher.dispatcher.sendToAllAround(message, point);
	}

	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		PacketDispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		PacketDispatcher.sendToAllAround(message, player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, range);
	}
	
	public static final void sendToAllAround(IMessage message, TileEntity tileEntity, double range) {
		PacketDispatcher.sendToAllAround(message, tileEntity.getWorldObj().provider.dimensionId, tileEntity.xCoord + 0.5D, tileEntity.yCoord + 0.5D, tileEntity.zCoord + 0.5D, range);
	}

	public static final void sendToDimension(IMessage message, int dimensionId) {
		PacketDispatcher.dispatcher.sendToDimension(message, dimensionId);
	}

	public static final void sendToServer(IMessage message) {
		PacketDispatcher.dispatcher.sendToServer(message);
	}
	
	public static final Packet getPacket(IMessage message) {
		return PacketDispatcher.dispatcher.getPacketFrom(message);
	}
}

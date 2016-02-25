package bq_educational.network;

import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Level;
import betterquesting.network.PktHandler;
import bq_educational.core.BQ_Educational;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketEdu implements IMessage
{
	NBTTagCompound tags = new NBTTagCompound();
	
	public PacketEdu()
	{
	}
	
	private PacketEdu(NBTTagCompound payload)
	{
		tags = payload;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		if(BQ_Educational.proxy.isClient() && Minecraft.getMinecraft().thePlayer != null)
		{
			tags.setString("Sender", Minecraft.getMinecraft().thePlayer.getUniqueID().toString());
			tags.setInteger("Dimension", Minecraft.getMinecraft().thePlayer.dimension);
		}
		
		ByteBufUtils.writeTag(buf, tags);
	}
	
	public static class HandlerServer implements IMessageHandler<PacketEdu,IMessage>
	{
		@Override
		public IMessage onMessage(PacketEdu message, MessageContext ctx)
		{
			if(message == null || message.tags == null)
			{
				BQ_Educational.logger.log(Level.ERROR, "A critical NPE error occured during while handling a BQ Standard packet server side", new NullPointerException());
				return null;
			}
			
			int ID = !message.tags.hasKey("ID")? -1 : message.tags.getInteger("ID");
			
			if(ID < 0)
			{
				BQ_Educational.logger.log(Level.ERROR, "Recieved a packet server side with an invalid ID", new NullPointerException());
				return null;
			}
			
			EntityPlayer player = null;
			
			if(message.tags.hasKey("Sender"))
			{
				try
				{
					WorldServer world = MinecraftServer.getServer().worldServerForDimension(message.tags.getInteger("Dimension"));
					player = world.func_152378_a(UUID.fromString(message.tags.getString("Sender")));
				} catch(Exception e)
				{
					
				}
			}
			
			EduPacketType dataType = EduPacketType.values()[ID];
			PktHandler handler = pktHandlers.get(dataType);
			
			if(handler != null)
			{
				return handler.handleServer(player, message.tags);
			} else
			{
				BQ_Educational.logger.log(Level.ERROR, "Unable to find valid packet handler for data type: " + dataType.toString());
				return null;
			}
		}
	}
	
	public static class HandlerClient implements IMessageHandler<PacketEdu,IMessage>
	{
		@Override
		public IMessage onMessage(PacketEdu message, MessageContext ctx)
		{
			if(message == null || message.tags == null)
			{
				BQ_Educational.logger.log(Level.ERROR, "A critical NPE error occured during while handling a BQ Standard packet client side", new NullPointerException());
				return null;
			}
			
			int ID = !message.tags.hasKey("ID")? -1 : message.tags.getInteger("ID");
			
			if(ID < 0 || ID >= EduPacketType.values().length)
			{
				BQ_Educational.logger.log(Level.ERROR, "Recieved a packet client side with an invalid ID", new NullPointerException());
				return null;
			}
			
			EduPacketType dataType = EduPacketType.values()[ID];
			PktHandler handler = pktHandlers.get(dataType);
			
			if(handler != null)
			{
				return handler.handleClient(message.tags);
			} else
			{
				BQ_Educational.logger.log(Level.ERROR, "Unable to find valid packet handler for data type: " + dataType.toString());
				return null;
			}
		}
	}
	
	static HashMap<EduPacketType,PktHandler> pktHandlers = new HashMap<EduPacketType,PktHandler>();
	
	static
	{
		pktHandlers.put(EduPacketType.EDU_WS_DB, new PktEduWorksheetDB());
	}
	
	public enum EduPacketType
	{
		EDU_WS_DB;
		
		public PacketEdu makePacket(NBTTagCompound payload)
		{
			payload.setInteger("ID", ordinal());
			return new PacketEdu(payload);
		}
	}
}

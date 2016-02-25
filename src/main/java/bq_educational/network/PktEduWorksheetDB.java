package bq_educational.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import betterquesting.network.PktHandler;
import betterquesting.utils.NBTConverter;
import bq_educational.network.PacketEdu.EduPacketType;
import bq_educational.tasks.worksheet.WorksheetDatabase;
import com.google.gson.JsonObject;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class PktEduWorksheetDB extends PktHandler
{
	@Override
	public IMessage handleServer(EntityPlayer sender, NBTTagCompound data)
	{
		NBTTagCompound tags = new NBTTagCompound();
		JsonObject json = new JsonObject();
		WorksheetDatabase.instance.writeToJson(json);
		tags.setTag("Database", NBTConverter.JSONtoNBT_Object(json, new NBTTagCompound()));
		return EduPacketType.EDU_WS_DB.makePacket(tags);
	}
	
	@Override
	public IMessage handleClient(NBTTagCompound data)
	{
		JsonObject json = NBTConverter.NBTtoJSON_Compound(data.getCompoundTag("Database"), new JsonObject());
		WorksheetDatabase.instance.readFromJson(json);
		return null;
	}
}

package bq_educational.core.proxies;

import bq_educational.client.gui.UpdateNotification;
import bq_educational.core.BQ_Educational;
import bq_educational.network.PacketEdu;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy
{
	public boolean isClient()
	{
		return false;
	}
	
	public void registerHandlers()
	{
		FMLCommonHandler.instance().bus().register(new UpdateNotification());
    	
    	BQ_Educational.instance.network.registerMessage(PacketEdu.HandlerServer.class, PacketEdu.class, 1, Side.SERVER);
	}

	public void registerThemes()
	{
	}
}

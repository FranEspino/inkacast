package com.inkamedia.inkacast.dlna.util;

import org.cybergarage.upnp.Device;

public class DLNAUtil {
	private static final String MEDIARENDER = "urn:schemas-upnp-org:device:MediaRenderer:1";

	public static boolean isMediaRenderDevice(Device device) {
		if (device != null
				&& MEDIARENDER.equalsIgnoreCase(device.getDeviceType())) {
			return true;
		}

		return false;
	}
}

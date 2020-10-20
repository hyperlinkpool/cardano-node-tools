package kr.hyperlinkpool.utils;

import java.util.regex.Pattern;

import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.NodeInfo;

public class CommonUtils {
	
	public static boolean ip4Validation(String ip4) {
		String regex = "((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])([.](?!$)|$)){4}";
		return Pattern.matches(regex, ip4);
	}
	
	public static boolean ip6Validation(String ip6) {
		String regex = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
		return Pattern.matches(regex, ip6);
	}
	
	public static String nodeInfoParse(String[][] nodeInfos) {
		StringBuilder result = new StringBuilder();
		int relayNodeCount = nodeInfos.length;
		for(int i=0;i<relayNodeCount;i++) {
			String type = nodeInfos[i][0];
			String ipOrDns = nodeInfos[i][1];
			String port = nodeInfos[i][2];
			String format = null;
			if(NodeInfo.IP.getNodeInfo().equals(type)) {
				format = "--pool-relay-ipv4 %s --pool-relay-port %s";
			}else {
				format = "--single-host-pool-relay %s --pool-relay-port %s";
			}
			result.append(String.format(format, ipOrDns, port));
			if(relayNodeCount-1 != i) {
				result.append(NodeConstants.BLANK_SPACE);
			}
		}
		
		return result.toString();
	}
	
}

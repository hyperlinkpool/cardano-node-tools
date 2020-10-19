package kr.hyperlinkpool.constants;

public enum NodeInfo {
	IP("ip")
	,DNS("dns")
	;
	
	private String nodeInfo;
	
	NodeInfo(String nodeInfo) {
		this.setNodeInfo(nodeInfo);
	}

	public String getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(String nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
}

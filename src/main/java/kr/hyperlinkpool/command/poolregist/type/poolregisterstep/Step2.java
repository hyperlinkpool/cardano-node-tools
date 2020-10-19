package kr.hyperlinkpool.command.poolregist.type.poolregisterstep;

import java.io.File;

import kr.hyperlinkpool.command.interfaces.Ordered;
import kr.hyperlinkpool.command.poolregist.type.poolregisterstep.domains.PoolRegisterDomain;
import kr.hyperlinkpool.command.poolregist.type.poolregisterstep.interfaces.PoolRegisterResult;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.constants.NodeInfo;
import kr.hyperlinkpool.constants.StepOrder;
import kr.hyperlinkpool.domain.ProcessResultDomain;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;
import kr.hyperlinkpool.utils.CommandListener;
import kr.hyperlinkpool.utils.CommonUtils;
import kr.hyperlinkpool.utils.MessagePrompter;

public class Step2 implements PoolRegisterResult, Ordered, Runnable{

	private PoolRegisterDomain poolRegisterDomain;
	
	private ProcessResultDomain<PoolRegisterDomain> result;
	
	@Override
	public int getOrder() {
		return StepOrder.STEP2.getStepOrder();
	}

	@Override
	public ProcessResultDomain<PoolRegisterDomain> getResult() {
		return result;
	}

	@Override
	public void setResult(ProcessResultDomain<PoolRegisterDomain> result) {
		this.result = result;
		this.setPoolRegisterDomain(result.getResponseData());
	}

	public PoolRegisterDomain getPoolRegisterDomain() {
		return poolRegisterDomain;
	}

	public void setPoolRegisterDomain(PoolRegisterDomain poolRegisterDomain) {
		this.poolRegisterDomain = poolRegisterDomain;
	}
	
	@Override
	public void run() {
		/**
		 * Step. 2
		 * Stake pool registration certificate 생성
		 */
		/**
		 * cold.vkey 검사
		 */
		String cardanoKeysFolderString = NodeProperties.getString("cardano.keys.folder");
		String cardanoKeysColdVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.cold.vkey");
		File cardanoKeysColdVkeyFile = new File(cardanoKeysColdVkeyFileString);
		if(!cardanoKeysColdVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Cold Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00061"), true);
			return;
		}
		
		/**
		 * vrf.vkey 검사
		 */
		String cardanoKeysVrfVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.vrf.vkey");
		File cardanoKeysVrfVkeyFile = new File(cardanoKeysVrfVkeyFileString);
		if(!cardanoKeysVrfVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("VRF Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00062"), true);
			return;
		}
		
		/**
		 * stake.vkey 검사
		 */
		String cardanoKeysStakeVkeyFileString = cardanoKeysFolderString + NodeConstants.PATH_DELIMITER + NodeProperties.getString("cardano.keys.stake.vkey");
		File cardanoKeysStakeVkeyFile = new File(cardanoKeysStakeVkeyFileString);
		if(!cardanoKeysStakeVkeyFile.exists()) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.", "M00041"), true);
			return;
		}
		
		boolean mainLoop = true;
		while(mainLoop) {
			/**
			 * Pool pledge
			 */
			long poolPledge = 0L;
			String inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool 보증금을 입력해 주세요. (단위 : lovelace, 1 ADA = 1,000,000 lovelace). [Q : 취소] : ", "M00063"), false);
			if("Q".equalsIgnoreCase(inputValue)) {
				poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setSuccess(false);
				return;
			}
			try {
				poolPledge = Long.parseLong(inputValue);
				poolRegisterDomain.setPoolPledge(poolPledge);
			} catch (Exception e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("입력값이 잘못되었습니다. 다시 입력하세요.", "M00064"), true);
				continue;
			}
			
			/**
			 * Pool cost
			 */
			long poolCost = 0L;
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool 고정 비용을 입력해 주세요. (단위 : lovelace, 1 ADA = 1,000,000 lovelace). [Q : 취소] : ", "M00065"), false);
			if("Q".equalsIgnoreCase(inputValue)) {
				poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setSuccess(false);
				return;
			}
			
			try {
				poolCost = Long.parseLong(inputValue);
				if(NodeConstants.POOL_COST_MINIMUM > poolCost) {
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Cost는 최소 %s 이상이어야 합니다.", "M00066", (NodeConstants.POOL_COST_MINIMUM + NodeConstants.POSTFIX_LOVELACE)), true);
					continue;
				}
				
				poolRegisterDomain.setPoolCost(poolCost);
			} catch (Exception e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("입력값이 잘못되었습니다. 다시 입력하세요.", "M00064"), true);
				continue;
			}
			
			/**
			 * Pool margin
			 */
			double poolMargin = 0.0D;
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Pool Margin을 입력해 주세요. (단위 : 100분위 소수점, 예: 3% => 0.03 으로 입력). [Q : 취소] : ", "M00067"), false);
			if("Q".equalsIgnoreCase(inputValue)) {
				poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setSuccess(false);
				return;
			}
			
			try {
				poolMargin = Double.parseDouble(inputValue);
				if(poolMargin > 1.0D) {
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("1이하로 입력해야 합니다.", "M00068"), true);
					continue;
				}
				
				poolRegisterDomain.setPoolMargin(poolMargin);
			} catch (Exception e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("입력값이 잘못되었습니다. 다시 입력하세요.", "M00064"), true);
				continue;
			}
			
			/**
			 * Relay node
			 */
			int relayNodeCount = 0;
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("Relay Node 개수를 입력해 주세요. ( 단위 : 정수 1 이상 ) [Q : 취소] : ", "M00069"), false);
			if("Q".equalsIgnoreCase(inputValue)) {
				poolRegisterDomain.setNextOrder(StepOrder.EXIT.getStepOrder());
				result.setSuccess(false);
				return;
			}
			
			try {
				relayNodeCount = Integer.parseInt(inputValue);
				if(relayNodeCount < 1) {
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("입력값이 잘못되었습니다. 다시 입력하세요.", "M00064"), true);
					continue;
				}
				
				poolRegisterDomain.setRelayNodeCount(relayNodeCount);
			} catch (Exception e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("입력값이 잘못되었습니다. 다시 입력하세요.", "M00064"), true);
				continue;
			}
			
			String[][] relayNodesInfo = new String[relayNodeCount][3];
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Relay Node 정보를 입력하세요.", "M00070"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("(예. 도메인네임:Port => relay.hyperlinkpool.kr:6000)", "M00071"), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("(예. xxx.xxx.xxx.xxx:Port => 123.123.123.123:6000)", "M00072"), true);
			boolean isCorrectNodeInfo = true;
			for(int i=0;i<relayNodeCount;i++) {
				inputValue = CommandListener.getInstance().listenCommand("Relay Node " + (i+1) + " : ", false);
				String[] nodeInfos = inputValue.split(NodeConstants.PORT_DELIMITER);
				if(nodeInfos.length != 2) {
					MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("예시의 형태대로 입력해 주세요.", "M00073"), true);
					isCorrectNodeInfo = false;
					break;
				}
				
				String ipOrDns = nodeInfos[0];
				int port = Integer.parseInt(nodeInfos[1]);
				/**
				 * IP가 아니면 DNS라고 판단
				 */
				if(CommonUtils.ip4Validation(ipOrDns)) {
					relayNodesInfo[i][0] = NodeInfo.IP.getNodeInfo();
				}else {
					relayNodesInfo[i][0] = NodeInfo.DNS.getNodeInfo();
				}
				
				relayNodesInfo[i][1] = ipOrDns;
				relayNodesInfo[i][2] = String.valueOf(port);
			}
			
			for(int i=0;i<relayNodeCount;i++) {
				String ipOrDns = relayNodesInfo[i][1];
				String port = relayNodesInfo[i][2];
				String hostInfo = ipOrDns+":"+port;
				int dupCount = 0;
				for(int j=0;j<relayNodeCount;j++) {
					String compareIpOrDns = relayNodesInfo[j][1];
					String comparePort = relayNodesInfo[j][2];
					String compareHostInfo = compareIpOrDns+":"+comparePort;
					if(hostInfo.equals(compareHostInfo)) {
						dupCount ++;
					}
					
					if(dupCount > 1) {
						MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Relay Node 정보가 중복되었습니다. 다시 입력하세요.", "M00074"), true);
						isCorrectNodeInfo = false;
						break;
					}
				}
				if(!isCorrectNodeInfo) {
					break;
				}
			}
			
			if(!isCorrectNodeInfo) {
				continue;
			}
			
			poolRegisterDomain.setRelayNodesInfo(relayNodesInfo);
			
			MessagePrompter.promptMessage("*******************************************************************************************************************", true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 보증금 : %s", "M00075",String.valueOf(poolRegisterDomain.getPoolPledge()) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool 고정 비용 : %s", "M00076",String.valueOf(poolRegisterDomain.getPoolCost()) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Pool Margin : %s", "M00077",String.valueOf(poolRegisterDomain.getPoolMargin())), true);
			for(int i=0;i<relayNodeCount;i++) {
				String relayNodeFormat = "Relay Node %s : %s:%s"; 
				MessagePrompter.promptMessage(String.format(relayNodeFormat, String.valueOf(i+1), relayNodesInfo[i][1], relayNodesInfo[i][2]), true);
				
			}
			MessagePrompter.promptMessage("*******************************************************************************************************************", true);
			inputValue = CommandListener.getInstance().listenCommand(MessageFactory.getInstance().getMessage("위에 입력한 정보가 맞습니까? (Y/n) : ", "M00057"), false);
			if("Y".equalsIgnoreCase(inputValue)) {
				poolRegisterDomain.setNextOrder(this.getOrder()+1);
				result.setSuccess(true);
				mainLoop = false;
			}
		}
	}
}
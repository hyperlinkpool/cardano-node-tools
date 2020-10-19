package kr.hyperlinkpool.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.hyperlinkpool.command.NodeCommandFormats;
import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.domain.ProcessResponse;
import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.properties.NodeProperties;

public class CommandExecutor {

	private static Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
	
	public synchronized static ProcessResponse executeCommandWithBash(String[] commands) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		InputStream inputStream = null;
		InputStream errorInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		byte[] buffer = new byte[4096];
		String result = null;
		
		ProcessResponse processResponse = new ProcessResponse();
		try {
			process = runtime.exec(commands);
			inputStream = process.getInputStream();
			
			byteArrayOutputStream = new ByteArrayOutputStream();
			while(inputStream.read(buffer) != -1) {
				byteArrayOutputStream.write(buffer);
			}
			
			result = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
			processResponse.setSuccessResultString(result);
			
			buffer = new byte[4096];
			byteArrayOutputStream = new ByteArrayOutputStream();
			errorInputStream = process.getErrorStream();
			while(errorInputStream.read(buffer) != -1) {
				byteArrayOutputStream.write(buffer);
			}
			result = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
			processResponse.setFailureResultString(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(errorInputStream != null) {
					errorInputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
				if(byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return processResponse;
	}
	
	public synchronized static ProcessResponse executeCommand(String command, String ... arguments) {
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		InputStream inputStream = null;
		InputStream errorInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		byte[] buffer = new byte[4096];
		String result = null;
		
		ProcessResponse processResponse = new ProcessResponse();
		try {
			String[] splitCommands = command.split(NodeConstants.BLANK_SPACE);
			process = runtime.exec(splitCommands);
			inputStream = process.getInputStream();
			
			byteArrayOutputStream = new ByteArrayOutputStream();
			while(inputStream.read(buffer) != -1) {
				byteArrayOutputStream.write(buffer);
			}
			
			result = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
			processResponse.setSuccessResultString(result);
			
			buffer = new byte[4096];
			byteArrayOutputStream = new ByteArrayOutputStream();
			errorInputStream = process.getErrorStream();
			while(errorInputStream.read(buffer) != -1) {
				byteArrayOutputStream.write(buffer);
			}
			result = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
			processResponse.setFailureResultString(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(errorInputStream != null) {
					errorInputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
				if(byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return processResponse;
	}
	
	
	public synchronized static ProcessResponse executeCommand(ProcessBuilder processBuilder, String ... arguments) {
		Process process = null;
		InputStream inputStream = null;
		InputStream errorInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		byte[] buffer = new byte[4096];
		String result = null;
		
		ProcessResponse processResponse = new ProcessResponse();
		try {
			process = processBuilder.start();
			inputStream = process.getInputStream();
			
			byteArrayOutputStream = new ByteArrayOutputStream();
			while(inputStream.read(buffer) != -1) {
				byteArrayOutputStream.write(buffer);
			}
			
			result = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
			processResponse.setSuccessResultString(result);
			
			buffer = new byte[4096];
			byteArrayOutputStream = new ByteArrayOutputStream();
			errorInputStream = process.getErrorStream();
			while(errorInputStream.read(buffer) != -1) {
				byteArrayOutputStream.write(buffer);
			}
			result = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
			processResponse.setFailureResultString(result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(errorInputStream != null) {
					errorInputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
				if(byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return processResponse;
	}
	
	public static ProcessResponse initializeProcessBuilderWithBash(String command) {
		String[] bashCommands = new String[3]; 
		bashCommands[0] = "/bin/sh";
		bashCommands[1] = "-c";
		bashCommands[2] = command;
		boolean commandDebug = NodeProperties.getBoolean("command.debug.mode");
		if(commandDebug) {
			MessagePrompter.promptMessage("command : " + command, true);
		}else {
			logger.info("command : " + command);
		}
		
		ProcessResponse processResponse = CommandExecutor.executeCommandWithBash(bashCommands);
		
		/**
		 * Pre validation
		 */
		if(processResponse.getFailureResultString().indexOf("cardano-cli: Network.Socket.connect") > -1) {
			MessagePrompter.promptMessage("Cardano 로컬 Node가 중지 상태 또는 동기화중입니다. Cardano Node 동기화 완료 후 실행하세요.", true);
		}
		
		return processResponse;
	}
	
	public static ProcessResponse initializeProcessBuilder(String commandString) {
		String cardanoCliPath = NodeProperties.getString("cardano.cli.path");
		boolean commandDebug = NodeProperties.getBoolean("command.debug.mode");
		if(commandDebug) {
			MessagePrompter.promptMessage("command : " + commandString, true);
		}else {
			logger.info("command : " + commandString);
		}
		
		String[] commandArray = commandString.split(NodeConstants.BLANK_SPACE);
		List<String> commands = Arrays.asList(commandArray);
		ProcessBuilder processBuilder = new ProcessBuilder(commands);
		processBuilder.directory(new File(cardanoCliPath));
		processBuilder.environment().put(NodeProperties.getString("os.ld.library.path.key"), NodeProperties.getString("os.ld.library.path.value"));
		processBuilder.environment().put(NodeProperties.getString("os.pkg.config.path.key"), NodeProperties.getString("os.pkg.config.path.value"));
		processBuilder.environment().put(NodeProperties.getString("cardano.node.socket.path.key"), NodeProperties.getString("cardano.node.socket.path.value"));
		ProcessResponse processResponse = CommandExecutor.executeCommand(processBuilder);
		
		/**
		 * Pre validation
		 */
		if(processResponse.getFailureResultString().indexOf("cardano-cli: Network.Socket.connect") > -1) {
			MessagePrompter.promptMessage("Cardano 로컬 Node가 중지 상태 또는 동기화중입니다. Cardano Node 동기화 완료 후 실행하세요.", true);
		}
		
		return processResponse;
	}
	
	public static String generateCommand(String command, Object ... arguments) {
		String format = String.format(command, arguments);
		return format;
	}
	
	public static String readFile(String filePath) {
		File file = new File(filePath);
		return CommandExecutor.readFile(file);
	}
	
	public static String readFile(File file) {
		String fileContents = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String buffer = null;
			while((buffer = bufferedReader.readLine()) != null) {
				stringBuffer.append(buffer);
			}
			fileContents = stringBuffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
				if(fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return fileContents.trim();
	}
	
	public static File writeFile(String contents, String folderName, String fileName) {
		File folder = new File(folderName);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		File writeFile = new File(fileName);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(writeFile);
			fileWriter.write(contents.trim());
		} catch (IOException e) {
			writeFile = null;
			e.printStackTrace();
		} finally {
			try {
				if(fileWriter != null) {
					fileWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return writeFile;
	}
	
	public static String mainnetCurrentTip() {
		String cardanoCliCommand = NodeProperties.getString("cardano.cli.name");; 
		String command = CommandExecutor.generateCommand(NodeCommandFormats.MAINNET_CURRENT_TIP, cardanoCliCommand);
		ProcessResponse processResponse = CommandExecutor.initializeProcessBuilder(command);
		return processResponse.getSuccessResultString();
	}
	
	public static List<Map<String, String>> parseTxHashString(String txHashString){
		List<Map<String, String>> txHashList = null;
		if(txHashString.indexOf("TxHash") > -1) {
			String[] split = txHashString.split(System.lineSeparator());
			txHashList = new ArrayList<Map<String, String>>();
			for(int i=2; i<split.length;i++) {
				String tx = split[i];
				if(tx.trim().length() > 0) {
					String[] txOneSplit = tx.split(" ");
					Map<String, String> txHashMap = new HashMap<String, String>();
					short count = 0;
					for(String element : txOneSplit) {
						if(element.trim().length() > 0 && count == 2) {
							txHashMap.put("LoveLace", element);
							count++;
						}
						
						if(element.trim().length() > 0 && count == 1) {
							txHashMap.put("TxIx", element);
							count++;
						}
						
						if(element.trim().length() > 0 && count == 0) {
							txHashMap.put("TxHash", element);
							count++;
						}
					}
					count = 0;
					txHashList.add(txHashMap);
				}
			}			
		}
		return txHashList;
	}
	
	public static long sumLoveLaceOnTxList(List<Map<String, String>> txList) {
		Iterator<Map<String, String>> txIterator = txList.iterator();
		
		long sumLovelace = 0;
		while (txIterator.hasNext()) {
			Map<String, String> txData = (Map<String, String>) txIterator.next();
			sumLovelace += Long.parseLong(txData.get("LoveLace").toString());
		}
		
		return sumLovelace;
	}
	
	public static String getTxInDataOnTxList(List<Map<String, String>> txList) {
		Iterator<Map<String, String>> txIterator = txList.iterator();
		StringBuffer stringBuffer = new StringBuffer();
		while (txIterator.hasNext()) {
			Map<String, String> txData = (Map<String, String>) txIterator.next();
			String TxHash = txData.get("TxHash").toString();
			String TxIx = txData.get("TxIx").toString();
			stringBuffer.append(" --tx-in ").append(TxHash).append("#").append(TxIx);
		}
		return stringBuffer.toString();
	}
	
	public static String firstBlankSpaceRemove(String source) {
		/**
		 * 첫번째 공백 제거
		 */
		String target = null;
		target = source.substring(1, source.length());
		return target;
	}
	
	public static String getTxOutData(String receiveAddress, long sendLovelace) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(" --tx-out ").append(receiveAddress).append("+").append(String.valueOf(sendLovelace));
		return stringBuffer.toString();
	}

	public static void pringStakePoolStatus(JSONArray rewardsData) {
		int length = rewardsData.length();
		MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("Stake Pool 주소 개수 : %s", "M00147", rewardsData.length()), true);
		MessagePrompter.promptMessage("=========================================================================================", true);
		for(int i=0;i<length;i++) {
			JSONObject stakePoolInfoJsonObject = rewardsData.getJSONObject(i);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("     Stake Pool 주소 : %s", "M00148", stakePoolInfoJsonObject.getString("address")), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("%3s  Stake Pool ID : %s", "M00149", String.valueOf(i+1), stakePoolInfoJsonObject.getString("delegation")), true);
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("     Rewards Balance : %s", "M00150", String.valueOf(stakePoolInfoJsonObject.getLong("rewardAccountBalance")) + NodeConstants.POSTFIX_LOVELACE), true);
			MessagePrompter.promptMessage("=========================================================================================", true);
		}
	}

	public static long[] parseValueNotConservedUTxO(String failureResultString) {
		int valueNotConservedUTxOIndexOf = failureResultString.indexOf("(ValueNotConservedUTxO");
		String valueNotConservedUTxOString = failureResultString.substring(valueNotConservedUTxOIndexOf);
		String valueString = valueNotConservedUTxOString.substring(0, valueNotConservedUTxOString.indexOf("))")+2);
		String removedBracket = valueString.replaceAll("\\(", "").replaceAll("\\)", "");
		String[] datas = removedBracket.split(NodeConstants.BLANK_SPACE);
		long[] results = new long[2];
		if(datas.length == 5) {
			results[0] = Long.parseLong(datas[2]);
			results[1] = Long.parseLong(datas[4]);
		}
		return results;
	}
}
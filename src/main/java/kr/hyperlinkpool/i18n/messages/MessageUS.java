package kr.hyperlinkpool.i18n.messages;

import java.util.HashMap;
import java.util.Map;

public class MessageUS {
	public static Map<String, String> messageFormats = null;
	static {
		messageFormats = new HashMap<String, String>();
		messageFormats.put("M00001", "The stake address was not created. Please create an address and try again.");
		messageFormats.put("M00002", "The stake key is not registered on the blockchain. After registering the key, please try again.");
		messageFormats.put("M00003", "(Main Menu -> No. 3 -> No. 1)");
		messageFormats.put("M00004", "Current rewards : %s");
		messageFormats.put("M00005", "There are no rewards that can be withdrawn.");
		messageFormats.put("M00006", "There is not transaction hash.");
		messageFormats.put("M00007", "Rewards that can be withdrawn: %s");
		messageFormats.put("M00008", "Please enter the quantity to transfer. (Unit: lovelace, 1 ADA = 1,000,000 lovelace, Q : cancel): ");
		messageFormats.put("M00009", "Insufficient quantity. Please enter again. Withdrawable quantity: %s, Input quantity: %s");
		messageFormats.put("M00010", "An error occurred while executing the process. :");
		messageFormats.put("M00011", "Please enter a number.");
		messageFormats.put("M00012", "Amount held: %s, amount withdrawn for compensation: %s, fee: %s");
		messageFormats.put("M00013", "Remaining compensation amount after withdrawal: %s");
		messageFormats.put("M00014", "Total amount held after withdrawal: %s");
		messageFormats.put("M00015", "Do you want to withdraw? (Y/n) : ");
		messageFormats.put("M00016", "The calculated fee is less than the actual transmission required. Do you want to manually enter it? (Y/n) : ");
		messageFormats.put("M00017", "Please enter the fee shown on the screen. (Q: Cancel): ");
		messageFormats.put("M00018", "The fee is too high. Please enter again. Fee allowance: %s");
		messageFormats.put("M00019", "Please enter again.");
		messageFormats.put("M00020", "Cancelled.");
		messageFormats.put("M00021", "Transfer failed.");
		messageFormats.put("M00022", "Transfered.");
		messageFormats.put("M00023", "(99 : Go Back) : ");
		messageFormats.put("M00024", "This is not a valid address.");
		messageFormats.put("M00025", "Payment address does not exist. Please create a payment address and try again.");
		messageFormats.put("M00026", "Current Pool Address : ");
		messageFormats.put("M00027", "There is no ADA to withdraw.");
		messageFormats.put("M00028", "Current holding quantity : %s");
		messageFormats.put("M00029", "Please enter the address you want to send (99 : Go Back) : ");
		messageFormats.put("M00030", "If sending to the current pool address, only the sum total can be sent. The total amount excluding the transfer fee will be sent.");
		messageFormats.put("M00031", "Do you want to proceed? (Y/n) : ");
		messageFormats.put("M00032", "1. If the sum of the transmission quantity and the fee is greater than the holding quantity,");
		messageFormats.put("M00033", "2. When the transmission quantity and the holding quantity are the same,");
		messageFormats.put("M00034", "We will transfer the rest of the holding quantity minus the fee.");
		messageFormats.put("M00035", "Retention quantity: %s, transfer quantity: %s, commission: %s");
		messageFormats.put("M00036", "Sender address : %s");
		messageFormats.put("M00037", "Receiver address : %s");
		messageFormats.put("M00038", "Do you want to transfer? (Y/n) : ");
		messageFormats.put("M00039", "The file already exists. Delete it and try again.");
		messageFormats.put("M00040", "Payment Verification Key was not created. Please generate the key and try again.");
		messageFormats.put("M00041", "The Stake Verification Key was not created. Please generate the key and try again.");
		messageFormats.put("M00042", "It was processed normally.");
		messageFormats.put("M00043", "The KES Verification Key is not generated. Please generate the key and try again.");
		messageFormats.put("M00044", "Cold Signing Key is not generated. Please generate the key and try again.");
		messageFormats.put("M00045", "Cold Counter is not created. Please create a key and try again.");
		messageFormats.put("M00046", "The mainnet-shelley-genesis.json file does not exist. Please check and try again.");
		messageFormats.put("M00047", "Please enter the Internet address to download Metadata JSON file. (Q : cancel): ");
		messageFormats.put("M00048", "Invalid URL format. Please enter again.");
		messageFormats.put("M00049", "The URL must be 64 Bytes (64 characters) or less including English, numbers, and symbols. Please use the Git Shorten URL. https://git.io");
		messageFormats.put("M00050", "Incorrect JSON file format. Please check the JSON file.");
		messageFormats.put("M00051", "This is a JSON file format without Pool Meta information (Pool name, Pool description, Pool ticker, etc.). Please check the URL again.");
		messageFormats.put("M00052", "Pool name:");
		messageFormats.put("M00053", "Pool description:");
		messageFormats.put("M00054", "Pool ticker : ");
		messageFormats.put("M00055", "Pool PR website address:");
		messageFormats.put("M00056", "Optional) extended url to be registered on adapools.org: ");
		messageFormats.put("M00057", "Is the information entered above correct? (Y/n) : ");
		messageFormats.put("M00058", "An error occurred while saving the MetaData file.");
		messageFormats.put("M00059", "MetaData file size must be less than 512 bytes. (Current size: %s bytes)");
		messageFormats.put("M00060", "An error occurred while generating hash of the MetaData file.");
		messageFormats.put("M00061", "The Cold Verification Key was not created. Please generate the key and try again.");
		messageFormats.put("M00062", "VRF Verification Key was not created. Please generate the key and try again.");
		messageFormats.put("M00063", "Please enter the pool deposit. (Unit: lovelace, 1 ADA = 1,000,000 lovelace). [Q : cancel]: ");
		messageFormats.put("M00064", "The input value is incorrect. Please enter it again.");
		messageFormats.put("M00065", "Please enter a fixed pool fee. (Unit: lovelace, 1 ADA = 1,000,000 lovelace). [Q : cancel]: ");
		messageFormats.put("M00066", "Pool Cost must be at least %s.");
		messageFormats.put("M00067", "Please enter the Pool Margin. (Unit: 100th decimal point, eg: 3%% => Enter 0.03). [Q : cancel]: ");
		messageFormats.put("M00068", "You must enter 1 or less.");
		messageFormats.put("M00069", "Please enter the number of Relay Nodes. (Unit: integer 1 or more) [Q : cancel]: ");
		messageFormats.put("M00070", "Please enter the relay node information.");
		messageFormats.put("M00071", "(Ex. domain name:Port => relay.hyperlinkpool.kr:6000)");
		messageFormats.put("M00072", "(Ex. xxx.xxx.xxx.xxx:Port => 123.123.123.123:6000)");
		messageFormats.put("M00073", "Please enter in the form of the example.");
		messageFormats.put("M00074", "The Relay Node information is duplicated. Please enter it again.");
		messageFormats.put("M00075", "Pool deposit: %s");
		messageFormats.put("M00076", "Pool fixed cost: %s");
		messageFormats.put("M00077", "Pool Margin : %s");
		messageFormats.put("M00078", "An error occurred while generating the pool registration certificate.");
		messageFormats.put("M00079", "An error occurred while generating the Pool Delegation certificate.");
		messageFormats.put("M00080", "The Pool Registration certificate was not created. Please generate the certificate and try again.");
		messageFormats.put("M00081", "The Pool Delegation certificate was not generated. Please generate the certificate and try again.");
		messageFormats.put("M00082", "Payment Signing Key was not created. Please generate the key and try again.");
		messageFormats.put("M00083", "The Stake Signing Key is not generated. Please generate the key and try again.");
		messageFormats.put("M00084", "An error occurred while checking Pool Id.");
		messageFormats.put("M00085", "I'm checking the pool information. It's taking a while.");
		messageFormats.put("M00086", "An error occurred while checking the Blockchain.");
		messageFormats.put("M00087", "You don't have enough ADA holdings.");
		messageFormats.put("M00088", "ADA Current Holdings: %s");
		messageFormats.put("M00089", "Pool Register required: %s");
		messageFormats.put("M00090", "Transaction fee: %s");
		messageFormats.put("M00091", "Do you want to register pool information? (Y/n) : ");
		messageFormats.put("M00092", "Do you want to modify the pool information? (Y/n) : ");
		messageFormats.put("M00093", "Estimated amount remaining after completion : ");
		messageFormats.put("M00094", "Pool registration failed.");
		messageFormats.put("M00095", "Registered.");
		messageFormats.put("M00096", "The Stake Verification Key was not created. Please try after creating the key file.");
		messageFormats.put("M00097", "The Stake Certificate was not created. Please try after generating the certificate.");
		messageFormats.put("M00098", "The Stake Key is already registered.");
		messageFormats.put("M00099", "Stake Key Register Required: %s");
		messageFormats.put("M00100", "Estimated amount remaining after processing: %s");
		messageFormats.put("M00101", "Would you like to register Stake Key? (Y/n) : ");
		messageFormats.put("M00102", "Failed to register Stake Key.");
		messageFormats.put("M00103", "Proceeds to create MetaData for pool registration.");
		messageFormats.put("M00104", "Example) [HYPER] Pool");
		messageFormats.put("M00105", "Pool name: Hyperlink Pool");
		messageFormats.put("M00106", "Description of the pool: We prepare for the era of hyperconnectivity.");
		messageFormats.put("M00107", "Pool Ticker (up to 5 characters in English): HYPER");
		messageFormats.put("M00108", "Pool PR website address: https://twitter.com/HYPERLINKPOOL");
		messageFormats.put("M00109", "Extended url to be registered on adapools.org. Optional. If not, enter enter key. (Reference format https://a.adapools.org/extended-example): https://git.io/JUMjN");
		messageFormats.put("M00110", "Pool Ticker (up to 5 characters in English):");
		messageFormats.put("M00111", "Please enter within 5 alphanumeric characters.");
		messageFormats.put("M00112", "Pool PR website address:");
		messageFormats.put("M00113", "Optional) The extended url to be registered on adapools.org. : ");
		messageFormats.put("M00114", "Do you want to continue creating MetaData? (Y/n) : ");
		messageFormats.put("M00115", "After uploading the Json Contents above as a file so that you can download it in an Internet environment, remember the URL to download the uploaded file. (Recommend : Github GIST)");
		messageFormats.put("M00116", "It's not a Core Node. Run it on a Core Node.");
		messageFormats.put("M00117", "* KES Key is required to sign the Block. Pool Operators must renew the KES Key before expiration of the KES Key Periods.");
		messageFormats.put("M00118", "* If you do not renew the KES Key, you will not be able to sign blocks created after expiration of the KES periods, so please be sure to renew it when the periods of expiration arrives.");
		messageFormats.put("M00119", "------------------------------ KES Key Information ------------------------------ ");
		messageFormats.put("M00120", "- Periods that created the KES Key: ");
		messageFormats.put("M00121", "- Periods when KES Key expires: ");
		messageFormats.put("M00122", "- KES Key Current Periods:");
		messageFormats.put("M00123", "- KES Key Remaining Periods: ");
		messageFormats.put("M00124", "- About %s %% of KES Key Periods remain.");
		messageFormats.put("M00125", "Do you want to proceed with KES Key Rotation?.(Y/n) : ");
		messageFormats.put("M00126", "We have deleted the existing KES Verification Key, KES, and Signing Key.");
		messageFormats.put("M00127", "New KES Verification Key, KES Signing Key have been created.");
		messageFormats.put("M00128", "The Node Certificate file with the new KES Period applied has been created.");
		messageFormats.put("M00129", "After copying the new KES Signing Key and Node Certificate file to the Block Producer node Config path, restart Block Producer.");
		messageFormats.put("M00130", "New KES Signing Key:");
		messageFormats.put("M00131", "New Node Certificate:");
		messageFormats.put("M00132", "Pool information has been verified. It is in a normal state.");
		messageFormats.put("M00133", "Pool properties could not be found.");
		messageFormats.put("M00134", "Application has shutdowned");
		messageFormats.put("M00135", "\n"
				+ "1. Balance check of the current wallet\n"
				+ "2. Balance check of another wallet\n"
				+ "3. Rewards balance check of the current wallet\n"
				+ "4. Rewards balance check of another wallet\n"
				+ "5. Withdrawal from the current wallet to another wallet\n"
				+ "6. Rewards withdrawal to the current wallet\n"
				+ "99. Go Back");
		messageFormats.put("M00136", "Enter the number,and press Enter. :");
		messageFormats.put("M00137", "\n"
				+ "1. Create Payment Key Pair\n"
				+ "2. Create Stake Key Pair\n"
				+ "3. Create Payment Address\n"
				+ "4. Create Stake Address\n"
				+ "5. Create Stake Pool Cold Keys & Cold Counter\n"
				+ "6. Create Stake Pool VRF Key Pair\n"
				+ "7. Create Stake Pool KES Key Pair\n"
				+ "8. Create Node Operation Certificate\n"
				+ "99. Go Back");
		messageFormats.put("M00138", "\n"
				+ "1. Register stake key(It requires 2 ADA at deposit.(October 9, 2020), and it's required a few transaction fee.)\n"
				+ "2. Withdrawal of Stake Key(You can get back the 2ADA(October 9, 2020) registered as a deposit.)\n"
				+ "3. Delegate Stake Key to Pool\n"
				+ "4. Metadata JSON file creation\n"
				+ "5. Pool information registration or update (You must register stake key at first, and it takes 500 ADA at deposit.(October 9, 2020). After that, it will be charged only transaction fee at modification.)\n"
				+ "6. Withdrawal of Pool (You can get back the 500ADA(October 9, 2020) registered as a deposit.)\n"
				+ "7. Check the status of pool registration\n"
				+ "8. KES Key Rotation\n"
				+ "99. Go Back");
		messageFormats.put("M00139", "\n"
				+ "1. Create Key & Certificate\n"
				+ "2. ADA Balance Check, Withdraw ADA\n"
				+ "3. Register pool / Manage pool\n"
				+ "99. Exit");
		messageFormats.put("M00140", "The input value is invalid.");
		messageFormats.put("M00141", "This is not in URL format.");
		messageFormats.put("M00142", "An error occurred during communication.");
		messageFormats.put("M00143", "- It makes the pool operation a little easier. It is still a beta version. There may be bugs in use.\n");
		messageFormats.put("M00144", "- We are preparing the web version. Please give me strength.\n");
		messageFormats.put("M00145", "- Disclaimer 1: Please note that the creators are not responsible for any matters caused by incorrect use of this program.\n");
		messageFormats.put("M00146", "- Disclaimer 2: This program is freeware. Therefore, there is no compulsory use, and modification, distribution, and use are also free, and the user is responsible for this.\n");
		messageFormats.put("M00147", "Stake Pool address count : %s");
		messageFormats.put("M00148", "     Stake Key Address : %s");
		messageFormats.put("M00149", "%3s  Delegated Pool ID : %s");
		messageFormats.put("M00150", "     Rewards Balance : %s");
		messageFormats.put("M00151", "There is not enough ADA balance in your current account. Please make a deposit of about 100 ADA and try again.");
		messageFormats.put("M00152", "It may takes some time to synchronize your balance.");
		messageFormats.put("M00153", "- Do not support TestNet.");
		messageFormats.put("M00154", "Cardano local node is stopped or synchronizing.Please run after completing Cardano Node synchronization.");
		messageFormats.put("M00155", "Stake Key is not registered. You can withdraw after registration.");
		messageFormats.put("M00156", "It's not delegated.");
		messageFormats.put("M00157", "Would you like to withdraw your Stake Key? Note) If you withdraw, you will not be able to receive the delegation rewards.(Y/n) : ");
		messageFormats.put("M00158", "It was withdrawn.");
		messageFormats.put("M00159", "It was delegated to the pool. If you withdraw, you will have to delegate again. Would you continue?(Y/n) : ");
		messageFormats.put("M00160", "The Stake Key is not registered. You can delegate after registration.");
		messageFormats.put("M00161", "Enter the ID of the Pool to be delegated. (Q: Cancel):");
		messageFormats.put("M00162", "Pool ID can only be in hexadecimal format. Ex) [HYPER] = 263498e010c7a49bbfd7c4e1aab29809fca7ed993f9e14192a75871e");
		messageFormats.put("M00163", "Do you want to delegate Stake Key?(Y/n) : ");
		messageFormats.put("M00164", "Pool ID : ");
		messageFormats.put("M00165", "It is delegated to pool. Do you want to move another pool?(Y/n) : ");
		messageFormats.put("M00166", "It was delegated.");
		messageFormats.put("M00167", "Please enter the Epoch to retire.（range:%s ~ %s, epoch of present : %s）[Q : cancel] : ");
		messageFormats.put("M00168", "It exceeds the input range. Please re-enter.");
		messageFormats.put("M00169", "Are you sure of retirement of this pool? (Y / n) : ");
		messageFormats.put("M00170", "Epoch of retirement : %s");
		messageFormats.put("M00171", "After retiring of pool, It will be return ADA deposit : %s");
		messageFormats.put("M00172", "Failed to register for pool retirement.");
		messageFormats.put("M00173", "Please enter a wallet address.(ex. Ddz... , Ae2... , addr1...)");
		messageFormats.put("M00174", "Please enter a stake address.(ex. stake1... )");
		messageFormats.put("E99999", "It is under development.");
	}
}
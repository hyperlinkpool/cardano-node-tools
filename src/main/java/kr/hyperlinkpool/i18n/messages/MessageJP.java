package kr.hyperlinkpool.i18n.messages;

import java.util.HashMap;
import java.util.Map;

public class MessageJP {
	public static Map<String, String> messageFormats = null;
	static {
		messageFormats = new HashMap<String, String>();
		messageFormats.put("M00001", "Stake 주소가 생성되지 않았습니다. アドレスを作成した後やり直してください。");
		messageFormats.put("M00002", "Stake Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00003", "(메인 메뉴 -> 3번 -> 1번 선택)");
		messageFormats.put("M00004", "현재 보상량 : %s");
		messageFormats.put("M00005", "인출할 수 있는 보상이 없습니다.");
		messageFormats.put("M00006", "TxHash가 존재하지 않습니다.");
		messageFormats.put("M00007", "인출 가능한 Rewards 수량 : %s");
		messageFormats.put("M00008", "전송할 수량을 입력해 주세요.(입력 단위 : lovelace, 1 ADA = 1,000,000 lovelace, 취소 : Q) : ");
		messageFormats.put("M00009", "수량이 부족합니다. 다시 입력하세요. 인출 가능 수량 : %s , 입력수량 : %s");
		messageFormats.put("M00010", "프로세스 수행중 오류가 발생했습니다. : ");
		messageFormats.put("M00011", "숫자를 입력해 주세요.");
		messageFormats.put("M00012", "보유수량 : %s , 보상분 인출 수량 : %s , 수수료 : %s");
		messageFormats.put("M00013", "인출 후 잔여 보상 수량 : %s");
		messageFormats.put("M00014", "인출 후 총 보유 수량 : %s");
		messageFormats.put("M00015", "인출하시겠습니까? (Y/n) : ");
		messageFormats.put("M00016", "수수료 계산값이 실제 전송시 소요량보다 부족합니다. 수동입력으로 진행하시겠습니까?(Y/n) : ");
		messageFormats.put("M00017", "화면에 보이는 수수료를 입력해 주세요. (Q : 취소) : ");
		messageFormats.put("M00018", "수수료가 과도하게 높습니다. 다시 입력하세요. 수수료 허용치 : %s");
		messageFormats.put("M00019", "다시 입력해 주세요.");
		messageFormats.put("M00020", "취소되었습니다.");
		messageFormats.put("M00021", "전송 실패하였습니다.");
		messageFormats.put("M00022", "전송되었습니다.");
		messageFormats.put("M00023", "주소를 입력해 주세요.(99 : 戻る) : ");
		messageFormats.put("M00024", "유효한 주소가 아닙니다.");
		messageFormats.put("M00025", "Payment 주소가 존재하지 않습니다. Payment 주소 생성 후 다시 시도하세요.");
		messageFormats.put("M00026", "Pool 주소 : ");
		messageFormats.put("M00027", "인출할 ADA가 없습니다.");
		messageFormats.put("M00028", "보유 수량 : %s");
		messageFormats.put("M00029", "보낼 주소를 입력해 주세요.(99 : 戻る) : ");
		messageFormats.put("M00030", "현재 Pool 주소로 보낼 경우는 합산한 총액만 전송가능합니다. 전송 수수료를 제외한 나머지 총액이 전송됩니다.");
		messageFormats.put("M00031", "진행하시겠습니까? (Y/n) :");
		messageFormats.put("M00032", "1. 전송수량과 수수료의 합계가 보유수량보다 많을 경우,");
		messageFormats.put("M00033", "2. 전송수량과 보유수량이 같을 경우,");
		messageFormats.put("M00034", "보유수량에서 수수료를 제외한 나머지를 전송합니다.");
		messageFormats.put("M00035", "보유수량 : %s , 전송수량 : %s , 수수료 : %s");
		messageFormats.put("M00036", "Sender address : %s");
		messageFormats.put("M00037", "Receiver address : %s");
		messageFormats.put("M00038", "전송하시겠습니까? (Y/n)");
		messageFormats.put("M00039", "이미 파일이 존재합니다. 삭제 후 다시 시도하세요.");
		messageFormats.put("M00040", "Payment Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00041", "Stake Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00042", "정상 처리되었습니다.");
		messageFormats.put("M00043", "KES Verification Key가 생성되지 않습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00044", "Cold Signing Key가 생성되지 않습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00045", "Cold Counter가 생성되지 않습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00046", "mainnet-shelley-genesis.json 파일이 존재하지 않습니다. 확인 후 다시 시도하세요.");
		messageFormats.put("M00047", "Metadata JSON 파일을 다운로드할 인터넷 주소를 입력하세요.(Q : 종료) : ");
		messageFormats.put("M00048", "잘못된 URL 형식입니다. 다시 입력하세요.");
		messageFormats.put("M00049", "URL 길이는 영문, 숫자, 기호 포함 64 Bytes(64자) 이하로 작성해야 합니다. Git Shorten URL을 이용하세요. https://git.io");
		messageFormats.put("M00050", "잘못된 JSON파일 형식입니다. JSON파일을 확인하세요.");
		messageFormats.put("M00051", "Pool Meta정보(pool 이름, pool 설명, pool Ticker, 등)이 없는 JSON파일 형식입니다. URL을 다시 확인하세요.");
		messageFormats.put("M00052", "pool 이름 : ");
		messageFormats.put("M00053", "pool 설명 : ");
		messageFormats.put("M00054", "pool Ticker : ");
		messageFormats.put("M00055", "pool 홍보 홈페이지 주소 : ");
		messageFormats.put("M00056", "선택사항) adapools.org 에 등록할 extended url : ");
		messageFormats.put("M00057", "위에 입력한 정보가 맞습니까? (Y/n) : ");
		messageFormats.put("M00058", "MetaData 파일을 저장하는 도중 에러가 발생했습니다.");
		messageFormats.put("M00059", "MetaData 파일 용량은 512 bytes 이하로 작성해야 합니다. (현재 사이즈 : %s bytes)");
		messageFormats.put("M00060", "MetaData 파일을 저장하는 도중 에러가 발생했습니다.");
		messageFormats.put("M00061", "Cold Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00062", "VRF Verification Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00063", "Pool 보증금을 입력해 주세요. (단위 : lovelace, 1 ADA = 1,000,000 lovelace). [Q : 취소] : ");
		messageFormats.put("M00064", "입력값이 잘못되었습니다. 다시 입력하세요.");
		messageFormats.put("M00065", "Pool 고정 비용을 입력해 주세요. (단위 : lovelace, 1 ADA = 1,000,000 lovelace). [Q : 취소] : ");
		messageFormats.put("M00066", "Pool Cost는 최소 %s 이상이어야 합니다.");
		messageFormats.put("M00067", "Pool Margin을 입력해 주세요. (단위 : 100분위 소수점, 예: 3% => 0.03 으로 입력). [Q : 취소] : ");
		messageFormats.put("M00068", "1이하로 입력해야 합니다.");
		messageFormats.put("M00069", "Relay Node 개수를 입력해 주세요. ( 단위 : 정수 1 이상 ) [Q : 취소] : ");
		messageFormats.put("M00070", "Relay Node 정보를 입력하세요.");
		messageFormats.put("M00071", "(예. 도메인네임:Port => relay.hyperlinkpool.kr:6000)");
		messageFormats.put("M00072", "(예. xxx.xxx.xxx.xxx:Port => 123.123.123.123:6000)");
		messageFormats.put("M00073", "예시의 형태대로 입력해 주세요.");
		messageFormats.put("M00074", "Relay Node 정보가 중복되었습니다. 다시 입력하세요.");
		messageFormats.put("M00075", "Pool 보증금 : %s");
		messageFormats.put("M00076", "Pool 고정 비용 : %s");
		messageFormats.put("M00077", "Pool Margin : %s");
		messageFormats.put("M00078", "Pool 등록 인증서 생성중 오류가 발생했습니다.");
		messageFormats.put("M00079", "Pool Delegation 인증서 생성중 오류가 발생했습니다.");
		messageFormats.put("M00080", "Pool Registration 인증서가 생성되지 않았습니다. 인증서 생성 후 다시 시도하세요.");
		messageFormats.put("M00081", "Pool Delegation 인증서가 생성되지 않았습니다. 인증서 생성 후 다시 시도하세요.");
		messageFormats.put("M00082", "Payment Signing Key가 생성되지 않았습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00083", "Stake Signing Key가 생성되지 않습니다. 키 생성 후 다시 시도하세요.");
		messageFormats.put("M00084", "Pool Id 확인중 에러가 발생했습니다.");
		messageFormats.put("M00085", "Pool 정보 확인 중입니다. 좀 오래 걸려요.");
		messageFormats.put("M00086", "Blockchain 확인중 에러가 발생했습니다.");
		messageFormats.put("M00087", "ADA 보유량이 충분하지 않습니다.");
		messageFormats.put("M00088", "ADA 현재 보유량 : %s");
		messageFormats.put("M00089", "Pool Register 필요량 : %s");
		messageFormats.put("M00090", "Transaction 수수료 : %s");
		messageFormats.put("M00091", "Pool 정보를 등록하시겠습니까?(Y/n) : ");
		messageFormats.put("M00092", "Pool 정보를 수정하시겠습니까?(Y/n) : ");
		messageFormats.put("M00093", "처리 완료 후 잔여 예상량 : ");
		messageFormats.put("M00094", "Pool 등록에 실패하였습니다.");
		messageFormats.put("M00095", "등록되었습니다.");
		messageFormats.put("M00096", "Stake Verification Key가 생성되지 않았습니다. 키 파일 생성 후 시도하세요.");
		messageFormats.put("M00097", "Stake Certificate가 생성되지 않았습니다. 인증서 생성 후 시도하세요.");
		messageFormats.put("M00098", "이미 Stake Key가 등록되어 있습니다.");
		messageFormats.put("M00099", "Stake Key Register 필요량 : %s");
		messageFormats.put("M00100", "처리 완료 후 잔여 예상량 : %s");
		messageFormats.put("M00101", "Stake Key를 등록하시겠습니까? (Y/n) : ");
		messageFormats.put("M00102", "Stake Key 등록에 실패하였습니다.");
		messageFormats.put("M00103", "Pool 등록용 MetaData 생성을 진행합니다.");
		messageFormats.put("M00104", "예시) [HYPER] Pool");
		messageFormats.put("M00105", "pool 이름 : Hyperlink Pool");
		messageFormats.put("M00106", "pool 설명 : We prepare for the era of hyperconnectivity.");
		messageFormats.put("M00107", "pool Ticker(영문 5자 이내) : HYPER");
		messageFormats.put("M00108", "pool 홍보 홈페이지 주소 : https://twitter.com/HYPERLINKPOOL");
		messageFormats.put("M00109", "adapools.org 에 등록할 extended url. 선택사항이며 없으면 enter키를 입력하세요.(참고형식 https://a.adapools.org/extended-example) : https://git.io/JUMjN");
		messageFormats.put("M00110", "pool Ticker(영문 5자 이내) : ");
		messageFormats.put("M00111", "영문, 숫자 5자 이내로 입력하세요.");
		messageFormats.put("M00112", "pool 홍보 홈페이지 주소 : ");
		messageFormats.put("M00113", "선택사항) adapools.org 에 등록시킬 extended url. : ");
		messageFormats.put("M00114", "MetaData 생성을 계속하시겠습니까? (Y/n) : ");
		messageFormats.put("M00115", "위의 Json Contents를 인터넷 환경에서 다운로드할 수 있도록 파일로 제작하여 업로드 한 후, 업로드한 파일을 다운로드하는 URL을 기억하십시오.(추천 : Github GIST)");
		messageFormats.put("M00116", "Core Node가 아닙니다. Core Node에서 실행하세요.");
		messageFormats.put("M00117", "* KES Key는 Block에 서명하기 위해 필요합니다. Pool 운영자는 KES Key Periods가 만료되기 전에 KES Key를 갱신해야 합니다.");
		messageFormats.put("M00118", "* KES Key를 갱신하지 않으면, 만료되는 Epoch 이후에 생성되는 Block에 서명하지 못하게 되므로, 반드시 만료 Periods가 도래하면 갱신하시기 바랍니다.");
		messageFormats.put("M00119", "------------------------------ KES Key 정보 ------------------------------ ");
		messageFormats.put("M00120", "- KES Key를 생성한 Periods : ");
		messageFormats.put("M00121", "- KES Key가 만료되는 Periods : ");
		messageFormats.put("M00122", "- KES Key 현재 Periods : ");
		messageFormats.put("M00123", "- KES Key 잔여 Periods : ");
		messageFormats.put("M00124", "- KES Key 잔여 Periods가 약 %s % 남았습니다.");
		messageFormats.put("M00125", "KES Key Rotation을 진행하시겠습니까?.(Y/n) : ");
		messageFormats.put("M00126", "기존의 KES Verification Key, KES, Signing Key를 삭제했습니다.");
		messageFormats.put("M00127", "신규 KES Verification Key, KES Signing Key가 생성되었습니다.");
		messageFormats.put("M00128", "신규 KES Period가 적용된 Node Certificate파일이 생성되었습니다.");
		messageFormats.put("M00129", "신규 KES Signing Key와 Node Certificate파일을 Block Producer노드 Config 경로에 복사한 후 Block Producer를 재시작 해주세요.");
		messageFormats.put("M00130", "신규 KES Signing Key : ");
		messageFormats.put("M00131", "신규 Node Certificate : ");
		messageFormats.put("M00132", "Pool 정보가 확인되었습니다. 정상 상태입니다.");
		messageFormats.put("M00133", "Pool 등록 정보를 찾을 수 없습니다.");
		messageFormats.put("M00134", "프로그램이 종료되었습니다.");
		messageFormats.put("M00135", "\n"
				+ "1. 현재 Stake Pool의 ADA Balance Check\n"
				+ "2. 다른 주소의 ADA Balance Check\n"
				+ "3. 현재 Stake Pool의 ADA Reward Balance Check\n"
				+ "4. 현재 Pool에서 다른 주소로 ADA 인출\n"
				+ "5. 현재 Pool에서 보상분(Rewards) ADA 인출\n"
				+ "99. 戻る");
		messageFormats.put("M00136", "번호를 입력한 후 엔터키를 눌러주세요. : ");
		messageFormats.put("M00137", "\n"
				+ "1. Payment Key Pair 생성\n"
				+ "2. Stake Key Pair 생성\n"
				+ "3. Payment Address 생성\n"
				+ "4. Stake Address 생성\n"
				+ "5. Stake Certificate 생성\n"
				+ "6. Stake Pool Cold Keys & Cold Counter 생성\n"
				+ "7. Stake Pool VRF Key Pair 생성\n"
				+ "8. Stake Pool KES Key Pair 생성\n"
				+ "9. Node Operation Certificate 생성\n"
				+ "99. 戻る");
		messageFormats.put("M00138", "\n"
				+ "1. Stake Key 등록(기본 등록은 2ADA가 소요되고(2020.10.09 기준 정보), 전송 수수료가 추가 소요됩니다.)\n"
				+ "2. Metadata JSON 파일 생성\n"
				+ "3. Pool 정보 등록 또는 갱신(Stake Key를 먼저 등록해야 하며, Pool 등록은 500ADA가 소요되고(2020.10.09 기준 정보), 이 후 갱신 시부터는 전송 수수료만 추가 소요됩니다.)\n"
				+ "4. Pool 등록 상태 확인\n5. KES Key Rotation\n"
				+ "99. 戻る");
		messageFormats.put("M00139", "\n"
				+ "1. Key 생성, Stake 인증서 생성\n"
				+ "2. ADA Balance Check, Pool에서 ADA 인출\n"
				+ "3. Stake Pool 등록 / 관리\n"
				+ "99. 종료");
		messageFormats.put("M00140", "입력 값이 잘못되었습니다.");
		messageFormats.put("M00141", "URL 형식이 아닙니다.");
		messageFormats.put("M00142", "통신 중 오류가 발생했습니다.");
		messageFormats.put("M00143", "- Pool 운영을 약간 편하게 해줍니다. Beta 버전입니다. 사용중 버그가 있을 수 있습니다.\n");
		messageFormats.put("M00144", "- Web 버전을 준비중에 있습니다. 여러분의 관심이 큰 힘이 됩니다.\n");
		messageFormats.put("M00145", "- 면책조항 1 : 본 프로그램의 잘못된 사용으로 야기된 사항에 대해 제작자는 일체 책임이 없음을 말씀드립니다.\n");
		messageFormats.put("M00146", "- 면책조항 2 : 본 프로그램은 Freeware입니다. 따라서 사용의 강제성은 없으며, 수정, 배포, 사용 역시 자유이며, 이에 따른 책임은 사용자에게 있습니다.\n");
		messageFormats.put("M00147", "Stake Pool 주소 개수 : %s");
		messageFormats.put("M00148", "      Stake Pool 주소 : %s");
		messageFormats.put("M00149", "%3s   Stake Pool ID : %s");
		messageFormats.put("M00150", "      Rewards Balance : %s");
		messageFormats.put("M00151", "잔고가 충분하지 않습니다.");
		messageFormats.put("M00152", "잔고 동기화에 시간이 걸릴 수 있습니다.");
		messageFormats.put("M00153", "- TestNet은 지원하지 않습니다.");
	}
}
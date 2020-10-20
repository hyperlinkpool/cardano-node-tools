package kr.hyperlinkpool;

import kr.hyperlinkpool.i18n.MessageFactory;
import kr.hyperlinkpool.utils.MessagePrompter;

public class ReadMe {

	public static void printWelcomeMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		stringBuilder.append("*  ██████╗ █████╗ ██████╗ ██████╗  █████╗ ███╗   ██╗ ██████╗     ███╗   ██╗ ██████╗ ██████╗ ███████╗    ████████╗ ██████╗  ██████╗ ██╗     ███████╗ *\n");
		stringBuilder.append("* ██╔════╝██╔══██╗██╔══██╗██╔══██╗██╔══██╗████╗  ██║██╔═══██╗    ████╗  ██║██╔═══██╗██╔══██╗██╔════╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝ *\n");
		stringBuilder.append("* ██║     ███████║██████╔╝██║  ██║███████║██╔██╗ ██║██║   ██║    ██╔██╗ ██║██║   ██║██║  ██║█████╗         ██║   ██║   ██║██║   ██║██║     ███████╗ *\n");
		stringBuilder.append("* ██║     ██╔══██║██╔══██╗██║  ██║██╔══██║██║╚██╗██║██║   ██║    ██║╚██╗██║██║   ██║██║  ██║██╔══╝         ██║   ██║   ██║██║   ██║██║     ╚════██║ *\n");
		stringBuilder.append("* ╚██████╗██║  ██║██║  ██║██████╔╝██║  ██║██║ ╚████║╚██████╔╝    ██║ ╚████║╚██████╔╝██████╔╝███████╗       ██║   ╚██████╔╝╚██████╔╝███████╗███████║ *\n");
		stringBuilder.append("*  ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝     ╚═╝  ╚═══╝ ╚═════╝ ╚═════╝ ╚══════╝       ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝ *\n");
		stringBuilder.append("*                                                                                                      Cardano Node Tools for Stake Pool Operators  *\n");
		stringBuilder.append("*                                                                                                                            version : v0.1.0 beta  *\n");
		stringBuilder.append("*                                                                                 source : https://github.com/hyperlinkpool/cardano-node-tools.git  *\n");
		stringBuilder.append("* Developed by                                                                                                                                      *\n");
		stringBuilder.append("* ██╗  ██╗██╗   ██╗██████╗ ███████╗██████╗ ██╗     ██╗███╗   ██╗██╗  ██╗    ██████╗  ██████╗  ██████╗ ██╗                                           *\n");     
		stringBuilder.append("* ██║  ██║╚██╗ ██╔╝██╔══██╗██╔════╝██╔══██╗██║     ██║████╗  ██║██║ ██╔╝    ██╔══██╗██╔═══██╗██╔═══██╗██║                                           *\n");     
		stringBuilder.append("* ███████║ ╚████╔╝ ██████╔╝█████╗  ██████╔╝██║     ██║██╔██╗ ██║█████╔╝     ██████╔╝██║   ██║██║   ██║██║                                           *\n");     
		stringBuilder.append("* ██╔══██║  ╚██╔╝  ██╔═══╝ ██╔══╝  ██╔══██╗██║     ██║██║╚██╗██║██╔═██╗     ██╔═══╝ ██║   ██║██║   ██║██║                                           *\n");
		stringBuilder.append("* ██║  ██║   ██║   ██║     ███████╗██║  ██║███████╗██║██║ ╚████║██║  ██╗    ██║     ╚██████╔╝╚██████╔╝███████╗                                      *\n");
		stringBuilder.append("* ╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝    ╚═╝      ╚═════╝  ╚═════╝ ╚══════╝                                      *\n");
		stringBuilder.append("* TICKER : HYPER                                                                                                                                    *\n");
		stringBuilder.append("* Pool ID : 263498e010c7a49bbfd7c4e1aab29809fca7ed993f9e14192a75871e                                                                                *\n");
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		stringBuilder.append("* Socials                                                                                                                                           *\n");
		stringBuilder.append("* E-mail : hyperlinkpool@gmail.com                                                                                                                  *\n");
		stringBuilder.append("* TWITTER : https://twitter.com/HYPERLINKPOOL                                                                                                       *\n");
		stringBuilder.append("* Telegram : https://t.me/HYPERLINKPOOL                                                                                                             *\n");
		stringBuilder.append("* GITHUB : https://github.com/hyperlinkpool                                                                                                         *\n");
		stringBuilder.append("*                                                                                                                                                   *\n");
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		stringBuilder.append("* Donations                                                                                                                                         *\n");
		stringBuilder.append("* ADA : addr1qye30zk97awn7ec0s52vxq47k7funsmge4cpgzkmp99tx493runz7jwqvktew9j609y7240pwplndn47z49hup3rl9tsvdkjzt                                     *\n");
		stringBuilder.append("* BTC : 3N21st18SUUFF3CHW8Pa9vaTGpjiR7LH16                                                                                                          *\n");
		stringBuilder.append("* ETH : 0xb5805d55fa0b2ea7c589dd7bdae8c8c796723eed                                                                                                  *\n");
		stringBuilder.append("* XRP : r3pb9PY6H6ZD3nPAbFAkrKnw9qjvpmbwPk / Destination Tag : 2878778027                                                                           *\n");
		stringBuilder.append("* XLM : GCVXSHSMJN67UJIT5EYY7DN6UGCYKOBRLIJQ3N7QZJAZSY5NWTHRBHNK / MEMO : enyM1532434833993                                                         *\n");
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		stringBuilder.append(MessageFactory.getInstance().getMessage("- Pool 운영을 약간 편하게 해줍니다. Beta 버전입니다. 사용중 버그가 있을 수 있습니다.\n", "M00143"));
		stringBuilder.append(MessageFactory.getInstance().getMessage("- Web 버전을 준비중에 있습니다. 여러분의 관심이 큰 힘이 됩니다.\n", "M00144"));
		stringBuilder.append(MessageFactory.getInstance().getMessage("- TestNet은 지원하지 않습니다.\n", "M00153"));
		stringBuilder.append(MessageFactory.getInstance().getMessage("- 면책조항 1 : 본 프로그램의 잘못된 사용으로 야기된 사항에 대해 제작자는 일체 책임이 없음을 말씀드립니다.\n", "M00145"));
		stringBuilder.append(MessageFactory.getInstance().getMessage("- 면책조항 2 : 본 프로그램은 Freeware입니다. 따라서 사용의 강제성은 없으며, 수정, 배포, 사용 역시 자유이며, 이에 따른 책임은 사용자에게 있습니다.\n", "M00146"));
		MessagePrompter.promptMessage(stringBuilder.toString(), true);
	}
	
	public static void printGoodbyeMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		stringBuilder.append("*████████╗██╗  ██╗ █████╗ ███╗   ██╗██╗  ██╗███████╗    ███████╗ ██████╗ ██████╗     ██╗   ██╗███████╗██╗███╗   ██╗ ██████╗                         *\n");
		stringBuilder.append("*╚══██╔══╝██║  ██║██╔══██╗████╗  ██║██║ ██╔╝██╔════╝    ██╔════╝██╔═══██╗██╔══██╗    ██║   ██║██╔════╝██║████╗  ██║██╔════╝                         *\n");
		stringBuilder.append("*   ██║   ███████║███████║██╔██╗ ██║█████╔╝ ███████╗    █████╗  ██║   ██║██████╔╝    ██║   ██║███████╗██║██╔██╗ ██║██║  ███╗                        *\n");
		stringBuilder.append("*   ██║   ██╔══██║██╔══██║██║╚██╗██║██╔═██╗ ╚════██║    ██╔══╝  ██║   ██║██╔══██╗    ██║   ██║╚════██║██║██║╚██╗██║██║   ██║                        *\n");
		stringBuilder.append("*   ██║   ██║  ██║██║  ██║██║ ╚████║██║  ██╗███████║    ██║     ╚██████╔╝██║  ██║    ╚██████╔╝███████║██║██║ ╚████║╚██████╔╝██╗                     *\n");
		stringBuilder.append("*   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝    ╚═╝      ╚═════╝ ╚═╝  ╚═╝     ╚═════╝ ╚══════╝╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝                     *\n");
		stringBuilder.append("*                                                                                                      Cardano Node Tools for Stake Pool Operators  *\n");
		stringBuilder.append("*                                                                                                                            version : v0.1.0 beta  *\n");
		stringBuilder.append("*                                                                                 source : https://github.com/hyperlinkpool/cardano-node-tools.git  *\n");
		stringBuilder.append("* Developed by                                                                                                                                      *\n");
		stringBuilder.append("* ██╗  ██╗██╗   ██╗██████╗ ███████╗██████╗ ██╗     ██╗███╗   ██╗██╗  ██╗    ██████╗  ██████╗  ██████╗ ██╗                                           *\n");     
		stringBuilder.append("* ██║  ██║╚██╗ ██╔╝██╔══██╗██╔════╝██╔══██╗██║     ██║████╗  ██║██║ ██╔╝    ██╔══██╗██╔═══██╗██╔═══██╗██║                                           *\n");     
		stringBuilder.append("* ███████║ ╚████╔╝ ██████╔╝█████╗  ██████╔╝██║     ██║██╔██╗ ██║█████╔╝     ██████╔╝██║   ██║██║   ██║██║                                           *\n");     
		stringBuilder.append("* ██╔══██║  ╚██╔╝  ██╔═══╝ ██╔══╝  ██╔══██╗██║     ██║██║╚██╗██║██╔═██╗     ██╔═══╝ ██║   ██║██║   ██║██║                                           *\n");
		stringBuilder.append("* ██║  ██║   ██║   ██║     ███████╗██║  ██║███████╗██║██║ ╚████║██║  ██╗    ██║     ╚██████╔╝╚██████╔╝███████╗                                      *\n");
		stringBuilder.append("* ╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝    ╚═╝      ╚═════╝  ╚═════╝ ╚══════╝                                      *\n");
		stringBuilder.append("* TICKER : HYPER                                                                                                                                    *\n");
		stringBuilder.append("* Pool ID : 263498e010c7a49bbfd7c4e1aab29809fca7ed993f9e14192a75871e                                                                                *\n");
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		stringBuilder.append("* Socials                                                                                                                                           *\n");
		stringBuilder.append("* E-mail : hyperlinkpool@gmail.com                                                                                                                  *\n");
		stringBuilder.append("* TWITTER : https://twitter.com/HYPERLINKPOOL                                                                                                       *\n");
		stringBuilder.append("* Telegram : https://t.me/HYPERLINKPOOL                                                                                                             *\n");
		stringBuilder.append("* GITHUB : https://github.com/hyperlinkpool                                                                                                         *\n");
		stringBuilder.append("*                                                                                                                                                   *\n");
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		stringBuilder.append("* Donations                                                                                                                                         *\n");
		stringBuilder.append("* ADA : addr1qye30zk97awn7ec0s52vxq47k7funsmge4cpgzkmp99tx493runz7jwqvktew9j609y7240pwplndn47z49hup3rl9tsvdkjzt                                     *\n");
		stringBuilder.append("* BTC : 3N21st18SUUFF3CHW8Pa9vaTGpjiR7LH16                                                                                                          *\n");
		stringBuilder.append("* ETH : 0xb5805d55fa0b2ea7c589dd7bdae8c8c796723eed                                                                                                  *\n");
		stringBuilder.append("* XRP : r3pb9PY6H6ZD3nPAbFAkrKnw9qjvpmbwPk / Destination Tag : 2878778027                                                                           *\n");
		stringBuilder.append("* XLM : GCVXSHSMJN67UJIT5EYY7DN6UGCYKOBRLIJQ3N7QZJAZSY5NWTHRBHNK / MEMO : enyM1532434833993                                                         *\n");
		stringBuilder.append("*****************************************************************************************************************************************************\n");
		MessagePrompter.promptMessage(stringBuilder.toString(), true);
	}
}
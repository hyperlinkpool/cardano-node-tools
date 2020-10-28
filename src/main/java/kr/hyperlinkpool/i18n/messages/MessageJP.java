package kr.hyperlinkpool.i18n.messages;

import java.util.HashMap;
import java.util.Map;

public class MessageJP {
	public static Map<String, String> messageFormats = null;
	static {
		messageFormats = new HashMap<String, String>();
		messageFormats.put("M00001", "ステークのアドレスが作成されていません。アドレスを作成した後、もう一度やり直してください。");
		messageFormats.put("M00002", "ステークキーがブロックチェーンに登録されていません。キーの登録後、もう一度やり直してください。");
		messageFormats.put("M00003", "(メインメニュー -> 3番目 -> 1番目 選択)");
		messageFormats.put("M00004", "現在補償量 : %s");
		messageFormats.put("M00005", "撤退することができる補償がありません");
		messageFormats.put("M00006", "トランザクションハッシュがありません。");
		messageFormats.put("M00007", "引き出し可能な補償量 : %s");
		messageFormats.put("M00008", "転送数量を入力してください。(入力ユニット : lovelace, 1 ADA = 1,000,000 lovelace, キャンセル：Q) : ");
		messageFormats.put("M00009", "数量が足りません。もう一度入力してください。引き出し可能の数量: %s , 入力の数量 : %s");
		messageFormats.put("M00010", "プロセス実行中にエラーが発生されました。 : ");
		messageFormats.put("M00011", "数字を入力してください。");
		messageFormats.put("M00012", "保有の数量 : %s , 補償分引き出し数量 : %s , 手数料 : %s");
		messageFormats.put("M00013", "引き出した後、残りの補償量 : %s");
		messageFormats.put("M00014", "引き出した後、総保有の数量 : %s");
		messageFormats.put("M00015", "引き出しますか？ (Y/n) : ");
		messageFormats.put("M00016", "手数料の計算値が実際の送信時に所要量よりも足りません。直接の入力で続きますか？(Y/n) : ");
		messageFormats.put("M00017", "画面に表示されてある手数料を入力してください。(Q：キャンセル) : ");
		messageFormats.put("M00018", "手数料が高すぎます。手数料の許容値 : %s");
		messageFormats.put("M00019", "もう一度入力してください。");
		messageFormats.put("M00020", "キャンセルされました。");
		messageFormats.put("M00021", "送信することができませんでした。");
		messageFormats.put("M00022", "送信されました。");
		messageFormats.put("M00023", "（99 : 戻る）: ");
		messageFormats.put("M00024", "有効なアドレスではありません。");
		messageFormats.put("M00025", "Paymentアドレスが作成されていません。Paymentアドレスを作成した後、やり直してください。");
		messageFormats.put("M00026", "プールのアドレス : ");
		messageFormats.put("M00027", "引き出すことができるADAがありません。");
		messageFormats.put("M00028", "保有の数量 : %s");
		messageFormats.put("M00029", "送信アドレスを入力してください。（99 : 戻る）: ");
		messageFormats.put("M00030", "現プールアドレスに送信する場合は、合算した総額のみを送信できます。送信の手数料を除いた残りの金額が送信されます。");
		messageFormats.put("M00031", "進みますか？(Y/n) : ");
		messageFormats.put("M00032", "1.送信量と手数料の合計が保有の数量より多い場合には、");
		messageFormats.put("M00033", "2.送信量と保有の数量が同じ場合には、");
		messageFormats.put("M00034", "保有の数量から手数料を除いた残りを送信します。");
		messageFormats.put("M00035", "保有の数量 : %s , 送信の数量 : %s , 手数料 : %s");
		messageFormats.put("M00036", "送信者のアドレス : %s");
		messageFormats.put("M00037", "受信者のアドレス : %s");
		messageFormats.put("M00038", "送信しますか？ (Y/n) : ");
		messageFormats.put("M00039", "既にファイルがあります。削除した後、もう一度やり直してください。");
		messageFormats.put("M00040", "PaymentのVerificationのKeyが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00041", "StakeのVerificationのKeyが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00042", "正常に処理されました。");
		messageFormats.put("M00043", "KESのVerificationのKeyが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00044", "ColdのSigningのKeyが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00045", "ColdのCounterが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00046", "mainnet-shelley-genesis.jsonのファイルがありません。確認後、もう一度やり直してください。");
		messageFormats.put("M00047", "MetadataのJSONファイルをダウンロードすることができるインターネットアドレスを入力してください。(Q：キャンセル) : ");
		messageFormats.put("M00048", "URLが正しくありません。もう一度入力してください。");
		messageFormats.put("M00049", "URLの長さは、英文、数字、記号を含む64 Bytes（64文字）以下で作成する必要があります。Git Shorten URLをご利用下さい。https://git.io");
		messageFormats.put("M00050", "JSONのファイルが間違っています。JSONのファイルを確認してください。");
		messageFormats.put("M00051", "プールのMeta情報（プールの名、プールの説明、プール Ticker、など）がないJSONファイル形式です。URLをもう一度確認してください。");
		messageFormats.put("M00052", "プールの名 : ");
		messageFormats.put("M00053", "プールの説明 : ");
		messageFormats.put("M00054", "プールのTicker : ");
		messageFormats.put("M00055", "プールの広報ホームページアドレス : ");
		messageFormats.put("M00056", "オプション）adapools.orgに登録するextended url : ");
		messageFormats.put("M00057", "上記の入力した情報が正しいですか？ (Y/n) : ");
		messageFormats.put("M00058", "MetaDataファイルを保存する際にエラーが発生されました。");
		messageFormats.put("M00059", "MetaDataファイルの容量は、512 bytes以下に記述する必要があります。（現在のサイズ：%s bytes)");
		messageFormats.put("M00060", "MetaDataファイルのHashを作成する際にエラーが発生されました。");
		messageFormats.put("M00061", "ColdのVerificationのKeyが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00062", "VRFのVerificationのKeyが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00063", "プールの保証金を入力してください。(ユニット: lovelace, 1 ADA = 1,000,000 lovelace)。[Q：キャンセル] : ");
		messageFormats.put("M00064", "入力値が間違っています。もう一度入力してください。");
		messageFormats.put("M00065", "プールの固定費を入力してください。(ユニット : lovelace, 1 ADA = 1,000,000 lovelace)。[Q：キャンセル] : ");
		messageFormats.put("M00066", "プールのCostは少なくとも%s以上である必要があります。");
		messageFormats.put("M00067", "プールのMarginを入力してください。(ユニット : 100分位小数点, 例: 3％=> 0.03で入力). [Q：キャンセル] : ");
		messageFormats.put("M00068", "1以下で入力する必要があります。");
		messageFormats.put("M00069", "RelayのNodeの数を入力してください。( ユニット : 1以上の整数) [Q：キャンセル] : ");
		messageFormats.put("M00070", "RelayのNodeの情報を入力してください。");
		messageFormats.put("M00071", "(例. ドメイン名：Port => relay.hyperlinkpool.kr:6000)");
		messageFormats.put("M00072", "(例. xxx.xxx.xxx.xxx:Port => 123.123.123.123:6000)");
		messageFormats.put("M00073", "例のとおりで入力してください。");
		messageFormats.put("M00074", "RelayのNode情報が重複されました。もう一度入力してください。");
		messageFormats.put("M00075", "プールの保証金 : %s");
		messageFormats.put("M00076", "プールの固定費 : %s");
		messageFormats.put("M00077", "プールのMargin : %s");
		messageFormats.put("M00078", "プール登録証明書の作成中にエラーが発生しました。");
		messageFormats.put("M00079", "プールのDelegation証明書の作成中にエラーが発生しました。");
		messageFormats.put("M00080", "プールのRegistrationの証明書が作成されていない。証明書の作成後、もう一度やり直してください。");
		messageFormats.put("M00081", "プールのDelegationの証明書が作成されていない。証明書の作成後、もう一度やり直してください。");
		messageFormats.put("M00082", "PaymentのSigningのキーが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00083", "StakeのSigningのキーが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00084", "プールのIDの確認中にエラーが発生しました。");
		messageFormats.put("M00085", "プールの情報の確認中です。少し長い時間がかかります。");
		messageFormats.put("M00086", "Blockchain確認中にエラーが発生しました。");
		messageFormats.put("M00087", "ADA保有の数量が十分ではありません。");
		messageFormats.put("M00088", "ADA現在の保有の数量 : %s");
		messageFormats.put("M00089", "プールの登録の必要量 : %s");
		messageFormats.put("M00090", "トランザクションの手数料 : %s");
		messageFormats.put("M00091", "プールの情報を登録しますか？(Y/n) : ");
		messageFormats.put("M00092", "プールの情報を修正しますか？(Y/n) : ");
		messageFormats.put("M00093", "処理完了後、残りの予想量 : ");
		messageFormats.put("M00094", "プールの登録に失敗しました。");
		messageFormats.put("M00095", "登録されました。");
		messageFormats.put("M00096", "StakeのVerificationのKeyキーが作成されていません。キーの作成後、もう一度やり直してください。");
		messageFormats.put("M00097", "StakeのCertificateの証明書が作成されていない。証明書の作成後、もう一度やり直してください。");
		messageFormats.put("M00098", "すでにStakeのKeyが登録されています。");
		messageFormats.put("M00099", "StakeのKeyの登録の必要量 : %s");
		messageFormats.put("M00100", "処理完了後、残りの予想量 : %s");
		messageFormats.put("M00101", "StakeのKeyを登録しますか？(Y/n) : ");
		messageFormats.put("M00102", "Stakeのeyの登録に失敗しました。");
		messageFormats.put("M00103", "プールの登録用のMetaDataの作成を行います。");
		messageFormats.put("M00104", "예시) [HYPER] プール");
		messageFormats.put("M00105", "プールの名 : Hyperlink Pool");
		messageFormats.put("M00106", "プールの説明 : We prepare for the era of hyperconnectivitydelegateStakeAddressDomain.setNextOrder(StepOrder.EXIT.getStepOrder());\n"
				+ "			result.setSuccess(false);。");
		messageFormats.put("M00107", "プールのTicker(英語5文字以内) : HYPER");
		messageFormats.put("M00108", "プールの広報ホームページアドレス : https://twitter.com/HYPERLINKPOOL");
		messageFormats.put("M00109", "adapools.orgに登録するextended url。オプションであり、存在しない場合、enterキーを入力してください。(参考形式 https://a.adapools.org/extended-example) : https://git.io/JUMjN");
		messageFormats.put("M00110", "プールのTicker(英語5文字以内) : ");
		messageFormats.put("M00111", "英語、数字5文字以内で入力してください。");
		messageFormats.put("M00112", "プールの広報ホームページアドレス : ");
		messageFormats.put("M00113", "オプション)adapools.orgに登録するextended url。: ");
		messageFormats.put("M00114", "MetaDataの作成を行いますか？(Y/n) : ");
		messageFormats.put("M00115", "上記のJsonのContentsをインターネット環境でダウンロードすることができるように、ファイルで製作して、アップロードした後、アップロードしたファイルをダウンロードするURLを覚えください。(추천 : Github GIST)");
		messageFormats.put("M00116", "CoreのNodeではありません。CoreのNodeで実行してください。");
		messageFormats.put("M00117", "* KESのKeyはBlockに署名するために必要です。プールオペレータはKESのKeyのPeriodsの期限が切れる前に、KESのKeyを更新する必要があります。");
		messageFormats.put("M00118", "* KESのKeyを更新しなければ、有効期限が切れているEpoch以降に作成されたBlockに署名できませんので必ず期限Periodsが到来すると、更新してください。");
		messageFormats.put("M00119", "------------------------------ KESのKeyの情報 ------------------------------ ");
		messageFormats.put("M00120", "- KESのKeyが作成されたPeriods : ");
		messageFormats.put("M00121", "- KESのKeyの有効期限が切れるPeriods : ");
		messageFormats.put("M00122", "- KESのKeyの現在のPeriods : ");
		messageFormats.put("M00123", "- KESのKeyの残りのPeriods : ");
		messageFormats.put("M00124", "- KESのKeyの残りのPeriodsが約%s％残りました。");
		messageFormats.put("M00125", "KESのKeyのRotationを進みますか？(Y/n) : ");
		messageFormats.put("M00126", "既存のKESのVerificationのKey、KESのSigningのKeyが削除されました。");
		messageFormats.put("M00127", "新規のKESのVerificationのKey、KESのSigningのKeyが作成されました。");
		messageFormats.put("M00128", "新規のKESのPeriodが適用されたNodeのCertificateのファイルが作成されました。");
		messageFormats.put("M00129", "新規のKESのSigningのKeyとNodeのCertificateのファイルをBlockProducerノードのConfigパスにコピーした後、BlockProducerを再起動してください。");
		messageFormats.put("M00130", "新規のKESのSigningのKey : ");
		messageFormats.put("M00131", "新規のNodeのCertificate : ");
		messageFormats.put("M00132", "プールの情報が確認されました。通常の状態です。");
		messageFormats.put("M00133", "プールの登録情報を見つけることができません。");
		messageFormats.put("M00134", "アプリケーションが終了されました。");
		messageFormats.put("M00135", "\n"
				+ "1. 現在の財布のADAのバランスチェック\n"
				+ "2. 他の財布のADAのバランスチェック\n"
				+ "3. 現在の財布のADAの報酬のバランスチェック\n"
				+ "4. 他の財布のADAの報酬のバランスチェック\n"
				+ "5. 現在の財布から他の財布にADAの引き出し\n"
				+ "6. 現在の財布からADAの報酬の引き出し\n"
				+ "99. 戻る");
		messageFormats.put("M00136", "番号を入力した後、エンターキーを押してください。: ");
		messageFormats.put("M00137", "\n"
				+ "1. PaymentのKeyのPairの作成\n"
				+ "2. StakeのKeyのPairの作成\n"
				+ "3. PaymentのAddressの作成\n"
				+ "4. StakeのAddressの作成\n"
				+ "5. ステークプールのColdのKeys & ColdのCounterの作成\n"
				+ "6. ステークプールのVRFのKeyのPairの作成\n"
				+ "7. ステークプールのKESのKeyのPairの作成\n"
				+ "8. NodeのOperationのCertificateの作成\n"
				+ "99. 戻る");
		messageFormats.put("M00138", "\n"
				+ "1. ステークキーの登録（2ADAがかかり、送信の手数料がかかります。）\n"
				+ "2. ステークキーの撤回（保証金として登録した2ADAを取り戻すことができます。）\n"
				+ "3. ステークキーをプールに委任\n"
				+ "4. MetadataのJSONのファイルの作成\n"
				+ "5. ステークプールの情報登録または更新（StakeのKeyを最初に登録する必要があり、プールの登録は500ADAがかかり、この後、更新の時からは送信の手数料のみかかります。)\n"
				+ "6. ステークプールの撤回（保証金として登録した500ADAを取り戻すことができます。）\n"
				+ "7. ステークプールの登録状況確認\n"
				+ "8. KESのKeyのRotation\n"
				+ "99. 戻る");
		messageFormats.put("M00139", "\n"
				+ "1. Keyの作成, Stakeの証明書の作成\n"
				+ "2. 財布のADAのバランスチェック, ADAの引き出し\n"
				+ "3. ステークプールの登録 / 管理\n"
				+ "99. 終了");
		messageFormats.put("M00140", "入力値が間違いました。");
		messageFormats.put("M00141", "URLが正しくありません。");
		messageFormats.put("M00142", "通信中にエラーが発生されました。");
		messageFormats.put("M00143", "- プール操作を少し楽にしてくれます。Beta版です。使用中のバグがあるとおもいます。\n");
		messageFormats.put("M00144", "- Web版を用意しております。皆さんの関心が大きな力になります。\n");
		messageFormats.put("M00145", "- 免責事項1：本プログラムの誤った使用生じた事項についてメーカーは一切の責任がないことを申し上げます。\n");
		messageFormats.put("M00146", "- 免責事項2：本プログラムはFreewareですので、使用の強制性はありません。また、修正、配布、使用も自由であり、これに伴う責任はユーザーにあります。\n");
		messageFormats.put("M00147", "ステークプールのアドレスの数 : %s");
		messageFormats.put("M00148", "      ステークキーのアドレス : %s");
		messageFormats.put("M00149", "%3s   委任されたプールのID : %s");
		messageFormats.put("M00150", "      RewardsのBalance : %s");
		messageFormats.put("M00151", "現在の口座にADA残高が足りません。100ADA程度入金した後、再試行してください。");
		messageFormats.put("M00152", "残高同期に時間がかかることがあります。");
		messageFormats.put("M00153", "- TestNetはサポートしていません。");
		messageFormats.put("M00154", "CardanoのローカルNodeが停止状態または同期中です。CardanoのNodeの同期完了後に実行してください。");
		messageFormats.put("M00155", "StakeのKeyが登録されていません。登録の後に撤回することができます。");
		messageFormats.put("M00156", "委任されていません。");
		messageFormats.put("M00157", "StakeのKeyを撤回しますか？ 注意）撤回すると、この後には、委任の報酬を受けることができません。(Y/n) : ");
		messageFormats.put("M00158", "撤回されました。");
		messageFormats.put("M00159", "プールに委任されています。撤回すると、再び委任する必要があります。続きますか？(Y/n) : ");
		messageFormats.put("M00160", "StakeのKeyが登録されていません。登録の後に委任することができます。");
		messageFormats.put("M00161", "委任するPoolのIDを入力してください。(Q：キャンセル) : ");
		messageFormats.put("M00162", "PoolのIDは16進数の形式のみ可能です。例)[HYPER] = 263498e010c7a49bbfd7c4e1aab29809fca7ed993f9e14192a75871e");
		messageFormats.put("M00163", "Stake Keyを委任しますか？(Y/n) : ");
		messageFormats.put("M00164", "Pool ID : ");
		messageFormats.put("M00165", "Poolに委任されています。委任の移動しますか？(Y/n) : ");
		messageFormats.put("M00166", "委任されました。");
		messageFormats.put("M00167", "引退するEpochを入力してください。（入力可能範囲：%s ~ %s、現在のEpoch：%s）[Q：キャンセル] : ");
		messageFormats.put("M00168", "入力可能な範囲を超えています。再入力してください。");
		messageFormats.put("M00169", "現在Poolの引退を行いますか？（Y / n）：");
		messageFormats.put("M00170", "引退のEpoch : %s");
		messageFormats.put("M00171", "引退のEpochこの後、現在のアドレスに返されるADAの保証金：%s");
		messageFormats.put("M00172", "Poolの引退の登録に失敗しました。");
		messageFormats.put("M00173", "ADAのWalletのアドレスを入力してください。（ex. Ddz... , Ae2..., addr1...）");
		messageFormats.put("M00174", "Stakeのアドレスを入力してください。（ex. stake1... ）");
		messageFormats.put("M00175", "BlockProducerではありません。BlockProducerではない場合は、関連するKey（KES singing key、Cold signing key、Cold Counter key）があればKES Key Roationを進めることができます。");
		messageFormats.put("M00176", "現在NodeでKES Key Rotationを進めますか？(Y/n)：");
		messageFormats.put("E99999", "開発中です。");
	}
}
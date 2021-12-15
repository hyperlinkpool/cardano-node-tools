# Introduction

**Cardano Node Version 1.32.1 compatible, Alonzo Era compatible**

- 안녕하세요. 한국 카르다노 Stake Pool Operator HYPER입니다. 노드 설치 후에 셋팅과 관리를 도와주는 서버용 지갑을 제작하게 되었습니다.(Dear Community. This is Korea Cardano Stake Pool Operator a.k.a. HYPER. We have created a wallet for the server that helps setting and management after installing the node.)

- ADA 송수신, ADA Rewards 인출, [Stake Pool 위임, 철회], [Pool 등록, 철회]등 Cardano 노드에서 사용하는 지갑입니다.(It is a wallet used by Cardano nodes such as ADA transmission and reception, ADA Rewards withdrawal, [Stake Pool delegation, withdrawal], and [Pool registration, withdrawal], etc.)

- cardano-cli를 사용하는데 번거로운 수작업을 줄인 툴입니다.(It is a tool that reduces the hassle of manual labor when using cardano-cli.)

- 그 동안 Pool을 운영하면서 수작업으로 Command를 작성하던 작업을 시스템화하였으며, 수작업으로 발생할 수 있는 실수를 줄일 수 있도록 개발했습니다.(During that time, while operating the pool, the task of manually writing commands has been systematized, and has been developed to reduce mistakes that was operated manually.)

- 악성 소프트웨어로 오해받을 수 있기 때문에 Java를 이해하시는 Pool운영자가 사용해 보셨으면 좋겠습니다.(It can be misunderstood as malicious software, so I hope that SPO, who understands Java, will use it.)

- 노드가 설치되어 있는 서버에서 Java로 실행하는 소프트웨어 입니다.(This software is executed in Java on the server where a node is installed.)

**TestNet은 지원하지 않습니다.(TestNet is not supported.)**

**면책조항 1 : 본 프로그램의 잘못된 사용으로 야기된 사항에 대해 제작자는 일체 책임이 없음을 말씀드립니다.**
**(- Disclaimer 1: Please note that the creators are not responsible for any matters caused by incorrect use of this program.)**

**면책조항 2 : 본 프로그램은 Freeware입니다. 따라서 사용의 강제성은 없으며, 수정, 배포, 사용 역시 자유이며, 이에 따른 책임은 사용자에게 있습니다.**
**(- Disclaimer 2: This program is freeware. Therefore, there is no compulsory use, and modification, distribution, and use are also free, and the user is responsible for this.)**

## 진행중인 개발 목록(List of ongoing developments)
```jsx
- ...
```

## Supported OS
- Java를 실행할 수 있는 모든 운영체제.(Any operating system that can run Java.)
- Cardano Node가 실행되는 서버에서 실행 가능.(It can be run on the server running Cardano Node.)

## Supported Language
- 한국어(Korean), 영어(English), 일본어(Japanese), (Other languages : 영어(English))

## Developer Infomation
- Developer : Hyperlink Pool SPO
- Ticker : HYPER
- Pool ID : 263498e010c7a49bbfd7c4e1aab29809fca7ed993f9e14192a75871e

## Social
- E-mail : hyperlinkpool@gmail.com
- TWITTER : https://twitter.com/HYPERLINKPOOL
- Telegram : https://t.me/HYPERLINKPOOL
- GITHUB : https://github.com/hyperlinkpool

## 주요 기능(Main Function)
1. Key, 인증서 생성(1. Create Key & Certificate)
2. ADA Balance Check, ADA 인출, Pool Rewards 인출(2. ADA Balance Check, Withdraw ADA)
3. Stake Pool 등록 및 관리(3. Register pool / Manage pool)

## Menu Tree
```diff
- 1. Key 생성, Stake 인증서 생성(1. Create Key & Certificate)
    - 1. Payment Key Pair 생성(1. Create Payment Key Pair)
    - 2. Stake Key Pair 생성(2. Create Stake Key Pair)
    - 3. Payment Address 생성(3. Create Payment Address)
    - 4. Stake Address 생성(4. Create Stake Address)
    - 5. Stake Pool Cold Keys & Cold Counter 생성(5. Create Stake Pool Cold Keys & Cold Counter)
    - 6. Stake Pool VRF Key Pair 생성(6. Create Stake Pool VRF Key Pair)
    - 7. Stake Pool KES Key Pair 생성(7. Create Stake Pool KES Key Pair)
    - 8. Node Operation Certificate 생성(8. Create Node Operation Certificate)
    - 99. 뒤로 가기(99. Go Back)

- 2. ADA Balance Check, Pool에서 ADA 인출(2. ADA Balance Check, Withdraw ADA)
    - 1. 현재 지갑의 ADA Balance Check(1. Balance check of the current wallet)
    - 2. 다른 지갑의 ADA Balance Check(2. Balance check of another wallet)
    - 3. 현재 지갑의 ADA Reward Balance Check(3. Rewards balance check of the current wallet)
    - 4. 다른 지갑의 ADA Reward Balance Check(4. Rewards balance check of another wallet)
    - 5. 현재 지갑에서 다른 지갑으로 ADA 인출(5. Withdrawal from the current wallet to another wallet)
    - 6. 현재 지갑에서 보상분(Rewards) ADA 인출(6. Rewards withdrawal to the current wallet)
    - 99. 뒤로 가기(99. Go Back)

- 3. Stake Pool 등록 / 관리(3. Register pool / Manage pool)
    - 1. Stake Key 등록(기본 등록은 2ADA가 소요되고, 전송 수수료가 추가 소요됩니다.)(1. Register stake key(It requires 2 ADA at deposit, and it's required a few transaction fee.))
    - 2. Stake Key 철회(보증금으로 등록한 2ADA를 돌려 받을 수 있습니다.)(2. Withdrawal of Stake Key(You can get back the 2ADA that registered as a deposit.))
    - 3. Stake Key를 Pool에 위임(3. Delegate Stake Key to Pool)
    - 4. Metadata JSON 파일 생성(4. Metadata JSON file creation)
    - 5. Pool 등록 (Stake Key를 먼저 등록해야 하며, Pool 등록은 500ADA가 소요되고, 이 후 갱신 시부터는 전송 수수료만 추가 소요됩니다.)(5. Pool information registration or update (You must register stake key at first, and it takes 500 ADA at deposit. After that, it will be charged only transaction fee at modification.))
    - 6. Pool 수정 (6. Pool modification)
    - 7. Pool 철회 (보증금으로 등록한 500ADA를 돌려 받을 수 있습니다.)(7. Withdrawal of Pool (You can get back the 500ADA that registered as a deposit.))
    - 8. Pool 등록 상태 확인(8. Check the status of pool registration)
    - 9. KES Key Rotation(9. KES Key Rotation)
    - 99. 뒤로 가기(99. Go Back)
```

# Installation

## Delevopment Environment
- Install `java 1.8 or higher`
- Install `Apache Maven latest`

## Initial Configuration
- config.properties 파일 작성 [ex)https://raw.githubusercontent.com/hyperlinkpool/cardano-node-tools/main/src/main/resources/config.properties]
```diff
- 아래 Properties 중 cardano.keys 로 시작되는 각종 키 정보(skey, vkey, etc)는 반드시 백업을 해 두시기 바랍니다.(Among below the properties, you make sure to back up various key information starting with [cardano.keys].)
- 이 Key들은 Daedalus 지갑의 복구 단어와 동일한 효과를 지닌 Key입니다. 유출되면 여러분의 소중한 자산을 강탈당할 수 있습니다.(These keys have the same effect as the recovery words of Daedalus wallet. If these are leaked, your valuable assets can be robbed.)
- 이 Key들은 ADA인출, Rewards인출, Pool생성, Pool위임, 철회등을 하는데 사용되는 매우 중요한 파일들이므로 보관 및 관리에 주의하시기 바랍니다.(These keys are very important files used for ADA withdrawal, Rewards withdrawal, Pool creation, Pool delegation, and withdrawal, so please be careful to store and manage them.)
```

```jsx
#Cardano Command
cardano.cli.path=[카르다노 노드 실행 파일 경로를 작성해 주세요.(Please write down the path to the Cardano node executable file.)]
cardano.cli.name=cardano-cli
cardano.node.name=cardano-node

#Cardano Configuration Files
mainnet.shelley.genesis.json.path=[mainnet-shelley-genesis.json 설정 파일의 경로를 작성해 주세요.(Please write down the path to the mainnet-shelley-genesis.json configuration file.)]

#Cardano keys names
cardano.keys.folder=[각종 키 및 인증서 등이 생성되는 폴더 경로를 작성해 주세요.(Please write down in the folder path where various keys and certificates are generated.)]
cardano.keys.payment.vkey=payment.vkey
cardano.keys.payment.skey=payment.skey
cardano.keys.payment.addr=payment.addr
cardano.keys.stake.vkey=stake.vkey
cardano.keys.stake.skey=stake.skey
cardano.keys.stake.addr=stake.addr
cardano.keys.stake.cert=stake.cert
cardano.keys.stake.deregister.cert=stake.deregister.cert
cardano.keys.stake.delegate.cert=stake.delegate.cert
cardano.keys.cold.vkey=cold.vkey
cardano.keys.cold.skey=cold.skey
cardano.keys.cold.counter=cold.counter
cardano.keys.vrf.vkey=vrf.vkey
cardano.keys.vrf.skey=vrf.skey
cardano.keys.kes.vkey=kes.vkey
cardano.keys.kes.skey=kes.skey
cardano.keys.node.cert=node.cert
cardano.keys.protocol.json=protocol.json
cardano.keys.tx.draft=tx.draft
cardano.keys.tx.raw=tx.raw
cardano.keys.tx.signed=tx.signed
cardano.keys.pool.metadata.json=poolmetadata.json
cardano.keys.pool.registration.cert=pool-registration.cert
cardano.keys.pool.delegation.cert=delegation.cert

# OS parameter
os.ld.library.path.key=LD_LIBRARY_PATH
os.ld.library.path.value=[카르다노 설치 시에 설정하는 라이브러리 경로를 작성해 주세요.(Please write down the library path to set when installing Cardano.)]
os.pkg.config.path.key=PKG_CONFIG_PATH
os.pkg.config.path.value=[카르다노 설치 시에 설정하는 라이브러리 경로를 작성해 주세요.(Please write down the library path to set when installing Cardano.)]
cardano.node.socket.path.key=CARDANO_NODE_SOCKET_PATH
cardano.node.socket.path.value=[카르다노 노드의 소켓 파일 경로를 작성해 주세요.(Please write down the path to the socket file of the Cardano node.)]

#Cardano validation Keys
cardano.validations.manualfeeallowpercentage=10

#Core Node Metrics Url
cardano.core.node.metrics.url=http://localhost:12798/metrics

#command debug mode
command.debug.mode=false

#logging path
logging.path=[노드 실행 이력을 저장하는 로그 파일의 경로를 작성해 주세요.(Please write down the path to the log file that stores the node execution history.)]

#Language Setting / Support languages : ko, en, ja
#운영 체체의 기본 언어셋을 이용하지만, 아래에 언어셋을 작성하면 해당 언어로 보여집니다.(The default language set of the operating system is used, but if you write the language set below, it will be displayed in that language.)
user.language=ko
```

## Compile
- Run `git clone https://github.com/hyperlinkpool/cardano-node-tools.git`
- Run `cd cardano-node-tools`
- Run `mvn package` 또는 Run `mvn install` 실행
- `/target` 폴더에 cardano-node-tools-0.1.0-Beta-Release-jar-with-dependencies.jar 생성 확인

## Execution Application
- -Dconfig.path : 위에서 작성한 config.properties내용이 포함되어 있는 파일의 전체 경로를 작성해야 합니다.(The full path of the file containing the contents of config.properties created above must be written.)
- Run `java -Dconfig.path=/home/cardano/tools/config.properties -jar cardano-node-tools-0.1.0-Beta-Release-jar-with-dependencies.jar`

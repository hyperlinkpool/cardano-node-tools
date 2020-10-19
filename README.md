# Introduction
- 안녕하세요. 한국 카르다노 Stake Pool Operator HYPER입니다. 노드 설치 후에 셋팅과 관리를 도와주는 툴을 제작하게 되었습니다.
- 그 동안 Pool을 운영하면서 수작업으로 Command를 작성하던 작업을 시스템화하였으며, 수작업으로 발생할 수 있는 실수를 줄일 수 있도록 개발했습니다.
- 노드를 관리하면서 사용하는 cardano-cli를 어느 정도 자동화할 수 있습니다.
- 악성 소프트웨어로 오해받을 수 있기 때문에 Java를 이해하시는 Pool운영자가 사용해 보셨으면 좋겠습니다.
- Block Producer 노드가 설치되어 있는 서버에서 Java로 실행하는 소프트웨어 입니다.

- **Withdrawing rewards 개발중**
- **Retiring a Stake Pool 개발중**

- **TestNet은 지원하지 않습니다.**
- **면책조항 1 : 본 프로그램의 잘못된 사용으로 야기된 사항에 대해 제작자는 일체 책임이 없음을 말씀드립니다.**
- **면책조항 2 : 본 프로그램은 Freeware입니다. 따라서 사용의 강제성은 없으며, 수정, 배포, 사용 역시 자유이며, 이에 따른 책임은 사용자에게 있습니다.**

## Supported OS
- Java를 실행할 수 있는 모든 운영체제.
- Cardano Node가 실행되는 서버에서 실행 가능.

## Developer Infomation
- Developer : Hyperlink Pool SPO
- Ticker : HYPER
- Pool ID : 263498e010c7a49bbfd7c4e1aab29809fca7ed993f9e14192a75871e

## Social
- E-mail : hyperlinkpool@gmail.com
- TWITTER : https://twitter.com/HYPERLINKPOOL
- Telegram : https://t.me/HYPERLINKPOOL
- GITHUB : https://github.com/hyperlinkpool

## 주요 기능
1. Key, 인증서 생성
2. ADA Balance Check, ADA 인출, Pool Rewards 인출
3. Pool 등록 및 관리

## Menu Tree
```jsx
- 1. Key 생성, Stake 인증서 생성
    - 1. Payment Key Pair 생성
    - 2. Stake Key Pair 생성
    - 3. Payment Address 생성
    - 4. Stake Address 생성
    - 5. Stake Certificate 생성
    - 6. Stake Pool Cold Keys & Cold Counter 생성
    - 7. Stake Pool VRF Key Pair 생성
    - 8. Stake Pool KES Key Pair 생성
    - 9. Node Operation Certificate 생성\
    - 99. 뒤로 가기
- 2. ADA Balance Check, Pool에서 ADA 인출
    - 1. 현재 Stake Pool의 ADA Balance Check
    - 2. 다른 주소의 ADA Balance Check
    - 3. 현재 Stake Pool의 ADA Reward Balance Check
    - 4. 현재 Pool에서 다른 주소로 ADA 인출
    - 5. 현재 Pool에서 보상분(Rewards) ADA 인출
    - 99. 뒤로 가기
- 3. Stake Pool 등록 / 관리
    - 1. Stake Key 등록(기본 등록은 2ADA가 소요되고(2020.10.09 기준 정보), 전송 수수료가 추가 소요됩니다.)
    - 2. Metadata JSON 파일 생성
    - 3. Pool 정보 등록 또는 갱신(Stake Key를 먼저 등록해야 하며, Pool 등록은 500ADA가 소요되고(2020.10.09 기준 정보), 이 후 갱신부터는 전송 수수료만 추가 소요됩니다.)
    - 4. Pool 등록 상태 확인
    - 5. KES Key Rotation
    - 99. 뒤로 가기
```

# Installation

## Delevopment Environment & Source Check out
- Install `java 1.8 or higher`
- Install `Apache Maven latest`

## Initial Configuration
- config.properties 파일 작성 [참고:https://raw.githubusercontent.com/hyperlinkpool/cardano-node-tools/main/src/main/resources/config.properties]
- Key Folder에 생성된 각종 키 정보는 반드시 백업을 해 두시기 바랍니다.
```jsx
#Cardano Command
cardano.cli.path=[카르다노 노드 실행 파일 경로를 작성해 주세요.]
cardano.cli.name=cardano-cli
cardano.node.name=cardano-node

#Cardano Configuration Files
mainnet.shelley.genesis.json.path=[mainnet-shelley-genesis.json 설정 파일의 경로를 작성해 주세요.]

#Cardano keys names
cardano.keys.folder=[각종 키 및 인증서 등이 생성되는 폴더 경로를 작성해 주세요.]
cardano.keys.payment.vkey=payment.vkey
cardano.keys.payment.skey=payment.skey
cardano.keys.payment.addr=payment.addr
cardano.keys.stake.vkey=stake.vkey
cardano.keys.stake.skey=stake.skey
cardano.keys.stake.addr=stake.addr
cardano.keys.stake.cert=stake.cert
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
os.ld.library.path.value=[카르다노 설치 시에 설정하는 라이브러리 경로를 작성해 주세요.]
os.pkg.config.path.key=PKG_CONFIG_PATH
os.pkg.config.path.value=[카르다노 설치 시에 설정하는 라이브러리 경로를 작성해 주세요.]
cardano.node.socket.path.key=CARDANO_NODE_SOCKET_PATH
cardano.node.socket.path.value=[카르다노 노드의 소켓 파일 경로를 작성해 주세요.]

#Cardano validation Keys
cardano.validations.manualfeeallowpercentage=10

#Core Node Metrics Url
cardano.core.node.metrics.url=http://localhost:12798/metrics

#command debug mode
command.debug.mode=false

#logging path
logging.path=[노드 실행 이력을 저장하는 로그 파일의 경로를 작성해 주세요.]

#Language Setting / Support languages : ko, en, ja
#운영 체체의 기본 언어셋을 이용하지만, 아래에 언어셋을 작성하면 해당 언어로 보여집니다.
user.language=ko
```

## Compile
- Run `git clone https://github.com/hyperlinkpool/cardano-node-tools.git`
- Run `cd cardano-node-tools`
- Run `mvn package` 또는 Run `mvn install` 실행
- `/target` 폴더에 cardano-node-tools-0.1.0-Beta-Release-jar-with-dependencies.jar 생성 확인

## Exceution Application
- -Dconfig.path : 위에서 작성한 config.properties내용이 포함되어 있는 파일의 전체 경로를 작성해야 합니다.
- Run `java -Dconfig.path=/home/cardano/tools/config.properties -jar cardano-node-tools-0.1.0-Beta-Release-jar-with-dependencies.jar`
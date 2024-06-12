# 전자봉투 생성 및 검증 시스템
2024-1 웹코드보안 과목 repo<br/><br/>

임의의 문서에 대하여 전자봉투를 생성하는 기능과 해당 문서의 전자봉투를 검증하는 기능을 제공합니다. <br/>안전한 데이터 전송과 전자서명을 통한 데이터의 기밀성, 무결성, 그리고 부인 방지를 지원합니다.
<br/><br/>

### 주요 기능:

- **키 생성:** 
  - 사용자 이름과 대칭키, 공개키, 사설키 파일 이름을 입력하면 각 키가 생성되고 파일이 저장됩니다. <br/>생성된 키는 전자봉투 생성 및 검증 시 사용됩니다.
  - 대칭키 알고리즘으로 AES, 비대칭키 알고리즘으로 RSA를 사용합니다.
  <br/>
- **전자봉투 생성:** 
  - 사용자는 전송자와 수신자의 정보를 입력하고, 파일 업로드 또는 데이터를 직접 입력하여 전자봉투를 생성할 수 있습니다.
  - 전자봉투 생성 시 데이터를 해시하여 전자서명을 생성하고, 생성된 전자서명과 데이터를 암호화한 후 전자봉투에 포함시킵니다.
  - 서명 알고리즘으로 SHA1withRSA를 사용합니다.
  <br/>
- **전자봉투 검증:** 
  - 수신자는 전자봉투를 확인하고, 데이터의 무결성을 검증할 수 있습니다. <br/>전자서명을 통해 데이터가 변경되지 않았는지 확인하고, 안전하게 수신되었는지 확인할 수 있습니다.
<br/>

## 1. 시나리오
![002](https://github.com/yslle/Digital-Envelope/assets/88431909/90a661a4-6188-4760-ae76-94a7720c3429)    
<br/>
## 2. UI
![003](https://github.com/yslle/Digital-Envelope/assets/88431909/8fcc915f-8d45-406f-a10c-a0f9549cae4d)
![004](https://github.com/yslle/Digital-Envelope/assets/88431909/f14c134c-77a6-487f-b18a-65915bd49091)
![005](https://github.com/yslle/Digital-Envelope/assets/88431909/63dc187a-4af1-4729-9f66-7a47a5207f02)
![006](https://github.com/yslle/Digital-Envelope/assets/88431909/9d46d3fb-395d-471a-9f03-07a2f889a18d)
![007](https://github.com/yslle/Digital-Envelope/assets/88431909/1d79955d-acb3-4e21-9362-d87f7ab1b05a)    
<br/>
## 3. 클래스 디자인
![008](https://github.com/yslle/Digital-Envelope/assets/88431909/834102e9-ca90-4031-81e8-ce0328e51167)
![009](https://github.com/yslle/Digital-Envelope/assets/88431909/4436e434-61d1-4e5f-9abe-554025e10d77)
![010](https://github.com/yslle/Digital-Envelope/assets/88431909/97e909c0-2645-4707-8afe-74ded5bc5e0f)
![011](https://github.com/yslle/Digital-Envelope/assets/88431909/ff809a13-97b0-4917-9bc6-0ad2e848af3e)

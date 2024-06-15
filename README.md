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
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/c83c3fea-b071-4a13-a4de-4ce8acbf1851" />
<br/><br/>

## 2. UI
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/1e942f2f-1c25-4830-a55e-26014d6ec7a2" />
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/1b138321-bd2c-40fe-98e0-56d6e631bbb3" />
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/34da8cd7-84fe-44e4-9433-50454d5e6b7e" />
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/a52993ef-4511-48b1-9ae9-dff20ec5e230" />
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/3fdade80-d2d9-4e15-b09b-389f37e78184" />  
<br/><br/>

## 3. 클래스 디자인
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/34ed5f44-f7fd-42b6-8df0-977b6cf91392" />
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/681530f6-afce-403e-a5f6-5067bb430c3d" />
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/04480936-4bb6-4a5e-8819-a65dc71bcca1" />
<img src="https://github.com/yslle/Digital-Envelope/assets/88431909/b26b69a8-0a1a-4ce0-bc15-5d0e69ca02a0" />

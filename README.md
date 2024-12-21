# 🚶 발-밤 (bal-bam)  
운동 부족을 겪고 있는 현대인들에게 꼭 필요한 산책 커뮤니티 서비스, **발-밤**  

---

## 📌 프로젝트 소개  
**발-밤**은 사용자의 걸음 수와 건강 데이터를 기록하고, 산책 커뮤니티 기능을 통해 이웃들과 소통할 수 있는 산책 장려 서비스입니다.
사용자는 자신의 걸음 목표를 설정해 달성률을 확인할 수 있으며, 친구들과 게시물을 공유할 수 있습니다.



### 개발 기간
*2024.11.05 – 2024.11.27*


---

## 팀 소개
<table width="500" align="left">
<tbody>
<tr>
<th>Pictures</th>
<td width="100" align="center">
<a href="https://github.com/eenzzi">
<img src="https://avatars.githubusercontent.com/u/142785172?v=4" width="60" height="60">
</a>
</td>
<td width="100" align="center">
<a href="https://github.com/Sooamazing">
<img src="https://avatars.githubusercontent.com/u/155598826?v=4" width="60" height="60">
</a>
</td>
</tr>
<tr>
<th>Name</th>
<td width="100" align="center">이은지</td>
<td width="100" align="center">박지현</td>

</tr>
<tr>
<th>Role</th>
<td width="150" align="center">
백엔드 주요 도메인 설계 및 구현 <br>
(피드, 팔로우, 게시글, 산책 기록)
<br>
</td>
<td width="150" align="center">
사용자/인증 도메인 개발 <br>
(로그인, 회원가입)

<br>
</td>
</tr>
<tr>
<th>GitHub</th>
<td width="100" align="center">
<a href="https://github.com/eenzzi">
<img src="http://img.shields.io/badge/eenzzi-green?style=social&logo=github"/>
</a>
</td>
<td width="100" align="center">
<a href="https://github.com/youngandmini">
<img src="http://img.shields.io/badge/journnie-green?style=social&logo=github"/>
</a>
</td>

</tr>
</tbody>
</table>


---

## 🔍 아키텍처

![](https://i.imgur.com/9ONCsKc.png) 


## 🔍 ERD


![](https://i.imgur.com/sF9q4f1.png) 

## 🛠️ 기술 스택  

<div align="center">
  

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<br/>  
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white">
<img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/jwt-000000?style=for-the-badge&logo=JSONWebTokens&logoColor=white">
<br/>   
<img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<br/>   

</div>

---

## 🔑 주요 기능 


### 🚶‍♀️ 1. 걸음 기록 및 분석  
- **산책 시작 및 종료**: 사용자는 서비스 내에서 산책을 시작 / 종료하여  자신의 산책을 기록할 수 있습니다.
  
- **목표 설정 및 분석**: 사용자가 설정한 일일 걸음 목표와 실제 걸음 수를 비교하여 주간 걸음 수와 월간 달성률을 통해 보여줍니다.
  
- **오늘의 건강 팁 제공**: OpenAI를 활용해 짧은 오늘의 건강 팁을 제공합니다.

<br>

### 🧑‍🤝‍🧑 2. 팔로우 및 비공개 계정 시스템  
- **팔로우 기능**: 사용자는 팔로우 기능을 통해 그들의 게시물과 활동을 확인할 수 있습니다.
  
- **비공개 계정 설정**: 사용자는 계정을 비공개로 설정할 수 있으며, 비공개 계정의 경우 팔로우 요청이 필요합니다.
  
- **팔로우 요청 시스템**: 비공개 계정 사용자는 팔로우 요청을 수락하거나 거절할 수 있습니다.  
  - 요청이 대기 상태로 남아 있으며, 사용자가 요청을 수락해야만 피드와 게시물을 볼 수 있습니다.

- **팔로우 추천**: 사용자의 위치를 기반으로 팔로우할 만한 사용자를 추천합니다.
  
- **팔로우/팔로잉 목록**: 사용자는 팔로우 및 팔로잉 목록을 확인할 수 있습니다.

<br>

### 🏷️ 3. 커뮤니티 및 태그 기능  
- **게시물 작성 및 공유**: 사용자는 자신의 게시물을 작성할 수 있습니다.

- **좋아요 및 댓글 기능**: 원하는 게시글에 좋아요 및 댓글을 통해 다른 사용자와 소통할 수 있습니다.
  - **댓글 기능(부모-자식 게시글 구조)**: 댓글은 부모 게시글에 속하는 자식 게시글로 저장됩니다. 메인 피드에는 자식 게시글은 노출되지 않습니다.
  
- **사용자 태그 기능**: 게시물 작성 시 친구나 다른 사용자를 태그할 수 있습니다.
  - **태그된 게시물 확인**: 사용자는 자신의 프로필에서 자신이 태그된 게시물을 별도로 확인할 수 있습니다.  


<br>

### 📰 4. 메인 피드 기능
- **메인 피드 조회**: 사용자는 메인 피드에서 다른 사용자의 게시글을 조회할 수 있습니다.
  - **팔로우 피드**: 사용자는 팔로우한 사용자의 게시물을 확인할 수 있습니다.
  - **위치 기반 피드**: 사용자의 현재 위치(위도, 경도)를 기반으로 가까운 게시물이 추천 피드에 노출됩니다.

<br>

### 5. 회원가입 및 로그인 기능
- **회원가입**: 사용자는 이메일과 비밀번호를 통해 회원가입을 진행할 수 있습니다.  
- **로그인**: 이메일 또는 아이디와 비밀번호를 통해 로그인을 할 수 있습니다.
  - **비밀번호 암호화**: 사용자 비밀번호는 해시 처리되어 데이터베이스에 저장됩니다.

---

## 💡 Problem Solving

### 게시글 태그

![](https://i.imgur.com/HifoiPK.png) 


### 건강 데이터

![](https://i.imgur.com/BaSzQtd.png) 


### 예외 처리

![](https://i.imgur.com/JAABxCo.png) 
![](https://i.imgur.com/1CdT1gu.png) 
![](https://i.imgur.com/SiuYkcN.png) 


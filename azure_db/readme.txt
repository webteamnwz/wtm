Azure Sql Service DB Schema

=> wtm_user : 사용자 테이블
user_id, nvarchar(60), pk, not null, 사용자 아이디 (Facebook id, Twitter id, G+ id 등)
user_email, nvarchar(50), null, 사용자 이메일
user_name, nvarchar(30), null, 사용자 이름, 별명
user_img, nvarchar(200), null, 사용자 프로필 이미지(각 sns 프로필 이미지)
user_profile, nvarchar(250), null, 사용자 프로필, 관심
user_group, nvarchar(50), null, 사용자 보유 기술
regdate, datetime, not null, 가입일

*user_id 형식
userid:"Facebook:my-actual-user-id"
userid:"Twitter:my-actual-user-id"
userid:"MicrosoftAccount:my-actual-user-id"
userid:"Google:my-actual-user-id"

=> wtm_room : 공부방 테이블
room_no, int, pk, not null, identity key, 공부방 일련번호
room_title, nvarchar(50), null, 공부방 이름
room_desc, navarchar(250), null, 공부방 설명
start_date, nchar(8), null, 공부방 시작일, yyyy-mm-dd
end_date, nchar(8), null, 공부방 종료일, yyyy-mm-dd
room_manager_id, nvarchar(60), fk, not null, 공부방을 개설한 사용자 일련번호

=> wtm_user_room : 사용자-공부방 join 테이블
user_id, nvarchar(60), pk, not null, 사용자 일련번호
room_no, int, pk, not null 공부방 일련번호
reg_date, nchar(8), 공부방 가입일, yyyymmdd
chk_date, nchar(8), 공부방 check일, yyyymmdd

* chk_date가 오늘날짜이면 chek한 사용자

=> wtm_category : 카테고리 테이블
category_no, int, pk, not null, identity key, 카테고리 일련번호
category_name, nvarchar(15), null, 카테고리 이름

=> wtm_room_category
room_no, int, pk, not null, 공부방 일련번호
category_no, int, pk, not null,  카테고리 일련번호
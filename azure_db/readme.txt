Azure Sql Service DB Schema

=> wtm_user : ����� ���̺�
user_id, nvarchar(60), pk, not null, ����� ���̵� (Facebook id, Twitter id, G+ id ��)
user_email, nvarchar(50), null, ����� �̸���
user_name, nvarchar(30), null, ����� �̸�, ����
user_img, nvarchar(200), null, ����� ������ �̹���(�� sns ������ �̹���)
user_profile, nvarchar(250), null, ����� ������, ����
user_group, nvarchar(50), null, ����� ���� ���
regdate, datetime, not null, ������

*user_id ����
userid:"Facebook:my-actual-user-id"
userid:"Twitter:my-actual-user-id"
userid:"MicrosoftAccount:my-actual-user-id"
userid:"Google:my-actual-user-id"

=> wtm_room : ���ι� ���̺�
room_no, int, pk, not null, identity key, ���ι� �Ϸù�ȣ
room_title, nvarchar(50), null, ���ι� �̸�
room_desc, navarchar(250), null, ���ι� ����
start_date, nchar(8), null, ���ι� ������, yyyy-mm-dd
end_date, nchar(8), null, ���ι� ������, yyyy-mm-dd
room_manager_id, nvarchar(60), fk, not null, ���ι��� ������ ����� �Ϸù�ȣ

=> wtm_user_room : �����-���ι� join ���̺�
user_id, nvarchar(60), pk, not null, ����� �Ϸù�ȣ
room_no, int, pk, not null ���ι� �Ϸù�ȣ
reg_date, nchar(8), ���ι� ������, yyyymmdd
chk_date, nchar(8), ���ι� check��, yyyymmdd

* chk_date�� ���ó�¥�̸� chek�� �����

=> wtm_category : ī�װ� ���̺�
category_no, int, pk, not null, identity key, ī�װ� �Ϸù�ȣ
category_name, nvarchar(15), null, ī�װ� �̸�

=> wtm_room_category
room_no, int, pk, not null, ���ι� �Ϸù�ȣ
category_no, int, pk, not null,  ī�װ� �Ϸù�ȣ
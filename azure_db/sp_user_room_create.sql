CREATE PROCEDURE wtm.sp_user_room_create
    @category_no INT,
    @user_id NVARCHAR(60),
	@room_title NVARCHAR(50),
	@room_desc NVARCHAR(250),
	@start_date NCHAR(8),
	@end_date NCHAR(8)                                                                                                                          
AS
BEGIN TRAN  
  SET NOCOUNT ON
  DECLARE @room_no INT
  
  -- room에 INSERT
  INSERT INTO wtm.wtm_room(room_title, room_desc, start_date, end_date, room_manager_id)
  VALUES(@room_title, @room_desc, @start_date, @end_date, @user_id)

  SET @room_no = @@IDENTITY;

  -- room_category에 INSERT
  INSERT INTO wtm.wtm_room_category(room_no, category_no)
  VALUES(@room_no, @category_no)

  -- user_room에 INSERT // Create하면 자동 참가
  INSERT INTO wtm.wtm_user_room(user_id, room_no, reg_date)
  VALUES(@user_id, @room_no, CONVERT(NVARCHAR(8), wtm.GetLocalDate(DEFAULT), 112)) 

  
IF @@ERROR <>0
  BEGIN
    ROLLBACK TRAN
  END
ELSE
  BEGIN
    COMMIT TRAN
  END
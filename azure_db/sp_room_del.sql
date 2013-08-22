CREATE PROCEDURE [wtm].[sp_room_del]
    @user_id NVARCHAR(60),
	@room_no INT	
AS
  BEGIN TRAN    
    SET NOCOUNT ON

    IF(EXISTS(SELECT TOP 1 * FROM wtm.wtm_room WHERE room_no = @room_no AND room_manager_id = @user_id))
    BEGIN
	  -- room table 삭제
	  DELETE wtm.wtm_room WHERE room_no = @room_no

	  -- room_category table 삭제
	  DELETE wtm.wtm_room_category WHERE room_no = @room_no

	  -- user_room table 삭제
	  DELETE wtm.wtm_user_room WHERE room_no = @room_no	
	END

  IF @@ERROR <>0
    BEGIN
      ROLLBACK TRAN
    END
  ELSE
    BEGIN
      COMMIT TRAN
    END
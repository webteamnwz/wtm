CREATE PROCEDURE [wtm].[sp_room_modify]
    @user_id NVARCHAR(60),
	@category_no INT,
	@room_no INT,
	@room_title NVARCHAR(50),
	@room_desc NVARCHAR(250),
	@start_date NCHAR(8),
	@end_date NCHAR(8)
AS
  BEGIN TRAN    
    SET NOCOUNT ON

    IF(EXISTS(SELECT TOP 1 * FROM wtm.wtm_room WHERE room_no = @room_no AND room_manager_id = @user_id))
    BEGIN
	  -- room update
      UPDATE wtm.wtm_room 
	  SET room_title = @room_title, room_desc = @room_desc, start_date = @start_date, end_date = @end_date
      WHERE room_no = @room_no AND room_manager_id = @user_id

	  -- room_category
	  UPDATE wtm.wtm_room_category 
	  SET category_no = @category_no
	  WHERE room_no = @room_no
	END

  IF @@ERROR <>0
    BEGIN
      ROLLBACK TRAN
    END
  ELSE
    BEGIN
      COMMIT TRAN
    END
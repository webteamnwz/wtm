CREATE PROCEDURE wtm.sp_category_room_modify
    @user_id NVARCHAR(60),
	@room_no INT,
	@category_no_1 INT,
	@category_no_2 INT,
	@category_no_3 INT                                                                                                                        
AS
BEGIN TRAN  
  SET NOCOUNT ON
   
  IF(EXISTS(SELECT TOP 1 * FROM wtm.wtm_room WHERE room_no = @room_no AND room_manager_id = @user_id))
    BEGIN
	  -- Delete room_category
	  DELETE wtm.wtm_room_category WHERE room_no = @room_no

	  -- INSERT room_category
	  IF (@category_no_1 <> 0)
	    BEGIN
	      INSERT INTO wtm.wtm_room_category(room_no, category_no) VALUES(@room_no, @category_no_1)
	    END

	  IF (@category_no_2 <> 0)
	    BEGIN
	      INSERT INTO wtm.wtm_room_category(room_no, category_no) VALUES(@room_no, @category_no_2)
	    END

	  IF (@category_no_3 <> 0)
	    BEGIN
	      INSERT INTO wtm.wtm_room_category(room_no, category_no) VALUES(@room_no, @category_no_3)
	    END
	END
  
IF @@ERROR <>0
  BEGIN
    ROLLBACK TRAN
  END
ELSE
  BEGIN
    COMMIT TRAN
  END
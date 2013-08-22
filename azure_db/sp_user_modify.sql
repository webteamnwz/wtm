CREATE PROCEDURE wtm.sp_user_modify
  @user_id NVARCHAR(60),
  @user_name NVARCHAR(30),
  @user_img NVARCHAR(200),
  @user_email NVARCHAR(50),
  @user_profile NVARCHAR(250),
  @user_group NVARCHAR(50),
  @category_no_1 INT,
  @category_no_2 INT,
  @category_no_3 INT
AS
BEGIN TRAN
  SET NOCOUNT ON
  IF(EXISTS(SELECT TOP 1 * FROM wtm.wtm_user WHERE user_id = @user_id))
    BEGIN
	  -- UPDATE User INFO
	  UPDATE wtm.wtm_user 
	  SET user_name = @user_name, 
	  user_img = @user_img,
	  user_email = @user_email,
	  user_profile = @user_profile, 
	  user_group = @user_group
      WHERE user_id = @user_id

	  -- INSERT Category
	  IF (@category_no_1 <> 0)
	    BEGIN
	      INSERT INTO wtm.wtm_user_category(user_id, category_no) VALUES(@user_id, @category_no_1)
	    END

	  IF (@category_no_2 <> 0)
	    BEGIN
	      INSERT INTO wtm.wtm_user_category(user_id, category_no) VALUES(@user_id, @category_no_2)
	    END

	  IF (@category_no_3 <> 0)
	    BEGIN
	      INSERT INTO wtm.wtm_user_category(user_id, category_no) VALUES(@user_id, @category_no_3)
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
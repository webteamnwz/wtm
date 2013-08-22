CREATE PROCEDURE wtm.sp_user
  @user_id NVARCHAR (60)
AS
BEGIN TRAN
  SET NOCOUNT ON
  IF(EXISTS(SELECT TOP 1 * FROM wtm.wtm_user WHERE user_id = @user_id))
    BEGIN
      SELECT *, is_first='false' FROM wtm.wtm_user WHERE user_id = @user_id
    END 
   ELSE 
    BEGIN    
      INSERT INTO wtm.wtm_user(user_id) VALUES(@user_id)
      IF(@@ROWCOUNT > 0) 
        SELECT *, is_first='true' FROM wtm.wtm_user WHERE user_id = @user_id
    END
IF @@ERROR <>0
  BEGIN
    ROLLBACK TRAN
  END
ELSE
  BEGIN
    COMMIT TRAN
  END
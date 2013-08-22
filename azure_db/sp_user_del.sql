CREATE PROCEDURE [wtm].[sp_user_del]
	@user_id NVARCHAR(60)
AS
  BEGIN TRAN    
    SET NOCOUNT ON
    -- user table 삭제
	DELETE wtm.wtm_user WHERE user_id = @user_id

	-- user_category table 삭제
	DELETE wtm.wtm_user_category WHERE user_id = @user_id

	-- user_room table 삭제
	DELETE wtm.wtm_user_room WHERE user_id = @user_id
	
  IF @@ERROR <>0
    BEGIN
      ROLLBACK TRAN
    END
  ELSE
    BEGIN
      COMMIT TRAN
    END
CREATE PROCEDURE [wtm].[sp_user_room_cnt]
	@user_id NVARCHAR(60)	
AS
BEGIN TRAN  
  DECLARE @create_room_cnt INT, @join_room_cnt INT
    	
  SET NOCOUNT ON

  -- user 개설 room 정보
  SELECT @create_room_cnt = COUNT(*)
  FROM wtm.wtm_room r
  INNER JOIN wtm.wtm_user u
  ON r.room_manager_id = u.user_id
  WHERE u.user_id= @user_id

  -- user 참여 room 정보
  SELECT @join_room_cnt = COUNT(*)
  FROM wtm_user_room
  WHERE user_id = @user_id

  -- 결과 출력
  SELECT @create_room_cnt create_room_cnt, @join_room_cnt join_room_cnt

IF @@ERROR <>0
  BEGIN
    ROLLBACK TRAN
  END
ELSE
  BEGIN
    COMMIT TRAN
  END
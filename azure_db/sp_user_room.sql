CREATE PROCEDURE [wtm].[sp_user_room]
	@user_id NVARCHAR(60),
	@s_no INT,
	@e_no INT		
AS
BEGIN TRAN  
  SET NOCOUNT ON
	
	-- user참여 room 정보
  IF @s_no <> 0 AND @e_no <> 0
    BEGIN
	    SELECT room_no, room_title, room_desc, start_date, end_date, room_manager_id, cnt_join_user, cnt_chk_user, is_checked
		FROM
		(
			SELECT ROW_NUMBER() OVER (ORDER BY r.room_no DESC) AS rownum, r.*,
			(
			  SELECT COUNT(*) 
			  FROM wtm.wtm_user_room
			  WHERE room_no = r.room_no
			) AS cnt_join_user,
			(
			  SELECT COUNT(*) 
			  FROM wtm.wtm_user_room
			  WHERE room_no = r.room_no
			  AND chk_date = CONVERT(NVARCHAR(8), wtm.GetLocalDate(DEFAULT), 112)
			) AS cnt_chk_user,
		    (
		      SELECT 
		      CASE WHEN (COUNT(*) > 0) THEN 'true' ELSE 'false' END 
		      FROM wtm.wtm_user_room
		      WHERE room_no = r.room_no
		      AND user_id = @user_id
		      AND chk_date = CONVERT(NVARCHAR(8), wtm.GetLocalDate(DEFAULT), 112)
		    ) AS is_checked
			FROM wtm.wtm_room r
			INNER JOIN wtm.wtm_user_room ur
			ON r.room_no = ur.room_no
			WHERE ur.user_id = @user_id
	    ) T1 WHERE rownum BETWEEN @s_no AND @e_no
    END
  ELSE
    BEGIN  
		SELECT r.*,
		(
		  SELECT COUNT(*) 
		  FROM wtm.wtm_user_room
		  WHERE room_no = r.room_no
		) AS cnt_join_user,
		(
		  SELECT COUNT(*) 
		  FROM wtm.wtm_user_room
		  WHERE room_no = r.room_no
		  AND chk_date = CONVERT(NVARCHAR(8), wtm.GetLocalDate(DEFAULT), 112)
		) AS cnt_chk_user,
		(
		  SELECT 
		  CASE WHEN (COUNT(*) > 0) THEN 'true' ELSE 'false' END 
		  FROM wtm.wtm_user_room
		  WHERE room_no = r.room_no
		  AND user_id = @user_id
		  AND chk_date = CONVERT(NVARCHAR(8), wtm.GetLocalDate(DEFAULT), 112)
		) AS is_checked
		FROM wtm.wtm_room r
		INNER JOIN wtm.wtm_user_room ur
		ON r.room_no = ur.room_no
		WHERE ur.user_id = @user_id
    END

IF @@ERROR <>0
  BEGIN
    ROLLBACK TRAN
  END
ELSE
  BEGIN
    COMMIT TRAN
  END
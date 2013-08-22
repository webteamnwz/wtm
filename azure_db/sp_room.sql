CREATE PROCEDURE wtm.sp_room
	@room_no INT
AS
BEGIN TRAN  
  DECLARE @cnt_join INT, @cnt_chk INT, @category_ids NVARCHAR(30)

  SET NOCOUNT ON
  
  -- room에 join한 사용자 수
  SELECT @cnt_join = COUNT(*) 
  FROM wtm.wtm_user_room 
  WHERE room_no = @room_no
  
  -- chk_date가 오늘 날짜와 같으면 Check
  SELECT @cnt_chk = COUNT(*) 
  FROM wtm.wtm_user_room 
  WHERE room_no = @room_no 
  AND chk_date = CONVERT(NVARCHAR(8), wtm.GetLocalDate(DEFAULT), 112)

  -- category_ids 가져오기, 한 room은 여러 Category에 속할 수 있음 1|2 형식으로 가져온다.
  SET @category_ids = null
  SELECT  @category_ids = 
  (
    CASE
      WHEN (@category_ids is not null) 
	    THEN @category_ids + '|' + LTRIM(STR(category_no))
	    ELSE LTRIM(STR(category_no))
    END
  )
   FROM wtm.wtm_room_category              
   WHERE room_no = 1

  -- room 정보 가져오기  
  SELECT *, @cnt_join cnt_join, @cnt_chk cnt_chk, @category_ids category_ids
  FROM wtm.wtm_room
  WHERE room_no = @room_no 

IF @@ERROR <>0
  BEGIN
    ROLLBACK TRAN
  END
ELSE
  BEGIN
    COMMIT TRAN
  END
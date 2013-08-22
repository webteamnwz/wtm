-> GMT+9 현재 시간을 출력한다.
-> Azure SQL Database의 시간은 GMT 기준이다.
따라서 Getdate()와 같은 시간 함수를 사용하면 Local 시간을 구할 수 없다.
-> GetLocalDate() 함수는 Local(서울) 시간을 출력하는 스칼라 함수이다.
-> Default는 GMT+9(서울) 기준이다.

CREATE FUNCTION [wtm].[GetLocalDate]
(
  @TimezoneDiffInHour TINYINT = 9 -- GMT+9 Seoul
)
RETURNS DATETIME
AS
BEGIN
  RETURN DATEADD(Hh, @TimezoneDiffInHour, GETUTCDATE())
END

사용법 : SELECT wtm.GetLocalDate(DEFAULT) or
             SELECT wtm.GetLocalDate(7) or
             SELECT wtm.GetLocalDAte(5) 
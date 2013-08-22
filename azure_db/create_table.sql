-- Category Table
CREATE TABLE [wtm].[wtm_category] (
    [category_no]   INT           IDENTITY (1, 1) NOT NULL,
    [category_name] NVARCHAR (15) NOT NULL,
    PRIMARY KEY CLUSTERED ([category_no] ASC)
);

-- Study Room Table
CREATE TABLE [wtm].[wtm_room] (
    [room_no]         INT            IDENTITY (1, 1) NOT NULL,
    [room_title]      NVARCHAR (50)  NULL,
    [room_desc]       NVARCHAR (250) NULL,
    [start_date]      NCHAR (8)      NULL,
    [end_date]        NCHAR (8)      NULL,
    [room_manager_id] NVARCHAR (60)  NOT NULL,
    PRIMARY KEY CLUSTERED ([room_no] ASC)
);

-- Category-Room 관계 Table
CREATE TABLE [wtm].[wtm_room_category] (
    [room_no]     INT NOT NULL,
    [category_no] INT NOT NULL,
    CONSTRAINT [PrimaryKey_8e1edc8b-b2fd-4924-9ff4-b60dea83080f] PRIMARY KEY CLUSTERED ([room_no] ASC, [category_no] ASC)
);

-- User Table
CREATE TABLE [wtm].[wtm_user] (
    [user_id]      NVARCHAR (60)  NOT NULL,
    [user_email]   NVARCHAR (50)  NULL,
    [user_name]    NVARCHAR (30)  NULL,
    [user_img]     NVARCHAR (200) NULL,
    [user_profile] NVARCHAR (250) NULL,
    [user_group]   NVARCHAR (50)  NULL,
    [regdate]      DATETIME       DEFAULT (getdate()) NULL,
    CONSTRAINT [PK_wtm_user] PRIMARY KEY CLUSTERED ([user_id] ASC)
);

-- User-Category 관계 Table
CREATE TABLE [wtm].[wtm_user_category] (
    [user_id]     NVARCHAR (60) NOT NULL,
    [category_no] INT           NOT NULL,
    CONSTRAINT [PrimaryKey_782bb619-7a54-4775-9117-125a65a9fc0c] PRIMARY KEY CLUSTERED ([user_id] ASC, [category_no] ASC)
);

-- User-Room 관계 Table
CREATE TABLE [wtm].[wtm_user_room] (
    [user_id]  NVARCHAR (60) NOT NULL,
    [room_no]  INT           NOT NULL,
    [reg_date] NCHAR (8)     NULL,
    [chk_date] NCHAR (8)     NULL,
    CONSTRAINT [PrimaryKey_f420ab19-b38a-48dc-ae4e-4d4bb1634274] PRIMARY KEY CLUSTERED ([user_id] ASC, [room_no] ASC)
);
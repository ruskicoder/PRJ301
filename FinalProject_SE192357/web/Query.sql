    create database FinalProject_SE192357
    use FinalProject_SE192357
    drop database FinalProject_SE192357
    go

    -- tblUsers
    CREATE TABLE tblUsers (
        Username VARCHAR(10) PRIMARY KEY,
        Fullname NVARCHAR(128) NOT NULL,
        Password VARCHAR(64) NOT NULL,
        Role NVARCHAR(50) NOT NULL,
        Money INT DEFAULT 0,
        UserImage TEXT,
        CitizenID BIGINT NOT NULL,
		DOB DATE NOT NULL,
    );

    -- tblArticles
    CREATE TABLE tblArticles (
        ArticleID VARCHAR(10) PRIMARY KEY,
        Title NVARCHAR(128) NOT NULL,
        Subtitle NVARCHAR(128) DEFAULT NULL,
        Author NVARCHAR(128) DEFAULT 'N/A',
        Content NVARCHAR(4000),
        Thumbnail TEXT,
	ArticleType NVARCHAR(128) DEFAULT N'Khác',
	PublishDate Date default GETDATE()
    );

    -- tblCars
    CREATE TABLE tblCars (
        Plate VARCHAR(8) PRIMARY KEY,
        CarName NVARCHAR(64) NOT NULL,
        CarImage TEXT,
        OwnerName NVARCHAR(128) NOT NULL,
        CarType NVARCHAR(64) NOT NULL,
        PlateType NVARCHAR(10) NOT NULL,
        RegDate DATE NOT NULL,
        FirstRegDate DATE NOT NULL,
        MfdDate DATE NOT NULL
    );

    -- tblViolation
    CREATE TABLE tblViolation (
        ViolationID varchar(10) primary key,
        Plate VARCHAR(8),  
        Violation NVARCHAR(50) NOT NULL,
        VImage TEXT,
        Status NVARCHAR(50) NOT NULL DEFAULT 'Pending',
        VDate DATE NOT NULL,
        Fine INT DEFAULT 0
    );

    CREATE TABLE tblLicense(
		LicenseID varchar(12) PRIMARY KEY,
		FullName nvarchar(128) NOT NULL,
		LicenseType varchar(2) NOT NULL,
		LRegDate DATE NOT NULL,
		ExpDate DATE NOT NULL
	);
        
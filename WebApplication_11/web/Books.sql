
-- Tạo bảng Sách
CREATE TABLE tblBooks (
    BookID Char(5) PRIMARY KEY,
    Title NVARCHAR(200) NOT NULL,
    Author NVARCHAR(100) NOT NULL,
    PublishYear INT,
    Price DECIMAL(10,2),
    Quantity INT DEFAULT 0,
);

-- Thêm dữ liệu mẫu
INSERT INTO tblBooks (BookID, Title, Author, PublishYear, Price, Quantity) VALUES
('B0001', N'Dế Mèn Phiêu Lưu Ký', N'Tô Hoài', 1941, 65000, 10),
('B0002', N'Số Đỏ', N'Vũ Trọng Phụng', 1936, 75000, 5),
('B0003', N'Nhật Ký Trong Tù', N'Hồ Chí Minh', 1943, 55000, 8),
('B0004', N'Truyện Kiều', N'Nguyễn Du', 1820, 85000, 15),
('B0005', N'Tắt Đèn', N'Ngô Tất Tố', 1937, 60000, 12),
('B0006', N'Chí Phèo', N'Nam Cao', 1941, 45000, 20),
('B0007', N'Vang Bóng Một Thời', N'Nguyễn Tuân', 1940, 70000, 7),
('B0008', N'Những Ngã Tư Và Những Cột Đèn', N'Trần Đăng Khoa', 1968, 50000, 9),
('B0009', N'Bên Kia Bờ Hy Vọng', N'Nguyễn Ngọc Thuần', 2005, 80000, 6),
('B0010', N'Mắt Biếc', N'Nguyễn Nhật Ánh', 1990, 95000, 25);

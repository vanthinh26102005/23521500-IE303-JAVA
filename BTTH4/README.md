# Báo cáo Bài thực hành 04

## 1. Yêu cầu bài thực hành

Dựa trên bài thực hành 03, xây dựng cơ sở dữ liệu để lưu thông tin sản phẩm và truy vấn sản phẩm từ cơ sở dữ liệu.

Trong bài này, giao diện bán sản phẩm từ bài thực hành 03 được giữ lại. Điểm thay đổi chính là danh sách sản phẩm không còn được khai báo trực tiếp trong mã nguồn Java, mà được lưu trong cơ sở dữ liệu SQLite và đọc lên bằng JDBC.

## 2. Công nghệ sử dụng

- Ngôn ngữ lập trình: Java
- Thư viện giao diện: Java Swing
- Cơ sở dữ liệu: SQLite
- Kết nối CSDL: JDBC
- Driver: `lib/sqlite-jdbc.jar`

## 3. Cấu trúc thư mục

```text
BTTH4/
|-- BTTH4.java
|-- products.db
|-- lib/
|   `-- sqlite-jdbc.jar
|-- img1.png
|-- img2.png
|-- img3.png
|-- img4.png
|-- img5.png
|-- img6.png
`-- sample.png
```

Trong đó:

- `BTTH4.java`: chương trình chính, gồm giao diện Swing và logic kết nối CSDL.
- `products.db`: file cơ sở dữ liệu SQLite lưu thông tin sản phẩm.
- `lib/sqlite-jdbc.jar`: thư viện JDBC driver để Java kết nối SQLite.
- `img1.png` đến `img6.png`: ảnh sản phẩm hiển thị trên giao diện.

## 4. Thiết kế cơ sở dữ liệu

Chương trình sử dụng bảng `products` để lưu thông tin sản phẩm.

```sql
CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    brand TEXT NOT NULL,
    price REAL NOT NULL,
    image_path TEXT NOT NULL
);
```

Ý nghĩa các cột:

- `id`: mã sản phẩm, tự động tăng.
- `name`: tên sản phẩm.
- `description`: mô tả sản phẩm.
- `brand`: thương hiệu.
- `price`: giá sản phẩm.
- `image_path`: tên file ảnh của sản phẩm.

## 5. Luồng xử lý của chương trình

Khi chương trình chạy, class `BTTH4` thực hiện các bước sau:

1. Kết nối đến SQLite bằng chuỗi kết nối `jdbc:sqlite:products.db`.
2. Tạo bảng `products` nếu bảng chưa tồn tại.
3. Kiểm tra số lượng dữ liệu trong bảng.
4. Nếu bảng đang rỗng, chương trình thêm 8 sản phẩm mẫu giống bài thực hành 03.
5. Truy vấn danh sách sản phẩm bằng câu lệnh:

```sql
SELECT id, name, description, brand, price, image_path
FROM products
ORDER BY id;
```

6. Chuyển từng dòng dữ liệu thành đối tượng `Product`.
7. Hiển thị danh sách sản phẩm lên giao diện Swing.

## 6. Chức năng đã thực hiện

- Hiển thị giao diện bán sản phẩm Adidas như bài thực hành 03.
- Hiển thị danh sách 8 sản phẩm ở phía bên phải.
- Hiển thị thông tin sản phẩm đang chọn ở phía bên trái.
- Khi click vào một sản phẩm, thông tin và hình ảnh bên trái được cập nhật.
- Có hiệu ứng chuyển ảnh khi thay đổi sản phẩm.
- Dữ liệu sản phẩm được lưu và truy vấn từ SQLite.
- Chương trình tự tạo bảng và tự thêm dữ liệu mẫu nếu CSDL chưa có dữ liệu.
- Khi chạy lại chương trình, dữ liệu không bị thêm trùng lặp.

## 7. Cách biên dịch và chạy

Mở terminal tại thư mục `BTTH4`.

Biên dịch:

```bash
javac -cp "lib/sqlite-jdbc.jar" BTTH4.java
```

Chạy chương trình trên macOS/Linux:

```bash
java -cp ".:lib/sqlite-jdbc.jar" BTTH4
```

Nếu chạy trên Windows, dùng dấu `;` thay cho dấu `:`:

```bash
java -cp ".;lib/sqlite-jdbc.jar" BTTH4
```

## 8. Kiểm tra cơ sở dữ liệu

Có thể kiểm tra dữ liệu trong SQLite bằng lệnh:

```bash
sqlite3 products.db "SELECT id, name, brand, price, image_path FROM products;"
```

Kết quả gồm 8 sản phẩm đã được lưu trong bảng `products`.

## 9. Kết luận

Bài thực hành 04 đã mở rộng bài thực hành 03 bằng cách thêm cơ sở dữ liệu SQLite để quản lý sản phẩm. Giao diện và chức năng chọn sản phẩm vẫn được giữ lại, nhưng dữ liệu hiển thị trên giao diện được truy vấn từ CSDL thay vì khai báo trực tiếp trong mã nguồn.

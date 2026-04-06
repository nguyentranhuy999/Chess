# Chess

Project cờ vua Java Swing, có bot AI đánh tự động bằng `Owl` (minimax nâng cấp).

## Bot đang nằm ở đâu?

- Bot chính: `src/bot/Owl.java`
- Điểm gọi bot mỗi frame: `src/main/GamePanel.java`
- Mặc định bot cầm quân đen (`-1`) và depth = `3`:
  - `this.minimaxBot = new Owl(-1, 3);`

## Luồng hoạt động của bot

Mỗi lần tới lượt bot:

1. `update()` kiểm tra trạng thái game (`end`, `moving`, `promotion`).
2. Bot lấy tất cả nước đi hợp lệ từ vị trí hiện tại.
3. Nếu còn trong khai cuộc, thử chọn nước từ opening book.
4. Nếu không có opening move, chạy minimax với iterative deepening.
5. Dùng alpha-beta pruning để cắt nhánh xấu.
6. Dùng transposition table (Zobrist hash) để tái sử dụng kết quả trạng thái đã tính.
7. Đến lá sâu thì chạy quiescence search (chỉ mở rộng nước “ồn” như bắt quân/phong cấp) để giảm horizon effect.
8. Chọn nước có điểm tốt nhất rồi thực thi lên board thật.

## Những kỹ thuật giúp bot đánh “khôn” hơn

- **Minimax + Alpha-Beta**
  - Tối ưu cây tìm kiếm, cho phép đi sâu hơn trong cùng thời gian.

- **Iterative Deepening**
  - Tìm từ depth nhỏ đến depth lớn, luôn có “best move tạm thời”.

- **Opening Book (rule-based)**
  - Một số mẫu khai cuộc phổ biến để đi tự nhiên hơn giai đoạn đầu.

- **Move Ordering**
  - Ưu tiên nước tốt trước (TT move, MVV-LVA capture score, killer move, history heuristic) để alpha-beta cắt mạnh hơn.

- **Transposition Table**
  - Lưu cache trạng thái theo hash để tránh tính lặp.

- **Quiescence Search**
  - Mở rộng thêm ở vị trí chiến thuật nóng (bắt quân/phong cấp), tránh đánh giá “non ổn định”.

- **Evaluation nâng cao**
  - Material score.
  - Piece-Square Tables (PST).
  - Mobility.
  - Bishop pair bonus.
  - King safety.

## Luật đặc biệt bot có xử lý

- Nhập thành (`castling`)
- Bắt tốt qua đường (`en passant`)
- Phong cấp (tự phong hậu trong bot move executor)

## Chỉnh độ khó / phe của bot

Trong `GamePanel`:

```java
this.minimaxBot = new Owl(-1, 3);
```

- Tham số 1 (`botSide`):
  - `-1` = bot cầm đen
  - `1` = bot cầm trắng
- Tham số 2 (`depth`):
  - Tăng depth => bot mạnh hơn nhưng chậm hơn.
  - Gợi ý: `2` (nhanh), `3` (cân bằng), `4+` (nặng CPU).

## Build nhanh

```bash
javac -encoding UTF-8 -d out/build $(find src -name '*.java')
java -cp "out/build:src" main.Main
```

Windows:

```bash
java -cp "out/build;src" main.Main
```

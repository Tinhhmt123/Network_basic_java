package org.example.RAFLab.LAB1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RAFSong {

    private String destFile;
    private static final int HEADER_SIZE = 4;
    private static final long RECORD_SIZE = 56; // 1 (deleted) + 1 (genre) + 50 (name) + 4 (size)

    public RAFSong(String destFile) {
        this.destFile = destFile;
    }

    // Tính vị trí byte bắt đầu của record thứ index (tính từ 0)
    private long getRecordPos(int index) {
        return HEADER_SIZE + RECORD_SIZE * index;
    }

    public void addSong(Song song) {
        if (song == null || song.getName() == null) {
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(destFile, "rw")) {
            int totalSong = 0;
            if (raf.length() >= HEADER_SIZE) {
                raf.seek(0);
                totalSong = raf.readInt();
            }

            // Kiểm tra trùng tên bài hát (không phân biệt hoa/thường, chỉ tính bài chưa xóa)
            if (getIndexByName(raf, song.getName(), totalSong) != -1) {
                System.out.println("Bai hat '" + song.getName() + "' da ton tai!");
                return;
            }

            // Cập nhật lại tổng số bài hát ở Header
            raf.seek(0);
            raf.writeInt(totalSong + 1);

            // Di chuyển con trỏ đến cuối danh sách record và ghi bài hát mới
            raf.seek(getRecordPos(totalSong));
            song.writeRAF(raf);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + destFile);
        } catch (IOException e) {
            System.err.println("Error IO file: " + destFile);
        }
    }

    public boolean delete(String name) {
        if (name == null) {
            return false;
        }
        try (RandomAccessFile raf = new RandomAccessFile(destFile, "rw")) {
            if (raf.length() < HEADER_SIZE) {
                return false;
            }
            raf.seek(0);
            int totalSong = raf.readInt();

            int indexByName = getIndexByName(raf, name, totalSong);
            if (indexByName == -1) {
                return false;
            }

            // Đánh dấu trường Deleted = true tại đầu record
            raf.seek(getRecordPos(indexByName));
            raf.writeBoolean(true);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + destFile);
            return false;
        } catch (IOException e) {
            System.err.println("Error IO file: " + destFile);
            return false;
        }
    }

    public int getIndexByName(RandomAccessFile raf, String name, int totalSong) throws IOException {
        if (name == null) {
            return -1;
        }
        for (int i = 0; i < totalSong; i++) {
            raf.seek(getRecordPos(i));
            boolean isDeleted = raf.readBoolean();
            if (isDeleted) {
                continue; // Bỏ qua bài hát đã bị đánh dấu xóa
            }
            raf.readByte(); // Bỏ qua 1 byte Genre để đến trường Name
            String nameSongCurrent = readNameRAF(raf);
            if (nameSongCurrent.equalsIgnoreCase(name.trim())) {
                return i;
            }
        }
        return -1;
    }

    public String readNameRAF(RandomAccessFile raf) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Song.NAME_LENGTH; i++) {
            char c = raf.readChar();
            if (c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public List<Song> list() {
        List<Song> songs = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(destFile, "rw")) {
            if (raf.length() < HEADER_SIZE) {
                return songs;
            }
            raf.seek(0);
            int totalSong = raf.readInt();

            for (int i = 0; i < totalSong; i++) {
                raf.seek(getRecordPos(i));
                boolean isDeleted = raf.readBoolean();
                if (isDeleted) {
                    continue;
                }
                byte genreSong = raf.readByte();
                String name = readNameRAF(raf);
                float size = raf.readFloat();
                songs.add(new Song(genreSong, name, size));
            }
            return songs;
        } catch (IOException e) {
            System.err.println("Error IO file: " + destFile);
            return songs;
        }
    }

    public Song get(int index) {
        try (RandomAccessFile raf = new RandomAccessFile(destFile, "rw")) {
            if (raf.length() < HEADER_SIZE) {
                return null;
            }
            raf.seek(0);
            int totalSong = raf.readInt();

            // Kiểm tra index nằm ngoài khoảng hợp lệ
            if (index < 0 || index >= totalSong) {
                return null;
            }

            raf.seek(getRecordPos(index));
            boolean isDeleted = raf.readBoolean();
            if (isDeleted) {
                return null;
            }

            byte genreSong = raf.readByte();
            String name = readNameRAF(raf);
            float size = raf.readFloat();
            return new Song(genreSong, name, size);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + destFile);
            return null;
        } catch (IOException e) {
            System.err.println("Error IO file: " + destFile);
            return null;
        }
    }

    public boolean update(int index, Song newSong) {
        if (newSong == null || newSong.getName() == null) {
            return false;
        }
        try (RandomAccessFile raf = new RandomAccessFile(destFile, "rw")) {
            if (raf.length() < HEADER_SIZE) {
                return false;
            }
            raf.seek(0);
            int totalSong = raf.readInt();

            // Kiểm tra index nằm ngoài khoảng hợp lệ
            if (index < 0 || index >= totalSong) {
                return false;
            }

            raf.seek(getRecordPos(index));
            boolean isDeleted = raf.readBoolean();
            if (isDeleted) {
                return false;
            }

            // Kiểm tra tên mới có bị trùng với bài hát khác (chưa xóa) hay không
            int existingIndex = getIndexByName(raf, newSong.getName(), totalSong);
            if (existingIndex != -1 && existingIndex != index) {
                System.out.println("Ten bai hat cap nhat bi trung: " + newSong.getName());
                return false;
            }

            // Quay lại đầu record thứ index để ghi đè thông tin mới
            raf.seek(getRecordPos(index));
            newSong.writeRAF(raf);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + destFile);
            return false;
        } catch (IOException e) {
            System.err.println("Error IO file: " + destFile);
            return false;
        }
    }

    public static void main(String[] args) {
        String filePath = "PlayList.data";
        // Xóa file cũ nếu có để test mới từ đầu
        new File(filePath).delete();

        RAFSong rafSong = new RAFSong(filePath);

        System.out.println("=== 1. TEST ADD SONG ===");
        rafSong.addSong(new Song(1, "Waiting For You", 4.5f));
        rafSong.addSong(new Song(2, "Numb", 3.8f));
        rafSong.addSong(new Song(3, "Fly Me To The Moon", 5.2f));
        rafSong.addSong(new Song(1, "waiting for you", 4.0f)); // Thử thêm trùng tên (khác hoa thường)

        System.out.println("\n=== 2. TEST LIST ===");
        for (Song s : rafSong.list()) {
            System.out.println(s);
        }

        System.out.println("\n=== 3. TEST GET BY INDEX ===");
        System.out.println("Index 1: " + rafSong.get(1));
        System.out.println("Index 10 (ngoai khoang): " + rafSong.get(10));

        System.out.println("\n=== 4. TEST UPDATE ===");
        boolean updated = rafSong.update(1, new Song(2, "In The End", 4.1f));
        System.out.println("Update index 1: " + updated);
        System.out.println("Index 1 sau update: " + rafSong.get(1));

        System.out.println("\n=== 5. TEST DELETE ===");
        boolean deleted = rafSong.delete("in the end"); // Xóa không phân biệt hoa thường
        System.out.println("Delete 'in the end': " + deleted);
        System.out.println("Get index 1 sau khi xoa: " + rafSong.get(1));
        System.out.println("Update index 1 sau khi xoa: " + rafSong.update(1, new Song(2, "New Divide", 3.9f)));

        System.out.println("\n=== 6. DANH SACH CUOI CUNG (KHONG GOM BAI DA XOA) ===");
        for (Song s : rafSong.list()) {
            System.out.println(s);
        }
    }
}

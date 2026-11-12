package org.example.RAFLab.LAB1;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Song {

    public static final int NAME_LENGTH = 25;

    private byte genre; // 1-POP, 2-ROCK, 3-JAZZ
    private String name;
    private float size;

    public Song(byte genre, String name, float size) {
        this.genre = genre;
        this.name = name;
        this.size = size;
    }

    public Song(int genre, String name, float size) {
        this((byte) genre, name, size);
    }

    public byte getGenre() {
        return genre;
    }

    public void setGenre(byte genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    private String getGenreName() {
        switch (genre) {
            case 1: return "POP";
            case 2: return "ROCK";
            case 3: return "JAZZ";
            default: return "UNKNOWN(" + genre + ")";
        }
    }

    @Override
    public String toString() {
        return "Song{" +
                "genre=" + genre + " (" + getGenreName() + ")" +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }

    public void writeCharNameRAF(RandomAccessFile raf, int length) throws IOException {
        for (int i = 0; i < length; i++) {
            if (this.name != null && i < this.name.length()) {
                raf.writeChar(this.name.charAt(i));
            } else {
                raf.writeChar(0); // Đệm bằng ký tự null '\0'
            }
        }
    }

    public void writeRAF(RandomAccessFile raf) throws IOException {
        raf.writeBoolean(false);              // 1 byte: Deleted = false (mặc định)
        raf.writeByte(this.genre);            // 1 byte: Genre
        writeCharNameRAF(raf, NAME_LENGTH);   // 50 bytes: Name (25 ký tự Unicode)
        raf.writeFloat(this.size);            // 4 bytes: Size
    }
}

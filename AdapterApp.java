
interface MediaPlayer {
    void play(String filename);
}

class Mp4Player {
    public void playMp4(String filename) {
        System.out.println("Playing mp4: " + filename);
    }
}

class MediaAdapter implements MediaPlayer {
    private Mp4Player mp4Player = new Mp4Player();
    public void play(String filename) {
        if (filename.endsWith(".mp4"))
            mp4Player.playMp4(filename);
        else
            System.out.println("Unsupported format");
    }
}

public class AdapterApp {
    public static void main(String[] args) {
        MediaPlayer player = new MediaAdapter();
        player.play("song.mp4"); // adapts to Mp4Player
        player.play("song.wav"); // unsupported
    }
}

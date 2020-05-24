import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 零拷贝
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");
        //得到该文件对应的Channel
        FileChannel fileChannel = randomAccessFile.getChannel();
        //MapMode有3种：只读、读写、私密。私密是指在堆外内存建立文件对应内容的一块镜像，对此镜像的修改不会导致文件被修改
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
        //对mappedByteBuffer进行写操作，实际上就是对其映射的堆外内存做写操作；在读写模式下这个修改会同步到文件，私密则不会同步
        mappedByteBuffer.put(0, (byte)'h');
        randomAccessFile.close();
    }
}

package netty;

/**
 * 消息协议
 */
public class MessageProtocol {
    private int len;     //消息头,记录TCP包的长度(字节数)
    private byte[] content;  //TCP包的内容

    public MessageProtocol() {
    }

    public MessageProtocol(int len, byte[] content) {
        this.len = len;
        this.content = content;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}

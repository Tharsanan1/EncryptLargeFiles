import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Main {
    private static long secret = 238423894l;
    private static String targetFile;
    private static String resultFile;
    private static final String ENCRYPT = "E";
    private static final String DECRYPT = "D";

    public static void main(String[] args) throws IOException {
        if (args.length == 4) {
            String task = args[0];
            String password = args[1];
            secret = password.hashCode();
            targetFile = args[2];
            resultFile = args[3];
            System.out.println("Target File : " + targetFile);
            System.out.println("Resulting file : " + resultFile);
            if(ENCRYPT.equals(task)) {
                encryptFileAndWrite(targetFile, resultFile);
            }
            else if(DECRYPT.equals(task)) {
                decryptFileAndWrite(targetFile, resultFile);
            }
        }

        else {
            System.out.println("Incompatible arguments passed. Please pass `task[E | D] secret targetFilePath resultFilePathIncludingRightExtension`");
        }


    }

    private static void encryptFileAndWrite(String pathToRead, String pathToWrite) throws IOException {
        final int BUFFER_SIZE = 1024*1024; //this is actually bytes
        FileInputStream fis = new FileInputStream(pathToRead);
        FileOutputStream fos = new FileOutputStream(pathToWrite);
        byte[] buffer = new byte[BUFFER_SIZE];
        int read = 0;
        while( ( read = fis.read( buffer ) ) > 0 ){
            encrypt(buffer, secret);
            fos.write(buffer);
        }
        fos.flush();
        fos.close();
        fis.close();
    }

    private static void decryptFileAndWrite(String pathToRead, String pathToWrite) throws IOException {
        final int BUFFER_SIZE = 1024*1024; //this is actually bytes
        FileInputStream fis = new FileInputStream(pathToRead);
        FileOutputStream fos = new FileOutputStream(pathToWrite);
        byte[] buffer = new byte[BUFFER_SIZE];
        int read = 0;
        while( ( read = fis.read( buffer ) ) > 0 ){
            decrypt(buffer, secret);
            fos.write(buffer);
        }
        fos.flush();
        fos.close();
        fis.close();
    }

    private static void printArray(byte[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

    private static void encrypt(byte[] array, long secret) {
        Random secureRandom = new Random(secret);
        for (int i = 0; i < array.length; i++) {
            int randomInt = secureRandom.nextInt(257);
            array[i] = rotateRight(array[i], randomInt);
        }
    }

    private static void decrypt(byte[] array, long secret) {
        Random secureRandom = new Random(secret);
        for (int i = 0; i < array.length; i++) {
            int randomInt = secureRandom.nextInt(257);
            array[i] = rotateLeft(array[i], randomInt);
        }
    }

    private static byte rotateLeft(byte b, int i) {
        if(i < 0  || i > 256){
            throw new IllegalArgumentException("Rotator value is wrong : " + i);
        }
        int result = b - i;
        if (result < -128) {
            result = 127 + (result + 129);
        }
        if(result < -128 || result > 127) {
            throw new RuntimeException("Created result is not a byte compatible : " + result);
        }
        return (byte)result;
    }

    private static byte rotateRight(byte b, int i) {
        if(i < 0  || i > 256){
            throw new IllegalArgumentException("Rotator value is wrong : " + i);
        }
        int result = b + i;
        if (result > 127) {
            result = -128 + (result - 128);
        }
        if(result < -128 || result > 127) {
            throw new RuntimeException("Created result is not a byte compatible : " + result);
        }
        return (byte)result;
    }
}

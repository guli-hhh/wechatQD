package com.obtk.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class QRcodeUtils {
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xFFFFFFFF;



    /**
     * 生成二维码
     * @param strJson  二维码参数
     * @param path     保存的服务器路径
     * @param filename  二维码图片名称
     * @return
     */
    public static String tomakeMode(String strJson, String path,String filename) {
        //图片格式
        String filePostfix="png";
        //创建服务器路径下图片文件
        File file = new File(path+"/"+filename+ "."+filePostfix);
        //调用方法生成
        encode(strJson, file,filePostfix, BarcodeFormat.QR_CODE, 5000, 5000, null);
        return filename+".png";
    }

    /**
     *  生成QRCode二维码<br>
     *  在编码时需要将com.google.zxing.qrcode.encoder.Encoder.java中的<br>
     *  static final String DEFAULT_BYTE_MODE_ENCODING = "ISO8859-1";<br>
     *  修改为UTF-8，否则中文编译后解析不了<br>
     * @param contents 二维码的内容
     * @param file 二维码保存的路径，如：C://test_QR_CODE.png
     * @param filePostfix 生成二维码图片的格式：png,jpeg,gif等格式
     * @param format qrcode码的生成格式
     * @param width 图片宽度
     * @param height 图片高度
     * @param hints
     */
    private static void encode(String contents, File file,String filePostfix, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) {
        try {
            //二维码内容的设置
            contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");
            //二维码参数对象
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, format, width, height);
            //生成二位码图片
            writeToFile(bitMatrix, filePostfix, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片<br>
     *
     * @param matrix
     * @param format
     *            图片格式
     * @param file
     *            生成二维码图片位置
     * @throws IOException
     */
    private  static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        /*
         * BufferedImage是Image的一个子类，
         * BufferedImage生成的图片在内存里有一个图像缓冲区，
         * 利用这个缓冲区操作这个图片，
         * 通常用来做图片修改操作如大小变换、图片变灰、设置图片透明或不透明等
         * */
        //生成二维码图片
        BufferedImage image = toBufferedImage(matrix);
        //ImageIO：图片处理类 将二维码图片添加到服务器磁盘
        ImageIO.write(image, format, file);
    }

    /**
     * 生成二维码内容<br>
     *
     * @param matrix  二维码参数对象
     * @return
     */
    private  static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //
        //BufferedImage.TYPE_INT_ARGB,表示一个图像，该图像具有整数像素的 8 位 RGB 颜色
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //图片二进制化
                image.setRGB(x, y, matrix.get(x, y) == true ? BLACK : WHITE);
            }
        }
        return image;
    }


}

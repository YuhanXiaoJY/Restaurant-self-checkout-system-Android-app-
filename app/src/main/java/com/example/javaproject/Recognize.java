package com.example.javaproject;


import android.speech.RecognizerResultsIntent;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.net.SocketOption;
import java.util.*;


public class Recognize
{

    public static int pos[] = {0, 5, 2, 3, 1, 6, 4};
    public static int prices[] = {8, 10, 12, 16, 18, 20, 24};
    public static int arg[][];
    public static Mat src_img;

    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static Mat findcircle(Mat img, int[] args)
    {
        Mat img_tmp = new Mat(img.rows(), img.cols(), CvType.CV_8UC3);
        Mat img_HSV = new Mat(img.rows(), img.cols(), CvType.CV_8UC3);
        List<Mat> img_split = new ArrayList<Mat>();
        Mat img_arr[] = new Mat[3];
        List<Mat> img_split2 = new ArrayList<Mat>();

        //rgb2hsv
        Imgproc.cvtColor(img, img_tmp, Imgproc.COLOR_RGB2HSV);
        int cnt = 0;
        Core.split(img_tmp, img_split);
        for (Iterator<Mat> it = img_split.iterator(); it.hasNext();)
        {
            img_arr[cnt] = (it.next()).clone();
            cnt += 1;
        }
        //equalize
        Imgproc.equalizeHist(img_arr[2], img_arr[2]);
        //merge
        img_split2.add(img_arr[0]);
        img_split2.add(img_arr[1]);
        img_split2.add(img_arr[2]);
        Core.merge(img_split2, img_HSV);

        //if (cnt == 3) {
        //	Imgproc.cvtColor(img_HSV, img_HSV, Imgproc.COLOR_HSV2RGB);
        //	return img_HSV;
        //}

        img_tmp = img_HSV.clone();

        //color
        Core.inRange(img_tmp, new Scalar(args[0], args[2], args[4]), new Scalar(args[1], args[3], args[5]), img_tmp);

        //dilate
        Mat element2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1));
        Imgproc.erode(img_tmp, img_tmp, element2, new Point(-1, -1), 1);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(img_tmp, img_tmp, element, new Point(-1, -1), 1);

        if (cnt == 3)
        {
            Imgcodecs.imwrite("data\\check_result.jpg", img_tmp);
        }
        else
        {
            System.out.println(cnt);
        }

        //circle
        Mat circles = new Mat();
        Imgproc.HoughCircles(img_tmp, circles, Imgproc.HOUGH_GRADIENT, 1.5, 100, 400, args[6], 30, 100);

        int Num = circles.cols();//System.out.println(circles.cols());
        Imgproc.cvtColor(img_HSV, img_HSV, Imgproc.COLOR_HSV2RGB);
        for (int i = 0; i < circles.cols(); i++)
        {
            double[] vCircle = circles.get(0, i);

            Point center = new Point(vCircle[0], vCircle[1]);
            int radius = (int)Math.round(vCircle[2]);
            if (radius == 0) Num -= 1;

            //draw center
            Imgproc.circle(img_HSV, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
            //draw circle
            Imgproc.circle(img_HSV, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
        }
        System.out.println(Num);
        return img_HSV;
    }

    public static int findCircleNum(Mat img, int[] args)
    {
        Mat img_tmp = new Mat(img.rows(), img.cols(), CvType.CV_8UC3);
        Mat img_HSV = new Mat(img.rows(), img.cols(), CvType.CV_8UC3);
        List<Mat> img_split = new ArrayList<Mat>();
        Mat img_arr[] = new Mat[3];
        List<Mat> img_split2 = new ArrayList<Mat>();

        //rgb2hsv
        Imgproc.cvtColor(img, img_tmp, Imgproc.COLOR_RGB2HSV);
        int cnt = 0;
        Core.split(img_tmp, img_split);
        for (Iterator<Mat> it = img_split.iterator(); it.hasNext();)
        {
            img_arr[cnt] = (it.next()).clone();
            cnt += 1;
        }
        //equalize
        Imgproc.equalizeHist(img_arr[2], img_arr[2]);
        //merge
        img_split2.add(img_arr[0]);
        img_split2.add(img_arr[1]);
        img_split2.add(img_arr[2]);
        Core.merge(img_split2, img_HSV);

        img_tmp = img_HSV.clone();

        //color
        Core.inRange(img_tmp, new Scalar(args[0], args[2], args[4]), new Scalar(args[1], args[3], args[5]), img_tmp);

        //dilate
        Mat element2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 1));
        Imgproc.erode(img_tmp, img_tmp, element2, new Point(-1, -1), 1);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.dilate(img_tmp, img_tmp, element, new Point(-1, -1), 1);

        //circle
        Mat circles = new Mat();
        Imgproc.HoughCircles(img_tmp, circles, Imgproc.HOUGH_GRADIENT, 1.5, 100, 400, args[6], 30, 100);

        int Num = circles.cols();//System.out.println(circles.cols());
        Imgproc.cvtColor(img_HSV, img_HSV, Imgproc.COLOR_HSV2RGB);
        for (int i = 0; i < circles.cols(); i++)
        {
            double[] vCircle = circles.get(0, i);

            Point center = new Point(vCircle[0], vCircle[1]);
            int radius = (int)Math.round(vCircle[2]);
            if (radius == 0) Num -= 1;

            //draw center
            Imgproc.circle(img_HSV, center, 3, new Scalar(0, 255, 0), -1, 8, 0);
            //draw circle
            Imgproc.circle(img_HSV, center, radius, new Scalar(0, 0, 255), 3, 8, 0);
        }
        return Num;
    }

    public static void init(String dataPath)
    {
        src_img = Imgcodecs.imread(dataPath);
        arg = new int[9][9];
        //arg:iLowH, iHighH, iLowS, iHighS, iLowV, iHighV, Mindist, para1, para2
        //coins
        arg[0][0] = 0; arg[0][1] = 175; arg[0][2] = 0; arg[0][3] = 255;
        arg[0][4] = 175; arg[0][5] = 255; arg[0][6] = 10; arg[0][7] = 200; arg[0][8] = 43;
        //blue
        arg[1][0] = 0; arg[1][1] = 27; arg[1][2] = 90; arg[1][3] = 199;
        arg[1][4] = 30; arg[1][5] = 255; arg[1][6] = 40; arg[1][7] = 200; arg[1][8] = 43;
        //red
        arg[2][0] = 110; arg[2][1] = 140; arg[2][2] = 100; arg[2][3] = 208;
        arg[2][4] = 80; arg[2][5] = 150; arg[2][6] = 40; arg[2][7] = 200; arg[2][8] = 43;
        //purple
        arg[3][0] = 160; arg[3][1] = 180; arg[3][2] = 100; arg[3][3] = 208;
        arg[3][4] = 0; arg[3][5] = 255; arg[3][6] = 40; arg[3][7] = 200; arg[3][8] = 43;
        //yellow
        arg[4][0] = 90; arg[4][1] = 99; arg[4][2] = 60; arg[4][3] = 159;
        arg[4][4] = 100; arg[4][5] = 255; arg[4][6] = 40; arg[4][7] = 200; arg[4][8] = 43;
        //green
        arg[5][0] = 50; arg[5][1] = 79; arg[5][2] = 19; arg[5][3] = 255;
        arg[5][4] = 50; arg[5][5] = 255; arg[5][6] = 45; arg[5][7] = 200; arg[5][8] = 43;
        //coffee
        arg[6][0] = 100; arg[6][1] = 115; arg[6][2] = 130; arg[6][3] = 170;
        arg[6][4] = 45; arg[6][5] = 70; arg[6][6] = 40; arg[6][7] = 200; arg[6][8] = 43;
        //orange
        arg[7][0] = 110; arg[7][1] = 144; arg[7][2] = 26; arg[7][3] = 255;
        arg[7][4] = 150; arg[7][5] = 255; arg[7][6] = 40; arg[7][7] = 200; arg[7][8] = 43;
        //Test
        arg[8][0] = 0; arg[8][1] = 50; arg[8][2] = 30; arg[8][3] = 199;
        arg[8][4] = 20; arg[8][5] = 255; arg[8][6] = 10; arg[8][7] = 200; arg[8][8] = 43;
    }

    public static int[] calc_total(String dataPath)
    {
        init(dataPath);

        int Sum = 0;
        int Cnt[] = new int[7];
        for (int i = 1; i < 8; i++)
        {
            int Num = findCircleNum(src_img, arg[i]);
            Sum += prices[i-1]*Num;
            Cnt[pos[i-1]] = Num;
        }
        return Cnt;
    }

    public static int[] RecognizeRun(String _file_path)
    {
        int Num[] = calc_total(_file_path);
        return Num;
    }

    public static void main(String[] args)
    {
        int[] plate_num = RecognizeRun("E:\\Codes\\Android\\JavaProject2\\plates\\1.png");
        for(int i = 0; i< 7; i++)
            System.out.println(plate_num[i]);
        int sum = 0;
        for(int i = 0; i < 7; i++)
            sum += plate_num[i] * prices[i];
        System.out.println(sum);

//		src_img = Imgcodecs.imread("data\\disks\\test\\2.png");
//		Mat tmp = findcircle(src_img, arg[7]);
//		Imgcodecs.imwrite("data\\result.jpg", tmp);
    }
}
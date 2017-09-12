package com.example.bsy.initialquiz_1.Item;

/**
 * Created by bsy on 2017-08-31.
 */

public class MethodUtils {

    private volatile static MethodUtils methodInstance;//volatile : 멀티스레드환경에서 변수 사용하기
    private int playCnt = 0;

    private MethodUtils() {   //생성자
    }

    public static MethodUtils getInstance() {
        if (methodInstance == null) { // 인스턴스가 있는지 확인
            synchronized (MethodUtils.class) { // 없으면 동기화 블럭으로 들어감
                if (methodInstance == null) { // 블럭으로 들어온 후에도 다시 한번 변수가 null인지 확인
                    methodInstance = new MethodUtils(); // null이면 인스턴스 생성
                }
            }
        }
        return methodInstance;
    }

    public static String getInitial(String fullStr) {

        String resultStr = "";

        for (int i = 0; i < fullStr.length(); i++) {

            char comVal = (char) (fullStr.charAt(i) - 0xAC00);

            if (comVal >= 0 && comVal <= 11172) {

                // 한글일경우
                // 초성만 입력 했을 시엔 초성은 무시해서 List에 추가합니다.

                char uniVal = (char) comVal;

                // 유니코드 표에 맞추어 초성 중성 종성을 분리합니다..

                char cho = (char) ((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100);
                char jung = (char) ((((uniVal - (uniVal % 28)) / 28) % 21) + 0x1161);
                char jong = (char) ((uniVal % 28) + 0x11a7);

                if (cho != 4519) {
                    System.out.print(cho + " ");
                    resultStr = resultStr + cho;
                }
                if (jung != 4519) {
                    //System.out.print(jung+" ");
                }
                if (jong != 4519) {
                    //System.out.print(jong+" ");
                }

            } else {
                // 한글이 아닐경우
                comVal = (char) (comVal + 0xAC00);
                resultStr = resultStr + comVal;
            }
        }
        return resultStr;
    }

    public static String getHintInitial(String fullStr) {

        String resultStr = "";

        for (int i = 1; i < fullStr.length(); i++) {

            char comVal = (char) (fullStr.charAt(i) - 0xAC00);

            if (comVal >= 0 && comVal <= 11172) {

                // 한글일경우
                // 초성만 입력 했을 시엔 초성은 무시해서 List에 추가합니다.

                char uniVal = (char) comVal;

                // 유니코드 표에 맞추어 초성 중성 종성을 분리합니다..

                char cho = (char) ((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100);
                char jung = (char) ((((uniVal - (uniVal % 28)) / 28) % 21) + 0x1161);
                char jong = (char) ((uniVal % 28) + 0x11a7);

                if (cho != 4519) {
                    System.out.print(cho + " ");
                    resultStr = resultStr + cho;
                }
                if (jung != 4519) {
                    //System.out.print(jung+" ");
                }
                if (jong != 4519) {
                    //System.out.print(jong+" ");
                }

            } else {
                // 한글이 아닐경우
                comVal = (char) (comVal + 0xAC00);
                resultStr = resultStr + comVal;
            }
        }
        return fullStr.charAt(0) + resultStr;
    }

    public int getPlayCnt() {
        return playCnt;
    }

    public void setPlayCnt(int playCnt) {
        this.playCnt = playCnt;
    }
}

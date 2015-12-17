package com.atet.tvmarket.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class KeyConfigInfo implements Cloneable,Serializable,AutoType{
    private static final long serialVersionUID = 1L;
//    按键值第31位表示是否拦截该按键
//    public static final int MODE_INTERCEPT_KEY=0x40000000;
//    按键值第1位表示是否拦截该按键
    public static final int MODE_INTERCEPT_KEY=0x1;
    public static final int KEY_SHIFT=4;
    
    @Expose
    private String pkgName;
    @Expose
    private String name;
    @Expose
    private int verCode;
    @Expose
    private String verName;
    
    //0b0010:gamepad
    //0b0100:keyboard
    @Expose
    private int mode;
    
    @Expose
    private long DLeft;
    @Expose
    private long DRight;
    @Expose
    private long DUp;
    @Expose
    private long DDown;
    @Expose
    private long X;
    @Expose
    private long Y;
    @Expose
    private long A;
    @Expose
    private long B;
    @Expose
    private long L1;
    @Expose
    private long R1;
    @Expose
    private long L2;
    @Expose
    private long R2;
    @Expose
    private int mouse;
    
    @Expose
    private String brief;

    public KeyConfigInfo() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
        KeyConfigInfo obj = null;  
        try {  
            obj = (KeyConfigInfo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVerCode() {
        return verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getDLeft() {
        return DLeft;
    }

    public void setDLeft(long dLeft) {
        DLeft = dLeft;
    }

    public long getDRight() {
        return DRight;
    }

    public void setDRight(long dRight) {
        DRight = dRight;
    }

    public long getDUp() {
        return DUp;
    }

    public void setDUp(long dUp) {
        DUp = dUp;
    }

    public long getDDown() {
        return DDown;
    }

    public void setDDown(long dDown) {
        DDown = dDown;
    }

    public long getX() {
        return X;
    }

    public void setX(long x) {
        X = x;
    }

    public long getY() {
        return Y;
    }

    public void setY(long y) {
        Y = y;
    }

    public long getA() {
        return A;
    }

    public void setA(long a) {
        A = a;
    }

    public long getB() {
        return B;
    }

    public void setB(long b) {
        B = b;
    }

    public long getL1() {
        return L1;
    }

    public void setL1(long l1) {
        L1 = l1;
    }

    public long getR1() {
        return R1;
    }

    public void setR1(long r1) {
        R1 = r1;
    }

    public long getL2() {
        return L2;
    }

    public void setL2(long l2) {
        L2 = l2;
    }

    public long getR2() {
        return R2;
    }

    public void setR2(long r2) {
        R2 = r2;
    }
    
    public static boolean isIntercept(long key){
        return (key & MODE_INTERCEPT_KEY) == MODE_INTERCEPT_KEY;
    }
    
    public int getMouse() {
        return mouse;
    }

    public void setMouse(int mouse) {
        this.mouse = mouse;
    }
    
    public boolean isMouseEnable(){
        return mouse == ConfigInfo.MOUSE_ENABLE;
    }
    
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Override
    public String toString() {
        return "KeyConfigInfo [pkgName=" + pkgName + ", name=" + name
                + ", verCode=" + verCode + ", verName=" + verName + ", mode="
                + mode + ", DLeft=" + DLeft + ", DRight=" + DRight + ", DUp="
                + DUp + ", DDown=" + DDown + ", X=" + X + ", Y=" + Y + ", A="
                + A + ", B=" + B + ", L1=" + L1 + ", R1=" + R1 + ", L2=" + L2
                + ", R2=" + R2 + ", mouse=" + mouse + ", brief=" + brief + "]";
    }
}

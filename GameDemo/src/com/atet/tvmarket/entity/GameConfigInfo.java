package com.atet.tvmarket.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class GameConfigInfo implements Cloneable,Serializable,AutoType{
    private static final long serialVersionUID = 1L;
    
    public static final int NO_ID=-100;
    private int id = NO_ID;
    @Expose
    private String pkgName;
    @Expose
    private String name;
    @Expose
    private int verCode;
    @Expose
    private String verName;
    @Expose
    private int Dx;
    @Expose
    private int Dy;
    @Expose
    private int Dr;
    @Expose
    private int SLx;
    @Expose
    private int SLy;
    @Expose
    private int SLr;
    @Expose
    private int Xx;
    @Expose
    private int Xy;
    @Expose
    private int Yx;
    @Expose
    private int Yy;
    @Expose
    private int Ax;
    @Expose
    private int Ay;
    @Expose
    private int Bx;
    @Expose
    private int By;
    @Expose
    private int Lx;
    @Expose
    private int Ly;
    @Expose
    private int Rx;
    @Expose
    private int Ry;
    @Expose
    private int L2x;
    @Expose
    private int L2y;
    @Expose
    private int R2x;
    @Expose
    private int R2y;
    @Expose
    private int DUx;
    @Expose
    private int DUy;
    @Expose
    private int DDx;
    @Expose
    private int DDy;
    @Expose
    private int DLx;
    @Expose
    private int DLy;
    @Expose
    private int DRx;
    @Expose
    private int DRy;
    @Expose
    private int mouse;


    // 坐标值前16位为横屏，后16位为竖屏
    // 第一位该方向是否存在该配置：1存在
    // 第二位该方向directionMode:0点击，1滑动
    // 第三位是否是默认配置，0默认配置，1手动配置
    @Expose
    private int mode;
    
    @Expose
    private String resolution;
    
    // 0横屏，1竖屏
    // private int orientation;
    // 0点击，1滑动
    private int directionMode;
    
    @Expose
    private int SRx;
    @Expose
    private int SRy;
    @Expose
    private int SRr;
    
    @Expose
    private String brief;
    @Expose
    private int isfloat;


    public GameConfigInfo() {
        // TODO Auto-generated constructor stub
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDx() {
        return Dx;
    }

    public void setDx(int dx) {
        Dx = dx;
    }

    public int getDy() {
        return Dy;
    }

    public void setDy(int dy) {
        Dy = dy;
    }

    public int getDr() {
        return Dr;
    }

    public void setDr(int dr) {
        Dr = dr;
    }

    public int getSLx() {
        return SLx;
    }

    public void setSLx(int sLx) {
        SLx = sLx;
    }
    
    public int getSLy() {
        return SLy;
    }

    public void setSLy(int sLy) {
        SLy = sLy;
    }

    public int getSLr() {
        return SLr;
    }

    public void setSLr(int sLr) {
        SLr = sLr;
    }

    public int getXx() {
        return Xx;
    }

    public void setXx(int xx) {
        Xx = xx;
    }

    public int getXy() {
        return Xy;
    }

    public void setXy(int xy) {
        Xy = xy;
    }

    public int getYx() {
        return Yx;
    }

    public void setYx(int yx) {
        Yx = yx;
    }

    public int getYy() {
        return Yy;
    }

    public void setYy(int yy) {
        Yy = yy;
    }

    public int getAx() {
        return Ax;
    }

    public void setAx(int ax) {
        Ax = ax;
    }

    public int getAy() {
        return Ay;
    }

    public void setAy(int ay) {
        Ay = ay;
    }

    public int getBx() {
        return Bx;
    }

    public void setBx(int bx) {
        Bx = bx;
    }

    public int getBy() {
        return By;
    }

    public void setBy(int by) {
        By = by;
    }

    public int getRx() {
        return Rx;
    }

    public void setRx(int rx) {
        Rx = rx;
    }

    public int getRy() {
        return Ry;
    }

    public void setRy(int ry) {
        Ry = ry;
    }

    public int getDirectionMode() {
        return directionMode;
    }

    public void setDirectionMode(int directionMode) {
        this.directionMode = directionMode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
    
    @Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
        GameConfigInfo obj = null;  
        try {  
            obj = (GameConfigInfo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    
    public boolean isEnable(boolean isLandscape) {
        int mode = isLandscape ? this.mode : (this.mode>>16);
        return (mode & 0x1) == 0x1;
    }
    
    public int getSRx() {
        return SRx;
    }

    public void setSRx(int sRx) {
        SRx = sRx;
    }

    public int getSRy() {
        return SRy;
    }

    public void setSRy(int sRy) {
        SRy = sRy;
    }

    public int getSRr() {
        return SRr;
    }

    public void setSRr(int sRr) {
        SRr = sRr;
    }
    
        public int getLx() {
        return Lx;
    }

    public void setLx(int lx) {
        Lx = lx;
    }

    public int getLy() {
        return Ly;
    }

    public void setLy(int ly) {
        Ly = ly;
    }

    public int getL2x() {
        return L2x;
    }

    public void setL2x(int l2x) {
        L2x = l2x;
    }

    public int getL2y() {
        return L2y;
    }

    public void setL2y(int l2y) {
        L2y = l2y;
    }

    public int getR2x() {
        return R2x;
    }

    public void setR2x(int r2x) {
        R2x = r2x;
    }

    public int getR2y() {
        return R2y;
    }

    public void setR2y(int r2y) {
        R2y = r2y;
    }

    public int getDUx() {
        return DUx;
    }

    public void setDUx(int dUx) {
        DUx = dUx;
    }

    public int getDUy() {
        return DUy;
    }

    public void setDUy(int dUy) {
        DUy = dUy;
    }

    public int getDDx() {
        return DDx;
    }

    public void setDDx(int dDx) {
        DDx = dDx;
    }

    public int getDDy() {
        return DDy;
    }

    public void setDDy(int dDy) {
        DDy = dDy;
    }

    public int getDLx() {
        return DLx;
    }

    public void setDLx(int dLx) {
        DLx = dLx;
    }

    public int getDLy() {
        return DLy;
    }

    public void setDLy(int dLy) {
        DLy = dLy;
    }

    public int getDRx() {
        return DRx;
    }

    public void setDRx(int dRx) {
        DRx = dRx;
    }

    public int getDRy() {
        return DRy;
    }

    public void setDRy(int dRy) {
        DRy = dRy;
    }
    
    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
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
    
    
    public int getIsfloat() {
        return isfloat;
    }

    public void setIsfloat(int isfloat) {
        this.isfloat = isfloat;
    }

    @Override
    public String toString() {
        return "GameConfigInfo [id=" + id + ", pkgName=" + pkgName + ", name="
                + name + ", verCode=" + verCode + ", verName=" + verName
                + ", Dx=" + Dx + ", Dy=" + Dy + ", Dr=" + Dr + ", SLx=" + SLx
                + ", SLy=" + SLy + ", SLr=" + SLr + ", Xx=" + Xx + ", Xy=" + Xy
                + ", Yx=" + Yx + ", Yy=" + Yy + ", Ax=" + Ax + ", Ay=" + Ay
                + ", Bx=" + Bx + ", By=" + By + ", Lx=" + Lx + ", Ly=" + Ly
                + ", Rx=" + Rx + ", Ry=" + Ry + ", L2x=" + L2x + ", L2y=" + L2y
                + ", R2x=" + R2x + ", R2y=" + R2y + ", DUx=" + DUx + ", DUy="
                + DUy + ", DDx=" + DDx + ", DDy=" + DDy + ", DLx=" + DLx
                + ", DLy=" + DLy + ", DRx=" + DRx + ", DRy=" + DRy + ", mouse="
                + mouse + ", mode=" + mode + ", resolution=" + resolution
                + ", directionMode=" + directionMode + ", SRx=" + SRx
                + ", SRy=" + SRy + ", SRr=" + SRr + ", brief=" + brief
                + ", isfloat=" + isfloat + "]";
    }
}
